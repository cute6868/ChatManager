import { ref, onMounted, onUnmounted } from 'vue';

export default function useEllipsis() {
  const isEllipsis = ref(false); // 关闭导航栏右侧item的省略

  function handleResize() {
    // 当宽度低于576px时，开启省略，否则关闭省略
    if (window.innerWidth <= 576) isEllipsis.value = true;
    else isEllipsis.value = false;
  }

  onMounted(() => {
    window.addEventListener('resize', handleResize); // 设置事件监听器
    handleResize(); // 初始化时设置一次
  });

  onUnmounted(() => {
    window.removeEventListener('resize', handleResize); // 移除事件监听器
  });

  return {
    isEllipsis
  };
}
