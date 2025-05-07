let BASE_URL: string = '';

if (import.meta.env.DEV) {
  // 开发环境
  BASE_URL = 'http://localhost:8080';
} else if (import.meta.env.PROD) {
  // 生产环境
  BASE_URL = 'http://localhost:8080';
} else {
  // 抛出异常
  throw new Error('Unknown environment! Please check your environment configuration.');
}

// 请求超时的毫秒数 (ms)
const TIME_OUT: number = 10 * 1000;

export { BASE_URL, TIME_OUT };
