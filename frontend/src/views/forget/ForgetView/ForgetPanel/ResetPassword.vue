<template>
  <div class="reset-password">
    <button @click="clickHandler">下一步</button>
  </div>
</template>

<script setup lang="ts">
// 定义一个名为“update:active”的事件，这个事件与父组件有关联
const emits = defineEmits(['update:active']);

function clickHandler() {
  emits('update:active'); // 运行“update:active”事件
}
</script>

<style scoped lang="scss">
.reset-password {
}
</style>
