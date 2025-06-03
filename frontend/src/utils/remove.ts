import { LOGIN_TOKEN, ROLE, SELECTED_MODELS, UID } from '@/global/constant/login';
import { localCache } from './cache';

export default function removeLoginData() {
  localCache.removeItem(UID);
  localCache.removeItem(LOGIN_TOKEN);
  localCache.removeItem(ROLE);
  localCache.removeItem(SELECTED_MODELS);
}
