import request from '../request';

// ==================== 注销业务 ====================

// 注销账号
export function deactivateAccount(uid: number, verifyCode: string) {
  return request({
    url: `/api/deactivate/${{ uid }}`,
    method: 'delete',
    data: {
      verifyCode: verifyCode
    }
  });
}
