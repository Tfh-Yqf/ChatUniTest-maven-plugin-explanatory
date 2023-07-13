<template>
  <div id="globe">

    <a-button id="btn" style="margin-left: 10px;border: 1px solid #d9d9d9" v-show="data.type == 2">
      <div v-if="start_plane == 0">开始绘制 空间范围</div>
      <div v-else-if="start_plane == 1" style="color: #ff4e50;font-weight: bold">右键停止</div>
      <div v-else style="color: #eb3e4c;font-weight: bold">重新绘制 空间范围</div>
    </a-button>
    <a-button id="btn_target" style="margin-left: 10px;border: 1px solid #d9d9d9" v-show="data.type == 2">
      <div v-if="start_point == 0">开始绘制 目标位置</div>
      <div v-else-if="start_point == 1" style="color: #ff4e50;font-weight: bold">右键停止</div>
      <div v-else style="color: #eb3e4c;font-weight: bold">重新绘制 目标位置</div>
    </a-button>
    <canvas id="canvas" style="height: 800px;padding: 10px" oncontextmenu="return false"></canvas>



  </div>
</template>


<script>

/*
      data: {
        type : 0, // 0 为首页，1为导航，2为声速场
        center:this.path[0],
        data:[],
        zoom:5
      }
 */
import config from "@/utils/config"

var viewer = null;

