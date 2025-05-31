import type { Ref } from 'vue';

/**
 *
 * @param text 想要展示的文本内容
 * @param speed 每个字符显示的时间间隔（毫秒）
 * @param refObj 用来展示数据的ref对象
 * @returns timerId 定时器ID（可用于打字未完成时，手动取消定时器）
 */

export function typing(text: string, speed: number, refObj: Ref): number {
  let index = 0;
  refObj.value = '';
  const timerId = setInterval(() => {
    if (index < text.length) {
      refObj.value += text[index];
      index++;
    } else {
      clearInterval(timerId); // 当所有字符都显示完后，自动清除定时器（注意：如果打字效果没有完成，需要手动清除）
    }
  }, speed);
  return timerId; // 返回定时器ID，以便后续手动清除
}
