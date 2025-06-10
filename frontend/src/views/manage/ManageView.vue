<template>
  <div class="manage">
    <div class="panel">
      <!-- 头像图片 -->
      <el-avatar :icon="UserFilled" :size="100" :src="avatarUrl" />

      <!-- 头像 -->
      <div class="avatar">
        <span>头像</span>
        <div class="update-avatar">
          <el-input v-model="avatarUrl" placeholder="输入您喜欢的头像链接" spellcheck="false">
            <template #suffix>
              <el-button :icon="Check" circle @click="wrapUpdateAvatar" />
            </template>
          </el-input>
        </div>
      </div>

      <!-- 昵称 -->
      <div class="nickname">
        <span>昵称</span>
        <div class="update-nickname">
          <el-input v-model="nickname" placeholder="设置您喜欢的昵称" spellcheck="false">
            <template #suffix>
              <el-button :icon="Check" circle @click="wrapUpdateNickname" />
            </template>
          </el-input>
        </div>
      </div>

      <!-- 账号 -->
      <div class="account">
        <span>账号</span>
        <div class="update-account">
          <el-input
            v-model="account"
            placeholder="账号由 6~20 位的数字或字母组成"
            spellcheck="false"
          >
            <template #suffix>
              <el-button :icon="Check" circle @click="wrapUpdateAccount" />
            </template>
          </el-input>
        </div>
      </div>

      <!-- 邮箱 -->
      <div class="email">
        <span>邮箱</span>
        <div class="update-email">
          <el-input v-model="email" placeholder="QQ邮箱" spellcheck="false">
            <template #suffix>
              <el-button :icon="Check" circle @click="wrapUpdateEmail" />
            </template>
          </el-input>
        </div>
      </div>

      <!-- 模型配置 -->
      <div class="config">
        <span>模型配置</span>
        <div class="update-config">
          <el-input
            v-model="config"
            type="textarea"
            :autosize="{ minRows: 12, maxRows: 22 }"
            spellcheck="false"
          />
          <el-button @click="wrapUpdateConfig">保存配置</el-button>
        </div>
      </div>

      <!-- 返回聊天页 -->
      <div class="return">
        <span>返回聊天页</span>
        <el-button @click="gotoChat">返回</el-button>
      </div>

      <!-- 注销账号 -->
      <div class="deactivate">
        <span>注销账号</span>
        <div class="deactivate-account">
          <span>警告：此操作不可逆，请谨慎操作！</span>
          <el-button @click="wrapDeactivateAccount">确认注销</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { UID } from '@/global/constant/login';
import {
  ACCOUNT_REGEX,
  EMAIL_REGEX,
  EMAIL_VERIFY_CODE_REGEX,
  THROTTLE_TIME
} from '@/global/constant/rule';
import {
  queryContactRequest,
  queryModelConfigRequest,
  queryProfileRequest
} from '@/service/api/query';
import {
  updateAccountRequest,
  updateAvatarRequest,
  updateEmailRequest1,
  updateEmailRequest2,
  updateModelConfigRequest,
  updateNicknameRequest
} from '@/service/api/update';
import { localCache } from '@/utils/cache';
import throttle from '@/utils/throttle';
import { Check, UserFilled } from '@element-plus/icons-vue';
import { ElMessageBox } from 'element-plus';
import 'element-plus/theme-chalk/el-overlay.css';
import 'element-plus/theme-chalk/el-message-box.css';
import { onUnmounted, ref, watch } from 'vue';
import {
  authRequestForDeactivateAccount,
  authRequestForUpdtAccount,
  authRequestForUpdtEmail
} from '@/service/api/authenticate';
import { deactivateAccountRequest } from '@/service/api/deactivate';
import { useRouter } from 'vue-router';
import ROUTE from '@/global/constant/route';

const router = useRouter();
const uid = localCache.getItem(UID); // 获取UID
const verifyCode1 = ref(''); // 修改账号验证码
const verifyCode2 = ref(''); // 修改邮箱验证码
const verifyCode3 = ref(''); // 绑定新邮箱验证码
const verifyCode4 = ref(''); // 注销账号验证码

