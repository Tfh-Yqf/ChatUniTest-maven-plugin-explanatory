<template>
  <div style="height: 100%;">
    <div style="width: 100%;position: fixed;top:75px;z-index: 1">
      <a-menu theme="light" mode="horizontal" :selectedKeys="now_menu_keys" @click="change_menu">
        <a-menu-item v-for="(item,index) in menu" :key="item.key">{{item.name}}</a-menu-item>
      </a-menu>
    </div>

    <div style="width: 100%;margin-top: 130px;">
      <a-card title="2维平面图" style="margin-top: 10px;">
        <div v-if="data.type!=4" style="display: flex;flex-direction: row;width: 100%;justify-content: center;" >
          <div id="echart1" :style="{width:screenWidth+'px'}" style="height: 700px;" ref="echart1"></div>
        </div>

        <div v-if="data.type==1||data.type==3||data.type==4||data.type==5" style="width: 100%;text-align: center;font-weight: bold;font-size: 20px" >精度RMSE:<a style="color: red;margin-right: 10px;margin-left: 10px">{{data.rmse.toFixed(4)}}</a>m/s</div>

      </a-card>
    </div>

  </div>
</template>

<script>
import * as echarts from 'echarts';

import {
  sound_2d_01,sound_svm
} from '@/components/Echarts/sound_result';

export default {
  name: 'sound_voice',
  components: {

  },
  data() {
    return {
      menu:[
        {key:1,name:'图表展示'}
      ],
      now_menu_keys:[1],
      now_menu:1,
      data:{},
      screenWidth: document.body.clientWidth -270,
      path:[]
    }
  },

  methods: {
    init_echart_SVM(){
      var temp = {
        test:[]
      }
      for(var i=0;i<this.data.depth.length;i++){
        temp.test.push([this.data.test[i],i*-1]);
      }
      var myChart = echarts.init(this.$refs.echart1);
      myChart.setOption(sound_svm(temp));
    },
    init_echart(){

      console.log("data:")
      console.log(this.data);

      var temp = {
        test:[],
        tar:[]
      }
      for(var i=0;i<this.data.test.length;i++){
        temp.test.push([this.data.test[i],i*-1]);
        temp.tar.push([this.data.tar[i],i*-1]);
      }
      var myChart = echarts.init(this.$refs.echart1);
      myChart.setOption(sound_2d_01(temp));

    },
    change_menu(e){
      console.log(e);
      this.now_menu_keys[0] = e.key;
      this.now_menu = e.key;

      this.init_echart();
    },
    initData(){

      this.data = this.$store.state.system.sound;
      // this.data.rmse = this.data.rmse.toFixed(4);
      if(this.data.type==1||this.data.type==3){
        // BP神经网络和元学习
        this.init_echart();
      }else if(this.data.type!=4&&this.data.type!=5){
        // svm
        this.init_echart_SVM();
      }else if(this.data.type==4){
        // NC反演
      }else if(this.data.type==5){
        // SC反演
        this.data.test = this.data.predicts;
        this.data.tar = this.data.y_true;
        this.init_echart();
      }



    }
  },
  mounted(){
    this.initData();

  },
}
</script>