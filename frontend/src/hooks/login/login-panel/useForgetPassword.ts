import { useRouter } from 'vue-router';
import ROUTE from '@/global/constant/route';
const router = useRouter();

export default function useForgetPassword() {
  function goToForget() {
    router.push(ROUTE.PATH.FORGET); // 跳转到忘记密码页
  }

  return {
    goToForget
  };
}
