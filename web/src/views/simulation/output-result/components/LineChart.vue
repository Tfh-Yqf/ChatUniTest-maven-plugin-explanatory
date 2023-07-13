<template>
  <div id="chart" style="height: 300px;"></div>
</template>


<script>
import * as echarts from 'echarts';
import 'echarts-gl';

export default {
  name: 'simulation_result',
  props: ['data'],
  data() {
    return {
      files: [],
    };
  },

  methods: {
    Draw_chart() {
      let xmap = {};
      let xData = [], yData = [];
      for (let i = 0; i < this.$props.data.length; ++i) {
        let [x, y] = this.$props.data[i].split(',');
        if (xmap.hasOwnProperty(x)) continue;
        xmap[x] = 1;
        xData.push(x), yData.push(y);
      }

      var chartDom = document.getElementById('chart');
      var myChart = echarts.init(chartDom);
      var option;

      option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['y']
        },
        title: {
          text: 'trace',
        },
        grid: {
          left: '1%',
          right: '10%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          name: "x",
          type: 'category',
          boundaryGap: false,
          data: xData,

        },
        yAxis: {
          name: "y",
          type: 'value',
          min: 4381400,
          max: 4382300,
          data: yData
        },
        series: [
          {
            name: 'y',
            type: 'line',
            stack: 'Total',
            color: "#5470C6",
            data: yData
          },
        ]
      };

      option && myChart.setOption(option);
    },
  },

  mounted() {
    this.Draw_chart();
  },
};
</script>

<style scoped></style>