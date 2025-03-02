import type { FormDataTypeA } from '@/types';
import { ref } from 'vue';
import type { FormInstance } from 'element-plus';
import useLoginStore from '@/store/login';
const loginStore = useLoginStore();

export default function useAccountLogin(formData: FormDataTypeA) {
  // 获取表单对象 formRef
  const formRef = ref<FormInstance | undefined>();

  // 父组件登录按钮，委托给子组件实现的登录功能
  async function login(checkboxState: boolean) {
    if (!formRef.value) return; // 检查表单对象是否存在

    formRef.value.validate(async (valid) => {
      // valid 是一个布尔值，表示表单验证是否通过
      if (valid) {
        // 执行账号登录行为
        const result = await loginStore.accountLoginAction(
          formData.account,
          formData.password,
          checkboxState
        );
        if (result) ElMessage({ message: result, type: 'error' }); // 弹出错误提示
      } else {
        ElMessage({ message: '登录失败，账号或密码错误！', type: 'error' }); // 格式错误，弹出提示
      }
    });
  }

  return {
    formRef,
    login
  };
}
