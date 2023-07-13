<template>
  <div >
    <div  v-if="data.type=='string'" class="group">

        <div style="width: 150px;flex-shrink: 0">{{data.name}}:</div>
        <a-input v-model="data.config" style="margin-left: 20px;width: 60%" />
    </div>
    <div  v-else-if="data.type=='number'" class="group">
      <div style="width: 150px;flex-shrink: 0">{{data.name}}:</div>
      <a-input-number step="0.5" defaultValue="10" v-model="data.config" style="margin-left: 20px;width: 60%"></a-input-number>
    </div>
    <div  v-else-if="data.type=='list'" class="group">
      <div style="width: 150px;flex-shrink: 0">{{data.name}}:</div>
      <div style="display: flex;flex-direction: column;width: 60%;margin-left: 20px;">
        <div  v-for="(item,index) in data.config" :key="index" style="">
          <div :class="{'gap':index!=0}" v-if="index==data.config.length-1" style="display: flex;justify-content: space-around;margin-left: -5px;">
            <a-input  v-model="data.config[index]" style="width: 80%"></a-input>
            <a-button @click="add_list(index)" type="primary" style="width: 15%;display: flex;align-items: center;justify-content: center;">添加</a-button>
          </div>
          <a-input :class="{'gap':index!=0}" v-else  v-model="data.config[index]" style="width: 100%"></a-input>

        </div>
      </div>
    </div>
    <div v-else-if="data.type=='time'" class="group">
      <div style="width: 150px;flex-shrink: 0">{{data.name}}</div>
      <a-date-picker v-model="data.config.date" placeholder="开始日期" valueFormat="YYYY-MM-DD" style="width: 150px;margin-left: 20px" />
      <a-time-picker v-model="data.config.time" placeholder="开始时间" format="HH:mm:ss" style="margin-left: 10px"></a-time-picker>
    </div>
    <div v-else-if="data.type=='location'" class="group">
      <div style="width: 150px;flex-shrink: 0">{{data.name}}</div>
      <a-input-number   :min="-180" :max="180" v-model="data.config[0]" style="margin-left: 20px"></a-input-number>
      <a-input-number   :min="-180" :max="180" v-model="data.config[1]" style="margin-left: 10px"></a-input-number>
      <div style="margin-left: 10px">经纬度</div>
    </div>
    <div v-else-if="data.type=='ThreeInput'" class="group">
      <div style="width: 150px;flex-shrink: 0">{{data.name}}</div>
      <a-input-number   :min="-180" :max="180" v-model="data.config[0]" style="margin-left: 20px"></a-input-number>

      <a-input-number   :min="-180" :max="180" v-model="data.config[1]" style="margin-left: 10px"></a-input-number>

      <a-input-number   :min="-180" :max="180" v-model="data.config[2]" style="margin-left: 10px"></a-input-number>

    </div>
  </div>
</template>

<script>

import moment from 'moment'

export default {
  name: 'ConfigNoisy',
  props:['data'],
  data() {
    return {

    };
  },
  methods: {
    add_list(e){
      if(this.data.config[e]==null||this.data.config[e]==""){
        this.$message.error('内容不能为空');
        return;
      }

      this.data.config.push(null);
    }

  },
  mounted() {
    if(this.data.type=='list'&&this.data.config.length==0){
      this.data.config.push(null);
    }
  },
}
</script>

<style>
  .group{
    display: flex;
    flex-direction: row;
    align-items: center;
    font-size: 14px;
    font-weight: 600;
    margin-top: 10px;
  }

  .gap{
    margin-top: 10px;
  }
</style>