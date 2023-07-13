// 测试环境配置
// module.exports = {
//     backendServiceUrl: "http://101.42.44.6:8090", // java 后端服务地址
//     imageryProviderUrl: "https://webst03.is.autonavi.com/appmaptile?style=6&z={z}&y={y}&x={x}", // 地图服务地址
//     assetsServiceUrl: "http://localhost:8081/", // 资源地址
// }

// 自己的云服务器
module.exports = {
    backendServiceUrl: "http://101.42.44.6:8090", // java 后端服务地址
    imageryProviderUrl: "https://webst03.is.autonavi.com/appmaptile?style=6&z={z}&y={y}&x={x}", // 地图服务地址
    assetsServiceUrl: "http://101.42.44.6:30569/", // 资源地址
}


//生产环境配置
// module.exports = {
//     backendServiceUrl: "http://172.16.100.140:30667", // java 后端服务地址
//     // imageryProviderUrl: "http://172.16.100.104:18081/geoimage/tileapi/tms/1.0.0/global_img_4326-BJ@EPSG:4326@jpeg/{z}/{x}/{y}.png", // 地图服务地址
//     imageryProviderUrl: "http://172.16.100.104:18081/geoimage/tileapi/tms/1.0.0/global_img_4326-BJ@EPSG:4490@png/{z}/{x}/{y}.png", // 地图服务地址
//     assetsServiceUrl: "http://172.16.100.140:30569/", // 资源地址
// }

// 跳转客户端用法，把跳转链接置为下面的地址，如window.location=
// UnderWaterNav://test
// 原理是客户端已经把地址注册到浏览器所在环境（Win，Linux）的注册列表中，可以直接使用
