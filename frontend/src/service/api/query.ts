import request from '../request';

// ==================== 查询业务 ====================

// 查询简介
export function queryProfileRequest(uid: string) {
  return request({
    url: `/api/query/${uid}/profile`,
    method: 'get'
  });
}

// 查询记录
export function queryRecordRequest(uid: string) {
  return request({
    url: `/api/query/${{ uid }}/record`,
    method: 'get'
  });
}

// 查询联系信息
export function queryContactRequest(uid: string) {
  return request({
    url: `/api/query/${{ uid }}/contact`,
    method: 'get'
  });
}

// 查询模型配置
export function queryModelConfigRequest(uid: string) {
  return request({
    url: `/api/query/${{ uid }}/config`,
    method: 'get'
  });
}
