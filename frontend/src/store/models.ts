import { SELECTED_MODELS } from '@/global/constant/login';
import { THROTTLE_TIME } from '@/global/constant/rule';
import { updateSeletedModelsRequest } from '@/service/api/update';
import type { ModelResponseData } from '@/types';
import { localCache } from '@/utils/cache';
import throttle from '@/utils/throttle';
import { defineStore } from 'pinia';
import { ref } from 'vue';

const useModelsStore = defineStore('models', () => {
  const isWorkingMode = ref<boolean>(false); // 是否为工作模式（只要不是第一次点击发送信息按钮则为工作模式）
  const uploadCompleted = ref<boolean>(false); // 模型选择上传成功与否
  const modelNameAndIdMap = ref(new Map()); // 模型名称映射模型id
  const modelIdAndNameMap = ref(new Map()); // 模型id映射模型名称
  const modelNameAndAvatarMap = ref(new Map()); // 模型名称和头像的映射
  const question = ref<string>(''); // 用户输入的问题（主要用来记录"初始模型"的问题）
  const selectedModels = ref<string[]>([]); // 用户选择的模型
  const lastSelectedModels: string[] = []; // 上一次问题所选择的模型
  const modelsResponse = ref<ModelResponseData[]>([]); // 模型返回的响应
  const wasCompleted = ref<boolean>(false); // 是否完成本次对话

  // 清空模型响应
  function emptyModelsResponse() {
    modelsResponse.value = [];
  }

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
    modelsResponse,
    wasCompleted,
    emptyModelsResponse,
    wrapUploadSelectedModels
  };
});

export default useModelsStore;
