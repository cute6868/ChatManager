import { ref } from 'vue';
import type { FormDataTypeD } from '@/types';
import debounce from '@/utils/debounce';
import useRegisterStore from '@/store/register';
const registerStore = useRegisterStore();

export default function useButton(formData: FormDataTypeD) {
  const formRef = ref(); // 获取表单的实例

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
          formData.verifyCode
        );
        if (result) ElMessage({ message: result, type: 'error' }); // 注册失败，弹出原因
      } else {
        ElMessage({ message: '格式错误，请重试', type: 'error' }); // 验证失败，弹出提示
      }
    });
  }

  // 包装点击注册操作，防抖
  const wrapRegisterHandler = debounce(registerHandler, 500);

  return {
    formRef,
    wrapRegisterHandler
  };
}
