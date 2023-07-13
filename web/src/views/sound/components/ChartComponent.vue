<template>
  <div style="height: 100%;">
    <div v-if="data.type != 4" style="display: flex;flex-direction: row;width: 100%;justify-content: center;">
      <div id="echart1" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="echart1"></div>
    </div>

    <div class="result-desc" v-if="data.type == 1 || data.type == 3 || data.type == 4 || data.type == 5">精度RMSE:
      <a class="result-number">{{ data.rmse.toFixed(4) }}</a>m/s</div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

import {
  sound_2d_01, sound_svm
} from '@/components/Echarts/sound_result';

export default {
  name: 'sound_voice',
  components: {

  },
  data() {
    return {
      menu: [
        { key: 1, name: '图表展示' }
      ],
      now_menu_keys: [1],
      now_menu: 1,
      data: {},
      screenWidth: document.body.clientWidth - 270,
      path: []
    };
  },

  methods: {
    init_echart_SVM() {
      var temp = {
        test: []
      };
      for (var i = 0; i < this.data.depth.length; i++) {
        temp.test.push([this.data.test[i], i * -1]);
      }
      var myChart = echarts.init(this.$refs.echart1);
      myChart.setOption(sound_svm(temp));
    },
    init_echart() {
      var temp = {
        test: [],
        tar: []
      };
      for (var i = 0; i < this.data.test.length; i++) {
        temp.test.push([this.data.test[i], i * -1]);
        temp.tar.push([this.data.tar[i], i * -1]);
      }
      var myChart = echarts.init(this.$refs.echart1);
      myChart.setOption(sound_2d_01(temp));
    },
    change_menu(e) {
      this.now_menu_keys[0] = e.key;
      this.now_menu = e.key;

      this.init_echart();
    },
    initData() {

      this.data = this.$store.state.system.sound;
      // this.data.rmse = this.data.rmse.toFixed(4);
      if (this.data.type == 1 || this.data.type == 3) {
        // BP神经网络和元学习
        this.init_echart();
      } else if (this.data.type != 4 && this.data.type != 5) {
        // svm
        this.init_echart_SVM();
      } else if (this.data.type == 4) {
        // NC反演
      } else if (this.data.type == 5) {
        // SC反演
        this.data.test = this.data.predicts;
        this.data.tar = this.data.y_true;
        this.init_echart();
      }



    }
  },
  mounted() {
    this.initData();
  },
};
</script>

<style lang="less" scoped>
.result-desc {
  width: 100%;
  text-align: center;
  font-weight: bold;
  font-size: 20px;
  color: #B0D8F5;
}
.result-number {
  color: aliceblue;
  margin-right: 10px;
  margin-left: 10px;
  text-decoration:none
}
</style>
