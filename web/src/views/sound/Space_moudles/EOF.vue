<template>
  <div style="display: flex;flex-direction: column;width: 100%">

    <a-divider orientation="left" style="margin-top: 30px" >数据读取</a-divider>

    <div style="display: flex;align-items: center" >
      <div style="width: 100px;flex-shrink: 0;margin-left: 20px;">温盐深数据</div>
      <a-select v-model.number="data.svp"  placeholder="选择声速剖面数据" style="width: 60%"  >
        <a-select-option :value="index" v-for="(item,index) in file.svp" :key="index">{{item.name}}</a-select-option>
      </a-select>
    </div>

    <div style="display: flex;align-items: center;margin-top: 20px" >
      <div style="width: 100px;flex-shrink: 0;margin-left: 20px;">点位置数据</div>
      <a-select v-model.number="data.position"  placeholder="选择点位置数据" style="width: 60%"  @change="change_file_data()">
        <a-select-option :value="index" v-for="(item,index) in file.position" :key="index">{{item.name}}</a-select-option>
      </a-select>
    </div>

    <div style="display: flex;align-items: center;margin-top: 20px" >
      <div style="width: 100px;flex-shrink: 0;margin-left: 20px;">通信数据</div>
      <a-select v-model.number="data.sonar"  placeholder="选择通信数据" style="width: 60%"  >
        <a-select-option :value="index" v-for="(item,index) in file.sonar" :key="index">{{item.name}}</a-select-option>
      </a-select>
    </div>


    <a-divider orientation="left" style="margin-top: 30px" >反演位置选择</a-divider>
    <div style="display: flex;flex-direction: column" >
      <div style="display: flex;flex-direction: row;align-items: center;margin-top: 20px">
        <div style="margin-left: 22px;width: 100px">目标点</div>
        <a-select v-model.number="data.Nowpoint"  placeholder="目标点" style="width: 60%"  @change="draw_point">
          <a-select-option  :value="index" v-for="(item,index) in data.pointList" :key="index">点{{index+1}}</a-select-option>
        </a-select>
      </div>

      <div style="display: flex;flex-direction: row;align-items: center;margin-top: 20px" v-if="data.pointList.length!=0">
        <div style="margin-left: 20px;width: 100px;flex-shrink: 0">目标点{{data.Nowpoint+1}}位置</div>
        <a-input-number :disabled="true"  :min="-180" :max="180" :step="0.5" v-model="data.pointList[data.Nowpoint][0]" style=""></a-input-number>
        <a-input-number :disabled="true"  :min="-180" :max="180" :step="0.5" v-model="data.pointList[data.Nowpoint][0]" style="margin-left: 10px"></a-input-number>
        <div style="margin-left: 10px">经纬度</div>
      </div>

      <div style="display: flex;flex-direction: column" v-for="(item,index) in data.pointList" :key="index">
        <div style="display: flex;flex-direction: row;align-items: center;margin-top: 20px" v-if="index!=data.Nowpoint">
          <div style="margin-left: 20px;width: 100px;flex-shrink: 0">参考点{{index+1}}位置</div>
          <a-input-number :disabled="true" :min="-180" :max="180" :step="0.5" v-model="item[0]" style=""></a-input-number>
          <a-input-number :disabled="true" :min="-180" :max="180" :step="0.5" v-model="item[1]" style="margin-left: 10px"></a-input-number>
          <div style="margin-left: 10px">经纬度</div>
        </div>
      </div>

    </div>


  </div>
</template>

<script>
import {Sound_initdata,SoundSpace_getPosition} from '@/api/sound';
export default {
  data(){
    return{
      file:{
        svp:[],
        position:[],
        sonar:[]
      },
      data:{
        svp:'',
        position:'',
        pointList:[],
        Nowpoint:'',
        sonar:''
      }
    }
  },
  mounted() {
    Sound_initdata().then((res)=>{
      if(res.code==0){
        console.log(res);
        this.file= res.data;
      }
    })
  },
  methods:{
    change_file_data(){
      console.log(this.file.position[this.data.position])
      SoundSpace_getPosition(this.file.position[this.data.position]).then((res)=>{
        if(res.code==0){
          this.data.pointList = res.data;
          this.data.Nowpoint = 0;
          this.draw_point();
        }
      })
    },
    draw_point(){
      var temp = {
        pointList:this.data.pointList,
        Nowpoint:this.data.Nowpoint
      };
      this.$emit('AddPoints',temp);

    }
  }
}
</script>

<style>

</style>