import { defineStore } from 'pinia';
import { getEmailVerificationCodeRequest } from '@/service/api/email';

const useEmailStore = defineStore('email', () => {
  // 获取邮箱验证码行为（已完成）
  async function getEmailVerificationCodeAction(email: string) {
    try {
      // 发送网络请求，网络异常时，无法收到 response，所以要捕获异常
      const response = await getEmailVerificationCodeRequest(email);
      return { state: true, content: response.data.msg };
    } catch (error) {
      console.log(error);
      return { state: false, content: '获取验证码失败，网络异常' };
    }
  }

  return {
    getEmailVerificationCodeAction
  };
});

export default useEmailStore;
