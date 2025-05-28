<template>
  <div class="function-panel">
    <!-- 多模型面板(聊天时展示) -->
    <div id="model">多模型面板的父级</div>

    <!-- 欢迎信息（初始时展示） -->
    <h1 id="title">晚上好,欢迎使用ChatManager</h1>

    <!-- 聊天框（一直展示，只是位置会变化） -->
    <div id="chat"><ChatFrame /></div>
  </div>
</template>

<script setup lang="ts">
import ChatFrame from './ChatFrame.vue';
</script>

<style scoped lang="scss">
.function-panel {
  width: 100%;
  height: 100%;

  display: flex;
  flex-direction: column;
  align-items: center;
}

#chat {
  margin-top: 180px;
}
</style>
