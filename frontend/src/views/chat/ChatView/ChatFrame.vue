<template>
  <div class="chat-frame">
    <!-- 选择区 -->
    <div class="select-area">
      <el-checkbox-group
        ref="checkboxGroupRef"
        @mouseenter="handleMouseEnter"
        @mouseleave="handleMouseLeave"
        v-model="selected"
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
import { THROTTLE_TIME } from '@/global/constant/rule';
import { chatRequest } from '@/service/api/chat';
import { queryAllModelsRequest, queryAvailableModelsRequest } from '@/service/api/query';
import { localCache } from '@/utils/cache';
import throttle from '@/utils/throttle';
import { typing } from '@/utils/typing';
import { Top } from '@element-plus/icons-vue';
import { onUnmounted, ref, watch } from 'vue';

// 获取用户uid
const uid = localCache.getItem(UID);

// 内容占位符（3秒切换内容）
const content = ['温馨提示：在设置中配置模型后才能正常使用。', '选择您喜欢的模型，与它们对话吧！'];
const placeholderContent = ref(content[0]); // 默认展示的内容
const displayDuration = 20 * 1000; //  每个句子的展示时长为20秒
let sentenceIndex = 0; // 当前句子索引
const speed = 80; // 每个字符的展示间隔为80ms
let timerId2: number;
const timerId1 = setInterval(() => {
  // 1.立即准备好下一个句子
  sentenceIndex = sentenceIndex < content.length - 1 ? sentenceIndex + 1 : 0;
  const sentence = content[sentenceIndex];

  timerId2 = typing(sentence, speed, placeholderContent);
}, displayDuration);
onUnmounted(() => {
  // 清除定时器
  clearInterval(timerId1);
  clearInterval(timerId2); // 防止中途突然切换页面，导致没有清除定时器
});

// 定义：模型名称和ID的映射关系
const nameAndIdMap = new Map();

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

const selected = ref([]); // 用户已经选择的模型
const maxSelectableQuantity = 10; // 最多可选数量

// 输入框的文本内容
const textarea = ref('');

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
  selected.value = [];
}

// 禁用发信息按钮
const isForbidden = ref(true);

// 监听输入框的内容，如果内容为空，则禁用发送按钮
const stopWatching = watch(textarea, (newValue) => {
  isForbidden.value = newValue === '' ? true : false;
});

// 组件卸载时销毁监听
onUnmounted(() => {
  stopWatching();
});

// 实现子传父
import { defineEmits } from 'vue';
const emit = defineEmits(['sendMsg', 'dataUpdate', 'dataEnd']);
// 发送信息
function sendMessage() {
  if (selected.value.length === 0) {
    ElMessage.warning('至少选择一个模型');
    return;
  }

  // 发送按钮点击事件，触发父组件的点击事件
  emit('sendMsg');

  // 将模型名称转为模型ID
  const modelIds = selected.value.map((modelName) => {
    return nameAndIdMap.get(modelName);
  });

  chatRequest(uid, textarea.value, modelIds, {
    onData: (data) => {
      emit('dataUpdate', data); // 触发父组件的数据更新事件，并传递data数据给父组件
    },
    onComplete: () => {
      emit('dataEnd'); // 触发父组件的数据结束事件
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
const wrapSendMessage = throttle(sendMessage, THROTTLE_TIME);
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
