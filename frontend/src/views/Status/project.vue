<template>
  <div style="width: 100%;display: flex;justify-content: center;">
    <div style="width: 100%;padding: 10px;background-color: white;">
      <a-table :columns="columns" :dataSource="data">
        <p slot="tags" slot-scope="text,tags,i">

          <a-button v-if="tags.status=='finished'" type="primary" @click="down(tags.dirName)">下载</a-button>
          <a-button v-else type="primary" style="background-color: grey;border: 1px solid grey" >未完成</a-button>

        </p>
      </a-table>
    </div>

  </div>
</template>

<script>

import {getProjectStatus} from '@/api/project'

export default {
  components: {

  },
  data() {
    return {
      columns:[
        {
          title: '序号',
          dataIndex: 'id',
          align: 'center'
        },
        {
          title: '工程名',
          dataIndex: 'name',
          align: 'center'
        },
        {
          title: '账户',
          dataIndex: 'email',
          align: 'center'
        },{
          title: '状态',
          dataIndex: 'status',
          align: 'center'
        },{
          title: '创建时间',
          dataIndex: 'createtime',
          align: 'center'
        },{
          title: '文件名',
          dataIndex: 'dirName',
          align: 'center'
        },{
          title: '结果',
          dataIndex: 'result',
          align: 'center'
        },
        {
          title: '操作',
          dataIndex: 'tags',
          key: 'tags',
          scopedSlots: { customRender: 'tags' },
        },
      ],
      data:[]
    };
  },
  methods: {
    down(e){
      window.location.href = "http://127.0.0.1:8081/file?filename="+e;
      console.log("http://127.0.0.1:8081/file?filename="+e)
    },
    init(){
      var data = {
        email:this.$store.state.user.email
      };
      getProjectStatus(data).then((res)=>{
        this.data = res.data;
      })
    }

  },
  mounted() {
    this.init()
  },
};
</script>

<style >

</style>
