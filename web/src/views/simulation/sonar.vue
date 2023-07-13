<template>
  <div>
    <div style="display: flex;margin-top: 10px;">
      <div style="width: 49%;">
        <a-card :loading="loading" title="应答器坐标" style="height: 420px">
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
                  <div class="table">序号</div>
                  <div class="table">东坐标 /m</div>
                  <div class="table">北坐标 /m</div>
                  <div class="table">天坐标 /m</div>
                </div>

                <div style="display: flex;flex-direction: row;width: 100%;margin-top: 5px"
                     v-for="(item, index) in datasource_transponder" :key="item.id">
                  <div class="table">
                    {{ item.id }}
                  </div>
                  <div class="table">
                    <a-input-number v-model="item.east" defaultValue="0" style="width: 60%"></a-input-number>
                  </div>
                  <div class="table">
                    <a-input-number v-model="item.north" defaultValue="0" style="width: 60%"></a-input-number>
                  </div>
                  <div class="table">
                    <a-input-number v-model="item.sky" defaultValue="0" style="width: 60%"></a-input-number>
                  </div>
                </div>

                <div style="width: 100%;display: flex;flex-direction: row-reverse;margin-top: 10px;">
                  <a-button type="primary" style="margin-right: 5%"
                            @click="add_datasource_transponder">添加行
                  </a-button>
                </div>

              </div>

              <div class="tab-pane fade" id="profile-tab-pane" role="tabpanel" aria-labelledby="profile-tab"
                   tabindex="0">
                <div style="display: flex;align-items: center;width: 100%">
                  <div style="width: 130px; margin: 20px;">应答器坐标数据:</div>

                  <a-select v-model.number="data.svp" placeholder="应答器坐标数据" style="width: 50%">
                    <a-select-option :value="item" v-for="(item, index) in initdatas.tspd" :key="item.id">{{
                        item.name
                      }}
                    </a-select-option>
                  </a-select>
                </div>
              </div>
            </div>
          </div>
        </a-card>
      </div>
      <div style="width: 49%;margin-left: 1%;">
        <a-card :loading="loading" title="系统误差" style="height: 420px">
          <div style="display: flex;flex-direction: column">
            <div style="display: flex;flex-direction: row;margin-top: 20px;align-items: center">
              <div style="width: 100px;flex-shrink: 0">位置误差：</div>

              <div class="little_item">
                <div style="display: flex;align-items: center">
                  <div style="margin: 5px;">东</div>
                  <a-input-number v-model="data.acoposerr[0]" class="little_group"></a-input-number>
                  <div style="margin: 5px;">米</div>
                </div>
                <div style="display: flex;align-items: center">
                  <div style="margin: 5px;">北</div>
                  <a-input-number v-model="data.acoposerr[1]" class="little_group"></a-input-number>
                  <div style="margin: 5px;">米</div>
                </div>
                <div style="display: flex;align-items: center">
                  <div style="margin: 5px;">天</div>
                  <a-input-number v-model="data.acoposerr[2]" class="little_group"></a-input-number>
                  <div style="margin: 5px;">米</div>
                </div>
              </div>

            </div>


            <div style="display: flex;flex-direction: row;margin-top: 20px;align-items: center">
              <div style="width: 100px;flex-shrink: 0">位置误差：</div>

              <div class="little_item">
                <div style="display: flex;align-items: center">
                  <div style="margin: 5px;">硬件时延</div>
                  <a-input-number v-model="data.acodelerr" class="little_group"></a-input-number>
                  <div style="margin: 5px;">s</div>
                </div>
                <div style="display: flex;align-items: center;">
                  <div style="margin: 5px;">随机误差</div>
                  <a-input-number v-model="data.acoranerr" class="little_group"></a-input-number>
                  <div style="margin: 5px;">s</div>
                </div>
              </div>

            </div>

          </div>
        </a-card>
      </div>
    </div>
    <a-card :loading="loading" title="文件选取" style="margin-top: 20px">
      <div style="display: flex;flex-direction: column">
        <div class="row row1">
          <div class="col">声速剖面数据</div>
          <div class="col">
            <a-select @change="change_svpfile" v-model.number="data.svp" placeholder="请先上传声速剖面数据！" style="width: 100%">
              <a-select-option :value="index" v-for="(item,index) in initdatas.svpfile" :key="index">{{
                  item.name
                }}
              </a-select-option>
            </a-select>
          </div>
          <div class="col"></div>
          <div class="col"></div>
        </div>

        <div class="row row1">
          <div class="col"> 声速剖面层深</div>
          <div class="col"><a-input-number class="form-control large_table_path" v-model="data.svpdh"/></div>
          <div class="col"> 采样频率</div>
          <div class="col"><a-input-number class="form-control large_table_path" v-model="data.samper"/></div>
        </div>

        <div class="row row1">
          <div class="col"> 杆臂值</div>
          <div class="col"><a-input-number class="form-control large_table_path" v-model="data.insacolever[0]"/></div>
          <div class="col"><a-input-number class="form-control large_table_path" v-model="data.insacolever[1]"/></div>
          <div class="col"><a-input-number class="form-control large_table_path" v-model="data.insacolever[2]"/></div>
        </div>
      </div>
    </a-card>

    <div style="height: 100px;width: 100%;display: flex;align-items: center;justify-content: center">
      <a-popconfirm title="确定执行吗?" @confirm="Excute">
        <a-button style="width: 100px;height: 40px" type="primary" >执行算法</a-button>
      </a-popconfirm>
    </div>
  </div>
