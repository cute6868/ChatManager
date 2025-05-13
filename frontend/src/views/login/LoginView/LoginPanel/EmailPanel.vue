<template>
  <div class="email-panel">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      style="width: 260px"
      label-width="auto"
    >
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
</template>

<script setup lang="ts">
// 表单相关的数据和方法
import useForm from '@/hooks/login/email-panel/useForm';
const { formData, formRules } = useForm();

// 验证码相关的数据和方法
import useVerifyCode from '@/hooks/login/email-panel/useVerifyCode';
const { flag, second, wrapGetCode } = useVerifyCode(formData);

// 邮箱登录相关的数据和方法
import useEmailLogin from '@/hooks/login/email-panel/useEmailLogin';
const { formRef, login } = useEmailLogin(formData);

// 对外暴露
defineExpose({
  login
});
</script>

<style scoped lang="scss">
.email-panel {
  .email-title {
    width: 100%;
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
