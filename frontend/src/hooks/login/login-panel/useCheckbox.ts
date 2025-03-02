import { ref } from 'vue';
import { localCache } from '@/utils/cache';

export default function useCheckbox() {
  // 局部常量，用来定义复选框状态在本地存储中的键名
  const CHECKBOX_STATE = 'remembered-password';

  // 优先从本地存储中获取复选框的状态，如果获取不到，则默认设置为 false
  const checkboxState = ref(localCache.getItem(CHECKBOX_STATE) ?? false);
  // 将"记住密码"的复选框的勾选状态保存到本地中
  function saveCheckboxState() {
    localCache.setItem(CHECKBOX_STATE, checkboxState.value);
  }

  return {
    checkboxState,
    CHECKBOX_STATE,
    saveCheckboxState
  };
}
