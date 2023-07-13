<template>
  <div style="height: 90vh;">


    <div style="width: 100%;height: 500px;margin-top: 60px;">

      <a-card title="基准点标定图" style="margin-top: 10px;">
        <div style="width: 100%;display: flex;flex-direction: row-reverse" v-if="loading">
          <div
              style="margin-top: 20px;margin-right: 20px;justify-content: center;position: absolute;z-index: 2;border-radius: 10px;background-color: #FFFFFF;">
            <a-table :columns="columns" :dataSource="data_source">

            </a-table>
          </div>
        </div>
        <piemap v-if="init_map_succ" :data="path" ref="map"></piemap>


      </a-card>
    </div>


  </div>
</template>


<script>


export default {
  name: 'demarcate_result',
  components: {
    piemap: () => import ('@/components/pie_map/map.vue')
  },

  data() {
    return {
      data: null,
      path: [],
      init_map_succ: false,
      columns: [],
      data_source: [],
      loading: false
    }
  },

  methods: {
    init_map() {
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
      console.log(this.data);
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
      this.loading = true;

      this.init_map();
      // console.log('result页面传参：');
      console.log(this.data);

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




    }
  },
  mounted() {
    this.initData();

  },
}
</script>