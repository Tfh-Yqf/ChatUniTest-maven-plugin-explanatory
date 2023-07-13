<template>
  <div style="font-weight: bold">
    <a-card title="参数配置" :loading="loading">
      <a-spin :spinning="loading">

        <a-card title="参数选取">
          <a-divider  orientation="left">参数配置</a-divider>
          <div style="display: flex;align-items: center;width: 100%"
               v-if="init_data.config != null ">
            <div style="width: 150px;flex-shrink: 0;margin-left: 20px;">参数配置文件</div>
            <a-select @change="changeConfig" v-model.number="NowConfig" placeholder="参数配置文件" style="width: 50%">
              <a-select-option :value="index" v-for="(item, index) in init_data.config" :key="index">{{ item.name
                }}</a-select-option>
            </a-select>
          </div>

          <a-divider orientation="left">时间设置</a-divider>
          <div style="display: flex;flex-direction: column;">
            <div style="display: flex;flex-direction: row;align-items: center">
              <div style="margin-left: 20px">开始时间</div>
              <a-date-picker v-model="time.start_date" placeholder="开始日期" valueFormat="YYYY-MM-DD"
                             style="width: 150px;margin-left: 10px" />
              <a-time-picker v-model="time.start_time" placeholder="开始时间" valueFormat="HH:mm:ss"
                             style="margin-left: 10px"></a-time-picker>
              <a-input-number defaultValue="872" :min="0" :max="1000" v-model="time.start_ms"
                              style="margin-left: 10px"></a-input-number>
              <div style="margin-left: 5px">毫秒</div>


              <div style="margin-left: 20px;">结束时间</div>
              <a-date-picker v-model="time.end_date" placeholder="结束日期" valueFormat="YYYY-MM-DD"
                             style="width: 150px;margin-left: 10px" />
              <a-time-picker v-model="time.end_time" placeholder="结束时间" valueFormat="HH:mm:ss"
                             style="margin-left: 10px"></a-time-picker>
              <a-input-number defaultValue="872" :min="0" :max="1000" v-model="time.end_ms"
                              style="margin-left: 10px"></a-input-number>
              <div style="margin-left: 5px">毫秒</div>

            </div>

            <div style="display: flex;flex-direction: row;">
              <div style="width: 100%;display: flex;flex-direction: column;">
                <a-divider orientation="left" style="margin-top: 40px">算法设置</a-divider>

                <div style="display: flex;flex-direction: row;margin-left: 20px;width: 60%;margin-top: 10px;'">
                  <div style="width: 100px;flex-shrink: 0">算法选取</div>
                  <a-select style="width: 70%" v-model.number="data.Algorithm" @change="change_suanfa">
                    <a-select-option v-for="(it,ind) in AlgorithmMenu" :value="ind" :key="ind">{{it.name}}</a-select-option>
                  </a-select>
                </div>

                <div
                    style="display: flex;flex-direction: row;margin-left: 20px;width: 60%;margin-top: 30px;align-items: center">
                  <div style="width: 100px;flex-shrink: 0">是否抗差</div>
                  <a-select defaultValue="kangcha" v-model.number="data.KangCha" style="width: 70%">
                    <a-select-option :value="false">非抗差</a-select-option>
                    <a-select-option :value="true">抗差</a-select-option>

                  </a-select>
                </div>

                <div
                    style="display: flex;flex-direction: row;margin-left: 20px;width: 60%;margin-top: 30px;align-items: center;"
                    v-if="data.Algorithm == 0">
                  <div style="width: 100px;flex-shrink: 0">单双程模式</div>
                  <a-select defaultValue="kangcha" v-model.number="data.DanShuangCheng" style="width: 70%">
                    <a-select-option v-for="(item, index) in DanShuang" :key="index" :value="item.value">{{ item.name
                      }}</a-select-option>

                  </a-select>
                </div>

                <div
                    style="display: flex;flex-direction: row;margin-left: 20px;width: 60%;margin-top: 30px;align-items: center;">
                  <div style="width: 100px;flex-shrink: 0">自适应滤波</div>
                  <a-select defaultValue="kangcha" v-model.number="data.shiying" style="width: 70%">
                    <a-select-option :value="true">自适应</a-select-option>
                    <a-select-option :value="false">非自适应</a-select-option>
                  </a-select>
                </div>

                <div
                    style="display: flex;flex-direction: row;margin-left: 20px;width: 70%;margin-top: 30px;align-items: center">
                  <div style="width: 100px;flex-shrink: 0">状态参数选取</div>
                  <a-checkbox :disabled="item.disabled" :checked="item.checked" @change="change_zhuangtai(index)"
                              v-for="(item, index) in zhuangtai_canshu" style="width: 400px" :key="item.value">

                    {{ item.label }}
                  </a-checkbox>
                </div>
              </div>
            </div>


          </div>

          <div style="display: flex;">

            <a-card title="文件选取" style="margin-top: 20px;width: 49%;" :loading="fileLoading">
              <div style="display: flex;flex-direction: column;">
                <div class="file_group">
                  <div style="flex-shrink: 0;font-size: 16px;width: 150px;">声速剖面数据选取</div>
                  <a-select v-model.number="file.svp" style="width: 42%" placeholder="请选择声速剖面数据数据">
                    <a-select-option :value="item.path" v-for="(item, index) in init_data.svp" :key="item.path">{{
                        item.name }}
                    </a-select-option>
                  </a-select>
                </div>

                <div class="file_group">
                  <div style="flex-shrink: 0;font-size: 16px;width: 150px;">声学数据选取</div>
                  <a-select v-model.number="file.navigation" style="width: 42%" placeholder="请选择声学数据">
                    <a-select-option :value="item.path" v-for="(item, index) in init_data.navigation" :key="item.path">{{
                        item.name }}
                    </a-select-option>
                  </a-select>
                </div>

                <div class="file_group" v-if="data.Algorithm != 0">
                  <div style="flex-shrink: 0;font-size: 16px;width: 150px;">声学导航结果数据选取</div>
                  <a-select v-model.number="file.acoupos" style="width: 42%" placeholder="声学导航结果数据选取">
                    <a-select-option :value="index" v-for="(item, index) in init_data.acoupos" :key="index">{{
                        item.name }}
                    </a-select-option>
                  </a-select>
                </div>

                <div class="file_group" v-if="data.Algorithm != 0">
                  <div style="flex-shrink: 0;font-size: 16px;width: 150px;">惯导数据选取</div>
                  <a-select v-model.number="file.imu" style="width: 42%" placeholder="惯导数据选取">
                    <a-select-option :value="index" v-for="(item, index) in init_data.imu" :key="index">{{
                        item.name }}
                    </a-select-option>
                  </a-select>
                </div>

                <div class="file_group" v-if="data.Algorithm != 0">
                  <div style="flex-shrink: 0;font-size: 16px;width: 150px;">DVL数据选取</div>
                  <a-select v-model.number="file.dvl" style="width: 42%" placeholder="DVL数据选取">
                    <a-select-option :value="index" v-for="(item, index) in init_data.dvl" :key="index">{{
                        item.name }}
                    </a-select-option>
                  </a-select>
                </div>
                <div class="file_group" v-if="data.Algorithm != 0">
                  <div style="flex-shrink: 0;font-size: 16px;width: 150px;">海洋内波数据选取</div>
                  <a-select style="width: 42%" placeholder="">
                    
                  </a-select>
                </div>

              </div>
            </a-card>

            <a-card title="杆臂参数设置" style="margin-top: 20px;width: 49%;margin-left: 1%;">
              <div style="display: flex;flex-direction: row;">

                <div style="display: flex;flex-direction: column;text-align: center;margin-top: 10px">
                  <div style="display: flex;flex-direction: row;">
                    <div style="width: 200px;">

                    </div>
                    <div style="width: 100px;">
                      Right
                    </div>
                    <div style="width: 100px;">
                      Front
                    </div>
                    <div style="width: 100px;">
                      Up
                    </div>
                  </div>

                  <div style="display: flex;flex-direction: row;margin-top: 10px;align-items: center">
                    <div style="width: 200px;">
                      SINS-UANP
                    </div>
                    <div style="width: 100px;">
                      <a-input-number v-model="data.GanBi.SUNS_UANP.right"></a-input-number>
                    </div>
                    <div style="width: 100px;">
                      <a-input-number v-model="data.GanBi.SUNS_UANP.front"></a-input-number>
                    </div>
                    <div style="width: 100px;">
                      <a-input-number v-model="data.GanBi.SUNS_UANP.up"></a-input-number>
                    </div>
                  </div>

                  <div style="display: flex;flex-direction: row;margin-top: 10px;align-items: center">
                    <div style="width: 200px;">
                      SINS-DVL
                    </div>
                    <div style="width: 100px;">
                      <a-input-number v-model="data.GanBi.SUNS_DVL.right"></a-input-number>
                    </div>
                    <div style="width: 100px;">
                      <a-input-number v-model="data.GanBi.SUNS_DVL.front"></a-input-number>
                    </div>
                    <div style="width: 100px;">
                      <a-input-number v-model="data.GanBi.SUNS_DVL.up"></a-input-number>
                    </div>
                  </div>
                </div>


              </div>
            </a-card>
          </div>

        </a-card>


        <a-row :gutter="8" style="margin-top: 8px;">
          <a-col :span="12">
            <config-noisy :items="configNoisy" />
          </a-col>
          <a-col :span="12">
            <NavgiationConfig :items="NavgiationConfig" />
          </a-col>
        </a-row>

      </a-spin>

      <div style="margin-top: 32px;display: flex;flex-direction: row;justify-content: space-around">
        <a-popconfirm title="确定执行吗?" @confirm="excute">
          <a-button type="primary" :loading="loading" style="width: 150px;border-radius: 10px;" size="large">执行算法
          </a-button>
        </a-popconfirm>
        <button type="button" class="ant-btn ant-btn-primary ant-btn-lg" @click="updateServeData"
              style="width: 100px;border-radius: 20px;margin-left: 20px;"><span>更新数据</span></button>

        <a-popconfirm title="确定Plot吗?" @confirm="plot">
          <a-button type="primary" :loading="loading" style="width: 150px;border-radius: 10px;" size="large">画图
          </a-button>
        </a-popconfirm>
      </div>
    </a-card>

    <div v-if="loading"
         style="width: 100%;display: flex;justify-content: center;background: #FFFFFF;height: 200px;align-items: center">
      <a-progress type="circle" :percent="percent"></a-progress>
    </div>

  </div>
