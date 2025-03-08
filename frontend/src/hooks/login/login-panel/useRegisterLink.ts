import { useRouter } from 'vue-router';
import ROUTE from '@/global/constant/route';

export default function useRegisterLink() {
  const router = useRouter();

  function goToRegister() {
    router.push(ROUTE.PATH.REGISTER); // 跳转到注册页
  }

  return {
    goToRegister
  };
}
