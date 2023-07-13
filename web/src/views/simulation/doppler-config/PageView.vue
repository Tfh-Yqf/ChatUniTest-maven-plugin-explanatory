<template>
  <div class="wrapper">
    <div class="doppler-config-item">
      <div class="title-bar">
        <div class="title-name">多普勒测速仪器误差参数设置</div>
      </div>
      <DiffConfig ref="diffConfigRef" />
    </div>
    <div class="excute-btn" @click="excute">执行</div>
  </div>
</template>


<script>
import {SimulationExcute} from '@/api/simulation';


import DiffConfig from './components/DiffConfig.vue';
export default {
  name: 'DopplerConfig',
  components: {
    DiffConfig
  },
  data() {
    return {
      
    };
  },
  methods: {
    excute(){
      if (this.getLoadingStatus()) {
        this.$message.error('任务执行中，请稍后');
        return;
      }
      this.setLoadingStatus(true);
      this.$message.success('开始执行任务');
      this.resultInit();
      var temp = {...this.$store.state.system.SimulationConfig};
      var params = {
        datatype:4,
        tratype: "classic_none",
        svpfile: "",
        oepflag: 0,
        data: this.$store.state.simulation.doppler,
        temp: temp
      };
      let files=["insdataavp.txt","insdataimu.txt","dvldata.txt"];
      let that = this;
      SimulationExcute(params).then((res)=>{
        if(res.code==0){
          that.dealResult(res.data, files);
          that.$message.success('任务执行成功');
        } else {
          that.$message.error(res.msg || res.message || "服务器错误");
        }
        that.setLoadingStatus(false);
      });
    },
    resultInit() {
      this.$store.state.simulation.resultShow = false;
      this.$store.state.simulation.resultType = 0;
      this.$store.state.simulation.resultFiles = [];
    },
    dealResult(data, files) {
      this.$store.state.simulation.resultType = 3;
      this.$store.state.simulation.resultShow = true;
      for (var i = 0; i < files.length; i++) {
          var filename = files[i];
          for (var j = 0; j < data.length; j++) {
              var fileinfo = data[j];
              if (fileinfo['name'] == filename) {
                  this.$store.state.simulation.resultFiles.push(fileinfo);
              }
          } 
        }
      },
      getLoadingStatus() {
          return this.$store.state.simulation.loading;
      },
      setLoadingStatus(stat) {
          this.$store.state.simulation.loading = stat;
      }
    },
    mounted() {
    },
};
</script>

<style scoped>
.wrapper {
  padding-left: 8px;
}
.excute-btn{
  width: 80px;
  height: 26px;
  line-height: 26px;
  color: #F5FBFF;
  text-align: center;
  background: linear-gradient(180deg, rgba(12, 53, 111, 0) 0%, rgba(25, 236, 255, 0.63) 100%);
  opacity: 1;
  border: 1px solid #19ECFF;
  margin-top: 8px;
}
.title-bar {
  padding: 0 0 0 8px;
  width: 100%;
  height: 24px;
  align-items: center;
  background: linear-gradient(90deg, rgba(8, 83, 106, 0.79) 0%, rgba(13, 78, 99, 0.03) 100%);

  .title-name {
    font-size: 14px;
    line-height: 24px;
    height: 24px;
    color: #99B3C8;
    font-weight: 500;
    margin-right: 10px;
  }

  .sub-title-radio-group-option {
    color: #fff;
  }
}
</style>