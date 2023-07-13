<template>
  <div class="panel">
    <a-card title="时间场" :loading="loading">

      <div style="display: flex;flex-direction: column;">

        <div style="display: flex;flex-direction: row;width: 100%;height: 900px;background-color: #ffffff">
          <div style="display: flex;flex-direction: column;width: 30%;flex-shrink: 0;height: 900px;align-items: center">
            <a-divider orientation="left" style="margin-top: 10px">算法设置</a-divider>
            <div style="display: flex;flex-direction: row;width: 100%;margin-top: 10px;align-items: center">
              <div style="width: 100px;flex-shrink: 0;margin-left: 20px;">算法选取</div>
              <a-select style="width: 60%" v-model.number="nowMenu" @change="change_suanfa">
                <a-select-option :value="index" v-for="(item, index) in menu" :key="index">{{ item }}</a-select-option>
              </a-select>
            </div>

            <DuoXiangShi ref="DuoXiangShi" v-if="nowMenu == 0 || nowMenu == 1"></DuoXiangShi>
            <BPAndRBF @change_plane="change_plane" :plane="mapdata.lon_positions" ref="BPAndRBF" v-else
              @ChangePoint="change_mapdata_targetpoint"></BPAndRBF>
            <ZhiChi ref="ZhiChi" v-if="nowMenu == 1"></ZhiChi>

            <div style="margin-top: 32px;display: flex;flex-direction: row;justify-content: space-around">
              <a-popconfirm title="确定执行吗?" @confirm="excute">
                <a-button type="primary" :loading="loading" style="width: 150px;border-radius: 20px;" size="large">执行算法
                </a-button>
                <button type="button" class="ant-btn ant-btn-primary ant-btn-lg" @click="updateServeData"
              style="width: 100px;border-radius: 20px;margin-left: 20px;"><span>更新数据</span></button>
              </a-popconfirm>


            </div>

          </div>

          <div style="width: 70%;border-radius: 10px;height: 700px">
            <piemap @shiquSucc="shiquSucc" :data="mapdata" ref="map"></piemap>
          </div>
        </div>


      </div>

      <!--      <a-divider orientation="left" style="margin-top: 40px">参数设置</a-divider>-->

      <!--      <a-row :gutter="8" style="margin-top: 8px;">-->
      <!--        <a-col :span="12">-->
      <!--          <config :items="config_time_space" title="时间场参数设置"/>-->
      <!--        </a-col>-->
      <!--      </a-row>-->
    </a-card>
  </div>
</template>

<script>
import { Sound_GetFileDetail, ExcuteDuoXiang, ExcuteSVM, ExcuteNCFanYan, ExcuteSCFanYan, UpdateServeData } from '@/api/sound'

