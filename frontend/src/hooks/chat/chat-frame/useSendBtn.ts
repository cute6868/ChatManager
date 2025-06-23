import { UID } from '@/global/constant/login';
import { chatRequest } from '@/service/api/chat';
import useModelsStore from '@/store/models';
import { localCache } from '@/utils/cache';
import throttle from '@/utils/throttle';
import type { Ref } from 'vue';

export default function useSendBtn(
  inputContent: Ref<string, string>,
  isForbidden: Ref<boolean, boolean>,
  emit: (event: 'firstClick', ...args: any[]) => void
) {
  const uid = localCache.getItem(UID); // 获取用户uid
  const modelsStore = useModelsStore(); // 使用Pinia内存存储库

  // 发送信息
  function sendMessage() {
    if (modelsStore.selectedModels.length === 0) {
      ElMessage.warning('至少选择一个模型');
      return;
    }

    // 第一次点击按钮是"初始模式"，之后才是"工作模式"
    // 如果是初始模式，则不进行任何处理，而是移交给工作模式处理
    if (!modelsStore.isWorkingMode) {
      modelsStore.question = inputContent.value; // 保存问题内容，方便移交数据
      inputContent.value = ''; // 清空输入框
      emit('firstClick'); // 触发父组件的点击事件
      return;
    }

    // 将模型名称转为模型ID
    const modelIds: number[] = [];
    modelsStore.selectedModels.forEach((modelName) => {
      if (modelsStore.modelNameAndIdMap.get(modelName) === 'undefined') {
        ElMessage({ message: '网络不通畅，请重试', type: 'warning', grouping: true });
        return;
      } else {
        modelIds.push(modelsStore.modelNameAndIdMap.get(modelName));
      }
    });

    // 清空上一次的响应数据
    modelsStore.emptyModelsResponse();

    // 覆盖上一次的选中的模型
    modelsStore.lastSelectedModels = modelsStore.selectedModels;

    // 开启模型响应定时，判断是否响应超时
    modelsStore.selectedModels.forEach((modelName) => {
      modelsStore.modelResponseTimer(modelName);
    });

    chatRequest(uid, inputContent.value, modelIds, {
      onData: (data) => {
        // 收到响应数据，停止计时器
        const timerId = modelsStore.modelTimerIds.get(data.model);
        if (timerId !== null) clearTimeout(timerId);

        // 更新pinia中的数据
        modelsStore.modelsResponse.push(data);
      },
      onComplete: () => {
        modelsStore.wasCompleted = true; // 更新pinia中的数据
      },
      onError: (error) => {
        // 一般情况下是网络异常，少数情况是读取数据错误或解析JSON数据错误
        ElMessage({ message: '网络异常', type: 'error', grouping: true }); // 提示网络异常
        console.error(error);
      }
    });

    inputContent.value = ''; // 清空输入框
  }

  // 包装函数，实现节流
  const wrapSendMessage = throttle(sendMessage, 1 * 1000);

  // 支持回车键调用
  function handleKeydown(event: KeyboardEvent) {
    // 在按钮没被禁用的前提下，按下回车键（不含shift）将触发"发送按钮"
    if (isForbidden.value === false && event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      wrapSendMessage();
    }
  }

  return {
    wrapSendMessage,
    handleKeydown
  };
}
