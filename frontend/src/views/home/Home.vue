<template>
  <div style="width: 100%;display: flex;justify-content: center;">

    <div style="padding: 10px;width: 100%;height: 600px;background-color: white;display: flex;flex-direction: column;align-items: center">
      <a href="https://github.com/ZJU-ACES-ISE/ChatUniTest" style="font-size: 30px;font-weight: bolder;margin-top: 40px">ChatUniTest</a>
      <div style="font-size: 20px;font-weight: bolder">A ChatGPT-based unit test generation tool</div>

      <a-menu theme="light" mode="horizontal" :selectedKeys="now_menu_keys" @click="change_menu">
        <a-menu-item v-for="(item,index) in target_menu" :key="item.key">{{item.name}}</a-menu-item>
      </a-menu>

      <div style="display: flex;align-items: center;margin-top: 20px;height: 50px  ">
        <div style="font-size: 17px;flex-shrink: 0;width: 120px;color: #1890ff">Project name:</div>
        <a-input v-model="name" style="height: 50px;width: 400px"></a-input>
      </div>


      <div style="width:800px;height: 180px;margin-top: 40px">
        <a-upload-dragger action="http://127.0.0.1:8081/upload" :multiple="false" accept=".zip" @change="uploadFile">
          <p className="ant-upload-drag-icon">
            <img :src="inbox" style="width: 70px;height: 70px">
          </p>
          <p style="font-size: 17px;margin-top: 10px;color: black" >点击或将文件拖拽到此区域上传</p>
          <p style="font-size: 15px;color: gray">支持单个或批量上传，严禁上传公司内部资料及其他违禁文件</p>
        </a-upload-dragger>
      </div>
      <a-popconfirm title="确定执行吗?" @confirm="excute">
        <a-button v-if="now_menu_keys[0]==0" type="primary" style="width: 300px;height: 50px;margin-top: 50px;font-size: 20px">Start Generate</a-button>
        <a-button v-else type="primary" style="width: 300px;height: 50px;margin-top: 50px;font-size: 20px">Start Predict</a-button>
      </a-popconfirm>

    </div>
  </div>
</template>

<script>

import {startProject} from '@/api/project'

export default {
  components: {

  },
  data() {
    return {
      inbox: require("@/assets/inbox.png"),
      now_menu_keys:[0],
      target_menu:[
        {key:0,name:'Generate the whole project'},
        {key:1,name:'Predict the consumed tokens'}
      ],
      name:'',
      file:null
    };
  },
  methods: {
    change_menu(e){
      console.log(e);
      this.now_menu_keys= [e.key];
    },
    uploadFile(e){
      console.log(e);
      this.file = e;
    },
    excute(){
      console.log(this.file)
      if(this.name.length==0){
        this.$message.error('Project name is null')
        return;
      }
      if(this.file==null){
        this.$message.error('Project is not upload')
        return;
      }


      if(this.now_menu_keys[0]==0){
        var data = {
          name:this.name,
          email:this.$store.state.user.email,
          dirName:this.file.file.name
        }

        startProject(data).then((res)=>{
          console.log(res);
        }).finally((res)=>{
          this.$message.success('执行中');

        })
      }else{
        var data = {};
      }
    }
  },
  mounted() {

  },
};
</script>

<style >

</style>
