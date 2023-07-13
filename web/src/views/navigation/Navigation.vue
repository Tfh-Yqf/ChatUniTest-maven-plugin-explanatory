<template>
  <div class="content-container">
    <div class="input-wrapper">
      <div class="title-bar">
          <div class="title-left"></div>
          <div class="title-text">水下导航</div>
          <img src="@/assets/img/common/data.png" class="sync-data-icon" alt="同步数据" title="同步数据" @click="syncData">
      </div>
      <div class="input-block" v-for="p in navList" :key="p.name">
        <div class="input-title-bar">
          <img src="@/assets/img/left-bar/navigation-icon.png" class="input-title-left-icon">
          <div class="input-title-text" @click="changeFolderStatus(p.type, 0)" v-if="$data[p.type] == 1">
            <div class="input-title-name">{{ p.name }}</div>
            <div class="triangle-down"></div>
          </div>
          <div class="input-title-text" @click="changeFolderStatus(p.type, 1)" v-if="$data[p.type] == 0">
            <div class="input-title-name">{{ p.name }}</div>
            <div class="triangle-up"></div>
          </div>
        </div>
        <div class="input-content" v-show="$data[p.type] == 1">
          <component :is="p.component"></component>
        </div>
      </div>
    </div>
    <div class="output-wrapper" v-if="$store.state.navigation.showResult" style="width: 500px;">
      <SingleResult v-if="$store.state.navigation.resultType == 1"></SingleResult>
      <OtherResult v-if="$store.state.navigation.resultType == 2"></OtherResult>
      <GravityResult v-if="$store.state.navigation.resultType == 3"></GravityResult>
    </div>
  </div>
</template>

<script>
import { UpdateServeData } from '@/api/sound'
import "@/assets/css/common.less";
import GravityConfig from './gravity/GravityConfig.vue';
import NavigationConfig from './navigation/NavigationConfig.vue';
import SingleResult from './output-result/SingleResult.vue';
import OtherResult from './output-result/OtherResult.vue';
import GravityResult from './output-result/GravityResult.vue';

export default {
  name: 'Navigation',
  components: {
    GravityConfig,
    NavigationConfig,
    SingleResult,
    OtherResult,
    GravityResult,
  },
  data: () =>{
    return {
      navList: [
        {name: '导航测试', type: 'navigation', component: NavigationConfig},
        {name: '重力匹配', type: 'garvity', component: GravityConfig},
      ],
      navigation: 0,
      garvity: 0,
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
