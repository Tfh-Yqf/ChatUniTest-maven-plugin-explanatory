<template>
  <div class="space-wrapper">
    <div class="space-config-item">
      <div class="title-bar">
        <div class="title-name">算法设置</div>
      </div>
      <div class="select-file-item">
        <span class="label">
          算法选取
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="$store.state.sound.space.algorithmSelect">
            <option :value="item.value" v-for="item in algorithmOption" :key="item.name">{{ item.name
            }}</option>
          </select>
        </div>
      </div>
    </div>
    <div class="space-config-item">
      <div class="title-bar">
        <div class="title-name">数据读取</div>
      </div>
      <div class="select-file-item">
        <span class="label">
          温盐深数据
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="$store.state.sound.space.svp" placeholder="选择温盐深数据">
            <option :value="item.path" v-for="item in fileList.svp" :key="item.name">{{ item.name
            }}</option>
          </select>
        </div>
      </div>
      <div class="select-file-item">
        <span class="label">
          点位置数据
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="$store.state.sound.space.position" placeholder="选择点位置数据"
            @change="positionSelect">
            <option :value="item.path" v-for="item in fileList.position" :key="item.name">{{ item.name
            }}</option>
          </select>
        </div>
      </div>
      <div class="select-file-item">
        <span class="label">
          通信数据
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="$store.state.sound.space.sonar" placeholder="选择通信数据">
            <option :value="item.path" v-for="item in fileList.sonar" :key="item.name">{{ item.name
            }}</option>
          </select>
        </div>
      </div>
    </div>
    <div class="space-config-item" v-if="pointList.length">
      <div class="title-bar">
        <div class="title-name">反演位置选择</div>
      </div>
      <div class="select-file-item">
        <span class="label">
          目标点
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="currentPoint">
            <option :value="index" v-for="(item, index) in pointList" :key="index">目标点{{ index + 1
            }}</option>
          </select>
        </div>
      </div>
      <div class="select-file-item" v-if="pointList.length">
        <span class="label">
          目标点{{ currentPoint + 1 }}位置
        </span>
        <div class="latitude"><input v-model="pointList[currentPoint][0]" class="table-item-input" disabled></div>
        <div class="longitude"><input v-model="pointList[currentPoint][1]" class="table-item-input" disabled></div>
        <div class="unit">经纬度</div>
      </div>
      <div class="select-file-item" v-for="(item, index) in pointList" :key="index">
        <span class="label">
          参考点{{ index + 1 }}位置
        </span>
        <div class="latitude"><input v-model="item[0]" class="table-item-input" disabled></div>
        <div class="longitude"><input v-model="item[1]" class="table-item-input" disabled></div>
        <div class="unit">经纬度</div>
      </div>
    </div>
    <div class="excute-btn" @click="excute">执行</div>
  </div>
</template>

<script>
import { Sound_initdata, SoundSpace_getPosition, ExcuteBP, ExcuteMeta } from '@/api/sound';

export default {
  name: 'SpaceWrapper',
  data: () => {
    return {
      algorithmOption: [
        { value: 1, name: 'BP神经网络' },
        { value: 2, name: '元学习' },
      ],
      fileList: {
        svp: [],
        position: [],
        sonar: []
      },
      pointList: [],
      currentPoint: 0,
    };
  },
  methods: {
    positionSelect() {
      const val = this.$store.state.sound.space.position;
      const params = this.fileList.position.find(item => item.path === val);
      SoundSpace_getPosition(params).then((res) => {
        if (res.code === 0) {
          this.pointList = res.data;
          this.currentPoint = 0;
          this.draw_point();
        }
      });
    },
    draw_point() {
      var temp = {
        pointList: this.pointList,
        Nowpoint: this.currentPoint
      };
      this.$emit('AddPoints', temp);
    },
    querySvpList() {
      Sound_initdata().then((res) => {
        if (res.code == 0) {
          this.fileList = res.data;
        }
      });
    },
    excute() {
      if (this.getLoadingStatus()) {
        this.$message.error('任务执行中，请稍后');
        return;
      }
      this.setLoadingStatus(true);
      this.$message.success('开始执行任务');
      const {algorithmSelect} = this.$store.state.sound.space;
      const serves = algorithmSelect === 1 ? ExcuteBP : ExcuteMeta;
      const params = { index: this.currentPoint };
      this.$store.state.sound.resultShow = false;
      serves(params).then(res => {
        if (res.code === 0) {
          this.$store.commit('setSound', res.data);
          this.$store.commit('setChartShow', true);
          this.$message.success('任务执行成功');
          this.$store.state.sound.resultShow = true;
        } else {
          this.$message.error(res.msg || res.message || "服务器错误");
        }
        this.setLoadingStatus(false);
      });
    },
    getLoadingStatus() {
        return this.$store.state.sound.loading;
    },
    setLoadingStatus(stat) {
        this.$store.state.sound.loading = stat;
    }
  },
  mounted() {
    this.querySvpList();
  }
};
</script>
<style lang="less" scoped>
.space-wrapper {
  padding-left: 8px;
}

.title-bar {
  padding: 0 0 0 8px;
  width: 100%;
  height: 24px;
  align-items: center;
  background: linear-gradient(90deg, rgba(8, 83, 106, 0.79) 0%, rgba(13, 78, 99, 0.03) 100%);

  .title-name {
    font-size: 14px;
    line-height: 24px;
    height: 24px;
    color: #99B3C8;
    font-weight: 500;
    margin-right: 10px;
  }
}

.select-file-item {
  display: flex;
  align-items: center;
  margin: 4px 0;

  .label {
    width: 80px;
    font-size: 12px;
    font-family: Source Han Sans CN-Regular, Source Han Sans CN;
    font-weight: 400;
    color: #99B3C8;
    line-height: 20px;
    margin-right: 8px;
  }

  .item-input {
    height: 20px;
    width: 170px;
    border-radius: 0px;
    border: none;
    background: rgba(47, 136, 181, 0.16);
    color: #99B3C8;
    font-size: 12px;

    :deep(.ant-select-selection--single) {
      width: 170px;
      height: 20px;
      border-radius: 0px;
      background-color: rgba(47, 136, 181, 0.16);
      border: none;
    }

    :deep(.ant-select-selection__placeholder) {
      font-size: 12px;
      color: #99B3C8;
    }

    :deep(.ant-select-selection__rendered) {
      line-height: 20px;
    }

    :deep(.ant-select-selection-selected-value) {
      color: #99B3C8;
      font-size: 12px;
    }
  }

  .latitude,
  .longitude {
    width: 97px;
    font-size: 12px;
    color: #99B3C8;
  }
  .table-item-input {
      height: 20px;
      width: 90%;
      border-radius: 0px 0px 0px 0px;
      margin: 0 5px;
      opacity: 1;
      border: none;
      background: rgba(47, 136, 181, 0.16);
    }
  .unit {
    color: #99B3C8;
    font-size: 12px;
  }
}

.excute-btn {
  margin: 0 auto;
  font-size: 12px;
  font-family: Source Han Sans CN-Regular, Source Han Sans CN;
  font-weight: 400;
  color: #F5FBFF;
  width: 80px;
  height: 26px;
  line-height: 26px;
  color: #F5FBFF;
  text-align: center;
  background: linear-gradient(180deg, rgba(12, 53, 111, 0) 0%, rgba(25, 236, 255, 0.63) 100%);
  opacity: 1;
  border: 1px solid #19ECFF;
  margin-top: 8px;
  cursor: pointer;
}
.table-item-select{
  width: 100%;
  background-color: transparent;
}
</style>
