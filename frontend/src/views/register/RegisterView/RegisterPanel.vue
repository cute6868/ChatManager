<template>
  <div class="register-panel">
    <div class="content">
      <div class="title">注册账号</div>

      <div class="form">
        <el-form ref="formRef" :model="formData" :rules="formRules" label-width="auto">
          <el-form-item label="账号" prop="account">
            <el-input v-model="formData.account" />
          </el-form-item>

          <el-form-item label="设置密码" prop="password">
            <el-input v-model="formData.password" show-password />
          </el-form-item>

          <el-form-item label="确认密码" prop="confirm">
            <el-input v-model="formData.confirm" show-password />
          </el-form-item>

          <el-form-item label="QQ邮箱" prop="email">
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

      <el-button type="primary" class="btn" @click="wrapRegisterHandler">立即注册</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
// 表单相关的数据和方法
import useForm from '@/hooks/register/register-panel/useForm';
const { formData, formRules } = useForm();

// 验证码相关的数据和方法
import useVerifyCode from '@/hooks/register/register-panel/useVerifyCode';
const { flag, second, wrapGetCode } = useVerifyCode(formData);

// 注册按钮相关的数据和方法
import useButton from '@/hooks/register/register-panel/useButton';
const { formRef, wrapRegisterHandler } = useButton(formData);
</script>

<style scoped lang="scss">
.register-panel {
  // 白色面板
  width: max(32%, 360px);
  height: max(66%, 490px);
  background-color: white;
  border-radius: 2%;
  margin-bottom: 14px;

  display: flex;
  justify-content: center;
  align-items: center;

  // 非白色面板的内容
  .content {
    width: 100%;
    height: 100%;

    display: flex;
    flex-direction: column;
    align-items: center;

    // 文字标题："注册账号"
    .title {
      margin-top: 32px;
      margin-bottom: 28px;

      font-size: 26px;
      font-weight: 600;
      letter-spacing: 4px;
    }

    // 表单
    .form {
      width: min(84%, 410px);

      border: 1px solid rgba(0, 0, 0, 0.1);
      box-shadow: 0 0 1px rgba(0, 0, 0, 0.1);
      border-radius: 10px;

      box-sizing: border-box;
      padding: 24px 16px 0;

      // 每个输入框之间的距离
      .el-form-item {
        margin-bottom: 30px;
      }
    }

    // 注册按钮
    .btn {
      width: min(84%, 410px);
      height: 36px;
      margin-top: 20px;
      border: 1px solid #e6e6e6;
      font-weight: 600;
    }
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
