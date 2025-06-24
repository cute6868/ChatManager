import { ref } from 'vue';
import useModelsStore from '@/store/models';

export default function useSwitchingMode(enableDrag: () => void) {
  const isFadeOut = ref(false); // 默认不开启淡出动画
  const modelsStore = useModelsStore(); // 使用Pinia存储库

  // 切换到工作模式
  function switchWorkingMode() {
    // 开启淡出动画
    isFadeOut.value = true;

    // 动画快结束时，切换为工作模式
    setTimeout(() => {
      // 切换为工作模式
      modelsStore.isWorkingMode = true;

      // 启动拖拽功能，使用setTimeout(..., 0)是为了确保"show-area"元素挂载成功
      setTimeout(() => {
        enableDrag(); // 启动拖拽
        console.log('enableDrag');
      }, 0);
    }, 300);
  }

  return { isFadeOut, switchWorkingMode };
}
