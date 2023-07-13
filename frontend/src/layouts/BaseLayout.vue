<template>
  <div>
    <a-layout>
      <a-layout-sider class="sidebar" width="256" v-model="collapsed" :trigger="null" collapsible>
        <sidebar :collapsed="collapsed" />
      </a-layout-sider>

      <a-layout v-if="isLogin">
        <a-layout-header class="headbar">
          <headbar :collapsed="collapsed" :username="username" @toggleCollapsed="toggleCollapsed" />
        </a-layout-header>

        <a-layout-content>
          <router-view v-if=" isRouterAlive" style="padding: 8px;" />
          <a-result v-else status="403" title="403" sub-title="抱歉，您无权访问此页面" style="margin-top: 36px;" />
        </a-layout-content>
      </a-layout>
    </a-layout>
  </div>
</template>

<script>

  import { getInfo } from '@/api/user'
  import Cookies from 'js-cookie';


  export default {
    name: 'BaseLayout',
    components: {
      Headbar: () => import('@/components/Headbar/Headbar'),
      Sidebar: () => import('@/components/Sidebar/Sidebar'),
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
        collapsed: false,
        isRouterAlive: true
      };
    },
    computed: {
      username() {
        return this.$store.state.user.username
      },
      isLogin(){
        if (!Cookies.get('token') )
          return false;
        else
          return true;
      }
    },
    methods: {
      initialize() {
        if (!Cookies.get('token') ) {
          return this.$router.push('/user/login');
        }
        var data = {
          token:Cookies.get('token'),
          email:Cookies.get('email')
        };
        this.$store.commit('setUser',data);
      },
      toggleCollapsed() {
        this.collapsed = !this.collapsed;
      },
      reloadPage() {
        // this.isRouterAlive = false;
        this.$nextTick(() => this.isRouterAlive = true);
      },
    },
    mounted() {
      this.initialize();
    },
  }
</script>

<style scoped>
  .headbar {
    background: #fff;
    padding: 0;
    box-shadow: 0px 1px 10px #7774;
  }

  .sidebar {
    background: #fff;
    overflow: auto;
    box-shadow: 1px 0px 10px #7774;
  }
</style>