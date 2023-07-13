<template>
  <div style="display: flex;flex-direction: column;width: 100%">

    <a-divider orientation="left" style="margin-top: 30px">模型选择</a-divider>
    <div style="display: flex;flex-direction: column">
      <div style="display: flex;flex-direction: row;align-items: center;margin-top: 20px">
        <div style="margin-left: 22px;width: 100px">模型名称</div>
        <a-select v-model.number="data.NowModels" placeholder="模型名称" style="width: 60%">
          <a-select-option :value="index" v-for="(item, index) in data.models" :key="index">{{ item.name }}</a-select-option>
        </a-select>
      </div>
    </div>

    <!-- <a-divider orientation="left" style="margin-top: 30px" >反演时刻选择</a-divider>

    <div style="display: flex;flex-direction: column" >
      <div style="display: flex;flex-direction: row;align-items: center;margin-top: 20px">
        <div style="margin-left: 22px;width: 80px">反演时刻</div>
        <a-date-picker v-model="time.date" placeholder="反演日期" valueFormat="YYYY,MM,DD" style="width: 150px;margin-left: 10px" />
        <a-time-picker v-model="time.time" placeholder="反演时间" valueFormat="HH:mm:ss" format="HH:mm:ss" style="margin-left: 10px"></a-time-picker>
      </div>
    </div> -->
  </div>
</template>

<script>
import { SoundTime_Zhichi_InitData } from '@/api/sound';
export default {
  data() {
    return {
      time: {
        date: '',
        time: '',
      },

      data: {
        FanYan: '',
        NowModels: '',
        models: []
      }
    }
  },
  mounted() {
    SoundTime_Zhichi_InitData().then((res) => {
      if (res.code == 0) {
        console.log(res);
        this.data.models = res.data.checkpoints;
      }
    })
  },
  methods: {

  }
}
</script>

<style></style>