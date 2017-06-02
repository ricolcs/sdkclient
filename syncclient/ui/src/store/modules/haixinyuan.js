import store from '../index.js'

const state = {
    haixinyuans: []
};


const mutations = {
    GETHAIXINYUANS(state, data) {
        state.haixinyuans = data;
    },
    CLEARHAIXINYUANS(state) {
        state.haixinyuans = [];
    }
}

export default {
    state,
    mutations
}