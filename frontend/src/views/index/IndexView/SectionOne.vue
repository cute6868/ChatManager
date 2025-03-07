<template>
  <div class="section-one">
    <div class="content">
      <!-- 标题 -->
      <h1 id="title">{{ displayText }}</h1>

      <!-- 卡片介绍 -->
      <ul id="introduction">
        <li
          v-for="(item, index) in data"
          :key="index"
          v-show="item.show"
          :class="{ 'fade-in': item.show }"
        >
          <div class="tip">
            <el-icon class="tip-icon"><SuccessFilled style="color: green" /></el-icon>
            <span>{{ item.tip }}</span>
          </div>

          <div class="text">
            <el-icon size="66" class="text-icon"><component :is="item.icon" /></el-icon>
            <div>{{ item.text }}</div>
          </div>
        </li>
      </ul>

      <!-- 体验按钮 -->
      <el-button id="btn" type="primary">立即体验</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
// v-for的内容
import { Link, Sort, Setting, Lock } from '@element-plus/icons-vue';
import { reactive } from 'vue';
const data = reactive([
  { show: false, tip: '免费连接', icon: Link, text: '这是我的内容，我的秘密是我' },
  { show: false, tip: '自由切换', icon: Sort, text: '这是我的内容，我的秘密是我' },
  { show: false, tip: '智能管理', icon: Setting, text: '这是我的内容，我的秘密是我' },
  { show: false, tip: '隐私安全', icon: Lock, text: '这是我的内容，我的秘密是我' }
]);

//  ==========================  打字效果    =================================
import { ref, onMounted } from 'vue';
const fullText = 'ChatManager，连接无限可能！'; // 完整文本
const displayText = ref(''); // 用于显示的文本，初始为空
const typingSpeed = 80; // 每个字符显示的时间间隔（毫秒）

onMounted(() => {
  // 在组件挂载后开始打字效果
  let index = 0;
  const intervalId = setInterval(() => {
    if (index < fullText.length) {
      displayText.value += fullText[index]; // 逐步添加字符到显示文本中
      index++;
    } else {
      clearInterval(intervalId); // 当所有字符都显示完后，清除定时器

      // 在组件挂载后执行逐个显示的逻辑
      data.forEach((item, index) => {
        setTimeout(() => {
          item.show = true;
        }, index * 400); // 每个 <li> 间隔 300 毫秒显示
      });
    }
  }, typingSpeed);
});

// li标签淡入效果=========================================

onMounted(() => {});
</script>

<style scoped lang="scss">
// 基础样式
.section-one {
  width: 100%;
  height: 100%;

  background-image: url('@/assets/img/index/bg0.svg');
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;

  // 圆角
  border-radius: 10px;

  // 内容居中
  display: flex;
  justify-content: center;
  align-items: center;
}

// 内容的基本样式
.content {
  width: 100%;
  padding: 6%;

  // 所有的内容都默认垂直居中
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

// 大标题
#title {
  font-size: min(10vw, 54px);
  font-weight: 600;
  line-height: 2;
  text-align: center;
  margin: 0 0 60px;
}

// 卡片介绍
#introduction {
  width: 100%;

  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;

  // 基本样式
  li {
    width: 200px;
    height: 288px;

    margin: 0 min(2%, 14px) 22px;
    border-radius: 4px;
    background-color: white;

    display: flex;
    flex-direction: column;
    align-items: center;

    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2); // 添加阴影
  }

  // 具体每个元素的样式
  li {
    .tip {
      width: 100%;
      height: 48px;

      // 提示文字和小图标居中对齐
      display: flex;
      justify-content: center;
      align-items: center;

      // 底部渐变颜色
      border-bottom: 1px solid;
      border-image: linear-gradient(
          to right,
          rgba(128, 128, 128, 0.2),
          gray,
          rgba(128, 128, 128, 0.2)
        )
        1;

      // 提示文字的样式
      font-size: 18px;
      font-weight: 300;
      letter-spacing: 2px;
    }

    .tip-icon {
      font-size: 20px; // 小图标尺寸
      vertical-align: middle; // 图标垂直居中
      margin-right: 5px; // 图标与文字之间的间距
    }

    .text {
      flex: 1; // 占据剩余所有的空间
      padding: 18px; // 文本内容与边界之间的间距

      font-size: 14px;
      font-weight: 300;
      letter-spacing: 1px;
      line-height: 1.8;
      text-align: center;

      overflow: hidden;
    }

    .text-icon {
      width: 100%;
      height: 90px;
      margin-bottom: 10px;
    }
  }
}

// 按钮
#btn {
  width: 300px;
  height: 46px;

  letter-spacing: 1px;
  font-weight: 500;

  margin-top: 36px;
}

/* 定义淡入动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 应用动画的类 */
.fade-in {
  animation: fadeIn 3s ease-out forwards;
}
</style>
