<template>
  <div class="not-found">
    <div class="content">
      <div id="error">404</div>
      <h1>未找到相关页面</h1>
      <p>抱歉，您访问的页面可能不存在或者被删除</p>
      <el-button type="primary" @click="goToIndex">返回首页</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import ROUTE from '@/global/constant/route';
const router = useRouter();

function goToIndex() {
  router.push(ROUTE.PATH.INDEX);
}
</script>

<style scoped lang="scss">
// 整体样式
.not-found {
  width: 100%;
  height: 100%;

  display: flex;
  justify-content: center;
  align-items: center;

  // 背景图片
  background-image: url('@/assets/img/bg.png');
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
}

// 内容样式
.content {
  width: 300px;
  height: 180px;
  letter-spacing: 1px;
  margin-bottom: 10%;

  // background-color: pink;

  display: flex;
  flex-direction: column;
  align-items: center;

  // 404
  #error {
    font-size: 68px;
    font-weight: 400;
    color: rgb(113, 178, 243);
    margin-bottom: 16px;
  }

  // 未找到相关页面
  h1 {
    font-size: 26px;
    margin-bottom: 12px;
  }

  // 抱歉，您访问的页面可能不存在或者被删除
  p {
    width: 100%;
    font-size: 14px;
    color: #928b8b;
    margin-bottom: 20px;
  }

  // 返回首页
  .el-button {
    width: 160px;
  }
}
</style>
