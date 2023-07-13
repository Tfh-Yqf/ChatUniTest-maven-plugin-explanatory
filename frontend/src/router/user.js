export default {
  path: '/user',
  name: 'user',
  component: () => import('@/layouts/UserLayout'),
  children: [
    {
      path: 'login',
      name: 'login',
      meta: { title: '登录' },
      component: () => import('@/views/login/Login'),
    }
  ],
}