<template>
  <div class="output-result" v-if="$store.state.sound.resultShow">
    <div class="output-item">
      <div class="title" @click="changeFolderStatus">声速场构建RMSE图</div>
      <ChartComponent v-if="folderStatus" />
    </div>
  </div>
</template>

<script>
import ChartComponent from '../components/ChartComponent.vue';
export default {
  name: 'OutputResult',
  components: {
    ChartComponent
  },
  data() {
    return {
      folderStatus: true
    }
  },
  methods: {
    changeFolderStatus() {
      this.folderStatus = !this.folderStatus;
    }
  }
};
</script>
<style lang="less" scoped>
.output-result {
  width: 100%;
  height: 100%;
  padding: 24px;

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
}
</style>
