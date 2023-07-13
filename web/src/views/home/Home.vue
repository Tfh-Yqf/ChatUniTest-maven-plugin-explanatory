<template>
  <div class="fullscreen-wrapper">
    <!-- <div class="left">
      <ul class="nav flex-column left-nav">
        <li class="nav-item content0" @click="jump_config()">
          <svg viewBox="0 0 24 24" preserveAspectRatio="xMidYMid meet" focusable="false" class="nav-svg">
            <g viewBox="0 0 24 24">
              <path d="M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z"></path>
            </g>
          </svg>
          <div class="tip0 tip" style="display:none">参数配置</div>
        </li>
        <li class="nav-item content1" @click="Add_GravityPlane(1)">
          <svg viewBox="0 0 24 24" preserveAspectRatio="xMidYMid meet" focusable="false" class="nav-svg">
            <g viewBox="0 0 24 24">
              <path
                d="M20.49 19l-5.73-5.73C15.53 12.2 16 10.91 16 9.5A6.5 6.5 0 109.5 16c1.41 0 2.7-.47 3.77-1.24L19 20.49 20.49 19zM5 9.5C5 7.01 7.01 5 9.5 5S14 7.01 14 9.5 11.99 14 9.5 14 5 11.99 5 9.5z">
              </path>
            </g>
          </svg>
          <div class="tip1 tip" style="display:none">数据仿真</div>
        </li>
        <li class="nav-item content2" @click="Add_GravityPlane()">
          <svg viewBox="0 0 24 24" preserveAspectRatio="xMidYMid meet" focusable="false" class="nav-svg">
            <g viewBox="0 0 24 24">
              <path
                d="M19.578 10.985c-.22-2.142-1.428-4.06-2.965-5.3L17.93 3.77l.11-.114c.22-.564 0-1.24-.548-1.466-.55-.225-1.21 0-1.428.564l-.99 2.142C14.088 4.333 13.1 4 12 4c-1.098 0-2.087.333-2.965.784l-.99-2.143s0-.11-.11-.11c-.328-.56-.987-.67-1.536-.34-.55.34-.66 1.02-.33 1.58l1.31 1.92c-1.65 1.24-2.75 3.16-2.97 5.3L2 11c-.66 0-1.093.436-.983 1.113.11.676.324 1 .983.887l2.532-.098c.22 2.142 1.427 4.06 2.965 5.3L6.18 20.118l-.11.113c-.22.57-.12 1.36.43 1.58.55.23 1.326.12 1.546-.45l.99-2.37C9.912 19.67 10.9 20 12 20c1.098 0 2.087-.333 2.965-.784l.99 2.143s0 .11.11.11c.328.56.987.67 1.536.34.55-.34.66-1.02.33-1.58l-1.31-1.92c1.65-1.24 2.75-3.16 2.97-5.3L22 13c.66 0 1.093-.436.983-1.113 0-.564-.324-.887-.983-.887l-2.422-.015zm-2.196.226l-2.197.12c-.11-.79-.55-1.46-1.098-1.91l1.208-1.92c1.208 1.016 1.867 2.256 2.087 3.72zM12 6.37c.77 0 1.428.113 2.087.45l-.99 2.03c-.327-.118-.767-.23-1.097-.23-.33 0-.77.112-1.098.225l-.99-2.03c.66-.338 1.32-.45 2.088-.45zM8.595 7.61l1.208 1.8c-.55.45-.988 1.126-1.098 1.915l-2.197-.112c.33-1.465.99-2.705 2.087-3.607zm-1.977 5.18l2.197-.113c.11.79.55 1.465 1.098 1.916L8.705 16.51c-1.208-1.015-1.867-2.255-2.087-3.72zM12 17.637c-.77 0-1.428-.113-2.087-.45l.99-2.03c.328.112.658.225 1.097.225.33 0 .77-.113 1.098-.226l.99 2.03c-.66.338-1.32.45-2.088.45zm2.197-3.157c.55-.45.988-1.127 1.098-1.916l2.197.113c-.33 1.578-.99 2.818-2.087 3.72l-1.208-1.916z">
              </path>
            </g>
          </svg>
          <div class="tip2 tip" style="display:none">重力匹配</div>
        </li>
        <li class="nav-item content3" @click="Add_GravityPlane(3)">
          <svg viewBox="0 0 24 24" preserveAspectRatio="xMidYMid meet" focusable="false" class="nav-svg">
            <g viewBox="0 0 24 24">
              <path d="M19 3H5a2 2 0 0 0-2 2v14c0 1.1.9 2 2 2h14a2 2 0 0 0 2-2V5a2 2 0 0 0-2-2zm0 16H5V5h14v14z"></path>
              <circle cx="7.5" cy="16.5" r="1.5"></circle>
              <circle cx="7.5" cy="7.5" r="1.5"></circle>
              <circle cx="12" cy="12" r="1.5"></circle>
              <circle cx="16.5" cy="16.5" r="1.5"></circle>
              <circle cx="16.5" cy="7.5" r="1.5"></circle>
            </g>
          </svg>
          <div class="tip3 tip" style="display:none">基准点标定</div>
        </li> 
        <li class="nav-item content4" @click="change_NowChoose(4)">
          <svg viewBox="0 0 24 24" preserveAspectRatio="xMidYMid meet" focusable="false" class="nav-svg">
            <g viewBox="0 0 24 24">
              <path
                d="M5.8 14.34c.37.52.8 1.04 1.25 1.56L5.5 17 12 21.56 18.51 17l-1.56-1.1c.46-.52.88-1.04 1.25-1.56L22 17l-10 7-10-7 3.8-2.66zM12 1c3.27 0 7 2.46 7 7.15 0 3.12-2.33 6.57-7 10.35-4.67-3.78-7-7.23-7-10.35A6.97 6.97 0 0 1 12 1zm5 7.15C17 5.05 14.7 3 12 3S7 5.06 7 8.15c0 2.13 1.62 4.74 5 7.73 3.38-2.99 5-5.6 5-7.73zm-5 1.6a1.75 1.75 0 1 1 0-3.5 1.75 1.75 0 0 1 0 3.5z">
              </path>
            </g>
          </svg>
          <div class="tip4 tip" style="display:none">水下导航</div>
        </li>


      </ul>
      <div style="height: 60px;flex-shrink: 0;width: 100%;display: flex;justify-content: center">
        <img @click="toggle" :src="full" style="width: 35px;height: 35px">
      </div>


    </div> -->

    <!-- <div class="extent-box">
      <div class="scroll-view">

        <JiZhun v-if="NowChoose == 3"></JiZhun>

        <Solution @hide_extent_box="hide_extent_box" @ShowGuiJi="ShowGuiJi" v-else-if="NowChoose == 4"
          @FinalExcute="Excute_Navigation" @drawGuiji="drawGuiji" ref="solution" @change_guiji_color="change_guiji_color">
        </Solution>
      </div>

    </div> -->

    <div class="map">
      <Earth ref="Earth" @DrawGuijiSuccess="DrawGuijiSuccess" @ShiQuCity="ShiQuCity" @change_point_position="change_point_position"></Earth>
      <Simulation v-if="!$store.state.system.fullScreen && $props.currentNav === '数据仿真'"/>
      <SoundStructure v-if="!$store.state.system.fullScreen && $props.currentNav === '声速场构建'"/>
      <ReferenceCalibration v-if="!$store.state.system.fullScreen && $props.currentNav === '基准点标定'" />
      <Navigation v-if="!$store.state.system.fullScreen && $props.currentNav === '水下导航'" />
    </div>


    <!-- <div v-if="card_show == 1" class="card">
      <img :src="cardData.img" style="width: 100%;height: 200px;border-radius: 7px;">
      <div class="NavigationPng" @click="changeCamera">
        <img :src="NavigationPng" style="width: 30px;height: 30px;">
      </div>

      <div style="font-size: 21px;font-weight: normal;color: white;padding: 20px;">{{ cardData.name }}</div>

      <div style="font-size: 15px;font-weight: normal;color: white;padding: 20px 20px 20px 20px;">
        {{ cardData.content }}<br>
        <a style="color: #8ab4f8;">了解详情</a>
      </div>

      <div
        style="font-size: 15px;font-weight: normal;color: #8ab4f8;position: absolute;bottom: 20px;border: 1px solid #5f6368;padding: 8px;border-radius: 5px;left: 20px;display: flex">

        <svg viewBox="0 0 24 24" preserveAspectRatio="xMidYMid meet" focusable="false"
          style="pointer-events: none; display: block; width: 20px; height: 20px;fill: #8ab4f8;">
          <g viewBox="0 0 24 24">
            <path
              d="M20 1v3h3v2h-3v3h-2V6h-3V4h3V1h2zm-8 12c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm1-9.94v2.02A6.53 6.53 0 0 0 12 5c-3.35 0-6 2.57-6 6.2 0 2.34 1.95 5.44 6 9.14 4.05-3.7 6-6.79 6-9.14V11h2v.2c0 3.32-2.67 7.25-8 11.8-5.33-4.55-8-8.48-8-11.8a7.95 7.95 0 0 1 9-8.14z">
            </path>
          </g>
        </svg>

        <a style="margin-left: 10px;" @click="changeCamera">添加到项目</a>
      </div>

      <img @click="closeCard" :src="close"
        style="width: 20px;height: 20px;position: absolute;right: 10px;bottom: 30px;" />
    </div>

    <div v-else-if="card_show == 2" class="card" style="background-color: white;">
      <div style="width: 100%;text-align: center;padding: 15px;">
        <a style="font-size: 20px;font-weight: bold;">导航方案</a>

        <img @click="closeCard" :src="close" class="card-close" />
      </div>
      <a-Collapse :activeKey="activeKey">
        <a-collapse-panel v-for="(item, index) in gui_detail_card_data" :key="index"
          :header="'路线' + (index + 1) + ' ' + item.name">
          <div style="color: white;background-color: white;width: 100%;">
            <a-table :dataSource="item.table" :columns="guiji_table_columns" />
          </div>

          <div style="width: 100%;text-align: center;" @click="Start_Navigation(index)">
            <a-button type="primary">导航模拟</a-button>
          </div>


        </a-collapse-panel>
      </a-Collapse>
    </div>

    <div v-else-if="card_show == 3" class="circle_card">
      <div class="circle">
        <div class="point"></div>
      </div>
    </div>

    <div class="zhonglishipeiqu zhonglishipeiqu-block" style="display: none;"> </div>
    <div class="zhonglishipeiqu zhonglishipeiqu-wenzi" style="display: none;">重力场适配区</div>

    <div class="cardBottom" v-show="card_show == 3">
      <div id="DyamicEchart" ref="DyamicEchart" style="width: 380px;height: 380px;margin-left: 10px;margin-top: 20px;">
      </div>
    </div> -->
  </div>
