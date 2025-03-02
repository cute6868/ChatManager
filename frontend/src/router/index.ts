import { createRouter, createWebHistory } from 'vue-router';
import ROUTE from '@/global/constant/route';
import { localCache } from '@/utils/cache';
import { LOGIN_TOKEN } from '@/global/constant/login';

const { PATH, NAME } = ROUTE;

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: PATH.ROOT,
      redirect: PATH.CHAT
    },
    {
      path: PATH.LOGIN,
      name: NAME.LOGIN,
      component: () => import('@/views/login/LoginView.vue')
    },
    {
      path: PATH.REGISTER,
      name: NAME.REGISTER,
      component: () => import('@/views/register/RegisterView.vue')
    },
    {
      path: PATH.FORGET,
      name: NAME.FORGET,
      component: () => import('@/views/forget/ForgetView.vue')
    },
    {
      path: PATH.INDEX,
      name: NAME.INDEX,
      component: () => import('@/views/index/IndexView.vue')
    },
    {
      path: PATH.CHAT,
      name: NAME.CHAT,
      component: () => import('@/views/chat/ChatView.vue')
    },
    {
      path: PATH.MANAGE,
      name: NAME.MANAGE,
      component: () => import('@/views/manage/ManageView.vue')
    },
    {
      path: '/:pathMatch(.*)*',
      component: () => import('@/views/not-found/NotFoundView.vue')
    }
  ]
});

// 导航守卫
router.beforeEach((to) => {
  const token = localCache.getItem(LOGIN_TOKEN) || ''; // 通过登录令牌判断当前的登录状态
  if (token && [PATH.LOGIN, PATH.REGISTER].includes(to.path)) return PATH.INDEX; // 登录后不允许访问登录和注册页面
  if (!token && [PATH.CHAT, PATH.MANAGE].includes(to.path)) return PATH.LOGIN; // 没登录不允许访问聊天和管理页面
});

export default router;
