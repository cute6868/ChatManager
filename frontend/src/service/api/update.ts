import request from '../request';

// ==================== 更新业务 ====================

// 更新昵称
export function updateNicknameRequest(uid: number, nickname: string) {
  return request({
    url: `/api/update/${{ uid }}/nickname`,
    method: 'put',
    data: {
      nickname: nickname
    }
  });
}

// 更新头像
export function updateAvatarRequest(uid: number, avatar: string) {
  return request({
    url: `/api/update/${{ uid }}/avatar`,
    method: 'put',
    data: {
      avatar: avatar
    }
  });
}

// 更新模型配置
export function updateModelConfigRequest(uid: number, modelConfig: string) {
  return request({
    url: `/api/update/${{ uid }}/config`,
    method: 'put',
    data: {
      modelConfig: modelConfig
    }
  });
}

// 更新账号
export function updateAccountRequest(uid: number, account: string, verifyCode: string) {
  return request({
    url: `/api/update/${{ uid }}/account`,
    method: 'put',
    data: {
      account: account,
      verifyCode: verifyCode
    }
  });
}

// 更新邮箱1：输入验证码确认是旧邮箱的主人，同时输入新邮箱以确保给新邮箱发送验证码
export function updateEmailRequest1(uid: number, email: string, verifyCode: string) {
  return request({
    url: `/api/update/${{ uid }}/email/auth`,
    method: 'put',
    data: {
      email: email,
      verifyCode: verifyCode
    }
  });
}

// 更新邮箱2：输入新邮箱以对接刚才的请求，输入验证码实现绑定这个新邮箱
export function updateEmailRequest2(uid: number, email: string, verifyCode: string) {
  return request({
    url: `/api/update/${{ uid }}/email/updt`,
    method: 'put',
    data: {
      email: email,
      verifyCode: verifyCode
    }
  });
}
