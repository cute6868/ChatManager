import { Link, Sort, Setting, Lock } from '@element-plus/icons-vue';
import { onMounted, ref } from 'vue';

export default function useCard() {
  // 控制卡片和按钮的普通显示以及动画效果
  const isShow = ref(false);

  // 卡片数据
  const data = [
    {
      tip: '免费连接',
      icon: Link,
      text: '使用个人第三方服务接口，连接完全免费'
    },
    {
      tip: '自由切换',
      icon: Sort,
      text: '配置多个人工智能，可随意切换使用，聊天更自由'
    },
    {
      tip: '智能管理',
      icon: Setting,
      text: '提供智能化管理服务，定制专属聊天体验，释放您的时间与精力'
    },
    {
      tip: '隐私安全',
      icon: Lock,
      text: '严格保护隐私，不泄露信息，仅将数据用于打造智能管家'
    }
  ];

  // 显示卡片和按钮，同时显示其动画效果
  onMounted(() => {
    isShow.value = true;
  });

  return { isShow, data };
}
