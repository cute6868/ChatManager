import { onUnmounted, ref, watch } from 'vue';
import useModelsStore from '@/store/models';

export default function useTextarea() {
  // 使用Pinia内存存储库
  const modelsStore = useModelsStore();

  // 输入框的文本内容，同步pinia中的内容
  const inputContent = ref(modelsStore.question);

  // 禁用发信息按钮
  const isForbidden = ref(true);

  // 监听输入框的内容，如果内容为空，则禁用发送按钮
  const stopWatchingTextarea = watch(inputContent, (newValue) => {
    isForbidden.value = newValue.trim() === '' ? true : false;
  });

  // 组件卸载时销毁监听
  onUnmounted(() => {
    stopWatchingTextarea();
  });

  return { inputContent, isForbidden };
}