</template>

<script>
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle";
import $ from 'jquery';
import {
  Dyamic_Echart, updateDyamicData
} from '@/components/Echarts/Dyamic.js';
import * as echarts from 'echarts';
import { GravityT4 } from '@/api/Navgiation';

export default {
  props: {
    currentNav: {
      type: String,
      default: '数据仿真'
    }
  },
  components: {
    Earth: () => import('@/components/pie_map/Earth.vue'),
    Simulation: () => import('@/views/simulation/Simulation.vue'),
    SoundStructure: () => import('@/views/sound/PageView'),
    ReferenceCalibration: () => import('@/views/reference_calibration/PageView.vue'),
    Navigation: () => import('@/views/navigation/Navigation.vue'),

    // Solution: () => import('@/views/home/Solution.vue'),
    // JiZhun: () => import('@/views/reference_calibration/config.vue')
  },
  data() {
    return {
      NavigationPng: require('@/assets/NavigationPng.png'),
      close: require('@/assets/close.png'),
      screenWidth: document.body.clientWidth - 270,
      NowChoose: 1,
      is_SoundSpace: true,
      target_menu: [
        { key: 1, name: '空间场' },
        { key: 2, name: '时间场' }
      ],
      now_menu_keys: [1],
      full: require('@/assets/fullscreen.png'),
      cardData: {
        img: 'https://img3.yun300.cn/repository/image/4fe6bbc1-c299-41dc-ab5b-23f71e8cf7e2.png_640xaf.png?tenantId=221581&viewType=1',
        name: '花莲港',
        content: '花莲市位于台湾花莲县东部北段，花东纵谷北侧的开口处，为花莲县县治所在地以及县内唯一的县辖市。',
        position: [121.3752, 23.5910],
      },
      card_show: -1,
      gui_detail_card_data: null,
      guiji_table_columns: [
        {
          title: '点',
          dataIndex: 'index',
          key: 'index'
        },
        {
          title: '经度',
          dataIndex: 'latitude',
          key: 'latitude'
        },
        {
          title: '维度',
          dataIndex: 'longitude',
          key: ' longitude'
        },
      ],
      activeKey: [],
      DyamicChart: null,
      showGravity: false
    };
  },
  methods: {
    DrawGuijiSuccess(e) {
      this.$refs.solution.DrawGuijiSuccess(e);
    },
    drawGuiji(e) {
      this.$refs.Earth.drawGuiji(e);
    },
    change_guiji_color(params) {
      this.$refs.Earth.change_guiji_color(params);
    },
    // 添加重力匹配图层
    Add_GravityPlane() {
      if (!this.showGravity) {

        GravityT4().then((res) => {
          if (res.code == 0) {
            this.showGravity = true;
            // 设置T4图
            var T4 = res.data;
            var result = [];

            for (var i = 0; i < T4.length; i++) {
              var temp = { coordinates: [T4[i][2], T4[i][0], T4[i][3], T4[i][1]], color: T4[i][4], origin: T4[i] };
              result.push(temp);
            }

            this.$refs.Earth.addT4Plane(result);

            $('.zhonglishipeiqu').css({
              "display": "inline-block"
            });
          }
        });
      } else {
        this.$refs.Earth.removeGravityEntity();
        this.showGravity = false;
      }

    },
    // 通过子组件Earth.vue,修改当先point的css
    change_point_position(left, top) {
      $(".point").css({
        "top": left + "px",
        "left": top + "px"
      });
    },

    hide_extent_box() {
      $(".extent-box").hide();
    },
    Show_Dyamic_Echart() {
      this.DyamicChart = echarts.init(this.$refs.DyamicEchart);
      this.DyamicChart.setOption(Dyamic_Echart());

      this.updateData();
    },
    updateData() {
      // 模拟数据
      const data = Math.random() * 100;

      // 更新 Echarts 图表数据
      this.DyamicChart.setOption(updateDyamicData());

      // 每隔一段时间更新一次数据
      setTimeout(this.updateData, 100);
    },
    Start_Navigation(index) {
      // 靶子Card
      this.card_show = 3;

      // 展示动态轨迹曲线图
      this.Show_Dyamic_Echart();

      let initial_data = this.gui_detail_card_data[index].table;
      let data = [];
      for (let i = 1; i < initial_data.length; i++) {
        let k = (initial_data[i].longitude - initial_data[i - 1].longitude) / (initial_data[i].latitude - initial_data[i - 1].latitude);
        let b = initial_data[i].longitude - k * initial_data[i].latitude;
        let dx = (initial_data[i].latitude - initial_data[i - 1].latitude) / 500;
        let x = initial_data[i - 1].latitude;
        while (1) {
          let ta = Math.random() * 200, tb = Math.random() * 200;
          data.push([x, x * k + b, [ta, tb]]);
          x += dx;
          if (dx > 0 && x > initial_data[i].latitude) break;
          if (dx < 0 && x < initial_data[i].latitude) break;
        }
      }

      this.$refs.Earth.Start_Navigation(data);
    },
    changeCamera() {

      var position = [...this.cardData.position, ...this.cardData.position];
      position[0] -= 0.5;
      position[1] -= 0.5;
      position[2] += 0.5;
      position[2] += 0.5;

      this.$refs.Earth.change_camera_to(position);
    },

    closeCard() {
      this.card_show = 0;
    },

    ShiQuCity(e) {
      // 拾取到城市

      this.cardData = e;
      this.cardData.position = [this.cardData.latitude, this.cardData.longitude];

      this.card_show = 1;
    },

    ShowGuiJi(e) {

      // 清楚轨迹
      this.$refs.Earth.clear_guiji();
      // 摄像机移动,找到轨迹的最大最小范围
      var minlatitude = 1000;
      var maxlatitude = -1000;
      var minlongitude = 1000;
      var maxlongitude = -1000;
      for (var i = 0; i < e[0].data.length; i++) {
        if (e[0].data[i][0] < minlatitude)
          minlatitude = e[0].data[i][0];
        if (e[0].data[i][0] > maxlatitude)
          maxlatitude = e[0].data[i][0];
        if (e[0].data[i][1] < minlongitude)
          minlongitude = e[0].data[i][1];
        if (e[0].data[i][1] > maxlongitude)
          maxlongitude = e[0].data[i][1];
      }
      var bouds = [minlatitude, minlongitude, maxlatitude, maxlongitude];
      this.$refs.Earth.change_camera_to(bouds);
      // 绘制轨迹
      this.$refs.Earth.add_guiji(e);
      // 显示路线Card
      this.gui_detail_card_data = e;
      // 转化table数据
      for (var i = 0; i < this.gui_detail_card_data.length; i++) {
        var table = [];
        for (var j = 0; j < this.gui_detail_card_data[i].data.length; j++) {
          table.push({
            latitude: this.gui_detail_card_data[i].data[j][0],
            longitude: this.gui_detail_card_data[i].data[j][1],
            index: j + 1
          });
        }
        this.gui_detail_card_data[i].table = table;
      }
      this.card_show = 2;

    },

    jump_config() {
      this.$emit('changestatus');
    },

    change_menu(e) {
      console.log(e);
      this.now_menu_keys = [e.key];
      if (e.key == 1)
        this.is_SoundSpace = true;
      else
        this.is_SoundSpace = false;
    },

    change_NowChoose(e) {
      this.NowChoose = e;
    },

    left_nav_init() {
      $(".content0").mouseenter(function (e) {
        $(".tip0").show();
      });
      $(".content0").mouseleave(function (e) {
        $(".tip0").hide();
      });
      for (let i = 1; i <= 4; i++) {
        if (i != 2) {
          $(".content" + i).click(function () {
            $('.extent-box').animate({ width: 'toggle' }, 350);
          });

        }
        $(".content" + i).mouseenter(function (e) {
          $(".tip" + i).show();
        });
        $(".content" + i).mouseleave(function (e) {
          $(".tip" + i).hide();
        });
      }
    },

    Excute_Navigation() {
      this.$refs.Earth.receive_props();
    },
    async toggle() {
      await this.$fullscreen.toggle();
      this.$refs.Earth.shouye();
    }


  },
  mounted() {
    this.left_nav_init();
  },
};

