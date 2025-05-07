import request from '../request';

// ==================== 注册业务 ====================

// 检测账号可用性
export function checkAccountRequest(account: string) {
  return request({
    url: '/api/register/availability/account',
    method: 'post',
    data: {
      account: account
    }
  });
}

// 检测密码可用性
export function checkPasswordRequest(password: string) {
  return request({
    url: '/api/register/availability/password',
    method: 'post',
    data: {
      password: password
    }
  });
}

// 检测邮箱可用性
export function checkEmailRequest(email: string) {
  return request({
    url: '/api/register/availability/email',
    method: 'post',
    data: {
      email: email
    }
  });
}

// 获取验证码
export function getVerifyCodeRequest(email: string) {
  return request({
    url: '/api/register/verify-code',
    method: 'post',
    data: {
      email: email
    }
  });
}

// 进行注册
export function registerRequest(
  account: string,
  password: string,
  email: string,
  verifyCode: string
) {
  return request({
    url: '/api/register',
    method: 'post',
    data: {
      account: account,
      password: password,
      email: email,
      verifyCode: verifyCode
    }
  });
}
