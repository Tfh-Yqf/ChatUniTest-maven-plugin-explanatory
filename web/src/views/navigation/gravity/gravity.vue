<template>
    <div class="card card0">
        <div class="card-body card1">
            <div class="row">
                <div class="col">
                  <a-card title="读取重力异常基准图数据">
                    <div class="card-body">
                      <select class="form-select form-select-lg mb-3" aria-label=".form-select-lg example"
                              v-model="data.data_path">
                        <option v-for="gravity in gravity_list" :key="gravity.id">{{ gravity }}</option>
                      </select>
                    </div>
                  </a-card>

                </div>
                <div class="col">
                  <a-card title="读取实测重力异常数据">
                    <div class="card-body">
                      <select class="form-select form-select-lg mb-3" aria-label=".form-select-lg example">
                      </select>
                    </div>
                  </a-card>

                </div>
                <div class="col">
                  <a-card title="读取惯性导航轨迹数据">
                    <div class="card-body">
                      <select class="form-select form-select-lg mb-3" aria-label=".form-select-lg example">
                      </select>
                    </div>
                  </a-card>
                </div>
            </div>

          <a-card style="margin-top: 10px" title="重力匹配仿真参数配置">
            <div class="">
              <div class="card-body">
                <form @submit.prevent="Excute">
                  <div class="row row0">
                    <div class="col">
                      <label for="Angle" class="form-label">Angle(°)</label>
                      <input type="text" class="form-control" id="Angle" v-model="data.angle">
                    </div>
                    <div class="col">
                      <label for="Initial" class="form-label">Initial(`)</label>
                      <input type="text" class="form-control" id="Initial" v-model="data.initial">
                    </div>
                    <div class="col">
                      <label for="Velocity" class="form-label">Velocity(kt)</label>
                      <input type="text" class="form-control" id="Velocity" v-model="data.velocity">
                    </div>
                  </div>

                  <div class="row row0">
                    <div class="col">
                      <label for="x_error" class="form-label">x_error(n mile/h)</label>
                      <input type="text" class="form-control" id="x_error" v-model="data.x_error">
                    </div>
                    <div class="col">
                      <label for="SampleInterval" class="form-label">SampleInterval(min)</label>
                      <input type="text" class="form-control" id="SampleInterval" v-model="data.sampleInterval">
                    </div>
                    <div class="col">
                      <label for="y_error" class="form-label">y_error(n mile/h)</label>
                      <input type="text" class="form-control" id="y_error" v-model="data.y_error">
                    </div>
                  </div>

                  <div class="row row0">
                    <div class="col">
                      <label for="SampleTimes" class="form-label">SampleTimes</label>
                      <input type="text" class="form-control" id="SampleTimes" v-model="data.sampleTimes">
                    </div>
                    <div class="col">
                      <label for="INS_sigma_path" class="form-label">INS_sigma_path(`)</label>
                      <input type="text" class="form-control" id="INS_sigma_path" v-model="data.INS_sigma_path">
                    </div>
                    <div class="col text-center" style="padding-top: 20px;">
                      <a-button type="primary" @click="Excute"
                              style="width: 120px;height: 40px;font-size: 17px">执行算法</a-button>
                      <button type="button" class="ant-btn ant-btn-primary ant-btn-lg" @click="updateServeData"
              style="width: 100px;border-radius: 20px;margin-left: 20px; margin-top: 5px;"><span>更新数据</span></button>
                    </div>
                  </div>
                </form>
              </div>

            </div>

          </a-card>


            <div class="Myprogress">
                <div class="d-flex align-items-center" style="margin-bottom: 5px;">
                    <strong>Loading...</strong>
                    <div class="spinner-border ms-auto" role="status" aria-hidden="true"></div>
                </div>
                <div class="progress" role="progressbar" aria-label="Animated striped example" aria-valuenow="75"
                    aria-valuemin="0" aria-valuemax="100">
                    <div class="progress-bar progress-bar-striped progress-bar-animated wait_div"></div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { gravity_initdata, gravity_excute } from '@/api/Navgiation'
import { UpdateServeData } from '@/api/sound'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.bundle"
import $ from 'jquery'

export default {
    data() {
        return {
            gravity_list: [],
            data: {
                data_path: null,
                sigma_path: 0.06,
                sigma_grav: 1,
                angle: 60.0,
                initial: 5.0,
                velocity: 10.0,
                x_error: 2.0,
                sampleInterval: 3,
                y_error: 0.1666666666,
                sampleTimes: 10,
                INS_sigma_path: 0.2,
            }
        }
    },
    mounted() {
        this.initData();
    },
    methods: {
        initData() {
            gravity_initdata().then((res) => {
                console.log(res);
                this.gravity_list = res.gravity;
            })
        },
        Excute() {
            $(".wait_div").css({
                'width': '0%'
            });
            $('.Myprogress').show();
            let width = 0;
            var interval = setInterval(function f() {
              width += 1;
              $(".wait_div").css({
                'width': width + '%'
              });
            }, 1200);
            gravity_excute(this.data).then((res) => {
                console.log(res);
                if (res.code == 0) {
                  this.$store.commit('setGravity',res.data);
                  this.$router.push('/navigation/gravity_result');
                  clearInterval(interval);
                }
            });

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
    }
}
</script>

<style scoped>
.card0 {
    background-color: white !important;
    margin-left: 10px;
    margin-right: 10px;
}

.card1 {
    background-color: white;
    padding: 30px 20px;
}

.card2 {
    margin-top: 30px;
}

.row0 {
    margin: 13px 0;
}

.Myprogress {
    display: none;
    height: 30px;
    margin: 10px;
}
</style>

