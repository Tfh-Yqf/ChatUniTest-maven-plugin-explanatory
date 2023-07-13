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
          <select class="table-item-select" v-model.number="$store.state.sound.time.algorithmSelect" @change="queryModelList">
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
          <select class="table-item-select" v-model.number="$store.state.sound.time.smlp" placeholder="选择温盐深数据">
            <option :value="item.path" v-for="item in fileList.smlp" :key="item.name">{{ item.name
            }}</option>
          </select>
        </div>
      </div>
      <div class="select-file-item">
        <span class="label">
          点时空数据
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="$store.state.sound.time.time" placeholder="选择通信数据"
            @change="timeSelect">
            <option :value="item.path" v-for="item in fileList.time" :key="item.name">{{ item.name
            }}</option>
          </select>
        </div>
      </div>
    </div>
    <div class="space-config-item">
      <div class="title-bar">
        <div class="title-name">反演时间选择</div>
      </div>
      <div class="select-file-item">
        <span class="label">
          反演时刻
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="$store.state.sound.time.currentTime">
            <option :value="item" v-for="item in timeList" :key="item">{{ item }}</option>
          </select>
        </div>
      </div>
    </div>
    <div class="space-config-item" v-if="$store.state.sound.time.algorithmSelect === 2">
      <div class="title-bar">
        <div class="title-name">模型选择</div>
      </div>
      <div class="select-file-item">
        <span class="label">
          模型名称
        </span>
        <div class="item-input">
          <select class="table-item-select" v-model.number="$store.state.sound.time.modelPath">
            <option :value="item.path" v-for="item in modelList" :key="item.path">{{ item.name
            }}</option>
          </select>
        </div>
      </div>
    </div>
    <div class="excute-btn" @click="excute">执行</div>
  </div>
</template>

<script>
import { SoundTime_DuoXisngShi_Initdata, SoundTime_DuoXisngShi_SspTime, SoundTime_Zhichi_InitData, ExcuteDuoXiang, ExcuteSVM, ExcuteNCFanYan, ExcuteSCFanYan } from '@/api/sound';

export default {
  name: 'SpaceWrapper',
  data: () => {
    return {
      algorithmOption: [
        { value: 1, name: '多项式拟合' },
        { value: 2, name: '支持向量机' },
      ],
      fileList: {
        smlp: [],
        time: [],
      },
      pointList: [],
      timeList: [],
      modelList: [],
    };
  },
  methods: {
    queryFileLlist() {
      SoundTime_DuoXisngShi_Initdata().then((res) => {
        if (res.code == 0) {
          console.log(res.data);
          this.fileList = res.data;
        }
      });
    },
    timeSelect() {
      const params = this.fileList.time.find(i => i.path === this.$store.state.sound.time.time);
      SoundTime_DuoXisngShi_SspTime(params).then((res) => {
        if (res.code == 0) {
          this.timeList = res.data;
          this.$store.commit('setCurrentTime', res.data[0]);
        }
      });
    },
    queryModelList() {
      const val = this.$store.state.sound.time.algorithmSelect;
      if (val === 2) {
        SoundTime_Zhichi_InitData().then((res) => {
          if (res.code == 0) {
            this.modelList = res.data.checkpoints;
          }
        });
      }
    },
    excute() {
      if (this.getLoadingStatus()) {
        this.$message.error('任务执行中，请稍后');
        return;
      }
      this.setLoadingStatus(true);
      this.$message.success('开始执行任务');
      const { algorithmSelect, smlp, time, currentTime, modelPath } = this.$store.state.sound.time;
      let params = {};
      let service = null;
      if (algorithmSelect === 1) {
        params = { ssp_path: smlp, time_path: time, test_idx: currentTime, mode: 'PF' };
        service = ExcuteDuoXiang;
      } else if (algorithmSelect == 2) {
        params = { time: currentTime, ssp_path: smlp, time_path: time, modeal_path: modelPath };
        service = ExcuteSVM;
      }
      this.$store.state.sound.resultShow = false;
      service(params).then(res => {
        if (res.code != 0 || res.code == undefined) {
          this.$message.error(res.msg || res.message || "服务器错误");
        } else {
          this.$message.success('任务执行成功');
          this.$store.commit('setChartShow', true);
          this.$store.commit('setSound', res.data);
          this.$store.state.sound.resultShow = true;
        }
        this.setLoadingStatus(false);
      });
    },
    getLoadingStatus() {
        return this.$store.state.position.loading;
    },
    setLoadingStatus(stat) {
        this.$store.state.position.loading = stat;
    }
  },
  mounted() {
    this.queryFileLlist();
  }
};
</script>
<style lang="less" scoped>
.space-wrapper {
  padding-left: 8px;
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

  .unit {
    color: #99B3C8;
    font-size: 12px;
  }
  .table-item-select{
  width: 100%;
  background-color: transparent;
}
}</style>
