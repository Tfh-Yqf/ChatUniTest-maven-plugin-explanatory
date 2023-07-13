<template>
    <div class="wrapper">
      <div class="sub-content">
        <div class="sub-title">
            <div class="sub-title-name">文件选取</div>
        </div>
        <div class="table-containor">
          <div class="table-content">
              <div class="table-row">
                  <div class="table-item-left">重力异常基准图数据</div>
                  <div class="table-item50">
                      <select class="table-item-select" name="" v-model.number="data.data_path">
                          <option v-for="gravity in gravity_list" :key="gravity.id">{{ gravity }}</option>
                      </select>
                  </div>
              </div>
              <div class="table-row">
                  <div class="table-item-left">实测重力异常数据</div>
                  <div class="table-item50">
                      <!-- <select class="table-item-select" name="" v-model.number="data.data_path">
                          <option v-for="gravity in gravity_list" :key="gravity.id">{{ gravity }}</option>
                      </select> -->
                      <select class="table-item-select">
                      </select>
                  </div>
              </div>
              <div class="table-row">
                  <div class="table-item-left">惯性导航轨迹数据</div>
                  <div class="table-item50">
                      <!-- <select class="table-item-select" name="" v-model.number="data.data_path">
                          <option v-for="gravity in gravity_list" :key="gravity.id">{{ gravity }}</option>
                      </select> -->
                      <select class="table-item-select">
                      </select>
                  </div>
              </div>
          </div>
        </div>

          <div class="sub-title">
              <div class="sub-title-name">重力匹配仿真参数配置</div>
          </div>
          <div class="table-containor">
            <div class="table-content">
                <div class="table-row">
                    <div class="table-item-left">Angle(°)</div>
                    <div class="table-item100">
                      <input v-model="data.angle" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">Initial(`)</div>
                    <div class="table-item100">
                      <input v-model="data.initial" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">Velocity(kt)</div>
                    <div class="table-item100">
                      <input v-model="data.velocity" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">x_error(n mile/h)</div>
                    <div class="table-item100">
                      <input v-model="data.x_error" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">SampleInterval(min)</div>
                    <div class="table-item100">
                      <input v-model="data.sampleInterval" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">y_error(n mile/h)</div>
                    <div class="table-item100">
                      <input v-model="data.y_error" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">SampleTimes</div>
                    <div class="table-item100">
                      <input v-model="data.sampleTimes" class="table-item-input">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">INS_sigma_path(`)</div>
                    <div class="table-item100">
                      <input v-model="data.INS_sigma_path" class="table-item-input">
                    </div>
                </div>
            </div>
          </div>
      </div>

      <div @click="excute" class="excute-btn">执行</div>
    </div>
  </template>
  
  
  <script>
  import { gravity_initdata, gravity_excute} from '@/api/Navgiation'

  export default {
    name: 'GravityConfig',
    data() {
      return this.$store.state.navigation.gravity;
    },
    methods: {
      init() {
        var that = this;
        gravity_initdata().then((res) => {
          that.gravity_list = res.gravity;
        });
      },
      excute() {
        if (this.getLoadingStatus()) {
          this.$message.error('任务执行中，请稍后');
          return;
        }
        this.setLoadingStatus(true);
        this.$message.success('开始执行任务');
        this.$store.state.navigation.resultType = 0;
        this.$store.state.navigation.showResult = false;
        gravity_excute(this.data).then((res) => {
            if (res.code == 0) {
              this.$store.commit('setGravity', res.data);
              this.$store.state.navigation.resultType = 3;
              this.$store.state.navigation.showResult = true;
              this.$message.success('任务执行成功');
            } else {
              this.$message.error(res.msg || res.message || "服务器错误");
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
    .table-item100 {
        text-align: center;
        width: 100%;
        height: 20px;
        display: flex;
        flex-direction: row;
    }

    .table-item-left {
        text-align: left;
        width: 35%;
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