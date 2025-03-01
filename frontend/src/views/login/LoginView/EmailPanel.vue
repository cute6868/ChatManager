<template>
  <div class="email-panel">
    <el-form ref="formRef" :model="form" :rules="rules" style="width: 260px" label-width="auto">
      <el-form-item prop="email">
        <template #label>
          <div class="email-title">邮箱</div>
        </template>
        <el-input v-model="form.email" />
      </el-form-item>

      <el-form-item label="验证码" prop="verificationCode">
        <el-input v-model="form.verificationCode">
          <template #suffix>
            <div class="get-code" @click="getCode">
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
import { reactive, ref } from 'vue';
import type { FormRules, FormInstance } from 'element-plus';
import useEmailStore from '@/store/email';
import useLoginStore from '@/store/login';
const emailStore = useEmailStore();
const loginStore = useLoginStore();

// 表单数据
const form = reactive({
  email: '',
  verificationCode: ''
});

// 表单验证规则
const rules: FormRules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  verificationCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { min: 6, max: 6, message: '验证码长度为6位', trigger: 'change' },
    { pattern: /^[0-9]+$/, message: '验证码只能是数字', trigger: 'change' }
  ]
};

const second = ref(60); // 倒计时的秒数
const flag = ref(false); // 显示倒计时的开关

// 编写获取验证码的逻辑
async function getCode() {
  // 判断邮箱格式是否符合规范
  const regex = /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
  if (!regex.test(form.email)) {
    ElMessage({ message: '邮箱格式不正确', type: 'error' });
    return;
  }

  // 如果格式符合规范，则执行获取验证码行为
  const result = await emailStore.getEmailVerificationCodeAction(form.email);
  if (result.state) {
    ElMessage({ message: result.content, type: 'success' }); // 获取验证码成功

    // 显示倒计时60秒，并且期间不得再申请验证码
    second.value = 60; // 初始化
    flag.value = true; // 显示
    document.querySelector('.get-code')?.classList.add('disabled-element'); //禁止操作

    // 进行倒计时
    const timer = setInterval(() => {
      second.value--;
      if (second.value === 0) {
        flag.value = false; // 隐藏
        document.querySelector('.get-code')?.classList.remove('disabled-element'); // 允许操作
        clearInterval(timer);
      }
    }, 1000);
  } else {
    ElMessage({ message: result.content, type: 'error' }); // 获取验证码失败
  }
}

// 获取邮箱登录的表单实例 formref
const formRef = ref<FormInstance | undefined>();

// 编写邮箱登录的逻辑
async function login() {
  if (!formRef.value) return;
  formRef.value.validate(async (valid) => {
    if (valid) {
      const result = await loginStore.emailLoginAction(form.email, form.verificationCode);
      if (result) {
        ElMessage({ message: result, type: 'error' });
      }
    } else {
      ElMessage({ message: '格式错误，请重试', type: 'error' }); // 格式不正确，弹出错误提示
    }
  });
}

// 对外暴露
defineExpose({
  login
});
</script>

<style scoped lang="scss">
.email-panel {
  .email-title {
    width: 100%;
    margin-right: 6px;
  }

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

  // 禁用元素
  .disabled-element {
    pointer-events: none;
  }
}
</style>
