<template>
  <div class="reset-password">
    <el-form ref="formRef" :model="formData" :rules="formRules">
      <el-form-item class="form-item" label="新的密码" prop="password">
        <el-input
          style="width: 184px"
          v-model="formData.password"
          type="password"
          autocomplete="off"
          show-password
        />
      </el-form-item>

      <el-form-item label="确认密码" prop="confirm">
        <el-input
          style="width: 184px"
          v-model="formData.confirm"
          type="password"
          autocomplete="off"
          show-password
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue';

const formData = reactive({
  password: '',
  confirm: ''
});

// 表单规则
function validatorFunc(rule: unknown, value: unknown, callback: (error?: Error) => void) {
  if (value === formData.password) callback();
  else callback(new Error('两次输入的密码不一致'));
}

const formRules = reactive({
  password: [
    { required: true, message: '请输入新的密码', trigger: 'blur' },
    {
      pattern: /^(?!(?:\d{8,32})$)[A-Za-z\d\W_~!@#$%^&*()\-_=+$${}|;:'",.<>?/\\]{8,32}$/,
      message: '长度为8到32位，不能全是数字，可以包含字母、特殊符号',
      trigger: 'blur'
    }
  ],
  confirm: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validatorFunc, trigger: 'blur' }
  ]
});

// ==========================================
// 获取表单对象 formRef
import { ref } from 'vue';
import type { FormInstance } from 'element-plus';
const formRef = ref<FormInstance | undefined>();

import useResetStore from '@/store/reset';
const resetStore = useResetStore();

// 定义一个名为“update:active”的事件，这个事件与父组件有关联
const emits = defineEmits(['update:active']);
function reset() {
  let isSuccess = false;
  if (!formRef.value) return; // 检查表单对象是否存在

  formRef.value.validate(async (valid) => {
    if (valid) {
      // 表单验证成功
      const result = await resetStore.resetPasswordAction(formData.password);
      if (result.state) isSuccess = true;
      else ElMessage.error(result.content);
    } else {
      // 表单验证失败
      ElMessage.error('格式错误，请重试');
    }
  });

  // 运行“update:active”事件，从而触发父组件的事件，实现自动切换到下一个面板
  if (isSuccess) emits('update:active');
}

// 对外暴露
defineExpose({
  reset
});
</script>

<style scoped lang="scss">
.reset-password {
  .form-item {
    margin-bottom: 30px;
  }
}
</style>
