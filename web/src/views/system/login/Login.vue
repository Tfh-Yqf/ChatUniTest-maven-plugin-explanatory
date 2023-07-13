<template>
  <div v-if="!is_zhuce">
    <div style="">
      <div>
        <a-form-model ref="form" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 14 }">
          <a-form-model-item prop="user" label="用户名">
            <a-input size="large" v-model="form.user" style="margin-left: 10px" />
          </a-form-model-item>
          <a-form-model-item prop="password" label="密码">
            <a-input-password size="large" v-model="form.password" style="margin-left: 10px"/>
          </a-form-model-item>
        </a-form-model>
      </div>

      <a-row>
        <a-col :span="14" offset="5">
          <a-button type="primary" size="large" :loading="isLoading" style="width: 100%;" @click="login">登录</a-button>
        </a-col>
      </a-row>

      <div @click="change_zhuce" style="display: flex;flex-direction: row;width: 100%;justify-content: center;margin-top: 20px;">
        <a style="font-size: 15px">没有账户？去注册</a>
      </div>

    </div>
  </div>
  <div v-else style="display: flex;flex-direction: column;">
    <div style="display: flex;flex-direction: row;width: 70%;margin-left: 10%;align-items: center">
      <a style="width: 100px;">用户名:</a>
      <a-input size="large" v-model="reg.user" />
    </div>

    <div style="display: flex;flex-direction: row;width: 70%;margin-left: 10%;align-items: center;margin-top: 20px;">
      <a style="width: 100px;">邮箱:</a>
      <a-input size="large" v-model="reg.email" />
    </div>

    <div style="display: flex;flex-direction: row;width: 70%;margin-left: 10%;align-items: center;margin-top: 20px">
      <a style="width: 100px;">验证码:</a>
      <div style="display: flex;flex-direction: row;margin-left: -8px;">
        <a-input size="large" v-model="reg.code" />
        <a-button v-if="!is_send" @click="send_email" type="primary" size="large"  style="width: 100px;margin-left: 20px;" >发送</a-button>
        <a-button v-else type="primary" size="large"  style="width: 100px;margin-left: 20px;background-color: #FFFFFF;color:black;" >{{second}}</a-button>
      </div>

    </div>

    <div style="display: flex;flex-direction: row;width: 70%;margin-left: 10%;align-items: center;margin-top: 20px">
      <a style="width: 100px;">密码:</a>
      <a-input size="large" v-model="reg.password" />
    </div>
    <div style="width: 100%;display: flex;flex-direction: row;justify-content: center;margin-top: 40px;">
      <a-button type="primary" size="large" :loading="isLoading" style="width: 200px;" @click="register_user">注册</a-button>
    </div>

    <div @click="change_zhuce" style="display: flex;flex-direction: row;width: 100%;justify-content: center;margin-top: 20px;">
      <a style="font-size: 15px">已有账户？去登录</a>
    </div>

    <div  style="display: flex;flex-direction: row;width: 100%;justify-content: center;margin-top: 20px;color: red">
      <a style="font-size: 15px;color: red">{{warn}}</a>
    </div>
  </div>
</template>

<script>
import { reg,sendemail,login } from '@/api/user';



export default {
  name: 'Login',
  data() {
    return {
      isLoading: false,
      form: {
        user: '',
        password: '',
      },
      reg:{
        email:'',
        password:'',
        code:'',
        user:'',
        phone:''
      },
      rules: {
      },
      is_zhuce:false,
      second:60,
      is_send:false,
      warn:'',
      code_interval:null
    };
  },
  mounted() {
    // this.$store.commit('setUser',{
    //   user:'admin',
    //   is_manager:true
    // });
    // this.$message.success('登录成功！');
    // this.$router.push('/home');
  },
  methods: {
    send_email(){
      if(this.reg.email==''){
        this.$message.warn('邮箱不能为空！');
        return;
      }
      sendemail(this.reg.email).then(res=>{
        console.log(res);
        if(res.code!=0||res.code==undefined){
          this.$message.error(res.message||'错误');
          return;
        }else{
          this.$message.success('发送成功');
          this.is_send=true;
          this.second=60;
          var that = this;
          this.code_interval = setInterval(function (){
            if(that.second==0){
              that.is_send=false;
              clearInterval(that.code_interval);
            }else{
              that.second--;
            }
          },1000);
        }
      }).finally((res)=>{

      });
    },
    register_user(){
      if(this.reg.email==''||this.reg.code==''||this.reg.password==''||this.reg.user==''){
        this.$message.warn('未填写完整');
        return;
      }
      this.isLoading=true;
      reg(this.reg).then(res => {
        console.log(res);
        if(res.code==0){
          this.$message.success('注册成功');
          this.change_zhuce();
        }else{
          this.$message.warn(res.message||'错误');
          this.reg.code='';
        }
      }).finally((res) => {
        this.isLoading=false;
      });
    },
    change_zhuce(){
      this.is_zhuce=!this.is_zhuce;
    },
    initialize() {

    },
    login() {
      // test

      // this.$store.commit('setUser',{
      //   user:this.form.user,
      //   is_manager:true
      // });
      // this.$message.success('登录成功！');
      // this.$router.push('/home');


      if(this.form.user==''||this.form.password==''){
        this.$message.warn('未填写完整！');
        return;
      }
      this.isLoading=true;
      login(this.form).then((res)=>{
        console.log(res);
        if(res.code!=0||res.code==undefined){
          this.$message.error(res.message||'密码错误！');
        }else{
          this.$store.commit('setUser',{
            user:this.form.user,
            is_manager:true
          });
          document.cookie = "token=" + res.token;

          this.$message.success('登录成功！');
          this.$router.push('/home/home');
        }
      }).finally(()=>{
        this.isLoading=false;
      })
    },
  }
}
</script>

<style>
</style>