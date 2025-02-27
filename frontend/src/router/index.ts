import { createRouter, createWebHistory } from 'vue-router';
import { localCache } from '@/utils/cache';
import { LOGIN_TOKEN } from '@/global/constant';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/chat'
    },
    {
      path: '/login',
      component: () => import('@/views/login/LoginView.vue')
    },
    {
      path: '/chat',
      component: () => import('@/views/chat/ChatView.vue')
    },
    {
      path: '/index',
      component: () => import('@/views/index/IndexView.vue')
    },
    {
      path: '/manage',
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
  // 当前的登录状态
  const token = localCache.getItem(LOGIN_TOKEN) ?? '';

  // 如果已经登录，想访问login页面，将强制跳转到index页面

  if (token) {
    if (to.path === '/login') {
      return '/index'; // 放行到index页面
    }
  }
  // 如果没有登录，想访问chat、manage页面，将强制跳转到login页面
  else {
    if (to.path === '/chat' || to.path === '/manage') {
      return '/login';
    }
  }

  return; // 直接放行
});

export default router;
