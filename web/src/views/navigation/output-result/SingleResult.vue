<template>
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
      <div style="width: 100%;display: flex;flex-direction: row-reverse">
        <div
          style="margin-top: 160px;margin-right: 40px;width: 300px;height: 300px;justify-content: center;background-color: #FFFFFF;position: absolute;z-index: 2;opacity: 0.9;border-radius: 10px">
          <div id="enu3dSingle" style="height: 100%;width: 100%" ref="enu3dSingle"></div>
        </div>
      </div>
      <a-card title="导航轨迹图" class="draw-title" style="color: #99B3C8;">
        <piemap :data="path" v-if="init_map_succ" ref="map"></piemap>
      </a-card>

    </div>
    <div class="draw-contanior" v-show="now_menu == 2 && folderStatus">
      <a-card title="ENU-3D散点图" class="draw-title">
        <div class="draw-content">
          <div id="enu3d" :style="{ width: screenWidth + 'px' }" style="height: 1000px;" ref="enu3d"></div>
        </div>
      </a-card>

      <a-card title="ENU-2维平面图" class="draw-title">

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
      <a-card title="DENU-2维平面图" class="draw-title">

        <div class="draw-content">
          <div id="denu" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="denu"></div>
        </div>

        <div
          style="width: 100%;display: flex;justify-content: center;height: 30px;padding: 5px;font-size: 20px;font-weight: bold">
          RMS2D:{{ data.rms }} M</div>
      </a-card>
    </div>

    <div class="draw-contanior" v-show="now_menu == 5 && folderStatus">
      <a-card title="pdop-2维平面图" class="draw-title">

        <div class="draw-content">
          <div id="pdop" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="pdop"></div>
        </div>

      </a-card>
    </div>

  </div>
</template>


<script>
import * as echarts from 'echarts';


import {
  Coordinate_System_enu,
  Coordinate_System_vel,
  Coordinate_System_denu,
  Coordinate_System_3DAnd2D,
  Coordinate_System_3D,
  Coordinate_System_pdop
} from '@/components/Echarts/navigation_result';

export default {
  name: 'SignleResult',
  components: {
    piemap: () => import('@/components/pie_map/map.vue')
  },

  data() {
    return {
      menu: [
        { key: 1, name: '导航轨迹' },
        { key: 2, name: 'ENU坐标系' },
        { key: 3, name: 'VEL坐标系' },
        { key: 4, name: '误差' },
        { key: 5, name: 'pdop' },
      ],
      now_menu_keys: [1],
      now_menu: 1,
      data: {},
      screenWidth: 450,
      path: [],
      init_map_succ: false,
      point: null,
      folderStatus: true, 
    };
  },

  methods: {
    init_map() {
      for (var i = 0; i < this.data.blh_b.length; i++) {
        this.path.push([parseFloat(this.data.blh_l[i]) / 3.1415926535 * 180, parseFloat(this.data.blh_b[i]) / 3.1415926535 * 180]);
      }
      var temp_point = [];
      for (var i = 0; i < this.point.lat.length; i++) {
        temp_point.push([this.point.lon[i] * 180 / 3.1415926535, this.point.lat[i] * 180 / 3.1415926535]);
      }

      let map = {};
      let data = [];
      this.path.forEach((e) => {
        if (map.hasOwnProperty(e[0])) {
          // return;
        }
        map[e[0]] = e[1];
        data.push(e);
      });

      this.path = {
        type: 1,
        center: data[0],
        data: data,
        zoom: 14,
        all_point_label: temp_point
      };
      this.init_map_succ = true;

    },
    init_echart() {
      const data = this.$store.state.system.Navgiation.data;
      var myChart = echarts.init(this.$refs.enu);
      myChart.setOption(Coordinate_System_enu(data));

      var myChart_enu3d_data = [['time', 'e', 'n', 'u']];
      for (var i = 0; i < data.time.length; i++) {
        var temp = [
          data.time[i],
          data.enu_e[i],
          data.enu_n[i],
          data.enu_u[i]
        ];
        myChart_enu3d_data.push(temp);
      }
      var myChart_enu3d = echarts.init(this.$refs.enu3d);
      myChart_enu3d.setOption(Coordinate_System_3DAnd2D(myChart_enu3d_data));

      var myChart_enu3dSingle = echarts.init(this.$refs.enu3dSingle);
      myChart_enu3dSingle.setOption(Coordinate_System_3D(myChart_enu3d_data));

      var myChart_vel = echarts.init(this.$refs.vel);
      myChart_vel.setOption(Coordinate_System_vel(data));

      var myChart_denu = echarts.init(this.$refs.denu);
      myChart_denu.setOption(Coordinate_System_denu(data));

      var myChart_pdop = echarts.init(this.$refs.pdop);
      myChart_pdop.setOption(Coordinate_System_pdop(data));



      var myChart_vel3d_data = [['time', 'e', 'n', 'u']];
      for (var i = 0; i < this.data.time.length; i++) {
        var temp = [
          data.time[i],
          data.vel_e[i],
          data.vel_n[i],
          data.vel_u[i]
        ];
        myChart_vel3d_data.push(temp);
      }
      var myChart_vel3d = echarts.init(this.$refs.vel3d);
      myChart_vel3d.setOption(Coordinate_System_3DAnd2D(myChart_vel3d_data));
    },
    change_menu(e) {
      this.now_menu_keys = [e.key];
      this.now_menu = e.key;
      this.init_echart();
    },
    initData() {
      this.data = this.$store.state.system.Navgiation.data;
      this.point = this.$store.state.system.Navgiation.point;
      this.init_map();
      this.init_echart();
    },
    changeFolderStatus() {
      this.folderStatus = !this.folderStatus;
    }
  },
  mounted() {
    this.initData();
  },
};
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
  border: none;
}

.draw-content {
  display: flex;
  flex-direction: row;
  width: 100%;
  justify-content: center;
}
</style>