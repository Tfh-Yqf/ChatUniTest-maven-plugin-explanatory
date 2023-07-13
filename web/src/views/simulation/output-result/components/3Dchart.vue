<template>
  <div id="chart" style="height: 300px;"></div>
</template>


<script>
import * as echarts from 'echarts';
import 'echarts-gl';
import { downFile } from '@/api/simulation';

export default {
  name: 'CubeChart',
  data() {
    return {
      files: [],
    };
  },
  methods: {
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
        path: "/home/ubuntu/AcousticModule/simulate-resources/plot/Datumstation.txt",
      };
      downFile(params).then(res => {
        const Data = res.split('\n');
        for (let i = 0; i < Data.length; ++i) {
          let [x, y, z] = Data[i].split(',');
          if (x.length === 0 || y.length === 0 || z.length == 0) continue;
          minx = Math.min(minx, x), maxx = Math.max(maxx, x), miny = Math.min(miny, y), maxy = Math.max(maxy, y), minz = Math.min(minz, z), maxz = Math.max(maxz, z);
          Points.push([x, y, z]);
        }

        params = {
          name: "trace.txt",
          path: "/home/ubuntu/AcousticModule/simulate-resources/plot/acotrace.txt",
        };

        downFile(params).then(res => {
          const Data = res.split('\n');
          let map = {};
          for (let i = 0; i < Data.length; ++i) {
            let [x, y, z] = Data[i].split(',');

            if (isNaN(x) || isNaN(y) || isNaN(z)) continue;

            if (map.hasOwnProperty(x + y)) continue;
            map[x + y] = 1;
            minx = Math.min(minx, x), maxx = Math.max(maxx, x), miny = Math.min(miny, y), maxy = Math.max(maxy, y), minz = Math.min(minz, z), maxz = Math.max(maxz, z);
            data.push([x, y, z]);
          }
          option = {
            tooltip: {},
            backgroundColor: 'transparent',
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
              nameTextStyle: {
                color: '#fff',
              },
              axisLabel: {
                show: true,
                textStyle: {
                  color: '#fff'
                }
              }
            },
            yAxis3D: {
              type: 'value',
              min: miny,
              max: maxy,
              nameTextStyle: {
                color: '#fff',
              },
              axisLabel: {
                show: true,
                textStyle: {
                  color: '#fff'
                }
              }
            },
            zAxis3D: {
              type: 'value',
              min: minz,
              max: maxz,
              nameTextStyle: {
                color: '#fff',
              },
              axisLabel: {
                show: true,
                textStyle: {
                  color: '#fff'
                }
              }
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
        });
      });
    }

  },

  mounted() {
    this.Sonar_draw_chart();
  },
};
</script>

<style scoped></style>