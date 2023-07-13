export default {
  path: '/system',
  name: 'system',
  component: () => import('@/layouts/UserLayout.vue'),
  children: [
    {
      path: 'status',
      name: 'status',
      meta: { title: '模块状态' },
      component: () => import('@/views/system/account/status.vue'),
    },
    {
      path: 'account',
      name: 'account',
      meta: { title: '账号管理', permission: 'account' },
      component: () => import('@/views/system/account/Account'),
    },
    {
      path: 'login',
      name: 'login',
      meta: { title: '登录' },
      component: () => import('@/views/system/login/Login'),
    },
    {
      path: 'set_password',
      name: 'setPassword',
      meta: { title: '设置密码' },
      component: () => import('@/views/system/setPassword/SetPassword'),
    },
  ],
}