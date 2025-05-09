import type { FormDataTypeB } from '@/types';
import { ref } from 'vue';
import type { FormInstance } from 'element-plus';
import { useRouter } from 'vue-router';
import { loginByEmailRequest } from '@/service/api/login';
import { localCache } from '@/utils/cache';
import { LOGIN_TOKEN, ROLE, UID } from '@/global/constant/login';
import ROUTE from '@/global/constant/route';

export default function useEmailLogin(formData: FormDataTypeB) {
  const formRef = ref<FormInstance | undefined>(); // 获取表单对象 formRef
  const router = useRouter(); // 实例化路由对象

  // 父组件登录按钮，委托给子组件实现的登录功能
  function login() {
    if (!formRef.value) return; // 检查表单对象是否存在

    formRef.value.validate((valid) => {
      if (valid) {
        // 通过表单验证，发送邮箱登录请求
        loginByEmailRequest(formData.email, formData.verifyCode)
          .then((res) => {
            const code = res.data.code; // 业务状态码

            if (code === 0) {
              // 登录成功
              // 1.保存uid、token、role
              localCache.setItem(UID, res.data.data.uid);
              localCache.setItem(LOGIN_TOKEN, res.data.data.token);
              localCache.setItem(ROLE, res.data.data.role);

              // 2.提示用户登录成功
              ElMessage({ message: res.data.msg, type: 'success' });

              // 3.跳转到聊天页面
              setTimeout(() => {
                router.push(ROUTE.PATH.CHAT);
              }, 3000);
            } else {
              // 登录失败
              ElMessage({ message: res.data.msg, type: 'error' });
            }
          })
          .catch(() => {
            ElMessage({ message: '网络异常', type: 'error' }); // 一般是网络异常的问题
          });
      } else {
        // 没有通过表单验证，提示错误原因
        ElMessage({ message: '格式错误，请重试', type: 'error' });
      }
    });
  }

  return {
    formRef,
    login
  };
}
