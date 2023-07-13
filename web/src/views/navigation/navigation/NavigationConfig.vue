<template>
    <div class="wrapper">
      <div class="sub-content">
          <div class="sub-title">
              <div class="sub-title-name">参数选取</div>
          </div>
          <div class="table-containor">
            <div class="table-content">
                <div class="table-row">
                    <div class="table-item-left">参数配置文件</div>
                    <div class="table-item50">
                        <select class="table-item-select" name="" v-model.number="NowConfig" @change="changeConfig">
                            <option v-for="(item, index) in initData.config" :key="index" :value="index">{{ item.name }}</option>
                        </select>
                    </div>
                </div>
            </div>
          </div>

          <div class="sub-title">
              <div class="sub-title-name">时间设置</div>
          </div>
          <div class="table-containor">
            <div class="table-content">
                <div class="table-row">
                    <div class="table-item-left">开始时间</div>
                    <div class="table-item90">
                      <input v-model="time.start_date" class="table-item-input">
                      <input v-model="time.start_time" class="table-item-input">
                      <input v-model="time.start_ms" class="table-item-input">
                      <div style="margin-left: 5px;">毫秒</div>
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">结束时间</div>
                    <div class="table-item90">
                      <input v-model="time.end_date" class="table-item-input">
                      <input v-model="time.end_time" class="table-item-input">
                      <input v-model="time.end_ms" class="table-item-input">
                      <div style="margin-left: 5px;">毫秒</div>
                    </div>
                </div>
            </div>
          </div>

          <div class="sub-title">
              <div class="sub-title-name">算法设置</div>
          </div>
          <div class="table-containor">
            <div class="table-content">
                <div class="table-row">
                    <div class="table-item-left">算法选取</div>
                    <div class="table-item50">
                        <select class="table-item-select" name="" v-model.number="data.Algorithm" @change="changeAlgorithm">
                            <option v-for="(it,ind) in AlgorithmMenu" :value="ind" :key="ind">{{ it.name }}</option>
                        </select>
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">是否抗差</div>
                    <div class="table-item50">
                        <select class="table-item-select" name="" v-model.number="data.KangCha">
                            <option :value="true">抗差</option>
                            <option :value="false">非抗差</option>
                        </select>
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">自适应滤波</div>
                    <div class="table-item50">
                        <select class="table-item-select" name="" v-model.number="data.shiying">
                            <option :value="true">自适应</option>
                            <option :value="false">非自适应</option>
                        </select>
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">状态参数</div>
                    <div class="table-item90">
                      <div class="item-checkbox" v-for="(item, index) in zhuangtai_canshu" :key="item.value" @change="changeStatus(index)"> 
                          <input type="checkbox" :disabled="item.disabled" :checked="item.checked"> {{ item.label }}
                      </div>
                    </div>
                </div>
            </div>
          </div>

          <div class="sub-title">
              <div class="sub-title-name">文件选取</div>
          </div>
          <div class="table-containor">
            <div class="table-content">
                <div class="table-row">
                    <div class="table-item-left">声速剖面数据</div>
                    <div class="table-item50">
                        <select class="table-item-select" name="" v-model.number="file.svp">
                            <option :value="item.path" v-for="(item, index) in initData.svp" :key="item.path">{{ item.name }}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="table-content">
                <div class="table-row">
                    <div class="table-item-left">声学数据</div>
                    <div class="table-item50">
                        <select class="table-item-select" name="" v-model.number="file.navigation">
                            <option :value="item.path" v-for="(item, index) in initData.navigation" :key="item.path">{{ item.name }}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="table-content" v-if="data.Algorithm != 0">
                <div class="table-row">
                    <div class="table-item-left">声导结果数据</div>
                    <div class="table-item50">
                        <select class="table-item-select" name="" v-model.number="file.acoupos">
                            <option :value="index" v-for="(item, index) in initData.acoupos" :key="index">{{ item.name }}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="table-content" v-if="data.Algorithm != 0">
                <div class="table-row">
                    <div class="table-item-left">惯导数据</div>
                    <div class="table-item50">
                        <select class="table-item-select" name="" v-model.number="file.imu">
                            <option :value="index" v-for="(item, index) in initData.imu" :key="index">{{ item.name }}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="table-content" v-if="data.Algorithm != 0">
                <div class="table-row">
                    <div class="table-item-left">DVL数据选取</div>
                    <div class="table-item50">
                        <select class="table-item-select" name="" v-model.number="file.dvl">
                            <option :value="index" v-for="(item, index) in initData.dvl" :key="index">{{ item.name }}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="table-content" v-if="data.Algorithm != 0">
                <div class="table-row">
                    <div class="table-item-left">海洋内波数据</div>
                    <div class="table-item50">
                        <!-- <select class="table-item-select" name="" v-model.number="file.dvl">
                            <option :value="index" v-for="(item, index) in initData.dvl" :key="index">{{ item.name }}</option>
                        </select> -->
                        <!-- 原来文件没有，留个站位 -->
                        <select class="table-item-select" name="">
                        </select>
                    </div>
                </div>
            </div>
          </div>

          <div class="sub-title">
              <div class="sub-title-name">杆臂参数设置</div>
          </div>
          <div class="table-containor">
            <div class="table-content">
                <div class="table-title">
                    <div class="table-path"></div>
                    <div class="table-path">Right</div>
                    <div class="table-path">Front</div>
                    <div class="table-path">Up</div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">SINS-UANP</div>
                    <div class="table-item90">
                      <input v-model="data.GanBi.SUNS_UANP.right" class="table-item-input">
                      <input v-model="data.GanBi.SUNS_UANP.front" class="table-item-input">
                      <input v-model="data.GanBi.SUNS_UANP.up" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">SINS-DVL</div>
                    <div class="table-item90">
                      <input v-model="data.GanBi.SUNS_DVL.right" class="table-item-input">
                      <input v-model="data.GanBi.SUNS_DVL.front" class="table-item-input">
                      <input v-model="data.GanBi.SUNS_DVL.up" class="table-item-input">
                    </div>
                </div>
            </div>
          </div>

          <div class="sub-title">
              <div class="sub-title-name">{{configNoisy[0].name}}</div>
          </div>
          <div class="table-containor">
            <div class="table-content">
                <div class="table-row">
                    <div class="table-item-left">{{configNoisy[0].children[0].name}}</div>
                    <div class="table-item90">
                      <input v-model="configNoisy[0].children[0].config" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">{{configNoisy[0].children[1].name}}</div>
                    <div class="table-item90">
                      <input v-model="configNoisy[0].children[1].config" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">{{configNoisy[0].children[2].name}}</div>
                    <div class="table-item90">
                      <input v-model="configNoisy[0].children[2].config[0]" class="table-item-input">
                      <input v-model="configNoisy[0].children[2].config[1]" class="table-item-input">
                      <input v-model="configNoisy[0].children[2].config[2]" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">{{configNoisy[0].children[3].name}}</div>
                    <div class="table-item90">
                      <input v-model="configNoisy[0].children[3].config[0]" class="table-item-input">
                      <input v-model="configNoisy[0].children[3].config[1]" class="table-item-input">
                      <input v-model="configNoisy[0].children[3].config[2]" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">{{configNoisy[0].children[4].name}}</div>
                    <div class="table-item90">
                      <input v-model="configNoisy[0].children[4].config[0]" class="table-item-input">
                      <input v-model="configNoisy[0].children[4].config[1]" class="table-item-input">
                      <input v-model="configNoisy[0].children[4].config[2]" class="table-item-input">
                    </div>
                </div>
            </div>
          </div>

          <div class="sub-title">
              <div class="sub-title-name">{{configNoisy[1].name}}</div>
          </div>
          <div class="table-containor">
            <div class="table-content">
                <div class="table-row">
                    <div class="table-item-left">{{configNoisy[1].children[0].name}}</div>
                    <div class="table-item90">
                      <input v-model="configNoisy[1].children[0].config[0]" class="table-item-input">
                      <input v-model="configNoisy[1].children[0].config[1]" class="table-item-input">
                      <input v-model="configNoisy[1].children[0].config[2]" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">{{configNoisy[1].children[1].name}}</div>
                    <div class="table-item90">
                      <input v-model="configNoisy[1].children[1].config[0]" class="table-item-input">
                      <input v-model="configNoisy[1].children[1].config[1]" class="table-item-input">
                      <input v-model="configNoisy[1].children[1].config[2]" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">{{configNoisy[1].children[2].name}}</div>
                    <div class="table-item90">
                      <input v-model="configNoisy[1].children[2].config[0]" class="table-item-input">
                      <input v-model="configNoisy[1].children[2].config[1]" class="table-item-input">
                      <input v-model="configNoisy[1].children[2].config[2]" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">{{configNoisy[1].children[3].name}}</div>
                    <div class="table-item90">
                      <input v-model="configNoisy[1].children[3].config[0]" class="table-item-input">
                      <input v-model="configNoisy[1].children[3].config[1]" class="table-item-input">
                      <input v-model="configNoisy[1].children[3].config[2]" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">{{configNoisy[1].children[4].name}}</div>
                    <div class="table-item90">
                      <input v-model="configNoisy[1].children[4].config[0]" class="table-item-input">
                      <input v-model="configNoisy[1].children[4].config[1]" class="table-item-input">
                      <input v-model="configNoisy[1].children[4].config[2]" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">{{configNoisy[1].children[5].name}}</div>
                    <div class="table-item90">
                      <input v-model="configNoisy[1].children[5].config[0]" class="table-item-input">
                      <input v-model="configNoisy[1].children[5].config[1]" class="table-item-input">
                      <input v-model="configNoisy[1].children[5].config[2]" class="table-item-input">
                    </div>
                </div>
            </div>
          </div>

          <div class="sub-title">
              <div class="sub-title-name">Kalman滤波方差设置</div>
          </div>
          <div class="table-containor">
            <div class="table-content">
                <div class="table-row">
                    <div class="table-item-left">{{NavgiationConfig[0].children[0].name}}</div>
                    <div class="table-item90">
                      <input v-model="NavgiationConfig[0].children[0].config" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">{{NavgiationConfig[0].children[1].name}}</div>
                    <div class="table-item90">
                      <input v-model="NavgiationConfig[0].children[1].config" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">{{NavgiationConfig[0].children[2].name}}</div>
                    <div class="table-item90">
                      <input v-model="NavgiationConfig[0].children[2].config" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">{{NavgiationConfig[0].children[3].name}}</div>
                    <div class="table-item90">
                      <input v-model="NavgiationConfig[0].children[3].config" class="table-item-input">
                    </div>
                </div>
            </div>
          </div>


      </div>
      <div @click="excute" class="excute-btn">执行</div>
    </div>
  </template>
  
  
