export let permissions = {
  'manager':'管理员',
  'role': '角色管理',
  'account': '账号管理',
  'config':'系统配置',
  'home': '首页',
  'chart_task':'任务报表',
  'chart_user':'员工报表',
  'chart_track':'检测报表',
  'task_create': '创建任务',
  'task_list': '任务列表',
  'task_chart': '任务数据报表',
  'algorithm_log': '算法记录',
  'algorithm_chart': '算法报表',
}

export let permissionsTree = [
  {
    title: '系统管理',
    value: '0',
    key: 0,
    children: [
      {
        title: '角色管理',
        value: 'role',
        key: 1,
      },
      {
        title: '账号管理',
        value: 'account',
        key: 2,
      },
      {
        title: '系统配置',
        value: 'config',
        key: 3,
      }
    ],
  },
  {
    title: '首页',
    value: '4',
    key: 4,
    children: [
      {
        title: '首页',
        value: 'home',
        key: 5,
      }
    ],
  },
  {
    title: '数据报表',
    value: '6',
    key: 6,
    children: [
      {
        title: '任务报表',
        value: 'chart_task',
        key: 7,
      },
      {
        title: '员工报表',
        value: 'chart_user',
        key: 8,
      },
    ],
  },
  {
    title: '检测任务',
    value: '9',
    key: '9',
    children: [
      {
        title: '创建任务',
        value: 'task_create',
        key: 10,
      },
      {
        title: '任务列表',
        value: 'task_list',
        key: 11,
      },
      {
        title: '任务数据报表',
        value: 'task_chart',
        key: 12,
      },
    ],
  },
  {
    title: '算法识别',
    value: '13',
    key: 13,
    children: [
      {
        title: '算法记录',
        value: 'algorithm_log',
        key: 14,
      },
      {
        title: '算法报表',
        value: 'algorithm_chart',
        key: 15,
      },
    ],
  },
]