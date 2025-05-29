<template>
  <!-- 初始模式时的功能面板 -->
  <div
    class="init-function-panel"
    :class="{ 'fade-out-animation': isFadeOut }"
    v-if="isWorkingMode"
  >
    <!-- 欢迎信息-->
    <h1 class="title">欢迎使用 ChatManager</h1>

    <!-- 聊天框 -->
    <div class="chat"><ChatFrame @send-msg="switchMode" /></div>
  </div>

  <!-- 工作模式时的功能面板 -->
  <div class="work-function-panel" v-if="!isWorkingMode">
    <!-- 展示区 -->
    <div class="show-area">
      <!-- <div v-for="item in [0, 1, 2, 3, 4]" :key="item">我是{{ item }}号模型</div> -->
    </div>

    <!-- 聊天框 -->
    <div class="chat"><ChatFrame /></div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import ChatFrame from './ChatFrame.vue';
import { Key } from '@element-plus/icons-vue';
const isFadeOut = ref(false); // 是否淡出
const isWorkingMode = ref(false); //  是否处于工作模式

function switchMode() {
  // 如果不是工作模式，则切换到工作模式
  if (!isWorkingMode.value) {
    // 1.开启淡出动画
    isFadeOut.value = true;

    // 2.动画快结束时立即切换为工作模式
    setTimeout(() => {
      isWorkingMode.value = true;
    }, 300);
  }
}
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
    margin-top: min(17vh, 130px);
  }

  .chat {
    margin-top: 50px;
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

  .show-area {
    // 固定基本样式（与其他地方配合密切，不要改动）
    width: 100%;
    height: 100%;
    margin-top: 56px;
    margin-bottom: 168px;
  }

  .chat {
    position: fixed;
    bottom: 26px;
  }
}

// 数据输出面板
.work-function-panel > .show-area {
  background-color: pink; // 测试

  display: flex;
  justify-content: center;
  align-items: center;

  div {
  }
}
</style>
