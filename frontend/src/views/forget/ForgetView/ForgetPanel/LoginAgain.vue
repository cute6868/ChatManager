<template>
  <div class="login-again">
    <div>即将跳转，登录页面</div>
    <div class="count-down" v-if="flag">{{ second }}</div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
const second = ref(3);
const flag = ref(false);

// 定义一个名为“update:active”的事件，这个事件与父组件有关联
const emits = defineEmits(['update:active']);

import ROUTE from '@/global/constant/route';
import { useRouter } from 'vue-router';
const router = useRouter();
function redirect() {
  // 运行“update:active”事件，从而触发父组件的事件，实现自动切换到下一个面板
  emits('update:active');

  // 倒计时
  flag.value = true;
  setInterval(() => {
    if (second.value === 1) router.push(ROUTE.PATH.LOGIN); // 倒计时结束，跳转到登录页面
    second.value--;
  }, 1000);
}

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
