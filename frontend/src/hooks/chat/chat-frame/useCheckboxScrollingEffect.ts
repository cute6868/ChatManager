import { ref } from 'vue';

export default function useCheckboxScrollingEffect() {
  // 引用容器（复选框对象）
  const checkboxGroupRef = ref();

  // 滚轮事件处理函数
  function handleWheel(e: WheelEvent) {
    e.preventDefault(); // 阻止默认滚轮行为
    const delta = e.deltaY; // 获取滚轮滚动的距离(正数为向上，负数为向下)
    const scrollStep = 50; // 设置滚动步长

    // 通过 $el 属性访问组件的根 DOM 元素
    if (checkboxGroupRef.value?.$el) {
      (checkboxGroupRef.value.$el as HTMLElement).scrollLeft +=
        delta > 0 ? scrollStep : -scrollStep;
    }
  }

  // 鼠标进入时绑定滚轮事件
  function handleMouseEnter() {
    if (checkboxGroupRef.value?.$el) {
      // 在组件的根 DOM 元素上添加事件监听器
      (checkboxGroupRef.value.$el as HTMLElement).addEventListener('wheel', handleWheel);
    }
  }

  // 鼠标离开时移除滚轮事件
  function handleMouseLeave() {
    if (checkboxGroupRef.value?.$el) {
      (checkboxGroupRef.value.$el as HTMLElement).removeEventListener('wheel', handleWheel);
    }
  }

  return {
    checkboxGroupRef,
    handleMouseEnter,
    handleMouseLeave
  };
}
