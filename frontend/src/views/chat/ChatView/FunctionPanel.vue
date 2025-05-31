<template>
  <!-- 初始模式时的功能面板 -->
  <div
    class="init-function-panel"
    :class="{ 'fade-out-animation': isFadeOut }"
    v-if="!modelsStore.isWorkingMode"
  >
    <!-- 欢迎信息-->
    <h1 class="title">{{ title }}</h1>

    <!-- 聊天框 -->
    <div class="chat"><ChatFrame @first-click="switchWorkingMode" /></div>
  </div>

  <!-- 工作模式时的功能面板 -->
  <div class="work-function-panel" v-if="modelsStore.isWorkingMode">
    <!-- 展示区 -->
    <div class="show-area" ref="showArea">
      <div
        class="show-panel"
        :class="{
          'show-panel-single-model': !isMultipleModels,
          'show-panel-multi-models': isMultipleModels
        }"
        v-for="data in modelsStore.modelsResponse"
        :key="data.model"
      >
        <!-- 模型外观内容 -->
        <div class="model-appearance">
          <!-- 头像 -->
          <el-divider>
            <el-avatar :size="32" :src="modelAvatarURL" />
          </el-divider>

          <!-- 名称 -->
          <div class="model-name">{{ data.model }}</div>
        </div>
        <!-- 模型响应内容 -->
        <div class="model-response" v-loading="!data">
          {{ data.response }}
        </div>
      </div>
    </div>

    <!-- 聊天框 -->
    <div class="chat"><ChatFrame /></div>
  </div>
</template>

<script setup lang="ts">
import { watch } from 'vue';
import ChatFrame from './ChatFrame.vue';
import useModelsStore from '@/store/models';
import { ref, onMounted, onUnmounted } from 'vue';
import { typing } from '@/utils/typing';

// 使用Pinia存储库
const modelsStore = useModelsStore();
const isFadeOut = ref(false); // 是否淡出
const isMultipleModels = ref(false); // 是否多个模型响应

const titleContent = '欢迎使用 ChatManager';
const title = ref('欢迎使用 ChatManager'); // 用于展示标题
let timerId: number;
onMounted(() => {
  timerId = typing(titleContent, 18, title);
});
onUnmounted(() => {
  clearTimeout(timerId);
});

// 切换到工作模式
function switchWorkingMode() {
  // 开启淡出动画
  isFadeOut.value = true;

  // 动画快结束时，切换为工作模式
  setTimeout(() => {
    // 切换为工作模式
    modelsStore.isWorkingMode = true;

    // 启动拖拽功能，使用setTimeout(..., 0)是为了确保"show-area"元素挂载成功
    setTimeout(() => {
      enableDrag(); // 启动拖拽
    }, 0);
  }, 300);
}

// ================== 鼠标右键按下拖动功能（start） ==================
const showArea = ref<HTMLElement | null>(null); // 引用元素对象
let isDragging = false; // 是否处于拖动状态
let startX = 0; //  鼠标按下的X坐标
let scrollLeft = 0; // 初始滚动位置

// 启动拖拽：按住鼠标右键可以左右拖动展示面板以展示更多内容
function enableDrag() {
  if (!showArea.value) {
    console.log('showArea.value is null');
    return;
  }

  // 开启各种事件监听
  showArea.value.addEventListener('mousedown', handleMouseDown); // 监听show-area元素上的鼠标按下事件
  document.addEventListener('mousemove', hanldeMouseMove); // 监听鼠标移动事件（因为可能从show-area移动到其他元素）
  document.addEventListener('mouseup', handleMouseUp); // 监听鼠标抬起事件（同理）
  showArea.value.addEventListener('mouseleave', handleMouseLeave); //  监听show-area元素上的鼠标离开事件
  showArea.value.addEventListener('contextmenu', preventDefault); // 阻止整个容器的右键菜单
}

onUnmounted(() => {
  if (!showArea.value) return;

  // 关闭各种事件监听
  showArea.value.removeEventListener('mousedown', handleMouseDown);
  document.removeEventListener('mousemove', hanldeMouseMove);
  document.removeEventListener('mouseup', handleMouseUp);
  showArea.value.removeEventListener('mouseleave', handleMouseLeave);
  showArea.value.removeEventListener('contextmenu', preventDefault);
});

// 处理鼠标按下事件
function handleMouseDown(event: MouseEvent) {
  if (!showArea.value) return;
  if (event.button !== 2) return; // 不是鼠标右键的不处理
  event.preventDefault(); // 阻止默认行为，防止右键菜单弹出
  startX = event.clientX; // 记录当前鼠标的X坐标
  scrollLeft = showArea.value.scrollLeft; // 记录当前滚动条的X坐标
  isDragging = true;
  showArea.value.style.cursor = 'grabbing'; // 设置鼠标样式为"抓取"
}

