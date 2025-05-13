import ROUTE from '@/global/constant/route';
import { resetPasswordRequest } from '@/service/api/reset';
import type { FormDataTypeC } from '@/types';
import debounce from '@/utils/debounce';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export default function useButton(formData: FormDataTypeC) {
  const formRef = ref(); // 获取表单对象 formRef
  const router = useRouter(); // 实例化路由对象

  function reset() {
    if (!formRef.value) return; // 检查表单对象是否存在

    formRef.value.validate((valid: true) => {
      if (valid) {
        // 通过表单验证，发送重置密码请求
        resetPasswordRequest(formData.newPassword, formData.email, formData.verifyCode)
          .then((res) => {
            const code = res.data.code; // 业务状态码

            if (code === 0) {
              // 重置请求处理成功
              ElMessage({ message: res.data.msg, type: 'success' });

              // 3秒后，自动跳转到登录页
              setTimeout(() => {
                router.push(ROUTE.PATH.LOGIN);
              }, 3000);
            } else {
              // 重置请求处理失败
              ElMessage({ message: res.data.msg, type: 'error' });
            }
          })
          .catch(() => {
            ElMessage({ message: '网络异常', type: 'error' }); // 一般情况下是网络异常
          });
      } else {
        // 表单验证不通过，提示信息
        ElMessage({ message: '格式错误，请重试', type: 'error' });
      }
    });
  }

  // 引入防抖函数，包装 reset，防止用户频繁点击按钮，执行多次重置逻辑
  const wrapReset = debounce(reset, 500);

  return {
    formRef,
    wrapReset
  };
}
