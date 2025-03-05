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

          <el-form-item label="电子邮箱" prop="email">
            <el-input v-model="formData.email" />
          </el-form-item>

          <el-form-item label="验证码" prop="verificationCode">
            <el-input v-model="formData.verificationCode">
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

      <el-button class="btn" @click="wrapRegisterHandler">立即注册</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
// 表单数据 =======================================================
import { reactive } from 'vue';
const formData = reactive({
  account: '',
  password: '',
  confirm: '',
  email: '',
  verificationCode: ''
});

// 验证码相关的数据和方法
import useVerificationCode from '@/hooks/login/email-panel/useVerificationCode';
const { flag, second, wrapGetCode } = useVerificationCode(formData);

// 表单校验====================================================
import {
  ACCOUNT_REGEX,
  PASSWORD_REGEX,
  EMAIL_VERIFICATION_CODE_REGEX
} from '@/global/constant/rule';
// 确认密码的校验逻辑
function validatorFunc(rule: unknown, value: unknown, callback: (error?: Error) => void) {
  if (value === formData.password) callback();
  else callback(new Error('两次输入的密码不一致'));
}

import type { FormRules } from 'element-plus';

const formRules = reactive<FormRules>({
  account: [
    { required: true, message: '请设置您的账号', trigger: 'blur' },
    { min: 6, max: 20, message: '账号长度为6到20位', trigger: ['blur', 'change'] },
    { pattern: ACCOUNT_REGEX, message: '账号只能含有数字、字母', trigger: ['blur', 'change'] }
  ],
  password: [
    { required: true, message: '请输入您的密码', trigger: 'blur' },
    {
      pattern: PASSWORD_REGEX,
      message: '长度为8到32位，不能全是数字，可以包含字母、特殊符号',
      trigger: 'blur'
    }
  ],
  confirm: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validatorFunc, trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  verificationCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { min: 6, max: 6, message: '验证码长度为6位', trigger: ['blur', 'change'] },
    {
      pattern: EMAIL_VERIFICATION_CODE_REGEX,
      message: '验证码只能是数字',
      trigger: ['blur', 'change']
    }
  ]
});

// 注册按钮 =======================================================
import { ref } from 'vue';
const formRef = ref();
import useRegisterStore from '@/store/register';
const registerStore = useRegisterStore();
function registerHandler() {
  if (!formRef.value) return; // 检查表单对象是否存在

  // 进行表单验证
  formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      // 验证通过，执行注册行为
      const result = await registerStore.registerAction(
        formData.account,
        formData.password,
        formData.email,
        formData.verificationCode
      );
      if (result) ElMessage({ message: result, type: 'error' }); // 注册失败，弹出原因
    } else {
      ElMessage({ message: '格式错误，请重试', type: 'error' }); // 验证失败，弹出提示
    }
  });
}

// 包装点击注册操作，防抖
import debounce from '@/utils/debounce';
const wrapRegisterHandler = debounce(registerHandler, 500);
</script>

<style scoped lang="scss">
.register-panel {
  // 白色面板
  width: max(34%, 340px);
  height: max(66%, 490px);
  background-color: white;
  border-radius: 4px;
  margin-bottom: 14px;

  display: flex;
  justify-content: center;
  align-items: center;

  // 非白色面板的内容
  .content {
    width: 74%;
    height: 84%;
    // background-color: pink;

    display: flex;
    flex-direction: column;
    align-items: center;

    // 文字标题："注册账号"
    .title {
      font-size: 26px;
      font-weight: 600;
      letter-spacing: 4px;
      margin-bottom: 30px;
    }

    // 表单
    .form {
      // background-color: gray;
      width: 96%;
      border: 1px solid rgba(0, 0, 0, 0.1);
      border-radius: 2px;
      padding: 10px;
      margin-bottom: 14px;

      // 每个输入框之间的距离
      .el-form-item {
        margin-bottom: 30px;
      }
    }

    // 注册按钮
    .btn {
      width: 90%;
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
