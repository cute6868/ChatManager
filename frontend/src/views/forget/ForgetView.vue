<template>
  <div class="forget">
    <div class="white-panel">
      <!-- 标题 -->
      <h3 class="title">重置密码</h3>

      <!-- 重置密码面板 -->
      <div class="reset-password-panel">
        <el-form ref="formRef" :model="formData" :rules="formRules" label-width="auto">
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="formData.newPassword" type="password" show-password />
          </el-form-item>

          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="formData.confirmPassword" type="password" show-password />
          </el-form-item>

          <el-form-item prop="email">
            <template #label>
              <div class="email-title">QQ邮箱</div>
            </template>
            <el-input v-model="formData.email" />
          </el-form-item>

          <el-form-item label="验证码" prop="verifyCode">
            <el-input v-model="formData.verifyCode">
              <template #suffix>
                <div class="get-code" @click="wrapGetCode">
                  <span v-show="!flag">获取验证码</span>
                  <span v-show="flag">重新获取({{ second }}秒)</span>
                </div>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </div>

      <!-- 确认按钮 -->
      <el-button class="confirm-btn" type="primary" @click="wrapReset">确认</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
// 表单相关的数据和方法
import useForm from '@/hooks/forget/useForm';
const { formData, formRules } = useForm();

// 验证码相关的数据和方法
import useVerifyCode from '@/hooks/forget/useVerifyCode';
const { second, flag, wrapGetCode } = useVerifyCode(formData);

// 按钮相关的数据和方法
import useButton from '@/hooks/forget/useButton';
const { formRef, wrapReset } = useButton(formData);
</script>

<style scoped lang="scss">
.forget {
  width: 100vw;
  height: 100vh;

  display: flex;
  justify-content: center;
  align-items: center;

  background-image: url('@/assets/img/bg.png');
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;

  .white-panel {
    width: max(29%, 360px);
    height: max(56%, 420px);
    background-color: #fff;

    display: flex;
    flex-direction: column;
    align-items: center;

    box-sizing: border-box;

    margin-bottom: 6%;
    border-radius: 2%;
  }

  // 标题样式
  .title {
    margin-top: 32px;
    margin-bottom: 20px;

    font-size: 22px;
    letter-spacing: 2px;
  }

  .reset-password-panel {
    width: max(80%, 300px);
    height: max(9%, 270px);

    display: flex;
    justify-content: center;

    border-radius: 10px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 0 1px 0 rgba(0, 0, 0, 0.1);

    :deep(.el-form--default) {
      width: 90%;

      .el-form-item {
        margin-top: 28px;
      }
    }
  }

  // 确认按钮样式
  .confirm-btn {
    width: max(80%, 300px);
    margin-top: 18px;
  }

  // 验证码样式
  .get-code {
    width: 62px;
    height: 14px;
    letter-spacing: 1px;
    font-size: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    &:hover {
      color: rgb(64, 158, 255);
    }
    &:active {
      color: rgb(181, 213, 246);
    }
  }

  // 验证码样式
  // 禁用元素
  .disabled-element {
    pointer-events: none;
  }
}
</style>
