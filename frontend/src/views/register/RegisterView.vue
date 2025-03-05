<template>
  <div class="register">
    <RegisterPanel />
  </div>
</template>

<script setup lang="ts">
import RegisterPanel from './RegisterView/RegisterPanel.vue';
</script>

<style scoped lang="scss">
.register {
  width: 100%;
  height: 100%;

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