export default {
  name: 'pie_map',
  props: ['data'],
  data() {
    return {
      globeLayer: null,
      globe: null,
      geometryID: 17,
      start_plane: 0,
      entity: null,
      entity_point: null,
      start_point: 0,
      loading: true,
      point_lab_entity: [],
      ship_entity: null,
      ship_interval: null,
      now_ship: 0,
      ship_move_buchang: 0,
      cankao_point_entity:[]
    }
  },
  mounted() {
    this.initmap();
  },
  methods: {
    initmap() {
      var that = this;
      console.log('test init Success!')
      // console.log(that.data.center)
      that.loading = false;
      var center = that.data.center;
      if (that.data.type == 1) center = [128, 39];
      viewer = new Earth.Viewer('canvas', {
        center: center, //初始中心点
        zoom: that.data.zoom, //初始层级
        cameraSmooth: false, //是否开启相机缓冲效果
        imageryProvider: new Earth.UrlTemplateImageryProvider({
          url: config.imageryProviderUrl,
          // epsg:4326,
          // levelOffset:-1,
          // tms:true
          // url: 'http://172.16.100.102:9000/wmts?tilematrix={z}&layer=wmts_ifile&style=wmts_satellite&tilerow={y}&tilecol={x}&tilematrixset=Global_ifile&format=image%2Fjpeg&service=WMTS&version=1.0.0&request=GetTile',
          // url:'http://172.16.100.103:9000/mapboxsat/maps?lyrs=i&x={x}&y={y}&z={z}',
        }),
      });


      // let argisImageryProvider = new Earth.ArcGisMapServerImageryProvider({
      //       url:'https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}',
      //       // url: 'http://172.16.100.102:9000/wmts?tilematrix={z}&layer=wmts_ifile&style=wmts_satellite&tilerow={y}&tilecol={x}&tilematrixset=Global_ifile&format=image%2Fjpeg&service=WMTS&version=1.0.0&request=GetTile',
      //   });
      // const layer = viewer.imageryLayers.addImageryProvider(argisImageryProvider);

      // let cityGMLDataSource = viewer.dataSources.add(
      //     Earth.CityGMLDataSource.load('https://engine.piesat.cn/earthweb/docsPlus/Demo/data/cityGML/citygml.xml', {
      //       resourceUrl: 'https://engine.piesat.cn/earthweb/docsPlus/Demo/data/cityGML/',
      //       name: 'cityGML',
      //       scale: 0.62
      //     })
      // );


      that.loading = false;
      // 线数据
      if (that.data.type == 1) {
        viewer.camera.flyTo({
          destination: Earth.Cartesian3.fromDegrees(that.data.center[0], that.data.center[1], 10000.0),
          orientation: {
            heading: Earth.Math.toRadians(0),
            pitch: Earth.Math.toRadians(0),
            roll: 0.0,
          },

          complete: () => {
            console.log('complete');
          },
        });

        that.draw_line();
        that.add_point_tag();
        that.add_boat();


      }
      else if (that.data.type == 2) {
        that.draw_plane();
        that.click_point();
        // that.bind_click();
      } else if (that.data.type == 3) {
        that.add_demarcate_point();
      }



    },
    DrawPoints(e){
      var that = this;
      // 先移除

      while (that.cankao_point_entity.length!=0) {
        viewer.entities.remove(that.cankao_point_entity[0]);
        that.cankao_point_entity.pop();
      }

      for(var i=0;i<e.length;i++){
        var entity = new Earth.Entity({
          name: 'billboard',
          show: true,
          position: Earth.Cartesian3.fromDegrees(e[i].point[0],e[i].point[1] , 0),
          point: {
            // show: true,
            pixelSize: 16,
            color: Earth.Color.BLUE.withAlpha(0),
            outlineColor: undefined, //不支持
            outlineWidth: undefined, //不支持
            distanceDisplayCondition: undefined, //暂不支持
          },
          label: {
            text: '参考点' + e[i].index,
            font: '20Px sans-serif', // 大小及字体 目前只支持大小
            fillColor: Earth.Color.RED.withAlpha(1),//可设置透明度
            heightReference: Earth.HeightReference.NONE,
            pixelOffset: new Earth.Cartesian2(50, 50),
          },
          billboard: {
            show: true, //可省略 默认是显示
            image: '/Resource/Icons/cankao.png', // 指定广告牌的URL，
            horizontalOrigin: Earth.HorizontalOrigin.CENTER,//
            verticalOrigin: Earth.VerticalOrigin.CENTER, // default: CENTER 垂直对齐位置 参考Cesium.VerticalOrigin
            heightReference: Earth.HeightReference.RELATIVE_TO_GROUND,//暂时不支持
            rotation: 90,//暂不支持
            width: 40, // 用于指定广告牌的宽度（以像素为单位），并覆盖原始尺寸。
            height: 40, // 用于指定广告牌的高度（以像素为单位），并覆盖原始尺寸。
          },
        });
        viewer.entities.add(entity);
        that.cankao_point_entity.push(entity);
      }


    },
    add_boat() {
      var that = this;

      console.log('--------------------------------------')

      this.ship_move_buchang = parseInt(this.data.data.length / (100 * 20));
      that.ship_interval = setInterval(function moveship() {
        if (that.now_ship >= that.data.data.length) clearInterval(that.ship_interval);
        else {
          if (that.ship_entity) {
            // viewer.entities.remove(that.ship_entity);
            that.ship_entity.position = new Earth.Cartesian3.fromDegrees(that.data.data[that.now_ship][0], that.data.data[that.now_ship][1], 0);
          } else {
            that.ship_entity = new Earth.Entity({
              name: 'billboard',
              show: true,
              position: new Earth.Cartesian3.fromDegrees(that.data.data[that.now_ship][0], that.data.data[that.now_ship][1], 0),
              point: {
                // show: true,
                pixelSize: 16,
                color: Earth.Color.BLUE.withAlpha(0),
                outlineColor: undefined, //不支持
                outlineWidth: undefined, //不支持
                distanceDisplayCondition: undefined, //暂不支持
              },
              billboard: {
                show: true, //可省略 默认是显示
                image: '/Resource/Icons/ship.png', // 指定广告牌的URL，
                // scale: 1.0, // default: 1.0 ，一个数字属性，指定要应用于图像尺寸的比例  //暂不支持
                // //像素偏移    type: Cartesian2    default:Cartesian2.ZERO
                // pixelOffset: new Earth.Cartesian2(200, 0), // 二维笛卡尔坐标系 //暂不支持
                // //眼睛偏移    type: Cartesian3    default:Cartesian3.ZERO
                // eyeOffset: new Earth.Cartesian3(0.0, 0.0, 0.0), // 三维笛卡尔坐标点 //暂不支持
                // // HorizontalOrigin CENTER 原点在对象的水平中心；LEFT 原点在对象的左侧；RIGHT 原点在对象的右侧
                horizontalOrigin: Earth.HorizontalOrigin.CENTER,//
                // // 垂直对齐方式  type: VerticalOrigin  default:VerticalOrigin.CENTER
                // // CENTER 原点位于中心， TOP 之间的垂直中心；BOTTOM 原点在对象的底部；
                verticalOrigin: Earth.VerticalOrigin.CENTER, // default: CENTER 垂直对齐位置 参考Cesium.VerticalOrigin
                // // NONE 位置绝对；CLAMP_TO_GROUND 位置固定在地形上；RELATIVE_TO_GROUND 位置高度是指地形上方的高度
                heightReference: Earth.HeightReference.RELATIVE_TO_GROUND,//暂时不支持
                // color: new Earth.Color.fromCssColorString('#ff88ff'), // 颜色不透明 暂不支持
                // // 获取或设置以弧度为单位的旋转角度  type: number  default:0
                rotation: 5,//暂不支持
                width: 80, // 用于指定广告牌的宽度（以像素为单位），并覆盖原始尺寸。
                height: 80, // 用于指定广告牌的高度（以像素为单位），并覆盖原始尺寸。
                // scaleByDistance: new Earth.NearFarScalar(1.0e3, 2.0, 2.0e3, 1.0),//不支持
                // disableDepthTestDistance: Number.POSITIVE_INFINITY,//不支持
              },
            });
            viewer.entities.add(that.ship_entity);


          }
        }



        that.now_ship += that.ship_move_buchang;

      }, 10);

    },
    bind_click() {
      var that = this;
      const handler = new Earth.ScreenSpaceEventHandler(viewer.scene.canvas);

      handler.setInputAction(function (movement) {
        const pickObj = viewer.scene.pick(movement.position);
        console.log(pickObj);
        // pickObj && alert('拾取成功：' + pickObj.name);
        if (pickObj) {
          console.log('拾取成功：' + pickObj.name);
          that.$emit('shiquSucc', pickObj.name);

        }
      }, Earth.ScreenSpaceEventType.LEFT_CLICK);
    },
    change_zoom_center(center, zoom) {
      viewer.setCenter(center);
      viewer.setZoom(zoom);
    },
    add_guiji(e) {
      // 处理数据
      const globe = viewer._pieGlobe
      const spatialReference = globe.getSpatialReference();
      const points = [];
      // const arrGeo = geoJson.features[0].geometry.coordinates[0][0];
      const arrGeo = this.data.circle;
      // console.log('arrGeo');
      console.log(arrGeo);

      for (let i = 0; i < arrGeo.length; i++) {
        const pointSR = spatialReference.forward(arrGeo[i])

        points.push(pointSR[0])
        points.push(pointSR[1])
      }

      // 添加
      const datasource = new Earth.RenderableDataSource()
      viewer.dataSources.add(datasource)
      const geoPolyline = new Earth.GeoPolyline({ points: points, ID: 17 })
      const renderPolyLine = new Earth.RenderPolyline({
        polyline: geoPolyline,
        color: new Earth.Color(1, 0, 0, 0.5),
        width: 5
      })
      datasource.add(renderPolyLine);
      this.$message.success('绘制成功！');
    },
    add_demarcate_point() {
      var that = this;
      console.log('add_demarcate point');
      console.log(that.data.data);
      for (var i = 0; i < that.data.data.all_point.length; i++) {
        var e = that.data.data.all_point[i];
        var entity_temp = new Earth.Entity({
          name: (that.data.data.accuracy[i]).toString(),
          position: Earth.Cartesian3.fromDegrees(e[0], e[1], 0),//Cartesian3类型，用于指定框的经度，维度和高度
          show: true,
          point: {
            // show: true,
            pixelSize: 11,
            color: Earth.Color.WHITE.withAlpha(1),
            heightReference: Earth.HeightReference.CLAMP_TO_GROUND, //贴地
            outlineColor: undefined, //不支持
            outlineWidth: undefined, //不支持
            distanceDisplayCondition: undefined, //暂不支持
          },
          circle: {
            radius: 100,
            material: new Earth.Material({
              type: Earth.MaterialType.Diffusion,//扩散圆材质
              fabric: {
                color: Earth.Color.RED,
              }

            }),
          },
          label: {
            // show: true,
            text: '点' + (i + 1).toString(),
            font: '30Px sans-serif', // 大小及字体 目前只支持大小
            fillColor: Earth.Color.WHITE.withAlpha(0.8),//可设置透明度
            // fillColor:new Earth.Color.fromCssColorString("#ff88ff"), // 颜色不透明
            // 指定距实体位置的高度是相对于什么的高度。 default: HeightReference.NONE
            // NONE    位置绝对0；CLAMP_TO_GROUND    固定在地形上1；RELATIVE_TO_GROUND  2 位置高度是指地形上方的高度2。
            heightReference: Earth.HeightReference.CLAMP_TO_GROUND,//贴地
            verticalOrigin: Earth.VerticalOrigin.BOTTOM, // 垂直 位置是相对于label 点的位置
            horizontalOrigin: Earth.HorizontalOrigin.LEFT, //水平
            pixelOffset: new Earth.Cartesian2(0, 15),
            showBackground: false,
            backgroundColor: new Earth.Color.fromCssColorString("#ff0000"),//暂不支持

          },
        });

        that.point_lab_entity.push(entity_temp);
        viewer.entities.add(entity_temp);
      }
    },
    add_point_tag() {
      var that = this;
      console.log('all')
      console.log(that.data.all_point_label);
      for (var i = 0; i < that.data.all_point_label.length; i++) {
        var e = that.data.all_point_label[i];
        var entity_temp = new Earth.Entity({
          name: (i + 1).toString(),
          position: Earth.Cartesian3.fromDegrees(e[0], e[1], 0),//Cartesian3类型，用于指定框的经度，维度和高度
          show: true,
          point: {
            // show: true,
            pixelSize: 13,
            color: Earth.Color.WHITE.withAlpha(1),
            heightReference: Earth.HeightReference.CLAMP_TO_GROUND, //贴地
            outlineColor: undefined, //不支持
            outlineWidth: undefined, //不支持
            distanceDisplayCondition: undefined, //暂不支持
          },
          label: {
            // show: true,
            text: '点' + (i + 1).toString(),
            font: '30Px sans-serif', // 大小及字体 目前只支持大小
            fillColor: Earth.Color.WHITE.withAlpha(0.8),//可设置透明度
            // fillColor:new Earth.Color.fromCssColorString("#ff88ff"), // 颜色不透明
            // 指定距实体位置的高度是相对于什么的高度。 default: HeightReference.NONE
            // NONE    位置绝对0；CLAMP_TO_GROUND    固定在地形上1；RELATIVE_TO_GROUND  2 位置高度是指地形上方的高度2。
            heightReference: Earth.HeightReference.CLAMP_TO_GROUND,//贴地
            verticalOrigin: Earth.VerticalOrigin.BOTTOM, // 垂直 位置是相对于label 点的位置
            horizontalOrigin: Earth.HorizontalOrigin.LEFT, //水平
            pixelOffset: new Earth.Cartesian2(0, 15),
            showBackground: false,
            backgroundColor: new Earth.Color.fromCssColorString("#ff0000"),//暂不支持
            // eyeOffset :undefined,//暂不支持
            // outlineColor : undefined,//暂不支持
            // outlineWidth : undefined,//暂不支持
            // distanceDisplayCondition : undefined,//暂不支持
            // scaleByDistance : undefined,//暂不支持
          },
        });

        that.point_lab_entity.push(entity_temp);
        viewer.entities.add(entity_temp);
      }
      that.bind_click();
    },
    click_point() {
      var that = this;
      const handler = new Earth.ScreenSpaceEventHandler(viewer.scene.canvas);
      let spatialReference = viewer.scene.globe.spatialReference.createWebMercator();
      document.getElementById('btn_target').onclick = () => {


        handler.setInputAction(function (movement) {
          console.log('鼠标左键点击-绘制点');
          that.start_point = 1;
          if (that.entity_point) {
            viewer.entities.remove(that.entity_point);
            that.data.point_positions = [];
            that.entity_point = null;
          }
          let carresian3 = viewer.scene.globe.pick([
            movement.position.x,
            movement.position.y,
          ]);
          that.data.point_positions = carresian3;
          that.data.point_lon_positions = spatialReference.inverse([carresian3.x, carresian3.y]);

          if (!that.entity_point) {
            that.entity_point = new Earth.Entity({
              name: 'billboard',
              show: true,
              position: that.data.point_positions,
              point: {
                // show: true,
                pixelSize: 16,
                color: Earth.Color.BLUE.withAlpha(0),
                outlineColor: undefined, //不支持
                outlineWidth: undefined, //不支持
                distanceDisplayCondition: undefined, //暂不支持
              },
              billboard: {
                show: true, //可省略 默认是显示
                image: '/Resource/Icons/target.png', // 指定广告牌的URL，
                // scale: 1.0, // default: 1.0 ，一个数字属性，指定要应用于图像尺寸的比例  //暂不支持
                // //像素偏移    type: Cartesian2    default:Cartesian2.ZERO
                // pixelOffset: new Earth.Cartesian2(200, 0), // 二维笛卡尔坐标系 //暂不支持
                // //眼睛偏移    type: Cartesian3    default:Cartesian3.ZERO
                // eyeOffset: new Earth.Cartesian3(0.0, 0.0, 0.0), // 三维笛卡尔坐标点 //暂不支持
                // // HorizontalOrigin CENTER 原点在对象的水平中心；LEFT 原点在对象的左侧；RIGHT 原点在对象的右侧
                horizontalOrigin: Earth.HorizontalOrigin.CENTER,//
                // // 垂直对齐方式  type: VerticalOrigin  default:VerticalOrigin.CENTER
                // // CENTER 原点位于中心， TOP 之间的垂直中心；BOTTOM 原点在对象的底部；
                verticalOrigin: Earth.VerticalOrigin.CENTER, // default: CENTER 垂直对齐位置 参考Cesium.VerticalOrigin
                // // NONE 位置绝对；CLAMP_TO_GROUND 位置固定在地形上；RELATIVE_TO_GROUND 位置高度是指地形上方的高度
                heightReference: Earth.HeightReference.RELATIVE_TO_GROUND,//暂时不支持
                // color: new Earth.Color.fromCssColorString('#ff88ff'), // 颜色不透明 暂不支持
                // // 获取或设置以弧度为单位的旋转角度  type: number  default:0
                rotation: 90,//暂不支持
                width: 40, // 用于指定广告牌的宽度（以像素为单位），并覆盖原始尺寸。
                height: 40, // 用于指定广告牌的高度（以像素为单位），并覆盖原始尺寸。
                // scaleByDistance: new Earth.NearFarScalar(1.0e3, 2.0, 2.0e3, 1.0),//不支持
                // disableDepthTestDistance: Number.POSITIVE_INFINITY,//不支持
              },
            });
            viewer.entities.add(that.entity_point);
          }
        }, Earth.ScreenSpaceEventType.LEFT_CLICK);

        handler.setInputAction(function (movement) {
          that.start_point = 2;
          console.log('鼠标右键点击-绘制点');
          handler.removeInputAction(Earth.ScreenSpaceEventType.LEFT_CLICK);
          handler.removeInputAction(Earth.ScreenSpaceEventType.MOUSE_MOVE);
          handler.removeInputAction(Earth.ScreenSpaceEventType.RIGHT_CLICK);
        }, Earth.ScreenSpaceEventType.RIGHT_CLICK);
      };
    },
    change_target_point() {
      var that = this;
      console.log('change_point');
      // console.log(this.data);
      let spatialReference = viewer.scene.globe.spatialReference.createWebMercator();

      var temp = spatialReference.forward(this.data.point_lon_positions);

      this.data.point_positions = { x: temp[0], y: temp[1], z: 10000 };
      console.log(that.data.point_positions);
      // console.log(this.data);

      if (that.entity_point) {
        viewer.entities.remove(that.entity_point);
      }

      that.entity_point = new Earth.Entity({
        name: 'billboard',
        show: true,
        position: Earth.Cartesian3.fromDegrees(that.data.point_lon_positions[0],that.data.point_lon_positions[1] , 0),
        point: {
          // show: true,
          pixelSize: 16,
          color: Earth.Color.BLUE.withAlpha(0),
          outlineColor: undefined, //不支持
          outlineWidth: undefined, //不支持
          distanceDisplayCondition: undefined, //暂不支持
        },
        label: {
          text: '目标点',
          font: '20Px sans-serif', // 大小及字体 目前只支持大小
          fillColor: Earth.Color.RED.withAlpha(1),//可设置透明度
          heightReference: Earth.HeightReference.NONE,
          pixelOffset: new Earth.Cartesian2(50, 50),
        },
        billboard: {
          show: true, //可省略 默认是显示
          image: '/Resource/Icons/target.png', // 指定广告牌的URL，

          horizontalOrigin: Earth.HorizontalOrigin.CENTER,//

          verticalOrigin: Earth.VerticalOrigin.CENTER, // default: CENTER 垂直对齐位置 参考Cesium.VerticalOrigin

          heightReference: Earth.HeightReference.RELATIVE_TO_GROUND,//暂时不支持

          rotation: 90,//暂不支持
          width: 40, // 用于指定广告牌的宽度（以像素为单位），并覆盖原始尺寸。
          height: 40, // 用于指定广告牌的高度（以像素为单位），并覆盖原始尺寸。

        },
      });
      viewer.entities.add(that.entity_point);
    },
    draw_plane() {
      var that = this;
      const handler = new Earth.ScreenSpaceEventHandler(viewer.scene.canvas);
      let count = 0;

      let spatialReference = viewer.scene.globe.spatialReference.createWebMercator();
      let start = false;
      document.getElementById('btn').onclick = () => {
        if (that.entity) {
          viewer.entities.remove(that.entity);
          that.data.positions = [];
          that.data.lon_positions = [];
          that.entity = null;
        }
        handler.setInputAction(function (movement) {
          that.start_plane = 1;
          console.log('鼠标左键点击');
          start = true;
          let carresian3 = viewer.scene.globe.pick([
            movement.position.x,
            movement.position.y,
          ]);
          that.data.positions.push(carresian3);
          that.data.lon_positions.push(spatialReference.inverse([carresian3.x, carresian3.y]));
          if (!that.entity) {
            that.entity = new Earth.Entity({
              polygon: {
                show: true,
                hierarchy: that.data.positions,
                color: new Earth.Color(0, 1, 0, 0.3),
                heightReference: Earth.HeightReference.CLAMP_TO_GROUND,
              },
            });
            viewer.entities.add(that.entity);
          }
        }, Earth.ScreenSpaceEventType.LEFT_CLICK);

        handler.setInputAction(function (movement) {
          console.log('鼠标移动');
          if (!start) return;
          if (that.entity && that.data.positions.length >= 3) {
            that.data.positions.pop();
            that.data.lon_positions.pop();
          }
          if (that.entity && that.data.positions.length >= 2) {
            let carresian3 = viewer.scene.globe.pick([
              movement.endPosition.x,
              movement.endPosition.y,
            ]);
            that.data.positions.push(carresian3);
            that.data.lon_positions.push(spatialReference.inverse([carresian3.x, carresian3.y]));
            that.entity.polygon.hierarchy = that.data.positions;
          }
        }, Earth.ScreenSpaceEventType.MOUSE_MOVE);

        handler.setInputAction(function (movement) {
          that.start_plane = 2;
          console.log('鼠标右键点击');
          start = false;
          handler.removeInputAction(Earth.ScreenSpaceEventType.LEFT_CLICK);
          handler.removeInputAction(Earth.ScreenSpaceEventType.MOUSE_MOVE);
          handler.removeInputAction(Earth.ScreenSpaceEventType.RIGHT_CLICK);
        }, Earth.ScreenSpaceEventType.RIGHT_CLICK);
      };
    },
    change_plane() {
      // console.log('change_plane');
      // console.log(this.data);
      let spatialReference = viewer.scene.globe.spatialReference.createWebMercator();

      var result = [];
      for (var i = 0; i < this.data.lon_positions.length; i++) {
        var temp = spatialReference.forward(this.data.lon_positions[i]);
        result.push({ x: temp[0], y: temp[1], z: this.data.positions[i].z });
      }
      this.data.positions = result;
      // console.log(result);
      // console.log(this.data);
      this.entity.polygon.hierarchy = this.data.positions;
    },
    draw_line() {

      // 处理数据
      const globe = viewer._pieGlobe
      const spatialReference = globe.getSpatialReference();
      const points = [];
      // const arrGeo = geoJson.features[0].geometry.coordinates[0][0];

      const arrGeo = JSON.parse(JSON.stringify(this.data.data));
      console.log('test');
      console.log(arrGeo);

      for (let i = 0; i < arrGeo.length; i++) {
        const pointSR = spatialReference.forward(arrGeo[i])
        points.push(pointSR[0])
        points.push(pointSR[1])
      }
      console.log(points);
      //添加
      const datasource = new Earth.RenderableDataSource()
      viewer.dataSources.add(datasource)
      const geoPolyline = new Earth.GeoPolyline({ points: points, ID: 10 })
      const renderPolyLine = new Earth.RenderPolyline({
        polyline: geoPolyline,
        color: new Earth.Color(82 / 255, 146 / 255, 241 / 255, 1),
        width: 6
      })
      datasource.add(renderPolyLine);
    },
  },
  beforeDestroy() {
    if (viewer != null) {
      console.log('Dispose start!')
      viewer.dispose();
    }
  }
}
</script>

<style>
#canvas {
  width: 100%;
  height: 100%;
}
</style>