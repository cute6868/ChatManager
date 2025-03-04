import type { FormDataTypeB } from '@/types';
import { ref } from 'vue';
import debounce from '@/utils/debounce';
import useEmailStore from '@/store/email';
const emailStore = useEmailStore();

export default function useVerificationCode(form: FormDataTypeB) {
  const second = ref(60); // 倒计时的秒数
  const flag = ref(false); // 显示倒计时的开关

  // 编写获取验证码的逻辑
  async function getCode() {
    // 判断邮箱格式是否符合规范
    const regex = /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
    if (!regex.test(form.email)) {
      ElMessage({ message: '邮箱格式不正确', type: 'error' });
      return;
    }

    // 如果格式符合规范，则执行获取验证码行为
    const result = await emailStore.getEmailVerificationCodeAction(form.email);
    if (result.state) {
      ElMessage({ message: result.content, type: 'success' }); // 获取验证码成功

      // 显示倒计时60秒，并且期间不得再申请验证码
      second.value = 60; // 初始化
      flag.value = true; // 显示
      document.querySelector('.get-code')?.classList.add('disabled-element'); // 禁止操作

      // 进行倒计时
      const timer = setInterval(() => {
        second.value--;
        if (second.value <= 0) {
          flag.value = false; // 隐藏
          document.querySelector('.get-code')?.classList.remove('disabled-element'); // 允许操作
          clearInterval(timer); // 清除定时器
        }
      }, 1000);
    } else {
      ElMessage({ message: result.content, type: 'error' }); // 获取验证码失败
    }
  }

  // 包装获取验证码的函数，实现防抖
  const wrapGetCode = debounce(getCode, 500);

  return {
    flag,
    second,
    wrapGetCode
  };
}
