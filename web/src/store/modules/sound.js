export default {
  state: () => ({
    loading: false,
    resultShow: false,
    space: {
      algorithmSelect: 1,
      svp: undefined,
      position: undefined,
      sonar: undefined,
    },
    time: {
      algorithmSelect: 1,
      smlp: undefined,
      time: undefined,
      currentTime: undefined,
      modelPath: undefined,
    },
    chart: false
  }),
  mutations: {
    setCurrentTime(state, item) {
      state.time.currentTime = item;
    },
    setChartShow(state, item) {
      state.chart = true;
    },
  }
};