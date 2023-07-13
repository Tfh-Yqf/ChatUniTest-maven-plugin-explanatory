<template>
  <div id="globe" class="canvas-container">
    <canvas ref="canvas" id="canvas" class="canvas" style=""></canvas>
  </div>
</template>

<script>
import $ from 'jquery'

var viewer = null;
export default {
  name: 'MyEarth',
  data() {
    return {
      data: [],
      viewer: null,
      Entitys: [],
      city: [
        {
          img: 'https://img3.yun300.cn/repository/image/4fe6bbc1-c299-41dc-ab5b-23f71e8cf7e2.png_640xaf.png?tenantId=221581&viewType=1',
          name: '花莲港',
          content: '花莲市位于台湾花莲县东部北段，花东纵谷北侧的开口处，为花莲县县治所在地以及县内唯一的县辖市。',
          latitude: 121.3752,
          longitude: 23.5910
        },
        {
          img: 'https://img1.baidu.com/it/u=2226097721,4194191472&fm=253&fmt=auto&app=120&f=JPEG?w=735&h=500',
          content: '南沙港位于台湾花莲县东部北段，花东纵谷北侧的开口处，为花莲县县治所在地以及县内唯一的县辖市。',
          name: '南沙港',
          latitude: 113.3448,
          longitude: 22.4839
        },
        {
          img: 'https://img1.baidu.com/it/u=751976069,2907256282&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=333',
          content: '新海港位于台湾花莲县东部北段，花东纵谷北侧的开口处，为花莲县县治所在地以及县内唯一的县辖市。',
          name: '新海港',
          latitude: 110.0922,
          longitude: 20.0323
        }
      ],
      city_entity: [],
      guiji: [],
      guiji_name_entity: [],
      Gravity_entity: [],
      NewGuijiEntity: {},
      NewGuijiPoints: [],
      NewGuiji_Position: {},
      NewGuiji_LonPosition: []
    }
  },
  mounted() {
    this.init();
    // this.test();
  },
  methods: {
    init() {
      var that = this;
      console.log('test init Success!')
      if (window.Module == undefined || window.Module == null) {
        createEarthModule().then(function () {
          viewer = new Earth.Viewer('canvas', {
            sceneMode: Earth.SceneMode.SCENE3D, //Earth.SceneMode.SCENE2D
            center: [116.4, 39.9], //初始中心点
            zoom: 3, //初始层级
            cameraSmooth: false, //是否开启相机缓冲效果
            imageryProvider: new Earth.UrlTemplateImageryProvider({
              url: 'https://webst03.is.autonavi.com/appmaptile?style=6&z={z}&y={y}&x={x}',
            }),
            terrainProvider: new Earth.EngineTerrainProvider({
              url: 'https://pie-engine-test.s3.cn-northwest-1.amazonaws.com.cn/earthdata/mapdata/sichuandem/_alllayers/{z}/{y}/{x}.terrain',
            }),
          });

          let argisImageryProvider = new Earth.ArcGisMapServerImageryProvider({
            url:
              'https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}',
          });
          const layer = viewer.imageryLayers.addImageryProvider(argisImageryProvider);

          let cityGMLDataSource = viewer.dataSources.add(
            Earth.CityGMLDataSource.load('https://engine.piesat.cn/earthweb/docsPlus/Demo/data/cityGML/citygml.xml', {
              resourceUrl: 'https://engine.piesat.cn/earthweb/docsPlus/Demo/data/cityGML/',
              name: 'cityGML',
              scale: 0.62
            })
          );

          const line = viewer.dataSources.add(
            Earth.GeoJsonDataSource.load('/Resource/taiwan.geojson', {
              stroke: Earth.Color.PINK.withAlpha(0),
              strokeWidth: 3,
            })
          );

          line.then((value) => {
            let labelTextStyle = new Earth.TextStyle({
              faceName: '宋体', // 字体设置暂不支持 - 底层接口无效
              foreColor: new Earth.Color(1, 1, 1, 1),
              width: 12,
              height: 12,
              align: Earth.TextAlignType.BottomCenter,
            });
            let labelRenderer = new Earth.ThemeLabelFeatureRenderer({
              textExpression: 'name',
              style: labelTextStyle,
            });
            value.renderer = labelRenderer;
          });


          // 添加城市
          that.add_city();

          that.bind_click();

          // 添加平面
          that.addPlane();
        });
      } else {
        viewer = new Earth.Viewer('canvas', {
          sceneMode: Earth.SceneMode.SCENE3D, //Earth.SceneMode.SCENE2D
          center: [116.4, 39.9], //初始中心点
          zoom: 4, //初始层级
          cameraSmooth: false, //是否开启相机缓冲效果
          imageryProvider: new Earth.UrlTemplateImageryProvider({
            url:
              'https://webst03.is.autonavi.com/appmaptile?style=6&z={z}&y={y}&x={x}',
          }),
          terrainProvider: new Earth.EngineTerrainProvider({
            url:
              'https://pie-engine-test.s3.cn-northwest-1.amazonaws.com.cn/earthdata/mapdata/sichuandem/_alllayers/{z}/{y}/{x}.terrain',
          }),
        });
        let argisImageryProvider = new Earth.ArcGisMapServerImageryProvider({
          url:
            'https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}',
        });
        const layer = viewer.imageryLayers.addImageryProvider(argisImageryProvider);

        let cityGMLDataSource = viewer.dataSources.add(
          Earth.CityGMLDataSource.load('https://engine.piesat.cn/earthweb/docsPlus/Demo/data/cityGML/citygml.xml', {
            resourceUrl: 'https://engine.piesat.cn/earthweb/docsPlus/Demo/data/cityGML/',
            name: 'cityGML',
            scale: 0.62
          })
        );


        // 添加城市
        that.add_city();

        that.bind_click();

        that.addPlane();
      }
    },
    // 绘制轨迹
    drawGuiji(id) {
      $(".canvas").css({
        "cursor": "crosshair"
      });

      var that = this;
      const handler = new Earth.ScreenSpaceEventHandler(viewer.scene.canvas);
      let start = false;
      let move = false;

      that.NewGuijiPoints = [];
      console.log(that.NewGuijiEntity);
      that.NewGuijiEntity[id] = null;
      that.NewGuiji_LonPosition = [];
      that.NewGuiji_Position[id] = [];

      handler.setInputAction(function (movement) {
        start = true;
        move = false;
        let cartesian3 = viewer.scene.globe.pick([
          movement.position.x,
          movement.position.y,
        ]);
        that.NewGuiji_Position[id].push(cartesian3);
        var cartographic = Earth.Cartographic.fromCartesian(cartesian3);
        var longitude = Earth.Math.toDegrees(cartographic.longitude); //经度
        var latitude = Earth.Math.toDegrees(cartographic.latitude); //纬度
        that.NewGuiji_LonPosition.push([longitude, latitude]);

        const entity = new Earth.Entity({
          name: 'billboard',
          show: true,
          position: Earth.Cartesian3.fromDegrees(longitude, latitude, 0), //Cartesian3类型，用于指定框的经度，维度和高度
          billboard: {
            image: 'Resource/Icons/location.png', // 指定广告牌的URL，
            horizontalOrigin: Earth.HorizontalOrigin.CENTER,
            verticalOrigin: Earth.VerticalOrigin.CENTER, // default: CENTER 垂直对齐位置 参考Cesium.VerticalOrigin
            width: 50, // 用于指定广告牌的宽度（以像素为单位），并覆盖原始尺寸。
            height: 50, // 用于指定广告牌的高度（以像素为单位），并覆盖原始尺寸。
          },
        });

        viewer.entities.add(entity);
        that.NewGuijiPoints.push(entity);

        if (!that.NewGuijiEntity[id]) {
          that.NewGuijiEntity[id] = new Earth.Entity({
            show: true,
            polyline: {
              positions: that.NewGuiji_Position[id],
              width: 6,
              color: new Earth.Color(0, 1, 1, 1),
              clampToGround: true, //贴地
            },
          });
          viewer.entities.add(that.NewGuijiEntity[id]);
        }
      }, Earth.ScreenSpaceEventType.LEFT_CLICK);

      handler.setInputAction(function (movement) {
        if (!start) return;
        move = true;

        if (that.NewGuijiEntity[id] && that.NewGuiji_Position[id].length >= 3) {
          that.NewGuiji_Position[id].pop();
          that.NewGuiji_LonPosition.pop();
        }
        if (that.NewGuijiEntity[id] && that.NewGuiji_Position[id].length >= 2) {
          let cartesian3 = viewer.scene.globe.pick([
            movement.endPosition.x,
            movement.endPosition.y,
          ]);
          that.NewGuiji_Position[id].push(cartesian3);
          var cartographic = Earth.Cartographic.fromCartesian(cartesian3);
          var longitude = Earth.Math.toDegrees(cartographic.longitude); //经度
          var latitude = Earth.Math.toDegrees(cartographic.latitude); //纬度
          that.NewGuiji_LonPosition.push([longitude, latitude]);
          that.NewGuijiEntity[id].polyline.positions = that.NewGuiji_Position[id];
        }
      }, Earth.ScreenSpaceEventType.MOUSE_MOVE);


      $(document).keyup((e) => {
        if (e.key === "Enter") {
          console.log("enter");
          $(".canvas").css({
            "cursor": "auto"
          });
          start = false;
          if (that.NewGuijiEntity[id] && move) {
            that.NewGuiji_Position[id].pop();
            that.NewGuiji_LonPosition.pop();
          }
          that.NewGuijiEntity[id].polyline.positions = that.NewGuiji_Position[id];

          handler.removeInputAction(Earth.ScreenSpaceEventType.LEFT_CLICK);
          handler.removeInputAction(Earth.ScreenSpaceEventType.MOUSE_MOVE);
          that.$emit("DrawGuijiSuccess", that.NewGuiji_LonPosition);
        }
      });
    },
    change_guiji_color(params) {
      let color = params.color, id = params.id;
      this.NewGuijiEntity[id].polyline.color = new Earth.Color(color[0], color[1], color[2], color[3]);
    },
    bind_click() {
      // 拾取Entity
      var that = this;
      const handler = new Earth.ScreenSpaceEventHandler(viewer.scene.canvas);

      handler.setInputAction(function (movement) {
        const pickObj = viewer.scene.pick(movement.position);
        console.log(pickObj);
        // pickObj && alert('拾取成功：' + pickObj.name);
        if (pickObj) {
          console.log('拾取成功：' + pickObj.name);
          for (var i = 0; i < that.city.length; i++) {
            if (that.city[i].name == pickObj.name) {
              that.$emit('ShiQuCity', that.city[i]);
              that.$message.success('拾取成功!');
            }
          }
        }
      }, Earth.ScreenSpaceEventType.LEFT_CLICK);
    },
    change_camera_to(e) {
      // 摄像机移动
      viewer.camera.flyTo({
        destination: Earth.Bounds.fromDegrees(e[0], e[1], e[2], e[3]),
        // destination: Earth.Bounds.fromDegrees(110.0922, 20.0323, 123.3752, 24.5910),
        orientation: {
          heading: Earth.Math.toRadians(20.0),
          pitch: Earth.Math.toRadians(-35.0),
          roll: 0.0,
        },
      });
    },
    clear_guiji() {
      // 删除其他轨迹
      while (this.guiji.length != 0) {
        viewer.dataSources.remove(this.guiji[0]);
        this.guiji.pop();
      }

      while (this.guiji_name_entity.length != 0) {
        viewer.entities.remove(this.guiji_name_entity[0])
        this.guiji_name_entity.splice(0, 1);
      }
    },
    add_guiji(e) {
      this.addPoint();
      // 处理数据
      const globe = viewer._pieGlobe;
      const spatialReference = globe.getSpatialReference();
      const datasource = new Earth.RenderableDataSource();
      var shpLine = viewer.dataSources.add(datasource)
      this.guiji.push(shpLine);

      for (var i = 0; i < e.length; i++) {
        const points = [];
        const arrGeo = e[i].data;

        for (let i = 0; i < arrGeo.length; i++) {
          const pointSR = spatialReference.forward(arrGeo[i])
          points.push(pointSR[0])
          points.push(pointSR[1])
        }

        const geoPolyline = new Earth.GeoPolyline({ points: points, ID: i })
        const renderPolyLine = new Earth.RenderPolyline({
          polyline: geoPolyline,
          color: e[i].color,
          width: 6
        });
        datasource.add(renderPolyLine);

        var mid = parseInt(e[i].data.length / 2);
        console.log(mid + e[i].data[mid])

        const entity = new Earth.Entity({
          name: 'billboard',
          show: true,
          position: Earth.Cartesian3.fromDegrees(e[i].data[mid][0], e[i].data[mid][1], 0),
          label: {
            text: e[i].name,
            font: '30Px sans-serif', // 大小及字体 目前只支持大小
            fillColor: e[i].color,//可设置透明度
            heightReference: Earth.HeightReference.CLAMP_TO_GROUND,//贴地
            verticalOrigin: Earth.VerticalOrigin.BOTTOM, // 垂直 位置是相对于label 点的位置
            horizontalOrigin: Earth.HorizontalOrigin.LEFT, //水平
            pixelOffset: new Earth.Cartesian2(10, 0),
            showBackground: false,
            backgroundColor: new Earth.Color.fromCssColorString("#ff0000"),//暂不支持
          },
        });
        viewer.entities.add(entity);
        this.guiji_name_entity.push(entity);
      }

      this.$message.success('绘制成功！');
    },

    add_city() {
      var that = this;
      console.log('add_demarcate point');
      console.log(that.data.data);
      for (var i = 0; i < that.city.length; i++) {
        var e = that.city[i];
        var entity_temp = new Earth.Entity({
          name: e.name,
          position: Earth.Cartesian3.fromDegrees(e.latitude, e.longitude, 0),//Cartesian3类型，用于指定框的经度，维度和高度
          show: true,
          billboard: {
            show: true, //可省略 默认是显示
            image: '/Resource/Icons/location.png', // 指定广告牌的URL，

            horizontalOrigin: Earth.HorizontalOrigin.CENTER,//

            verticalOrigin: Earth.VerticalOrigin.CENTER, // default: CENTER 垂直对齐位置 参考Cesium.VerticalOrigin

            heightReference: Earth.HeightReference.RELATIVE_TO_GROUND,//暂时不支持

            rotation: 0,//暂不支持
            width: 50, // 用于指定广告牌的宽度（以像素为单位），并覆盖原始尺寸。
            height: 50, // 用于指定广告牌的高度（以像素为单位），并覆盖原始尺寸。

          },
          label: {
            // show: true,
            text: e.name,
            font: '30Px sans-serif', // 大小及字体 目前只支持大小
            fillColor: Earth.Color.WHITE.withAlpha(1),//可设置透明度
            // fillColor:new Earth.Color.fromCssColorString("#ff88ff"), // 颜色不透明
            // 指定距实体位置的高度是相对于什么的高度。 default: HeightReference.NONE
            // NONE    位置绝对0；CLAMP_TO_GROUND    固定在地形上1；RELATIVE_TO_GROUND  2 位置高度是指地形上方的高度2。
            heightReference: Earth.HeightReference.CLAMP_TO_GROUND,//贴地
            verticalOrigin: Earth.VerticalOrigin.BOTTOM, // 垂直 位置是相对于label 点的位置
            horizontalOrigin: Earth.HorizontalOrigin.LEFT, //水平
            pixelOffset: new Earth.Cartesian2(10, 0),
            showBackground: false,
            backgroundColor: new Earth.Color.fromCssColorString("#ff0000"),//暂不支持
            // eyeOffset :undefined,//暂不支持
            // outlineColor : undefined,//暂不支持
            // outlineWidth : undefined,//暂不支持
            // distanceDisplayCondition : undefined,//暂不支持
            // scaleByDistance : undefined,//暂不支持
          },
        });

        that.city_entity.push(entity_temp);
        viewer.entities.add(entity_temp);
      }
    },
    deal_data_Navigation() {
      var result = { data: null };
      var path = [];
      result.data = this.$store.state.system.Navgiation.data;
      var point = this.$store.state.system.Navgiation.point;

      for (var i = 0; i < result.data.blh_b.length; i++) {
        path.push([parseFloat(result.data.blh_l[i]) * 180 / 3.1415, parseFloat(result.data.blh_b[i]) * 180 / 3.1415]);
      }
      var temp_point = [];
      for (var i = 0; i < point.lat.length; i++) {
        temp_point.push([point.lon[i] * 180 / 3.1415, point.lat[i] * 180 / 3.1415]);
      }
      path = {
        type: 1,
        center: path[0],
        data: path,
        zoom: 14,
        all_point_label: temp_point
      };
      return path;
    },
    receive_props() {
      var data = this.deal_data_Navigation();
      this.data.push(data);

      for (let entity of this.Entitys) {
        viewer.remove(entity)
      }
      this.Entitys = [];

      const entityCollection = viewer.entities;
      for (let point of data.all_point_label) {
        const entity = new Earth.Entity({
          name: 'billboard',
          show: true,
          position: Earth.Cartesian3.fromDegrees(point[0], point[1], 0),
          billboard: {
            image: '../../Resource/SymbolLib/System/01.png',
            horizontalOrigin: Earth.HorizontalOrigin.LEFT,
            verticalOrigin: Earth.VerticalOrigin.BOTTOM,
            color: new Earth.Color.WHITE.withAlpha(0.5),
            width: 50,
            height: 50,
            sizeInMeters: true,
          },
        });
        entityCollection.add(entity);
        this.Entitys.push(entity);
      }


      let degreesArray = [];
      for (let x of data.data) {
        degreesArray.push(...x);
      }

      const entity = new Earth.Entity({
        name: 'polyine',
        show: true,
        polyline: new Earth.Polyline({
          positions: Earth.Cartesian3.fromDegreesArray(degreesArray),
          width: 6,
          color: Earth.Color.YELLOW.withAlpha(1),
          clampToGround: true,
        }),
      });
      entityCollection.add(entity);
      this.Entitys.push(entity);


      viewer.camera.flyTo({
        destination: Earth.Cartesian3.fromDegrees(data.center[0], data.center[1], 10000.0),
        orientation: {
          heading: Earth.Math.toRadians(20.0),
          pitch: Earth.Math.toRadians(-35.0),
          roll: 0.0,
        },
      });

      const ship_move_buchang = parseInt(data.data.length / (100 * 20));
      let now_ship = 0;
      let ship_entity = null;
      const ship_interval = setInterval(function moveship() {
        if (now_ship >= data.data.length) clearInterval(ship_interval);
        else {
          if (ship_entity) {
            ship_entity.position = new Earth.Cartesian3.fromDegrees(data.data[now_ship][0], data.data[now_ship][1], 0);
          } else {
            ship_entity = new Earth.Entity({
              name: 'billboard',
              show: true,
              position: new Earth.Cartesian3.fromDegrees(data.data[now_ship][0], data.data[now_ship][1], 0),
              point: {
                pixelSize: 16,
                color: Earth.Color.BLUE.withAlpha(0),
              },
              billboard: {
                image: '/Resource/Icons/ship.jpg',
                horizontalOrigin: Earth.HorizontalOrigin.CENTER,
                verticalOrigin: Earth.VerticalOrigin.CENTER,
                heightReference: Earth.HeightReference.RELATIVE_TO_GROUND,
                rotation: 5,
                width: 80,
                height: 80,
                sizeInMeters: true, //如果想要近大远小效果，置为true
              },
            });
            entityCollection.add(ship_entity);
            this.Entitys.push(ship_entity);
          }
        }
        now_ship += ship_move_buchang;
      }, 10);
    },
    Start_Navigation(data) {
      var that = this;
      const ship_move_buchang = 1;
      console.log(data[0][0]);
      let now_ship = 0;
      let ship_entity = null;

      const ship_interval = setInterval(function moveship() {
        if (now_ship >= data.length) clearInterval(ship_interval);
        else {
          // 设置摄像机的中心点

          if (ship_entity) {
            viewer.camera.lookAtCenter(new Earth.Cartesian3.fromDegrees(data[now_ship][0], data[now_ship][1], 1000));
            ship_entity.position = new Earth.Cartesian3.fromDegrees(data[now_ship][0], data[now_ship][1], 0);
          } else {

            ship_entity = new Earth.Entity({
              name: 'billboard',
              show: true,
              position: new Earth.Cartesian3.fromDegrees(data[now_ship][0], data[now_ship][1], 0),
              point: {
                pixelSize: 16,
                color: Earth.Color.BLUE.withAlpha(0),
              },
              billboard: {
                image: '/Resource/Icons/ship.png',
                horizontalOrigin: Earth.HorizontalOrigin.CENTER,
                verticalOrigin: Earth.VerticalOrigin.CENTER,
                heightReference: Earth.HeightReference.RELATIVE_TO_GROUND,
                rotation: 5,
                width: 80,
                height: 80,
                sizeInMeters: true, //如果想要近大远小效果，置为true
              },

            });
            viewer.entities.add(ship_entity);
          }
          that.$emit("change_point_position", data[now_ship][2][0], data[now_ship][2][1]);
        }
        now_ship += ship_move_buchang;
        that.Entitys.push(ship_entity);
      }, 10);
    },
    addPlane() {
      // var data = [118.34862, 22.73022, 119.29802, 23.65292];
      // var color = Earth.Color.fromCssColorString('#7697ff').withAlpha(0.5);

      // const entity = new Earth.Entity({
      //   name: 'rectangle',
      //   rectangle: {
      //     coordinates: data, //[west,south,east,north]
      //     color: color,
      //     // heightReference: Earth.HeightReference.NONE,
      //     heightReference: Earth.HeightReference.CLAMP_TO_GROUND, //贴地
      //     height: 0.0,
      //   },
      // });

      // viewer.entities.add(entity);
      // var mid = [(data[0] + data[2]) / 2, (data[1] + data[3]) / 2];
      // var entity_text = new Earth.Entity({
      //   name: 'billboard',
      //   show: true,
      //   position: Earth.Cartesian3.fromDegrees(mid[0], mid[1], 0),
      //   label: {
      //     text: '声学基准布放区域',
      //     font: '28Px sans-serif', // 大小及字体 目前只支持大小
      //     fillColor: Earth.Color.WHITE.withAlpha(1),//可设置透明度
      //     heightReference: Earth.HeightReference.NONE,
      //     pixelOffset: new Earth.Cartesian2(10, 10),
      //   },
      // });
      // viewer.entities.add(entity_text);
    },
    addPoint() {
      var data = [
        [
          116.39598,
          18.781586
        ],
        [
          116.41517,
          18.763410
        ],
        [
          116.39617,
          18.764283
        ],
        [
          116.40655,
          18.773626
        ]
      ];

      for (var i = 0; i < data.length; i++) {
        var t = i + 1;
        var entity_text = new Earth.Entity({
          name: 'billboard',
          show: true,
          position: Earth.Cartesian3.fromDegrees(data[i][0], data[i][1], 0),
          label: {
            // text: '海底基准站' + t,
            text: '海底基准站',
            font: '28Px sans-serif', // 大小及字体 目前只支持大小
            fillColor: Earth.Color.RED.withAlpha(1),//可设置透明度
            heightReference: Earth.HeightReference.NONE,
            pixelOffset: new Earth.Cartesian2(50, 50),
          },
          circle: {
            radius: 1000,
            material: new Earth.Material({
              type: Earth.MaterialType.Diffusion,//扩散圆材质
              fabric: {
                color: Earth.Color.RED,
              }

            }),
          },
        });

        if (i == 0)
          viewer.entities.add(entity_text);
      }
    },
    addT4Plane(e) {


      var maxx = -1000;
      var maxNumber = -1000;
      var color = new Earth.Color.fromCssColorString('#DFEAFF');
      for (var i = 0; i < e.length; i++) {

        // var color = Earth.Color.fromCssColorString('#FF0000').withAlpha(0.5);
        // var number = e[i].color;
        // if (number == 1.0)
        //   color = new Earth.Color.fromCssColorString('#FF0000').withAlpha(0.8)
        // else if (number == 2.0)
        //   color = new Earth.Color.fromCssColorString('#871F78').withAlpha(0.8)
        // else if (number == 3.0)
        //   color = new Earth.Color.fromCssColorString('#00FF00').withAlpha(0.8)
        // else
        //   color = new Earth.Color.fromCssColorString('#E6E6FA').withAlpha(0.8)

        const entity = new Earth.Entity({
          name: 'rectangle',
          rectangle: {
            coordinates: e[i].coordinates, //[west,south,east,north]
            color: color,
            // heightReference: Earth.HeightReference.NONE,
            heightReference: Earth.HeightReference.CLAMP_TO_GROUND, //贴地
            height: 0.0,
          },
        });

        viewer.entities.add(entity);
        this.Gravity_entity.push(entity);

        if (e[i].origin[5] > maxNumber) {
          maxx = i;
          maxNumber = e[i].origin[5];
        }


      }


      // var mid = [(e[maxx].origin[2] + e[maxx].origin[3]) / 2, (e[maxx].origin[0] + e[maxx].origin[1]) / 2];
      // var entity_text = new Earth.Entity({
      //   name: 'billboard',
      //   billboard: {
      //     show: true, //可省略 默认是显示
      //     image: '/Resource/Icons/wujiao.png',
      //     // scale: 1.0, // default: 1.0 ，一个数字属性，指定要应用于图像尺寸的比例  //暂不支持
      //     // //像素偏移    type: Cartesian2    default:Cartesian2.ZERO
      //     // pixelOffset: new Earth.Cartesian2(200, 0), // 二维笛卡尔坐标系 //暂不支持
      //     // //眼睛偏移    type: Cartesian3    default:Cartesian3.ZERO
      //     // eyeOffset: new Earth.Cartesian3(0.0, 0.0, 0.0), // 三维笛卡尔坐标点 //暂不支持
      //     // // HorizontalOrigin CENTER 原点在对象的水平中心；LEFT 原点在对象的左侧；RIGHT 原点在对象的右侧
      //     horizontalOrigin: Earth.HorizontalOrigin.CENTER,//
      //     // // 垂直对齐方式  type: VerticalOrigin  default:VerticalOrigin.CENTER
      //     // // CENTER 原点位于中心， TOP 之间的垂直中心；BOTTOM 原点在对象的底部；
      //     verticalOrigin: Earth.VerticalOrigin.CENTER, // default: CENTER 垂直对齐位置 参考Cesium.VerticalOrigin
      //     // // NONE 位置绝对；CLAMP_TO_GROUND 位置固定在地形上；RELATIVE_TO_GROUND 位置高度是指地形上方的高度
      //     heightReference: Earth.HeightReference.RELATIVE_TO_GROUND,//暂时不支持
      //     // color: new Earth.Color.fromCssColorString('#ff88ff'), // 颜色不透明 暂不支持
      //     // // 获取或设置以弧度为单位的旋转角度  type: number  default:0
      //     rotation: 90,//暂不支持
      //     width: 80, // 用于指定广告牌的宽度（以像素为单位），并覆盖原始尺寸。
      //     height: 80, // 用于指定广告牌的高度（以像素为单位），并覆盖原始尺寸。
      //     // scaleByDistance: new Earth.NearFarScalar(1.0e3, 2.0, 2.0e3, 1.0),//不支持
      //     // disableDepthTestDistance: Number.POSITIVE_INFINITY,//不支持
      //   },

      //   show: true,
      //   position: Earth.Cartesian3.fromDegrees(mid[0], mid[1], 0),
      //   // label: {
      //   //   text: e[maxx].origin[5].toString(),
      //   //   font: '15Px sans-serif', // 大小及字体 目前只支持大小
      //   //   fillColor: Earth.Color.RED.withAlpha(1),//可设置透明度
      //   //   heightReference: Earth.HeightReference.NONE,
      //   //   pixelOffset: new Earth.Cartesian2(10, 10),
      //   // },
      // });
      // viewer.entities.add(entity_text);
      // this.Gravity_entity.push(entity_text);
    },
    removeGravityEntity() {
      for (var i = 0; i < this.Gravity_entity.length; i++) {
        viewer.entities.remove(this.Gravity_entity[i]);
      }
    }
  },
  beforeDestroy() {
    if (viewer != null) {
      console.log('Dispose start!')
      viewer.dispose();
    }
  },
}
</script>

<style>
.canvas-container {
  width: 100%;
  height: 100%;
  position: relative;
}

.canvas {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
}
</style>