<template>
  <div class="wrapper">
    <div class="sonar-data-item">
      <div class="title-bar">
        <div class="title-name">应答器坐标</div>
        <a-radio-group v-model="inputWay" name="radioGroup" class="sub-title-radio-group">
          <a-radio value="1" class="sub-title-radio-group-option">表格填写</a-radio>
          <a-radio value="2" class="sub-title-radio-group-option">文件选取</a-radio>
        </a-radio-group>
      </div>
      <div class="table-content" v-if="inputWay == 1">
            <div class="tab-pane fade show active" role="tabpanel" aria-labelledby="home-tab"
                tabindex="0">
                <div class="table-title">
                    <div class="table_path">序号</div>
                    <div class="table_path">东坐标 /m</div>
                    <div class="table_path">北坐标 /m</div>
                    <div class="table_path">天坐标 /m</div>
                    <div class="table_path"></div>
                </div>

                <div class="table-row"  v-for="(item, index) in datasource_transponder" :key="item.id">
                    <div class="table-item">{{ item.id }}</div>
                    <div class="table-item">
                        <input v-model="item.east" defaultValue="0" class="table-item-input">
                    </div>
                    <div class="table-item">
                        <input v-model="item.north" defaultValue="0" class="table-item-input">
                    </div>
                    <div class="table-item">
                        <input  v-model="item.sky" defaultValue="0" class="table-item-input">
                    </div>
                </div>

                <div class="tail-row">
                    <div @click="addDatasourceTransponder()" class="confirm-botton">添加行</div>
                </div>
            </div>
        </div>
        <div class="row-containor" v-if="inputWay == 2">
            <div class="left-title">
                航迹列表数据:
            </div>
            <div class="rigth-content">
                <select class="table-item-select" v-model.number="data.svp">
                    <option :value="item" v-for="(item, index) in initdatas.tspd" :key="item.id">{{item.name}}</option>
                </select>
            </div>
        </div>
    </div>
    <div class="sonar-data-item">
      <div class="title-bar">
        <div class="title-name">系统误差</div>
      </div>
      <div class="system-error">
        <div class="error-item">
          <div class="label">东</div>
          <div class="table-item">
            <input v-model="data.acoposerr[0]" class="item-input">
          </div>
        </div>
        <div class="error-item">
          <div class="label">北</div>
          <div class="table-item">
            <input v-model="data.acoposerr[1]" class="item-input">
          </div>
        </div>
        <div class="error-item">
          <div class="label">天</div>
          <div class="table-item">
            <input v-model="data.acoposerr[2]" class="item-input">
          </div>
        </div>
        <div class="error-item">
          <div class="label">硬件延迟</div>
          <div class="table-item">
            <input v-model="data.acodelerr" class="item-input">
          </div>
        </div>
        <div class="error-item">
          <div class="label">随机误差</div>
          <div class="table-item">
            <input v-model="data.acoranerr" class="item-input">
          </div>
        </div>
      </div>
    </div>


    <div class="sonar-data-item">
      <div class="title-bar">
        <div class="title-name">文件选取</div>
      </div>
      <div class="select-file">
      <div class="select-file-item">
        <span class="label">
          声速剖面数据
        </span>
        <div class="item-input">
          <select class="table-item-select"  placeholder="上传声速剖面数据" @change="changeSvpfile" v-model.number="data.svp" >
            <option :value="index" v-for="(item, index) in initdatas.svpfile" :key="index">{{item.name}}
            </option>        
          </select>
        </div>
      </div>
      <div class="select-file-item">
        <div class="label">声速剖面层深</div>
        <div class="table-item">
          <input v-model="data.svpdh" class="item-input">
        </div>
      </div>
      <div class="select-file-item">
        <div class="label">采样频率</div>
        <div class="table-item">
          <input v-model="data.samper" class="item-input">
        </div>
      </div>
      <div class="select-file-item" style="text-align: left;">
        <div class="label">杆臂值</div>
        <div class="table-item">
          <input v-model="data.insacolever[0]" class="item-input insacolever">
        </div>
        <div class="table-item">
          <input v-model="data.insacolever[1]" class="item-input insacolever">
        </div>
        <div class="table-item">
          <input v-model="data.insacolever[2]" class="item-input insacolever">
        </div>
      </div>
    </div>
    </div>
    <div class="excute-btn" @click="excute">执行</div>
  </div>
