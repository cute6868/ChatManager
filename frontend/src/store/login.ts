import { defineStore } from 'pinia';
import type { AxiosResponse } from 'axios';
import { localCache } from '@/utils/cache';
import { LOGIN_TOKEN, ACCOUNT, PASSWORD } from '@/global/constant/login';
import { accountLoginRequest, emailLoginRequest } from '@/service/api/login';

const useLoginStore = defineStore('login', () => {
  // 优先读取本地存储中的登录令牌，如果没有，则设置为空字符串
  let token = localCache.getItem(LOGIN_TOKEN) ?? '';

  // 登录成功后的，其他准备工作（未完成）
  function work(response: AxiosResponse) {
    // 1.将登录令牌保存到 Pinia 管理的内存中，以方便读取和修改
    token = response.data.token;

    // 2.为了防止刷新后 Pinia 内存中的数据消失，将数据备份到本地存储中
    localCache.setItem(LOGIN_TOKEN, token);

    // 3.携带token数据，发送网络请求，请求获取用户个人数据（昵称、头像等）

    // 4.将获取到的用户数据，进行渲染，然后跳转到应用页面，让用户开始使用
    // const router = useRouter();
    // router.push('/chat');
  }

  // 账号登录行为（已完成）
  async function accountLoginAction(account: string, password: string, state: boolean) {
    try {
      // 发送网络请求，如果网络异常，response 将没有值，同时会抛出异常，所以要捕获异常
      const response = await accountLoginRequest(account, password);

      // 如果网络正常，response 就有值
      // 然后根据 response.status 进行判断，用户是否登录成功
      if (response.status === 200) {
        // 登录成功
        // 根据“记住密码”的勾选状态，保存或删除本地存储中的登录信息
        if (state) {
          localCache.setItem(ACCOUNT, account);
          localCache.setItem(PASSWORD, password);
        } else {
          localCache.removeItem(ACCOUNT);
          localCache.removeItem(PASSWORD);
        }
        // 执行其他准备工作
        work(response);

        return '';
      } else {
        // 登录失败
        return response.data.msg;
      }
    } catch (error) {
      // 网络异常
      console.log(error);
      return '网络异常';
    }
  }

  // 邮箱登录行为（已完成）
  async function emailLoginAction(email: string, verificationCode: string) {
    try {
      const response = await emailLoginRequest(email, verificationCode);
      if (response.status === 200) {
        // 登录成功
        work(response);
      } else {
        // 登录失败
        return response.data.msg;
      }
    } catch (error) {
      console.log(error);
      return '网络异常';
    }
  }

  return {
    token,
    accountLoginAction,
    emailLoginAction
  };
});

export default useLoginStore;
