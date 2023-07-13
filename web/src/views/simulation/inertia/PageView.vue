<template>
    <div class="wrapper">
        <div class="sub-content">
            <div class="sub-title">
                <div class="sub-title-name">轨迹列表</div>
                <a-radio-group v-model.number="inputWay" name="radioGroup"
                    class="sub-title-radio-group">
                    <a-radio value="1" class="sub-title-radio-group-option">表格填写</a-radio>
                    <a-radio value="2" class="sub-title-radio-group-option">文件选取</a-radio>
                </a-radio-group>
            </div>
            <div class="table-content" v-if="inputWay == 1">
                <div class="tab-pane fade show active" role="tabpanel" aria-labelledby="home-tab"
                    tabindex="0">
                    <div class="table-title">
                        <div class="table_path">序号</div>
                        <div class="table_path">航迹类型</div>
                        <div class="table_path">航行时间</div>
                        <div class="table_path">角速度</div>
                        <div class="table_path">加速度</div>
                    </div>

                    <div class="table-row" v-for="(item, index) in datasource_path" :key="item.id">
                        <div class="table-item">{{ item.id }}</div>
                        <div class="table-item">
                            <select class="table-item-select" name="" v-model.number="item.type">
                                <option :value="it" v-for="it in type_option" :key="it">{{ it }}</option>
                            </select>
                        </div>
                        <div class="table-item">
                            <input v-model="item.time" defaultValue="0" class="table-item-input">
                        </div>
                        <div class="table-item">
                            <input v-model="item.palstance" defaultValue="0" class="table-item-input">
                        </div>
                        <div class="table-item">
                            <input v-model="item.accelerated_speed" defaultValue="0"
                                class="table-item-input">
                        </div>
                    </div>

                    <div class="tail-row">
                        <div @click="addDatasourcePath()" class="confirm-botton">添加行</div>
                    </div>
                </div>
            </div>
            <div class="row-containor" v-if="inputWay == 2">
                <div class="left-title">
                    航迹列表数据:
                </div>
                <div class="rigth-content">
                    <select class="table-item-select" v-model.number="data.path">
                        <option :value="item" v-for="item in initdatas.path" :key="item.id">{{ item.name }}</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="sub-content">
            <div class="sub-title">
            <div class="sub-title-name">初始参数配置</div>
        </div>
        <div class="table-containor">
            <div class="table-content">
                <div class="table-row">
                    <div class="table-item-left">惯导采样率</div>
                    <div class="table-item25">
                        <input v-model="data.insrate" class="table-item-input95">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">初始姿态</div>
                    <div class="table-item25">
                        <input v-model="data.initatt[0]" placeholder="Pitch"
                            class="table-item-input95">
                    </div>
                    <div class="table-item25">
                        <input v-model="data.initatt[1]" placeholder="Roll"
                            class="table-item-input95">
                    </div>
                    <div class="table-item25">
                        <input v-model="data.initatt[2]" placeholder="Heading"
                            class="table-item-input95">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">初始速度</div>
                    <div class="table-item25">
                        <input v-model="data.initvel[0]" placeholder="E"
                            class="table-item-input95">
                    </div>
                    <div class="table-item25">
                        <input v-model="data.initvel[1]" placeholder="N"
                            class="table-item-input95">
                    </div>
                    <div class="table-item25">
                        <input v-model="data.initvel[2]" placeholder="U"
                            class="table-item-input95">
                    </div>
                </div>
                <div class="table-row">
                    <div class="table-item-left">初始位置</div>
                    <div class="table-item25">
                        <input v-model="data.initpos[0]" class="table-item-input95">
                    </div>
                    <div class="table-item25">
                        <input v-model="data.initpos[1]" class="table-item-input95">
                    </div>
                    <div class="table-item25">
                        <input v-model="data.initpos[2]" class="table-item-input95">
                    </div>
                </div>
            </div>
            </div>
        </div>
        <div class="sub-content">
            <div class="sub-title">
                <div class="sub-title-name">海洋环境参数</div>
            </div>
            <div class="table-containor">
                <div class="table-content">
                    <div class="table-row">
                        <div class="table-item-left" style="width: 50%;">是否模拟海洋环境参数</div>
                        <a-radio-group v-model.number="moni" name="radioGroup"
                            class="table-radio-group">
                            <a-radio value="1" class="table-radio-group-option">是</a-radio>
                            <a-radio value="0" class="table-radio-group-option">否</a-radio>
                        </a-radio-group>
                    </div>
                    <div class="table-row">
                        <div class="table-item" style="width: 100%;">潮汐</div>
                    </div>
                    <div class="table-row">
                        <div class="table-item-left">振幅(deg)</div>
                        <div class="table-item25">
                            <input v-model="data.tideamp" class="table-item-input95">
                        </div>
                        <div class="table-item-left">周期(h)</div>
                        <div class="table-item25">
                            <input v-model="data.tideper" class="table-item-input95">
                        </div>
                    </div>
                    <div class="table-row">
                        <div class="table-item" style="width: 100%;">波浪</div>
                    </div>
                    <div class="table-row">
                        <div class="table-item-left">角振幅(deg)</div>
                        <div class="table-item25">
                            <input v-model="data.waveangamp[0]" class="table-item-input95">
                        </div>
                        <div class="table-item25">
                            <input v-model="data.waveangamp[1]" class="table-item-input95">
                        </div>
                        <div class="table-item25">
                            <input v-model="data.waveangamp[2]" class="table-item-input95">
                        </div>
                    </div>
                    <div class="table-row">
                        <div class="table-item-left">角周期(m)</div>
                        <div class="table-item25">
                            <input v-model="data.waveangper[0]" class="table-item-input95">
                        </div>
                        <div class="table-item25">
                            <input v-model="data.waveangper[1]" class="table-item-input95">
                        </div>
                        <div class="table-item25">
                            <input v-model="data.waveangper[2]" class="table-item-input95">
                        </div>
                    </div>
                    <div class="table-row">
                        <div class="table-item-left">线振幅(deg)</div>
                        <div class="table-item25">
                            <input v-model="data.wavelinamp[0]" class="table-item-input95">
                        </div>
                        <div class="table-item25">
                            <input v-model="data.wavelinamp[1]" class="table-item-input95">
                        </div>
                        <div class="table-item25">
                            <input v-model="data.wavelinamp[2]" class="table-item-input95">
                        </div>
                    </div>
                    <div class="table-row">
                        <div class="table-item-left">线周期(m)</div>
                        <div class="table-item25">
                            <input v-model="data.wavelinper[0]" class="table-item-input95">
                        </div>
                        <div class="table-item25">
                            <input v-model="data.wavelinper[1]" class="table-item-input95">
                        </div>
                        <div class="table-item25">
                            <input v-model="data.wavelinper[2]" class="table-item-input95">
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <div class="sub-content">
            <div class="sub-title">
                <div class="sub-title-name" style="width: 50%;">惯性导航仪器误差参数设置</div>
            </div>
            <div class="table-containor">
                <div class="table-content">
                    <div class="tab-pane fade show active" role="tabpanel" aria-labelledby="home-tab"
                        tabindex="0">
                        <div class="table-title" style="height: 20px; text-align: left;">
                            <div class="table-item-left50"></div>
                            <div class="table-item17">X</div>
                            <div class="table-item17">Y</div>
                            <div class="table-item17">Z</div>
                        </div>

                        <div class="table-row">
                            <div class="table-item-left50">陀螺仪初始偏差</div>
                            <div class="table-item17">
                                <input v-model="data.gb[0]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.gb[1]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.gb[2]" class="table-item-input95">
                            </div>
                        </div>

                        <div class="table-row">
                            <div class="table-item-left50">陀螺仪比例因子误差标准偏差</div>
                            <div class="table-item17">
                                <input v-model="data.gsf[0]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.gsf[1]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.gsf[2]" class="table-item-input95">
                            </div>
                        </div>

                        <div class="table-row">
                            <div class="table-item-left50">陀螺标度因子误差功率谱密度</div>
                            <div class="table-item17">
                                <input v-model="data.gsfpsd[0]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.gsfpsd[1]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.gsfpsd[2]" class="table-item-input95">
                            </div>
                        </div>

                        <div class="table-row">
                            <div class="table-item-left50">加速度计初始偏差</div>
                            <div class="table-item17">
                                <input v-model="data.ab[0]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.ab[1]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.ab[2]" class="table-item-input95">
                            </div>
                        </div>
                        <div class="table-row">
                            <div class="table-item-left50">加速度计比例因子误差标准偏差</div>
                            <div class="table-item17">
                                <input v-model="data.asf[0]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.asf[1]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.asf[2]" class="table-item-input95">
                            </div>
                        </div>
                        <div class="table-row">
                            <div class="table-item-left50">加速度计标度因子误差功率谱密度</div>
                            <div class="table-item17">
                                <input v-model="data.asfpsd[0]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.asfpsd[1]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.asfpsd[2]" class="table-item-input95">
                            </div>
                        </div>

                        <div class="table-row">
                            <div class="table-item-left50">角度随机游走</div>
                            <div class="table-item17">
                                <input v-model="data.arw[0]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.arw[1]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.arw[2]" class="table-item-input95">
                            </div>
                        </div>
                        <div class="table-row">
                            <div class="table-item-left50">角速度随机游走</div>
                            <div class="table-item17">
                                <input v-model="data.arrw[0]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.arrw[1]" class="table-item-input95">
                            </div>
                            <div class="table-item17">
                                <input v-model="data.arrw[2]" class="table-item-input95">
                            </div>
                        </div>
                        <div class="table-row">
                            <div class="table-item-left50">一阶马尔可夫过程的相关时间</div>
                            <div class="table-item17">
                                <input v-model="data.cortime" class="table-item-input95">
                            </div>
                        </div>

                    </div>

                </div>
            </div>
        </div>
        <div @click="excute" class="confirm-botton excute-btn">执行</div>
    </div>
  </template>
  
  
  <script>
  import { SimulationExcute} from '@/api/simulation'

  export default {
    name: 'Inertia',
    data() {
      return this.$store.state.simulation.navigation;
    },
    methods: {
        initData() {
            if (this.datasource_path.length == 0) {
                this.addDatasourcePath();
            }
        },
        addDatasourcePath() {
            var temp = JSON.parse(JSON.stringify(this.path_template));
            temp.id = this.datasource_path.length + 1;
            this.datasource_path.push(temp);
        },
        excute() {
            if (this.getLoadingStatus()) {
                this.$message.error('任务执行中，请稍后');
                return;
            }
            this.setLoadingStatus(true);
            this.$message.success('开始执行任务');
            this.resultInit();
            let that = this;
            var params = {
                datatype: 1,
                tratype: "classic_none",
                svpfile: "", // 不需要
                oepflag: this.moni,
                data: this.data,
            };
            let files = ["insdataavp.txt", "insdataimu.txt"];
            SimulationExcute(params).then((res)=>{
                if (res.code==0) {
                    that.dealResult(res.data, files);
                    that.$message.success('任务执行成功');
                } else {
                    this.$message.error(res.msg || res.message || "服务器错误");
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
            this.$store.state.simulation.resultType = 1;
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
        this.initData();
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
        width: 100px;
    }

    .sub-title-radio-group {
        margin-left: 10px;
    }

    .sub-title-radio-group-option {
        color: #e4ebf0;
        height: 15px;
    }

    .table-radio-group {
        margin-left: 10px;
    }

    .table-radio-group-option {
        color: #e4ebf0;
        height: 15px;
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