import { onMounted, onUnmounted } from 'vue';

export default function useScroll() {
  let preScrollTop = 0; // 记录上一次滚动条滚动的高度

  function handleScroll() {
    const currentScrollTop = window.pageYOffset; // 获取当前的滚动高度
    const navbar = document.querySelector('.navigation-bar') as HTMLElement | null; // 获取导航栏元素

    if (navbar) {
      if (currentScrollTop > preScrollTop) {
        // 说明向下滚动了
        navbar.style.top = '-80px'; // 向下滚动时隐藏导航栏
      } else {
        // 说明向上滚动了
        navbar.style.top = '0'; // 向上滚动时显示导航栏
      }
      preScrollTop = currentScrollTop; // 保存当前的滚动高度
    }
  }

  onMounted(() => {
    window.addEventListener('scroll', handleScroll); // 设置事件监听器
  });

  onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll); // 移除事件监听器
  });

  return {};
}