</template>

<script>
import { NavgiationDanyi, Navgiation_initdata, NavigationGetConfig, NavigationExcuteOther } from '@/api/Navgiation'
import { UpdateServeData } from '@/api/sound'


export default {
  components: {
    ConfigNoisy: () => import("./ConfigNoisy.vue"),
    NavgiationConfig: () => import('./Navgiation_config.vue')
  },
  data() {
    return {
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
      action_url: this.server_url + '/upload',
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
      config: null
    };
  },
  methods: {
    changeConfig() {

      NavigationGetConfig(this.init_data.config[this.NowConfig]).then((res) => {
        this.$message.success("加载配置成功");
        if (res.code == 0) {
          console.log(res);
          this.config = res.data;
          if("dvl" in res.data){
            this.data.Algorithm = '153p';
            this.change_suanfa('153p');
            this.time.start_date = res.data.timestaend[0]+"-"+res.data.timestaend[1]+"-"+res.data.timestaend[2];
            this.time.start_time = res.data.timestaend[3]+":"+res.data.timestaend[4]+":"+res.data.timestaend[5];
            this.time.end_date  = res.data.timestaend[6]+"-"+res.data.timestaend[7]+"-"+res.data.timestaend[8];
            this.time.end_time  = res.data.timestaend[9]+":"+res.data.timestaend[10]+":"+res.data.timestaend[11];
            console.log("------------------")
            console.log(this.AlgorithmMenu)
            for(var i=0;i<this.AlgorithmMenu.length;i++){
              if(this.AlgorithmMenu[i].value ==res.data.function){
                this.data.Algorithm = i;
              }
            }

            console.log(this.data.Algorithm);
            this.data.shiying = res.data.rkflag==1?true:false


            this.data.KangCha = res.data.robmodel==1?true:false

            console.log(this.init_data)

            for(var i=0;i<this.init_data.imu.length;i++){
              if(res.data.imu==this.init_data.imu[i].name){
                this.file.imu = i;
              }
            }

            for(var i=0;i<this.init_data.acoupos.length;i++){
              if(res.data.acoupos==this.init_data.acoupos[i].name){
                this.file.acoupos = i;
              }
            }

            for(var i=0;i<this.init_data.dvl.length;i++){
              if(res.data.dvl==this.init_data.dvl[i].name){
                this.file.dvl = i;
              }
            }

          }else{
            this.change_suanfa('Algorithm01');
            // 替换
            this.data.navigationPath = res.data.navigationPath;
            this.NavgiationConfig[0].children[2].config = res.data.mes;
            this.NavgiationConfig[0].children[3].config = res.data.mes_size;

            this.config.DatumStationPath = res.data.DatumStationPath;
            this.config.outputPath = res.data.outputPath;
            var time_arr = res.data.start_date.split(',');
            this.time.start_date = time_arr[0] + "," + time_arr[1] + "," + time_arr[2];
            this.time.start_time = time_arr[3] + "," + time_arr[4] + "," + time_arr[5].substr(0, 3);
            this.time.start_ms = time_arr[5].substr(4);

            time_arr = res.data.end_date.split(',');
            this.time.end_date = time_arr[0] + "," + time_arr[1] + "," + time_arr[2];
            this.time.end_time = time_arr[3] + "," + time_arr[4] + "," + time_arr[5].substr(0, 3);
            this.time.end_ms = time_arr[5].substr(4);
            var arr = res.data.svpPath.split('/');
            var sz = arr.length;
            this.file.svp = arr[sz - 1];
            arr = res.data.navigationPath.split('/');
            sz = arr.length;
            this.file.navigation = arr[sz - 1];

            if (res.data.waymode == 1) {
              this.data.DanShuangCheng = "dan";
            } else if (res.data.waymode == 2) {
              this.data.DanShuangCheng = "shuang";
            }
          }


        }
      })
    },
    LoadFile() {
      if (this.file.svp == '' || this.file.navigation == '') {
        this.$message.warn('文件未选择!');
        return;
      }
      var that = this;
      this.fileLoading = true;
      setTimeout(function () {
        that.LoadComplete = true;
        that.fileLoading = false;

        that.$message.success('加载成功');
      }, 2000);


    },
    init() {
      this.loading = true;
      Navgiation_initdata().then((res) => {
        if (res.code == 0) {
          console.log("init_data")
          console.log(res.data)
          this.loading = false;
          this.init_data = res.data;
          this.$message.success(res.message, 0.5);

          this.changeConfig();
        }
      }).finally((res) => {
        this.loading = false;

      })
    },
    change_zhuangtai(e) {
      this.zhuangtai_canshu[e].checked = !this.zhuangtai_canshu[e].checked;
    },
    ExcuteDanYi() {
      var temp = {
        config_path: this.init_data.config[this.NowConfig].path,
        output_path: this.config.outputPath,
        DatumStationPath: this.config.DatumStationPath
      };
      console.log(temp);
      NavgiationDanyi(temp).then((res) => {
        console.log(res);
        if (res.code != 0 || res.code == undefined) {
          this.$message.error(res.message || '错误！');
        } else {
          this.percent = 100;
          this.$message.success('执行成功！');
          this.$store.commit('setNavgiation', res);
          var that = this;
          setTimeout(function () {
            that.$router.push('/navigation/navigation_result');
          }, 150);

        }
        this.loading = false;
      }).finally(() => {
        this.loading = false;
        this.percent = 100;
        if (this.percent_interval) clearInterval(this.percent_interval);
      });
    },
    ExcuteOther() {
      console.log("执行中。。。")
      var temp = {
        timestaend: [],
        robmodel: this.data.KangCha ? 1 : 0,
        rkflag: this.data.shiying?1:0,
        name: this.AlgorithmMenu[this.data.Algorithm].value,
        acoupos:this.init_data.acoupos[this.file.acoupos].name,
        imu:this.init_data.imu[this.file.imu].name,
        dvl:this.init_data.dvl[this.file.dvl].name,
        function:this.AlgorithmMenu[this.data.Algorithm].value
      };
      console.log(this.time);
      var last = 0;
      for(var i=0;i<this.time.start_date.length;i++){
        if(this.time.start_date[i]=='-'){
          temp.timestaend.push(parseFloat( this.time.start_date.substring(last, i) ));
          last = i+1;
        }
      }
      temp.timestaend.push(parseFloat(this.time.start_date.substring(last,this.time.start_date.length )));

      last = 0;
      for(var i=0;i<this.time.start_time.length;i++){
        if(this.time.start_time[i]==':'){
          temp.timestaend.push(parseFloat(this.time.start_time.substring(last, i)));
          last = i+1;
        }
      }
      temp.timestaend.push(parseFloat(this.time.start_time.substring(last,this.time.start_time.length )));

      last = 0;
      for(var i=0;i<this.time.end_date.length;i++){
        if(this.time.end_date[i]=='-'){
          temp.timestaend.push(parseFloat(this.time.end_date.substring(last, i)));
          last = i+1;
        }
      }
      temp.timestaend.push(parseFloat(this.time.end_date.substring(last,this.time.end_date.length )));

      last = 0;
      for(var i=0;i<this.time.end_time.length;i++){
        if(this.time.end_time[i]==':'){
          temp.timestaend.push(parseFloat(this.time.end_time.substring(last, i)));
          last = i+1;
        }
      }
      temp.timestaend.push(parseFloat(this.time.end_time.substring(last,this.time.end_time.length )));
      console.log(temp);

      console.log("执行navigationExcuteOther")


      NavigationExcuteOther(temp).then((res) => { 
        if (res.code != 0 || res.code == undefined) {
          this.$message.error(res.message || '错误！');
        } else {
          this.percent = 100;
          this.$message.success('执行成功！');
          this.$store.commit('setNavgiation', res);
          var that = this;
          setTimeout(function () {
            that.$router.push('/navigation/result_other');
          }, 150);
        }
      })
    },
    excute() {
      // 开始执行
      console.log("开始执行")
      console.log(this.data.Algorithm);

      // 开始调用接口，并加载进度条
      this.loading = true;
      var that = this;
      that.percent = 0;
      this.percent_interval = setInterval(function () {
        that.percent += 10;
        if (that.percent >= 100) {
          clearInterval(that.percent_interval)
        }
      }, 1500);
      if (this.data.Algorithm == 0) {
        // 单一声学 执行
        this.ExcuteDanYi();
      } else {
        this.ExcuteOther();
      }
    },
    plot() {
      this.excute();
    },
    file_upload_DVL(e) {
      this.file.DVL = e.file.response;
    },
    change_suanfa(e) {

      var temp = JSON.parse(JSON.stringify(this.zhuangtai_canshu));
      this.zhuangtai_canshu.splice(0, this.zhuangtai_canshu.length);
      console.log(temp);
      console.log(e);
      if (e == 'Algorithm01') {

        for (var i = 0; i < temp.length; i++) {
          if (i >= 2) {
            temp[i].checked = false;
            temp[i].disabled = true;
          } else {
            temp[i].disabled = false;
            temp[i].checked = true;
          }
        }

      } else {
        for (var i = 0; i < temp.length; i++) {
          temp[i].checked = true;
          temp[i].disabled = false;
        }
      }

      this.zhuangtai_canshu = JSON.parse(JSON.stringify(temp));
      console.log(this.zhuangtai_canshu);
    },
    updateServeData() {
      this.$message.success('开始更新数据！');
      var that = this;
      var data = { "folders": "ANModule,loose_combination,TERCOM"};
      UpdateServeData(data).then((res) => {
        if (res.code == 0) {
          that.$message.success('更新数据完成');
        }
      });
    }
  },
  mounted() {
    this.init();
  },
};
</script>

<style>
.file_group {
  display: flex;
  margin-top: 20px;
  align-items: center;
  font-weight: normal;
}
</style>
