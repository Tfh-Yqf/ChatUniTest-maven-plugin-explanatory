<template>
  <div class="panel">
    <a-card title="空间场" :loading="loading">
        <div style="display: flex;flex-direction: row;width: 100%;height: 900px;background-color: #ffffff">
          <div style="display: flex;flex-direction: column;width: 30%;flex-shrink: 0;height: 900px;align-items: center">
            <a-divider orientation="left" style="margin-top: 10px">算法设置</a-divider>
            <div style="display: flex;flex-direction: row;width: 100%;margin-top: 10px;align-items: center">
              <div style="width: 100px;flex-shrink: 0;margin-left: 20px;">算法选取</div>
              <a-select style="width: 60%" v-model.number="nowMenu" @change="change_suanfa">
                <a-select-option :value="index" v-for="(item, index) in menu" :key="index">{{ item }}</a-select-option>
              </a-select>
            </div>

            <Kongjian ref="KongJian" @AddPoints="AddPoints" v-if="nowMenu == 3"></Kongjian>
            <EOF ref="EOF" @AddPoints="AddPoints" v-else-if="nowMenu == 4"></EOF>
            <BPAndMeta ref="BPAndMeta" @AddPoints="AddPoints" v-else></BPAndMeta>

            <div style="margin-top: 32px;display: flex;flex-direction: row;justify-content: space-around">
              <a-popconfirm title="确定执行吗?" @confirm="excute">
                <a-button type="primary" :loading="loading" style="width: 150px;border-radius: 20px;" size="large">执行算法
                </a-button>
              </a-popconfirm>
              <button type="button" class="ant-btn ant-btn-primary ant-btn-lg" @click="updateServeData"
              style="width: 100px;border-radius: 20px;margin-left: 20px;"><span>更新数据</span></button>
            </div>
            </div>

            <div style="width: 70%;border-radius: 10px;height: 700px">
              <piemap @shiquSucc="shiquSucc" :data="mapdata" ref="map"></piemap>
            </div>
        </div>
        <!-- <div style="margin-top: 32px;display: flex;flex-direction: row;justify-content: space-around">
        <div style="display: flex;flex-direction: column;">
          <div style="width: 70%;border-radius: 10px;height: 700px">
            <piemap @shiquSucc="shiquSucc" :data="mapdata" ref="map" id="map"></piemap>
          </div>
        </div>
      </div> -->

    </a-card>
  </div>
</template>

<script>
import {
  sound_api,
  Sound_initdata,
  ExcuteMeta,
  ExcuteBP,
  ExcuteKongJianOrEOF,
  UpdateServeData
} from '@/api/sound'
import { getData } from '../../api/system';

