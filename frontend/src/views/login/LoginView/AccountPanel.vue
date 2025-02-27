<template>
  <div class="account-panel">
    <el-form ref="formRef" :model="form" :rules="rules" style="min-width: 260px" label-width="auto">
      <el-form-item label="账号" prop="account">
        <el-input v-model="form.account" />
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" type="password" autocomplete="true" show-password />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import type { FormRules, FormInstance } from 'element-plus';
import useLoginStore from '@/store/login';
const loginStore = useLoginStore();

// 表单数据
const form = reactive({
  account: '',
  password: ''
});

// 表单校验规则
const rules: FormRules = {
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 6, max: 20, message: '账号长度为6到20位', trigger: 'change' },
    { pattern: /^[a-zA-Z0-9]+$/, message: '账号只能包含数字和字母', trigger: 'change' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^(?!(?:\d{8,32})$)[A-Za-z\d\W_~!@#$%^&*()\-_=+$${}|;:'",.<>?/\\]{8,32}$/,
      message: '密码格式错误',
      trigger: 'blur'
    }
  ]
};

// 获取 formRef 对象
const formRef = ref<FormInstance | undefined>();

// 编写账号登录的逻辑
function login() {
  // 检查 formRef 对象是否存在，如果不存在则直接返回，不执行后续操作
  if (!formRef.value) return;

  // valid 是一个布尔值，表示表单验证是否通过
  formRef.value.validate((valid) => {
    if (valid) {
      // 获取用户输入的账号和密码
      const account = form.account;
      const password = form.password;

      // 执行账号登录行为
      loginStore.accountLoginAction(account, password).then((res) => {
        ElMessage({ message: res, type: 'error' });
      });
    } else {
      // 格式不正确，弹出错误提示
      ElMessage({ message: '登录失败，账号或密码错误！', type: 'error' });
    }
  });
}

// 对外暴露
defineExpose({
  login
});
</script>

<style scoped lang="scss"></style>
