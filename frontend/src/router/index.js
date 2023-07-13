import VueRouter from 'vue-router'
import user from './user'
import account from './account'
import system from './system'
import status from './status'


const index = {
  path: '/',
  component: () => import('@/layouts/BaseLayout'),
  redirect: '/home',
  children: [
    {
      path: '/home',
      name: 'home',
      meta: { title: '首页' },
      component: () => import('@/views/home/Home'),
    },
  ]
}

const routes = [index, user, account, system,status];

export default new VueRouter({ mode: 'history', routes })
