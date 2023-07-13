<template>
  <div class="content-container">
    <div class="input-wrapper">
      <div class="title-bar">
          <div class="title-left"></div>
          <div class="title-text">声速场构建</div>
          <img src="@/assets/img/common/data.png" class="sync-data-icon" alt="同步数据" title="同步数据" @click="syncData">
      </div>
      <div class="input-block" v-for="p in navList" :key="p.name">
        <div class="input-title-bar">
          <img src="@/assets/img/left-bar/sound-icon.png" class="input-title-left-icon">
          <div class="input-title-text" @click="changeFolderStatus(p.type, 0)" v-if="$data[p.type] == 1">
            <div class="input-title-name">{{ p.name }}</div>
            <div class="triangle-down"></div>
          </div>
          <div class="input-title-text" @click="changeFolderStatus(p.type, 1)" v-if="$data[p.type] == 0">
            <div class="input-title-name">{{ p.name }}</div>
            <div class="triangle-up">
            </div>
          </div>
        </div>
        <div class="input-content" v-show="$data[p.type] == 1">
          <component :is="p.component"></component>
        </div>
      </div>
    </div>
    <div class="output-wrapper">
      <OutputResult v-if="$store.state.sound.chart" />
    </div>
  </div>
</template>

<script>
import { UpdateServeData } from '@/api/sound'
import "@/assets/css/common.less";
import Space from './space/PageView.vue';
import Time from './time/PageView.vue';
import OutputResult from './output-result/PageView.vue';
export default {
  name: 'SoundStructure',
  components: {
    Space,
    Time,
    OutputResult
  },
  data: () =>{
    return {
      navList: [
        {name: '空间场', type: 'space', component: Space},
        {name: '时间场', type: 'time', component: Time},
      ],
      space: 0,
      time: 0,
    }
  },
  methods: {
    changeFolderStatus(mode, state) {
      this[mode] = state;
    },
    syncData() {
      this.$message.success('更新数据开始');
      var that = this;
      var data = { "folders": "ANModule,loose_combination,TERCOM"};
      UpdateServeData(data).then((res) => {
        if (res.code == 0) {
            that.$message.success('更新数据完成');
        }
      });
    }
  }
}
</script>
<style lang="less" scoped>
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
.sync-data-icon {
    width: 20px;
    height: 20px;
    margin-top: 10px;
    cursor: pointer;
}
</style>
