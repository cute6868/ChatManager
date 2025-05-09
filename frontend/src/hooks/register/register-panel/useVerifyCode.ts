import type { FormDataTypeB, FormDataTypeD } from '@/types';
import { ref } from 'vue';
import debounce from '@/utils/debounce';
import { EMAIL_REGEX } from '@/global/constant/rule';
import { getVerifyCodeRequest } from '@/service/api/register';

export default function useVerifyCode(form: FormDataTypeB | FormDataTypeD) {
  const second = ref(60); // 倒计时的秒数
  const flag = ref(false); // 显示倒计时的开关

  // 编写获取验证码的逻辑
  function getCode() {
    // 判断邮箱格式是否符合规范
    if (!EMAIL_REGEX.test(form.email)) {
      ElMessage({ message: '邮箱格式不正确', type: 'error' });
      return;
    }

    // 如果格式符合规范，则执行获取验证码行为
    getVerifyCodeRequest(form.email)
      .then((res) => {
        const code = res.data.code; // 业务状态码

        if (code === 0) {
          // 发送提示信息, 告诉用户验证码已发送
          ElMessage({ message: res.data.msg, type: 'success' });

          // 显示倒计时
          second.value = 60; // 初始化
          flag.value = true; // 显示
          document.querySelector('.get-code')?.classList.add('disabled-element'); // 禁止操作

          // 进行倒计时
          const timer = setInterval(() => {
            second.value--;
            if (second.value <= 0) {
              flag.value = false; // 隐藏
              document.querySelector('.get-code')?.classList.remove('disabled-element'); // 允许操作
              clearInterval(timer); // 倒计时结束后, 清除定时器
            }
          }, 1000);
        } else {
          // 提示获取验证码失败的原因
          ElMessage({ message: res.data.msg, type: 'error' });
        }
      })
      .catch(() => {
        ElMessage({ message: '网络异常', type: 'error' });
      });
  }

  // 包装获取验证码的函数，实现防抖
  const wrapGetCode = debounce(getCode, 500);

  return {
    flag,
    second,
    wrapGetCode
  };
}
