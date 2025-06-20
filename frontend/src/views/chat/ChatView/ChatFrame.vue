<template>
  <div class="chat-frame">
    <!-- 选择区 -->
    <div class="select-area">
      <el-checkbox-group
        ref="checkboxGroupRef"
        @mouseenter="handleMouseEnter"
        @mouseleave="handleMouseLeave"
        v-model="modelsStore.selectedModels"
        size="small"
        fill="#6eaced"
        :max="maxSelectableQuantity"
      >
        <el-checkbox-button
          v-for="item in support"
          :key="item"
          :value="item"
          :disabled="!available.includes(item)"
        >
          {{ item }}
        </el-checkbox-button>
      </el-checkbox-group>
    </div>
    <!-- 输入区 -->
    <div class="input-area">
      <!-- 输入框 -->
      <div class="text-area" ref="textAreaRef" @keydown="handleKeydown">
        <el-input
          type="textarea"
          :placeholder="placeholderContent"
          v-model="inputContent"
          :autosize="{ minRows: 3, maxRows: 3 }"
          :autofocus="true"
        />
      </div>
      <!-- 功能按钮 -->
      <div class="btn-area">
        <!-- 左边的功能按钮 -->
        <div class="left-btns">
          <el-button class="cancel-btn" @click="empty">
            <el-icon class="delete-icon"><DeleteFilled /></el-icon>
            <span class="delete-text">清空已选</span>
          </el-button>
          <el-button class="upload-btn" @click="clickUploadBtn" :disabled="disableUploadBtn">
            <el-icon class="upload-icon"><Upload /></el-icon>
            <span class="upload-text">备份已选</span>
          </el-button>
          <div
            class="loading-state"
            v-loading="showLoading"
            element-loading-background="rgba(255, 255, 255, 0.7)"
          >
            {{ uploadPrompt }}
          </div>
        </div>
        <!-- 右边的功能按钮 -->
        <el-button
          class="send-btn"
          type="primary"
          :icon="Top"
          @click="wrapSendMessage"
          :disabled="isForbidden"
          circle
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { SELECTED_MODELS, UID } from '@/global/constant/login';
import { chatRequest } from '@/service/api/chat';
import {
  queryAllModelsRequest,
  queryAvailableModelsRequest,
  queryModelAvatarRequest,
  querySelectedModelsRequest
} from '@/service/api/query';
import useModelsStore from '@/store/models';
import { localCache } from '@/utils/cache';
import throttle from '@/utils/throttle';
import { typing } from '@/utils/typing';
import { Top } from '@element-plus/icons-vue';
import { onMounted, onUnmounted, ref, toRaw, watch } from 'vue';

// 获取用户uid
const uid = localCache.getItem(UID);

// 使用Pinia内存存储库
const modelsStore = useModelsStore();

// 通过输入框对象，绑定回车键
function handleKeydown(event: KeyboardEvent) {
  // 按下回车键（不含shift）将触发"发送按钮"
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault();
    wrapSendMessage();
  }
}

// 内容占位符
const content = [
  '在设置中完成模型配置后，即可使用。',
  '选择您喜欢的模型，与它们对话吧！',
  '按住鼠标右键，左右拖动模型展示区域，可以浏览更多内容。'
];
const placeholderContent = ref(content[0]); // 默认展示的内容
const displayDuration = 20 * 1000; //  每个句子的展示时长为20秒
let sentenceIndex = 0; // 当前句子索引
const speed = 80; // 每个字符的展示间隔80ms
let timerId1: number | null = null; // 主定时器ID
let timerId2: number | null = null; // 打字效果定时器ID
let isVisible = true; // 页面可见性

// 开启打字循环
function startTypingCycle() {
  // 清除可能存在的旧定时器
  if (timerId1 !== null) clearInterval(timerId1);
  if (timerId2 !== null) clearInterval(timerId2);

  timerId1 = setInterval(() => {
    // 只在页面可见时执行内容切换
    if (isVisible) {
      sentenceIndex = sentenceIndex < content.length - 1 ? sentenceIndex + 1 : 0; // 立即准备好下一个句子的索引
      timerId2 = typing(content[sentenceIndex], speed, placeholderContent); // 启动打字效果
    }
  }, displayDuration);
}

// 处理事件: 页面可见性发生改变
function handleVisibilitychange() {
  isVisible = document.visibilityState === 'visible';

  if (isVisible) {
    // 页面重新可见，重置定时器
    if (timerId1 !== null) clearInterval(timerId1);
    if (timerId2 !== null) clearInterval(timerId2);

    // 立即显示当前句子的完整内容
    placeholderContent.value = content[sentenceIndex];

    // 重新启动定时器
    startTypingCycle();
  }
}

// 监听页面是否可见
document.addEventListener('visibilitychange', handleVisibilitychange);

// 初始启动
startTypingCycle();

// 组件卸载时完成清理
onUnmounted(() => {
  if (timerId1 !== null) clearInterval(timerId1);
  if (timerId2 !== null) clearInterval(timerId2);
  document.removeEventListener('visibilitychange', handleVisibilitychange);
});

