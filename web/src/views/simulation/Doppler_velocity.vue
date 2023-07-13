<template>
    <div>
      <a-card :loading="loading" title="多普勒测速仪器误差参数设置">
        <div class="card-body fw-bold text-center">
          <div class="row row1">
            <div class="col"></div>
            <div class="col label1">E</div>
            <div class="col label1">N</div>
            <div class="col label1">U</div>
          </div>
          <div class="row row1">
            <div class="col">多普勒测速仪比例因子误差:</div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.dvlsf[0]"/>
            </div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.dvlsf[1]"/>
            </div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.dvlsf[2]"/>
            </div>
          </div>
          <div class="row row1">
            <div class="col">普勒测速仪速度随机误差:</div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.dvlranerr[0]"/>
            </div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.dvlranerr[1]"/>
            </div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.dvlranerr[2]"/>
            </div>
          </div>
          <div class="row row1">
            <div class="col">多普勒测速仪速角度安装误差:</div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.dvlangerr[0]"/>
            </div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.dvlangerr[1]"/>
            </div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.dvlangerr[2]"/>
            </div>
          </div>
          <div class="row row1">
            <div class="col">多普勒测速仪速时间延迟误差:</div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.dvltimdel"/>
            </div>
            <div class="col">
            </div>
            <div class="col">
            </div>
          </div>
          <div class="row row1">
            <div class="col">多普勒测速仪位置转换参数:</div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.Insdvllever[0]"/>
            </div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.Insdvllever[1]"/>
            </div>
            <div class="col">
              <a-input-number class="form-control large_table_path" v-model="data.Insdvllever[2]"/>
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
                dvlsf: [2, 2, 2],//多普勒测速仪比例因子误差
                dvlranerr: [4, 4, 4],//多普勒测速仪速度随机误差
                dvlangerr: [0, 0, 0],//多普勒测速仪速角度安装误差
                dvltimdel: 0,//多普勒测速仪速时间延迟误差
                Insdvllever: [1, 1, 1],//多普勒测速仪位置转换参数
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
        // //   datatype:4,
        // //   tratype:"classic_none",
        // //   svpfile:svpfile,
        // //   oepflag:moni
        // // };
        // this.loading = true;
        // SimulationExcute(temp).then((res)=>{
        //   this.loading = false;
        //   if(res.code==0){
        //     var temp=[res.data[1],res.data[3],res.data[2]]
        //     console.log(temp)
        //     res.data=temp
        //     this.$store.commit('setSimulationResult',res.data);
        //     this.$router.push("/simulation/simulation_result");
        //   }

        // })
        var _temp = {
          datatype:4,
          tratype:"classic_none",
          svpfile:svpfile,
          oepflag:moni,
          data:this.data,
          temp:temp
        };
        // console.log(_temp)
        let files=["insdataavp.txt","insdataimu.txt","dvldata.txt"]
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
            // var _temp=[res.data[1],res.data[3],res.data[2]]
            // res.data=_temp
            this.$store.commit('setSimulationResult',result);
            this.$router.push({
              path:"/simulation/simulation_result",
              query:{
                runway:"Doppler_velocity"
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
  