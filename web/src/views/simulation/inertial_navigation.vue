<template>
  <div style="display: flex;flex-direction: column">
    <a-card :loading="loading" title="轨迹列表">
      <div class="card-body fw-bold text-center">
        <ul class="nav nav-tabs" id="myTab" role="tablist">
          <li class="nav-item" role="presentation">
            <button class="nav-link active" id="home-tab" data-bs-toggle="tab" data-bs-target="#home-tab-pane"
                    type="button" role="tab" aria-controls="home-tab-pane" aria-selected="true">表格填写
            </button>
          </li>
          <li class="nav-item" role="presentation">
            <button class="nav-link" id="profile-tab" data-bs-toggle="tab" data-bs-target="#profile-tab-pane"
                    type="button" role="tab" aria-controls="profile-tab-pane" aria-selected="false">文件选取
            </button>
          </li>
        </ul>
        <div class="tab-content" id="myTabContent">
          <div class="tab-pane fade show active" id="home-tab-pane" role="tabpanel" aria-labelledby="home-tab"
               tabindex="0">
            <div style="display: flex;flex-direction: row;width: 100%;background-color: #fafafa;">
              <div class="table_path">序号</div>
              <div class="table_path" style="width: 40%">航迹类型</div>
              <div class="table_path">航行时间</div>
              <div class="table_path">角速度</div>
              <div class="table_path">加速度</div>
            </div>

            <div style="display: flex;flex-direction: row;width: 100%;margin-top: 5px"
                 v-for="(item, index) in datasource_path" :key="item.id">
              <div class="table_path">
                {{ item.id }}
              </div>
              <div class="table_path" style="width: 40%">
                <a-select style="width: 70%" v-model.number="item.type">
                  <a-select-option :value="it" v-for="(it, ind) in type_option" :key="it">{{
                      it
                    }}
                  </a-select-option>
                </a-select>
              </div>
              <div class="table_path">
                <a-input-number v-model="item.time" defaultValue="0" style="width: 70%"></a-input-number>
              </div>
              <div class="table_path">
                <a-input-number v-model="item.palstance" defaultValue="0"
                                style="width: 70%"></a-input-number>
              </div>
              <div class="table_path">
                <a-input-number v-model="item.accelerated_speed" defaultValue="0"
                                style="width: 70%"></a-input-number>
              </div>
            </div>

            <div style="width: 100%;display: flex;flex-direction: row-reverse;margin-top: 10px;">
              <a-button type="primary" style="margin-right: 5%" @click="add_datasource_path">添加行</a-button>
            </div>
          </div>

          <div class="tab-pane fade" id="profile-tab-pane" role="tabpanel" aria-labelledby="profile-tab"
               tabindex="0">
            <div style="display: flex;align-items: center;width: 100%">
              <div style="width: 130px; margin: 20px;">航迹列表数据:</div>
              <a-select v-model.number="data.path" placeholder="航迹列表数据" style="width: 50%">
                <a-select-option :value="item" v-for="(item, index) in initdatas.path" :key="item.id">{{
                    item.name
                  }}
                </a-select-option>
              </a-select>
            </div>
          </div>
        </div>
      </div>
    </a-card>
    <a-card title="初始参数配置" style="margin-top: 10px">
      <div class="card-body fw-bold text-center">
        <div class="row row1">
          <div class="col">惯导采样率:</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.insrate"/>
          </div>
          <div class="col"></div>
          <div class="col"></div>
        </div>
        <div class="row row1">
          <div class="col"></div>
          <div class="col left_">Pitch</div>
          <div class="col left_">Roll</div>
          <div class="col left_">Heading</div>
        </div>
        <div class="row row1">
          <div class="col">初始姿态：</div>
          <div class="col">
            <a-input-number  class="form-control large_table_path" v-model="data.initatt[0]"/>
          </div>
          <div class="col">
            <a-input-number  class="form-control large_table_path" v-model="data.initatt[1]"/>
          </div>
          <div class="col">
            <a-input-number  class="form-control large_table_path" v-model="data.initatt[2]"/>
          </div>
        </div>
        <div class="row row1">
          <div class="col"></div>
          <div class="col label1">E</div>
          <div class="col label1">N</div>
          <div class="col label1">U</div>
        </div>
        <div class="row row1">
          <div class="col">初始速度：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.initvel[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.initvel[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.initvel[2]"/>
          </div>
        </div>
        <div class="row row1">
          <div class="col">初始位置：</div>
          <div class="col">
            <a-input-number  class="form-control large_table_path" v-model="data.initpos[0]"/>
          </div>
          <div class="col">
            <a-input-number  class="form-control large_table_path" v-model="data.initpos[1]"/>
          </div>
          <div class="col">
            <a-input-number  class="form-control large_table_path" v-model="data.initpos[2]"/>
          </div>
        </div>
      </div>
    </a-card>


    <a-card :loading="loading" title="海洋环境参数" style="margin-top: 10px">
      <div class="card-body fw-bold text-center">
        <div class="row row1">
          <div class="col">是否模拟海洋环境参数</div>
          <div class="col"><a-radio :checked="moni" @click="change_moni">模拟</a-radio></div>
          <div class="col"><a-radio :checked="!moni" @click="change_moni">不模拟</a-radio></div>


        </div>
        <!-- <div class="row row1">
          <div class="col">潮汐：</div>
          <div class="col">
            振 幅： <input type="text" class="form-control little_group" v-model="data.tideamp"> deg
          </div>
          <div class="col">
            周 期： <input type="text" class="form-control little_group" v-model="data.tideper"> h
          </div>
        </div>
        <div class="row row1">
          <div class="col" style="position: relative; top: 25px;">波浪：</div>
          <div class="col">
            角振幅：<input type="text" class="form-control little_group" v-model="data.waveangamp"> deg
          </div>
          <div class="col">
            角周期：<input type="text" class="form-control little_group" v-model="data.waveangper"> m
          </div>
        </div>

        <div class="row row1">
          <div class="col"></div>
          <div class="col">
            线振幅：<input type="text" class="form-control little_group" v-model="data.wavelinamp"> deg
          </div>
          <div class="col">
            线周期：<input type="text" class="form-control little_group" v-model="data.wavelinper"> m
          </div>
        </div> -->
        <div class="row row2">
          <div class="col">潮汐：</div>
          <div class="col"></div>
          <div class="col"></div>
          <div class="col"></div>
        </div>
        <div class="row row1">
          <div class="col">振幅(单位/deg)：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.tideamp"/>
          </div>
          <div class="col">周期(单位/h)：</div>
          <div class="col"><a-input-number class="form-control large_table_path" v-model="data.tideper"/></div>
        </div>
        <div class="row row2">
          <div class="col">波浪：</div>
          <div class="col"></div>
          <div class="col"></div>
          <div class="col"></div>
        </div>
        <div class="row row1">
          <div class="col">角振幅(单位/deg)：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.waveangamp[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.waveangamp[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.waveangamp[2]"/>
          </div>
        </div>
        <div class="row row1">
          <div class="col">角周期(单位/m)：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.waveangper[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.waveangper[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.waveangper[2]"/>
          </div>
        </div>
        <div class="row row1">
          <div class="col">线振幅(单位/deg)：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.wavelinamp[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.wavelinamp[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.wavelinamp[2]"/>
          </div>
        </div>
        <div class="row row1">
          <div class="col">线周期(单位/m)：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.wavelinper[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.wavelinper[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.wavelinper[2]"/>
          </div>
        </div>
      </div>
    </a-card>

    <a-card :loading="loading" title="惯性导航仪器误差参数设置" style="margin-top: 10px">
      <div class="card-body fw-bold text-center">
        <div class="row row1">
          <div class="col"></div>
          <div class="col">X</div>
          <div class="col">Y</div>
          <div class="col">Z</div>
        </div>
        <div class="row row1">
          <div class="col">陀螺仪初始偏差：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.gb[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.gb[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.gb[2]"/>
          </div>
        </div>
        <div class="row row1">
          <div class="col">陀螺仪比例因子误差标准偏差：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.gsf[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.gsf[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.gsf[2]"/>
          </div>
        </div>
        <div class="row row1">
          <div class="col">陀螺标度因子误差功率谱密度：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.gsfpsd[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.gsfpsd[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.gsfpsd[2]"/>
          </div>
        </div>
        <div class="row row1">
          <div class="col">加速度计初始偏差：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.ab[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.ab[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.ab[2]"/>
          </div>
        </div>
        <div class="row row1">
          <div class="col">加速度计比例因子误差标准偏差：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.asf[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.asf[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.asf[2]"/>
          </div>
        </div>
        <div class="row row1">
          <div class="col">加速度计标度因子误差功率谱密度：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.asfpsd[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.asfpsd[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.asfpsd[2]"/>
          </div>
        </div>
        <div class="row row1">
          <div class="col">角度随机游走：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.arw[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.arw[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.arw[2]"/>
          </div>
        </div>

        <div class="row row1">
          <div class="col">速度随机游走：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.vrw[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.vrw[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.vrw[2]"/>
          </div>
        </div>
        <div class="row row1">
          <div class="col">角速度随机游走：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.arrw[0]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.arrw[1]"/>
          </div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.arrw[2]"/>
          </div>
        </div>
        <div class="row row1">
          <div class="col">一阶马尔可夫过程的相关时间：</div>
          <div class="col">
            <a-input-number class="form-control large_table_path" v-model="data.cortime"/>
          </div>
          <div class="col"></div>
          <div class="col"></div>
        </div>
      </div>
    </a-card>

    <div style="height: 100px;width: 100%;display: flex;align-items: center;justify-content: center">
      <a-popconfirm title="确定执行吗?" @confirm="Excute">
        <a-button style="width: 100px;height: 40px" type="primary">执行算法</a-button>
      </a-popconfirm>
    </div>

  </div>
</template>

<script>
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.bundle"
import {simulation_api, simulationInitdata, SimulationExcute} from '@/api/simulation'

export default {
  data() {
    return {
      loading:false,
      moni:false,
      datasource_path: [],
      type_option: ["classic_none","static", "uniform", "accelerate", "decelerate", "pitchleft", "pitchright", "rollleft", "rollright", "headingleft", "headingright", "turnleft", "turnright", "turnup", "turndown", "coturnleft", "coturnright", "coturnup", "coturndown", "turns", "coturns"],
      path_template: {id: 1, type: 'static', time: 0, palstance: 0, accelerated_speed: 0},
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
        waveangamp: [5,5,3],	//角振幅
        waveangper: [5,7,10],	//角周期
        wavelinamp: [1,1,0.5],//	线振幅
        wavelinper: [5,7,10],//	线周期
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
    }
  },
  methods: {
    change_moni(){
      this.moni=!this.moni;
    },
    Excute(){
      var temp = this.$store.state.system.SimulationConfig;
      var svpfile = '';
      console.log(temp);
      if(temp==undefined||'svpfile'in temp == false){
        this.$message.error('svpfile未选择');
        return;
      }
      var moni = 0;
      if('moni' in temp==false){
        moni = 0;
        this.change_moni();
      }

      // // var temp = {
      // //   datatype:1,
      // //   tratype:"classic_none",
      // //   svpfile:svpfile,
      // //   oepflag:moni
      // // };
      // this.loading = true;
      
      // console.log(temp)
      // console.log("aaaaaaaaaa")

      // SimulationExcute(temp).then((res)=>{
      //   this.loading = false;
      //   if(res.code==0){
      //     console.log(res.data)
      //     var temp=[res.data[1],res.data[3]]
      //     res.data=temp
      //     this.$store.commit('setSimulationResult',res.data);
      //     this.$router.push("/simulation/simulation_result");
      //   }

      // })
      console.log(this.data)
      var temp = {
        datatype:1,
        tratype:"classic_none",
        svpfile:svpfile,
        oepflag:moni,
        data:this.data,

      };
      let files=["insdataavp.txt","insdataimu.txt"]
      // console.log(temp)
      this.loading = true;
      SimulationExcute(temp).then((res)=>{
        this.loading = false;
        if(res.code==0){
          // var temp=[res.data[1],res.data[3]]
          // res.data=temp
          let result=[]
          // console.log(res)
          for(let i=0;i<res.data.length;++i){
            console.log(res.data[i].name)
            if(files.indexOf(res.data[i].name)>-1){
              result.push(res.data[i])
            }
          }
          // console.log(result)
          this.$store.commit('setSimulationResult',result);
          this.$router.push({
            path:"/simulation/simulation_result",
            query:{
              runway:"inertial_navigation"
            }
          });
        }

      })
    },
    init_data() {
      simulationInitdata().then((res) => {
        if (res.code == 0) {
          this.initdatas.path = res.data.path;
          console.log(this.initdatas.path);
          this.$message.success('执行成功!', 0.5);
        } else {
          this.$message.success(res.message, 0.5);
        }
      })
      this.add_datasource_path();

    },
    add_datasource_path() {
      var temp = JSON.parse(JSON.stringify(this.path_template));
      temp.id = this.datasource_path.length + 1;
      this.datasource_path.push(temp);
    },
  },
  mounted() {
    this.init_data();
    var temp = this.$store.state.system.SimulationConfig;
    temp.data=this.data
    this.$store.commit('setSimulationConfig',temp);
  }
}
</script>

<style scoped>
.row1 {
  margin-top: 10px;
}

.row2 {
  margin-top: 20px;
}
.table_path {
  width: 25%;
  height: 45px;
  align-items: center;
  display: flex;
  justify-content: center;
}

.large_table_path{
  width: 75%;
  height: 45px;
  align-items: center;
  display: flex;
  justify-content: center;
}

.little_group {
  width: 250px;
  display: inline;
  justify-content: center;
}

.input1 {
  width: 250px;
}

.left_{
  justify-content: left;
}

.small_group{
  width: 75px;
  display: inline;
}

</style>
  