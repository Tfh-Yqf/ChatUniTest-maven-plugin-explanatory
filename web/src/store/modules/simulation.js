export default {
  state: () => ({
    loading: false,
    resultType: 0,
    resultShow: false,
    resultFiles: [],
    navigation: {
      datasource_path: [],
      acoposerr: [0.1, 0.2, 0.3],
      acodelerr: 50,
      acoranerr: 3,
      positionData: null,
      svpdh: 10,
      samper: 0,
      insacolever: [0, 0, 0],
      inputWay: "1",
      moni: "1",
      type_option: ["classic_none", "static", "uniform", "accelerate", "decelerate", "pitchleft", "pitchright", "rollleft", "rollright", "headingleft", "headingright", "turnleft", "turnright", "turnup", "turndown", "coturnleft", "coturnright", "coturnup", "coturndown", "turns", "coturns"],
      path_template: { id: 1, type: 'static', time: 0, palstance: 0, accelerated_speed: 0 },
      data: {
          insrate: 100, //惯导采样率
          initatt: [0, 0, 0], //初始姿态
          initvel: [0, 0, 0], //初始速度
          initpos: [40.091633386, 116.252733997, 34.790], //初始位置
          tideamp: 5,//潮汐：振幅
          tideper: 12, //潮汐：周期
          // waveangamp: 5,	//角振幅
          // waveangper: 5,	//角周期
          // wavelinamp: 1,//	线振幅
          // wavelinper: 5,//	线周期
          waveangamp: [5, 5, 3],	//角振幅
          waveangper: [5, 7, 10],	//角周期
          wavelinamp: [1, 1, 0.5],//	线振幅
          wavelinper: [5, 7, 10],//	线周期
          gb: [0.1, 0.1, 0.1],//	陀螺初始标准差
          ab: [0, 0, 0],//加速度计初始标准差
          gsf: [0, 0, 0],//陀螺仪比例因子误差标准偏差
          asf: [0.01, 0.01, 0.01],//加速度计比例因子误差标准偏差
          arw: [25, 25, 25],//角度随机游走
          vrw: [0, 0, 0],//速度随机游走
          arrw: [0, 0, 0],//角速度随机游走
          sfrw: [0, 0, 0],//比力随机游走
          gsfpsd: [0, 0, 0],//陀螺标度因子误差功率谱密度
          asfpsd: [0, 0, 0],//	加速度计标度因子误差功率谱密度
          cortime: "inf",//	一阶马尔可夫过程的相关时间
          path: '',
          tspd: '',
      },
      initdatas: {
        path: [],
      },
    },
    sonar: {
      inputWay: "1",
      data: {
        svpfile: 0, //声速剖面数据
        svpdh: 10,//	声速剖面层深
        samper: 0,//采样频率
        insacolever: [0, 0, 0],	//杆臂值
        acoposerr: [0.1, 0.1, 0.2],//位置误差
        acodelerr: 50,//硬件时延
        acoranerr: 3,//随机误差
      },
      datasource_transponder: [],
      initdatas: {
        tspd: [],
        input: [],
      },
      transponder_template: {id: 1, east: 0, north: 0, sky: 0},
    },
    doppler: {
      gb: [2, 2, 2],
      dvlranerr: [4, 4, 4],
      dvlangerr: [0, 0, 0],
      dvltimdel: 0,
      insdvllever: [1, 1, 1]
    },
    pressureObserve: {
      pgsf: 5,
      pgranerr: 4,
      inspglever: [1, 1, 1]
    },
  })
};