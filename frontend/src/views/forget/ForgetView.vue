<template>
  <div class="forget">
    <ForgetPanel />
  </div>
</template>

<script setup lang="ts">
import ForgetPanel from './ForgetView/ForgetPanel.vue';
</script>

<style scoped lang="scss">
.forget {
  width: 100vw;
  height: 100vh;

  // 让容器里面的元素居中
  display: flex;
  justify-content: center;
  align-items: center;

  // 背景图片
  background-image: url('@/assets/img/bg.png');
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
}
</style>
