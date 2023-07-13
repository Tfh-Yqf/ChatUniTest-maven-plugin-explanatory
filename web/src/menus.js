export default [
  // {
  //   name:'系统设置',
  //   key:'/system_settins',
  //   children:[
  //     {
  //       key: '/home/home', name: '首页', icon: 'team', submenus: [
  //
  //       ]
  //     },
  //     {
  //       key: '/system/status', name: '模块状态', icon: 'team', submenus: [
  //
  //       ]
  //     },
  //     {
  //       key: '/system/role', name: '角色管理', icon: 'team', submenus: [
  //
  //       ]
  //     },
  //     {
  //       key: '/system/account', name: '用户账号', icon: 'team', submenus: [
  //
  //       ]
  //     }
  //   ],
  // },
  // {
  //   name:'首页大屏',
  //   key:'/FirstPage',
  //   children:[
  //     {
  //       key: '/home/home', name: '首页', icon: 'team', submenus: [

  //       ]
  //     }
  //   ],
  // },
  {
    name: '数据仿真',
    key: '/simulation',
    children: [
      {
        key: '/simulation/inertial_navigation', name: '惯性导航数据', icon: 'team', submenus: [
        ]
      },
      {
        key: '/simulation/sonar', name: '声呐数据', icon: 'team', submenus: [

        ]
      },
      {
        key: '/simulation/Doppler_velocity', name: '多普勒测速数据', icon: 'team', submenus: [

        ]
      },
      {
        key: '/simulation/Pressure_observation', name: '压力计观测数据', icon: 'team', submenus: [
        ]
      },
    ],
  },

  {
    name: '声速场构建',
    key:'/sound',
    children: [
      {
        key: '/sound/sound_space', name: '空间场', icon: 'pie-chart', submenus: [

        ]
      },
      {
        key: '/sound/sound_time', name: '时间场', icon: 'code', submenus: [

        ]
      }

    ],
  },
  {
    name: '基准点标定',
    key: '/calibration',
    children:[
      {
        key: '/calibration/config', name: '标定-参数配置', icon: 'pie-chart', submenus: [

        ]
      },
      // {
      //   key: '/calibration/select', name: '功能选择', icon: 'file-text', submenus: [

      //   ]
      // },
      {
        key: '/calibration/result', name: '结果展示', icon: 'file-text', submenus: [

        ]
      },

    ],
  },
  {
    name:'水下导航',
    key:'/navigation',
    children: [

      {
        key: '/navigation/underwater_navigation', name: '参数配置-导航测试', icon: 'code', submenus: [

        ]
      },
      {
        key: '/navigation/gravity', name: '参数配置-重力匹配', icon: 'code', submenus: [

        ]
      },
      {
        key: '/navigation/navigation_result', name: '导航结果', icon: 'pie-chart', submenus: [

        ]
      },
      {
        key: '/navigation/gravity_result', name: '重力匹配结果', icon: 'pie-chart', submenus: [

        ]
      },
    ]
  },
  // {
  //   name:'请求数据',
  //   key:'/send',
  //   children: [
  //     {
  //       key: '/send/send', name: '发送请求', icon: 'pie-chart', submenus: [

  //       ]
  //     }
  //   ]
  // }

]