// ==================== 弹出框 ====================
// 身份验证框
const openAuthenticationBox = (mode: number) => {
  ElMessageBox.prompt('我们已向您的邮箱发送了验证码，请注意查收！', '身份验证', {
    inputPlaceholder: '请输入验证码，完成身份验证',
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: EMAIL_VERIFY_CODE_REGEX,
    inputErrorMessage: '验证码应为6位数字'
  })
    .then(({ value }) => {
      switch (mode) {
        case 1: // 修改账号-身份验证
          verifyCode1.value = value;
          break;
        case 2: // 修改邮箱-身份验证
          verifyCode2.value = value;
          break;
        case 4: // 注销账号-身份验证
          verifyCode4.value = value;
          break;
      }
    })
    .catch(() => {
      switch (mode) {
        case 1: // 修改账号-身份验证
          account.value = originalAccount;
          break;
        case 2: // 修改邮箱-身份验证
          email.value = originalEmail;
          break;
      }
    });
};

// 绑定新邮箱框
const openBindNewEmailBox = () => {
  ElMessageBox.prompt('我们已向新邮箱发送了验证码，请注意查收！', '绑定新邮箱', {
    inputPlaceholder: '输入验证码，绑定新邮箱',
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: EMAIL_VERIFY_CODE_REGEX,
    inputErrorMessage: '验证码应为6位数字'
  })
    .then(({ value }) => {
      verifyCode3.value = value;
    })
    .catch(() => {
      email.value = originalEmail;
    });
};

// ==================== 头像和昵称 ====================
const avatarUrl = ref('');
const nickname = ref('');
let originalAvatarUrl = ''; // 修改前头像URL
let originalNickname = ''; // 修改前昵称

