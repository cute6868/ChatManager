import { SELECTED_MODELS, UID } from '@/global/constant/login';
import {
  queryAllModelsRequest,
  queryAvailableModelsRequest,
  queryModelAvatarRequest,
  querySelectedModelsRequest
} from '@/service/api/query';
import useModelsStore from '@/store/models';
import { localCache } from '@/utils/cache';
import { onMounted, ref } from 'vue';

export default function useLoadingModel(wrapSendMessage: (...args: []) => void | undefined) {
  const uid = localCache.getItem(UID); // 获取用户uid
  const modelsStore = useModelsStore(); // 使用Pinia内存存储库

  // 服务器支持的模型
  const support = ref<string[]>([]);

  // 用户可用的模型（用户已配置的模型为可用模型）
  const available = ref<string[]>([]);

  // 最多可选模型的数量
  const maxSelectableQuantity = 10;

  // 请求获取服务器支持的模型
  queryAllModelsRequest()
    .then((res) => {
      const code = res.data.code;
      if (code === 0) {
        const modelArr = res.data.data;
        modelArr.forEach((model: { name: string; modelId: number }) => {
          support.value.push(model.name); // 更新服务器支持的模型
          modelsStore.modelNameAndIdMap.set(model.name, model.modelId); // 更新模型名称和ID的映射关系
          modelsStore.modelIdAndNameMap.set(model.modelId, model.name); // 更新模型ID和名称的映射关系
          // 更新模型名称和头像的映射关系
          queryModelAvatarRequest(model.modelId).then((res) => {
            if (res.data.code === 0) {
              modelsStore.modelNameAndAvatarMap.set(model.name, res.data.data);
            }
          });
        });

        // 加载完modelNameAndIdMap映射关系后，检测pinia里面是否有用户的提问
        // 如果有用户的提问，说明当前是由"初始模式"刚转为"工作模式"，需要完成任务的交接！（初始模式是不干活的，工作模式要把初始模式的提问完成）
        if (modelsStore.question !== '') {
          modelsStore.question = ''; // 清空在"初始模式"时候所遗留的提问内容
          wrapSendMessage(); // 触发函数，完成"初始模式"时候没有完成的提问请求
        }
      } else {
        ElMessage({ message: '服务器异常', type: 'error', grouping: true });
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
    });

  // 请求获取用户可用的模型
  queryAvailableModelsRequest(uid)
    .then((res) => {
      const code = res.data.code;
      if (code === 0) {
        const modelArr = res.data.data;
        modelArr.forEach((model: { name: string; modelId: number }) => {
          available.value.push(model.name); // 更新：用户可用的模型
        });
      } else {
        ElMessage({
          message: '没有检测到模型配置',
          type: 'warning',
          grouping: true
        });
      }
    })
    .catch(() => {
      ElMessage({ message: '网络异常', type: 'error', grouping: true });
    });

  // 用户已经选择的模型
  onMounted(() => {
    // 组件挂载成功后，优先从本地存储中获取模型选择
    // 如果本地存储不存在数据，则从服务器中获取
    const jsonString = localCache.getItem(SELECTED_MODELS);
    if (jsonString) {
      modelsStore.selectedModels = JSON.parse(jsonString); // 直接解析即可使用
    } else {
      // 请求获取用户已经选择的模型
      querySelectedModelsRequest(uid)
        .then((res) => {
          if (res.data.code === 0) {
            const modelIds: number[] = JSON.parse(res.data.data);
            // 解析后需要将模型id数组转为模型名称数组，才可以使用
            modelsStore.selectedModels = modelIds.map((modelId) => {
              return modelsStore.modelIdAndNameMap.get(modelId);
            });
          }
        })
        .catch(() => {
          ElMessage({ message: '网络异常', type: 'error', grouping: true });
        });
    }
  });

  // 清空用户已选择的模型
  function empty() {
    modelsStore.selectedModels = [];
  }

  return {
    support,
    available,
    maxSelectableQuantity,
    empty
  };
}
