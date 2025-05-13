import request from '../request';

// ==================== 重置密码业务 ====================

// 身份验证
export function authRequestForResetPwd(email: string) {
  return request({
    url: '/api/reset/authenticate',
    method: 'post',
    data: {
      email: email
    }
  });
}

// 重置密码
export function resetPasswordRequest(password: string, email: string, verifyCode: string) {
  return request({
    url: '/api/reset/password',
    method: 'put',
    data: {
      password: password,
      email: email,
      verifyCode: verifyCode
    }
  });
}
