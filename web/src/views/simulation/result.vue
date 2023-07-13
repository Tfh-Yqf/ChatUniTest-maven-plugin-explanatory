<template>
  <div style="height: 90vh;">
    <div style="display: flex;flex-direction: column;width: 100%;height: 90vh">
      <div
        style="display: flex;flex-direction: row;width: 100%;height: 40vh;justify-content: space-around;margin-top: 10px;">
        <div class="group_box">
          <div class="item" id="pitch" ref="pitch"></div>
        </div>

        <div class="group_box">
          <div class="item" id="roll" ref="roll"></div>
        </div>

        <div class="group_box">
          <div class="item" id="heading" ref="heading"></div>
        </div>


      </div>
      <div
        style="display: flex;flex-direction: row;width: 100%;height: 40vh;margin-top: 20px;justify-content: space-around">
        <div class="group_box">
          <div class="item" id="draft" ref="draft"></div>
        </div>

        <div class="group_box">
          <div class="item" id="two" ref="two"></div>
        </div>

        <div class="group_box">
          <div class="item" id="third" ref="third"></div>
        </div>




      </div>
    </div>
  </div>
</template>


<script>
import * as echarts from 'echarts'
import * as echarts_gl from 'echarts-gl'
import * as ecStat from 'echarts-stat';
echarts.registerTransform(ecStat.transform.regression);

import {
  discount, third_chart, scatter,
} from '@/components/Echarts/simulation';

export default {
  name: 'simulation_result',
  components: {

  },

  data() {
    return {
      data: {},
      temp: {
        time: [],
        pitch: [],
        roll: [],
        heading: [],
        draft_time: [],
        draft: []
      },
      position: {
        east: [],
        north: [],
        up: []
      },
      tspd: {}
    }
  },

  methods: {

    init_echart() {

      console.log("data:")
      console.log(this.data);

      var temp = {
        x: this.temp.time,
        y: this.temp.pitch
      };
      var myChart = echarts.init(this.$refs.pitch);
      myChart.setOption(discount(temp, -1.5, 1.5, 'Pitch', '#0072bd', 'Pitch Time 图'));

      temp = {
        x: this.temp.time,
        y: this.temp.roll
      };
      var myChart_roll = echarts.init(this.$refs.roll);
      myChart_roll.setOption(discount(temp, -4, 2, 'Roll /°', '#d95319', 'Roll Time图'));


      temp = {
        x: this.temp.time,
        y: this.temp.heading
      };
      var myChart_heading = echarts.init(this.$refs.heading);
      myChart_heading.setOption(discount(temp, -0, 100, 'Heading /°', '#edb120', 'Heading Time图'));


      temp = {
        x: this.temp.draft_time,
        y: this.temp.draft
      };
      var myChart_draft = echarts.init(this.$refs.draft);
      myChart_draft.setOption(discount(temp, -5.5, -4.5, 'Draft /°', '#0072bd', 'Draft Time图'));

      temp = [];
      for (var i = 0; i < this.position.east.length; i++) {
        temp.push([this.position.east[i], this.position.north[i]])
      }
      var myChart_two = echarts.init(this.$refs.two);
      myChart_two.setOption(scatter({ x: this.position.east, y: this.position.north }, '2维仿真图', this.tspd));

      // var myChart_enu3d_data = [['time','e','n','u']];
      // for(var i=0;i<this.data.time.length;i++){
      //   var temp = [
      //     this.data.time[i],
      //     this.data.enu_e[i],
      //     this.data.enu_n[i],
      //     this.data.enu_u[i]
      //   ];
      //   myChart_enu3d_data.push(temp);
      // }

      temp = [['east', 'north', 'up']];
      for (var i = 0; i < this.position.east.length; i++) {
        temp.push([this.position.east[i].toFixed(3), this.position.north[i], this.position.up[i]])
      }
      var myChart_third = echarts.init(this.$refs.third);
      myChart_third.setOption(third_chart(temp, '3维仿真图', this.tspd));


    },

    initData() {
      this.data = this.$store.state.system.simulation.data;
      this.tspd = this.$store.state.system.simulation.tspd;

      // 点稀释12倍
      var last = this.data.time[0];
      let first_draft_time = 1363596550966n;

      for (var i = 0; i < this.data.time.length; i += 12) {
        this.temp.time.push(((this.data.time[i] - last) / 10000).toFixed(2));
        this.temp.pitch.push(this.data.pitch[i]);
        this.temp.roll.push(this.data.roll[i]);
        this.temp.heading.push(this.data.heading[i]);
        if (i < this.data.draft_time.length) {
          this.temp.draft_time.push(BigInt(this.data.draft_time[i]) - first_draft_time);
          this.temp.draft.push(this.data.draft[i]);
        }

      }

      for (var i = 0; i < this.data.position.east.length; i++) {
        this.position.east.push(this.data.position.east[i]);
        this.position.north.push(this.data.position.north[i]);
        this.position.up.push(this.data.position.up[i]);
      }
      this.data = {};
      console.log(this.data);
      this.init_echart();

    }
  },
  mounted() {
    this.initData();

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
}</style>