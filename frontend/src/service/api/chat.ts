import { localCache } from '@/utils/cache';
import { LOGIN_TOKEN } from '@/global/constant/login';

// 由于后端的POST请求和SSE混用架构，导致前端无法使用单纯的axios和SSE进行请求
// 所以这样使用 fetch + 流式响应，模拟了SSE的功能
// 下面函数返回值是一个Promise对象，在请求成功时不返回具体数据（数据通过回调处理）

export function chatRequest(
  uid: string,
  question: string,
  modelIds: number[],
  {
    onData, // 数据处理回调函数
    onComplete, // 请求完成回调函数
    onError // 错误处理回调函数
  }: {
    onData: (data: any) => void;
    onComplete: () => void;
    onError: (error: any) => void;
  }
) {
  const token = localCache.getItem(LOGIN_TOKEN); // 获取token令牌
  const url = `/api/chat/${uid}`; // 构建请求地址

  // 发送POST请求到后端API
  return fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify({ question, modelIds })
  })
    .then(async (response) => {
      // 不是200的响应状态码，主动抛出错误
      if (!response.ok) throw new Error(`HTTP Error: ${response.status}`);

      // 当响应体为空时
      if (!response.body) {
        onComplete?.(); // 调用回调函数，通知已完成
        return Promise.resolve(); // 返回一个resolved的Promise
      }

      // 当状态码为200而且响应体不为空时，进入读取响应体的逻辑
      const reader = response.body.getReader(); // 获取响应体的可读流
      const decoder = new TextDecoder(); // 用于将二进制数据解码为字符串
      let buffer = ''; // 缓冲区，用于处理可能被分割在多个数据块中的完整行
      let isCompleted = false; // 标记是否结束

      // 定义异步读取函数（核心逻辑）
      async function read() {
        try {
          // 循环读取流中的每个数据块
          while (!isCompleted) {
            // 读取下一个数据块（done表示流是否结束，value是数据）
            const { done, value } = await reader.read();

            // 流结束处理
            if (done) {
              isCompleted = true;
              onComplete?.(); // 调用回调函数，通知已完成
              break;
            }

            // 解码数据并拼接缓冲区
            // 将二进制数据解码为字符串并添加到缓冲区
            buffer += decoder.decode(value, { stream: true });

            // 按行分割SSE数据（兼容换行符：\n 或 \r\n）
            const lines = buffer.split(/\r?\n/);
            buffer = lines.pop() || ''; // 最后一行可能不完整，保留到下一次处理

            // 处理每一行数据
            lines.forEach((line) => {
              line = line.trim();
              // 识别SSE格式的数据行
              if (line.startsWith('data:')) {
                const result = line.slice('data:'.length).trim(); // 去除"data:"前缀
                if (result) {
                  try {
                    const data = JSON.parse(result); // 解析JSON数据
                    onData?.(data); // 调用回调函数，处理数据
                  } catch (error: any) {
                    onError?.(new Error(`Json Parsing failure: ${error.message}`));
                  }
                }
              }
            });
          }
        } catch (error) {
          isCompleted = true;
          onError?.(error); // 捕获读取过程中的错误
        }
      }

      // 启动读取流
      // 注意：这里只是启动异步读取，不会阻塞后续代码
      read(); // 持续读取后端返回的流式响应（模拟 SSE），当后端关闭连接时（done: true），循环终止

      // 返回一个resolved的Promise
      // 注意：此时数据是通过回调（handleData）处理的，而不是通过Promise返回
      return Promise.resolve(); // 返回一个resolved的Promise
    })
    .catch((error) => {
      onError?.(error); // 传递请求阶段的错误（主要是自己抛出的异常，原因是网络异常）
      return Promise.reject(error);
    });
}
