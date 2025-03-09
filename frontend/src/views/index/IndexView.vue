<template>
  <div class="index">
    <!-- 导航栏 -->
    <NavigationBar />

    <!-- 主体部分 -->
    <div class="main">
      <SectionOne />
      <SectionTwo />
    </div>

    <!-- 底部备案 -->
    <div class="footer">
      <!-- 版权声明 -->
      <span>&copy;2025-2035 版权所有 ChatManager 运营者 </span>

      <!-- ICP备案信息 -->
      <a href="#" target="_blank">粤ICP备2024000001号</a>

      <!-- 公安备案信息 -->
      <a href="#" target="_blank">粤公网安备44010102000001号</a>
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
// 基本布局
.index {
  width: 100%;
  position: relative;
}

// 主体部分
.main {
  width: 100%;
  border-radius: 48px;
}

// 备案信息
.footer {
  width: 100%;
  padding: 10px 0;
  background-color: rgb(251, 251, 251);

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  font-size: 12px;
  line-height: 1.5;
}
</style>
