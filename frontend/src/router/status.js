export default {
    path: '/',
    name: 'status',
    component: () => import('@/layouts/BaseLayout'),
    children: [
        {
            path: 'project',
            name: 'project',
            meta: { title: 'Project Status', permission: 'role' },
            component: () => import('@/views/Status/project.vue'),
        },
        {
            path: 'predict',
            name: 'predict',
            meta: { title: 'Predict Status', permission: 'account' },
            component: () => import('@/views/Status/predict.vue'),
        }
    ],
}