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
            <div>
              <span class="get-code" @click="handler">获取验证码</span>
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
import useLoginStore from '@/store/login';
import useVerificationStore from '@/store/verification';
const loginStore = useLoginStore();
const verificationStore = useVerificationStore();

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

// 编写获取验证码的逻辑
function handler() {
  // 判断邮箱格式是否符合规范
  const regex = /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
  if (!regex.test(form.email)) return;
  // 申请获得验证码
  verificationStore.getVerificationCodeAction();
  // 倒计时
  setTimeout(() => {
    // xxxxxxxxxxxxxxx
  }, 60 * 1000);
}

// 获取邮箱登录的表单实例 formref
const formRef = ref<FormInstance | undefined>();

// 编写邮箱登录的逻辑
function login() {
  if (!formRef.value) return;
  formRef.value.validate((valid) => {
    if (valid) {
      const email = form.email;
      const verificationCode = form.verificationCode;

      loginStore.emailLoginAction(email, verificationCode).then((res) => {
        // ElMessage({ message: res, type: 'error' });
      });
    } else {
      // 格式不正确，弹出错误提示
      ElMessage({ message: '格式错误，请重试', type: 'error' });
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
    width: 76px;
    border-left: 2px solid rgb(192, 186, 186);
    font-size: 12px;
    padding-left: 10px;
    cursor: pointer;
    &:hover {
      color: rgb(64, 158, 255);
    }
  }
}
</style>
