import { defineStore } from 'pinia';
import { resetPasswordRequest } from '@/service/api/update';

const useResetStore = defineStore('reset', () => {
  // 重置密码行为（已完成）
  async function resetPasswordAction(password: string) {
    try {
      const response = await resetPasswordRequest(password);
      return { state: true, content: response.data.msg };
    } catch (error) {
      console.log(error);
      return { state: false, content: '获取验证码失败，网络异常' };
    }
  }

  return {
    resetPasswordAction
  };
});

export default useResetStore;
