import { UID } from '@/global/constant/login';
import { localCache } from '@/utils/cache';
import ROUTE from '@/global/constant/route';
import removeLoginData from '@/utils/remove';
import logoutRequest from '@/service/api/logout';
import { useRouter } from 'vue-router';

export default function useLogoutBtn() {
  const router = useRouter();
  const uid = localCache.getItem(UID);

  // 退出登录
  function logout() {
    logoutRequest(uid)
      .then(() => {
        removeLoginData(); // 直接清除本地登录数据
        router.push(ROUTE.PATH.INDEX);
      })
      .catch(() => {
        removeLoginData();
        router.push(ROUTE.PATH.INDEX);
      });
  }
  return {
    logout
  };
}
