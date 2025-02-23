<template>
  <div>Hello {{ str }}</div>
  <input type="text" v-model="str" />
</template>

<script setup lang="ts">
import { ref } from 'vue';
const str = ref('World');
</script>

<style scoped></style>
