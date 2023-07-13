<template>
  <div style="height: 90vh;">
    <a-card title="计算结果"
      style="width: 100%;margin-top: 10px;margin-left: 20px;margin-bottom: 10px;justify-content: space-between;">
      <div style="display: flex;flex-direction: row;flex-wrap: wrap;width: 100%">
        <div v-for="(item, index) in files" :key="index"
          style="display: flex;margin-top: 10px;width: 200px;align-items: center">
          <div style="width: 100px;flex-shrink: 0;">{{ item.name }}</div>
          <a-button style="" type="primary" @click="down(item)">下载</a-button>
        </div>

      </div>
    </a-card>

    <div id="chart" style="margin-left: 20px; height: 500px;"></div>
    <div>
    </div>
  </div>
</template>


<script>
import { downFile } from '@/api/simulation'
import { SimulationGetTxt } from '@/api/simulation'
import * as echarts from 'echarts';
import 'echarts-gl';

export default {
  name: 'simulation_result',
  data() {
    return {
      files: [],
    }
  },

  methods: {
    // init(){

    // },
    down(e) {
      console.log(e);
      downFile(e).then((res) => {

        let url = URL.createObjectURL(new Blob([res], { type: "octet/stream" }));
        let a = document.createElement("a");
        a.href = url;
        a.download = e.name;
        a.click();
        URL.revokeObjectURL(url);
      });
    },
    fetchData(e) {
      console.log(e);
      downFile(e).then((res => {

        const fileData = res;
        const lines = fileData.split('\n')

        let pointList = [];
        for (let i = 0; i < lines.length; ++i) {
          let splits = lines[i].split(',')
          let arr = [];
          arr.push(parseFloat(splits[0]))
          arr.push(parseFloat(splits[1]))
          pointList.push(arr);
        }

        var chartDom = document.getElementById('pointsShow1');
        var myChart = echarts.init(chartDom);
        var option;

        console.log("PointList")
        console.log(pointList);

        option = {
          toolbox: {
            show: true,
            feature: {
              dataZoom: {
                yAxisIndex: 'none'
              },
              dataView: { readOnly: false },
              magicType: { type: ['line', 'bar'] },
              restore: {},
              saveAsImage: {}
            },
            right: '5%'
          },
          title: {
            text: 'trace',
          },
          symbol: 'circle',
          xAxis: {
            interval: 100,
            min: -2161350,
            max: -2161000,
          },
          yAxis: {
            min: 4382000,
            max: 4382220,
          },
          series: [
            {
              symbolSize: 20,
              data: pointList,
              type: 'scatter',
              symbolSize: 12
            }
          ]
        }
        option && myChart.setOption(option);

      }))

    },

    Draw_chart() {
      let params = {
        name: "trace.txt",
        path: "/home/ubuntu/AcousticModule/simulate-resources/plot/trace.txt"
      };

      downFile(params).then(res => {
        const Data = res.split('\n')

        let xmap = {};
        let xData = [], yData = [];
        for (let i = 0; i < Data.length; ++i) {
          let [x, y] = Data[i].split(',')
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
      })
    },

    Sonar_draw_chart() {
      let data = [];
      let Points = ["x", "y", "z"];
      var chartDom = document.getElementById('chart');
      var myChart = echarts.init(chartDom);
      var option;
      let minx = 999999999, maxx = -999999999;
      let miny = 999999999, maxy = -999999999;
      let minz = 999999999, maxz = -999999999;

      let params = {
        name: "trace.txt",
        // path: ["/home/ubuntu/AcousticModule/simulate-resources/plot/acotrace.txt", "/home/ubuntu/AcousticModule/simulate-resources/plot/Datumstation.txt"],
        path: "/home/ubuntu/AcousticModule/simulate-resources/plot/Datumstation.txt",
      };
      downFile(params).then(res => {
        const Data = res.split('\n')
        for (let i = 0; i < Data.length; ++i) {
          let [x, y, z] = Data[i].split(',')
          if (x.length === 0 || y.length === 0 || z.length == 0) continue;
          minx = Math.min(minx, x), maxx = Math.max(maxx, x), miny = Math.min(miny, y), maxy = Math.max(maxy, y), minz = Math.min(minz, z), maxz = Math.max(maxz, z);
          Points.push([x, y, z]);
        }

        params = {
          name: "trace.txt",
          path: "/home/ubuntu/AcousticModule/simulate-resources/plot/acotrace.txt",
        };

        downFile(params).then(res => {
          const Data = res.split('\n')
          let map = {};
          for (let i = 0; i < Data.length; ++i) {
            let [x, y, z] = Data[i].split(',')

            if (isNaN(x) || isNaN(y) || isNaN(z)) continue;

            if (map.hasOwnProperty(x + y)) continue;
            map[x + y] = 1;
            minx = Math.min(minx, x), maxx = Math.max(maxx, x), miny = Math.min(miny, y), maxy = Math.max(maxy, y), minz = Math.min(minz, z), maxz = Math.max(maxz, z);
            data.push([x, y, z]);
          }

          console.log(minx, maxx, miny, maxy, minz, maxz);
          console.log(data.length);
          console.log(Points);

          option = {
            tooltip: {},
            backgroundColor: '#fff',
            visualMap: {
              show: false,
              dimension: 2,
              min: minz,
              max: maxz,
              inRange: {
                color: [
                  '#313695',
                  '#4575b4',
                  '#74add1',
                  '#abd9e9',
                  '#30f3f8',
                  '#ffffbf',
                  '#fee090',
                  '#fdae61',
                  '#f46d43',
                  '#d73027',
                  '#a50026'
                ]
              }
            },
            xAxis3D: {
              type: 'category'
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
              viewControl: {
                projection: 'orthographic'
              }
            },
            dataset: {
              source: Points
            },
            series: [
              {
                type: 'line3D',
                data: data,
                lineStyle: {
                  width: 5
                }
              },

              {
                type: 'scatter3D',
                symbolSize: 20,
              }
            ]
          };

          option && myChart.setOption(option);
        })
      })
    }
  },

  mounted() {

    // this.fetchData({
    //   name: "trace.txt",
    //     path: "/home/ubuntu/AcousticModule/simulate-resources/plot/trace.txt"
    // })

    this.files = this.$store.state.system.SimulationResult;
    console.log(this.files);
    console.log(this.$route.query.runway);

    if (this.$route.query.runway === "sonar") {
      this.Sonar_draw_chart();
    } else {
      this.Draw_chart();
    }
  },
}
</script>

<style scoped>
.group_box {
  background: #FFFFFF;
  height: 37vh;
  width: 30%;
  display: flex;
  align-items: center;
  justify-content: center
}

.item {
  width: 100%;
  height: 100%;
  margin-top: 50px;
  margin-left: -10px
}
</style>