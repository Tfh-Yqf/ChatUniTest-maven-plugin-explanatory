<template>
  <div class="output-contanior">

    <div class="output-item">
      <div class="title" @click="changeFolderStatus">重力匹配结果</div>
    </div>

    <div v-if="showMap != null">
      <piemap ref="map" :data="T4" :matrix="matrix"></piemap>
    </div>

    <div class="draw-contanior" v-show="folderStatus">
      <div class="draw-content">
        <!-- <div v-if="showMap != null">
          <piemap ref="map" :data="T4" :matrix="matrix"></piemap>
        </div> -->

        <div class="draw-result">

          <a-card title="误差变化曲线图" class="draw-title">
            <div id="third_picture" style="height: 320px"></div>
          </a-card>
          <a-card title="总体误差统计图" class="draw-title">
            <div id="fourth_picture" style="height: 290px"></div>
          </a-card>
      </div>


      </div>

      <div class="draw-content" v-show="folderStatus">

        <a-card title="研究海域的重力异常热力图" class="draw-title">
          <div id="first_picture" style="height: 600px"></div>
        </a-card>
        <div style="width: 100%; height: 5px;"></div>
        <a-card title="研究海域的重力异常3D模型图" class="draw-title">
          <div id="second_picture" style="height: 550px"></div>
        </a-card>

      </div>

    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import 'echarts-gl';