</template>


<script>
// import TableComponents from './components/TableComponents.vue';
// import SystemError from './components/SystemError.vue';
// import SelectFile from './components/SelectFile.vue';
import {simulationInitdata, SimulationExcute} from '@/api/simulation'
export default {
  name: 'SonarData',
  // components: {
  //   // TableComponents,
  //   // SystemError,
  //   // SelectFile
  // },

  data() {
    return this.$store.state.simulation.sonar;
  },

  methods: {
    init() {
      simulationInitdata().then((res) => {
        if (res.code == 0) {
          this.initdatas.tspd = res.data.tspd;
          this.initdatas.svpfile = res.data.input;
          this.$message.success('执行成功!', 0.5);
        } else {
          this.$message.success(res.message, 0.5);
        }
      })
      if (this.datasource_transponder.length == 0) {
        this.addDatasourceTransponder();
      }
    },
    addDatasourceTransponder() {
      var temp = JSON.parse(JSON.stringify(this.transponder_template));
      temp.id = this.datasource_transponder.length + 1;
      this.datasource_transponder.push(temp);
    },
    changeSvpfile() {
      this.data.svpfile = this.initdatas.svpfile[this.data.svp].path;
    },
    excute(){
      if (this.getLoadingStatus()) {
        this.$message.error('任务执行中，请稍后');
        return;
      }
      if (!this.data.svpfile) {
        this.$message.error('svpfile未选择');
        return;
      }
      this.resultInit();
      this.setLoadingStatus(true);
      this.$message.success('开始执行任务');
      var temp = {...this.$store.state.system.SimulationConfig};
      temp.svpfile = this.data.svpfile;
      var params = {
        datatype: 2,
        tratype: "classic_none",
        svpfile: this.data.svpfile,
        oepflag: 0,
        data: this.data,
        temp: temp
      };
      let files=["insdataavp.txt","insdataimu.txt","acodata.txt"]
      let that = this;
      SimulationExcute(params).then((res)=>{
        if(res.code==0){
          that.dealResult(res.data, files);
          that.$message.success('任务执行成功');
        } else {
          that.$message.error(res.msg || res.message || "服务器错误");
        }
        that.setLoadingStatus(false);
      })
    },
    resultInit() {
        this.$store.state.simulation.resultShow = false;
        this.$store.state.simulation.resultType = 0;
        this.$store.state.simulation.resultFiles = [];
    },
    dealResult(data, files) {
      this.$store.state.simulation.resultType = 2;
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
    this.init();
  },
};
</script>

<style scoped>
.wrapper {
  padding-left: 8px;
}

.table-content {
        font-size: 12px;
        font-family: Source Han Sans CN-Regular, Source Han Sans CN;
        font-weight: 400;
        color: #99B3C8;
        line-height: 20px;
    }

    .table-title {
        display: flex;
        flex-direction: row;
        width: 100%;
        height: 45px;
    }

    .table-row {
        display: flex;
        flex-direction: row;
        width: 100%;
        height: 20px;
        margin-bottom: 5px;
    }

    .table-item {
        text-align: center;
        width: 20%;
        height: 20px;
    }

    .table-item17 {
        text-align: center;
        width: 17%;
        height: 20px;
    }

    .table-item-left {
        text-align: left;
        width: 20%;
        ;
        height: 20px;
    }

    .table-item-left50 {
        text-align: left;
        width: 50%;
        ;
        height: 20px;
    }

    .table-item25 {
        text-align: center;
        width: 25%;
        ;
        height: 20px;
    }

    .table-item50 {
        text-align: center;
        width: 50%;
        ;
        height: 20px;
    }

    .table-item-select {
        height: 20px;
        width: 100%;
        background: rgba(47, 136, 181, 0.16);
    }

    .table-item-input {
        height: 20px;
        width: 50%;
        border-radius: 0px 0px 0px 0px;
        opacity: 1;
        border: none;
        background: rgba(47, 136, 181, 0.16);
    }

    .table-item-input95 {
        height: 20px;
        width: 95%;
        border-radius: 0px 0px 0px 0px;
        opacity: 1;
        border: none;
        background: rgba(47, 136, 181, 0.16);
    }

    .table_path {
        width: 25%;
        height: 45px;
        align-items: center;
        display: flex;
        justify-content: center;
    }

    .tail-row {
        width: 95%;
        margin-left: 5px;
        margin-top: 10px;
        height: 26px;
        display: flex;
        flex-direction: row;
        flex-wrap: nowrap;
        justify-content: center;
    }

    .confirm-botton {
        width: 80px;
        height: 26px;
        line-height: 26px;
        color: #F5FBFF;
        text-align: center;
        background: linear-gradient(180deg, rgba(12, 53, 111, 0) 0%, rgba(25, 236, 255, 0.63) 100%);
        opacity: 1;
        border: 1px solid #19ECFF;
    }

    .row-containor {
        width: 95%;
        height: 40px;
        margin-left: 5px;
        margin-top: 5px;
        display: flex;
        flex-direction: row;
    }

    .left-title {
        width: 30%;
        text-align: center;
        font-size: 14px;
        font-family: Source Han Sans CN-Regular, Source Han Sans CN;
        font-weight: 400;
        color: #99B3C8;
        line-height: 40px;
    }

    .rigth-content {
        width: 70%;
        display: flex;
        flex-direction: column;
        justify-content: center;
    }

.system-error{
  margin-top: 4px;
}
.error-item {
  margin-top: 5px;
  display: flex;
  align-items: center;

  .label {
    font-size: 12px;
    font-family: Source Han Sans CN-Regular, Source Han Sans CN;
    font-weight: 400;
    color: #99B3C8;
    line-height: 20px;
    margin-right: 8px;
    width: 72px;
  }

  .item-input {
    height: 20px;
    width: 170px;
    border-radius: 0px;
    border: none;
    background: rgba(47, 136, 181, 0.16);
    color: #99B3C8;
    font-size: 12px;
  }
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
.sonar-data-item {
  margin-top: 5px;
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
.select-file{
  margin-top: 8px;
}
.select-file-item {
  margin-top: 5px;
  display: flex;
  align-items: center;

  .label {
    width: 72px;
    font-size: 12px;
    font-family: Source Han Sans CN-Regular, Source Han Sans CN;
    font-weight: 400;
    color: #99B3C8;
    line-height: 20px;
    margin-right: 8px;
  }

  .item-input {
    height: 20px;
    width: 170px;
    border-radius: 0px;
    border: none;
    background: rgba(47, 136, 181, 0.16);
    color: #99B3C8;
    font-size: 12px;
    :deep(.ant-select-selection--single) {
      width: 170px;
      height: 20px;
      border-radius: 0px;
      background-color: rgba(47, 136, 181, 0.16);
      border: none;
    }
    :deep(.ant-select-selection__rendered) {
      line-height: 20px;
    }
    :deep(.ant-select-selection-selected-value){
      color: #99B3C8;
      font-size: 12px;
    }
  }
  .insacolever{
    width: 50px;
    margin-right: 10px;
    :deep(.ant-select-selection--single) {
      width: 50px;
    }
  }
}
.file-select {
  display: flex;
  align-items: center;
  .left-title {
    text-align: center;
    font-size: 14px;
    font-family: Source Han Sans CN-Regular, Source Han Sans CN;
    font-weight: 400;
    color: #99B3C8;
    line-height: 40px;
  }

  .rigth-content {
    height: 20px;
    margin-left: 8px;

    :deep(.ant-select-selection--single) {
      width: 170px;
      height: 20px;
      border-radius: 0px;
      background-color: rgba(47, 136, 181, 0.16);
      border: none;
    }
    :deep(.ant-select-selection__rendered) {
      line-height: 20px;
    }
  }
}</style>