<template>

  <!-- <div class="output-contanior">

    <div class="output-result">
    </div>
  </div> -->

  <div class="output-contanior">

    <div class="output-item">
      <div class="title" @click="changeFolderStatus">水下导航结果</div>
    </div>

    <div class="menu-contanior" v-show="folderStatus">
      <a-menu class="menu-bar" mode="horizontal" :selectedKeys="now_menu_keys" @click="change_menu">
        <a-menu-item v-for="(item, index) in menu" :key="item.key">{{ item.name }}</a-menu-item>
      </a-menu>

    </div>

    <div class="draw-contanior" v-show="now_menu == 1 && folderStatus">
      <a-card title="Att-3D散点图" class="draw-title">
        <div class="draw-content">
          <div id="att3d" :style="{ width: screenWidth + 'px' }" style="height: 1000px" ref="att3d"></div>
        </div>
      </a-card>

      <a-card title="Att-2维平面图" class="draw-title">
        <div class="draw-content">
          <div id="att" :style="{ width: screenWidth + 'px' }" style="height: 800px;" ref="att"></div>
        </div>
      </a-card>
    </div>

    <div class="draw-contanior" v-show="now_menu == 2 && folderStatus">
      <a-card title="enu-3D散点图" class="draw-title">
        <div class="draw-content">
          <div id="enu3d" :style="{ width: screenWidth + 'px' }" style="height: 1000px;" ref="enu3d"></div>
        </div>
      </a-card>

      <a-card title="enu-2维平面图" class="draw-title">

        <div class="draw-content">
          <div id="enu" :style="{ width: screenWidth + 'px' }" style="height: 800px;" ref="enu"></div>
        </div>
      </a-card>
    </div>

    <div class="draw-contanior" v-show="now_menu == 3 && folderStatus">
      <a-card title="VEL-3D散点图" class="draw-title">
        <div class="draw-content">
          <div id="vel3d" :style="{ width: screenWidth + 'px' }" style="height: 1000px;" ref="vel3d"></div>
        </div>
      </a-card>

      <a-card title="VEl-2维平面图" class="draw-title">

        <div class="draw-content">
          <div id="vel" :style="{ width: screenWidth + 'px' }" style="height: 800px;" ref="vel"></div>
        </div>
      </a-card>
    </div>

    <div class="draw-contanior" v-show="now_menu == 4 && folderStatus">
      <a-card title="poserr-2维平面图" class="draw-title">
        <div class="draw-content">
          <div id="poserr" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="poserr"></div>
        </div>

      </a-card>
    </div>

  </div>
</template>


<script>
import * as echarts from 'echarts'


import {
  Coordinate_System_enu,
  Coordinate_System_vel,
  Coordinate_System_3DAnd2D,
  Coordinate_System_poserr,
  Coordinate_System_att
} from '@/components/Echarts/navigation_result';

export default {
  name: 'OtherResult',
  data() {
    return {
      menu: [

        { key: 1, name: '姿态(att)' },
        { key: 2, name: '位置(enu)' },
        { key: 3, name: '速度(vel)' },
        { key: 4, name: 'Denu' },
      ],
      now_menu_keys: [1],
      now_menu: 1,
      data: {},
      screenWidth: 450,
      path: [],
      init_map_succ: false,
      point: null,
      folderStatus: true, 
    }
  },

  methods: {
    init_echart() {
      var myChartAtt = echarts.init(this.$refs.att);
      myChartAtt.setOption(Coordinate_System_att(this.data));

      var myChart_Att3d_data = [['time', 'e', 'n', 'u']];
      for (var i = 0; i < this.data.time.length; i++) {
        var temp = [
          this.data.time[i],
          this.data.att_e[i],
          this.data.att_n[i],
          this.data.att_u[i]
        ];
        myChart_Att3d_data.push(temp);
      }
      var myChart_att3d = echarts.init(this.$refs.att3d);
      myChart_att3d.setOption(Coordinate_System_3DAnd2D(myChart_Att3d_data));

      // 位置（enu）
      var myChart = echarts.init(this.$refs.enu);
      myChart.setOption(Coordinate_System_enu(this.data));

      var myChart_enu3d_data = [['time', 'e', 'n', 'u']];
      for (var i = 0; i < this.data.time.length; i++) {
        var temp = [
          this.data.time[i],
          this.data.enu_e[i],
          this.data.enu_n[i],
          this.data.enu_u[i]
        ];
        myChart_enu3d_data.push(temp);
      }
      var myChart_enu3d = echarts.init(this.$refs.enu3d);
      myChart_enu3d.setOption(Coordinate_System_3DAnd2D(myChart_enu3d_data));


      // 速度（vel）
      var myChart_vel3d_data = [['time', 'e', 'n', 'u']];
      for (var i = 0; i < this.data.time.length; i++) {
        var temp = [
          this.data.time[i],
          this.data.vel_e[i],
          this.data.vel_n[i],
          this.data.vel_u[i]
        ];
        myChart_vel3d_data.push(temp);
      }
      var myChart_vel3d = echarts.init(this.$refs.vel3d);
      myChart_vel3d.setOption(Coordinate_System_3DAnd2D(myChart_vel3d_data));

      var myChart_vel = echarts.init(this.$refs.vel);
      myChart_vel.setOption(Coordinate_System_vel(this.data));

      // Denu
      var myChart_poserr = echarts.init(this.$refs.poserr);
      myChart_poserr.setOption(Coordinate_System_poserr(this.data));
    },
    change_menu(e) {
      this.now_menu_keys[0] = e.key;
      this.now_menu = e.key;
      this.init_echart();
    },
    initData() {

      this.data = this.$store.state.system.Navgiation.data;
      this.init_echart();

    },
    changeFolderStatus() {
      this.folderStatus = !this.folderStatus;
    }
  },
  mounted() {
    this.initData();
  },
}
</script>
<style scoped lang="less">
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

.menu-contanior {
  width: 100%;
  background-color: #000;
  padding-top: 10px;
  opacity: 0.8;
}
.menu-bar {
  background-color: #000;
  color: #99B3C8;
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
.draw-title {
  margin-top: 10px;
  background-color: #000;
  color: #99B3C8;
  border:  none;
}
.draw-content {
  display: flex;
  flex-direction: row;
  width: 100%;
  justify-content: center;
}
</style>