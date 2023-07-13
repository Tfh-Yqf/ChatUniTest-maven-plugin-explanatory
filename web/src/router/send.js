export default {
    path: '/send',
    name: 'send',
    component: () => import('@/layouts/BaseLayout'),
    children: [
        {
            path: 'send',
            name: 'send_send',
            meta: { title: 'send' },
            component: () => import('@/views/send/send.vue'),
        },
    ]
}