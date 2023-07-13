<template>
  <div >
    <a-table rowKey="id" :columns="columns" :data-source="datasource" style="margin-top: 5px;">
      <div slot.attrs="number"   slot-scope="obj,index">

        {{obj}}-{{index}}
      </div>
    </a-table>
  </div>
</template>

<script>

/*
  column 字段
  {
    title: '序号',
    dataIndex: 'id',
    key: 'id',
    type:'id'   // id | number | string | dropdown
  }
  source
  {
    key: '1',
    name: '胡彦斌',
    age: 32,
    address: '西湖区湖底公园1号'
  }
 */

export default {
  name: 'tableedit',
  props:['columns','datasource'],
  data() {
    return {

    };
  },
  methods: {


  },
  mounted() {

  },
}
</script>

<style>

</style>