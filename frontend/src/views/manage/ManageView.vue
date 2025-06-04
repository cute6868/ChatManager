<template>
  <div class="manage">
    <div class="panel">
      <!-- 头像图片 -->
      <el-avatar :icon="UserFilled" :size="100" :src="avatarUrl" />

      <!-- 头像 -->
      <div class="avatar">
        <span>头像</span>
        <div class="update-avatar">
          <el-input v-model="avatarUrl" placeholder="输入您喜欢的头像链接">
            <template #suffix>
              <el-button :icon="Check" circle />
            </template>
          </el-input>
        </div>
      </div>

      <!-- 昵称 -->
      <div class="nickname">
        <span>昵称</span>
        <div class="update-nickname">
          <el-input v-model="nickname">
            <template #suffix>
              <el-button :icon="Check" circle />
            </template>
          </el-input>
        </div>
      </div>

      <!-- 账号 -->
      <div class="account">
        <span>账号</span>
        <div class="update-account">
          <el-input v-model="account">
            <template #suffix>
              <el-button :icon="Check" circle />
            </template>
          </el-input>
        </div>
      </div>

      <!-- 邮箱 -->
      <div class="email">
        <span>邮箱</span>
        <div class="update-email">
          <el-input v-model="email">
            <template #suffix>
              <el-button :icon="Check" circle />
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
          <el-button>保存配置</el-button>
        </div>
      </div>

      <!-- 注销账号 -->
      <el-button>注销账号</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { UID } from '@/global/constant/login';
import {
  queryContactRequest,
  queryModelConfigRequest,
  queryProfileRequest
} from '@/service/api/query';
import { localCache } from '@/utils/cache';
import { Check, UserFilled } from '@element-plus/icons-vue';
import { ref } from 'vue';

const uid = localCache.getItem(UID); // 获取UID

// ==================== 头像和昵称 ====================
const avatarUrl = ref('');
const nickname = ref('');
// 获取昵称和头像
function getNicknameAndAvatar() {
  queryProfileRequest(uid)
    .then((res) => {
      if (res.data.code === 0) {
        avatarUrl.value = res.data.data.avatar === null ? '' : res.data.data.avatar;
        nickname.value = res.data.data.nickname;
      } else {
        ElMessage({ message: res.data.msg, type: 'error', grouping: true });
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
    });
}
getNicknameAndAvatar();

// ==================== 账号和邮箱 ====================
const account = ref('');
const email = ref('');

// 获取账号和邮箱
function getAccountAndEmail() {
  queryContactRequest(uid)
    .then((res) => {
      if (res.data.code === 0) {
        account.value = res.data.data.account;
        email.value = res.data.data.email;
      } else {
        ElMessage({ message: res.data.msg, type: 'error', grouping: true });
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
    });
}

getAccountAndEmail();

// ==================== 模型配置 ====================
const config = ref('');

// 获取模型配置
function getConfig() {
  queryModelConfigRequest(uid)
    .then((res) => {
      if (res.data.code === 0) {
        config.value = JSON.stringify(res.data.data, null, 2);
      } else {
        ElMessage({ message: res.data.msg, type: 'error', grouping: true });
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
    });
}

getConfig();

// ==================== 注销账号 ====================
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
.config {
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
    }
  }
}
</style>
