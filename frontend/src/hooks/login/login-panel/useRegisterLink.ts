import ROUTE from '@/global/constant/route';
import { useRouter } from 'vue-router';

export default function useRegisterLink() {
  const router = useRouter();

  function goToRegister() {
    router.push(ROUTE.PATH.REGISTER); // 跳转到注册页
  }

  return {
    goToRegister
  };
}
