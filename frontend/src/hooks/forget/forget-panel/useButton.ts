import { ref } from 'vue';
import type { Ref } from 'vue';
import debounce from '@/utils/debounce';

export default function useButton(active: Ref<number>) {
  // 获取子组件的实例
  const VI = ref();
  const RP = ref();
  const LA = ref();

  // 点击按钮的操作
  function clickHandler() {
    if (active.value === 0) VI.value?.check();
    else if (active.value === 1) RP.value?.reset();
    else if (active.value === 2) LA.value?.redirect();
  }

  // 包装点击操作，实现防抖
  const wrapClickHandler = debounce(clickHandler, 500);

  return {
    VI,
    RP,
    LA,
    wrapClickHandler
  };
}
