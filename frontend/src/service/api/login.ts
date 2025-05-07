import request from '../request';

// ==================== 注册业务 ====================

// 获取验证码
export function getVerifyCodeRequest(email: string) {
  return request({
    url: '/api/login/verify-code',
    method: 'post',
    data: {
      email: email
    }
  });
}

// 邮箱登录
export function LoginByEmailRequest(email: string, verifyCode: string) {
  return request({
    url: '/api/login/email',
    method: 'post',
    data: {
      email: email,
      verifyCode: verifyCode
    }
  });
}

// 账号登录
export function LoginByAccountRequest(account: string, password: string) {
  return request({
    url: '/api/login/account',
    method: 'post',
    data: {
      account: account,
      password: password
    }
  });
}
