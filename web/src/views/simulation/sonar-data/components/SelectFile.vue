<template>
  <div class="select-file">
    <div class="select-file-item">
      <span class="label">
        声速剖面数据
      </span>
      <div class="item-input">
        <a-select class="table-item-select" v-model="positionData" placeholder="请先上传声速剖面数据！" @change="changeSvpfile" v-model.number="data.svp" >
          <a-select-option :value="index" v-for="(item, index) in initdatas.svpfile" :key="index">{{item.name}}
          </a-select-option>        
        </a-select>
      </div>
    </div>
    <div class="select-file-item">
      <div class="label">声速剖面层深</div>
      <div class="table-item">
        <input v-model="$store.state.data.simulation.svpdh" class="item-input">
      </div>
    </div>
    <div class="select-file-item">
      <div class="label">采样频率</div>
      <div class="table-item">
        <input v-model="$store.state.data.simulation.samper" class="item-input">
      </div>
    </div>
    <div class="select-file-item">
      <div class="label">杆臂值</div>
      <div class="table-item">
        <input v-model="$store.state.data.simulation.insacolever[0]" class="item-input insacolever">
      </div>
      <div class="table-item">
        <input v-model="$store.state.data.simulation.insacolever[1]" class="item-input insacolever">
      </div>
      <div class="table-item">
        <input v-model="$store.state.data.simulation.insacolever[2]" class="item-input insacolever">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SystemError',
  props: ['fileList'],
  data() {
    return this.$store.state.data.simulation;
  },
  methods: {
    changeSvpfile(){
      var temp = {...this.$store.state.system.SimulationConfig};
      temp.svpfile = this.initdatas.svpfile[this.data.svp].path;
      // console.log(temp)
      this.$store.commit('setSimulationConfig',temp);

      console.log(this.$store.state.system.SimulationConfig);
    },
  },
};
</script>

<style scoped lang="less">
.select-file{
  margin-top: 8px;
}
.select-file-item {
  display: flex;
  align-items: center;

  .label {
    width: 72px;
    font-size: 12px;
    font-family: Source Han Sans CN-Regular, Source Han Sans CN;
    font-weight: 400;
    color: #99B3C8;
    line-height: 20px;
    margin-right: 8px;
  }

  .item-input {
    height: 20px;
    width: 170px;
    border-radius: 0px;
    border: none;
    background: rgba(47, 136, 181, 0.16);
    color: #99B3C8;
    font-size: 12px;
    :deep(.ant-select-selection--single) {
      width: 170px;
      height: 20px;
      border-radius: 0px;
      background-color: rgba(47, 136, 181, 0.16);
      border: none;
    }
    :deep(.ant-select-selection__rendered) {
      line-height: 20px;
    }
    :deep(.ant-select-selection-selected-value){
      color: #99B3C8;
      font-size: 12px;
    }
  }
  .insacolever{
    width: 50px;
    margin-right: 10px;
    :deep(.ant-select-selection--single) {
      width: 50px;
    }
  }
}
</style>