</template>

<script>

import {simulation_api, simulationInitdata, SimulationExcute} from '@/api/simulation'

export default {
  data() {
    return {
      loading:false,
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
    }
  },
  methods: {
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
      }

      // // var temp = {
      // //   datatype:2,
      // //   tratype:"classic_none",
      // //   svpfile:svpfile,
      // //   oepflag:moni
      // // };
      // this.loading = true;
      // SimulationExcute(temp).then((res)=>{
      //   this.loading = false;
      //   if(res.code==0){
      //     console.log(res.data);
      //     var temp=[res.data[1],res.data[3],res.data[0]]
      //     res.data=temp
      //     this.$store.commit('setSimulationResult',res.data);
      //     this.$router.push("/simulation/simulation_result");
      //   }

      // })
      var _temp = {
        datatype:2,
        tratype:"classic_none",
        svpfile:svpfile,
        oepflag:moni,
        data:this.data,
        temp:temp
      };
      // console.log("aaaaaaaaaa")
      // console.log(_temp)
      let files=["insdataavp.txt","insdataimu.txt","acodata.txt"]
      this.loading = true;
      SimulationExcute(_temp).then((res)=>{
        this.loading = false;
        if(res.code==0){
          let result=[]
          // console.log(res)
          for(let i=0;i<res.data.length;++i){
            console.log(res.data[i].name)
            if(files.indexOf(res.data[i].name)>-1){
              result.push(res.data[i])
            }
          }
          // console.log(res.data)
          // var _temp=[res.data[1],res.data[3],res.data[0]]
          // res.data=_temp
          this.$store.commit('setSimulationResult',result);
          this.$router.push({
            path:"/simulation/simulation_result",
            query:{
              runway:"sonar"
            }
          });
        }
      })
    },
    change_svpfile(){
      var temp = {...this.$store.state.system.SimulationConfig};
      temp.svpfile = this.initdatas.svpfile[this.data.svp].path;

      // console.log(temp)
      this.$store.commit('setSimulationConfig',temp);

      console.log(this.$store.state.system.SimulationConfig);
    },
    init_data() {
      simulationInitdata().then((res) => {
        if (res.code == 0) {
          this.initdatas.tspd = res.data.tspd;
          this.initdatas.svpfile = res.data.input;
          this.$message.success('执行成功!', 0.5);
        } else {
          this.$message.success(res.message, 0.5);
        }
      })

      this.add_datasource_transponder();
    },

    add_datasource_transponder() {
      console.log(JSON);
      var temp = JSON.parse(JSON.stringify(this.transponder_template));
      temp.id = this.datasource_transponder.length + 1;
      this.datasource_transponder.push(temp);
    },


  },
  mounted() {
    this.init_data();
  }
}
</script>

<style scoped>
.table_path {
  width: 25%;
  height: 45px;
  align-items: center;
  display: flex;
  justify-content: center;
}

.row1 {
  margin-top: 10px;
}

.input1 {
  width: 250px;
}

.label1 {
  position: relative;
  right: 20px;
}

.little_group {
  width: 250px;
  display: inline;
}


.table {
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

.little_item {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  flex-wrap: wrap;
  line-height: 50px;
  width: 100%;
}
</style>
  