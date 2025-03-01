import request from '../request';

// 获取邮箱验证码请求
export function getEmailVerificationCodeRequest(email: string) {
  return request({
    url: '/api/email',
    method: 'post',
    data: {
      email: email
    }
  });
}
