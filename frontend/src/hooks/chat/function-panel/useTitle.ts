import { ref, onMounted, onUnmounted } from 'vue';
import { typing } from '@/utils/typing';

export default function useTitle() {
  const titleContent = '欢迎使用 ChatManager'; // 标题内容
  const title = ref(titleContent); // 用于展示标题
  let timerId: number; // 打字定时器ID

  onMounted(() => {
    // 启动标题的打字效果
    timerId = typing(titleContent, 18, title);
  });

  onUnmounted(() => {
    // 停止标题的打字效果
    clearTimeout(timerId);
  });

  return {
    title
  };
}
