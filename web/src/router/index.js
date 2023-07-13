import VueRouter from 'vue-router'

import system from './system'
import navigation from './navigation'
import sound from './sound'
import calibration from './reference_calibration'
import simulation from './simulation'
import send from './send'

const index = {
  path: '/',
  component: () => import('@/layouts/BaseLayout'),
  redirect:'/home',
  children: [
    {
      path: '/home',
      name: 'home',
      meta: { title: '首页' },
      component: () => import('@/views/home/Home'),
    },
  ]
}

const routes = [index,system,navigation,sound,calibration,simulation,send];




export default new VueRouter({ mode: 'hash', routes })

