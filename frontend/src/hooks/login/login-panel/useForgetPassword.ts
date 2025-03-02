import ROUTE from '@/global/constant/route';
import { useRouter } from 'vue-router';

export default function useForgetPassword() {
  const router = useRouter();

  function goToForget() {
    router.push(ROUTE.PATH.FORGET); // 跳转到忘记密码页
  }

  return {
    goToForget
  };
}
