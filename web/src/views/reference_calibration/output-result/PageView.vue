<template>
  <div class="output-result">
    <div class="title" @click="changeFolderStatus">基准点标定图</div>
    <div class="result-content">
      <a-table :columns="columns" :dataSource="data_source" class="lk-table" v-if="folderStatus"/>
      <piemap v-if="init_map_succ" :data="path" ref="map"></piemap>
    </div>
  </div>
</template>

<script>

export default {
  name: 'OutputResult',
  components: {
    piemap: () => import ('@/components/pie_map/map.vue')
  },
  data() {
    return {
      data: null,
      path: [],
      init_map_succ: false,
      columns: [],
      data_source: [{lat: 38}],
      loading: false,
      folderStatus: true,
    }
  },

  methods: {
    initMap() {
      var all_point = [];
      for (var i = 0; i < this.data.lon.length; i++) {
        var temp = [this.data.lat[i], this.data.lon[i]];
        all_point.push(temp);
      }
      this.data.all_point = all_point;
      this.path = {
        type: 3,
        center: all_point[0],
        data: this.data,
        zoom: 17
      };

      this.init_map_succ = true;


    },
    initData() {

      this.data = this.$store.state.system.demarcate;
      if (this.data.wangpingcha == false) {
        this.columns = [
          {
            title: '点',
            dataIndex: 'name',
          }, {
            title: '经度',
            dataIndex: 'lat',
          }, {
            title: '纬度',
            dataIndex: 'lon',
          }
          , {
            title: '高程',
            dataIndex: 'depth',
          }
          , {
            title: '精度',
            dataIndex: 'accuracy',
          }
          , {
            title: '解算耗时',
            dataIndex: 'time',
          }];
        // 局部坐标系
        if (this.data.jubu) {
          var temp = {
            title: '东',
            dataIndex: 'dong',
          };
          this.columns.push({...temp});
          temp = {
            title: '北',
            dataIndex: 'bei',
          };
          this.columns.push({...temp});
          temp = {
            title: '天',
            dataIndex: 'tian',
          };
          this.columns.push({...temp});

        }
      } else {
        this.columns = [
          {
            title: '标号',
            dataIndex: 'dian',
          }, {
            title: '经度',
            dataIndex: 'lat',
          }, {
            title: '纬度',
            dataIndex: 'lon',
          }
          , {
            title: '高程',
            dataIndex: 'depth',
          }
          , {
            title: '精度',
            dataIndex: 'accuracy',
          }];
      }
      this.initMap();

      if(this.data.wangpingcha==false){
        if (this.data.jubu) {
          for (var i = 0; i < this.data.lat.length; i++) {
            var temp = {
              accuracy: this.data.accuracy[i],
              lat: this.data.lat[i],
              lon: this.data.lon[i],
              depth: this.data.depth[i],
              name: '点' + (i + 1).toString(),
              time: this.data.time[i],
              dong: this.data.dong[i],
              bei: this.data.bei[i],
              tian: this.data.tian[i]
            };
            this.data_source.push(temp);
          }
        } else {
          for (var i = 0; i < this.data.lat.length; i++) {
            var temp = {
              accuracy: this.data.accuracy[i],
              lat: this.data.lat[i],
              lon: this.data.lon[i],
              depth: this.data.depth[i],
              name: '点' + (i + 1).toString(),
              time: this.data.time[i]
            };
            this.data_source.push(temp);
          }
        }
      }else{
        for (var i = 0; i < this.data.lat.length; i++) {
          var temp = {
            accuracy: this.data.accuracy[i],
            lat: this.data.lat[i],
            lon: this.data.lon[i],
            depth: this.data.depth[i],
            dian: this.data.dian[i],
          };
          this.data_source.push(temp);
        }
      }
    },
    changeFolderStatus() {
      this.folderStatus = !this.folderStatus;
    }
  },
  mounted() {
    this.initData();

  },
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
  .title-bar {
    padding: 0 0 0 8px;
    width: 100%;
    height: 24px;
    align-items: center;
    background: linear-gradient(90deg, rgba(8, 83, 106, 0.79) 0%, rgba(13, 78, 99, 0.03) 100%);

    .title-name {
      font-size: 14px;
      line-height: 24px;
      height: 24px;
      color: #99B3C8;
      font-weight: 500;
      margin-right: 10px;
    }
  }
  .down-file-containor {
    width: 100%;
    margin-top: 10px;
    color: #B0D8F5;
  }
  .file-content {
    width: 30%;
    height: 30px;
    line-height: 30px;
  }
  .file-name {
    color: #B0D8F5;
    cursor: pointer;
    text-decoration: none;
  }
  .file-result {
    display: flex;
    flex-direction: row;
  }
}
:deep(.lk-card){
  color: #99B3C8;
  border-color: rgba(47, 136, 181, 0.16);
  background-color: rgba(47, 136, 181, 0.16);
  .ant-card-head{
    color: #99B3C8;
    border-color: rgba(47, 136, 181, 0.16);
  }
}
:deep(.lk-table){
  
  width: 100%;
  
  .ant-table-thead,.ant-table-tbody{
    color: #99B3C8;
    th{
      background-color: rgba(47, 136, 181, 0.3);
      color: #99B3C8;
      border-color: rgba(47, 136, 181, 0.16);
    }
  }
  .ant-table-tbody{
    .ant-table-row{
      background-color: rgba(47, 136, 181, 0.18);
    }
  }
  .ant-table-pagination{
    li{
      color: #99B3C8;
      border-color: rgba(47, 136, 181, 0.16);
      background-color: rgba(47, 136, 181, 0.16);
      a{
        color: #99B3C8;
        border-color: rgba(47, 136, 181, 0.16);
        background-color: rgba(47, 136, 181, 0.16);
      }
    }
  }
}
.result-content{
  padding-top: 16px;
  width: 700px;
  position: absolute;
  left: -253px;
}
</style>
