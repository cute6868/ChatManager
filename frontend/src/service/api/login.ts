import request from '../request';

// 账号登录请求
export function accountLoginRequest(account: string, password: string) {
  return request({
    url: '/api/login/account',
    method: 'post',
    data: {
      account: account,
      password: password
    }
  });
}

// 邮箱登录请求
export function emailLoginRequest(emailNumber: string, verificationCode: string) {
  return request({
    url: '/api/login/email',
    method: 'post',
    data: {
      emailNumber: emailNumber,
      verificationCode: verificationCode
    }
  });
}
