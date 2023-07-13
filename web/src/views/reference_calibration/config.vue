<template>
  <div>
    <a-card title="参数配置">
      <a-spin :spinning="loading">
        <div style="display: flex;flex-direction: row;">
          <a-card :loading="loading" title="选择声速剖面数据和观测数据集" style="width: 49%;flex-shrink: 0;justify-content: center">
            <div style="display: flex;flex-direction: column;width: 100%;align-items: center">
              <div style="display: flex;align-items: center;width: 100%">
                <div style="width: 150px;flex-shrink: 0;margin-left: 20px;">参数配置文件</div>
                <a-select @change="changeConfig" v-model.number="data.Nowconfig" placeholder="参数配置文件" style="width: 50%">
                  <a-select-option :value="index" v-for="(item, index) in configList" :key="index">{{ item.name
                  }}</a-select-option>
                </a-select>
              </div>

              <div style="display:flex;flex-direction: row;font-weight: bold;margin-top: 20px">
                <div style="width: 230px;text-align: center">可解算点号</div>
                <div style="width: 210px;text-align: center">待解算点号</div>
              </div>
              <a-transfer :dataSource="mockData" :targetKeys="targetKeys" @change="handleChange"
                :render="item => item.title" style="margin-left: 20px">
              </a-transfer>
            </div>


            <a-Collapse style="width: 100%;margin-top: 20px;">
              <a-collapse-panel v-for="(item, index) in  data.tspd " :key="index" :header="'点' + (index + 1) + '的时间范围'">

                <div style="margin-top: 10px;display: flex;flex-direction: column">
                  <div style="display: flex;flex-direction: row;width: 100%;background-color: #fafafa;">
                    <div class="table_five">序号</div>
                    <div class="table_five">开始日期</div>
                    <div class="table_five">开始时间</div>
                    <div class="table_five">结束日期</div>
                    <div class="table_five">结束时间</div>
                  </div>

                  <div style="display: flex;flex-direction: row;width: 100%;margin-top: 5px"
                    v-for="(it, Idx) in  item.timeRange " :key="it.id">
                    <div class="table_five">
                      {{ Idx + 1 }}
                    </div>
                    <div class="table_five" v-for="str in  TimeStrs " :key="str.id">
                      <a-date-picker v-model="it[str]" placeholder="日期" valueFormat="YYYY,MM,DD" style="width: 95%;"
                        @change="DateChange(index, Idx)" v-if="str.endsWith('date')" />
                      <a-time-picker v-model="it[str]" placeholder="时间" valueFormat="HH,mm,ss" format="HH,mm,ss"
                        style="width: 95%;" @change="DateChange(index, Idx)" v-else />
                    </div>
                  </div>

                  <div style="width: 100%;display: flex;flex-direction: row-reverse;margin-top: 10px;">
                    <a-button type="primary" style="margin-right: 5%" @click="AddLine_Tspd(index)">添加行</a-button>
                  </div>

                </div>


              </a-collapse-panel>
            </a-Collapse>


            <a-divider orientation="left">文件选择</a-divider>
            <div style="display: flex;flex-direction: column;margin-top: 10px">

              <div style="display: flex;flex-direction: column;" v-if="wangpingcha">
                <div style="display: flex;align-items: center">
                  <div style="width: 150px;flex-shrink: 0;margin-left: 20px;">走航式标定结果</div>
                  <a-select v-model.number="data.wangpingchaFile.posAnsFile" placeholder="选择走航式标定结果" style="width: 50%">
                    <a-select-option :value="index" v-for="( item, index ) in  wangpingchaFile.posAnsFile "
                      :key="index">{{
                        item.name }}</a-select-option>
                  </a-select>
                </div>

                <div style="display: flex;align-items: center;margin-top: 20px">
                  <div style="width: 150px;flex-shrink: 0;margin-left: 20px;">走航式观测数据</div>
                  <a-select v-model.number="data.wangpingchaFile.acousticPositioningPath" placeholder="选择走航式数据观测数据"
                    style="width: 50%">
                    <a-select-option :value="index" v-for="( item, index ) in  wangpingchaFile.acousticPositioningPath "
                      :key="index">{{ item.name }}</a-select-option>
                  </a-select>
                </div>

                <div style="display: flex;align-items: center;margin-top: 20px">
                  <div style="width: 150px;flex-shrink: 0;margin-left: 20px;">应答器互测距数据</div>
                  <a-select v-model.number="data.wangpingchaFile.posObsFile" placeholder="应答器互测距数据" style="width: 50%">
                    <a-select-option :value="index" v-for="( item, index ) in  wangpingchaFile.posObsFile "
                      :key="index">{{
                        item.name }}</a-select-option>
                  </a-select>
                </div>

                <div style="display: flex;align-items: center;margin-top: 20px">
                  <div style="width: 150px;flex-shrink: 0;margin-left: 20px;">应答器深度数据</div>
                  <a-select v-model.number="data.wangpingchaFile.tspdDepObs" placeholder="应答器深度数据" style="width: 50%">
                    <a-select-option :value="index" v-for="( item, index ) in  wangpingchaFile.tspdDepObs "
                      :key="index">{{
                        item.name }}</a-select-option>
                  </a-select>
                </div>

                <div style="display: flex;align-items: center;margin-top: 20px">
                  <div style="width: 150px;flex-shrink: 0;margin-left: 20px;">声速剖面数据</div>
                  <a-select v-model.number="data.wangpingchaFile.svpdata" placeholder="声速剖面数据" style="width: 50%">
                    <a-select-option :value="index" v-for="( item, index ) in  wangpingchaFile.svpdata " :key="index">{{
                      item.name }}</a-select-option>
                  </a-select>
                </div>
              </div>

              <div v-else>
                <div style="display: flex;align-items: center">
                  <div style="width: 150px;flex-shrink: 0;margin-left: 20px;">走航式观测数据</div>
                  <a-select v-model.number="data.acousticPositioningPath" placeholder="选择声速剖面数据" style="width: 50%">
                    <a-select-option :value="index" v-for="( item, index ) in  acousticPositioningPathList "
                      :key="index">{{
                        item.name }}</a-select-option>
                  </a-select>
                </div>

                <div style="display: flex;align-items: center;margin-top: 20px">
                  <div style="width: 150px;flex-shrink: 0;margin-left: 20px;">声速剖面文件</div>
                  <a-select v-model.number="data.svpPath" placeholder="选择声速剖面数据" style="width: 50%">
                    <a-select-option :value="index" v-for="( item, index ) in  svpList " :key="index">{{ item.name
                    }}</a-select-option>
                  </a-select>
                </div>
              </div>
            </div>

            <div style="width: 100%;display: flex;justify-content: center;margin-top: 20px;">
              <a-popconfirm title="确定解算吗?" @confirm="Excute">
                <a-button type="primary" class="primary"
                  style="background-color: #ff4d4f;border: none;width: 150px;height: 50px;font-weight: bold;font-size: 20px">解算
                </a-button>
              </a-popconfirm>
              <button type="button" class="ant-btn ant-btn-primary ant-btn-lg" @click="updateServeData"
              style="width: 100px;border-radius: 20px;margin-left: 20px; margin-top: 5px;"><span>更新数据</span></button>
            </div>

          </a-card>

          <a-card :loading="loading" style="width: 49%;margin-left: 2%" title="函数模型">
            <a-divider orientation="left">函数模型弹性构建</a-divider>
            <div class="group">
              <div style="width: 100px;flex-shrink: 0">构建选项：</div>
              <div style="flex-wrap: wrap;display: flex;flex-direction: row;line-height: 40px">
                <a-checkbox :disabled="item.disabled" :checked="item.checked" @change="change_function_module(index)"
                  v-for="( item, index ) in  model_function " style="margin-left: 20px" :key="item.value">

                  {{ item.label }}
                </a-checkbox>
              </div>

            </div>
            <div class="group" v-if="model_function[3].checked">
              <div>时变声速模型时间间隔参数</div>
              <a-input-number style="margin-left: 20px" v-model="data.time_interval"></a-input-number>
              <div style="margin-left: 15px;">分钟</div>
            </div>

            <a-divider orientation="left" style="margin-top: 30px">距离观测值构建</a-divider>
            <div class="group" style="margin-top: 30px">
              <div style="display: flex;width: 60%;flex-shrink: 0">
                <div style="width: 100px;flex-shrink: 0;">构建选项</div>
                <a-radio v-for="( item, index ) in  JuLiGuanCeZhi_List " :key="index"
                  :checked="index == data.NowJuLiGuanCeZhi" @change="change_distance_structure(index)">{{ item.name }}
                </a-radio>

              </div>
            </div>


            <a-divider orientation="left" style="margin-top: 30px">随机模型弹性构建</a-divider>
            <div class="group" style="margin-top: 30px">
              <div style="display: flex;width: 60%;flex-shrink: 0">
                <div style="width: 100px;flex-shrink: 0;">构建选项</div>
                <a-radio :checked="data.random_structure == index" @change="change_random_structure(index)"
                  v-for="( item, index ) in  RandomStructureList " :key="index">{{ item.name }}
                </a-radio>
              </div>

            </div>


            <a-divider orientation="left" style="margin-top: 30px">
              坐标原点
            </a-divider>
            <div class="group" style="margin-top: 30px">
              <div style="display: flex;width: 60%;flex-shrink: 0">
                <div style="width: 100px;flex-shrink: 0;">构建选项</div>
                <a-radio :key="index" :checked="data.NowZuoBiaoYuanDian == index" @change="change_ZuoBiaoYuanDian(index)"
                  v-for="( item, index ) in  ZuoBiaoYuanDian_List ">{{ item.name
                  }}
                </a-radio>
              </div>
            </div>

            <div v-if="data.NowZuoBiaoYuanDian == 1" style="margin-top: 20px;display: flex;flex-direction: column">
              <div style="display: flex;flex-direction: row;width: 100%;background-color: #fafafa;align-items: center;">
                <div class="table">名称</div>
                <div class="table">X</div>
                <div class="table">Y</div>
                <div class="table">Z</div>
              </div>

              <div style="display: flex;flex-direction: row;width: 100%;margin-top: 5px">
                <div class="table">
                  局部坐标系原点
                </div>
                <div class="table">
                  <a-input-number v-model="data.XYZ0[0]" style="width: 70%"></a-input-number>
                </div>
                <div class="table">
                  <a-input-number v-model="data.XYZ0[1]" style="width: 70%"></a-input-number>
                </div>
                <div class="table">
                  <a-input-number v-model="data.XYZ0[2]" style="width: 70%"></a-input-number>
                </div>
              </div>


            </div>


            <a-divider orientation="left" style="margin-top: 30px">
              <a-checkbox :checked="wangpingcha" @change="change_wangpingcha()">网平差构建</a-checkbox>
            </a-divider>
            <div class="group" style="margin-top: 30px" v-if="wangpingcha">
              <div style="display: flex;width: 60%;flex-shrink: 0">
                <div style="width: 100px;flex-shrink: 0;">构建选项</div>
                <a-radio :checked="data.net_structure" @change="change_net_structure">三维自由网平差</a-radio>
                <a-radio :checked="!data.net_structure" @change="change_net_structure">深度约束网平差</a-radio>
              </div>
            </div>
          </a-card>
        </div>


      </a-spin>


    </a-card>


  </div>
