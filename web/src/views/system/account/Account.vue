<template>
  <div>
    <a-card title="员工账号">
      <a-row :gutter="16">
        <a-col :span="24" :md="8" :xl="6" style="margin-bottom: 12px;">
          <a-input-search v-model="searchForm.search" placeholder="用户名,电话,邮箱" allowClear @search="search" />
        </a-col>

        <div style="margin-bottom: 12px; float: right;">
          <a-button type="primary" icon="plus" style="margin: 0 8px;" @click="targetItem = {...form}; visible = true;">
            新增账号</a-button>
        </div>
      </a-row>

      <a-row style="margin-top: 12px;">
        <a-table rowKey="id" size="small" :columns="columns" :dataSource="items" :loading="loading" :pagination="pagination"
          @change="tableChange">
          <div slot="role_names" slot-scope="roleNames">
            <a-tag v-for="role in roleNames" :key="role" color="#2db7f5">
              {{role}}
            </a-tag>
          </div>
          <div slot="action" slot-scope="value, item,index">
            <a-button-group>
              <a-button icon="edit" size="small" @click="edit(item,index)">编辑</a-button>
              <a-popconfirm title="确定重置吗? 密码: 123456" @confirm="resetPassword(item.email)">
                <a-button size="small" type="primary" icon="sync">重置密码</a-button>
              </a-popconfirm>
              <a-popconfirm title="确定删除吗?" @confirm="destroy(item.email,index)">
                <a-button type="danger" icon="delete" size="small">删除</a-button>
              </a-popconfirm>
            </a-button-group>
          </div>

        </a-table>
      </a-row>
    </a-card>

    <form-modal v-model="visible" :form="targetItem" :roleItems="roleItems" @create="create"  @update="update" />


  </div>
</template>

<script>
  import { userList, userDestroy, userResetPassword,get_role } from '@/api/account'

  import { permissions } from '@/permissions.js'

  export default {
    name: 'Account',
    components: {
      FormModal: () => import('./FormModal.vue')
    },
    data() {
      return {
        columns: [
          {
            title: '序号',
            dataIndex: 'index',
            key: 'index',
            customRender: (value, item, index) => {
              return index + 1
            },
          },
          {
            title: '用户名',
            dataIndex: 'name',
            key:'name',
            sorter: true,
          },
          {
            title: '手机号',
            dataIndex: 'telephone',
            key:'telephone'
          },
          {
            title: '邮箱',
            dataIndex: 'email',
            key:'email'
          },
          {
            title: '密码',
            dataIndex: 'password',
            key:'password',
            customRender: (value, item, index) => {
              return "********";
            },
          },
          {
            title: '角色',
            dataIndex: 'role_names',
            scopedSlots: { customRender: 'role_names' },
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
            width: '256px'
          },
        ],
        permissions,
        searchForm: { search: '', page: 1, ordering: undefined, page_size: 16 },
        pagination: { current: 1, total: 0, pageSize: 16 },
        loading: false,
        items: [],
        roleItems: [],
        visible: false,
        targetItem: {},
        form: {},
        now:0
      };
    },
    methods: {
      initialize() {
        this.items = [{name:'admin',telephone:'15753962023',email:'2813223276@qq.com',role_names:['系统管理员'],roles:[0]},
          {name:'qxxqx',telephone:'13334334344',email:'2813223133@qq.com',role_names:['首页',],roles:[4]},
          {name:'谭福华',telephone:'19819600510',email:'2813223283@qq.com',role_names:['数据报表'],roles:[9]},
          {name:'齐辛格',telephone:'13334331231',email:'2832232123@qq.com',role_names:['算法识别'],roles:[13]}
        ];

        userList(this.now).then((res)=>{
          if(res.code==0){
            this.items = res.data;
            this.now+=res.data.length;
          }
        })
      },
      create(item) {
        this.items.push(item);
        this.visible=false;
      },
      edit(item,index){
        this.targetItem = {...item,id:index+1};
        this.visible = true;
      },
      update(item) {

        item.id--;
        var data = [...this.items];
        data[item.id] = item;
        this.items = [...data];
        this.visible=false;

      },
      destroy(email,index) {
        this.items.splice(index,1);
        userDestroy({ email }).then((res) => {
          if(res.code==0){
            this.$message.success('删除成功');
          }
        });
      },
      resetPassword(email) {
        userResetPassword({ email }).then((res) => {
          if(res.code==0){
            this.$message.success('重置成功');
          }

        });
      },
      search() {
        var data = [];
        var value = this.searchForm.search;
        for(var i=0;i<this.items.length;i++){

          if(this.items[i].name.includes(value)||this.items[i].telephone.includes(value)||this.items[i].email.includes(value)){
            data.push(this.items[i]);
          }
        }
        this.items=data;
      },
      tableChange(pagination, filters, sorter) {
        this.searchForm.page = pagination.current;
        this.pagination.current = pagination.current;
        this.searchForm.ordering = `${sorter.order == 'descend' ? '-' : ''}${sorter.field}`;
      },
      getRole(){
        this.roleItems =[
          {key:0,value:'0',name:'系统管理'},
          {key:4,value:'4',name:'首页'},
          {key:6,value:'6',name:'数据报表'},
          {key:9,value:'9',name:'检测任务'},
          {key:13,value:'13',name:'算法识别'}
        ];

        get_role({}).then((res)=>{
          if(res.code==0){
            this.roleItems  = res.data;
          }
        });
      },
    },
    mounted() {
      this.initialize();
      this.getRole();
    },
  }
</script>

<style scoped>
</style>