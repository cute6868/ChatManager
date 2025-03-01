<template>
  <div class="login-panel">
    <!-- 标题 -->
    <div class="title">测试测试测试</div>

    <!-- 登录方式 tabs -->
    <div class="tabs">
      <el-tabs type="border-card" stretch v-model="activeName" @tab-change="tabChangeHandler">
        <!-- 账号登录 -->
        <el-tab-pane name="byAccount">
          <!-- 顶部标签 -->
          <template v-slot:label>
            <div class="label">
              <el-icon><User /></el-icon>
              <span>账号登录</span>
            </div>
          </template>

          <!-- 具体内容 -->
          <AccountPanel ref="accountPanel" />
        </el-tab-pane>

        <!-- 邮箱登录 -->
        <el-tab-pane name="byEmail">
          <!-- 顶部标签 -->
          <template v-slot:label>
            <div class="label">
              <el-icon><Message /></el-icon>
              <span>邮箱登录</span>
            </div>
          </template>

          <!-- 具体内容 -->
          <EmailPanel ref="emailPanel" />
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 可选控制区 -->
    <div class="control">
      <el-checkbox id="remember-pwd" v-model="remembered" label="记住密码" @change="saveState" />
      <el-link id="forget-pwd" href="#" target="_blank" :underline="false" type="primary">
        忘记密码
      </el-link>
    </div>

    <!-- 登录按钮 -->
    <el-button class="login-btn" type="primary" size="large" @click="loginHandler">
      立即登录
    </el-button>

    <!-- 注册链接 -->
    <el-link class="register-link" href="#" target="_blank">没有账号？前往注册</el-link>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import AccountPanel from './AccountPanel.vue';
import EmailPanel from './EmailPanel.vue';
import { localCache } from '@/utils/cache';

// 默认登录方式为账号登录
const activeName = ref('byAccount');

// 局部常量，本地存储的存键值
const STATE = 'remember-pwd-state';

// 优先从本地缓存中获取状态，如果没有，则默认设置为 false
const remembered = ref(localCache.getItem(STATE) ?? false);
// 将"记住密码的勾选状态"保存到本地缓存中
function saveState() {
  localCache.setItem(STATE, remembered.value);
}
function tabChangeHandler() {
  if (activeName.value === 'byEmail') {
    // 切换到邮箱登录时，取消勾选，并禁用"记住密码"，同时禁止点击"忘记密码"
    remembered.value = false;
    document.querySelector('#remember-pwd')?.setAttribute('disabled', 'disabled');
    document.querySelector('#forget-pwd')?.classList.add('disabled-element');
  } else {
    // 切换到账号登录时，解封"记住密码"，并恢复原来的勾选状态，同时解封"忘记密码"
    document.querySelector('#remember-pwd')?.removeAttribute('disabled');
    remembered.value = localCache.getItem(STATE);
    document.querySelector('#forget-pwd')?.classList.remove('disabled-element');
  }
}

// 获取AccountPanel组件的实例
const accountPanel = ref();

// 获取EmailPanel组件的实例
const emailPanel = ref();

// 登录操作转移
function loginHandler() {
  if (activeName.value === 'byAccount') {
    // 调用AccountPanel组件的login方法，进行账号登录
    const state: boolean = remembered.value;
    accountPanel.value.login(state);
  } else {
    // 调用EmailPanel组件的login方法，进行邮箱登录
    emailPanel.value.login();
  }
}
</script>

<style scoped lang="scss">
.login-panel {
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
  padding-top: max(2%, 20px);
  padding-right: max(2%, 12px);
  padding-bottom: max(1.5%, 16px);
  padding-left: max(2%, 12px);

  // 标题
  .title {
    width: 90%;
    text-align: center;
    margin-bottom: 5%;

    // 标题字体设置
    font-size: max(1.5vw, 20px);
    font-weight: bold;
  }

  // 登录方式 tabs
  .tabs {
    // label设置
    .label {
      width: 74%;
      display: flex;
      justify-content: space-between;
    }
  }

  // 可选控制区
  .control {
    width: 96%;
    display: flex;
    justify-content: space-between;
    margin-top: 4%;
  }

  // 登录按钮
  .login-btn {
    width: 100%;
    margin-top: 2%;
  }

  // 注册链接
  .register-link {
    margin-top: 5%;
    font-size: max(84%, 13px);
  }
}

// 禁用元素（切换到邮箱登录时，禁止点击"忘记密码"）
.disabled-element {
  pointer-events: none;
}
</style>