// 输入框的文本内容
const inputContent = ref(modelsStore.question); // 同步pinia中的内容

// 服务器支持的模型
const support = ref<string[]>([]);
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

// 用户可用的模型（用户已配置的模型为可用模型）
const available = ref<string[]>([]);
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

// 清空用户已选择的模型
function empty() {
  modelsStore.selectedModels = [];
}

const maxSelectableQuantity = 10; // 最多可选数量

// 容器的引用（复选框对象）
const checkboxGroupRef = ref();

// 滚轮事件处理函数
function handleWheel(e: WheelEvent) {
  e.preventDefault(); // 阻止默认滚轮行为
  const delta = e.deltaY; // 获取滚轮滚动的距离(正数为向上，负数为向下)
  const scrollStep = 50; // 设置滚动步长

  // 通过 $el 属性访问组件的根 DOM 元素
  if (checkboxGroupRef.value?.$el) {
    (checkboxGroupRef.value.$el as HTMLElement).scrollLeft += delta > 0 ? scrollStep : -scrollStep;
  }
}

// 鼠标进入时绑定滚轮事件
function handleMouseEnter() {
  if (checkboxGroupRef.value?.$el) {
    // 在组件的根 DOM 元素上添加事件监听器
    (checkboxGroupRef.value.$el as HTMLElement).addEventListener('wheel', handleWheel);
  }
}

// 鼠标离开时移除滚轮事件
function handleMouseLeave() {
  if (checkboxGroupRef.value?.$el) {
    (checkboxGroupRef.value.$el as HTMLElement).removeEventListener('wheel', handleWheel);
  }
}

// 禁用发信息按钮
const isForbidden = ref(true);

// 监听输入框的内容，如果内容为空，则禁用发送按钮
const stopWatchingTextarea = watch(inputContent, (newValue) => {
  isForbidden.value = newValue === '' ? true : false;
});

// 组件卸载时销毁监听
onUnmounted(() => {
  stopWatchingTextarea();
});

// 实现子传父
const emit = defineEmits(['firstClick']);

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

// 节流
const wrapSendMessage = throttle(sendMessage, 1 * 1000);
</script>

<style scoped lang="scss">
.chat-frame {
  width: min(94vw, 800px);
  height: max(24vh, 184px);
  background-color: #fff;
}

.select-area {
  display: flex;
  justify-content: center;

  // 复选框父级容器
  .el-checkbox-group {
    display: flex;
    width: 96%;
    overflow-x: auto;
    height: 34px;

    // 滚动条样式
    &::-webkit-scrollbar {
      height: 2px;
    }
    &::-webkit-scrollbar-track {
      background: rgb(200, 200, 200);
      border-radius: 10px;
    }
    &::-webkit-scrollbar-thumb {
      background-color: rgb(75, 135, 191);
      border-radius: 20px;
    }
    &::-webkit-scrollbar-thumb:hover {
      background-color: rgb(13, 72, 131);
      border-radius: 20px;
      cursor: pointer;
    }

    :deep(.el-checkbox-button__inner) {
      border-radius: 10px;
      border: 1px solid rgb(186, 214, 234);
      margin: 0 4px 0;

      // 消除移动端浏览器的默认触摸高亮效果
      -webkit-tap-highlight-color: transparent;
    }
  }
}

// 输入区
.input-area {
  background-color: #fff;
  margin-top: 10px;
  width: 100%;
  height: 80%;

  border-radius: 18px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 0 1px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  padding: 10px 6px 6px;
}

// 文本区
.text-area {
  :deep(.el-textarea__inner) {
    // 去除默认样式
    border: none; // 去掉边框
    outline: none; // 去掉轮廓线
    box-shadow: none; // 去掉阴影
    resize: none; // 去掉拉伸

    &::placeholder {
      user-select: none; // 禁止选择占位符
    }

    // 字体
    font-size: 16px;
    color: #262626;

    // 滚动条
    // 滚动条样式
    &::-webkit-scrollbar {
      width: 3px;
    }
    &::-webkit-scrollbar-thumb {
      background: rgb(200, 200, 200);
      border-radius: 10px;
    }
  }
}

// 按钮区
.btn-area {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 14px 9px 0;

  .left-btns {
    display: flex;
  }

  // 取消已选按钮和上传按钮
  .cancel-btn,
  .upload-btn {
    width: 92px;
    height: 24px;
    border: 1px solid #e0e0e0;

    .delete-icon,
    .upload-icon {
      margin-top: 1px;
      font-size: 14px;
    }

    .delete-text,
    .upload-text {
      font-size: 12px;
      font-weight: 400;
      letter-spacing: 1px;
    }
  }

  // 上传按钮右边的加载样式
  .loading-state {
    height: 20px;
    margin-top: 3px;
    margin-left: 10px;
    user-select: none; // 禁止用户选中文字

    display: flex;
    align-items: center;
    letter-spacing: 1px;
    font-size: 9px;

    :deep(.circular) {
      width: 20px;
    }
  }

  //  发送按钮
  .send-btn {
    font-size: 19px;
  }
}
</style>
