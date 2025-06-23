import { typing } from '@/utils/typing';
import { onUnmounted, ref } from 'vue';

export default function useTypingEffect() {
  const content = [
    '在设置中完成模型配置后，即可使用。',
    '选择您喜欢的模型，与它们对话吧！',
    '按住鼠标右键，左右拖动模型展示区域，可以浏览更多内容。'
  ];
  const placeholderContent = ref(content[0]); // 默认展示的内容
  const displayDuration = 20 * 1000; //  每个句子的展示时长为20秒
  let sentenceIndex = 0; // 当前句子索引
  const speed = 80; // 每个字符的展示间隔80ms
  let timerId1: number | null = null; // 主定时器ID
  let timerId2: number | null = null; // 打字效果定时器ID
  let isVisible = true; // 页面可见性

  // 打字循环
  function startTypingCycle() {
    // 清除可能存在的旧定时器
    if (timerId1 !== null) clearInterval(timerId1);
    if (timerId2 !== null) clearInterval(timerId2);

    timerId1 = setInterval(() => {
      // 只在页面可见时执行内容切换
      if (isVisible) {
        sentenceIndex = sentenceIndex < content.length - 1 ? sentenceIndex + 1 : 0; // 立即准备好下一个句子的索引
        timerId2 = typing(content[sentenceIndex], speed, placeholderContent); // 启动打字效果
      }
    }, displayDuration);
  }

  // 处理事件: 页面可见性发生改变
  function handleVisibilitychange() {
    isVisible = document.visibilityState === 'visible';

    if (isVisible) {
      // 页面重新可见，重置定时器
      if (timerId1 !== null) clearInterval(timerId1);
      if (timerId2 !== null) clearInterval(timerId2);

      // 立即显示当前句子的完整内容
      placeholderContent.value = content[sentenceIndex];

      // 重新启动定时器
      startTypingCycle();
    }
  }

  // 监听页面是否可见
  document.addEventListener('visibilitychange', handleVisibilitychange);

  // 开启打字循环
  startTypingCycle();

  // 组件卸载时完成清理
  onUnmounted(() => {
    if (timerId1 !== null) clearInterval(timerId1);
    if (timerId2 !== null) clearInterval(timerId2);
    document.removeEventListener('visibilitychange', handleVisibilitychange);
  });

  return {
    placeholderContent
  };
}
