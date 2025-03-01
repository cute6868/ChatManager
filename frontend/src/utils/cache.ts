// 这个文件是对浏览器所提供的 localStorage 和 sessionStorage 的封装
// 提高可用性，同时防止浏览器 API 的更改

enum CacheType {
  LocalStorage,
  SessionStorage
}

class Cache {
  // 成员变量
  private cache: Storage;

  // 构造函数
  constructor(type: CacheType) {
    this.cache = type === CacheType.LocalStorage ? localStorage : sessionStorage;
  }

  // 存数据
  setItem(key: string, value: boolean | number | string | object) {
    this.cache.setItem(key, JSON.stringify(value));
  }

  // 取数据
  getItem(key: string) {
    const res = this.cache.getItem(key);
    if (res === null) return null;
    return JSON.parse(res);
  }

  // 删数据
  removeItem(key: string) {
    this.cache.removeItem(key);
  }

  // 清空所有数据
  clear() {
    this.cache.clear();
  }
}

export const localCache = new Cache(CacheType.LocalStorage);
export const sessionCache = new Cache(CacheType.SessionStorage);
