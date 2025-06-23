import { SELECTED_MODELS, UID } from '@/global/constant/login';
import { localCache } from '@/utils/cache';
import { onUnmounted, ref, toRaw, watch } from 'vue';
import useModelsStore from '@/store/models';

export default function useUploadBtn() {
  const uid = localCache.getItem(UID); // 获取用户uid
  const modelsStore = useModelsStore(); // 使用Pinia内存存储库

  const disableUploadBtn = ref(false); // 禁止点击上传（备份）按钮
  const showLoading = ref(false); // 展示加载效果
  const uploadPrompt = ref(''); // 上传状态提示词

  let timerId3: number | null = null;
  watch(
    () => modelsStore.selectedModels,
    () => {
      // 获取选中的模型（普通数组对象）
      const arr = toRaw(modelsStore.selectedModels).filter((item) => item !== null);

      // 将选中的模型保存到本地存储
      const jsonString = JSON.stringify(arr); // 将对象转换为JSON字符串
      localCache.setItem(SELECTED_MODELS, jsonString); //  保存字符串

      // 当发生变化时，30秒后才将本地数据保存到服务器，避免重复发生请求
      if (timerId3) clearTimeout(timerId3);
      timerId3 = setTimeout(() => {
        disableUploadBtn.value = true; // 禁用按钮
        showLoading.value = true; // 开启加载效果
        uploadPrompt.value = '自动备份中...';
        modelsStore.wrapUploadSelectedModels(uid); // 上传数据到服务器
        timerId3 = null;
      }, 30 * 1000);
    },
    { deep: true }
  );
  onUnmounted(() => {
    if (timerId3 !== null) clearTimeout(timerId3);
  });

  // 上传已选择的模型
  function clickUploadBtn() {
    disableUploadBtn.value = true; // 禁用按钮
    uploadPrompt.value = '正在备份...';
    modelsStore.wrapUploadSelectedModels(uid); // 上传数据到服务器
  }

  // 监听上传是否成功
  let timerId4: number | null = null;
  watch(
    () => modelsStore.uploadCompleted,
    (newValue) => {
      if (newValue) {
        // 上传成功
        // 1.重置状态标志
        modelsStore.uploadCompleted = false;
        // 2.在三秒后重置数据，让所有的上传效果失效，恢复正常
        if (timerId4) clearTimeout(timerId4);
        timerId4 = setTimeout(() => {
          disableUploadBtn.value = false;
          showLoading.value = false;
          uploadPrompt.value = '备份成功';
        }, 3 * 1000);
      }
    }
  );
  onUnmounted(() => {
    if (timerId4 !== null) clearTimeout(timerId4);
  });

  let timerId5: number | null = null;
  watch(uploadPrompt, (newValue) => {
    if (newValue === '备份成功') {
      // 一秒后不显示内容
      if (timerId5) clearTimeout(timerId5);
      timerId5 = setTimeout(() => {
        uploadPrompt.value = '';
      }, 1000);
    }
  });
  onUnmounted(() => {
    if (timerId5 !== null) clearTimeout(timerId5);
  });

  return {
    disableUploadBtn,
    showLoading,
    uploadPrompt,
    clickUploadBtn
  };
}
