<template>
  <div class="index">
    <!-- 导航栏 -->
    <nav>
      <NavigationBar />
    </nav>

    <!-- 主体部分 -->
    <div class="main">
      <SectionOne />
      <SectionTwo />
    </div>

    <!-- 底部备案 -->
    <div class="footer">
      <p>备案号：粤 ICP 备 12345678 号</p>
    </div>
  </div>
</template>

<script setup lang="ts">
// 引入子组件
import NavigationBar from './IndexView/NavigationBar.vue';
import SectionOne from './IndexView/SectionOne.vue';
import SectionTwo from './IndexView/SectionTwo.vue';
</script>

<style scoped lang="scss">
// 样式布局
.index {
  width: 100%;

  nav {
    width: 100%;
    position: relative;
  }

  // 主体部分
  .main {
    margin-top: clamp(58px, 10vh, 76px);
  }

  // 备案信息
  .footer {
    width: 100%;
    height: 50px;
    background-color: pink;
  }
}
</style>
