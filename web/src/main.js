import functions from "@/utils/functions";
import VueRouter from "vue-router";
import router from "./router";
import store from "./store";
import App from "./App.vue";
import Vue from "vue";
import Viser from "viser-vue";
import Antd from "ant-design-vue/es";

import 'ant-design-vue/dist/antd.css';
import VueFullscreen from 'vue-fullscreen'


import axios from 'axios'
Vue.prototype.$axios = axios

Vue.config.productionTip = false;
Vue.prototype.$functions = functions;
Vue.use(VueRouter);
Vue.use(Viser);
Vue.use(Antd);
Vue.use(VueFullscreen)


new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");

// Vue.prototype.server_url='http://127.0.0.1:8092';
Vue.prototype.server_url='http://172.16.100.140:30667';
// Vue.prototype.server_url='http://172.15.8.6:30034';
// Vue.prototype.server_url='http://101.42.44.6:8092';


// Vue.prototype.default_page={
//   head:2,
//   page_key:'/sound/sound_time',
//   key:2
// };

// Vue.prototype.default_page={
//   head:3,
//   page_key:'/navigation/underwater_navigation',
//   key:3
// };

Vue.prototype.default_page={
  head:1,
  page_key:'/home',
  key:1
};