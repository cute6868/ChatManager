<template>
  <div class="verify-identity">
    <EmailPanel ref="emailPanel" />
  </div>
</template>

<script setup lang="ts">
// 引入邮箱登录面板的组件
import EmailPanel from '@/views/login/LoginView/LoginPanel/EmailPanel.vue';

// 定义一个名为“update:active”的事件，这个事件与父组件有关联
const emits = defineEmits(['update:active']);

// 验证身份，验证通过后自动切换到下一个面板
import { ref } from 'vue';
const emailPanel = ref<InstanceType<typeof EmailPanel>>();
async function check() {
  const isSuccess = await emailPanel.value?.login();
  if (isSuccess) emits('update:active'); // 运行“update:active”事件
}

// 对外暴露
defineExpose({
  check
});
</script>

<style scoped lang="scss"></style>
