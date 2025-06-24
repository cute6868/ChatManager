import useModelsStore from '@/store/models';
import { watch } from 'vue';
import { onUnmounted } from 'vue';

export default function useModelResponse(parseMarkdown: (text: string) => string) {
  // 使用Pinia存储库
  const modelsStore = useModelsStore();

  // 监听模型响应的数据
  const stopWatchingModelsResponse = watch(
    () => modelsStore.modelsResponse,
    () => {
      // 可以做些事情...
    },
    { deep: true } // 启动深度监听
  );

  // 监听模型响应是否完成
  const stopWatchingCompletionStatus = watch(
    () => modelsStore.wasCompleted,
    (newValue) => {
      if (newValue) {
        modelsStore.wasCompleted = !newValue; // 重置为false，以标记下次响应完成状态
        // 可以做其他事情...
      }
    }
  );
  onUnmounted(() => {
    stopWatchingModelsResponse();
    stopWatchingCompletionStatus();
  });

  // 获取模型响应内容中的推理部分
  function getReasoning(modelName: string) {
    const responseData = modelsStore.modelsResponse.find((item) => item.model === modelName);
    if (typeof responseData?.response !== 'string') {
      return parseMarkdown(responseData?.response.reasoning as string);
    }
    return '';
  }

  // 获取模型响应内容中的答案部分
  function getAnswer(modelName: string) {
    const responseData = modelsStore.modelsResponse.find((item) => item.model === modelName);
    if (typeof responseData?.response !== 'string') {
      return parseMarkdown(responseData?.response.answer as string);
    }
    return responseData?.response;
  }

  // 根据模型响应的实际情况自动决定是否加载动画
  function autoLoading(modelName: string) {
    // 该模型是否参与本次聊天（如果模型没有参与本次聊天，则没有发送请求，更不可能有响应，所以不显示加载动画）
    const inChat = modelsStore.lastSelectedModels.includes(modelName);

    // 没有超时 true (初始状态true) + 没有响应 null = 显示加载动画 (true)
    // 没有超时 true (清除了定时器后所保持true) + 有响应 data = 隐藏加载动画 (false)
    // 超时 false + 没有响应 null = 隐藏加载动画 (false)
    // 超时 false + 有响应 data = 隐藏加载动画 (false)
    const showLoading = modelsStore.modelResponseStatus.get(modelName) && !getAnswer(modelName);

    // 最终是否开始加载动画 = 模型参与了本次聊天 + 同意显示加载动画
    return inChat && showLoading;
  }

  // 展示模型响应超时的提示
  function responseTimeout(modelName: string) {
    // 该模型是否参与本次聊天（如果模型没有参与本次聊天，则没有发送请求，更不可能有响应）
    const inChat = modelsStore.lastSelectedModels.includes(modelName);

    // 模型响应是否正常
    const isNormal = modelsStore.modelResponseStatus.get(modelName);

    return inChat && !isNormal;
  }

  return {
    getReasoning,
    getAnswer,
    autoLoading,
    responseTimeout
  };
}
