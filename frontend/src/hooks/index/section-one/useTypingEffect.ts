import { ref, onMounted } from 'vue';

export default function typingEffect() {
  const fullText = 'ChatManager，连接无限可能！';
  const displayText = ref(''); // 用于显示的文本，初始为空
  const typingSpeed = 80; // 每个字符显示的时间间隔（毫秒）

  // 在组件挂载后开始打字效果
  onMounted(() => {
    let index = 0;
    const intervalId = setInterval(() => {
      if (index < fullText.length) {
        displayText.value += fullText[index]; // 逐步添加字符到显示文本中
        index++;
      } else {
        clearInterval(intervalId); // 当所有字符都显示完后，清除定时器
      }
    }, typingSpeed);
  });

  return {
    displayText
  };
}
