<template>
  <div v-if="!is_zhuce">
    <div style="">
      <div>
        <a-form-model ref="form" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 14 }">
          <a-form-model-item prop="email" label="Email">
            <a-input size="large" v-model="form.email" />
          </a-form-model-item>
          <a-form-model-item prop="password" label="Password">
            <a-input-password size="large" v-model="form.password" />
          </a-form-model-item>
        </a-form-model>
      </div>

      <a-row>
        <a-col :span="14" offset="5">
          <a-button type="primary" size="large" :loading="isLoading" style="width: 100%;" @click="login">Login</a-button>
        </a-col>
      </a-row>

      <div @click="change_zhuce" style="display: flex;flex-direction: row;width: 100%;justify-content: center;margin-top: 20px;">
        <a style="font-size: 15px">Dont have a account？Register</a>
      </div>

    </div>
  </div>
  <div v-else style="display: flex;flex-direction: column;">


    <div style="display: flex;flex-direction: row;width: 70%;margin-left: 10%;align-items: center;margin-top: 20px;">
      <a style="width: 100px;">Email:</a>
      <a-input size="large" v-model="reg.email" />
    </div>

    <div style="display: flex;flex-direction: row;width: 70%;margin-left: 10%;align-items: center;margin-top: 20px">
      <a style="width: 100px;">Code:</a>
      <div style="display: flex;flex-direction: row;margin-left: 5px;">
        <a-input size="large" v-model="reg.code" />
        <a-button v-if="!is_send" @click="send_email" type="primary" size="large"  style="width: 100px;margin-left: 20px;" >SendCode</a-button>
        <a-button v-else type="primary" size="large"  style="width: 100px;margin-left: 20px;background-color: #FFFFFF;color:black;" >{{second}}</a-button>
      </div>

    </div>

    <div style="display: flex;flex-direction: row;width: 70%;margin-left: 10%;align-items: center;margin-top: 20px">
      <a style="width: 100px;">Password:</a>
      <a-input size="large" v-model="reg.password" />
    </div>
    <div style="width: 100%;display: flex;flex-direction: row;justify-content: center;margin-top: 40px;">
      <a-button type="primary" size="large" :loading="isLoading" style="width: 200px;" @click="register_user">注册</a-button>
    </div>

    <div @click="change_zhuce" style="display: flex;flex-direction: row;width: 100%;justify-content: center;margin-top: 20px;">
      <a style="font-size: 15px">Go to login</a>
    </div>

    <div  style="display: flex;flex-direction: row;width: 100%;justify-content: center;margin-top: 20px;color: red">
      <a style="font-size: 15px;color: red">{{warn}}</a>
    </div>
  </div>
</template>

<script>
  import { login,reg,sendemail } from '@/api/user';
  import Cookies from 'js-cookie';

  export default {
    name: 'Login',
    data() {
      return {
        isLoading: false,
        form: {
          number: '1',
          email: '',
          password: '',
        },
        reg:{
          email:'',
          password:'',
          code:'',
        },
        rules: {
          email: [
            { required: true, message: '请输入用户名', trigger: 'change' },
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'change' },
          ],
        },
        is_zhuce:false,
        second:60,
        is_send:false,
        warn:'',
        code_interval:null
      };
    },
    mounted() {

    },
    methods: {
      send_email(){
        if(this.reg.email==''){
          this.warn = '邮箱不能为空！';
          return;
        }
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
        var data = {
          email:this.reg.email
        }

        sendemail(data).then(data=>{
          console.log(data);
        }).finally((res)=>{
          console.log(res);
        });
      },
      register_user(){
        if(this.reg.email==''||this.reg.code==''||this.reg.password==''||this.reg.email==''){
          this.warn = '未填写完整';
          return;
        }
        var that = this;
        var data = {
          email:this.reg.email,
          password:this.reg.password,
          code:this.reg.code
        }
        reg(data).then(data => {
          console.log(data);
          if(data.code==0){
            that.warn="注册成功！";
            that.change_zhuce();
          }else{
            that.warn = data.msg;
            that.reg.code='';
          }
        }).finally((res) => {
          console.log(res);

        });
      },
      change_zhuce(){
        this.is_zhuce=!this.is_zhuce;
      },
      initialize() {
        document.onkeypress = (e) => {
          let code = document.all ? event.keyCode : e.which;
          if (code == 13) {
            this.login();
            return false;
          }
        }
      },
      login() {
        this.$refs.form.validate(valid => {
          if (valid) {
            this.isLoading = true;

            login(this.form).then(data => {
              console.log(data);
              this.$store.commit('setUser',data.data);
              Cookies.set('token', data.data.token);
              Cookies.set('email', data.data.email);
              this.$router.push('/home');
            }).finally(() => {
              this.isLoading = false;
            });
          }
        });
      },
    },
    created() {
      this.initialize();
    },
  }
</script>

<style>
  .reg_group{

  }
</style>