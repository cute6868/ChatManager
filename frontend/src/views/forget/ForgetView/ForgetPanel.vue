<template>
  <div class="forget-panel">
    <!-- 步骤条 -->
    <div class="step-bar">
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="身份验证" />
        <el-step title="密码重置" />
        <el-step title="重新登录" />
      </el-steps>
    </div>

    <!-- 操作区间 -->
    <div class="option">
      <!-- 操作框 -->
      <div class="action-frame">
        <VerifyIdentity v-if="active === 0" @update:active="updateActiveData" ref="VI" />
        <ResetPassword v-if="active === 1" @update:active="updateActiveData" ref="RP" />
        <LoginAgain v-if="active >= 2" @update:active="updateActiveData" ref="LA" />
      </div>

      <!-- 底部按钮 -->
      <el-button class="btn" type="primary" @click="wrapClickHandler">下一步</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
const active = ref(0);
function updateActiveData() {
  if (active.value < 3) active.value++;
}

// 引入子组件
import VerifyIdentity from './ForgetPanel/VerifyIdentity.vue';
import ResetPassword from './ForgetPanel/ResetPassword.vue';
import LoginAgain from './ForgetPanel/LoginAgain.vue';

// 获取子组件对象
const VI = ref<InstanceType<typeof VerifyIdentity>>();
const RP = ref<InstanceType<typeof ResetPassword>>();
const LA = ref<InstanceType<typeof LoginAgain>>();

// 点击操作
function clickHandler() {
  if (active.value === 0) VI.value?.check();
  // else if (active.value === 1) RP.value?.reset();
  // else if (active.value === 2) LA.value?.login();
}

// 包装点击操作，进行防抖
import debounce from '@/utils/debounce';
const wrapClickHandler = debounce(clickHandler, 500);
</script>

<style scoped lang="scss">
.forget-panel {
  // 面板大小
  width: max(28%, 300px);
  height: max(36%, 270px);

  // 面板颜色
  background-color: white;

  // 面板边框
  border-radius: 1%;

  // 让面板向上移动一点
  margin-bottom: 4%;

  // 面板内容元素间隔
  padding-top: max(2%, 34px);

  // 步骤条
  .step-bar {
    --el-color-success: rgb(64, 158, 255);
  }

  // 操作区间
  .option {
    width: 100%;
    padding-top: 14px;

    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    // 操作框
    .action-frame {
      width: max(76%, 274px);
      height: 140px;

      display: flex;
      justify-content: center;
      align-items: center;

      border: 1px solid rgb(220, 223, 230);
      border-radius: 4px;
    }

    // 底部按钮
    .btn {
      width: max(77%, 276px);
      margin-top: 13px;
    }
  }
}
</style>
