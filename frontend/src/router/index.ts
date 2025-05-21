import { createRouter, createWebHistory } from 'vue-router';
import ROUTE from '@/global/constant/route';
import { localCache } from '@/utils/cache';
import { LOGIN_TOKEN, ROLE, UID } from '@/global/constant/login';

const { PATH, NAME, CN_NAME } = ROUTE;

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: PATH.ROOT,
      redirect: PATH.CHAT
    },
    {
      path: '/:pathMatch(.*)*',
      component: () => import('@/views/not-found/NotFoundView.vue')
    },
    {
      path: PATH.LOGIN,
      name: NAME.LOGIN,
      component: () => import('@/views/login/LoginView.vue'),
      meta: {
        title: CN_NAME.LOGIN
      }
    },
    {
      path: PATH.REGISTER,
      name: NAME.REGISTER,
      component: () => import('@/views/register/RegisterView.vue'),
      meta: {
        title: CN_NAME.REGISTER
      }
    },
    {
      path: PATH.FORGET,
      name: NAME.FORGET,
      component: () => import('@/views/forget/ForgetView.vue'),
      meta: {
        title: CN_NAME.FORGET
      }
    },
    {
      path: PATH.INDEX,
      name: NAME.INDEX,
      component: () => import('@/views/index/IndexView.vue'),
      meta: {
        title: CN_NAME.INDEX
      }
    },
    {
      path: PATH.CHAT,
      name: NAME.CHAT,
      component: () => import('@/views/chat/ChatView.vue'),
      meta: {
        title: CN_NAME.CHAT
      }
    },
    {
      path: PATH.MANAGE,
      name: NAME.MANAGE,
      component: () => import('@/views/manage/ManageView.vue'),
      meta: {
        title: CN_NAME.MANAGE
      }
    }
  ]
});

// 导航守卫：负责合理访问页面
router.beforeEach((to) => {
  let isLogin = false;
  const uid: null | string = localCache.getItem(UID);
  const token: null | string = localCache.getItem(LOGIN_TOKEN);
  const role: null | number = localCache.getItem(ROLE);
  if (uid && token && (role === 0 || role === 1)) isLogin = true; // null和空字符串都已经考虑
  if (isLogin && [PATH.LOGIN, PATH.REGISTER].includes(to.path)) return PATH.INDEX; // 登录后不允许访问登录和注册页面
  if (!isLogin && [PATH.CHAT, PATH.MANAGE].includes(to.path)) return PATH.LOGIN; // 没登录不允许访问聊天和管理页面
});

// 导航守卫：负责页面标题的管理
router.beforeEach((to) => {
  document.title = to.meta.title as string;
});

export default router;
