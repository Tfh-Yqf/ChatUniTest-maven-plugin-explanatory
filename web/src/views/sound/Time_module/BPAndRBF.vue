<template>
  <div style="display: flex;flex-direction: column;width: 100%">

    <a-divider orientation="left" style="margin-top: 30px" >模型选择</a-divider>
    <div style="display: flex;flex-direction: column" >
      <div style="display: flex;flex-direction: row;align-items: center;margin-top: 20px">
        <div style="margin-left: 22px;width: 100px">模型名称</div>
        <a-select v-model.number="data.NowModels"  placeholder="模型名称" style="width: 60%"  >
          <a-select-option  :value="index" v-for="(item,index) in data.models" :key="index">{{item}}</a-select-option>
        </a-select>
      </div>
    </div>

    <a-divider orientation="left" style="margin-top: 30px" >反演时刻选择</a-divider>
    <div style="display: flex;flex-direction: row;align-items: center;margin-top: 10px;">
      <div style="width: 100px;flex-shrink: 0;margin-left: 20px;">方案</div>
      <a-radio :checked="UseNC" @change="change_UseNC">NC文件</a-radio>
      <a-radio :checked="!UseNC" @change="change_UseNC">实测文件</a-radio>
    </div>
    <div v-if="UseNC"  style="display: flex;flex-direction: column;">
      <div style="display: flex;align-items: center;margin-top: 20px" >
        <div style="width: 100px;flex-shrink: 0;margin-left: 20px;">盐度NC文件</div>

        <a-select v-model.number="data.NowNC"  placeholder="选择盐度数据" style="width: 60%"  @change="change_file_data()">
          <a-select-option :value="index" v-for="(item,index) in data.NC.ss" :key="index">{{item.name}}</a-select-option>
        </a-select>
      </div>

      <div style="display: flex;align-items: center;margin-top: 20px" >
        <div style="width: 100px;flex-shrink: 0;margin-left: 20px;">温度NC文件</div>

        <a-select v-model.number="data.NowNC"  placeholder="选择温度数据" style="width: 60%"  @change="change_file_data()">
          <a-select-option :value="index" v-for="(item,index) in data.NC.tt" :key="index">{{item.name}}</a-select-option>
        </a-select>
      </div>

      <div style="display: flex;flex-direction: row;align-items: center;margin-top: 20px">
        <div style="margin-left: 20px;width: 100px;flex-shrink: 0">点位置</div>
        <a-input-number @change="change_tarpoint_have_change"  :min="-180" :max="180" :step="0.5" v-model="point.point_lon_positions[0]" style=""></a-input-number>
        <a-input-number @change="change_tarpoint_have_change"  :min="-180" :max="180" :step="0.5" v-model="point.point_lon_positions[1]" style="margin-left: 10px"></a-input-number>
        <div style="margin-left: 10px">经纬度</div>
      </div>

      <div style="display: flex;flex-direction: row;align-items: center;margin-top: 20px">
        <div style="margin-left: 20px;width: 100px;flex-shrink: 0">目标时间</div>
        <a-date-picker v-model="data.TargetDate" placeholder="目标时间" valueFormat="YYYY-MM" style="width: 60%;" :disabled="true"/>
      </div>

      <div style="display: flex;flex-direction: row;align-items: center;margin-top: 20px">
        <div style="margin-left: 20px;width: 100px;flex-shrink: 0">空间范围</div>
        <div style="display: flex;flex-direction: column">
          <div v-for="(it,ind) in plane" :key="ind" style="display: flex;margin-top: 10px;align-items: center">
            <a-input-number @change="change_plane"  :min="-180" :max="180" :step="0.5" v-model="it[0]" style=""></a-input-number>
            <div style="margin-left: 5px">经度</div>
            <a-input-number @change="change_plane"  :min="-180" :max="180" :step="0.5" v-model="it[1]" style="margin-left: 10px"></a-input-number>
            <div style="margin-left: 5px">维度</div>
          </div>
        </div>

      </div>
    </div>
    



    <div v-if="!UseNC"  style="display: flex;flex-direction: column" >
      <div style="display: flex;align-items: center;margin-top: 20px" >
        <div style="width: 100px;flex-shrink: 0;margin-left: 20px;">实测文件</div>

        <a-select v-model.number="data.Nowdata"  placeholder="选择实测数据" style="width: 60%"  >
          <a-select-option :value="index" v-for="(item,index) in data.NC.data" :key="index">{{item.name}}</a-select-option>
        </a-select>
      </div>

      <div style="display: flex;flex-direction: row;align-items: center;margin-top: 20px">
        <div style="margin-left: 20px;width: 100px;flex-shrink: 0">点位置</div>
        <a-input-number @change="change_tarpoint_have_change"  :min="-180" :max="180" :step="0.5" v-model="point.point_lon_positions[0]" style=""></a-input-number>
        <a-input-number @change="change_tarpoint_have_change"  :min="-180" :max="180" :step="0.5" v-model="point.point_lon_positions[1]" style="margin-left: 10px"></a-input-number>
        <div style="margin-left: 10px">经纬度</div>
      </div>

      <div style="display: flex;flex-direction: row;align-items: center;margin-top: 20px">
        <div style="margin-left: 20px;width: 100px">反演时刻</div>
        <a-date-picker v-model="time.date" placeholder="反演日期" valueFormat="YYYY,MM,DD" style="width: 150px;" />
        <a-time-picker v-model="time.time" placeholder="反演时间" valueFormat="HH:mm:ss" format="HH:mm" style="margin-left: 10px"></a-time-picker>
      </div>
    </div>
  </div>
</template>

<script>
import {SoundTime_BPAndRBP_InitData} from '@/api/sound';
export default {
  props:["plane"],
  data(){
    return{
      UseNC:true,
      time:{
        date:'',
        time:'',
      },
      point:{
        point_lon_positions:[110,34]
      },
      data:{
        Nowdata:0,
        FanYan:'',
        NowModels:'',
        models:[],
        NC:{
          ss:[],
          tt:[],
          data:[]
        },
        NowNC:'',
        TargetDate:''
      },

    }
  },
  mounted() {
    SoundTime_BPAndRBP_InitData().then((res)=>{
      if(res.code==0){
        console.log(res);
        this.data.models = res.data.models;
        this.data.NC = res.data.nc;
        this.data.NowModels = 0;
        this.data.NowNC = 0;
        this.change_file_data();
      }
    })
  },
  methods:{
    change_plane(){
      this.$emit('change_plane');
    },
    change_UseNC() {
      this.UseNC = !this.UseNC;
    },
    change_tarpoint_have_change(){
      this.$emit('ChangePoint',this.point.point_lon_positions)
    },
    change_file_data(){
      this.data.TargetDate = this.data.NC.ss[this.data.NowNC].name;
      this.data.TargetDate = this.data.TargetDate.substring(3,this.data.TargetDate.length-3);
    }
  }
}
</script>

<style>

</style>