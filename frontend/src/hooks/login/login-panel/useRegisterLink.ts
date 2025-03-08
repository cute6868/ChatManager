import { useRouter } from 'vue-router';
import ROUTE from '@/global/constant/route';
const router = useRouter();

export default function useRegisterLink() {
  function goToRegister() {
    router.push(ROUTE.PATH.REGISTER); // 跳转到注册页
  }

  return {
    goToRegister
  };
}
