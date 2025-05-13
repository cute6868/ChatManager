import request from '../request';

// ==================== 验证业务 ====================

// 更新账号验证
export function authRequestForUpdtAccount(uid: number) {
  return request({
    url: `/api/authenticate/${{ uid }}/update/account`,
    method: 'post'
  });
}

// 更新邮箱验证
export function authRequestForUpdtEmail(uid: number) {
  return request({
    url: `/api/authenticate/${{ uid }}/update/email`,
    method: 'post'
  });
}

// 注销账号验证
export function authRequestForDeactivateAccount(uid: number) {
  return request({
    url: `/api/authenticate/${{ uid }}/deactivate`,
    method: 'post'
  });
}
