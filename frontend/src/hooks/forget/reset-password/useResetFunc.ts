import type { FormDataTypeC } from '@/types';
import { ref } from 'vue';
import useResetStore from '@/store/reset';
const resetStore = useResetStore();

export default function useResetFunc(
  emits: (event: 'update:active', ...args: unknown[]) => void,
  formData: FormDataTypeC
) {
  const formRef = ref(); // 获取表单对象 formRef

  // 重置密码
  function reset() {
    let isSuccess = false;
    if (!formRef.value) return; // 检查表单对象是否存在

    formRef.value.validate(async (valid: boolean) => {
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

    if (isSuccess) emits('update:active'); // 运行“update:active”事件，从而触发父组件的事件，实现自动切换到下一个面板
  }

  return {
    formRef,
    reset
  };
}
