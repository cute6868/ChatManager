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
// 定义一个名为“update:active”的事件，这个事件与父组件有关联
const emits = defineEmits(['update:active']);

// 表单相关的数据和方法
import useForm from '@/hooks/forget/reset-password/useForm';
const { formData, formRules } = useForm();

// reset相关的数据和方法
import useResetFunc from '@/hooks/forget/reset-password/useResetFunc';
const { formRef, reset } = useResetFunc(emits, formData);

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