// 处理鼠标移动事件
function hanldeMouseMove(event: MouseEvent) {
  if (!isDragging || !showArea.value) return;
  const moveX = event.clientX - startX; // 计算鼠标移动的距离
  showArea.value.scrollLeft = scrollLeft - moveX; // 更新滚动条位置
}

// 处理鼠标释放事件
function handleMouseUp(event: MouseEvent) {
  if (!showArea.value) return;
  if (event.button !== 2) return; //  不是鼠标右键不处理
  isDragging = false;
  showArea.value.style.cursor = ''; // 恢复默认光标
}

// 处理鼠标离开事件
function handleMouseLeave() {
  if (!showArea.value) return;
  isDragging = false; // 更新拖动标志
  showArea.value.style.cursor = ''; // 恢复默认光标
}

// 阻止默认事件
function preventDefault(event: MouseEvent) {
  event.preventDefault();
}

// ================== 鼠标右键按下拖动功能（end） ==================

// 监听模型响应的数据
const stopWatchingModelsResponse = watch(
  () => modelsStore.modelsResponse,
  () => {
    // 根据模型响应的数量来判断是否为多个模型响应，从而启动不同的CSS样式
    isMultipleModels.value = modelsStore.modelsResponse.length > 1 ? true : false;
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

// 模型头像地址
const modelAvatarURL = ref(
  'https://lf-flow-web-cdn.doubao.com/obj/flow-doubao/doubao/logo-doubao-overflow.png'
);
</script>

<style scoped lang="scss">
// 动画类：当应用这个类，元素会向上移动并淡出
.fade-out-animation {
  transform: translateY(-100vh); // 向上移动自身高度的距离
  opacity: 0; // 完全透明
}

// 初始样式
.init-function-panel {
  width: 100%;
  height: 100%;

  display: flex;
  flex-direction: column;
  align-items: center;

  .title {
    font-size: min(5.9vw, 32px);
    letter-spacing: 1px;
    margin-top: min(25vh, 200px);
  }

  .chat {
    margin-top: 40px;
  }

  // 过渡效果
  // transform属性变化时应用0.8秒的过渡，缓动函数为ease
  // opacity属性变化时应用0.5秒的过渡，缓动函数为ease
  transition:
    transform 0.7s ease,
    opacity 0.4s ease;
}

// 工作样式
.work-function-panel {
  width: 100%;
  height: 100%;
  position: absolute;

  display: flex;
  flex-direction: column;
  align-items: center;

  // 显示模型面板的区域
  .show-area {
    // 固定基本样式（与其他地方配合密切，不要改动）
    width: 100%;
    height: 100%;
    margin-top: 56px;
    margin-bottom: 160px;
  }

  .chat {
    position: fixed;
    bottom: 26px;
  }
}

// 显示区域
.work-function-panel > .show-area {
  display: flex;
  justify-content: safe center; // 重点：即想要元素居中，又想要元素滚动时不被裁切 ，则使用safe center

  // 启动滚动
  overflow-x: auto;

  // 隐藏滚掉条
  &::-webkit-scrollbar {
    display: none;
  }
}

// 显示区域（滚动条）
@media (min-width: 769px) {
  .work-function-panel > .show-area {
    width: 88%; // 当屏幕够宽时，两边留白
  }
}

// 模型展示面板
// 公共样式：不论是单个模型还是多个模型，都使用这个样式
.show-panel {
  .model-response {
    font-size: 16px;
    font-weight: 400;
    line-height: 28px;
    color: rgba(0, 0, 0, 0.85);
    color-scheme: light; // 这个属性是为了适配暗黑模式
  }
}

// 模型展示面板
// 单个模型时的样式（默认）
.show-panel-single-model {
  width: min(92vw, 790px);

  // 模型外观
  .model-appearance {
    display: none; // 单个模型时无需展示模型外观
  }

  // 模型响应
  .model-response {
    box-sizing: border-box;
    padding: 20px;
  }
}

// 模型展示面板
// 多个模型时的样式
.show-panel-multi-models {
  width: max(36vw, 360px);
  flex-shrink: 0; // 关闭了flex挤压，使得可以横向滚动
  margin-left: 16px;
  margin-right: 16px;

  // 模型外观
  .model-appearance {
    display: flex;
    flex-direction: column;
    align-items: center;

    // 分割线（模型图标）
    :deep(.el-divider--horizontal) {
      width: 94%; // 分割线宽度
      margin-top: 17px;
      margin-bottom: 16px;
    }

    // 模型名称
    .model-name {
      font-size: 11px;
      margin-bottom: 5px; // 模型名称与下文之间的间距
    }
  }

  // 模型响应
  .model-response {
    height: 87%;
    margin-left: 18px;
    margin-right: 11px;
    padding-right: 10px;

    // 自动开启滚动
    overflow: auto;

    // 滚动条样式
    &::-webkit-scrollbar {
      width: 3px;
    }
    &::-webkit-scrollbar-track {
      border-radius: 16px;
    }
    &::-webkit-scrollbar-thumb {
      background-color: #eee;
    }
  }
}
</style>
