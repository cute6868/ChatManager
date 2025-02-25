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

// 表单数据
const form = reactive({
  account: '',
  password: ''
});

// 表单校验规则
const rules: FormRules = {
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 6, max: 20, message: '账号长度为6到20位', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9]+$/, message: '账号只能包含数字和字母', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^(?!(?:\d{8,32})$)[A-Za-z\d\W_~!@#$%^&*()\-_=+$${}|;:'",.<>?/\\]{8,32}$/,
      message: '密码格式错误'
    }
  ]
};

// 获取 formRef 对象
const formRef = ref<FormInstance | undefined>();

// 账号登录的逻辑
function login() {
  // 检查 formRef 对象是否存在，如果不存在则直接返回，不执行后续操作
  if (!formRef.value) return;

  formRef.value.validate((valid, fields) => {
    // validate 方法接受一个回调函数作为参数，该回调函数会在验证完成后被调用
    // valid 是一个布尔值，表示表单验证是否通过
    // fields 是一个包含验证失败字段信息的对象
    if (valid) {
      console.log('已经发送登录请求');
    } else {
      console.log('请求错误', fields);
    }
  });
}

// 对外暴露
defineExpose({
  login
});
</script>

<style scoped lang="scss"></style>
