<template>
  <div class="login-panel">
    <!-- 标题 -->
    <div class="title">AI聊天管家</div>

    <!-- 登录方式 tabs -->
    <div class="tabs">
      <el-tabs v-model="activeTabName" type="border-card" @tab-change="tabChangeHandler" stretch>
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
      <el-checkbox
        v-model="checkboxState"
        @change="saveCheckboxState"
        label="记住密码"
        :class="{ 'disabled-element': isDisabled }"
      />
      <el-link
        :underline="false"
        type="primary"
        @click="goToForget"
        :class="{ 'disabled-element': isDisabled }"
      >
        忘记密码
      </el-link>
    </div>

    <!-- 登录按钮 -->
    <el-button class="login-btn" type="primary" size="large" @click="wrapLoginHandler">
      立即登录
    </el-button>

    <!-- 注册链接 -->
    <el-link class="register-link" @click="goToRegister">没有账号？前往注册</el-link>
  </div>
</template>

<script setup lang="ts">
// 引入子组件
import AccountPanel from './LoginPanel/AccountPanel.vue';
import EmailPanel from './LoginPanel/EmailPanel.vue';

// 复选框相关的数据和方法
import useCheckbox from '@/hooks/login/login-panel/useCheckbox';
const { checkboxState, CHECKBOX_STATE, saveCheckboxState } = useCheckbox();

// tabs相关的数据和方法
import useTabs from '@/hooks/login/login-panel/useTabs';
const { activeTabName, isDisabled, tabChangeHandler } = useTabs(checkboxState, CHECKBOX_STATE);

// 登录按钮相关的数据和方法
import useLoginButton from '@/hooks/login/login-panel/useLoginButton';
const { accountPanel, emailPanel, wrapLoginHandler } = useLoginButton(activeTabName, checkboxState);

// 忘记密码相关的数据和方法
import useForgetPassword from '@/hooks/login/login-panel/useForgetPassword';
const { goToForget } = useForgetPassword();

// 注册链接相关的数据和方法
import useRegisterLink from '@/hooks/login/login-panel/useRegisterLink';
const { goToRegister } = useRegisterLink();
</script>

<style scoped lang="scss">
.login-panel {
  width: max(20%, 304px);
  background-color: white;

  // 让面板容器上移一点
  margin-bottom: 6%;

  // 让面板容器的四个角为圆角
  border-radius: 1%;

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

// 禁用元素（切换到邮箱登录时，禁止点击"记住密码"和"忘记密码"）
.disabled-element {
  pointer-events: none;
  color: #c0c4cc;
}
</style>
