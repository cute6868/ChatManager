<template>
  <div class="top-menu">
    <div class="left-content">
      <!-- 历史记录 -->
      <div class="record">
        <!-- 图标 -->
        <el-icon size="24"><Clock /></el-icon>

        <!-- 显示历史记录卡片 -->
        <el-card id="record-card">
          <h3>
            历史记录
            <el-icon size="18"><Clock /></el-icon>
          </h3>
          <p v-for="i in 4" :key="i">
            <el-icon size="16" class="chat-icon"><ChatDotRound /></el-icon>
            <span>{{ '今天的天气怎么样呢？我明天要出发，你能陪我一起吗？' }}</span>
          </p>
        </el-card>
      </div>
    </div>

    <div class="right-content">
      <!-- 昵称 -->
      <div class="nickname">{{ nickname }}</div>

      <!-- 头像 -->
      <div class="avatar">
        <el-dropdown class="avatar-dropdown" trigger="click">
          <el-avatar :icon="UserFilled" :size="30" :src="imgUrl" />
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="setting">进入设置 </el-dropdown-item>
              <el-dropdown-item divided @click="logout"> 退出登录 </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { queryProfileRequest } from '@/service/api/query';
import { onMounted, ref } from 'vue';
import { LOGIN_TOKEN, ROLE, UID } from '@/global/constant/login';
import { UserFilled } from '@element-plus/icons-vue';
import { localCache } from '@/utils/cache';
import ROUTE from '@/global/constant/route';
import { useRouter } from 'vue-router';

const router = useRouter();

// 默认昵称和头像
const nickname = ref('');
const imgUrl = ref('');

// 加载昵称和头像
function loadUserData() {
  // 获取UID
  const uid = localCache.getItem(UID);
  if (uid === null && uid === '') router.push(ROUTE.PATH.LOGIN);

  // 发送请求，获取昵称和头像
  queryProfileRequest(uid)
    .then((res) => {
      const code = res.data.code; // 业务状态码

      if (code === 0) {
        nickname.value = res.data.data.nickname;
        imgUrl.value = res.data.data.avatar === null ? '' : res.data.data.avatar;
      } else {
        ElMessage({ message: res.data.msg, type: 'error' });
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error' });
    });
}

onMounted(() => {
  // 组件挂载后，执行加载数据
  loadUserData();
});

// 头像菜单的函数
// 1.进入设置
function setting() {
  router.push(ROUTE.PATH.MANAGE);
}

// 2.退出登录
function logout() {
  localCache.removeItem(UID);
  localCache.removeItem(LOGIN_TOKEN);
  localCache.removeItem(ROLE);
  router.push(ROUTE.PATH.INDEX);
}

// 历史记录
// 点击历史记录图标时显示记录卡片left: -6px；点击卡片不做操作；点击其他地方，隐藏卡片left: -320px
document.querySelector('.record')?.addEventListener('click', (e) => {
  e.stopPropagation(); // 阻止事件冒泡
  const recordCard = document.querySelector('#record-card');
  if (recordCard) {
    if (recordCard.style.left === '-320px') {
      recordCard.style.left = '-6px';
    } else {
      recordCard.style.left = '-320px';
    }
  }
});
</script>

<style scoped lang="scss">
.top-menu {
  height: 100%;

  display: flex;
  justify-content: space-between;
  align-items: center;

  div {
    height: 100%;
  }

  .left-content {
    flex: 3;
    display: flex;

    // 历史记录
    .record {
      margin-left: 20px; // 历史记录距离左边的距离
      position: relative;

      &:hover {
        cursor: pointer;
      }

      display: flex;
      justify-content: center;
      align-items: center;
    }

    // 历史记录卡片
    #record-card {
      position: absolute;
      top: 50px;
      left: -320px; // 当left: -6px时，才能显示卡片

      width: 280px;
      height: 80vh;
      background-color: rgb(243, 244, 246);
      overflow-y: auto;

      // 标题
      h3 {
        font-size: 22px;
        padding: 5px 0 16px;
        border-bottom: 1px solid rgb(31, 41, 59);
        margin-bottom: 10px;

        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      // 历史文本
      p {
        overflow: hidden; // 溢出的部分隐藏
        text-overflow: ellipsis; // 显示省略号
        white-space: nowrap; // 不换行

        margin-top: 10px; // 每行文本的间隔距离
        border: 2px solid rgba(0, 0, 0, 0);
        border-radius: 10px;
        padding: 5px 8px 0;

        // 字体样式
        font-family: Arial, Helvetica, sans-serif;
        font-size: 15px; // 文本字体

        .chat-icon {
          margin-right: 5px; // 文本和左边图标的距离

          // 图片对齐文字
          display: inline-flex;
          vertical-align: middle;
          margin-bottom: 5px;
        }
      }

      p:hover {
        border: 2px solid rgb(214, 234, 255);
        background-color: rgb(214, 234, 255);
        transition: all 0.2s;
      }
    }
  }

  .right-content {
    flex: 2;
    display: flex;
    justify-content: flex-end;

    // 昵称
    .nickname {
      margin-right: 18px; // 昵称距离右边的距离

      font-size: 14px;
      letter-spacing: 1px;
      font-family: 'Times New Roman', Times, serif;

      display: flex;
      justify-content: center;
      align-items: center;
    }

    // 头像
    .avatar {
      margin-right: 20px; // 头像距离右边的距离
      :hover {
        cursor: pointer;
      }

      display: flex;
      justify-content: center;
      align-items: center;

      .avatar-dropdown {
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
  }
}
</style>
