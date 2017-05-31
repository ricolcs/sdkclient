import Vue from 'vue';
import Vuex from 'vuex';
import * as actions from './actions.js'
import * as getters from './getters'
import user from './modules/user.js'
import projects from './modules/projects.js'
import apis from './modules/apis.js'
import api from './modules/api.js'
import refs from './modules/refs.js'
import haixinyuans from './modules/haixinyuan.js'

Vue.use(Vuex)

export default new Vuex.Store({
    actions,
    getters,
    modules: {
        user,
        projects,
        apis,
        api,
        refs,
        haixinyuans
    }
})