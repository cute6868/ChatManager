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
          'show-panel-single-model': modelsStore.selectedModels.length < 2,
          'show-panel-multi-models': modelsStore.selectedModels.length >= 2
        }"
        v-for="modelName in modelsStore.selectedModels"
        :key="modelName"
      >
        <!-- 模型外观内容 -->
        <div class="model-appearance">
          <!-- 头像 -->
          <el-divider>
            <el-avatar
              :size="32"
              @error="reloadAvatar(modelName)"
              :src="modelsStore.modelNameAndAvatarMap.get(modelName)"
            />
          </el-divider>

          <!-- 名称 -->
          <div class="model-name">{{ modelName }}</div>
        </div>
        <!-- 如果模型响应没有超时，则正常显示 -->
        <div
          class="model-response"
          v-loading="autoLoading(modelName)"
          v-if="!responseTimeout(modelName)"
          element-loading-text="正在思考......"
        >
          <!-- 模型思考内容 -->
          <el-collapse style="margin-bottom: 16px" v-if="getReasoning(modelName)">
            <el-collapse-item title="深度思考" name="1">
              <div
                class="markdown-body"
                v-html="getReasoning(modelName)"
                style="opacity: 0.5"
              ></div>
            </el-collapse-item>
          </el-collapse>
          <!-- 模型回答正文 -->
          <div
            class="markdown-body"
            v-if="getAnswer(modelName)"
            v-html="getAnswer(modelName)"
          ></div>
        </div>
        <!-- 如果模型响应超时，则提示超时信息 -->
        <div v-if="responseTimeout(modelName)" style="margin-top: 26px">模型响应超时，请重试！</div>
      </div>
    </div>

    <!-- 聊天框 -->
    <div class="chat"><ChatFrame /></div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import ChatFrame from './ChatFrame.vue';
import useModelsStore from '@/store/models';
import { queryModelAvatarRequest } from '@/service/api/query';

import 'katex/dist/katex.min.css'; // 引入Katex样式
import 'highlight.js/styles/github.min.css'; // 引入高亮样式
import 'github-markdown-css/github-markdown.css'; // 引入 GitHub Markdown 样式

// 使用Pinia存储库
const modelsStore = useModelsStore();

// 加载头像出错时，重新加载函数
function reloadAvatar(modelName: string) {
  queryModelAvatarRequest(modelsStore.modelNameAndIdMap.get(modelName))
    .then((res) => {
      if (res.data.code === 0) {
        modelsStore.modelNameAndAvatarMap.set(modelName, res.data.data);
      }
    })
    .catch(() => {
      ElMessage({ message: '网络不通畅，请重试', type: 'warning', grouping: true });
    });
}

// 标题与打字效果
import useTitle from '@/hooks/chat/function-panel/useTitle';
const { title } = useTitle();

// 解析Markdown格式的文本
import useMarkdownHandler from '@/hooks/chat/function-panel/useMarkdownHandler';
const { parseMarkdown } = useMarkdownHandler();

// 模型响应相关的数据和功能
import useModelResponse from '@/hooks/chat/function-panel/useModelResponse';
const { getReasoning, getAnswer, autoLoading, responseTimeout } = useModelResponse(parseMarkdown);

// 按住鼠标右键可以拖拽模型正文的功能
import useDragging from '@/hooks/chat/function-panel/useDragging';
const showArea = ref<HTMLElement | null>(null); // 引用元素对象
const { enableDrag } = useDragging(showArea);

// 切换模式
import useSwitchingMode from '@/hooks/chat/function-panel/useSwitchingMode';
const { isFadeOut, switchWorkingMode } = useSwitchingMode(enableDrag);
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
    user-select: none; // 禁止用户选择文本
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
  overflow: hidden; // 解决子孙元素过长超越父类形成溢出的问题
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
  .model-appearance {
    // 模型名称与下文内容之间的间距
    margin-bottom: 10px;

    // 模型名称
    .model-name {
      margin-top: 5px;
      font-size: 11px;
    }
  }

  .model-response {
    font-size: 16px;
    line-height: 1.6;
    font-weight: 400;
    color: rgba(0, 0, 0, 0.85);
    color-scheme: light; // 这个属性是为了适配暗黑模式
  }

  &:deep(.el-loading-spinner) {
    user-select: none; // 禁止复制
  }

  &:deep(.el-loading-mask) {
    z-index: 1;
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
    height: 52px;
    display: flex;
    flex-direction: column;
    align-items: center;

    // 分割线（模型图标）
    :deep(.el-divider--horizontal) {
      width: 94%; // 分割线宽度
      margin-top: 17px;
      margin-bottom: 16px;
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

// 模型头像的背景色设置为透明
:deep(.el-avatar) {
  background-color: transparent;
  border: 1px solid rgba(0, 0, 0, 0.1);

  .model-name {
  }
}

// 穿透作用域，调整Markdown样式
::v-deep(.markdown-body) {
  // 覆盖Markdown-GitHub样式的最大宽度限制
  max-width: 100% !important;

  // 代码块样式优化
  .hljs {
    border-radius: 8px;
    margin: 1rem 0;
    padding: 1rem;
    overflow-x: auto;
  }
}
</style>