</script>

<style scoped>
div::-webkit-scrollbar {
  display: none
}

.scroll-view {
  height: 100vh;
  overflow-y: scroll;
  scrollbar-width: 0;
}

.fullscreen-wrapper {
  display: flex;
  padding: 0 !important;
}

.content-wrapper {
  width: 100%;
  height: 100%;
  min-height: 100vh;
  background-color: #000;
}

.map {
  flex-grow: 1;
  background-color: #2A2B2E;
  position: relative;
  min-height: 100vh;
}

.circle {
  width: 214px;
  height: 214px;
  margin: 15px auto;
  border-radius: 50%;
  position: relative;
  background-image: radial-gradient(107px at center,
      #fff 0%, #fff 10%,
      #000 10%, #000 15%,
      #000 15%, #000 20%,
      #fff 20%, #fff 25%,
      #fff 25%, #fff 30%,
      #000 30%, #000 35%,
      #000 35%, #000 40%,
      #fff 40%, #fff 45%,
      #fff 45%, #fff 50%,
      #000 50%, #000 55%,
      #000 55%, #000 60%,
      #fff 60%, #fff 65%,
      #fff 65%, #fff 70%,
      #000 70%, #000 75%,
      #000 75%, #000 80%,
      #fff 80%, #fff 85%,
      #fff 85%, #fff 90%,
      #000 90%, #000 95%,
      #000 95%, #000 100%);
}

.point {
  width: 13px;
  height: 13px;
  background-color: red;
  border-radius: 50%;
  position: absolute;
  z-index: 999;
  top: 100px;
  left: 100px;
}

.card {
  width: 300px;
  max-height: 600px;
  position: fixed;
  background-color: #303235;
  right: 15px;
  top: 15px;
  border-radius: 7px;
  overflow-y: scroll;
}

.card-close {
  width: 26px;
  height: 26px;
  right: 30px;
  top: 25px;
  position: fixed;
}

.circle_card {
  width: 250px;
  height: 250px;
  position: fixed;
  background-color: transparent;
  right: -5px;
  border-radius: 7px;
  overflow-y: scroll;
}

.NavigationPng {
  width: 50px;
  height: 50px;
  position: absolute;
  right: 10px;
  top: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #8ab4f8;
  border-radius: 100px;
}

.cardBottom {
  width: 400px;
  height: 400px;
  position: fixed;
  background-color: #FFFFFF;
  right: 15px;
  bottom: 15px;
  border-radius: 7px;
  overflow-y: scroll;
}

.zhonglishipeiqu-block {
  width: 30px;
  height: 30px;
  background-color: #DFEAFF;
  position: absolute;
  bottom: 18px;
  right: 145px;
}

.zhonglishipeiqu-wenzi {
  width: 110px;
  height: 30px;
  color: #DFEAFF;
  position: absolute;
  bottom: 16px;
  right: 25px;
  font-size: large;
}
</style>
