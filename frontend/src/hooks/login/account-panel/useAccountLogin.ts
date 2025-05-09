import type { FormDataTypeA } from '@/types';
import { ref } from 'vue';
import type { FormInstance } from 'element-plus';
import { loginByAccountRequest } from '@/service/api/login';
import { localCache } from '@/utils/cache';
import { ACCOUNT, LOGIN_TOKEN, PASSWORD, ROLE, UID } from '@/global/constant/login';
import { useRouter } from 'vue-router';
import ROUTE from '@/global/constant/route';

export default function useAccountLogin(formData: FormDataTypeA) {
  const formRef = ref<FormInstance | undefined>(); // 获取表单对象 formRef
  const router = useRouter();

  // 父组件登录按钮，委托给子组件实现的登录功能
  function login(checkboxState: boolean) {
    if (!formRef.value) return; // 检查表单对象是否存在

    formRef.value.validate((valid) => {
      if (valid) {
        // 通过表单验证，发送登录请求
        loginByAccountRequest(formData.account, formData.password)
          .then((res) => {
            const code = res.data.code; // 业务状态码

            if (code === 0) {
              // 登录成功
              // 1.保存uid、token、role
              localCache.setItem(UID, res.data.data.uid);
              localCache.setItem(LOGIN_TOKEN, res.data.data.token);
              localCache.setItem(ROLE, res.data.data.role);

              // 2.根据“记住密码”的勾选状态，保存或删除本地存储中的登录信息
              if (checkboxState) {
                localCache.setItem(ACCOUNT, formData.account);
                localCache.setItem(PASSWORD, formData.password);
              } else {
                localCache.removeItem(ACCOUNT);
                localCache.removeItem(PASSWORD);
              }

              // 3.提示用户登录成功
              ElMessage({ message: res.data.msg, type: 'success' });

              // 4.跳转到聊天页面
              setTimeout(() => {
                router.push(ROUTE.PATH.CHAT);
              }, 3000);
            } else {
              // 登录失败
              ElMessage({ message: res.data.msg, type: 'error' });
            }
          })
          .catch(() => {
            ElMessage({ message: '网络异常', type: 'error' }); // 一般是网络异常导致
          });
      } else {
        // 没有通过表单验证
        ElMessage({ message: '账号或密码错误', type: 'error' });
      }
    });
  }

  return {
    formRef,
    login
  };
}
