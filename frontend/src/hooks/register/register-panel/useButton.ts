import { ref } from 'vue';
import type { FormDataTypeD } from '@/types';
import throttle from '@/utils/throttle';
import { registerRequest } from '@/service/api/register';
import { useRouter } from 'vue-router';
import ROUTE from '@/global/constant/route';
import { localCache } from '@/utils/cache';
import { LOGIN_TOKEN, ROLE, UID } from '@/global/constant/login';
import { THROTTLE_TIME } from '@/global/constant/rule';

export default function useButton(formData: FormDataTypeD) {
  const formRef = ref(); // 获取表单的实例
  const router = useRouter(); // 在外部作用域获取 router 实例

  function registerHandler() {
    if (!formRef.value) return; // 检查表单对象是否存在

    // 进行表单验证
    formRef.value.validate((valid: boolean) => {
      if (valid) {
        // 表单验证通过，发送注册请求
        registerRequest(formData.account, formData.password, formData.email, formData.verifyCode)
          .then((res) => {
            const code = res.data.code;

            if (code === 0) {
              // 注册成功
              ElMessage({ message: res.data.msg, type: 'success' });

              // 保存uid、token、role
              localCache.setItem(UID, res.data.data.uid);
              localCache.setItem(LOGIN_TOKEN, res.data.data.token);
              localCache.setItem(ROLE, res.data.data.role);

              // 2秒后跳转到聊天页面
              setTimeout(() => {
                router.push(ROUTE.PATH.CHAT); // 通过router实例跳转到聊天页面
              }, 2000);
            } else {
              // 注册失败
              ElMessage({ message: res.data.msg, type: 'error' });
            }
          })
          .catch(() => {
            ElMessage({ message: '网络异常', type: 'error', grouping: true });
          });
      } else {
        // 表单验证不通过，弹出提示
        ElMessage({ message: '格式错误，请重试', type: 'error' });
      }
    });
  }

  // 包装点击注册操作，节流
  const wrapRegisterHandler = throttle(registerHandler, THROTTLE_TIME);

  return {
    formRef,
    wrapRegisterHandler
  };
}
