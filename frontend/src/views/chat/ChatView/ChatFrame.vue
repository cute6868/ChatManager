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
        fill="#51a4dd"
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
      <div class="text-area">
        <el-input
          type="textarea"
          :placeholder="placeholderContent"
          v-model="textarea"
          :autosize="{ minRows: 3, maxRows: 3 }"
          ref="textareaRef"
        />
      </div>
      <!-- 功能按钮 -->
      <div class="btn-area">
        <el-button class="cancel-btn" @click="empty">
          <el-icon class="delete-icon"><DeleteFilled /></el-icon>
          <span class="delete-text">清空已选</span>
        </el-button>
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
import { UID } from '@/global/constant/login';
import { chatRequest } from '@/service/api/chat';
import { queryAllModelsRequest, queryAvailableModelsRequest } from '@/service/api/query';
import useModelsStore from '@/store/models';
import { localCache } from '@/utils/cache';
import throttle from '@/utils/throttle';
import { typing } from '@/utils/typing';
import { Top } from '@element-plus/icons-vue';
import { onMounted, onUnmounted, ref, watch } from 'vue';

// 获取用户uid
const uid = localCache.getItem(UID);

// 使用Pinia内存存储库
const modelsStore = useModelsStore();

// 引用输入框对象
const textareaRef = ref();
onMounted(() => {
  textareaRef.value.focus(); // 当页面挂载完毕，自动聚焦输入框
});

// 内容占位符（3秒切换内容）
const content = ['温馨提示：在设置中配置模型后才能正常使用。', '选择您喜欢的模型，与它们对话吧！'];
const placeholderContent = ref(content[0]); // 默认展示的内容
const displayDuration = 20 * 1000; //  每个句子的展示时长为20秒
let sentenceIndex = 0; // 当前句子索引
const speed = 80; // 每个字符的展示间隔为80ms
let timerId2: number;
const timerId1 = setInterval(() => {
  // 立即准备好下一个句子
  sentenceIndex = sentenceIndex < content.length - 1 ? sentenceIndex + 1 : 0;
  const sentence = content[sentenceIndex];

  // 启动打字效果
  timerId2 = typing(sentence, speed, placeholderContent);
}, displayDuration);
onUnmounted(() => {
  // 清除定时器
  clearInterval(timerId1);
  clearInterval(timerId2); // 防止中途突然切换页面，导致没有清除定时器
});

function handleKeydown(event: KeyboardEvent) {
  // 按下回车键触发"发送按钮"（不含shift）
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault();
    wrapSendMessage();
    return;
  }
}
onMounted(() => {
  document.addEventListener('keydown', handleKeydown);
});
onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown);
});

// 定义：模型名称和ID的映射关系
const nameAndIdMap = new Map();

// 输入框的文本内容
const textarea = ref(modelsStore.question); // 同步pinia中的内容

// 定义：服务器支持的模型
const support = ref<string[]>([]);
queryAllModelsRequest()
  .then((res) => {
    const code = res.data.code;
    if (code === 0) {
      const modelArr = res.data.data;
      modelArr.forEach((model: { name: string; modelId: number }) => {
        support.value.push(model.name); // 更新：服务器支持的模型
        nameAndIdMap.set(model.name, model.modelId); // 更新：模型名称和ID的映射关系
      });
      // 加载完nameAndIdMap映射关系后，检测pinia里面是否有用户的提问
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

// 定义：用户可用的模型（用户已配置的模型为可用模型）
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
      ElMessage({ message: '服务器异常', type: 'error', grouping: true });
    }
  })
  .catch(() => {
    ElMessage({ message: '网络异常', type: 'error', grouping: true });
  });

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

// 清空用户已选择的模型
function empty() {
  modelsStore.selectedModels = [];
}

// 禁用发信息按钮
const isForbidden = ref(true);

// 监听输入框的内容，如果内容为空，则禁用发送按钮
const stopWatchingTextarea = watch(textarea, (newValue) => {
  isForbidden.value = newValue === '' ? true : false;
});

// 组件卸载时销毁监听
onUnmounted(() => {
  stopWatchingTextarea();
});

// 实现子传父
const emit = defineEmits(['firstClick']);

//
// 发送信息
function sendMessage() {
  if (modelsStore.selectedModels.length === 0) {
    ElMessage.warning('至少选择一个模型');
    return;
  }

  // 第一次点击按钮是"初始模式"，之后才是"工作模式"
  // 如果是初始模式，则不进行任何处理，而是移交给工作模式处理
  if (!modelsStore.isWorkingMode) {
    modelsStore.question = textarea.value; // 保存问题内容，方便移交数据
    textarea.value = ''; // 清空输入框
    emit('firstClick'); // 触发父组件的点击事件
    return;
  }

  // 将模型名称转为模型ID
  const modelIds: number[] = [];
  modelsStore.selectedModels.forEach((modelName) => {
    if (nameAndIdMap.get(modelName) === 'undefined') {
      ElMessage({ message: '网络不通畅，请重试', type: 'warning', grouping: true });
      return;
    } else {
      modelIds.push(nameAndIdMap.get(modelName));
    }
  });

  // 清空上一次的响应数据
  modelsStore.emptyModelsResponse();

  chatRequest(uid, textarea.value, modelIds, {
    onData: (data) => {
      modelsStore.modelsResponse.push(data); // 更新pinia中的数据
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

  textarea.value = ''; // 清空输入框
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

  margin: 14px 10px 0;

  // 取消已选按钮
  .cancel-btn {
    height: 26px;
    border: 1px solid #e0e0e0;

    &:hover {
      font-weight: 500;
    }

    .delete-icon {
      margin-top: 1px;
      font-size: 14px;
    }

    .delete-text {
      font-size: 12px;
      font-weight: 400;
      letter-spacing: 1px;
    }
  }

  //  发送按钮
  .send-btn {
    font-size: 19px;
  }
}
</style>
