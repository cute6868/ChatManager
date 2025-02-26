import request from '../request';

// 账号登录请求
export function accountLoginRequest(account: string, password: string) {
  return request({
    url: '/api/login',
    method: 'post',
    data: {
      account: account,
      password: password
    }
  });
}

// 手机登录请求
export function cellphoneLoginRequest(cellphoneNumber: string, verificationCode: string) {
  return request({
    url: '/api/login',
    method: 'post',
    data: {
      cellphoneNumber: cellphoneNumber,
      verificationCode: verificationCode
    }
  });
}
