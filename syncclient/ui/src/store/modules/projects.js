import store from '../index.js'

const state = {
	projects: []
};


const mutations = {
	GETPROJECTS(state, data) {
		state.projects = data;
	},
	CLEARPROJECTS(state) {
		state.projects = [];
	}
}

export default {
	state,
	mutations
}