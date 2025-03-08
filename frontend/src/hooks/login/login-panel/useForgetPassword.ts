import { useRouter } from 'vue-router';
import ROUTE from '@/global/constant/route';

export default function useForgetPassword() {
  const router = useRouter();

  function goToForget() {
    router.push(ROUTE.PATH.FORGET); // 跳转到忘记密码页
  }

  return {
    goToForget
  };
}
