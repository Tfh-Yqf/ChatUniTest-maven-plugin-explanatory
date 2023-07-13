<template>
  <div class="params-config-wrapper">
    <div class="position-config-item">
      <div class="title-bar">
        <div class="title-name">选择声速剖面数据和观测数据集</div>
      </div>
      <div class="select-file-item">
        <span class="label">
          参数配置文件
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="data.Nowconfig" placeholder="选择参数配置文件" @change="changeConfig">
            <option :value="index" v-for="(item, index) in configList" :key="index">{{ item.name}}</option>
          </select>
        </div>
      </div>
      <div class="transfer-title">
        <div class="title-text">可解算点号</div>
        <div class="title-text">待解算点号</div>
      </div>
      <a-transfer :dataSource="mockData" :targetKeys="targetKeys" @change="handleChange" 
      :render="item => item.title" class="lk-transfer">
      </a-transfer>

      <a-Collapse style="width: 100%;margin-top: 20px;" class="lk-collapse">
              <a-collapse-panel v-for="(item, index) in  data.tspd " :key="index" :header="'点' + (index + 1) + '的时间范围'">

                <div style="margin-top: 10px;display: flex;flex-direction: column">
                  <div style="display: flex;flex-direction: row;width: 100%;">
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
                        @change="DateChange(index, Idx)" v-if="str.endsWith('date')" class="lk-date-picker" />
                      <a-time-picker v-model="it[str]" placeholder="时间" valueFormat="HH,mm,ss" format="HH,mm,ss"
                        style="width: 95%;" @change="DateChange(index, Idx)" v-else class="lk-time-picker" />
                    </div>
                  </div>

                  <div style="width: 100%;display: flex;flex-direction: row-reverse;margin-top: 10px;">
                    <div style="margin-right: 5%" @click="AddLine_Tspd(index)" class="add-line">添加行</div>
                  </div>

                </div>


              </a-collapse-panel>
            </a-Collapse>
    </div>

    <div class="position-config-item">
      <div class="title-bar">
        <div class="title-name">文件选择</div>
      </div>
      <div class="select-file-item" v-if="wangpingcha">
        <span class="label">
          走航式标定结果
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="data.wangpingchaFile.posAnsFile" placeholder="选择走航式标定结果">
            <option :value="index" v-for="( item, index )  in wangpingchaFile.posAnsFile" :key="index">{{ item.name}}</option>
          </select>
        </div>
      </div>
      <div class="select-file-item" v-if="wangpingcha">
        <span class="label">
          走航式观测数据
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="data.wangpingchaFile.acousticPositioningPath" placeholder="选择走航式观测数据">
            <option :value="index" v-for="( item, index )  in wangpingchaFile.acousticPositioningPath" :key="index">{{ item.name}}</option>
          </select>
        </div>
      </div>
      <div class="select-file-item" v-if="wangpingcha">
        <span class="label">
          应答器互测距数据
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="data.wangpingchaFile.posObsFile" placeholder="选择应答器互测距数据">
            <option :value="index" v-for="( item, index )  in wangpingchaFile.posObsFile" :key="index">{{ item.name}}</option>
          </select>
        </div>
      </div>
      <div class="select-file-item" v-if="wangpingcha">
        <span class="label">
          应答器深度数据
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="data.wangpingchaFile.tspdDepObs" placeholder="选择应答器深度数据">
            <option :value="index" v-for="( item, index )  in wangpingchaFile.tspdDepObs" :key="index">{{ item.name}}</option>
          </select>
        </div>
      </div>
      <div class="select-file-item" v-if="wangpingcha">
        <span class="label">
          声速剖面数据
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="data.wangpingchaFile.svpdata" placeholder="选择声速剖面文件">
            <option :value="index" v-for="( item, index )  in wangpingchaFile.svpdata" :key="index">{{ item.name}}</option>
          </select>
        </div>
      </div>

      <div class="select-file-item" v-if="!wangpingcha">
        <span class="label">
          走航式观测数据
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="data.acousticPositioningPath" placeholder="选择走航式观测数据文件">
            <option :value="index" v-for="( item, index )  in acousticPositioningPathList" :key="index">{{ item.name}}</option>
          </select>
        </div>
      </div>
      <div class="select-file-item" v-if="!wangpingcha">
        <span class="label">
          声速剖面文件
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="data.svpPath" placeholder="选择声速剖面文件">
            <option :value="index" v-for="( item, index )  in svpList" :key="index">{{ item.name}}</option>
          </select>
        </div>
      </div>
    </div>

    <div class="position-config-item">
      <div class="title-bar">
        <div class="title-name">函数模型弹性构建</div>
      </div>
      <div class="table-content">
          <div class="table-row" style="display: block;">
              <div class="table-item100">
                <div class="item-checkbox" v-for="( item, index ) in model_function" :key="item.value" @change="changeFunctionModule(index)"> 
                          <input type="checkbox" :disabled="item.disabled" :checked="item.checked"> {{ item.label }}
                </div>
              </div>
              <div class="table-item100">
                <div class="group" v-if="model_function[3].checked">
                  <div>时变声速模型时间间隔参数</div>
                  <a-input-number style="margin-left: 20px" v-model="data.time_interval"></a-input-number>
                  <div style="margin-left: 15px;">分钟</div>
                </div>
              </div>
          </div>
      </div>
    </div>

    <div class="position-config-item">
      <div class="title-bar">
        <div class="title-name">距离观测值构建</div>
      </div>
      <div class="table-content">
          <div class="table-row">
              <div class="table-item100">
                <a-radio class="item-radio" v-for="( item, index ) in  JuLiGuanCeZhi_List " :key="index"
                  :checked="index == data.NowJuLiGuanCeZhi" @change="changeDistanceStructure(index)">{{ item.name }}
                </a-radio>
            </div>
          </div>
      </div>
    </div>

    <div class="position-config-item">
      <div class="title-bar">
        <div class="title-name">随机模型弹性构建</div>
      </div>
      <div class="table-content">
          <div class="table-row">
              <div class="table-item100">
                <a-radio  class="item-radio" :checked="data.random_structure == index" @change="changeRandomStructure(index)"
                  v-for="( item, index ) in  RandomStructureList " :key="index">{{ item.name }}
                </a-radio>
              </div>
          </div>
      </div>
    </div>

    <div class="position-config-item">
      <div class="title-bar">
        <div class="title-name">坐标原点</div>
      </div>
      <div class="table-content">
          <div class="table-row">
              <div class="table-item100">
                <a-radio class="item-radio" :key="index" :checked="data.NowZuoBiaoYuanDian == index" @change="changeZuoBiaoYuanDian(index)"
                  v-for="( item, index ) in  ZuoBiaoYuanDian_List ">{{ item.name
                  }}
                </a-radio>
              </div>
          </div>
      </div>
      <div class="table-content" v-if="data.NowZuoBiaoYuanDian == 1">
          <div class="tab-pane fade show active" role="tabpanel" aria-labelledby="home-tab"
              tabindex="0">
              <div class="table-title">
                  <div class="table-path">名称</div>
                  <div class="table-path">X</div>
                  <div class="table-path">Y</div>
                  <div class="table-path">Z</div>
              </div>

              <div class="table-row">
                  <div class="table-item20"></div>
                  <div class="table-item20">
                      <input v-model="data.XYZ0[0]" defaultValue="0" class="table-item-input95">
                  </div>
                  <div class="table-item20">
                      <input v-model="data.XYZ0[1]" defaultValue="0" class="table-item-input95">
                  </div>
                  <div class="table-item20">
                      <input v-model="data.XYZ0[2]" defaultValue="0" class="table-item-input95">
                  </div>
              </div>
          </div>
      </div>
    </div>

    <div class="position-config-item" v-if="wangpingcha">
      <div class="title-bar">
        <div class="title-name">网平差构建</div>
      </div>
      <div class="table-content">
          <div class="table-row">
              <div class="table-item100">
                <a-radio class="item-radio" :checked="data.net_structure" @change="changeNetStructure">三维自由网平差</a-radio>
                <a-radio class="item-radio" :checked="!data.net_structure" @change="changeNetStructure">深度约束网平差</a-radio>
              </div>
          </div>
      </div>
    </div>

    <div class="position-config-item">
      <div class="excute-btn" @click="excute">执行</div>
    </div>
  </div>

