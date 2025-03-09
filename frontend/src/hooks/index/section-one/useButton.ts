import { useRouter } from 'vue-router';
import ROUTE from '@/global/constant/route';

export default function useButton() {
  const router = useRouter();
  function gotoChat() {
    router.push(ROUTE.PATH.CHAT); // 跳转到聊天页
  }

  return {
    gotoChat
  };
}
