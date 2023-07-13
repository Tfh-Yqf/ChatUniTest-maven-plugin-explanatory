<template>
  <div>
    <div style="float: left;">
      <a-icon class="trigger" :type="collapsed ? 'menu-unfold' : 'menu-fold'" @click="$emit('toggleCollapsed')" />
    </div>
    <div style="float: right;">


      <a-dropdown :trigger="['click']">
        <span class="trigger">
          {{email}}
          <a-icon type="down" />
        </span>
        <a-menu slot="overlay">

          <a-menu-item @click="logout">
            <span>LoginOut</span>
          </a-menu-item>
        </a-menu>
      </a-dropdown>
    </div>
  </div>
</template>

<script>

  import Cookies from 'js-cookie';

  export default {
    name: 'Headbar',
    props: ['collapsed', 'username'],
    inject: ['reloadPage'],
    data() {
      return {


      };
    },
    computed:{
      email(){
        return this.$store.state.user.email
      }
    },
    methods: {

      logout() {
        Cookies.remove('access');
        Cookies.remove('refresh');
        this.$router.push('/user/login');
      },

    },
    mounted() {

    },
  }
</script>

<style scoped>
  .trigger {
    font-size: 18px;
    line-height: 64px;
    padding: 0 24px;
    cursor: pointer;
    transition: color 0.3s;
  }

  .trigger:hover {
    color: #1890ff;
  }
</style>