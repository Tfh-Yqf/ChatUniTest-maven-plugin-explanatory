export default {
    path: '/simulation',
    name: 'simulation',
    component: () => import('@/layouts/BaseLayout'),
    children: [
        {
            path: 'inertial_navigation',
            name: 'simulation_inertial_navigation',
            meta: { title: '惯性导航数据' },
            component: () => import('@/views/simulation/inertial_navigation.vue'),
        },
        {
            path: 'sonar',
            name: 'simulation_sonar',
            meta: { title: '声呐数据' },
            component: () => import('@/views/simulation/sonar.vue'),
        },
        {
            path: 'Pressure_observation',
            name: 'simulation_Pressure_observation',
            meta: { title: '压力计观测数据' },
            component: () => import('@/views/simulation/Pressure_observation.vue'),
        },
        {
            path: 'Doppler_velocity',
            name: 'simulation_Doppler_velocity',
            meta: { title: '多普勒测速数据' },
            component: () => import('@/views/simulation/Doppler_velocity.vue'),
        },

        {
            path: 'result',
            name: 'result',
            meta: { title: '结果展示' },
            component: () => import('@/views/simulation/result.vue'),
        },
        {
            path: 'simulation_result',
            name: 'simulation_result',
            meta: { title: '结果展示' },
            component: () => import('@/views/simulation/SimulationResult.vue'),
        },
    ],
}