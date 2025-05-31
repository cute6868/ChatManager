import type { ModelResponseData } from '@/types';
import { defineStore } from 'pinia';
import { ref } from 'vue';

const useModelsStore = defineStore('models', () => {
  const isWorkingMode = ref<boolean>(false); // 是否为工作模式（只要不是第一次点击发送信息按钮则为工作模式）
  const question = ref<string>(''); // 用户输入的问题
  const selectedModels = ref<string[]>([]); // 用户选择的模型
  const modelsResponse = ref<ModelResponseData[]>([]); // 模型返回的响应
  const wasCompleted = ref<boolean>(false); // 完成当前问题的回答

  function emptyModelsResponse() {
    modelsResponse.value = [];
  }

  return {
    question,
    selectedModels,
    isWorkingMode,
    modelsResponse,
    wasCompleted,
    emptyModelsResponse
  };
});

export default useModelsStore;