</template>

<script>
import { WangpingchaInit, ExcuteWangpingcha, GetConfig, excuteAPI, init} from '@/api/position.js'

export default {
  name: 'ParamsConfig',
  data() {
    return this.$store.state.position;
  },
  methods: {
    init() {
      this.initData();
      this.queryFileList();
      this.formatTime();
    },
    initData() {
      init().then((res) => {
        if (res.code == 0) {
          this.acousticPositioningPathList = res.data.acousticPositioningPath;
          this.svpList = res.data.svpdata;
          this.configList = res.data.cfg;
          this.data.acousticPositioningPath = 0;
          this.data.svpPath = 0;
        } else {
          this.$message.success(res.message, 0.5);
        }

      });
    },
    queryFileList() {
      WangpingchaInit().then((res) => {
        if (res.code == 0) {
          this.configList = res.data.cfg;
        }
      });
    },
    handleChange(targetKeys) {
      this.targetKeys = targetKeys;
    },
    changeConfig() {
      var that = this;
      GetConfig(this.configList[this.data.Nowconfig]).then((res) => {
        if (res.code == 0) {
          if ('posobsfile_path' in res.data) {
            that.data.tspd = res.data.tspd;
            // 网平插
            this.wangpingcha = false;
            this.changeWangpingcha().then((re) => {
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
            this.formatTime();

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
    changeWangpingcha() {

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
    getCommonIndex(str, n) {
      var index = str.indexOf(',');
      while (--n && index != -1) {
        index = str.indexOf(',', index + 1);
      }
      return index;
    },
    getTime(e) {
      return e.substring(this.getCommonIndex(e, 3) + 1, e.length);
    },
    getDate(e) {
      return e.substring(0, this.getCommonIndex(e, 3));
    },
    formatTime() {
      for (var i = 0; i < this.data.tspd.length; i++) {
        for (var j = 0; j < this.data.tspd[i].timeRange.length; j++) {
          this.data.tspd[i].timeRange[j].start_time = this.getTime(this.data.tspd[i].timeRange[j].startTime);
          this.data.tspd[i].timeRange[j].start_date = this.getDate(this.data.tspd[i].timeRange[j].startTime);
          this.data.tspd[i].timeRange[j].end_time = this.getTime(this.data.tspd[i].timeRange[j].endTime);
          this.data.tspd[i].timeRange[j].end_date = this.getDate(this.data.tspd[i].timeRange[j].endTime);
        }
      }
      this.mockData = [];
      for (let i = 0; i < this.data.tspd.length; i++) {
        var t = i + 1;
        this.mockData.push({
          key: i.toString(), title: '点' + t, description: this.data.tspd[i], chosen: false,
        })
      }
    },
    changeZuoBiaoYuanDian(e) {
      this.data.NowZuoBiaoYuanDian = e;
    },
    changeFunctionModule(idx) {
      this.model_function[idx].checked = !this.model_function[e].checked;
    },
    changeNetStructure() {
      this.data.net_structure = !this.data.net_structure;

    },
    changeRandomStructure(e) {
      this.data.random_structure = e;
    },
    changeDistanceStructure(e) {
      this.data.NowJuLiGuanCeZhi = e;
    },
    ExcutePosition() {
      if (this.getLoadingStatus()) {
        this.$message.error('任务执行中，请稍后');
        return;
      }
      this.setLoadingStatus(true);
      this.$message.success('开始执行任务');
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
      this.$store.state.position.showResult = false;
      excuteAPI(temp).then((res) => {
        if (res.code == 0) {
          this.$store.commit('setDemarcate', res.data);
          this.$message.success('执行成功！');
          this.$store.state.position.showResult = true;
        } else {
          this.$message.error(res.msg || res.message || "服务器错误");
        }
        this.setLoadingStatus(false);
      });
    },
    ExcuteWangPingcha() {
      if (this.getLoadingStatus()) {
        this.$message.error('任务执行中，请稍后');
        return;
      }
      this.setLoadingStatus(true);
      this.$message.success('开始执行任务');
      var temp = {
        posansfile_path: this.wangpingchaFile.posAnsFile[this.data.wangpingchaFile.posAnsFile].path,
        posobsfile_path: this.wangpingchaFile.acousticPositioningPath[this.data.wangpingchaFile.acousticPositioningPath].path,
        tspdttobsfile_path: this.wangpingchaFile.posObsFile[this.data.wangpingchaFile.posObsFile].path,
        tspddepobsfile_path: this.wangpingchaFile.tspdDepObs[this.data.wangpingchaFile.tspdDepObs].path,
        svpfile_path: this.wangpingchaFile.svpdata[this.data.wangpingchaFile.svpdata].path,
        netmod: this.data.net_structure ? 1 : 2,
        robmod: this.RandomStructureList[this.data.random_structure].value
      };
      this.$store.state.position.showResult = false;
      ExcuteWangpingcha(temp).then((res) => {
        if (res.code == 0) {
          this.$store.commit('setDemarcate', res.data);
          this.$message.success('任务执行成功');
          this.$store.state.position.showResult = true;
        } else {
          this.$message.error(res.msg || res.message || "服务器错误");
        }
        this.setLoadingStatus(false);
      });

    },
    excute() {
      if (!this.wangpingcha) {
        this.ExcutePosition();
      } else {
        this.ExcuteWangPingcha();
      }
    },
    getLoadingStatus() {
        return this.$store.state.sound.loading;
    },
    setLoadingStatus(stat) {
        this.$store.state.sound.loading = stat;
    }
  },
  mounted() {
    this.init();
  }
};
</script>
<style lang="less" scoped>
.params-config-wrapper {
  padding-left: 8px;
}
.position-config-item {
  padding: auto;
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
}
.table-containor {
        width: 95%;
        margin-left: 5px;
        margin-top: 5px;
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
        height: 25px;
    }

    .table-row {
        display: flex;
        flex-direction: row;
        width: 100%;
        height: 20px;
        margin-bottom: 5px;
        color: #99B3C8;
    }

    .table-item {
        text-align: center;
        width: 20%;
        height: 20px;
    }
    .table-item100 {
        text-align: center;
        width: 100%;
        height: 20px;
        display: flex;
        flex-direction: row;
    }
    .item-checkbox {
      margin: 0 5px;
    }
    .item-radio {
      color: #99B3C8;
    }
    .table-item17 {
        text-align: center;
        width: 17%;
        ;
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

    .table-item20 {
        text-align: center;
        width: 20%;
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

    .table-path {
        width: 20%;
        height: 25px;
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


.select-file-item {
  display: flex;
  align-items: center;
  margin: 4px 0;

  .label {
    width: 25%;
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

    :deep(.ant-select-selection__placeholder) {
      font-size: 12px;
      color: #99B3C8;
    }

    :deep(.ant-select-selection__rendered) {
      line-height: 20px;
    }

    :deep(.ant-select-selection-selected-value) {
      color: #99B3C8;
      font-size: 12px;
    }
  }

  .latitude,
  .longitude {
    width: 97px;
    font-size: 12px;
    color: #99B3C8;
  }

  .unit {
    color: #99B3C8;
    font-size: 12px;
  }
}
.transfer-title{
  display:flex;
  flex-direction: row;
  column-gap: 30px;
  font-weight: bold;
  margin-top: 20px;
  .title-text{
    width: 50%;
    text-align: center;
    font-size: 14px;
    font-family: Source Han Sans CN-Regular, Source Han Sans CN;
    font-weight: 400;
    color: #99B3C8;
  }
}
:deep(.ant-transfer-list-body){
  background-color: #fff;
}
.excute-btn {
  margin: 0 auto;
  font-size: 12px;
  font-family: Source Han Sans CN-Regular, Source Han Sans CN;
  font-weight: 400;
  color: #F5FBFF;
  width: 80px;
  height: 26px;
  line-height: 26px;
  color: #F5FBFF;
  text-align: center;
  background: linear-gradient(180deg, rgba(12, 53, 111, 0) 0%, rgba(25, 236, 255, 0.63) 100%);
  opacity: 1;
  border: 1px solid #19ECFF;
  margin-top: 8px;
  cursor: pointer;
}
:deep(.lk-transfer){
  .ant-transfer-list{
    color: #99B3C8;
    border-color: rgba(47, 136, 181, 0.16);
    .ant-transfer-list-header,.ant-transfer-list-body{
      border-color: rgba(47, 136, 181, 0.16);
      color: #99B3C8;
      background-color: rgba(47, 136, 181, 0.16);
    }
  }
  .ant-transfer-operation{
    .ant-btn{
      color: #99B3C8;
      border-color: rgba(47, 136, 181, 0.16);
      background-color: rgba(47, 136, 181, 0.16);
    }
  }
  .ant-empty-normal{
    display: none;
  }
}
:deep(.lk-collapse){
  background-color: rgba(47, 136, 181, 0.16);
  border-color: rgba(47, 136, 181, 0.16);
  .ant-collapse-item{
    color: #99B3C8;
    border-color: rgba(47, 136, 181, 0.16);
    .ant-collapse-content,.ant-collapse-header{
      color: #99B3C8;
      border-color: rgba(47, 136, 181, 0.16);
      background-color: transparent;
    }
  }
}
.table-item-select{
  width: 100%;
  background-color: transparent;
}
</style>
