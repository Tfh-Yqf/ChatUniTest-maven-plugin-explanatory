<template>
    <div class="content-container">
        <div class="input-wrapper">
            <div class="input-container">
                <div class="title-bar">
                    <div class="title-left"></div>
                    <div class="title-text">数据仿真</div>
                    <img src="@/assets/img/common/data.png" class="sync-data-icon" alt="同步数据" title="同步数据" @click="syncData">
                </div>
                <div class="input-block">
                    <div class="input-title-bar">
                        <img src="@/assets/img/left-bar/silumation-icon.png" class="input-title-left-icon">
                        <div class="input-title-text" @click="changeFolderStatus('navigation', 0)" v-if="navigation.folderStatus == 1">
                            <div class="input-title-name">惯性导航数据</div>
                            <div class="triangle-down"></div>
                        </div>
                        <div class="input-title-text" @click="changeFolderStatus('navigation', 1)" v-if="navigation.folderStatus == 0">
                            <div class="input-title-name">惯性导航数据</div>
                            <div class="triangle-up"></div>
                        </div>
                    </div>
                    <div class="input-content" v-show="navigation.folderStatus == 1">
                        <Inertia />

                    </div>
                </div>
                <div class="input-block">
                    <div class="input-title-bar">
                        <img src="@/assets/img/left-bar/silumation-icon.png"  class="input-title-left-icon">
                        <div class="input-title-text" @click="changeFolderStatus('sonar', 0)" v-if="sonar.folderStatus == 1">
                            <div class="input-title-name">声呐数据</div>
                            <div class="triangle-down"></div>
                        </div>
                        <div class="input-title-text" @click="changeFolderStatus('sonar', 1)" v-if="sonar.folderStatus == 0">
                            <div class="input-title-name">声呐数据</div>
                            <div class="triangle-up"></div>
                        </div>
                    </div>
                    <div class="input-content" v-show="sonar.folderStatus == 1">
                      <SonarData />
                    </div>
                </div>
                <div class="input-block">
                    <div class="input-title-bar">
                        <img src="@/assets/img/left-bar/silumation-icon.png" class="input-title-left-icon">
                        <div class="input-title-text" @click="changeFolderStatus('doppler', 0)" v-if="doppler.folderStatus == 1">
                            <div class="input-title-name">多普勒测速数据</div>
                            <div class="triangle-down"></div>
                        </div>
                        <div class="input-title-text" @click="changeFolderStatus('doppler', 1)" v-if="doppler.folderStatus == 0">
                            <div class="input-title-name">多普勒测速数据</div>
                            <div class="triangle-up"></div>
                        </div>
                    </div>
                    <div class="input-content" v-show="doppler.folderStatus == 1">
                      <DopplerConfig />
                    </div>
                </div>
                <div class="input-block">
                    <div class="input-title-bar">
                        <img src="@/assets/img/left-bar/silumation-icon.png"  class="input-title-left-icon">
                        <div class="input-title-text" @click="changeFolderStatus('pressure', 0)" v-if="pressure.folderStatus == 1">
                            <div class="input-title-name">压力计观测数据</div>
                            <div class="triangle-down"></div>
                        </div>
                        <div class="input-title-text" @click="changeFolderStatus('pressure', 1)" v-if="pressure.folderStatus == 0">
                            <div class="input-title-name">压力计观测数据</div>
                            <div class="triangle-up"></div>
                        </div>
                    </div>
                    <div class="input-content" v-show="pressure.folderStatus == 1">
                      <PressureObserve />
                    </div>
                </div>
            </div>
        </div>
        <div class="output-wrapper" v-if="$store.state.simulation.resultType != 0">
            <OutputResult />
        </div>
    </div>
</template>
<script>
import "@/assets/css/common.less";
import { simulationInitdata, SimulationExcute } from '@/api/simulation';
import { UpdateServeData } from '@/api/sound'
import Inertia from './inertia/PageView.vue';
import SonarData from './sonar-data/PageView.vue';
import DopplerConfig from './doppler-config/PageView.vue';
import PressureObserve from './pressure-observe/PageView.vue';
import OutputResult from './output-result/PageView.vue';

export default {
    name: 'Simulation',
    components: {
        Inertia,
        SonarData,
        DopplerConfig,
        PressureObserve,
        OutputResult
    },
    data() {
        return {
            navigation: {
                folderStatus: 0,
            },
            sonar: {
                folderStatus: 0,
            },
            doppler: {
                folderStatus: 0,
            },
            pressure: {
                folderStatus: 0,
            },
        };
    },
    methods: {
        initData() {
            this.getSimulationInitdata();
        },
        getSimulationInitdata() {
            simulationInitdata().then((res) => {
                if (res.code == 0) {
                    this.$store.state.simulation.navigation.initdatas.path = res.data.path;
                } else {
                    this.$message.success(res.message, 0.5);
                }
            });
        },
        changeFolderStatus(module, status) {
            this[module].folderStatus = status;
        },
        excute() {
            var temp = this.$store.state.system.SimulationConfig;
            var svpfile = '';
            if (temp == undefined || 'svpfile' in temp == false) {
                this.$message.error('svpfile未选择');
                return;
            }
            var moni = 0;
            if ('moni' in temp == false) {
                moni = 0;
                this.change_moni();
            }
            var temp = {
                datatype: 1,
                tratype: "classic_none",
                svpfile: svpfile,
                oepflag: moni,
                data: this.data,

            };
            let files = ["insdataavp.txt", "insdataimu.txt"];
            this.loading = true;
            SimulationExcute(temp).then((res) => {
                this.loading = false;
                if (res.code == 0) {
                    let result = [];
                    for (let i = 0; i < res.data.length; ++i) {
                        if (files.indexOf(res.data[i].name) > -1) {
                            result.push(res.data[i]);
                        }
                    }
                    this.$store.commit('setSimulationResult', result);
                }

            });
        },
        syncData() {
          this.$message.success('更新数据开始');
          var that = this;
          var data = { "folders": "ANModule,loose_combination,TERCOM"};
          UpdateServeData(data).then((res) => {
            if (res.code == 0) {
                that.$message.success('更新数据完成');
            }
          });
        }
    },
    mounted() {
        this.initData();
    }
};
</script>
  
<style scoped>
.sync-data-icon {
    width: 20px;
    height: 20px;
    margin-top: 10px;
    cursor: pointer;
}
</style>
