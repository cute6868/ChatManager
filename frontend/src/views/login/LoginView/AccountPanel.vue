<template>
  <div class="account-panel">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      style="width: 260px"
      label-width="auto"
    >
      <el-form-item label="账号" prop="account">
        <el-input v-model="formData.account" />
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input v-model="formData.password" type="password" autocomplete="true" show-password />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
// 表单相关的数据和方法
import useForm from '@/hooks/login/account-panel/useForm';
const { formData, formRules } = useForm();

// 账号登录相关的数据和方法
import useAccountLogin from '@/hooks/login/account-panel/useAccountLogin';
const { formRef, login } = useAccountLogin(formData);

// 对外暴露，让父组件调用
defineExpose({
  login
});
</script>

<style scoped lang="scss"></style>
