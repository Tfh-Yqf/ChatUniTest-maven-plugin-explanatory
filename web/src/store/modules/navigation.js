export default {
  state: () => ({
    loading: false,
    resultType: 0,
    showResult: false,
    navigation: {
      initData:{},
      AlgorithmMenu:[
        {name:'单-声学导航',value:'Algorithm01'},
        {name:'声学+惯导松组合',value:'153p'},
        {name:'惯导+DVL松组合',value:'153v'},
        {name:'声学+惯导+DVL松组合',value:'156vp'},
      ],
      NowConfig: 0,
      DanShuang: [
        { name: '单程', value: 'dan' },
        { name: '双程', value: 'shuang' },
      ],
      LoadComplete: false,
      fileLoading: false,
      percent: 0,
      percent_interval: null,
      init_data: {},
      action_url: '/upload',
      file: {
        svp: '',
        navigation: '',
        DVL: '',
        acoupos:'',
        imu:'',
        dvl:''
      },
      data: {
        DanShuangCheng: 'dan',
        shiying: 'true',
        start_date: '',
        end_date: '',
        zhuangtai: [],
        KangCha: '抗差',
        Algorithm: 0,
        mes: 0,
        navigationPath: "",
        svpPath: "",
        outputPath: "",
        GanBi: {
          SUNS_UANP: {
            right: 0,
            front: 0,
            up: 0
          },
          SUNS_DVL: {
            right: 0,
            front: 0,
            up: 0
          }
        },
        config: {
          configNoisy: {},
          NavgiationConfig: {}
        }
      },
      time: {
        // start_date: '2022,08,24',
        // start_time:'15,38,16',
        // start_ms:0,
        // end_time: '15,49,58',
        // end_date:'2022,08,24',
        // end_ms:0
        start_date: '2019,07,14',
        start_time: '20,43,47',
        start_ms: 0,
        end_time: '03,31,15',
        end_date: '2019,07,15',
        end_ms: 0
      },
      loading: false,
      model: {},
      zhuangtai_canshu: [
        { label: '位置', value: 'weizhi', checked: true, disabled: false },
        { label: '速度', value: 'sudu', checked: false, disabled: false },
        { label: '姿态', value: 'zitai', checked: false, disabled: true },
        { label: '惯导零偏', value: 'guangdao', checked: false, disabled: true },
        { label: '加计零偏', value: 'jiaji', checked: false, disabled: true }
      ],
      upload_image: require("@/assets/upload.png"),
      configNoisy: [
  
        {
          name: '惯导初始配准设置',
          type: 'xiala',
          children: [
            {
              name: '惯导采样率',
              type: 'number',
              config: 100.0
            },
            {
              name: '初始校对模式',
              type: 'string',
              config: '0'
            },
            {
              name: '初始位置',
              type: 'ThreeInput',
              config: [40.091633, 116.25273, 34.79]
            },
            {
              name: '初始速度',
              type: 'ThreeInput',
              config: [0.001, -0.001, 0]
            },
            {
              name: '初始姿态',
              type: 'ThreeInput',
              config: [-0.5695635, 1.2930767, 116.27004]
            }
          ]
        },
        {
          name: '惯导过程噪声设置',
          type: 'xiala',
          children: [
            {
              name: '角度随机游走',
              type: 'ThreeInput',
              config: [0.01, 0.01, 0.01]
            },
            {
              name: '速度随机游走',
              type: 'ThreeInput',
              config: [25, 25, 25]
            },
            {
              name: '角速率随机游走',
              type: 'ThreeInput',
              config: [0, 0, 0]
            },
            {
              name: '比力随机游走',
              type: 'ThreeInput',
              config: [0, 0, 0]
            },
            {
              name: '陀螺比例因子',
              type: 'ThreeInput',
              config: [0, 0, 0]
            },
            {
              name: '加计比例因子',
              type: 'ThreeInput',
              config: [0, 0, 0]
            }
          ]
        }
      ],
      NavgiationConfig: [
  
        {
          name: 'configurations',
          type: 'xiala',
          children: [
            {
              name: '过程噪声矩阵',
              type: 'string',
              config: '1'
            },
            {
              name: '参数初始方差',
              type: 'string',
              config: '5'
            },
            {
              name: '量测噪声',
              type: 'number',
              config: 5.0,
            },
            {
              name: 'mes_size',
              type: 'number',
              config: null,
            },
  
          ]
        }
      ],
      config: []
    },
    gravity: {
      gravity_list: [],
        data: {
            data_path: null,
            sigma_path: 0.06,
            sigma_grav: 1,
            angle: 60.0,
            initial: 5.0,
            velocity: 10.0,
            x_error: 2.0,
            sampleInterval: 3,
            y_error: 0.1666666666,
            sampleTimes: 10,
            INS_sigma_path: 0.2,
        }
    },
  }),
  mutations: {
    setDatasourcePath(state, item) {
      state.datasource_path = [...state.datasource_path, item];
    },
  }
};