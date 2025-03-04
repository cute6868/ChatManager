import request from '../request';

// 重置密码请求
export function resetPasswordRequest(password: string) {
  return request({
    url: '/api/reset',
    method: 'post',
    data: {
      password: password
    }
  });
}
