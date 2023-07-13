import system from "./modules/system";
import user from "./modules/user";
import simulation from './modules/simulation';
import position from "./modules/position";
import navigation from './modules/navigation';
import sound from './modules/sound';
import Vuex from "vuex";
import Vue from "vue";

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    user,
    system,
    navigation,
    simulation,
    position,
    sound,
  },
});
