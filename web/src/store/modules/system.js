export default {
  state: () => ({
    fullScreen: false,
    config: {},
    Navgiation:{},
    sound:{},
    demarcate:{},
    SimulationConfig:{},
    gravity:{},
    SimulationResult:{},
  }),
  mutations: {
    setConfig(state, item) {
      for (let key of Object.keys(item)) {
        state.config[key] = item[key].strategy;
      }
    },
    setNavgiation(state,item){
      // for (let key of Object.keys(item)) {
      //   state.Navgiation[key] = item[key].strategy;
      // }
      state.Navgiation = item;
    },
    setSound(state,item){
      state.sound = item;
    },
    setDemarcate(state,item){
      state.demarcate = item;
    },
    setSimulationConfig(state,item){
      state.SimulationConfig = item;
    },
    setSimulationResult(state,item){
      state.SimulationResult = item;
    },
    setGravity(state,item){
      state.gravity = item;
    },
  }
}