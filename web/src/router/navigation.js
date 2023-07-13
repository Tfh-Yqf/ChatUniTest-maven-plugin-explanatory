export default {
    path: '/navigation',
    name: 'navigation',
    component: () => import('@/layouts/BaseLayout'),
    children: [
        {
            path: 'underwater_navigation',
            name: 'underwater_navigation',
            meta: { title: '水下导航' },
            component: () => import('@/views/navigation/underwater_navigation/underwater_navigation.vue'),
        },
        {
            path: 'gravity',
            name: 'gravity',
            meta: { title: '重力匹配' },
            component: () => import('@/views/navigation/gravity/gravity.vue'),
        },
        {
            path: 'navigation_result',
            name: 'navigation_result',
            meta: { title: '导航结果' },
            component: () => import('@/views/navigation/navigation_result/navigation_result.vue'),
        },
        {
            path: 'result_other',
            name: 'result_other',
            meta: { title: '其他导航结果' },
            component: () => import('@/views/navigation/navigation_result/result_other.vue'),
        },
        {
            path: 'gravity_result',
            name: 'gravity_result',
            meta: { title: '重力匹配结果' },
            component: () => import('@/views/navigation/gravity/gravity_result.vue'),
        },
    ],
}