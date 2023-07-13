export default {
    state: () => ({
        loading: false,
        showResult: false,
        TimeStrs: ["start_date", "start_time", "end_date", "end_time"],
        configList: [],
        wangpingchaList: [],
        svpList: [],
        acousticPositioningPathList: [],
        wangpingcha: false,
        data: {
          Nowconfig: '',
          tspd: [
            {
              tspdIndex: 1,
              svpPath: "../data/nh/J12023-03-28-03-25-52-Down.txt",
              timeRange: [
                {
                  startTime: "2023,4,1,20,20,0",
                  endTime: "2023,4,1,23,59,0",
                }
              ]
            },
            {
              tspdIndex: 2,
              svpPath: "../data/nh/J12023-03-28-03-25-52-Down.txt",
              timeRange: [
                {
                  startTime: "2023,3,31,20,0,0",
                  endTime: "2023,4,1,1,0,0"
                }
              ]
            },
            {
              tspdIndex: 3,
              svpPath: "../data/nh/J12023-03-28-03-25-52-Down.txt",
              timeRange: [
                {
                  startTime: "2023,3,25,14,9,0",
                  endTime: "2023,3,25,18,15,0"
                },
                {
                  startTime: "2023,3,25,18,40,0",
                  endTime: "2023,3,25,22,50,0"
                }
              ]
            },
            {
              tspdIndex: 4,
              svpPath: "../data/nh/J12023-03-28-03-25-52-Down.txt",
              timeRange: [
                {
                  startTime: "2023,4,2,0,30,0",
                  endTime: "2023,4,2,4,40,0"
                }
              ]
            }
          ],
          NowJuLiGuanCeZhi: 0,
          time_interval: 60,
          random_structure: 0,
          net_structure: true,
          svpPath: '',
          acousticPositioningPath: '',
          XYZ0: [-2480392.432, 5529514.539, 1970781.314],
          NowZuoBiaoYuanDian: 0,
          wangpingchaFile: {
            posAnsFile: 0,
            acousticPositioningPath: 0,
            posObsFile: 0,
            tspdDepObs: 0,
            svpdata: 0
          }
        },
        loading: false,
        model_function: [
          { label: '坐标模型', value: 'CoordinateModel', checked: true, disabled: true },
          { label: '杆臂模型', value: 'LeverModel', checked: false, disabled: false },
          { label: '常声速模型', value: 'ConstantSoundVelocityModel', checked: false, disabled: false },
          { label: '时变声速模型', value: 'SoundVelocityWithBSplineModel', checked: false, disabled: false },
          { label: '多普勒模型', value: 'DopplerModel', checked: false, disabled: false }
        ],
        JuLiGuanCeZhi_List: [
          { name: '加权平均声速', value: 'AveVel' },
          { name: '声线跟踪', value: 'RayTrack' },
          { name: '快速声线跟踪', value: 'FastRayTrack' }
        ],
        RandomStructureList: [
          { name: '等权', value: 'no' },
          { name: 'IGG3算法', value: 'igg3' },
          { name: 'huber算法', value: 'hubber' }
        ],
        ZuoBiaoYuanDian_List: [
          { name: '大地坐标系', value: '0' },
          { name: '局部坐标系', value: '1' },
        ],
        mockData: [
          { key: '1', title: '点1', description: '点1', chosen: false },
          { key: '2', title: '点2', description: '点2', chosen: false },
          { key: '3', title: '点3', description: '点3', chosen: false },
          { key: '4', title: '点4', description: '点4', chosen: false },
          { key: '5', title: '点5', description: '点5', chosen: false },
        ],
        targetKeys: [],
        wangpingchaFile: {
          posAnsFile: [],
          acousticPositioningPath: [],
          posObsFile: [],
          tspdDepObs: [],
          svpdata: []
        }
      }),
  };