import { typing } from '@/utils/typing';
import { ref, onUnmounted } from 'vue';

export default function typingEffect() {
  const text = 'ChatManager，连接无限可能！';
  const speed = 80;
  const displayText = ref('');
  const timerId = typing(text, speed, displayText);

  onUnmounted(() => {
    clearInterval(timerId); // 组件卸载时清除定时器
  });

  return {
    displayText
  };
}
