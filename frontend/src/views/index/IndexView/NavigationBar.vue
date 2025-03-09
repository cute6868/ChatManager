<template>
  <div class="navigation-bar">
    <el-menu :ellipsis="isEllipsis" mode="horizontal" :collapse="false">
      <!-- 左侧logo -->
      <el-menu-item index="0">
        <a :href="ROUTE.PATH.INDEX">
          <img src="/src/assets/img/index/logo.png" alt="ChatManager Logo" style="width: 180px" />
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

// ellipsis相关的数据和方法
import useEllipsis from '@/hooks/index/navigation-bar/useEllipsis';
const { isEllipsis } = useEllipsis();

// scroll相关的数据和方法
import useScroll from '@/hooks/index/navigation-bar/useScroll';
useScroll();
</script>

<style scoped lang="scss">
.navigation-bar {
  width: 100%;
}

// 展示样式
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

// 隐藏样式
.navigation-bar {
  position: fixed;
  top: 0px; // 初始时让导航栏在视口上方的0px位置，即暂时不隐藏
  transition: top 0.5s ease; // 过渡效果
  z-index: 1000; // 确保导航栏在其他元素之上
}
</style>
