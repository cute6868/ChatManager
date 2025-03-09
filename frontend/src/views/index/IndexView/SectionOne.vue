<template>
  <div class="section-one">
    <div class="content">
      <!-- 标题 -->
      <h1 id="title">{{ displayText }}</h1>

      <!-- 卡片介绍 -->
      <ul id="card" v-show="isShow" :class="{ 'fade-in-top-to-bottom': isShow }">
        <li v-for="(item, index) in data" :key="index">
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
      <el-button
        v-show="isShow"
        :class="{ 'fade-in-right-to-left': isShow }"
        id="btn"
        type="primary"
        @click="gotoChat"
      >
        立即体验
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
// 打字效果相关的数据和方法
import useTypingEffect from '@/hooks/index/section-one/useTypingEffect.ts';
const { displayText } = useTypingEffect();

// 卡片相关的数据和方法
import useCard from '@/hooks/index/section-one/useCard.ts';
const { isShow, data } = useCard();

// 按钮相关的数据和方法
import useButton from '@/hooks/index/section-one/useButton.ts';
const { gotoChat } = useButton();
</script>

<style scoped lang="scss">
// 基础样式
.section-one {
  width: 100%;
  margin-bottom: 60px;

  // 背景图片
  background-image: url('@/assets/img/index/bg0.svg');
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;

  // 内容居中
  display: flex;
  justify-content: center;
  align-items: center;
}

// 内容的基本样式
.content {
  width: 100%;
  margin-top: clamp(58px, 10vh, 76px); // 留位置给导航栏浮现，因为导航栏是动态高度，所以它也动态变化

  // 所有的内容都默认垂直居中
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

// 大标题
#title {
  font-size: max(3.6vw, 36px);
  font-weight: 600;
  line-height: 2;
  text-align: center;
  margin: 66px 0 80px;
}

// 卡片介绍
#card {
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

    &:hover {
      transform: scale(1.05);
      box-shadow: 0 0 20px rgba(0, 0, 0, 0.3);
      transition:
        transform 0.3s ease-in-out,
        box-shadow 0.3s ease-in-out;
    }
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

  margin: 36px 0 42px;
}

// 定义从上到下的淡入动画
@keyframes fadeInFromTopToBottom {
  from {
    opacity: 0; // 初始状态为透明
    transform: translateY(-46px); // 初始状态在上面40像素
  }
  to {
    opacity: 1; // 最终状态不透明
    transform: translateY(0); // 最终状态在下面0像素
  }
}

// 定义从右到左的淡入动画
@keyframes fadeInFromRightToLeft {
  from {
    opacity: 0; // 初始状态为透明
    transform: translateX(46px); // 初始状态在右边40像素
  }
  to {
    opacity: 1; // 最终状态不透明
    transform: translateX(0); // 最终状态在右边0像素
  }
}

// 应用动画
.fade-in-top-to-bottom {
  animation: fadeInFromTopToBottom 2s ease-out forwards; // 应用淡入动画，持续2秒，淡入效果，向前播放
}

.fade-in-right-to-left {
  animation: fadeInFromRightToLeft 3s ease-out forwards; // 应用淡入动画，持续2秒，淡入效果，向前播放
}
</style>
