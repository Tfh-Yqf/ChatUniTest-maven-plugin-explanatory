export default {
    path: '/sound',
    name: 'sound',
    component: () => import('@/layouts/BaseLayout'),
    children: [
        {
            path: 'sound_time',
            name: 'sound_time',
            meta: { title: '时间场' },
            component: () => import('@/views/sound/sound_time.vue'),
        },
        {
            path: 'sound_space',
            name: 'sound_space',
            meta: { title: '空间场' },
            component: () => import('@/views/sound/sound_space.vue'),
        },
        {
            path: 'result',
            name: 'result',
            meta: { title: '结果展示' },
            component: () => import('@/views/sound/result.vue'),
        },
    ],
}