import ROUTE from '@/global/constant/route';
import { useRouter } from 'vue-router';

export default function useSettingBtn() {
  const router = useRouter();
  // 进入设置
  function setting() {
    router.push(ROUTE.PATH.MANAGE);
  }

  return {
    setting
  };
}
