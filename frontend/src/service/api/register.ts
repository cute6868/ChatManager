import request from '../request';

// 注册请求
export function registerRequest(
  account: string,
  password: string,
  email: string,
  verificationCode: string
) {
  return request({
    url: '/api/register',
    method: 'post',
    data: {
      account: account,
      password: password,
      email: email,
      verificationCode: verificationCode
    }
  });
}
