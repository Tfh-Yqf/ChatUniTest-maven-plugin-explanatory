<template>
  <div style="height: 90vh;margin-top: 10px;z-index: 1">
    <div style="width: 100%;position: fixed;top:60px;z-index: 3;display: flex;flex-direction: column">
      <div style="background: white;z-index: 3;height: 12px"></div>
      <a-menu theme="light" mode="horizontal" :selectedKeys="now_menu_keys" @click="change_menu">
        <a-menu-item v-for="(item, index) in menu" :key="item.key">{{ item.name }}</a-menu-item>
      </a-menu>

    </div>


    <div style="width: 100%;margin-top: 60px;z-index: 0;" v-show="now_menu == 1">
      <a-card title="Att-3D散点图">
        <div style="display: flex;flex-direction: row;width: 100%;justify-content: center;">
          <div id="att3d" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="att3d"></div>
        </div>
      </a-card>

      <a-card title="Att-2维平面图" style="margin-top: 10px;">

        <div style="display: flex;flex-direction: row;width: 100%;justify-content: center;">
          <div id="att" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="att"></div>
        </div>
      </a-card>
    </div>



    <div style="width: 100%;margin-top: 60px;" v-show="now_menu == 2">
      <a-card title="enu-3D散点图">
        <div style="display: flex;flex-direction: row;width: 100%;justify-content: center;">
          <div id="enu3d" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="enu3d"></div>
        </div>
      </a-card>

      <a-card title="enu-2维平面图" style="margin-top: 10px;">

        <div style="display: flex;flex-direction: row;width: 100%;justify-content: center">
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
      <a-card title="poserr-2维平面图" style="margin-top: 10px;">
        <div style="display: flex;flex-direction: row;width: 100%;justify-content: center">
          <div id="poserr" :style="{ width: screenWidth + 'px' }" style="height: 500px;" ref="poserr"></div>
        </div>

      </a-card>
    </div>





  </div>
</template>


<script>
import * as echarts from 'echarts'
import * as echarts_gl from 'echarts-gl'



import {
  Coordinate_System_enu,
  Coordinate_System_vel,
  Coordinate_System_3DAnd2D,
  Coordinate_System_poserr,
  Coordinate_System_att
} from '@/components/Echarts/navigation_result';

export default {
  name: 'other_result',
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
      screenWidth: document.body.clientWidth - 270,
      path: [],
      init_map_succ: false,
      point: null
    }
  },

  methods: {
    init_echart() {
      // this.$refs.map.add_point_tag();
      console.log("data:")
      console.log(this.data);
      //  姿态（att）
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
      console.log(e);
      this.now_menu_keys[0] = e.key;
      this.now_menu = e.key;

      this.init_echart();
    },
    initData() {

      this.data = this.$store.state.system.Navgiation.data;
      // if(this.data==null||this.data==undefined||this.data.time==undefined||this.data.time==null){
      //   this.$message.error('请先配置导航参数');
      //   return;
      // }
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