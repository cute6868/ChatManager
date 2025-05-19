<template>
  <div class="chat">
    <div class="top">
      <!-- 顶部菜单栏 -->
      <TopMenu />
    </div>

    <div class="middle"></div>
  </div>
</template>

<script setup lang="ts">
import TopMenu from './ChatView/TopMenu.vue';
</script>

<style scoped lang="scss">
.chat {
  width: 100vw;
  height: 100vh;

  .top {
    width: 100%;
    height: 8%;
    background-color: pink;
  }

  .middle {
    width: 100%;
    height: 92%;
    background-color: gray;
  }
}
</style>
