<template>
  <div class="login-again">
    <div>立即前往，登录页面</div>
    <div class="count-down" v-if="flag">{{ second }}</div>
  </div>
</template>

<script setup lang="ts">
// 定义一个名为“update:active”的事件，这个事件与父组件有关联
const emits = defineEmits(['update:active']);

// redirect相关的数据和方法
import useRedirectFunc from '@/hooks/forget/login-again/useRedirectFunc';
const { flag, second, redirect } = useRedirectFunc(emits);

// 对外暴露
defineExpose({
  redirect
});
</script>

<style scoped lang="scss">
.login-again {
  height: 58px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;

  // 文字大小
  font-size: 18px;
  font-weight: 300;
  letter-spacing: 2px;
}
</style>
