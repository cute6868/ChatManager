<template>
  <div class="section-two">
    <div class="content">
      <ul>
        <li v-for="(item, index) in data" :key="index">
          <img :src="`/src/assets/img/index/case${index}.jpg`" alt="案例图片" />
          <span>{{ item }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
// 要展示的文本数据
// 一段文本对应一个case图片，比如：
// '你好，世界' -> case0.jpg
// '你好，人类' -> case1.jpg
const data = [
  '通过 ChatManager，您可以在不同的AI聊天对话中自由切换。除了切换，您还可以同时提问，这样可以大大提升您的工作效率。',
  '此外，ChatManager 会根据您访问的 AI 数据和行为分析信息，并智能地为您推荐感兴趣的内容或与您感兴趣的 AI 相关的信息。',
  '无需担心，我们会严格保护您的个人隐私。我们不会泄露您的数据，只是将这些数据用于您个性化的训练，请放心使用。'
];
</script>

<style scoped lang="scss">
.section-two {
  width: 100%;
  margin-bottom: 60px;

  // 背景图片
  background-image: url('@/assets/img/index/bg1.svg');
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

  display: flex;
  justify-content: center;
  align-items: center;
}

// 列表布局
li {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;

  margin: 40px 0;
}

// 图片样式
img {
  width: 360px;
  height: 230px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
}

// 文字样式
span {
  width: 360px;
  margin: 40px; // 设置文字与图片的上下左右间隔

  font-family: Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;
  font-size: 20px;
  font-weight: 300;
}

// 当屏幕宽度小于768px时，将文字左右两侧的间隔取消，只保留上下间隔
@media screen and (max-width: 768px) {
  span {
    margin-left: 0;
    margin-right: 0;
  }
}

// 鼠标悬停时图片翻转效果
li:hover img {
  transform: rotateY(360deg);
  transition: transform 1.5s ease-in-out;
}

// 鼠标悬停时文字大小和颜色效果
li:hover span {
  scale: 1.04;
  transition: scale 0.5s ease-in-out;
}
</style>