export default {
  name: 'sound_time',
  components: {
    piemap: () => import('@/components/pie_map/map.vue'),
    Kongjian: () => import("@/views/sound/Space_moudles/Kongjian.vue"),
    BPAndMeta: () => import("@/views/sound/Space_moudles/BPAndMeta.vue"),
    EOF: () => import("@/views/sound/Space_moudles/EOF.vue"),
  },
  props: [],
  data() {
    return {
      menu: [ 'BP神经网络', '元学习'],
      nowMenu: 0,
      mapdata: {
        type: 2,
        center: [113.1],
        zoom: 5,
        positions: [],
        lon_positions: [],
        point_positions: null,
        point_lon_positions: [0, 0],
        choose_label: '',
        all_point_label: null,
        circle: null
      },
      loading: false
    }
  },
  methods: {
    AddPoints(e) {
      this.mapdata.point_lon_positions = e.pointList[e.Nowpoint];
      this.change_mapdata_targetpoint();
      var temp = [];
      for (var i = 0; i < e.pointList.length; i++)
        if (i != e.Nowpoint) {
          temp.push({ point: e.pointList[i], index: i });

        }



      this.$refs.map.DrawPoints(temp);
    },
    shiquSucc(e) {
      this.plot_point.target = e;
      this.change_menu({ key: 2 })
      this.change_target_point();
      this.$message.success('拾取点' + e + '成功！')
    },
    change_menu(e) {
      console.log(e);
      this.now_menu_keys = [e.key];
    },
    init() {
      this.loading = true;
      Sound_initdata().then((res) => {
        if (res.code == 0) {
          this.loading = false;
          this.init_data = res;
          this.$message.success(res.message);
        }
      }).finally((res) => {
        this.loading = false;

      })
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
    change_target_point() {
      // 刷新点
      this.plot_point.cankao = [];
      for (var i = 0; i < this.mapdata.all_point_label.length; i++) {
        if (i + 1 != this.plot_point.target)
          this.plot_point.cankao.push((i + 1).toString());
      }

    },
    change_mapdata_targetpoint() {
      this.tarpoint_have_change = false;
      this.$refs.map.change_target_point();
      this.$message.success('修改目标位置成功');
    },
    change_mapdata() {
      this.map_have_change = false;
      this.$refs.map.change_plane();
      this.$message.success('修改空间范围成功');
    },
    excute() {

      // if (this.nowMenu == 0) {
      //   // 声速场 执行 空间插值
      //   var temp = {
      //     ssp_path: this.$refs.KongJian.file.svp[this.$refs.KongJian.data.svp].path,
      //     ssploc_path: this.$refs.KongJian.file.position[this.$refs.KongJian.data.position].path,
      //     test_idx: this.$refs.KongJian.data.Nowpoint,
      //     mode: 'SITP'
      //   }
      //   console.log(temp);

      //   ExcuteKongJianOrEOF(temp).then((res)=>{
      //     if(res.code!=0||res.code==undefined){
      //       this.$message.error(res.message||'错误！');
      //     }else{

      //       this.$message.success('执行成功！');
      //       this.$store.commit('setSound', res.data);
      //       this.$router.push('/sound/result');
      //     }
      //     this.loading = false;
      //   })


      // } else if (this.nowMenu == 1) {
      //   // 声速场 执行 EOF
      //   var temp = {
      //     ssp_path: this.$refs.EOF.file.svp[this.$refs.EOF.data.svp].path,
      //     ssploc_path: this.$refs.EOF.file.position[this.$refs.EOF.data.position].path,
      //     test_idx: this.$refs.EOF.data.Nowpoint,
      //     son_path: this.$refs.EOF.file.sonar[this.$refs.EOF.data.sonar].path,
      //     mode: 'EOF',
      //     eof_odr: 5,
      //     dpt_win: 20,
      //     space: 5,
      //     num_recv: 50,
      //     num_pso_ptc: 30,
      //     num_itr: 30,
      //     loc_rcv: [0.566809114324481, 2.20203379056793, 1.33614949168378, "在新版本中写死了"],
      //     dpt_org: 3400
      //   }
      //   console.log(temp);
      //   ExcuteKongJianOrEOF(temp).then((res) => {
      //     if (res.code != 0 || res.code == undefined) {
      //       this.$message.error(res.message || '错误！');
      //     } else {
      //       this.$message.success('执行成功！');
      //       this.$store.commit('setSound', res.data);
      //       this.$router.push('/sound/result');
      //     }
      //     this.loading = false;
      //   })
      // } else 
      if (this.nowMenu == 0) {
        // BP神经网络
        var data = { index: this.$refs.BPAndMeta.data.Nowpoint };
        ExcuteBP(data).then((res) => {
          if (res.code == 0) {

            this.$store.commit('setSound', res.data);
            this.$router.push('/sound/result');
          }
        });
      } else if (this.nowMenu == 1) {
        // 元学习
        var data = { index: this.$refs.BPAndMeta.data.Nowpoint }

        ExcuteMeta(data).then((res) => {
          if (res.code == 0) {

            this.$store.commit('setSound', res.data);
            this.$router.push('/sound/result');
          }
        })
      }




    },
    change_suanfa(e) {

      if (e == 'Algorithm02' || e == 'Algorithm01') {
        this.show_sonar = false
      }
      else {
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

  },
  onUnmounted() {

  }
}
</script>

<style></style>