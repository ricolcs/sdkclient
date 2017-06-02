import Vue from 'vue';
import Vuex from 'vuex';
import * as actions from './actions.js'
import * as getters from './getters'
import haixinyuans from './modules/haixinyuan.js'

Vue.use(Vuex)

export default new Vuex.Store({
    actions,
    getters,
    modules: {
        haixinyuans
    }
})