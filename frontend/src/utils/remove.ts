import { LOGIN_TOKEN, ROLE, SELECTED_MODELS, UID } from '@/global/constant/login';
import { localCache } from './cache';
import useModelsStore from '@/store/models';

export default function removeLoginData() {
  useModelsStore().clearAllTimers(); // 清除模型响应超时判断的定时器
  localCache.removeItem(UID);
  localCache.removeItem(LOGIN_TOKEN);
  localCache.removeItem(ROLE);
  localCache.removeItem(SELECTED_MODELS);
}
