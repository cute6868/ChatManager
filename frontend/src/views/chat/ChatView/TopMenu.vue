<template>
  <div class="top-menu">
    <div class="left-content">
      <!-- 历史记录 -->
      <div class="record">
        <!-- 历史记录图标 -->
        <el-icon id="record-icon" size="18" @click="toggleCard"><Notebook /></el-icon>

        <!-- 历史记录卡片 -->
        <el-card id="record-card">
          <h3>历史记录</h3>
          <p v-for="item in recordList" :key="item.sequence_num">
            <el-icon size="12" class="chat-icon"><ChatDotRound /></el-icon>
            <span>{{ item.question }}</span>
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
import { queryProfileRequest, queryRecordRequest } from '@/service/api/query';
import { onMounted, ref } from 'vue';
import { UID } from '@/global/constant/login';
import { UserFilled } from '@element-plus/icons-vue';
import { localCache } from '@/utils/cache';
import ROUTE from '@/global/constant/route';
import { useRouter } from 'vue-router';
import removeLoginData from '@/utils/remove';
import logoutRequest from '@/service/api/logout';

interface RecordItem {
  question: string;
  time: string;
  sequence_num: number;
}

const router = useRouter();
const nickname = ref('');
const imgUrl = ref('');
const recordList = ref<RecordItem[]>([]);
const uid = localCache.getItem(UID);

// 加载昵称和头像
function loadUserData() {
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
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
    });
}

onMounted(() => {
  loadUserData(); // 当组件挂载完成后，加载用户数据
});

// 头像菜单的函数
// 1.进入设置
function setting() {
  router.push(ROUTE.PATH.MANAGE);
}

// 2.退出登录
function logout() {
  logoutRequest(uid)
    .then(() => {
      removeLoginData(); // 直接清除本地登录数据
      router.push(ROUTE.PATH.INDEX);
    })
    .catch(() => {
      removeLoginData();
      router.push(ROUTE.PATH.INDEX);
    });
}

// 卡片显示状态
let isCardOpen = false;

function toggleCard() {
  // 获取卡片元素
  const card = document.querySelector('#record-card') as HTMLElement | null;
  if (!card) return;

  // 修改显示状态
  isCardOpen = !isCardOpen;

  // 根据显示状态进行相应操作
  if (isCardOpen) {
    // 获取数据
    queryRecordRequest(uid)
      .then((res) => {
        const code = res.data.code;
        if (code === 0) {
          recordList.value = res.data.data;
        }
      })
      .catch(() => {
        ElMessage({ message: '网络异常', type: 'error', grouping: true });
      });

    card.style.left = '-6px'; // 显示卡片

    // 延迟绑定外部点击事件
    // 如果直接在 toggleSidebar() 中同步绑定事件，当前的点击事件会继续冒泡到 document，卡片刚打开就又被关闭
    setTimeout(() => {
      document.addEventListener('click', handleOutsideClick);
    }, 0);
  } else {
    card.style.left = '-320px'; // 隐藏卡片
    document.removeEventListener('click', handleOutsideClick);
  }
}

// 处理外部点击事件
function handleOutsideClick(e: MouseEvent) {
  // 获取卡片和图标元素
  const card = document.querySelector('#record-card') as HTMLElement | null;
  const icon = document.getElementById('record-icon') as HTMLElement | null;
  if (!card || !icon) return;

  // 将 e.target 断言为 Node 类型
  const target = e.target as Node | null;
  if (!target) return;

  if (!card.contains(target) && !icon.contains(target)) {
    // 说明点击发生在卡片和图标之外的区域
    isCardOpen = false;
    card.style.left = '-320px'; // 隐藏卡片
    document.removeEventListener('click', handleOutsideClick);
  } else {
    // 说明点击发生在卡片和图标内的区域
    // 检查点击的元素或其祖先是否是p标签（图标里面没有p标签，只有卡片有）
    let currentElement = target as HTMLElement;
    while (currentElement) {
      // 1.先判断点击元素是否是p标签
      if (currentElement.tagName === 'P') {
        // 1.1 获取p标签内的span元素
        const spanElement = currentElement.querySelector('span');
        if (spanElement) {
          // 1.2 获取span元素的文本内容
          const text = spanElement.textContent || '';

          // 复制文本到剪贴板
          navigator.clipboard
            .writeText(text)
            .then(() => {
              ElMessage({ message: '已复制到剪贴板', type: 'success', grouping: true });
            })
            .catch((error) => {
              console.info('复制失败:', error);
            });
        }

        break; // 找到p标签后停止遍历
      }

      // 2.如果不是，则向上遍历父元素
      if (currentElement.parentElement !== null) {
        currentElement = currentElement.parentElement;
      } else {
        break; // 父元素为null时停止遍历
      }
    }
  }
}
</script>

<style scoped lang="scss">
.top-menu {
  position: fixed;
  top: 0px;

  width: 100%;
  height: 56px;
  z-index: 100;
  background-color: #fff;

  display: flex;
  justify-content: space-between;
  align-items: center;

  div {
    height: 100%;
  }

  .left-content {
    flex: 3;
    display: flex;
    align-items: center;

    // 历史记录
    .record {
      margin-left: 20px; // 历史记录距离左边的距离
      position: relative;
      border-radius: 50%;
      height: 24px;

      &:hover {
        cursor: pointer;
        background-color: rgb(240, 240, 240);
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
        transition: all 0.2s;
      }

      display: flex;
      justify-content: center;
      align-items: center;
    }

    // 历史记录卡片
    #record-card {
      position: absolute;
      top: 36px;
      left: -320px; // 当left: -6px时，才能显示卡片
      z-index: 1000;

      width: 280px;
      height: 80vh;
      background-color: rgb(248, 248, 248);
      overflow-y: auto;
      border-radius: 10px;

      // 滚动条宽度
      &::-webkit-scrollbar {
        width: 4px;
      }

      // 滚动条轨道
      &::-webkit-scrollbar-track {
        background: rgb(243, 244, 246);
        border-radius: 16px;
      }

      // 滚动条滑块
      &::-webkit-scrollbar-thumb {
        background: rgb(220, 220, 220);
        border-radius: 10px;
      }
      &::-webkit-scrollbar-thumb:hover {
        background: rgb(180, 180, 180);
      }

      // 标题
      h3 {
        font-size: 22px;
        padding: 5px 0 16px;
        border-bottom: 2px solid rgba(31, 41, 59, 0.7);
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

        margin: 10px 0; // 每行文本的间隔距离
        border: 2px solid rgba(0, 0, 0, 0);
        border-radius: 4px;
        padding: 5px 8px;

        // 字体样式
        font-family: Arial, Helvetica, sans-serif;
        font-size: 15px; // 文本字体

        .chat-icon {
          margin-right: 5px; // 文本和左边图标的距离

          // 图片对齐文字
          display: inline-flex;
          vertical-align: middle;
          margin-bottom: 3px;
        }

        &:hover {
          border: 2px solid rgb(235, 235, 235);
          background-color: rgb(235, 235, 235);
          transition: all 0.3s;
        }

        &:active {
          border: 2px solid rgb(245, 245, 245);
          background-color: rgb(245, 245, 245);
        }
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
