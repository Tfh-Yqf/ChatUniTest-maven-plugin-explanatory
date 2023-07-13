<template>
  <div>
    <a-modal  :confirmLoading="loading" :maskClosable="false" @cancel="cancel" @ok="confirm">
      <div slot="title">{{form.id ? '编辑账号' : '新增账号' }}</div>
      <div>
        <a-form-model ref="form" :model="form"  :label-col="{ span: 4 }" :wrapper-col="{ span: 16 }">
          <a-form-model-item prop="name" label="用户名">
            <a-input v-model="form.name" />
          </a-form-model-item>
          <a-form-model-item prop="telephone" label="手机号">
            <a-input v-model="form.telephone" />
          </a-form-model-item>
          <a-form-model-item prop="email" label="邮箱">
            <a-input v-model="form.email" />
          </a-form-model-item>

          <a-form-model-item prop="roles" label="角色">
            <a-select v-model="form.roles" mode="multiple" allowClear style="width: 100%;">
              <a-select-option v-for="item in roleItems" :key="item.key" :value="item.key">{{item.name}}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-form-model>

        <div v-if="!form.id" style="color: rgb(255, 77, 79); text-align: center;">
          默认初始密码为: 123456, 登录后请修改密码
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { userCreate, userUpdate } from '@/api/account'


export default {
  name: 'FormModal',
  props: ['visible', 'form', 'roleItems'],
  model: { prop: 'visible', event: 'cancel' },
  data() {
    return {
      loading: false,
    };
  },
  methods: {
    confirm() {
      if(this.form.name==''||this.form.email==''||this.form.telephone==''){
        this.$message.warn('未填写完整!');
        return;
      }

      if(this.form.name==undefined||this.form.email==undefined||this.form.telephone==undefined){
        this.$message.warn('未填写完整!');
        return;
      }

      this.$message.success(this.form.id ? '修改成功' : '新增成功');
      this.$emit(this.form.id ? 'update' : 'create', this.form);
      this.cancel();


      let func = this.form.id ? userUpdate : userCreate;
      func(this.form).then(res => {
        if(res.code==0){
          this.$message.success(this.form.id ? '修改成功' : '新增成功');
          this.$emit(this.form.id ? 'update' : 'create', res.data);
          this.cancel();
        }
      });
    },
    cancel() {
      this.$emit('cancel', false);
    },
  },
}
</script>

<style scoped>
</style>