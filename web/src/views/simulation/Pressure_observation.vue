<template>
    <div>
      <a-card :loading="loading" title="压力计仪器误差参数设置">
        <div class="card-body fw-bold text-center">
          <div class="row row1">
            <div class="col">压力计比例因子误差:</div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.pgsf"/>
            </div>
            <div class="col">
            </div>
            <div class="col">
            </div>
          </div>
          <div class="row row1">
            <div class="col">压力计随机误差:</div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.pgranerr"/>
            </div>
            <div class="col">
            </div>
            <div class="col">
            </div>
          </div>
          <div class="row row1">
            <div class="col"></div>
            <div class="col label1">E</div>
            <div class="col label1">N</div>
            <div class="col label1">U</div>
          </div>
          <div class="row row1">
            <div class="col">压力计位置转换参数:</div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.inspglever[0]"/>
            </div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.inspglever[1]"/>
            </div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.inspglever[2]"/>
            </div>
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


import {SimulationExcute} from "@/api/simulation";

export default {
    data() {
        return {
          loading:false,
            data: {
                pgsf: 5,//压力计比例因子误差
                pgranerr: 4,//压力计随机误差
                inspglever: [1, 1, 1],//压力计位置转换参数

            }
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
        // //   datatype:8,
        // //   tratype:"class ic_none",
        // //   svpfile:svpfile,
        // //   oepflag:moni
        // // };
        // this.loading = true;
        // SimulationExcute(temp).then((res)=>{
        //   this.loading = false;
        //   if(res.code==0){
        //     var temp=[res.data[1],res.data[3],res.data[4]]
        //     res.data=temp
        //     this.$store.commit('setSimulationResult',res.data);
        //     this.$router.push("/simulation/simulation_result");
        //   }

        // })
        var _temp = {
          datatype:8,
          tratype:"classic_none",
          svpfile:svpfile,
          oepflag:moni,
          data:this.data,
          temp:temp
        };
        // console.log(_temp)
        let files=["insdataavp.txt","insdataimu.txt","pgdata.txt"]
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
            // var _temp=[res.data[1],res.data[3],res.data[4]]
            // res.data=_temp
            this.$store.commit('setSimulationResult',result);
            this.$router.push({
              path:"/simulation/simulation_result",
              query:{
                runway:"Pressure_observation"
              }
            });
          }

        })
      },
    },
    mounted() {

    }
}
</script>
  
<style scoped>
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

.large_table_path{
  width: 75%;
  height: 45px;
  align-items: center;
  display: flex;
  justify-content: center;
}
</style>
  