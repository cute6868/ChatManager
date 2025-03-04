<template>
  <div class="verify-identity">
    <EmailPanel ref="emailPanel" />
  </div>
</template>

<script setup lang="ts">
// 定义一个名为“update:active”的事件，这个事件与父组件有关联
const emits = defineEmits(['update:active']);

// 引入邮箱登录面板的组件
import EmailPanel from '@/views/login/LoginView/LoginPanel/EmailPanel.vue';

// check相关的数据和方法
import useCheckFunc from '@/hooks/forget/verify-identity/useCheckFunc';
const { emailPanel, check } = useCheckFunc(emits);

// 对外暴露
defineExpose({
  check
});
</script>

<style scoped lang="scss"></style>
