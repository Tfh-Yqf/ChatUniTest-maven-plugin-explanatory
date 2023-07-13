<template>
  <div style="background-color: #2A2B2E;">
    <div>
      <div class="head-bar" >
        <Headbar :menu="menuHead"  :username="username"  @changeHead="changeHead" @fullScreen="fullScreen" />
      </div>
      <div class="home">
        <Home :currentNav="currentNav" ref="home"></Home>
      </div>
    </div>
  </div>
</template>

<script>
import menus from '@/menus.js'

function getCookie(name) {
  var cookieArr = document.cookie.split("; ");
  for (var i = 0; i < cookieArr.length; i++) {
    var cookiePair = cookieArr[i].split("=");
    if (name == cookiePair[0]) {
      return decodeURIComponent(cookiePair[1]);
    }
  }
  return null;
}

export default {
  name: 'BaseLayout',
  components: {
    Headbar: () => import('@/components/Headbar/Headbar'),
    Home: () => import('@/views/home/Home'),
  },
  provide() {
    return {
      reloadPage: () => {
        return this.reloadPage()
      },
    }
  },
  data() {
    return {
      isRouterAlive: true,
      now_head:1,
      menus_chuancna:{
        menus_single:[],
        menus_multyi:[]
      },
      screenwidth: document.body.clientWidth,
      menuHead:[],
      currentNav: '数据仿真',
    };
  },
  computed: {
    username() {
      return this.$store.state.user.username
    },
    isLogin: {
      get() {
        return this.$store.state.user.isManager;
      },
      set(val) {}
    }
  },
  methods: {
    initialize() {
      // 先拿掉登录
      this.$store.state.user.isManager = true;
      this.$store.commit('setUser',{
          user:"wentianlou",
          is_manager:true
        });
      this.menuHead = menus;
      this.isLogin = true;  
      return;
      // 自动登录
      console.log('自动登录');
      console.log(this.$store.state.user);
      var token = getCookie("token");
      console.log(token);
      if(this.$store.state.user.isManager===true||(token!=null&&token!=undefined)){

        this.$message.success('自动登录！',0.5);
        this.$store.commit('setUser',{
          user:this.username,
          is_manager:true
        });
        // this.$router.push(this.default_page.page_key);
        this.menus_head = menus;
        this.isLogin = true;
        return;
      }
      this.$store.commit('setUser',{
        user:'未登录',
        is_manager:false
      });
      this.isLogin = false;
      this.$message.warn('登录失效');
      this.$router.push('/system/login');

    },
    changeHead({e, name}){
      this.now_head = e;
      this.currentNav = name;
      var temp = menus[this.now_head].children;
      this.menus_chuancna.menus_single = [];
      this.menus_chuancna.menus_multyi = [];
      for(var i=0;i<temp.length;i++){
        if(temp[i].submenus.length==0){
          this.menus_chuancna.menus_single.push(temp[i])
        }else{
          this.menus_chuancna.menus_multyi.push(temp[i])
        }
      }
      if(this.$router.currentRoute.fullPath!=this.menus_chuancna.menus_single[0].key)
        this.$router.push(this.menus_chuancna.menus_single[0].key);
    },
    reloadPage() {
      this.isRouterAlive = false;
      this.$nextTick(() => this.isRouterAlive = true);
    },
    fullScreen({fullScreen}) {
      this.$refs.home.toggle();
    },
    getFullScreen() {
      console.log("父容器得到子容器的消息");
    }
  },
  mounted() {
    const that = this;
    this.initialize();

    window.onresize = () => {
      return (() => {
        that.screenWidth = document.body.clientWidth
      })()
    }

  },
}
</script>

<style scoped>
  .head-bar {
    background: #2A2B2E;
    padding: 0;
    height: 50px;
    position: fixed;
    z-index: 1;
    width: 100%;
  }
  .home{
    width: 100%;
    height: 100%;
    background: #2A2B2E;
  }
</style>