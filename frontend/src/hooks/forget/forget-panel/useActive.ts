import { ref } from 'vue';

export default function useActive() {
  const active = ref(0); // 初始化激活页为第0页

  // 更新active的数据
  function updateActiveData() {
    if (active.value < 3) active.value++;
  }

  return {
    active,
    updateActiveData
  };
}
