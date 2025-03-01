import { defineStore } from 'pinia';

const useVerificationStore = defineStore('verification', () => {
  // 获取验证码行为
  function getVerificationCodeAction() {
    // 发送网络请求
  }

  return {
    getVerificationCodeAction
  };
});
export default useVerificationStore;