</template>

<script>
import { excuteAPI, init, ExcuteWangpingcha, WangpingchaInit, GetConfig } from '@/api/position.js'
import { UpdateServeData } from '@/api/sound'
export default {
  components: {},
  data() {
    return {
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
    };
  },
  methods: {
    changeConfig() {
      var that = this;
      GetConfig(this.configList[this.data.Nowconfig]).then((res) => {
        if (res.code == 0) {
          if ('posobsfile_path' in res.data) {
            that.data.tspd = res.data.tspd;
            console.log(that.data.tspd);

            // 网平插
            this.wangpingcha = false;
            this.change_wangpingcha().then((re) => {
              for (var i = 0; i < this.wangpingchaFile.posObsFile.length; i++) {
                if (res.data.tspdttobsfile_path == this.wangpingchaFile.posObsFile[i].path) {
                  this.data.wangpingchaFile.posAnsFile = i;
                }
              }

              for (var i = 0; i < this.wangpingchaFile.tspdDepObs.length; i++) {
                if (res.data.tspddepobsfile_path == this.data.wangpingchaFile.tspdDepObs[i].path) {
                  this.data.wangpingchaFile.tspdDepObs = i;
                }
              }

              for (var i = 0; i < this.wangpingchaFile.svpdata.length; i++) {
                if (res.data.svpfile_path == this.data.wangpingchaFile.svpdata[i].path) {
                  this.data.wangpingchaFile.svpdata = i;
                }
              }

              for (var i = 0; i < this.wangpingchaFile.posAnsFile.length; i++) {
                if (res.data.posansfile_path == this.data.wangpingchaFile.posAnsFile[i].path) {
                  this.data.wangpingchaFile.posAnsFile = i;
                }
              }

              for (var i = 0; i < this.wangpingchaFile.acousticPositioningPath.length; i++) {
                if (res.data.posobsfile_path == this.data.wangpingchaFile.acousticPositioningPath[i].path) {
                  this.data.wangpingchaFile.acousticPositioningPath = i;
                }
              }

              for (var i = 0; i < this.RandomStructureList.length; i++) {
                if (this.data.robmod == this.RandomStructureList[i].value) {
                  this.data.random_structure = i;
                }
              }

              if (res.data.netmod == 1)
                this.net_structure = false;
              else
                this.net_structure = true;
            })


          } else {

            that.data.tspd = res.data.tspd;
            this.chaifentime();

            for (var i = 0; i < this.model_function.length; i++)
              this.model_function[i].checked = false;
            for (var i = 0; i < res.data.errorModels.length; i++) {
              for (var j = 0; j < this.model_function.length; j++) {
                if (res.data.errorModels[i].modelName == this.model_function[j].value) {
                  this.model_function[j].checked = true;
                }
              }
            }

            for (var i = 0; i < this.acousticPositioningPathList.length; i++) {
              if (res.data.acousticPositioningPath == this.acousticPositioningPathList[i].path) {
                this.data.acousticPositioningPath = i;
              }
            }

            for (var i = 0; i < this.svpList.length; i++) {
              if (res.data.tspd[0].svpPath == this.svpList[i].path) {
                this.data.svpPath = i;
              }
            }

            for (var i = 0; i < this.JuLiGuanCeZhi_List.length; i++) {
              if (res.data.distanceMethod == this.JuLiGuanCeZhi_List[i].value) {
                this.data.NowJuLiGuanCeZhi = i;
              }
            }

            for (var i = 0; i < this.RandomStructureList.length; i++) {
              if (res.data.RobustModel == this.RandomStructureList[i].value) {
                this.data.random_structure = i;
              }
            }

            if ('XYZ0' in res.data) {
              // 局部坐标欧
              this.data.NowZuoBiaoYuanDian = 1;
              this.data.XYZ0 = res.data.XYZ0;
            }

            if (this.data.Nowconfig) {
              this.wangpingcha = false;
            }
          }
        }

      })
    },
    DateChange(index, ind) {
      this.$nextTick(() => {
        this.$forceUpdate()
      })
      this.data.tspd[index].timeRange[ind].startTime = this.data.tspd[index].timeRange[ind].start_date + ',' + this.data.tspd[index].timeRange[ind].start_time;
      this.data.tspd[index].timeRange[ind].endTime = this.data.tspd[index].timeRange[ind].end_date + ',' + this.data.tspd[index].timeRange[ind].end_time;
    },
    getCommaIndex(str, n) {
      var index = str.indexOf(',');
      while (--n && index != -1) {
        index = str.indexOf(',', index + 1);
      }
      return index;
    },
    gettime(e) {
      return e.substring(this.getCommaIndex(e, 3) + 1, e.length);
    },
    getdate(e) {
      return e.substring(0, this.getCommaIndex(e, 3));
    },
    chaifentime() {
      for (var i = 0; i < this.data.tspd.length; i++) {
        for (var j = 0; j < this.data.tspd[i].timeRange.length; j++) {
          this.data.tspd[i].timeRange[j].start_time = this.gettime(this.data.tspd[i].timeRange[j].startTime);
          this.data.tspd[i].timeRange[j].start_date = this.getdate(this.data.tspd[i].timeRange[j].startTime);
          this.data.tspd[i].timeRange[j].end_time = this.gettime(this.data.tspd[i].timeRange[j].endTime);
          this.data.tspd[i].timeRange[j].end_date = this.getdate(this.data.tspd[i].timeRange[j].endTime);
        }
      }
      this.mockData = [];
      for (let i = 0; i < this.data.tspd.length; i++) {
        var t = i + 1;
        this.mockData.push({
          key: i, title: '点' + t, description: this.data.tspd[i], chosen: false,
        })
      }
    },
    AddLine_Tspd(index) {
      var temp = {
        startTime: '',
        endTime: '',
        start_date: '',
        end_date: '',
        start_time: '',
        end_time: ''
      };
      var list = this.data.tspd[index].timeRange;
      list.push(temp);
      this.data.tspd[index].timeRange = [...list];
    },
    // change_time_range(index, ind) {
    //   this.data.tspd[index].timeRange[ind].startTime = this.data.tspd[index].timeRange[ind].start_date + ',' + this.data.tspd[index].timeRange[ind].start_time;
    //   this.data.tspd[index].timeRange[ind].endTime = this.data.tspd[index].timeRange[ind].end_date + ',' + this.data.tspd[index].timeRange[ind].end_time;
    // },
    change_ZuoBiaoYuanDian(e) {
      this.data.NowZuoBiaoYuanDian = e;
    },
    change_wangpingcha() {

      if (!this.wangpingcha) {
        WangpingchaInit().then((res) => {
          if (res.code == 0) {
            this.wangpingchaFile.acousticPositioningPath = res.data.acousticPositioningPath;
            this.wangpingchaFile.posAnsFile = res.data.posAnsFile;
            this.wangpingchaFile.posObsFile = res.data.posObsFile;
            this.wangpingchaFile.tspdDepObs = res.data.tspdDepObs;
            this.wangpingchaFile.svpdata = res.data.svpdata;
            this.configList = res.data.cfg;

          }
        })
      }

      this.wangpingcha = !this.wangpingcha;
    },
    ExcutePosition() {
      var temp = {
        ...this.data,
        RobustModel: this.RandomStructureList[this.data.random_structure].value,
        distanceMethod: this.JuLiGuanCeZhi_List[this.data.NowJuLiGuanCeZhi].value,
        errorModels: []
      };

      temp.tspd = [];
      this.targetKeys.forEach(element => {
        temp.tspd.push(this.mockData[element].description);
      });

      temp.acousticPositioningPath = this.acousticPositioningPathList[this.data.acousticPositioningPath].path;
      for (var i = 0; i < this.model_function.length; i++)
        if (this.model_function[i].checked)
          temp.errorModels.push({ modelName: this.model_function[i].value });
      for (var i = 0; i < temp.tspd.length; i++)
        temp.tspd[i].svpPath = this.svpList[this.data.svpPath].path;
      if (this.data.NowZuoBiaoYuanDian == 0) delete temp.XYZ0;
      this.loading = true;

      excuteAPI(temp).then((res) => {
        this.loading = false;
        console.log(res);
        if (res.code == 0) {
          this.$store.commit('setDemarcate', res.data);
          this.$message.success('执行成功！');
          this.$router.push('/calibration/result');
        }
      });
    },
    ExcuteWangPingcha() {

      var temp = {
        posansfile_path: this.wangpingchaFile.posAnsFile[this.data.wangpingchaFile.posAnsFile].path,
        posobsfile_path: this.wangpingchaFile.acousticPositioningPath[this.data.wangpingchaFile.acousticPositioningPath].path,
        tspdttobsfile_path: this.wangpingchaFile.posObsFile[this.data.wangpingchaFile.posObsFile].path,
        tspddepobsfile_path: this.wangpingchaFile.tspdDepObs[this.data.wangpingchaFile.tspdDepObs].path,
        svpfile_path: this.wangpingchaFile.svpdata[this.data.wangpingchaFile.svpdata].path,
        netmod: this.data.net_structure ? 1 : 2,
        robmod: this.RandomStructureList[this.data.random_structure].value
      };


      console.log(temp);
      this.loading = true;
      ExcuteWangpingcha(temp).then((res) => {
        this.loading = false;
        console.log(res);
        if (res.code == 0) {
          this.$store.commit('setDemarcate', res.data);
          this.$message.success('执行成功！');
          this.$router.push('/calibration/result');
        }
      });

    },
    Excute() {
      if (!this.wangpingcha) {
        this.ExcutePosition();
      } else {
        this.ExcuteWangPingcha();
      }

    },
    handleChange(targetKeys, direction, moveKeys) {
      console.log(targetKeys, direction, moveKeys);
      this.targetKeys = targetKeys
    },
    change_net_structure() {
      this.data.net_structure = !this.data.net_structure;

    },
    change_random_structure(e) {
      this.data.random_structure = e;
    },
    change_distance_structure(e) {
      this.data.NowJuLiGuanCeZhi = e;
    },
    change_function_module(e) {
      this.model_function[e].checked = !this.model_function[e].checked;
    },
    initdata() {
      init().then((res) => {
        console.log(res)
        if (res.code == 0) {
          this.acousticPositioningPathList = res.data.acousticPositioningPath;
          this.svpList = res.data.svpdata;
          this.configList = res.data.cfg;
          this.data.acousticPositioningPath = 0;
          this.data.svpPath = 0;
          this.$message.success('初始化数据成功', 0.5);
          // this.tspd.forEach(element1 => {
          //   element1.timeRange.forEach(element => {

          //   });
          // });

        } else {
          this.$message.success(res.message, 0.5);
        }

      })
    },
    updateServeData() {
      this.$message.success('开始更新数据！');
      var that = this;
      var data = { "folders": "postioning"};
      UpdateServeData(data).then((res) => {
        if (res.code == 0) {
          that.$message.success('更新数据完成');
        }
      });
    }
  },
  mounted() {
    this.initdata();

    this.chaifentime();
  },
};
</script>

<style>
.group {
  margin-left: 20px;
  margin-top: 30px;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.dragger {
  height: 140px;
  width: 350px;
  margin-left: 10px;
}

.primary {
  height: 40px;
  width: 100px;
  font-size: 16px;
  border-radius: 10px
}


.timebox {
  width: 200px;
  margin-top: 5px;
}

.table {
  width: 25%;
  height: 30px;
  align-items: center;
  display: flex;
  justify-content: center;
}

.table_five {
  width: 20%;
  height: 40px;
  align-items: center;
  display: flex;
  justify-content: center;
}
</style>
