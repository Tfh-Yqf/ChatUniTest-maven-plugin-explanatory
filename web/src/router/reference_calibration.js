export default {
    path: '/calibration',
    name: 'calibration',
    component: () => import('@/layouts/BaseLayout'),
    children: [
        {
            path: 'config',
            name: 'config',
            meta: { title: '标定-参数配置' },
            component: () => import('@/views/reference_calibration/config.vue'),
        },
        {
            path: 'select',
            name: 'select',
            meta: { title: '功能选择' },
            component: () => import('@/views/reference_calibration/select.vue'),
        },
        {
            path: 'result',
            name: 'result',
            meta: { title: '结果展示' },
            component: () => import('@/views/reference_calibration/result.vue'),
        },
    ],
}