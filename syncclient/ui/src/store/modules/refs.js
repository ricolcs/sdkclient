import store from '../index.js'

const state = {
	refs: []
};


const mutations = {
	GETREFSLIST(state, data) {
		state.refs = data;
	},
	CLEARREFSLIST(state) {
		state.refs = [];
	}
}

export default {
	state,
	mutations
}