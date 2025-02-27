<template>
  <div class="not-found">not-found</div>
</template>

<script setup lang="ts">
import router from '@/router';

// 编程式导航，3秒后跳转回首页
setTimeout(() => {
  router.push('/index');
}, 3000);
</script>

<style scoped lang="scss">
.not-found {
}
</style>
