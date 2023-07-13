<template>
  <div class="output-result">
    <div class="output-item" v-if="($store.state.simulation.resultType == 1 || $store.state.simulation.resultType == 3 ||
    $store.state.simulation.resultType == 4)">
      <div class="title" v-if="$store.state.simulation.resultType == 1" @click="changeFolderStatus">惯性导航数据</div>
      <div class="title" v-if="$store.state.simulation.resultType == 3" @click="changeFolderStatus">多普勒测速数据</div>
      <div class="title" v-if="$store.state.simulation.resultType == 4" @click="changeFolderStatus">压力计观测数据</div>
      <LineChart :data="data" v-if="folderStatus && $store.state.simulation.resultShow" />
    </div>
    <div class="output-item" v-if="$store.state.simulation.resultType == 2" >
      <div class="title" @click="changeFolderStatus">声呐数据</div>
      <CubeChart :data="sonarData" :points="sonarPoints"  v-if="folderStatus && $store.state.simulation.resultShow"/>
    </div>
    <div class="down-file-containor" v-if="folderStatus && $store.state.simulation.resultFiles.length > 0">
      <div class="title-bar">
        <div class="title-name">结果文件</div>
      </div>
      <div class="file-result">
        <div v-for="(item, index) in $store.state.simulation.resultFiles" :key="index" class="file-content">
          <a @click="down(item)" class="file-name">{{ item.name }}</a>
      </div>
      </div>
    </div>
  </div>
</template>

<script>
import LineChart from './components/LineChart.vue';
import CubeChart from './components/3Dchart.vue';
import { downFile } from '@/api/simulation';

export default {
  name: 'OutputResult',
  components: {
    LineChart,
    CubeChart
  },
  data() {
    return {
      folderStatus: true,
      isShow: false,
      data: [],
      sonarData: [],
      sonarPoints: [],
      isRender: false,
    };
  },
  methods: {
    down(e) {
      downFile(e).then((res) => {
        let url = URL.createObjectURL(new Blob([res], { type: "octet/stream" }));
        let a = document.createElement("a");
        a.href = url;
        a.download = e.name;
        a.click();
        URL.revokeObjectURL(url);
      });
    },
    drawChart() {
      let params = {
        name: "trace.txt",
        path: "/home/ubuntu/AcousticModule/simulate-resources/plot/trace.txt"
      };
      downFile(params).then(res => {
        const Data = res.split('\n');
        this.data = Data;
        this.$store.state.simulation.resultShow = true;
      });
    },
    initChart() {
      let data = [];
      let points = ["x", "y", "z"];
      let minx = 999999999, maxx = -999999999;
      let miny = 999999999, maxy = -999999999;
      let minz = 999999999, maxz = -999999999;

      let params = {
        name: "trace.txt",
        path: "/home/ubuntu/AcousticModule/simulate-resources/plot/Datumstation.txt",
      };
      downFile(params).then(res1 => {
        if (res1) {
          const Data = res1.split('\n');
          for (let i = 0; i < Data.length; ++i) {
            let [x, y, z] = Data[i].split(',');
            if (x.length === 0 || y.length === 0 || z.length == 0) continue;
            minx = Math.min(minx, x), maxx = Math.max(maxx, x), miny = Math.min(miny, y), maxy = Math.max(maxy, y), minz = Math.min(minz, z), maxz = Math.max(maxz, z);
            points.push([x, y, z]);
            this.sonarPoints = points;
          }
        }
        params = {
          name: "trace.txt",
          path: "/home/ubuntu/AcousticModule/simulate-resources/plot/acotrace.txt",
        };
        downFile(params).then(res2 => {
          if (res2) {
            const Data = res2.split('\n');
            let map = {};
            for (let i = 0; i < Data.length; ++i) {
              let [x, y, z] = Data[i].split(',');

              if (isNaN(x) || isNaN(y) || isNaN(z)) continue;

              if (map.hasOwnProperty(x + y)) continue;
              map[x + y] = 1;
              minx = Math.min(minx, x), maxx = Math.max(maxx, x), miny = Math.min(miny, y), maxy = Math.max(maxy, y), minz = Math.min(minz, z), maxz = Math.max(maxz, z);
              data.push([x, y, z]);
              this.sonarData = data;
              this.isRender = true;
            }
          }
        });
      });
    },
    changeFolderStatus() {
      this.folderStatus = !this.folderStatus;
    }
  },
  mounted() {
    this.drawChart();
    // this.initChart();
  },
};
</script>
<style lang="less" scoped>
.output-result {
  width: 100%;
  height: 100%;
  padding: 24px;

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
  }
  .down-file-containor {
    width: 100%;
    margin-top: 10px;
    color: #B0D8F5;
  }
  .file-content {
    width: 30%;
    height: 30px;
    line-height: 30px;
  }
  .file-name {
    color: #B0D8F5;
    cursor: pointer;
    text-decoration: none;
  }
  .file-result {
    display: flex;
    flex-direction: row;
  }
}
</style>
