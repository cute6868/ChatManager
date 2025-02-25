<template>
  <div class="panel">
    <!-- 标题 -->
    <div class="title">测试测试测试</div>

    <!-- 登录方式tabs -->
    <el-tabs type="border-card" stretch>
      <!-- 账号登录 -->
      <el-tab-pane class="account-login">
        <template v-slot:label>
          <el-icon><Cellphone /></el-icon>
          <span>账号登录</span>
        </template>

        <el-input class="input" v-model="account" style="width: 240px" placeholder="请输入账号名" />
        <el-input
          v-model="password"
          style="width: 240px"
          placeholder="请输入登录密码"
          type="password"
          show-password
        />
      </el-tab-pane>

      <!-- 手机登录 -->
      <el-tab-pane label="手机登录" class="phone-login">
        <el-input v-model="account" style="width: 240px" placeholder="请输入手机号" />
        <el-input v-model="account" style="width: 240px" placeholder="请输入验证码" />
      </el-tab-pane>
    </el-tabs>

    <!-- 其他 -->
    <div class="other">
      <el-checkbox v-model="remembered" label="记住密码" size="large" />
      <el-link href="#" target="_blank" :underline="false" type="primary">忘记密码</el-link>
    </div>

    <!-- 登录按钮 -->
    <el-button type="primary" :loading="loading" size="large" @click="run">立即登录</el-button>

    <!-- 注册链接 -->
    <el-link href="#" target="_blank">没有账号？前往注册</el-link>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
const remembered = ref(true);
const loading = ref(false);
const account = ref('');
const password = ref('');
function run() {
  loading.value = !loading.value;
}
</script>

<style scoped lang="scss">
.panel {
  width: max(20%, 304px);
  background-color: white;

  // 让面板容器上移一点
  margin-bottom: 6%;

  // 让面板容器里面元素居中
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  // 设置面板容器与里面元素之间内部间距
  padding: 2%;

  // 标题
  .title {
    width: 90%;
    text-align: center;
    margin-bottom: 4%;

    // 标题字体设置
    font-size: max(1.5vw, 20px);
    font-weight: bold;
  }

  // 登录方式tabs
  .account-login {
    width: 100%;
    background-color: pink;
    // display: flex;
    // justify-content: center;
  }
  // other容器两边对齐
  // .other {
  //   width: 100%;
  //   background-color: pink;
  //   display: flex;
  //   justify-content: space-between;
  // }
}
</style>
