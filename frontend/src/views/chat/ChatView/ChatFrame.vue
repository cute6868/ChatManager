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
import { Top } from '@element-plus/icons-vue';
import useModelsStore from '@/store/models';

const modelsStore = useModelsStore();
const emit = defineEmits(['firstClick']); // 实现子传父

// 输入框内部的提示文本、打字效果
import useTypingEffect from '@/hooks/chat/chat-frame/useTypingEffect';
const { placeholderContent } = useTypingEffect();

// 输入文本区的功能
import useTextarea from '@/hooks/chat/chat-frame/useTextarea';
const { inputContent, isForbidden } = useTextarea();

// 发送按钮相关的功能
import useSendBtn from '@/hooks/chat/chat-frame/useSendBtn';
const { wrapSendMessage, handleKeydown } = useSendBtn(inputContent, isForbidden, emit);

// 加载支持的模型、可用的模型、已选择的模型
import useLoadingModel from '@/hooks/chat/chat-frame/useLoadingModel';
const { support, available, maxSelectableQuantity, empty } = useLoadingModel(wrapSendMessage);

// 使用上传已选模型的按钮
import useUploadBtn from '@/hooks/chat/chat-frame/useUploadBtn';
const { disableUploadBtn, showLoading, uploadPrompt, clickUploadBtn } = useUploadBtn();

// "模型选择"复选框滚动效果
import useCheckboxScrollingEffect from '@/hooks/chat/chat-frame/useCheckboxScrollingEffect.ts';
const { checkboxGroupRef, handleMouseEnter, handleMouseLeave } = useCheckboxScrollingEffect();
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