function timeToSeconds(timeString) {
  console.log(timeString)
  var timeParts = timeString.split("-");
  var hours = parseInt(timeParts[0]);
  var minutes = parseInt(timeParts[1]);
  var seconds = parseInt(timeParts[2]);
  console.log(hours)
  console.log(minutes)
  console.log(seconds)
  return hours * 3600 + minutes * 60 + seconds;
}
export default {
  name: 'sound_time',
  components: {
    // Earth : ()=> import ('@/components/pie_map/Earth.vue'),
    // Config: () => import("../../components/Config/Config.vue"),
    piemap: () => import('@/components/pie_map/map.vue'),
    DuoXiangShi: () => import("@/views/sound/Time_module/DuoXiangShi.vue"),
    ZhiChi: () => import("@/views/sound/Time_module/ZhiChi.vue"),
    BPAndRBF: () => import("@/views/sound/Time_module/BPAndRBF.vue")
  },
  props: [],
  data() {
    return {
      loading: false,
      menu: ['多项式拟合', '支持向量机'],
      nowMenu: 0,
      mapdata: {
        type: 2,
        center: [113.1],
        zoom: 5,
        positions: [],
        lon_positions: [],
        point_positions: null,
        point_lon_positions: [0, 0],
      },
    };
  },
  methods: {
    change_plane() {
      this.$refs.map.change_plane();
    },
    delete_point(item) {
      // 删除点
      var temp = JSON.parse(JSON.stringify(this.plot_point.cankao));
      this.plot_point.cankao = [];
      for (var i = 0; i < temp.length; i++) {
        if (temp[i] != item)
          this.plot_point.cankao.push(temp[i]);
      }
    },
    change_target_point(e) {
      // 刷新点
      this.plot_point.cankao = e;
      for (var i = 0; i < this.mapdata.all_point_label.length; i++) {
        if (i + 1 != this.plot_point.target)
          this.plot_point.cankao.push((i + 1).toString());
      }

    },
    change_menu(e) {
      console.log(e);
      this.now_menu_keys = [e.key];
    },
    change_mapdata_targetpoint(e) {
      console.log(e);
      this.mapdata.point_lon_positions = e;
      this.$refs.map.change_target_point();
      this.$message.success('修改目标位置成功');
    },
    change_tarpoint_have_change() {
      this.tarpoint_have_change = true;
    },
    shiquSucc(e) {
      this.plot_point.target = e;
      this.change_menu({ key: 2 })
      this.change_target_point();
      this.$message.success('拾取点' + e + '成功！')
    },
    load_position(e) {
      this.have_load_position = true;

      // this.$refs.map.add_guiji();
      this.$refs.map.add_point_tag();

      this.$message.success('绘制点位置成功');
      this.change_target_point();
      this.position_disabled = true;
    },
    change_file_data(e) {
      var temp = {
        type: e,
        FileName: ''
      }
      if (e == 'position') temp.FileName = this.data.position;
      else temp.FileName = this.data.svp;

      var that = this;
      Sound_GetFileDetail(temp).then((res) => {
        if (e == 'position') {
          this.mapdata.circle = res.data.circle;
          this.mapdata.all_point_label = res.data.point;
        } else {
          that.svp_chart = res.data;

        }
        that.$message.success('加载成功');
      });
    },
    init() {

    },
    change_time_kuadu(e) {
      this.time.range_now = '';
      this.time.time_kuadu = e;
      this.time.range = [];
      switch (e) {
        case 0:
          for (var i = 1990; i < 2030; i++) {
            this.time.range.push({ name: i + '年', value: i });
          }
          break;
        case 1:
          for (var i = 1; i <= 12; i++) {
            this.time.range.push({ name: i + '月', value: i });
          }
          break;
        case 2:
          for (var i = 1; i <= 31; i++) {
            this.time.range.push({ name: i + '日', value: i });
          }
          break;
      }
    },
    excute(e) {
      if (this.nowMenu == 0) {
        // 多项式拟合
        var temp = {
          ssp_path: this.$refs.DuoXiangShi.files.smlp[this.$refs.DuoXiangShi.data.smlp].path,
          time_path: this.$refs.DuoXiangShi.files.time[this.$refs.DuoXiangShi.data.time].path,
          test_idx: this.$refs.DuoXiangShi.data.FanYan,
          mode: 'PF'
        }
        console.log("dxs")
        console.log(temp)
        this.loading = true;
        ExcuteDuoXiang(temp).then((res) => {
          if (res.code == 0) {
            if (res.code != 0 || res.code == undefined) {
              this.$message.error(res.message || '错误！');
            } else {
              this.$message.success('执行成功！');
              this.$store.commit('setSound', res.data);
              this.$router.push('/sound/result');
            }
            this.loading = false;
          }
        });
      } else if (this.nowMenu == 1) {
        // 支持向量机
        console.log('zcxlj')
        var temp = {
          time: timeToSeconds(this.$refs.DuoXiangShi.time_range[this.$refs.DuoXiangShi.data.FanYan].time),
          ssp_path: this.$refs.DuoXiangShi.files.smlp[this.$refs.DuoXiangShi.data.smlp].path,
          time_path: this.$refs.DuoXiangShi.files.time[this.$refs.DuoXiangShi.data.time].path,
          modeal_path: this.$refs.ZhiChi.data.models[this.$refs.ZhiChi.data.NowModels].name
        }
        // console.log(this.$refs.DuoXiangShi.time.data)
        // console.log(this.$refs.DuoXiangShi.time.time)
        // console.log(this.$refs.DuoXiangShi.fanyan_timeList)
        // console.log(this.$refs.DuoXiangShi.files)
        console.log("BBBBBBBBBBBBB")
        console.log(this.$refs.DuoXiangShi.time_range[this.$refs.DuoXiangShi.data.FanYan])
        console.log(this.$refs.DuoXiangShi.data.FanYan)
        
        console.log()
        console.log(temp);
        this.loading = true;
        ExcuteSVM(temp).then((res) => {
          if (res.code == 0) {
            if (res.code != 0 || res.code == undefined) {
              this.$message.error(res.message || '错误！');
            } else {
              console.log("fanhuizhi")
              console.log(res.data)
              this.$message.success('执行成功！');
              this.$store.commit('setSound', res.data);
              this.$router.push('/sound/result');
            }
            this.loading = false;
          }
        });
      } else {
        // BP OR RBF
        var temp = {
          test: '在这里传参'
        };
        this.loading = true;
        if (this.$refs.BPAndRBF.UseNC == true) {
          // 使用NC
          ExcuteNCFanYan(temp).then((res) => {
            if (res.code != 0 || res.code == undefined) {
              this.$message.error(res.message || '错误！');
            } else {
              this.$message.success('执行成功！');
              this.$store.commit('setSound', res.data);
              this.$router.push('/sound/result');
            }
            this.loading = false;
          })
        } else {
          ExcuteSCFanYan(temp).then((res) => {
            if (res.code != 0 || res.code == undefined) {
              this.$message.error(res.message || '错误！');
            } else {
              this.$message.success('执行成功！');
              this.$store.commit('setSound', res.data);
              this.$router.push('/sound/result');
            }
            this.loading = false;
          })
        }


      }


    },
    file_upload_templature(e) {
      this.data.file.templature = e.file.response;
    },
    file_upload_config(e) {
      this.data.file.config = e.file.response;
    },
    file_upload_sonar(e) {
      this.data.file.sonar = e.file.response;
    },
    change_suanfa(e) {

      if (e == 'Algorithm02' || e == 'Algorithm01') {
        this.show_sonar = false
      } else {
        this.show_sonar = true
      }

    },
    updateServeData() {
      this.$message.success('开始更新数据！');
      var that = this;
      var data = { "folders": "svm,ssp_module,ssp_python,meta_learning"};
      UpdateServeData(data).then((res) => {
        if (res.code == 0) {
          that.$message.success('更新数据完成');
        }
      });
    }

  },
  mounted() {
    this.init();
    this.change_time_kuadu(0);
  },
}
</script>

<style></style>