export default {
  name: 'GravityConfig',
  components: {
    piemap: () => import('@/components/pie_map/GravityMap.vue')
  },
  data() {
    return {
      data: null,
      T4: null,
      showMap: false,
      matrix: null,
      folderStatus: true,
    }
  },
  methods: {
    init() {
      this.data = this.$store.state.system.gravity;
      // 设置T4图
      var T4 = this.data.T4;
      var res = [];
      var max_latitude = -1000;
      var max_longitude = -1000;
      var min_latitude = 1000;
      var min_longitude = 1000;

      for (var i = 0; i < T4.length; i++) {
        if (T4[i][0] > max_longitude) max_longitude = T4[i][0];
        if (T4[i][0] < min_longitude) min_longitude = T4[i][0];

        if (T4[i][1] > max_longitude) max_longitude = T4[i][1];
        if (T4[i][1] < min_longitude) min_longitude = T4[i][1];

        if (T4[i][2] > max_latitude) max_latitude = T4[i][2];
        if (T4[i][2] < min_latitude) min_latitude = T4[i][2];

        if (T4[i][3] > max_latitude) max_latitude = T4[i][3];
        if (T4[i][3] < min_latitude) min_latitude = T4[i][3];

        var temp = { coordinates: [T4[i][2], T4[i][0], T4[i][3], T4[i][1]], color: T4[i][4], origin: T4[i] };
        res.push(temp);
      }

      max_longitude += 0.5;
      max_latitude += 0.5;
      min_latitude -= 0.5;
      min_longitude -= 0.5;
      var matrix = [[max_latitude, max_longitude], [max_latitude, min_longitude], [min_latitude, min_longitude], [min_latitude, max_longitude], [max_latitude, max_longitude]];

      this.matrix = matrix;
      this.T4 = res;
      var that = this;
      setTimeout(function () {
        that.showMap = true;
      }, 2000);

      this.Draw_First_and_Second_Picture();
      this.Draw_Third_Picture();
      this.Draw_Fourth_Picture();


    },

    Draw_First_and_Second_Picture() {
      var chartDom = document.getElementById('first_picture');
      var myChart = echarts.init(chartDom);
      var option;

      let xData = [];
      let yData = [];
      let data = [];
      let map = {};
      let minx = 99999, maxx = -99999;
      let miny = 99999, maxy = -99999;
      let minz = 99999, maxz = -99999;

      function operator(str) {
        let sz = str.length;
        if (str[sz - 1] < '5') str = str.slice(0, -1) + '0';
        else str = str.slice(0, -1) + '5';
        return str;
      }

      for (let i = 0; i < this.data.Picture3d.length; i++) {
        var t = this.data.Picture3d[i][0].trim().split(/\s+/);
        t[0] = operator(parseFloat(t[0]).toFixed(2));
        t[1] = operator(parseFloat(t[1]).toFixed(2));
        t[2] = operator(parseFloat(t[2]).toFixed(2));
        if (map.hasOwnProperty(t[0] + t[1])) continue;
        map[t[0] + t[1]] = t[2];
        if (isNaN(t[0] || isNaN(t[1]) || isNaN(t[2]))) continue;
        data.push([t[0], t[1], t[2]]);
        minx = Math.min(minx, t[0]), maxx = Math.max(maxx, t[0]);
        miny = Math.min(miny, t[1]), maxy = Math.max(maxy, t[1]);
        minz = Math.min(minz, t[2]), maxz = Math.max(maxz, t[2]);
      }
      for (let i = minx; i <= maxx; i += 0.05) xData.push(i.toFixed(2));
      for (let i = miny; i <= maxy; i += 0.05) yData.push(i.toFixed(2));

      option = {
        tooltip: {},
        xAxis: {
          type: 'category',
          data: xData
        },
        yAxis: {
          type: 'category',
          data: yData
        },
        visualMap: {
          min: minz,
          max: maxz,
          calculable: true,
          realtime: false,
          inRange: {
            color: [
              '#3E26A8',
              '#4537D5',
              '#4269FE',
              '#2698E9',
              '#01BAC3',
              '#56CC7B',
              '#DCBD29',
              '#FEC238',
              '#F9FB15',
            ]
          }
        },
        series: [
          {
            name: 'Gaussian',
            type: 'heatmap',
            data: data,
            emphasis: {
              itemStyle: {
                borderColor: '#333',
                borderWidth: 1
              }
            },
            progressive: 1000,
            animation: false
          }
        ]
      };
      option && myChart.setOption(option);


      chartDom = document.getElementById('second_picture');
      myChart = echarts.init(chartDom);
      option = {
        tooltip: {},
        backgroundColor: '#fff',
        visualMap: {
          min: minz,
          max: maxz,
          calculable: true,
          realtime: false,
          inRange: {
            color: [
              '#3E26A8',
              '#4537D5',
              '#4269FE',
              '#2698E9',
              '#01BAC3',
              '#56CC7B',
              '#DCBD29',
              '#FEC238',
              '#F9FB15',
            ]
          }
        },
        xAxis3D: {
          type: 'value',
          min: minx,
          max: maxx,
        },
        yAxis3D: {
          type: 'value',
          min: miny,
          max: maxy,
        },
        zAxis3D: {
          type: 'value',
          min: minz,
          max: maxz,
        },
        grid3D: {
          viewControl: {}
        },
        series: [
          {
            type: 'surface',
            wireframe: {},
            data: data,
          }
        ]
      };

      option && myChart.setOption(option);
    },
    Draw_Third_Picture() {
      let xData = this.data.error.x;
      let yData0 = [], yData1 = [], yData2 = [];
      for (let i = 0; i < this.data.error.y.length; i++) {
        var t = this.data.error.y[i];
        yData0.push(t[0]), yData1.push(t[1]), yData2.push(t[2]);
      }

      var chartDom = document.getElementById('third_picture');
      var myChart = echarts.init(chartDom);
      var option;

      option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['B-err', 'L-err', 'D-err']
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
          name: "t(min)",
          type: 'category',
          boundaryGap: false,
          data: xData
        },
        yAxis: {
          name: "err(n mile)",
          type: 'value'
        },
        series: [
          {
            name: 'B-err',
            type: 'line',
            stack: 'Total',
            color: "#5470C6",
            data: yData0
          },
          {
            name: 'L-err',
            type: 'line',
            stack: 'Total',
            color: "#FE4B00",
            data: yData1
          },
          {
            name: 'D-err',
            type: 'line',
            stack: 'Total',
            color: "yellow",
            data: yData2
          }
        ]
      };

      option && myChart.setOption(option);
    },

    Draw_Fourth_Picture() {
      let data1 = [], data2 = [], xAxisData = ['B-err', 'D-err', 'L-err'];
      for (let i = 0; i < this.data.meanerror.length; i++) {
        var t = this.data.meanerror[i];
        data1.push(t[0]), data2.push(t[1]);
      }

      var chartDom = document.getElementById('fourth_picture');
      var myChart = echarts.init(chartDom);
      var option;

      option = {
        legend: {
          data: ['mean (n mile)', 'std (n mile)'],
          left: '10%'
        },
        toolbox: {
          feature: {
            magicType: {
              type: ['stack']
            },
            dataView: {}
          }
        },
        tooltip: {},
        xAxis: {
          data: xAxisData,
          name: 'X Axis',
          axisLine: { onZero: true },
          splitLine: { show: false },
          splitArea: { show: false }
        },
        yAxis: {},
        grid: {
          bottom: '5%'
        },
        series: [
          {
            name: 'mean (n mile)',
            type: 'bar',
            stack: 'one',
            color: "#5470C6",
            data: data1
          },
          {
            name: 'std (n mile)',
            type: 'bar',
            stack: 'two',
            color: "#FE4B00",
            data: data2
          },
        ]
      };

      option && myChart.setOption(option);

    },
    changeFolderStatus() {
      this.folderStatus = !this.folderStatus;
    }
  },
  mounted() {
    this.init();
  },
}
</script>

<style scoped>

.output-contanior {
  height: 90vh;
  margin-top: 10px;
}
.title {
  background: rgba(25, 236, 255, 0.24);
  width: 100%;
  height: 40px;
  border-left: 5px solid rgba(25, 236, 255);
  line-height: 40px;
  text-align: center;
  font-size: 20px;
  font-family: Source Han Sans CN-Medium, Source Han Sans CN;
  font-weight: 500;
  color: #B0D8F5;
}
.draw-contanior {
  width: 100%;
  margin-top: 10px;
  background-color: #000;
  opacity: 0.8;
  :deep(.ant-card-head-title){
    color: #99B3C8;
  }
}
.draw-result {
  display: flex;
  flex-direction: column;
  height: 800px;
  color: #99B3C8;
}
.draw-content {
  display: flex;
  flex-direction: column;
  width: 100%;
}
.draw-title {
  margin-top: 10px;
  background-color: #000;
  color: #99B3C8;
  border:  none;
}
</style>