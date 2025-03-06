<template>
  <div class="main-content">
    <!-- 内容1 -->
    <div class="item">内容1</div>
    <!-- 内容2 -->
    <div class="item">内容2</div>
    <!-- 内容3 -->
    <div class="item">内容3</div>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped lang="scss">
.main-content {
  .item {
    width: 100%;
    height: 500px;
    border: 2px solid red;
    margin: 5px 0;
    background-color: gray;
  }
}
</style>
