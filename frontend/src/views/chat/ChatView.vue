<template>
  <div class="chat">
    <div class="top">
      <!-- 顶部菜单栏 -->
      <TopMenu />
    </div>
    <!-- 功能面板 -->
    <div class="other">
      <FunctionPanel />
    </div>
  </div>
</template>

<script setup lang="ts">
import TopMenu from './ChatView/TopMenu.vue';
import FunctionPanel from './ChatView/FunctionPanel.vue';
</script>

<style scoped lang="scss">
.chat {
  width: 100vw;
  height: 100vh;

  .top {
    width: 100%;
    height: 8%;
    position: absolute;
  }

  .other {
    width: 100%;
    height: 92%;
    position: absolute;
  }
}
</style>
