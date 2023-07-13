<template>
  <div class="table-content">
    <div class="tab-pane fade show active" role="tabpanel" aria-labelledby="home-tab" tabindex="0">
      <div class="table-title">
        <div class="table_path">序号</div>
        <div class="table_path">东坐标/m</div>
        <div class="table_path">北坐标/m</div>
        <div class="table_path">天坐标/m</div>
      </div>

      <div class="table-row" v-for="(item, index) in $store.state.data.simulation.datasource_path" :key="item.id">
        <div class="table-item">{{ item.id }}</div>
        <div class="table-item">
          <input v-model="item.east" defaultValue="0" class="table-item-input">
        </div>
        <div class="table-item">
          <input v-model="item.north" defaultValue="0" class="table-item-input">
        </div>
        <div class="table-item">
          <input v-model="item.sky" defaultValue="0" class="table-item-input">
        </div>
      </div>

      <div class="tail-row">
        <div @click="addDatasourcePath()" class="confirm-botton">添加行</div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'Table',
  data() {
    return {
      path_template: { id: 1, east: 0, north: 0, sky: 0 },
    };
  },
  methods: {
    initData() {
      this.addDatasourcePath();
    },
    addDatasourcePath() {
      var temp = JSON.parse(JSON.stringify(this.path_template));
      temp.id = this.$store.state.data.simulation.datasource_path.length + 1;
      // this.$store.state.data.simulation.datasource_path.push(temp);
      // this.$store.commit('setDatasourcePath', temp);
    },
  },
  mounted() {
    this.initData();
  }
};
</script>

<style scoped lang="less">
.table-content {
  font-size: 12px;
  font-family: Source Han Sans CN-Regular, Source Han Sans CN;
  font-weight: 400;
  color: #99B3C8;
  line-height: 20px;

  .table-title {
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 45px;
    .table_path {
      width: 76px;
      height: 45px;
      align-items: center;
      display: flex;
      justify-content: center;
    }
  }

  .table-row {
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 20px;
    margin-bottom: 5px;

    .table-item {
      text-align: center;
      width: 20%;
      height: 20px;
    }

    .table-item-input {
      height: 20px;
      width: 50%;
      border-radius: 0px 0px 0px 0px;
      opacity: 1;
      border: none;
      background: rgba(47, 136, 181, 0.16);
    }
  }

  .tail-row {
    width: 95%;
    margin-left: 5px;
    margin-top: 10px;
    height: 26px;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: center;

    .confirm-botton {
      width: 80px;
      height: 26px;
      line-height: 26px;
      color: #F5FBFF;
      text-align: center;
      background: linear-gradient(180deg, rgba(12, 53, 111, 0) 0%, rgba(25, 236, 255, 0.63) 100%);
      opacity: 1;
      border: 1px solid #19ECFF;
    }
  }
}
</style>