<script>

import { Navgiation_initdata, NavigationGetConfig ,NavgiationDanyi, NavigationExcuteOther} from '@/api/Navgiation'


  export default {
    name: 'NavigationConfig',
    data() {
      return this.$store.state.navigation.navigation;
    },
    methods: {
      init() {
        var that = this;
        Navgiation_initdata().then((res) => {
          if (res.code == 0) {
            that.initData = res.data;
            that.$message.success(res.message, 0.5);
            that.changeConfig();
          }
        });
      },
      changeConfig() {
        NavigationGetConfig(this.initData.config[this.NowConfig]).then((res) => {
          this.$message.success("加载配置成功");
          if (res.code == 0) {
            this.config = res.data;
            if("dvl" in res.data){
              this.data.Algorithm = '153p';
              this.changeAlgorithm('153p');
              this.time.start_date = res.data.timestaend[0]+"-"+res.data.timestaend[1]+"-"+res.data.timestaend[2];
              this.time.start_time = res.data.timestaend[3]+":"+res.data.timestaend[4]+":"+res.data.timestaend[5];
              this.time.end_date  = res.data.timestaend[6]+"-"+res.data.timestaend[7]+"-"+res.data.timestaend[8];
              this.time.end_time  = res.data.timestaend[9]+":"+res.data.timestaend[10]+":"+res.data.timestaend[11];
              for(var i=0;i<this.AlgorithmMenu.length;i++){
                if(this.AlgorithmMenu[i].value ==res.data.function){
                  this.data.Algorithm = i;
                }
              }
              this.data.shiying = res.data.rkflag==1;
              this.data.KangCha = res.data.robmodel==1;

              for(var i=0;i<this.initData.imu.length;i++){
                if(res.data.imu==this.initData.imu[i].name){
                  this.file.imu = i;
                }
              }

              for(var i=0;i<this.initData.acoupos.length;i++){
                if(res.data.acoupos==this.initData.acoupos[i].name){
                  this.file.acoupos = i;
                }
              }

              for(var i=0;i<this.initData.dvl.length;i++){
                if(res.data.dvl==this.initData.dvl[i].name){
                  this.file.dvl = i;
                }
              }

            }else{
              this.changeAlgorithm('Algorithm01');
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
        });
      },
      changeAlgorithm(e) {
        var temp = JSON.parse(JSON.stringify(this.zhuangtai_canshu));
        this.zhuangtai_canshu.splice(0, this.zhuangtai_canshu.length);
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
      },
      changeStatus(e) {
        this.zhuangtai_canshu[e].checked = !this.zhuangtai_canshu[e].checked;
      },
      excute() {
        if (this.data.Algorithm == 0) {
          this.ExcuteDanYi();
        } else {
          this.ExcuteOther();
        }
      },
      ExcuteDanYi() {
        if (this.getLoadingStatus()) {
          this.$message.error('任务执行中，请稍后');
          return;
        }
        var temp = {
          config_path: this.initData.config[this.NowConfig].path,
          output_path: this.config.outputPath,
          DatumStationPath: this.config.DatumStationPath
        };
        this.$store.state.navigation.resultType = 0;
        this.$store.state.navigation.showResult = false;
        this.setLoadingStatus(true);
        this.$message.success('开始执行任务');
        NavgiationDanyi(temp).then((res) => {
          if (res.code != 0 || res.code == undefined) {
            this.$message.error(res.msg || res.message || "服务器错误");
          } else {
            this.$message.success('任务执行成功');
            this.$store.commit('setNavgiation', res);
            this.$store.state.navigation.resultType = 1;
            this.$store.state.navigation.showResult = true;
          }        
          this.setLoadingStatus(false);
        });
    },
    ExcuteOther() {
      if (this.getLoadingStatus()) {
        this.$message.error('任务执行中，请稍后');
        return;
      }
      var temp = {
        timestaend: [],
        robmodel: this.data.KangCha ? 1 : 0,
        rkflag: this.data.shiying?1:0,
        name: this.AlgorithmMenu[this.data.Algorithm].value,
        acoupos:this.initData.acoupos[this.file.acoupos].name,
        imu:this.initData.imu[this.file.imu].name,
        dvl:this.initData.dvl[this.file.dvl].name,
        function:this.AlgorithmMenu[this.data.Algorithm].value
      };
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
      this.$store.state.navigation.resultType = 0;
      this.$store.state.navigation.showResult = false;
      this.setLoadingStatus(true);
      this.$message.success('开始执行任务');
      NavigationExcuteOther(temp).then((res) => { 
        if (res.code != 0 || res.code == undefined) {
          this.$message.error(res.msg || res.message || "服务器错误");
        } else {
          this.$store.commit('setNavgiation', res);
          this.$store.state.navigation.resultType = 2;
          this.$store.state.navigation.showResult = true;
          this.$message.success('任务执行成功');
        }
        this.setLoadingStatus(false);
      });
      },
      getLoadingStatus() {
        return this.$store.state.navigation.loading;
      },
      setLoadingStatus(stat) {
        this.$store.state.navigation.loading = stat;
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
  .sub-content {
    margin-top: 5px;
  }
  .sub-title {
    width: 90%;
    margin-left: 7px;
    font-size: 14px;
    line-height: 24px;
    height: 24px;
    color: #99B3C8;
    background: linear-gradient(90deg, rgba(8,83,106,0.79) 0%, rgba(13,78,99,0.03) 100%);
    padding-left: 5px;
    font-weight: 500;
  }
  .sub-title-name {
        display: inline-block;
        width: 150px;
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
    .table-path {
        width: 20%;
        height: 25px;
        align-items: center;
        display: flex;
        justify-content: center;
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
    .table-item50 {
        text-align: center;
        width: 17%;
        height: 20px;
    }
    .table-item90 {
        text-align: center;
        width: 90%;
        height: 20px;
        display: flex;
        flex-direction: row;
    }

    .table-item-left {
        text-align: left;
        width: 25%;
        height: 20px;
    }

    .table-item-left50 {
        text-align: left;
        width: 50%;
        height: 20px;
    }

    .table-item25 {
        text-align: center;
        width: 25%;
        height: 20px;
    }

    .table-item50 {
        text-align: center;
        width: 50%;
        height: 20px;
    }

    .table-item-select {
        height: 20px;
        width: 100%;
        background: rgba(47, 136, 181, 0.16);
    }

    .table-item-input {
        height: 20px;
        width: 25%;
        border-radius: 0px 0px 0px 0px;
        margin: 0 5px;
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
    .item-checkbox {
      margin: 0 5px;
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
  
  </style>