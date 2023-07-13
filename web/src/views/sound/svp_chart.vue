<template>
  <div style="height: 100%;">
    <a-card title="声速场剖面图" style="">
      <div style="display: flex;flex-direction: row;width: 100%;justify-content: center;" >
        <div id="echart1"  style="height: 500px;width: 100%" ref="echart1"></div>
      </div>
    </a-card>
  </div>
</template>

<script>
import * as echarts from 'echarts';

import {
  sound_2d_svp
} from '@/components/Echarts/sound_result';

export default {
  name: 'svp_chart',
  components: {

  },
  data() {
    return {

    }
  },
  props:['data'],
  methods: {
    init_echart(){
      console.log("data:")
      console.log(this.data);


      var temp = [[],[],[],[],[]];
      for(var j=0;j<5;j++){
        for(var i=0;i<this.data.depth.length;i++){
          temp[j].push([this.data.data[j][i],this.data.depth[i]]);
        }
      }

      var myChart = echarts.init(this.$refs.echart1);
      myChart.setOption(sound_2d_svp(temp));

    },
  },
  mounted(){
    this.init_echart();
  },
}
</script>