<template>
  <div style="display: flex;flex-direction: column" class="canvas-container">

    <canvas ref="canvas" id="canvas" class="canvas" style="margin-top: 10px"></canvas>
  </div>
</template>


<script>
import config from "@/utils/config"

var viewer = null;

export default {
  name: 'GravityMap',
  data() {
    return {
      viewer: null,
      guiji: []
    }
  },
  mounted() {
    this.init();

  },
  props: ['data', 'matrix'],
  methods: {
    init() {
      var that = this;
      if (window.Module == undefined || window.Module == null) {
        createEarthModule().then(function () {
          viewer = new Earth.Viewer('canvas', {
            sceneMode: Earth.SceneMode.SCENE3D, //Earth.SceneMode.SCENE2D
            center: [138.841667, 28.841667], //初始中心点
            zoom: 6, //初始层级
            cameraSmooth: false, //是否开启相机缓冲效果
            imageryProvider: new Earth.UrlTemplateImageryProvider({
              url: config.imageryProviderUrl,
              // epsg:4326,
              // levelOffset:-1,
              // tms:true
              // url: 'http://172.16.100.102:9000/wmts?tilematrix={z}&layer=wmts_ifile&style=wmts_satellite&tilerow={y}&tilecol={x}&tilematrixset=Global_ifile&format=image%2Fjpeg&service=WMTS&version=1.0.0&request=GetTile',
              // url:'http://172.16.100.103:9000/mapboxsat/maps?lyrs=i&x={x}&y={y}&z={z}',
            }),
            terrainProvider: new Earth.EngineTerrainProvider({
              // url: 'https://pie-engine-test.s3.cn-northwest-1.amazonaws.com.cn/earthdata/mapdata/sichuandem/_alllayers/{z}/{y}/{x}.terrain',
              // url: 'http://172.16.100.102:9000/wmts?tilematrix={z}&layer=wmts_ifile&style=wmts_satellite&tilerow={y}&tilecol={x}&tilematrixset=Global_ifile&format=image%2Fjpeg&service=WMTS&version=1.0.0&request=GetTile',
            }),
          });

          let argisImageryProvider = new Earth.ArcGisMapServerImageryProvider({
            // url:'https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}',
            // url: 'http://172.16.100.102:9000/wmts?tilematrix={z}&layer=wmts_ifile&style=wmts_satellite&tilerow={y}&tilecol={x}&tilematrixset=Global_ifile&format=image%2Fjpeg&service=WMTS&version=1.0.0&request=GetTile',
            
          });
          const layer = viewer.imageryLayers.addImageryProvider(argisImageryProvider);

          let cityGMLDataSource = viewer.dataSources.add(
            Earth.CityGMLDataSource.load('https://engine.piesat.cn/earthweb/docsPlus/Demo/data/cityGML/citygml.xml', {
              resourceUrl: 'https://engine.piesat.cn/earthweb/docsPlus/Demo/data/cityGML/',
              name: 'cityGML',
              scale: 0.62
            })
          );

          const gsfPolygon = viewer.dataSources.add(
            Earth.GSFDataSource.load('./data/Provinces_R.gsf', {
              fill: Earth.Color.GREEN.withAlpha(0.3),
              stroke: Earth.Color.RED,
              strokeWidth: 1,
            })
          );

          that.addPlane(this.data);
          that.add_kuang(this.matrix);
          this.change_camera_to(this.matrix);
        });
      } else {
        viewer = new Earth.Viewer('canvas', {
          sceneMode: Earth.SceneMode.SCENE3D, //Earth.SceneMode.SCENE2D
          center: [138.841667, 28.841667], //初始中心点
          zoom: 6, //初始层级
          cameraSmooth: false, //是否开启相机缓冲效果
          imageryProvider: new Earth.UrlTemplateImageryProvider({
            url: config.imageryProviderUrl,
            // epsg:4326,
            // levelOffset:-1,
            // tms:true
            // url: 'http://172.16.100.102:9000/wmts?tilematrix={z}&layer=wmts_ifile&style=wmts_satellite&tilerow={y}&tilecol={x}&tilematrixset=Global_ifile&format=image%2Fjpeg&service=WMTS&version=1.0.0&request=GetTile',
            // url:'http://172.16.100.103:9000/mapboxsat/maps?lyrs=i&x={x}&y={y}&z={z}',
          }),
          // terrainProvider: new Earth.EngineTerrainProvider({
          //   url:'https://pie-engine-test.s3.cn-northwest-1.amazonaws.com.cn/earthdata/mapdata/sichuandem/_alllayers/{z}/{y}/{x}.terrain',
          //   // url: 'http://172.16.100.102:9000/wmts?tilematrix={z}&layer=wmts_ifile&style=wmts_satellite&tilerow={y}&tilecol={x}&tilematrixset=Global_ifile&format=image%2Fjpeg&service=WMTS&version=1.0.0&request=GetTile',
          //   }),
        });
        // let argisImageryProvider = new Earth.ArcGisMapServerImageryProvider({
        //   url:'https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}',
        //   // url: 'http://172.16.100.102:9000/wmts?tilematrix={z}&layer=wmts_ifile&style=wmts_satellite&tilerow={y}&tilecol={x}&tilematrixset=Global_ifile&format=image%2Fjpeg&service=WMTS&version=1.0.0&request=GetTile',
        // });
        // const layer = viewer.imageryLayers.addImageryProvider(argisImageryProvider);

        // let cityGMLDataSource = viewer.dataSources.add(
        //   Earth.CityGMLDataSource.load('https://engine.piesat.cn/earthweb/docsPlus/Demo/data/cityGML/citygml.xml', {
        //     resourceUrl: 'https://engine.piesat.cn/earthweb/docsPlus/Demo/data/cityGML/',
        //     name: 'cityGML',
        //     scale: 0.62
        //   })
        // );

        // const gsfPolygon = viewer.dataSources.add(
        //   Earth.GSFDataSource.load('https://engine.piesat.cn/earthweb/docsPlus/Demo/data/Provinces_R.gsf', {
        //     fill: Earth.Color.GREEN.withAlpha(0.3),
        //     stroke: Earth.Color.RED,
        //     strokeWidth: 1,
        //     height: 200
        //   })
        // );

        this.addPlane(this.data);
        this.add_kuang(this.matrix);
        this.add_big_plane(this.matrix);
        this.change_camera_to(this.matrix);
      }
    },
    change_camera_to(e) {
      let bounds = { a: 9999, b: 0, c: 9999, d: 0 };
      e.forEach(element => {
        bounds.a = Math.min(bounds.a, element[0]);
        bounds.b = Math.max(bounds.b, element[0]);
        bounds.c = Math.min(bounds.c, element[1]);
        bounds.d = Math.max(bounds.d, element[1]);
      });
      console.log(bounds);
      // 摄像机移动
      viewer.camera.flyTo({
        destination: Earth.Bounds.fromDegrees(bounds.a, bounds.c, bounds.b, bounds.d),
        // destination: Earth.Bounds.fromDegrees(110.0922, 20.0323, 123.3752, 24.5910),
        orientation: {
          heading: Earth.Math.toRadians(20.0),
          pitch: Earth.Math.toRadians(-35.0),
          roll: 0.0,
        },
      });
    },
    addPlane(e) {
      var maxx = -1000;
      var maxNumber = -1000;
      for (var i = 1; i < e.length; i++) {
        const entity = new Earth.Entity({
          name: 'rectangle',
          rectangle: {
            coordinates: e[i].coordinates, //[west,south,east,north]
            color: new Earth.Color.fromCssColorString('#DFEAFF'),
            // heightReference: Earth.HeightReference.NONE,
            heightReference: Earth.HeightReference.CLAMP_TO_GROUND, //贴地
            height: 0.0,
          },
        });

        viewer.entities.add(entity);

        if (e[i].origin[5] > maxNumber) {
          maxx = i;
          maxNumber = e[i].origin[5];
        }

        // var max_longitude = e[i].origin[1];
        // var min_longitude = e[i].origin[0];

        // var min_latitude = e[i].origin[2];
        // var max_latitude = e[i].origin[3];

        // var matrix = [[max_latitude, max_longitude], [max_latitude, min_longitude], [min_latitude, min_longitude], [min_latitude, max_longitude]];
        // this.add_line(matrix)
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


    },
    add_big_plane(e){
      var FanWei = [e[2][0],e[2][1],e[0][0],e[0][1]];
      const EntityBigPlane = new Earth.Entity({
        name: 'rectangle',
        rectangle: {
          coordinates: FanWei, //[west,south,east,north]
          color: new Earth.Color.fromCssColorString('#945D2B').withAlpha(0.8),
          // heightReference: Earth.HeightReference.NONE,
          heightReference: Earth.HeightReference.CLAMP_TO_GROUND, //贴地
          height: 0.0,
        },
      });

      viewer.entities.add(EntityBigPlane);
    },
    add_kuang(e) {
      // 处理数据
      const globe = viewer._pieGlobe;
      const spatialReference = globe.getSpatialReference();
      const datasource = new Earth.RenderableDataSource();
      viewer.dataSources.add(datasource)


      const points = [];
      const arrGeo = e;

      for (let i = 0; i < arrGeo.length; i++) {
        const pointSR = spatialReference.forward(arrGeo[i])
        points.push(pointSR[0])
        points.push(pointSR[1])
      }

      const geoPolyline = new Earth.GeoPolyline({ points: points, ID: 1 })
      const renderPolyLine = new Earth.RenderPolyline({
        polyline: geoPolyline,
        color: new Earth.Color.fromCssColorString('#0070C0'),
        width: 5
      });
      datasource.add(renderPolyLine);


    },
    add_line(e) {
      const globe = viewer._pieGlobe;
      const spatialReference = globe.getSpatialReference();

      const points = [];
      const arrGeo = e;

      for (let i = 0; i < arrGeo.length; i++) {
        const pointSR = spatialReference.forward(arrGeo[i])
        points.push(pointSR[0])
        points.push(pointSR[1])
      }

      const entity = new Earth.Entity({
        name: 'polyine',
        show: true,
        polyline: new Earth.Polyline({
          // show: false,
          positions: Earth.Cartesian3.fromDegreesArray(points),
          width: 1,
          color: Earth.Color.YELLOW.withAlpha(1),
          // clampToGround: false, //不贴地
          clampToGround: true, //贴地
        }),
      });
      viewer.entities.add(entity);

      var FanWei = e;
      const EntityBigPlane = new Earth.Entity({
        name: 'rectangle',
        rectangle: {
          coordinates: FanWei, //[west,south,east,north]
          color: new Earth.Color.fromCssColorString('#DFEAFF'),
          // heightReference: Earth.HeightReference.NONE,
          heightReference: Earth.HeightReference.CLAMP_TO_GROUND, //贴地
          height: 0.0,
        },
      });

      viewer.entities.add(EntityBigPlane);
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