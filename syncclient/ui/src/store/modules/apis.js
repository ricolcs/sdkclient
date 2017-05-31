import store from '../index.js'

const state = {
	apis: []
};


const mutations = {
	GETAPIS(state, data) {
		data.sort(function (a,b) {
			return b.sort-a.sort;
		});
		state.apis = data;
	},
	CLEARAPIS(state) {
		state.apis = [];
	}
}

export default {
	state,
	mutations
}