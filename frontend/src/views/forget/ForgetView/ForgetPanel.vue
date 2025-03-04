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
      <el-button class="btn" type="primary" @click="wrapClickHandler">
        <span v-if="active !== 2">下一步</span>
        <span v-if="active === 2">确认</span>
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
// 引入子组件
import VerifyIdentity from '@/views/forget/ForgetView/ForgetPanel/VerifyIdentity.vue';
import ResetPassword from '@/views/forget/ForgetView/ForgetPanel/ResetPassword.vue';
import LoginAgain from '@/views/forget/ForgetView/ForgetPanel/LoginAgain.vue';

// active相关的数据和方法
import useActive from '@/hooks/forget/forget-panel/useActive';
const { active, updateActiveData } = useActive();

// 按钮相关的数据和方法
import useButton from '@/hooks/forget/forget-panel/useButton';
const { VI, RP, LA, wrapClickHandler } = useButton(active);
</script>

<style scoped lang="scss">
.forget-panel {
  // 面板大小
  width: max(28%, 300px);
  height: max(36%, 286px);

  // 面板颜色
  background-color: white;

  // 面板边框
  border-radius: 1%;

  // 让面板向上移动一点
  margin-bottom: 4%;

  // 面板内容元素间隔
  padding: 2px;
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
      width: max(76%, 260px);
      height: 140px;
      padding: 4px;

      display: flex;
      justify-content: center;
      align-items: center;

      border: 1px solid rgb(220, 223, 230);
      border-radius: 4px;
    }

    // 底部按钮
    .btn {
      width: max(77%, 274px);
      margin-top: 13px;
    }
  }
}
</style>
