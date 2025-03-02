/**
 * 防抖函数
 * @param func 要执行的函数
 * @param delay 延迟时间，单位为毫秒
 * @returns 返回一个包装后的函数，该函数在指定的延迟时间后执行原始函数
 */
export default function debounce<T extends unknown[] = unknown[], R = void>(
  func: (...args: T) => R,
  delay: number
) {
  let timer: number | null = null;

  return (...args: T) => {
    if (timer) clearTimeout(timer);
    timer = setTimeout(() => {
      return func(...args);
    }, delay);
  };
}
