import request from '../request';

// 发送一个注册请求
export function reqRegister() {
  return request({
    url: '/api/register',
    method: 'post',
    data: {
      username: 'admin',
      password: '123456'
    }
  });
}

// 发送一个登录请求
export function reqLogin() {
  return request({
    url: '/api/login',
    method: 'post',
    data: {
      username: 'admin',
      password: '123456'
    }
  });
}
