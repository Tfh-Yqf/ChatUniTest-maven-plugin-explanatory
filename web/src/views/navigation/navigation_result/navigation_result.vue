<template>
  <div style="height: 90vh;margin-top: 10px;z-index: 1">
    <div style="width: 100%;position: fixed;top:60px;z-index: 3;display: flex;flex-direction: column">
      <div style="background: white;z-index: 3;height: 12px"></div>
      <a-menu theme="light" mode="horizontal" :selectedKeys="now_menu_keys" @click="change_menu">
        <a-menu-item v-for="(item, index) in menu" :key="item.key">{{ item.name }}</a-menu-item>
      </a-menu>

    </div>


    <div style="width: 100%;margin-top: 60px;z-index: 0;" v-show="now_menu == 2">
      <a-card title="ENU-3D散点图">
        <div style="display: flex;flex-direction: row;width: 100%;justify-content: center;">
          <div id="enu3d" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="enu3d"></div>
        </div>
      </a-card>

      <a-card title="ENU-2维平面图" style="margin-top: 10px;">

        <div style="display: flex;flex-direction: row;width: 100%;justify-content: center;">
          <div id="enu" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="enu"></div>
        </div>
      </a-card>
    </div>

    <div style="width: 100%;margin-top: 60px;" v-show="now_menu == 3">
      <a-card title="VEL-3D散点图">
        <div style="display: flex;flex-direction: row;width: 100%;justify-content: center;">
          <div id="vel3d" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="vel3d"></div>
        </div>
      </a-card>

      <a-card title="VEl-2维平面图" style="margin-top: 10px;">

        <div style="display: flex;flex-direction: row;width: 100%;justify-content: center">
          <div id="vel" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="vel"></div>
        </div>
      </a-card>
    </div>

    <div style="width: 100%;margin-top: 60px;" v-show="now_menu == 4">
      <a-card title="DENU-2维平面图" style="margin-top: 10px;">

        <div style="display: flex;flex-direction: row;width: 100%;justify-content: center">
          <div id="denu" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="denu"></div>
        </div>

        <div
          style="width: 100%;display: flex;justify-content: center;height: 30px;padding: 5px;font-size: 20px;font-weight: bold">
          RMS2D:{{ data.rms }} M</div>
      </a-card>
    </div>

    <div style="width: 100%;margin-top: 60px;" v-show="now_menu == 5">
      <a-card title="pdop-2维平面图" style="margin-top: 10px;">

        <div style="display: flex;flex-direction: row;width: 100%;justify-content: center">
          <div id="pdop" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="pdop"></div>
        </div>

      </a-card>
    </div>


    <div style="width: 100%;height: 500px;margin-top: 60px;" v-show="now_menu == 1">
      <div style="width: 100%;display: flex;flex-direction: row-reverse">
        <div
          style="margin-top: 160px;margin-right: 40px;width: 300px;height: 300px;justify-content: center;background-color: #FFFFFF;position: absolute;z-index: 2;opacity: 0.9;border-radius: 10px">
          <div id="enu3dSingle" style="height: 100%;width: 100%" ref="enu3dSingle"></div>
        </div>
      </div>

      <a-card title="导航轨迹图" style="margin-top: 60px;z-index: 1">
        <piemap :data="path" v-if="init_map_succ" ref="map"></piemap>
      </a-card>


    </div>


  </div>
</template>


<script>
import * as echarts from 'echarts'
import * as echarts_gl from 'echarts-gl'
// import piemap from '@/components/pie_map/map.vue'


import {
  Coordinate_System_enu,
  Coordinate_System_vel,
  Coordinate_System_denu,
  Coordinate_System_3DAnd2D,
  Coordinate_System_3D,
  Coordinate_System_pdop
} from '@/components/Echarts/navigation_result';

export default {
  name: 'navigation_result',
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
      screenWidth: document.body.clientWidth - 270,
      path: [],
      init_map_succ: false,
      point: null
    }
  },

  methods: {
    init_map() {
      console.log("init_map.map.vue")
      console.log(this.data)
      for (var i = 0; i < this.data.blh_b.length; i++) {
        this.path.push([parseFloat(this.data.blh_l[i]) / 3.1415926535 * 180, parseFloat(this.data.blh_b[i]) / 3.1415926535 * 180]);
      }
      var temp_point = [];
      for (var i = 0; i < this.point.lat.length; i++) {
        temp_point.push([this.point.lon[i] * 180 / 3.1415926535, this.point.lat[i] * 180 / 3.1415926535]);
      }
      console.log('基准点坐标');
      console.log(temp_point);

      console.log('导航结果路径');
      console.log(this.path);

      let map = {};
      let data = [];
      this.path.forEach((e) => {
        if (map.hasOwnProperty(e[0])) {
          // return;
        }
        map[e[0]] = e[1];
        data.push(e);
      })
      // 数据采样策略
      // for(let i=0;i<this.path.length;i++){
      //   data.push(this.path[i])
      // }
      console.log("数据")
      console.log(data);

      this.path = {
        type: 1,
        center: data[0],
        data: data,
        zoom: 14,
        all_point_label: temp_point
      };
      this.init_map_succ = true;

      // this.$refs.map.init_navgiation(this.path);
    },
    init_echart() {
      // this.$refs.map.add_point_tag();
      console.log("data:")
      console.log(this.data);

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

      var myChart_enu3dSingle = echarts.init(this.$refs.enu3dSingle);
      myChart_enu3dSingle.setOption(Coordinate_System_3D(myChart_enu3d_data));

      var myChart_vel = echarts.init(this.$refs.vel);
      myChart_vel.setOption(Coordinate_System_vel(this.data));

      var myChart_denu = echarts.init(this.$refs.denu);
      myChart_denu.setOption(Coordinate_System_denu(this.data));

      var myChart_pdop = echarts.init(this.$refs.pdop);
      myChart_pdop.setOption(Coordinate_System_pdop(this.data));



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
    },
    change_menu(e) {
      console.log(e);
      this.now_menu_keys[0] = e.key;
      this.now_menu = e.key;

      this.init_echart();
    },
    initData() {

      this.data = this.$store.state.system.Navgiation.data;
      this.point = this.$store.state.system.Navgiation.point;
      // if(this.data==null||this.data==undefined||this.data.time==undefined||this.data.time==null){
      //   this.$message.error('请先配置导航参数');
      //   return;
      // }
      this.init_map();
      // console.log('result页面传参：');
      console.log(this.data);
      this.init_echart();

    }
  },
  mounted() {
    this.initData();

  },
}
</script>