// 获取昵称和头像
function getNicknameAndAvatar() {
  queryProfileRequest(uid)
    .then((res) => {
      if (res.data.code === 0) {
        avatarUrl.value = res.data.data.avatar === null ? '' : res.data.data.avatar;
        originalAvatarUrl = res.data.data.avatar === null ? '' : res.data.data.avatar;
        nickname.value = res.data.data.nickname;
        originalNickname = res.data.data.nickname;
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
    });
}
getNicknameAndAvatar();

// 更新头像
function updateAvatar() {
  if (avatarUrl.value === originalAvatarUrl) return;
  if (
    avatarUrl.value === '' ||
    !avatarUrl.value.startsWith('http') ||
    !avatarUrl.value.startsWith('https')
  ) {
    ElMessage({ message: '修改失败，内容不符合要求', type: 'error', grouping: true });
    avatarUrl.value = originalAvatarUrl;
    return;
  }

  updateAvatarRequest(uid, avatarUrl.value)
    .then((res) => {
      if (res.data.code === 0) {
        ElMessage({ message: '修改成功', type: 'success', grouping: true });
        originalAvatarUrl = avatarUrl.value;
      } else {
        ElMessage({ message: '修改失败，内容不符合要求', type: 'error', grouping: true });
        avatarUrl.value = originalAvatarUrl;
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
      avatarUrl.value = originalAvatarUrl;
    });
}
const wrapUpdateAvatar = throttle(updateAvatar, THROTTLE_TIME);

// 更新昵称
function updateNickname() {
  if (nickname.value === originalNickname) return;
  if (nickname.value === '') {
    ElMessage({ message: '修改失败，内容不符合要求', type: 'error', grouping: true });
    nickname.value = originalNickname;
    return;
  }

  updateNicknameRequest(uid, nickname.value)
    .then((res) => {
      if (res.data.code === 0) {
        ElMessage({ message: '修改成功', type: 'success', grouping: true });
        originalNickname = nickname.value;
      } else {
        ElMessage({ message: '修改失败，内容不符合要求', type: 'error', grouping: true });
        nickname.value = originalNickname;
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
      nickname.value = originalNickname;
    });
}
const wrapUpdateNickname = throttle(updateNickname, THROTTLE_TIME);

// ==================== 账号和邮箱 ====================
const account = ref('');
const email = ref('');
let originalAccount = ''; // 修改前的账号
let originalEmail = ''; // 修改前的邮箱

// 获取账号和邮箱
function getAccountAndEmail() {
  queryContactRequest(uid)
    .then((res) => {
      if (res.data.code === 0) {
        account.value = res.data.data.account;
        originalAccount = res.data.data.account;
        email.value = res.data.data.email;
        originalEmail = res.data.data.email;
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
    });
}
getAccountAndEmail();

// 更新账号
function updateAccount() {
  if (account.value === originalAccount) return;
  if (!ACCOUNT_REGEX.test(account.value)) {
    ElMessage({ message: '修改失败，内容不符合要求', type: 'error', grouping: true });
    account.value = originalAccount;
    return;
  }

  authRequestForUpdtAccount(uid)
    .then((res) => {
      if (res.data.code === 0) {
        // 打开身份验证框，模式为修改账号
        openAuthenticationBox(1);
      } else {
        ElMessage({ message: res.data.msg, type: 'warning', grouping: true });
        account.value = originalAccount;
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
      account.value = originalAccount;
    });
}
const stopWatchingVerifyCode1 = watch(verifyCode1, (newValue) => {
  if (EMAIL_VERIFY_CODE_REGEX.test(newValue)) {
    updateAccountRequest(uid, account.value, newValue)
      .then((res) => {
        if (res.data.code === 0) {
          ElMessage({ message: '修改成功', type: 'success', grouping: true });
          originalAccount = account.value;
        } else {
          ElMessage({ message: res.data.msg, type: 'warning', grouping: true });
          account.value = originalAccount;
        }
      })
      .catch(() => {
        ElMessage({ message: '网络异常', type: 'error', grouping: true });
        account.value = originalAccount;
      });
  }
});
onUnmounted(() => {
  stopWatchingVerifyCode1();
});
const wrapUpdateAccount = throttle(updateAccount, THROTTLE_TIME);

// 更新邮箱
function updateEmail() {
  if (email.value === originalEmail) return;
  if (!EMAIL_REGEX.test(email.value)) {
    ElMessage({ message: '修改失败，内容不符合要求', type: 'error', grouping: true });
    email.value = originalEmail;
    return;
  }

  authRequestForUpdtEmail(uid)
    .then((res) => {
      if (res.data.code === 0) {
        // 打开身份验证框
        openAuthenticationBox(2);
      } else {
        ElMessage({ message: res.data.msg, type: 'warning', grouping: true });
        email.value = originalEmail;
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
      email.value = originalEmail;
    });
}
const stopWatchingVerifyCode2 = watch(verifyCode2, (newValue) => {
  if (EMAIL_VERIFY_CODE_REGEX.test(newValue)) {
    updateEmailRequest1(uid, email.value, newValue)
      .then((res) => {
        if (res.data.code === 0) {
          // 弹出"绑定新邮箱"框
          openBindNewEmailBox();
        } else {
          ElMessage({ message: res.data.msg, type: 'warning', grouping: true });
          email.value = originalEmail;
        }
      })
      .catch(() => {
        ElMessage({ message: '网络异常', type: 'error', grouping: true });
        email.value = originalEmail;
      });
  }
});
const stopWatchingVerifyCode3 = watch(verifyCode3, (newValue) => {
  if (EMAIL_VERIFY_CODE_REGEX.test(newValue)) {
    updateEmailRequest2(uid, email.value, newValue)
      .then((res) => {
        if (res.data.code === 0) {
          ElMessage({ message: '修改成功', type: 'success', grouping: true });
          originalEmail = email.value;
        } else {
          ElMessage({ message: res.data.msg, type: 'warning', grouping: true });
          email.value = originalEmail;
        }
      })
      .catch(() => {
        ElMessage({ message: '网络异常', type: 'error', grouping: true });
        email.value = originalEmail;
      });
  }
});
onUnmounted(() => {
  stopWatchingVerifyCode2();
  stopWatchingVerifyCode3();
});
const wrapUpdateEmail = throttle(updateEmail, THROTTLE_TIME);

// ==================== 模型配置 ====================
const config = ref('');
let originalConfig = ''; // 修改前的模型配置

// 获取模型配置
function getConfig() {
  queryModelConfigRequest(uid)
    .then((res) => {
      if (res.data.code === 0) {
        config.value = JSON.stringify(res.data.data, null, 2);
        originalConfig = config.value;
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
    });
}
getConfig();

// 更新模型配置
function updateConfig() {
  if (config.value === originalConfig) return;
  let parsed;

  try {
    if (config.value === '') throw new Error('JSON字符串为空');
    parsed = JSON.parse(config.value);
  } catch (error) {
    console.info(error);
    ElMessage({ message: '修改失败，内容不符合要求', type: 'error', grouping: true });
    config.value = originalConfig;
    return;
  }

  updateModelConfigRequest(uid, JSON.stringify(parsed.config))
    .then((res) => {
      if (res.data.code === 0) {
        ElMessage({ message: '修改成功', type: 'success', grouping: true });
        originalConfig = config.value;
      } else {
        ElMessage({ message: '修改失败，内容不符合要求', type: 'error', grouping: true });
        config.value = originalConfig;
      }
    })
    .catch(() => {
      ElMessage({ message: '修改失败，内容不符合要求', type: 'error', grouping: true });
      config.value = originalConfig;
    });
}
const wrapUpdateConfig = throttle(updateConfig, THROTTLE_TIME);

// ==================== 返回聊天 ====================
function gotoChat() {
  router.push(ROUTE.PATH.CHAT);
}

// ==================== 注销账号 ====================
// 注销账号
function deactivateAccount() {
  authRequestForDeactivateAccount(uid)
    .then((res) => {
      if (res.data.code === 0) {
        // 打开身份验证框，模式为注销账号
        openAuthenticationBox(4);
      } else {
        ElMessage({ message: res.data.msg, type: 'warning', grouping: true });
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
    });
}
const stopWatchingVerifyCode4 = watch(verifyCode4, (newValue) => {
  if (EMAIL_VERIFY_CODE_REGEX.test(newValue)) {
    deactivateAccountRequest(uid, newValue)
      .then((res) => {
        if (res.data.code === 0) {
          ElMessage({ message: '账号已注销', type: 'success', grouping: true });
          router.push(ROUTE.PATH.INDEX); // 跳转到首页
        } else {
          ElMessage({ message: res.data.msg, type: 'warning', grouping: true });
        }
      })
      .catch(() => {
        ElMessage({ message: '网络异常', type: 'error', grouping: true });
      });
  }
});
onUnmounted(() => {
  stopWatchingVerifyCode4();
});
const wrapDeactivateAccount = throttle(deactivateAccount, THROTTLE_TIME);
</script>

<style scoped lang="scss">
// 布局样式
.manage {
  width: 100vw;
  height: 100vh;
  position: relative;

  display: flex;
  justify-content: center;
  align-items: center;
  overflow-x: hidden;
}

// 面板样式
.panel {
  width: min(100%, 600px);
  position: absolute;
  top: 5%;

  display: flex;
  flex-direction: column;
  align-items: center;
}

// 修改ElementPlus元素样式
.el-avatar {
  // 修改头像里面的小人图标
  font-size: 42px;
  color: rgb(153, 174, 204);
  background-color: rgb(225, 240, 255);
}
:deep(.el-input__wrapper) {
  // 修改输入框的官方样式
  height: 42px;
  border: 1px solid transparent;
  border-radius: 14px;
  background-color: rgb(245, 245, 245);
  box-shadow: 0 0 1px rgb(245, 245, 245);

  &:focus-within {
    border: 1px solid rgb(64, 158, 255);
  }
}
:deep(.is-circle) {
  // 修改按钮的官方样式
  width: 24px;
  height: 24px;
  background-color: rgb(251, 251, 251);
}
:deep(.el-textarea__inner) {
  // 修改textarea的官方样式
  border-radius: 10px;
  resize: none; // 禁止调整输入框大小
}

// 抽出公共样式
.avatar,
.nickname,
.account,
.email,
.config,
.return,
.deactivate {
  width: 76%;
  font-size: 16px;
  font-weight: 500;

  margin-bottom: 20px; // 与下一个功能条的隔距
}

.avatar {
  margin-top: 20px; // 与头像图片的间距

  .update-avatar {
    margin-top: 10px; // 输入框与上方介绍文字的距离
  }
}

.nickname {
  .update-nickname {
    margin-top: 10px;
  }
}

.account {
  .update-account {
    margin-top: 10px;
  }
}

.email {
  .update-email {
    margin-top: 10px;
  }
}

.config {
  .update-config {
    margin-top: 14px;
    display: flex;
    flex-direction: column;
    align-items: center;

    &:deep(.el-button) {
      width: 99%;
      margin-top: 16px;
      background-color: rgb(248, 248, 248);

      &:hover {
        color: rgb(64, 158, 255);
        border: 1px solid rgb(64, 158, 255);
      }
    }
  }
}

.return {
  margin-top: 20px;
  display: flex;
  flex-direction: column;

  &:deep(.el-button) {
    width: 99%;
    margin-top: 16px;
    background-color: rgb(248, 248, 248);

    &:hover {
      color: rgb(64, 158, 255);
      border: 1px solid rgb(64, 158, 255);
    }
  }
}

.deactivate {
  margin-top: 20px;

  .deactivate-account {
    margin-top: 16px;
    display: flex;
    flex-direction: column;

    span {
      font-weight: 600;
      font-size: 12px;
      color: rgb(180, 20, 20);
      margin-bottom: 14px;
    }

    &:deep(.el-button) {
      width: 99%;
      background-color: rgb(248, 248, 248);

      &:hover {
        border: 1px solid rgb(180, 20, 20);
        color: rgb(180, 20, 20);
        font-size: 700;
      }
    }
  }
}
</style>
