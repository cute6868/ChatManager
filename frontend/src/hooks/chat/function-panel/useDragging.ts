import { onUnmounted } from 'vue';
import type { Ref } from 'vue';

export default function useDragging(showArea: Ref<HTMLElement | null>) {
  // 实现按住鼠标右键可以拖动模型正文的功能

  let isDragging = false; // 是否处于拖动状态
  let startX = 0; //  鼠标按下的X坐标
  let scrollLeft = 0; // 初始滚动位置

  // 启动拖拽：按住鼠标右键可以左右拖动展示面板以展示更多内容
  function enableDrag() {
    if (!showArea.value) return;

    // 开启各种事件监听
    showArea.value.addEventListener('mousedown', handleMouseDown); // 监听show-area元素上的鼠标按下事件
    document.addEventListener('mousemove', hanldeMouseMove); // 监听鼠标移动事件（因为可能从show-area移动到其他元素）
    document.addEventListener('mouseup', handleMouseUp); // 监听鼠标抬起事件（同理）
    showArea.value.addEventListener('mouseleave', handleMouseLeave); //  监听show-area元素上的鼠标离开事件
    showArea.value.addEventListener('contextmenu', preventDefault); // 阻止整个容器的右键菜单
  }

  onUnmounted(() => {
    if (!showArea.value) return;

    // 关闭各种事件监听
    showArea.value.removeEventListener('mousedown', handleMouseDown);
    document.removeEventListener('mousemove', hanldeMouseMove);
    document.removeEventListener('mouseup', handleMouseUp);
    showArea.value.removeEventListener('mouseleave', handleMouseLeave);
    showArea.value.removeEventListener('contextmenu', preventDefault);
  });

  // 处理鼠标按下事件
  function handleMouseDown(event: MouseEvent) {
    if (!showArea.value) return;
    if (event.button !== 2) return; // 不是鼠标右键的不处理
    event.preventDefault(); // 阻止默认行为，防止右键菜单弹出
    startX = event.clientX; // 记录当前鼠标的X坐标
    scrollLeft = showArea.value.scrollLeft; // 记录当前滚动条的X坐标
    isDragging = true;
    showArea.value.style.cursor = 'grabbing'; // 设置鼠标样式为"抓取"
  }

  // 处理鼠标移动事件
  function hanldeMouseMove(event: MouseEvent) {
    if (!isDragging || !showArea.value) return;
    const moveX = event.clientX - startX; // 计算鼠标移动的距离
    showArea.value.scrollLeft = scrollLeft - moveX; // 更新滚动条位置
  }

  // 处理鼠标释放事件
  function handleMouseUp(event: MouseEvent) {
    if (!showArea.value) return;
    if (event.button !== 2) return; //  不是鼠标右键不处理
    isDragging = false;
    showArea.value.style.cursor = ''; // 恢复默认光标
  }

  // 处理鼠标离开事件
  function handleMouseLeave() {
    if (!showArea.value) return;
    isDragging = false; // 更新拖动标志
    showArea.value.style.cursor = ''; // 恢复默认光标
  }

  // 阻止默认事件
  function preventDefault(event: MouseEvent) {
    event.preventDefault();
  }

  return {
    enableDrag
  };
}
