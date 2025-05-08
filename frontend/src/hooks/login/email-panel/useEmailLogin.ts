import type { FormDataTypeB } from '@/types';
import { ref } from 'vue';
import type { FormInstance } from 'element-plus';
import useLoginStore from '@/store/login';
const loginStore = useLoginStore();

export default function useEmailLogin(form: FormDataTypeB) {
  // 获取表单对象 formRef
  const formRef = ref<FormInstance | undefined>();

  // 父组件登录按钮，委托给子组件实现的登录功能
  async function login() {
    // 登录是否成功
    let isSuccess = false;

    if (!formRef.value) return isSuccess; // 检查表单对象是否存在

    // 进行表单验证
    formRef.value.validate(async (valid) => {
      // valid 是一个布尔值，表示表单验证是否通过
      if (valid) {
        // 验证通过，执行邮箱登录行为
        const result = await loginStore.emailLoginAction(form.email, form.verifyCode);

        if (result) {
          ElMessage({ message: result, type: 'error' }); // 登录失败，弹出错误原因
        } else {
          isSuccess = true; // 登录成功
        }
      } else {
        // 验证失败，弹出提示
        ElMessage({ message: '格式错误，请重试', type: 'error' });
      }
    });

    return isSuccess;
  }

  return {
    formRef,
    login
  };
}
