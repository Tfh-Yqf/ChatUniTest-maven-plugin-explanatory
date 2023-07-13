<template>
  <div class="system-error">
    <div class="error-item">
      <div class="label">东</div>
      <div class="table-item">
        <input v-model="$store.state.data.simulation.acoposerr[0]" class="item-input">
      </div>
    </div>
    <div class="error-item">
      <div class="label">北</div>
      <div class="table-item">
        <input v-model="$store.state.data.simulation.acoposerr[1]" class="item-input">
      </div>
    </div>
    <div class="error-item">
      <div class="label">天</div>
      <div class="table-item">
        <input v-model="$store.state.data.simulation.acoposerr[2]" class="item-input">
      </div>
    </div>
    <div class="error-item">
      <div class="label">硬件延迟</div>
      <div class="table-item">
        <input v-model="$store.state.data.simulation.acodelerr" class="item-input">
      </div>
    </div>
    <div class="error-item">
      <div class="label">随机误差</div>
      <div class="table-item">
        <input v-model="$store.state.data.simulation.acoranerr" class="item-input">
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'SystemError',
};
</script>

<style scoped lang="less">
.system-error{
  margin-top: 4px;
}
.error-item {
  display: flex;
  align-items: center;

  .label {
    font-size: 12px;
    font-family: Source Han Sans CN-Regular, Source Han Sans CN;
    font-weight: 400;
    color: #99B3C8;
    line-height: 20px;
    margin-right: 8px;
    width: 72px;
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
