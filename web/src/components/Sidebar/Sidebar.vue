<template>
  <div style="position: fixed;width: 256px;background-color: #FFFFFF;margin-top: 10px" :style="{height:screenHeight+'px'}">


    <div style="display: flex;flex-direction: column;color: black;color: #000000;font-size: 15px;margin-top: 10px;width: 100%;height: 100vh">
      <div style="width: 100%;display: flex;align-items: center;"
           v-for="(item,index) in menus.menus_single" :key="item.key"
           @click="change_page(item.key)"
           @mouseleave="mouseleave(item)"
           @mouseover="mouseover(item)"
           :class="{'active':selectedKeys[0]==item.key}">
        <div style="width: 90%;display: flex;align-items: center;height: 60px;" :class="{'xuanting':xuanting.key==item.key}">
          <a-icon :type="item.icon" style="margin-left: 25px"></a-icon>
          <div style="margin-left: 10px;font-weight: 500">{{item.name}}</div>
        </div>

        <div v-if="selectedKeys[0]==item.key"  style="height: 30px;width: 3.5px;background-color: #5292f1;border-radius: 10px;margin-left: 0px;"></div>
      </div>
      <div v-for="(item,index) in menus.menus_multyi" :key="item.key"
           @click="change_page(item)"
           @mouseleave="mouseleave(item)"
           @mouseover="mouseover(item)"
           :class="{'active':selectedKeys[0]==item.key}">
        <div style="width: 100%;display: flex;align-items: center;height: 60px" >
          <div style="width: 80%;display: flex;align-items: center" :class="{'xuanting':xuanting.key==item.key}">
            <a-icon :type="item.icon" style="margin-left: 25px"></a-icon>
            <div style="margin-left: 10px;font-weight: 500">{{item.name}}</div>
          </div>

          <img :src="array" width="10" style="left: 50px" />

          <div v-if="selectedKeys[0]==item.key"  style="height: 27px;width: 3px;background-color: #5292f1;margin-left: 30px;border-radius: 20px;flex-shrink: 0;"></div>
        </div>
      </div>
    </div>



  </div>
</template>

<script>


  export default {
    name: 'Sidebar',
    props: ['menus'],
    data() {
      return {
        openKeys: [],
        selectedKeys: [this.default_page.page_key],
        array:require('@/assets/xiajiantou.png'),
        screenHeight: document.body.clientHeight,
        xuanting:{key:'NULL'}
      };
    },
    methods: {
      initialize() {
        this.changeRoute(this.$route.path);
      },
      change_page(path){

        if(this.selectedKeys[0]!=path&&this.$router.currentRoute.fullPath!=path)
          this.$router.replace(path);
      },
      changeRoute(path) {
        this.selectedKeys = [path];
        console.log('当前选中页面路径'+this.selectedKeys);

          // for (let m of menus) {
          //   if (m.submenus.findIndex(item => item.key == path) != -1) {
          //     this.openKeys = [m.key];
          //     return
          //   }
          // }
        },
      mouseover(e){
        this.xuanting = e;
      },
      mouseleave(e){
        this.xuanting = {key:'NULL'};
      },
    },

    watch: {
      $route(to) {
        console.log('change_route to:'+to.path)
        this.changeRoute(to.path);
      },
    },
    created() {
      this.initialize();
    },
  }
</script>

<style scoped>


  .active{
    color:#4590ff;
    /*background-color: #1da9bc;*/
  }

  .xuanting{
    background-color: #ecf5ff;
    border-radius: 10px;
  }
</style>