<template>
  <div style="display: flex;flex-direction: column;width: 100%">
    <a-divider orientation="left" style="margin-top: 30px">数据读取</a-divider>

    <div style="display: flex;align-items: center">
      <div style="width: 100px;flex-shrink: 0;margin-left: 20px;">温盐深数据</div>

      <a-select v-model.number="data.smlp" placeholder="选择声速剖面数据" style="width: 60%">
        <a-select-option :value="index" v-for="(item, index) in files.smlp" :key="index">{{ item.name }}</a-select-option>
      </a-select>
    </div>

    <div style="display: flex;align-items: center;margin-top: 20px;">
      <div style="width: 100px;flex-shrink: 0;margin-left: 20px;">点时空数据</div>

      <a-select v-model.number="data.time" placeholder="选择点位置数据" style="width: 60%" @change="Change_Ssp_Time()">
        <a-select-option :value="index" v-for="(item, index) in files.time" :key="index">{{ item.name }}</a-select-option>
      </a-select>
    </div>



    <a-divider orientation="left" style="margin-top: 30px">反演时间选择</a-divider>

    <div style="display: flex;flex-direction: column">
      <div style="display: flex;flex-direction: row;align-items: center;margin-top: 20px">
        <div style="margin-left: 22px;width: 100px">反演时刻</div>
        <a-select v-model.number="data.FanYan" placeholder="反演时刻" style="width: 60%" @change="$forceUpdate()">
          <a-select-option :value="index" v-for="(item, index) in fanyan_timeList" :key="index">{{ item
          }}</a-select-option>
        </a-select>
      </div>
    </div>

    <!-- <a-divider orientation="left" style="margin-top: 30px">参考时间选择</a-divider>

    <div style="display: flex;flex-direction: column">
      <div v-for="(item, index) in time_range" :key="index">
        <div style="display: flex;flex-direction: row;align-items: center;margin-top: 20px" v-if="index != data.FanYan">
          <div style="margin-left: 22px;width: 80px">时刻</div>
          <a-date-picker v-model="item.date" placeholder="开始日期" valueFormat="YYYY-MM-DD" :disabled="true"
            style="width: 150px;margin-left: 10px" />
          <a-time-picker v-model="item.date" placeholder="开始时间" valueFormat="HH:mm:00" :disabled="true" format="HH:mm:00"
            style="margin-left: 10px"></a-time-picker>
        </div>

      </div>
    </div> -->

  </div>
</template>

<script>
import { SoundTime_DuoXisngShi_Initdata, SoundTime_DuoXisngShi_SspTime } from '@/api/sound';
export default {
  data() {
    return {
      files: null,
      fanyan_timeList: [],
      data: {
        smlp: '',  // 声速剖面数据
        time: '',
        models: [],
      },
      time_range: [],
    }
  },
  mounted() {
    SoundTime_DuoXisngShi_Initdata().then((res) => {
      if (res.code == 0) {
        console.log("res")
        console.log(res.data);
        this.files = res.data;
      }
    });
  },
  methods: {
    Change_Ssp_Time() {
      // console.log(this.data.FanYan)
      console.log(this.data.time)
      SoundTime_DuoXisngShi_SspTime(this.files.time[this.data.time]).then((res) => {
        console.log(res);
        if (res.code == 0) {
          this.fanyan_timeList = res.data;
          this.data.FanYan = 0;
          for (var i = 0; i < this.fanyan_timeList.length; i++) {
            var temp = {
              date: this.fanyan_timeList[i].substring(0, 8),
              time: this.fanyan_timeList[i].substring(8, this.fanyan_timeList[i].length)
            }
            this.fanyan_timeList[i]=temp.date+"-"+temp.time
            this.time_range.push(temp);
          }
        }
      })
    }
  }
}
</script>

<style></style>