<template>
  <div class="section-two">
    <div class="content">123456</div>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped lang="scss">
.section-two {
  width: 100%;
  height: 800px;
  border-radius: 48px;
  margin-bottom: 60px;

  // 背景图片
  background-image: url('@/assets/img/index/bg1.svg');
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;

  // 内容居中
  display: flex;
  justify-content: center;
  align-items: center;
}

.content {
  width: 100%;
  height: 600px;
  background-color: pink;
}
</style>
