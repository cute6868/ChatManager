<template>
  <div class="navigation-bar">
    <el-menu :ellipsis="isEllipsis" mode="horizontal">
      <!-- 左侧logo -->
      <el-menu-item index="0">
        <a :href="ROUTE.PATH.INDEX">
          <img src="/src/assets/img/index/logo.png" alt="ChatManager logo" style="width: 180px" />
        </a>
      </el-menu-item>

      <!-- 右侧内容 -->
      <el-menu-item index="1">下载App</el-menu-item>
      <el-menu-item index="2">项目链接</el-menu-item>
      <el-menu-item index="3">赞助作者</el-menu-item>
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import ROUTE from '@/global/constant/route';
import { ref, onMounted, onUnmounted } from 'vue';

const isEllipsis = ref(false);

// 导航栏右侧内容自动伸缩功能
function handleResize() {
  // 宽度阈值576px
  if (window.innerWidth <= 576) isEllipsis.value = true;
  else isEllipsis.value = false;
}

onMounted(() => {
  window.addEventListener('resize', handleResize); // 设置事件监听器
  handleResize(); // 初始化时设置一次
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize); // 移除事件监听器
});
</script>

<style scoped lang="scss">
.navigation-bar {
  .el-menu--horizontal {
    // 导航栏的高度
    --el-menu-horizontal-height: clamp(58px, 10vh, 76px);
  }

  .el-menu--horizontal > .el-menu-item {
    // 去除底部的边框线条
    border-bottom: none;

    // 每个item里面的文字间距
    letter-spacing: 2px;
  }

  .el-menu--horizontal > .el-menu-item:nth-child(1) {
    margin-left: 1vw; // 第一个item距离左边的间距
    margin-right: auto; // 其他item距离第一个item的间距
  }

  .el-menu--horizontal > .el-menu-item:last-child {
    margin-right: 4vw; // 最后一个item距离右边的间距
  }
}
</style>
