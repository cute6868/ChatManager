import axios from 'axios';
import { BASE_URL, TIME_OUT } from '../config';
import { localCache } from '@/utils/cache';

// 创建一个axios对象，用来维护一份配置信息
const axiosInstance = axios.create({
  baseURL: BASE_URL,
  timeout: TIME_OUT
});

// 请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    // 在发送请求之前，先尝试从浏览器数据中获取 token 并设置到请求头中
    const token = localCache.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },

  (error) => {
    // 对请求错误做些什么

    return Promise.reject(error);
  }
);

// 响应拦截器
axiosInstance.interceptors.response.use(
  (response) => {
    // 2xx 范围内的状态码都会触发该函数
    // 对响应数据做点什么

    return response;
  },
  (error) => {
    // 超出 2xx 范围的状态码都会触发该函数
    // 对响应错误做点什么

    return Promise.reject(error);
  }
);

export default axiosInstance;
