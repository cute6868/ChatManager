import { defineStore } from 'pinia';
import { accountLoginRequest } from '@/service/api/login';
import { localCache } from '@/utils/cache';
import { UID, LOGIN_TOKEN } from '@/global/constant';

const useLoginStore = defineStore('login', () => {
  // 刷新后，优先读取本地存储中的数据，如果没有，则设置为空字符串
  let uid = localCache.getItem(UID) ?? '';
  let token = localCache.getItem(LOGIN_TOKEN) ?? '';

  // 账号登录行为
  async function accountLoginAction(account: string, password: string) {
    // 发送网络请求
    const response = await accountLoginRequest(account, password);

    // 处理响应结果
    if (response.status === 200) {
      // 登录成功
      // 1.将登录令牌、其他必要数据，保存到 Pinia 管理的内存中，方便管理
      uid = response.data.uid;
      token = response.data.token;

      // 2.为了防止刷新后数据消失，将数据保存到本地存储中
      localCache.setItem(UID, uid);
      localCache.setItem(LOGIN_TOKEN, token);

      // 3.跳转到应用页面，让用户开始使用
    } else {
      // 登录失败，返回错误信息
      return response.data.msg;
    }
  }

  // 邮箱登录行为
  async function emailLoginAction(email: string, verificationCode: string) {
    return { email, verificationCode };
  }

  return {
    uid,
    token,
    accountLoginAction,
    emailLoginAction
  };
});

export default useLoginStore;
