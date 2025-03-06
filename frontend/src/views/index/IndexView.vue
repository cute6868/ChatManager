<template>
  <div class="index">
    <!-- 导航栏 -->
    <nav>
      <NavigationBar />
    </nav>

    <!-- 正文内容 -->
    <main>
      <MainContent />
    </main>

    <!-- 底部备案 -->
    <footer id="footer">
      <p>备案号：粤 ICP 备 12345678 号</p>
    </footer>
  </div>
</template>

<script setup lang="ts">
// 引入子组件
import NavigationBar from './IndexView/NavigationBar.vue';
import MainContent from './IndexView/MainContent.vue';
</script>

<style scoped lang="scss">
// 样式布局
.index {
  // 导航栏
  nav {
    width: 100%;
    height: 80px;
  }

  // 备案信息
  #footer {
    width: 100%;
    height: 50px;
    background-color: pink;
  }
}
</style>
