import { SELECTED_MODELS } from '@/global/constant/login';
import { THROTTLE_TIME } from '@/global/constant/rule';
import { updateSeletedModelsRequest } from '@/service/api/update';
import type { ModelResponseData } from '@/types';
import { localCache } from '@/utils/cache';
import throttle from '@/utils/throttle';
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { onBeforeUnmount } from 'vue';

const useModelsStore = defineStore('models', () => {
  const isWorkingMode = ref<boolean>(false); // 是否为工作模式（只要不是第一次点击发送信息按钮则为工作模式）
  const uploadCompleted = ref<boolean>(false); // 模型选择上传成功与否
  const modelNameAndIdMap = ref(new Map()); // 模型名称映射模型id
  const modelIdAndNameMap = ref(new Map()); // 模型id映射模型名称
  const modelNameAndAvatarMap = ref(new Map()); // 模型名称和头像的映射
  const question = ref<string>(''); // 用户输入的问题（主要用来记录"初始模型"的问题）
  const selectedModels = ref<string[]>([]); // 用户选择的模型
  const lastSelectedModels: string[] = []; // 上一次问题所选择的模型
  const modelResponseStatus = ref(new Map()); // 模型响应的状态是否正常
  const modelTimerIds = ref(new Map()); // 模型响应定时器id
  const modelsResponse = ref<ModelResponseData[]>([]); // 模型返回的响应
  const wasCompleted = ref<boolean>(false); // 是否完成本次对话

  // 清空模型响应
  function emptyModelsResponse() {
    modelsResponse.value = [];
  }

  // 模型响应定时器
  function modelResponseTimer(modelName: string) {
    // 初始化状态，默认模型响应是正常的
    modelResponseStatus.value.set(modelName, true);

    // 设置定时器，设定90秒内没有模型响应，则当模型响应是超时的，模型响应状态设置为不正常
    const timerId = setTimeout(() => {
      modelResponseStatus.value.set(modelName, false);
    }, 90 * 1000);

    // 将定时器添加到Id列表中，如果规定时间内模型作出相应，则可以通过modelTimerIds获取定时器id，从而清除定时器
    modelTimerIds.value.set(modelName, timerId);

    return timerId;
  }

  // 清除模型响应定时器
  function clearAllTimers() {
    modelTimerIds.value.forEach((timerId) => {
      clearTimeout(timerId);
    });
  }

  // 使用onBeforeUnmount监听store实例的销毁
  onBeforeUnmount(() => {
    clearAllTimers(); // 清除所有定时器
  });

  // 上传模型选择数据（包装函数是为了防止同一时间多次上传数据）
  const wrapUploadSelectedModels = throttle(uploadSelectedModels, THROTTLE_TIME);
  function uploadSelectedModels(uid: string) {
    // 获取已选模型的id数组
    const modelNames: string[] = JSON.parse(localCache.getItem(SELECTED_MODELS));
    const modelIds = modelNames.map((modelName) => modelNameAndIdMap.value.get(modelName));
    const jsonString = JSON.stringify(modelIds);

    // 将数据上传到服务器
    updateSeletedModelsRequest(uid, jsonString).then((res) => {
      if (res.data.code === 0) {
        console.info(`{"msg": "模型选择备份成功", "Time": "${new Date().toLocaleString()}"}`);
        uploadCompleted.value = true;
      }
    });
  }

  return {
    isWorkingMode,
    uploadCompleted,
    modelNameAndIdMap,
    modelIdAndNameMap,
    modelNameAndAvatarMap,
    question,
    selectedModels,
    lastSelectedModels,
    modelResponseStatus,
    modelTimerIds,
    modelsResponse,
    wasCompleted,
    emptyModelsResponse,
    modelResponseTimer,
    clearAllTimers,
    wrapUploadSelectedModels
  };
});

export default useModelsStore;
