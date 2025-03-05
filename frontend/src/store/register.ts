import { defineStore } from 'pinia';
import { registerRequest } from '@/service/api/register';
import { localCache } from '@/utils/cache';
import { LOGIN_TOKEN } from '@/global/constant/login';

const useRegisterStore = defineStore('register', () => {
  async function registerAction(
    account: string,
    password: string,
    email: string,
    verificationCode: string
  ) {
    try {
      // 发送网络请求
      const response = await registerRequest(account, password, email, verificationCode);
      if (response.status) {
        // 登录成功
        // 1.保存登录令牌
        localCache.setItem(LOGIN_TOKEN, response.data.token);

        // 2.携带token数据，发送网络请求，请求获取用户个人数据（昵称、头像等）

        // 3.将获取到的用户数据，进行渲染，然后跳转到应用页面，让用户开始使用
        // const router = useRouter();
        // router.push('/chat');

        return '';
      } else {
        return response.data.msg; // 登录失败
      }
    } catch (error) {
      console.log(error);
      return '网络异常';
    }
  }

  return {
    registerAction
  };
});

export default useRegisterStore;
