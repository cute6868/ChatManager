import { ref } from 'vue';
import { useRouter } from 'vue-router';
import ROUTE from '@/global/constant/route';
const router = useRouter();

export default function useRedirectFunc(
  emits: (event: 'update:active', ...args: unknown[]) => void
) {
  const flag = ref(false); // 倒计时的显示开关
  const second = ref(3); // 初始化倒计时的秒数

  // 跳转到登录页
  function redirect() {
    emits('update:active'); // 运行“update:active”事件，从而触发父组件的事件，实现自动切换到下一个面板

    // 倒计时逻辑
    flag.value = true;
    const id = setInterval(() => {
      if (second.value <= 1) {
        clearInterval(id); // 清除定时器
        router.push(ROUTE.PATH.LOGIN); // 跳转到登录页面
      }
      second.value--;
    }, 1000);
  }

  return {
    flag,
    second,
    redirect
  };
}
