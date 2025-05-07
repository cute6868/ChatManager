import request from '../request';

// ==================== 聊天业务 ====================

// 发起聊天
export function chatRequest(uid: string, question: string, modelIds: number[]) {
  return request({
    url: `/api/chat/${{ uid }}`,
    method: 'post',
    data: {
      question: question,
      modelIds: modelIds
    }
  });
}
