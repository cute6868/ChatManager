<template>
  <div class="login">
    <!-- 登录面板 -->
    <LoginPanel />
  </div>
</template>

<script setup lang="ts">
import LoginPanel from './LoginView/LoginPanel.vue';
</script>

<style scoped lang="scss">
.login {
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
