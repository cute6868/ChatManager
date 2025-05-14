/**
 * 节流函数
 * @param func 要执行的函数
 * @param cooldown 冷却时间，单位为毫秒
 * @returns 返回一个包装后的函数，该函数会立即执行，然后在冷却时间内无法再次执行
 */
export default function throttle<T extends unknown[] = unknown[], R = void>(
  func: (...args: T) => R,
  cooldown: number
) {
  let lastExecTime = 0;
  let result: R | undefined;

  return (...args: T) => {
    const now = Date.now();

    // 检查是否已经过了冷却时间
    if (now - lastExecTime >= cooldown) {
      lastExecTime = now;
      result = func(...args);
    }

    return result;
  };
}
