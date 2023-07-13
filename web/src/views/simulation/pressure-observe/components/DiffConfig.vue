<template>
  <div class="diff-config">
    <div class="diff-config-item ">
      <div class="label">压力计比例因子误差</div>
      <div class="table-item">
        <input v-model="$store.state.simulation.pressureObserve.pgsf" class="item-input">
      </div>
    </div>
    <div class="diff-config-item">
      <div class="label">压力计随机误差</div>
      <div class="table-item">
        <input v-model="$store.state.simulation.pressureObserve.pgranerr" class="item-input">
      </div>
    </div>
    <div class="tab-pane">
      <div class="table-title">
        <div class="table-item-left"></div>
        <div class="table-item">E</div>
        <div class="table-item">N</div>
        <div class="table-item">U</div>
      </div>

      <div class="table-row">
        <div class="table-item-left">压力计位置转换参数</div>
        <div class="table-item">
          <input v-model="$store.state.simulation.pressureObserve.inspglever[0]" class="table-item-input">
        </div>
        <div class="table-item">
          <input v-model="$store.state.simulation.pressureObserve.inspglever[1]" class="table-item-input">
        </div>
        <div class="table-item">
          <input v-model="$store.state.simulation.pressureObserve.inspglever[2]" class="table-item-input">
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'DiffConfig',
};
</script>

<style scoped lang="less">
.diff-config {
  margin-top: 4px;
}

.tab-pane {

  .table-row,
  .table-title {
    display: flex;
    column-gap: 4px;
    font-size: 12px;
    font-family: Source Han Sans CN-Regular, Source Han Sans CN;
    font-weight: 400;
    color: #99B3C8;
    line-height: 20px;
    margin-right: 8px;

    .table-item-left {
      min-width: 160px;
      font-size: 12px;
      font-family: Source Han Sans CN-Regular, Source Han Sans CN;
      font-weight: 400;
      color: #99B3C8;
      line-height: 20px;
    }

    .table-item {
      width: 85px;
      text-align: center;

      .table-item-input {
        height: 20px;
        width: 85px;
        border-radius: 0px;
        border: none;
        background: rgba(47, 136, 181, 0.16);
        color: #99B3C8;
        font-size: 12px;
      }
    }
  }
  .table-row{
    margin-bottom: 4px;
  }
}
.diff-config-item {
  display: flex;
  align-items: center;
  column-gap: 4px;

  .label {
    min-width: 160px;
    font-size: 12px;
    font-family: Source Han Sans CN-Regular, Source Han Sans CN;
    font-weight: 400;
    color: #99B3C8;
    line-height: 20px;
  }

  .item-input {
    height: 20px;
    width: 170px;
    border-radius: 0px;
    border: none;
    background: rgba(47, 136, 181, 0.16);
    color: #99B3C8;
    font-size: 12px;
  }
}
</style>
