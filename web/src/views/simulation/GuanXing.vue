<template>
  <div>
    <div class="card">
      <div class="card-header">
        轨迹列表
      </div>
      <div class="card-body">
        <table class="table table-hover">
          <thead>
          <tr>
            <th scope="col">序号</th>
            <th scope="col">轨迹类型</th>
            <th scope="col">航行时间</th>
            <th scope="col">角速度</th>
            <th scope="col">加速度速度</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <th scope="row">1</th>
            <td>
              <a-select defaultValue="classic" class="select">
                <a-select-option value="classic">classic</a-select-option>
                <a-select-option value="static">static</a-select-option>
                <a-select-option value="line">line</a-select-option>
                <a-select-option value="circle">circle</a-select-option>
                <a-select-option value="s">s</a-select-option>
                <a-select-option value="well">well</a-select-option>
                <a-select-option value="spiral">spiral</a-select-option>
              </a-select>
            </td>
            <td><Input type="textarea" /></td>
            <td><Input type="textarea" /></td>
            <td><Input type="textarea" /></td>
          </tr>
          </tbody>
        </table>
        <button type="button" class="btn btn-outline-primary" style="float: right;">添加行</button>
      </div>
    </div>
    <div class="card" style="margin-top: 10px;">
      <div class="card-header">
        初始参数设置
      </div>
      <div class="card-body">
        <div class="row">
          <span class="label1">惯导采样率：</span>
          <Input type="textarea" style="width:200px;display:inline;" />
        </div>
        <div class="row">
          <table class="table table-hover text-center">
            <thead>
            <tr>
              <th scope="col"></th>
              <th scope="col">Pitch</th>
              <th scope="col">Roll</th>
              <th scope="col">Heading</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <th scope="row">初始姿态</th>
              <td><Input type="textarea" /></td>
              <td><Input type="textarea" /></td>
              <td><Input type="textarea" /></td>
            </tr>
            </tbody>
          </table>
        </div>
        <div class="row">
          <table class="table table-hover text-center">
            <thead>
            <tr>
              <th scope="col"></th>
              <th scope="col">E</th>
              <th scope="col">N</th>
              <th scope="col">U</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <th scope="row">初始速度</th>
              <td><Input type="textarea" /></td>
              <td><Input type="textarea" /></td>
              <td><Input type="textarea" /></td>
            </tr>
            <tr>
              <th scope="row">初始位置</th>
              <td><Input type="textarea" /></td>
              <td><Input type="textarea" /></td>
              <td><Input type="textarea" /></td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div style="height: 300px;width: 100%">
      <a-button style="width: 100px;height: 40px" type="primary" @click="Excute">Excute</a-button>
    </div>
  </div>
</template>

<script>
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.bundle"

export default {
  data() {
    return {
      table_row_cnt: 1,

    }
  },
  methods: {
    Excute(){

    }
  },
  mounted() {

  }
}
</script>

<style scoped>
.select {
  width: 202px;
}

.label1 {
  width: 200px;
  margin-left: 20px;
  font-weight: bolder;
  display: inline;
}
</style>
