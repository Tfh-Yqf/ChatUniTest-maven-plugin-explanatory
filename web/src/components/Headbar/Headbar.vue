<template>
  <div class="top-bar">

    <div class="title-area">
      <img :src="logo" width="45" style=" margin-left: 8px;" />
      <div class="title-block">
        <div class="title-txt-zh">智慧地球系统</div>
        <div class="title-txt-pin">ZHIHUIDIQIUXITONG</div>
      </div>
      <div class="name-bg">
        <div class="name-txt">海洋水下导航</div>
      </div>
    </div>

    <div class="menu-area">
      <div class="menu-content">
        <div v-for="(item, index) in menu" :key="item.key" class="menu-group">
          <div @click="changeHead(item, index)">
            <div v-if="nowhead == item.key" class="menu-item-selected">{{ item.name }}</div>
            <div v-if="nowhead != item.key" class="menu-item">{{ item.name }}</div>
          </div>

        </div>
      </div>
    </div>

    <div class="time-container">
      <div class="time-area">
        <div class="time-name">天文时间:</div>
        <div class="time-show">
          <div class="time-detail">{{nowDate}}</div>
        </div>
        <div class="time-name">作战时间:</div>
        <div class="time-show">
          <div class="time-detail">{{nowDate}}</div>
        </div>
        <img @click="fullScreen()" class="screen-icon" src="@/assets/fullscreen.png">

      </div>
    </div>

  </div>
</template>

<script>
import Cookies from 'js-cookie';


export default {
  name: 'Headbar',
  computed: {
  },
  props: ['username', 'menu'],
  inject: ['reloadPage'],
  data() {
    return {
      logo: require('@/assets/img/head-bar/logo.png'),
      abount: require('@/assets/img/head-bar/abount.png'),
      mouseMenu: { key: 'NULL' },
      nowhead: '',
      nowDate:''
    };
  },
  methods: {
    freshTime(){
      const now = new Date();

      const year = now.getFullYear();
      const month = String(now.getMonth() + 1).padStart(2, '0');
      const day = String(now.getDate()).padStart(2, '0');
      const hours = String(now.getHours()).padStart(2, '0');
      const minutes = String(now.getMinutes()).padStart(2, '0');
      const seconds = String(now.getSeconds()).padStart(2, '0');

      const formattedDateTime = `${year}/${month}/${day}   ${hours}:${minutes}:${seconds}`;

      this.nowDate = formattedDateTime;
    },
    mouseover(e) {
      this.mouseMenu = e;
    },
    mouseleave(e) {
      this.mouseMenu = { key: 'NULL' };
    },
    changeHead(item, e) {
      this.nowhead = item.key;
      this.$emit('changeHead', {e, name: item.name});
    },
    initData() {
      // this.nowhead = "";
    },
    logout() {
      Cookies.remove('access');
      Cookies.remove('refresh');
      Cookies.remove('isManager');
      this.$router.push('/system/login');
    },
    fullScreen() {
      this.$store.state.system.fullScreen = !this.$store.state.system.fullScreen;
      this.$emit('fullScreen', {fullScreen: this.$store.state.system.fullScreen});
    }
  },
  mounted() {
    this.initData();
    var self = this;
    setInterval(function (){{self.freshTime()}},1000);
  },
}
</script>

<style scoped>

.top-bar{
  display: flex;
  flex-direction: row;
  height: 66px;
  background-size: 100% 100%;
  width: 100%;
  background-image: url("../../assets/img/head-bar/bar_bac.png");
  background-color: #000;
}

.title-area {
  font-weight: bold;
  height: 64px;
  margin-left: 15px;
  display: flex;
  flex-direction: row;
  align-items: center;
  flex-shrink: 0;
  height: 100%;
  width: 27%
}

.title-block{
  display: flex;
  color: #FFFFFF;
  flex-direction: column;
  margin-left: 15px;
  height: 100%;
  opacity: 0.9
}

.title-txt-zh {
  font-size: 28px;
  font-weight: lighter;
  line-height: 60px;
}
.title-txt-pin{
  font-size: 12px;
  letter-spacing: 3.1px;
  opacity: 0.8;
  text-align: center;
  line-height: 12px;
  font-weight: 400;
  margin-top: -15px;
}
.name-bg{
  margin-left: 10px;
  color: #fff;
  background-image: url("../../assets/img/head-bar/name_bg.png");
  background-size: 100% 100%;
  height: 40px;
}
.name-txt {
  font-size: 19px;
  margin-left: 10px;
  opacity: 0.8;
  line-height: 30px;
}
.menu-area {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 30%;
  flex-shrink: 0
}
.menu-content {
  display: flex;
  flex-direction: row;
  height: 55px;
  align-items: center;
}

.menu-group {
  font-size: 18px;
  color: white;
  height: 100%;
  width: 25%;
  text-align: center;
  vertical-align: center;
  font-weight: normal;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.menu-item{
  height: 30px;
  line-height: 30px;
}

.menu-item-selected{
  height: 30px;
  line-height: 30px;
  background-image: url("../../assets/img/head-bar/menu-item-select.png");
}

.time-container {
  display: flex;
  flex-direction: column;
  width: 50%;
  height: 100%;
  padding-left: 4%;
  margin-top: -5px;
}
.time-area {
  display: flex;
  flex-direction: row;
  height: 95%;
}

.time-name {
  margin-left: 5px;
  font-size: 16px;
  color: white;
  font-weight: normal;
  letter-spacing: 0.86px;
  display: flex;
  flex-direction: column-reverse;
  justify-content: center;
}
.time-show{
  width: 160px;
  color: #DDF7FF;
  font-size: 16px;
  font-weight: normal;
  margin-left: 5px;
  margin-right: 5px;
  display: flex;
  flex-direction: column-reverse;
  justify-content: center;
}
.time-detail {
  padding-left: 5px;
  background-image: linear-gradient(180deg, #6AC2D6 0%, #388FAC 100%);
  height: 30px;
  width: 100%;
  border-radius: 2px;
  line-height: 30px;
}
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

.select {
  color: #1890ff;
  font-weight: bolder;
  font-size: 17px;
  display: flex;
  flex-direction: column;
  height: 50px;
  justify-content: space-between;
  margin-left: 20px;
}

.no_select {
  color: black;
  font-weight: bolder;
  font-size: 17px;
  display: flex;
  flex-direction: column;
  height: 50px;
  justify-content: space-between;
  margin-left: 20px;
}
.screen-icon {
  margin-top: 15px;
  margin-left: 5px;
  width: 30px;
  height: 30px;
}
</style>