import request from '../request';

// ==================== 登出业务 ====================

// 退出登录
export default function logoutRequest(uid: number) {
  return request({
    url: `/api/logout/${{ uid }}`,
    method: 'post'
  });
}
