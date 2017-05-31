import store from '../index.js'

const state = {
	basicInfo: {
		operationId: '',
		apiName: '',
		url: '',
		scheme: '',
		description: '',
		requestWay: '',
		tags: '',
	},
	params: {
		length: 0
	},
	responses: {
		length: 0
	},
	refs: [],
};


const mutations = {
	GETPARAMS(state, data) {
		state.params = data;
	},
	CLEARPARAMIS(state) {
		state.params = {};
	},
	// GETBASICINFO(state, {operationId, apiName, url, scheme, description, requestWay, tags}) {
	GETBASICINFO(state, info) {
		// state.operationId = operationId;
		// state.apiName = apiName;
		// state.url = url;
		// state.scheme = scheme;
		// state.description = description;
		// state.requestWay = requestWay;
		// state.tags = tags;
		state.basicInfo = info;
	},
	CLEARAPIINFO(state) {
		state.basicInfo = {
			operationId: '',
			apiName: '',
			url: '',
			scheme: '',
			description: '',
			requestWay: '',
			tags: '',
		};
		state.params = {
			length: 0
		};
		state.responses = {
			length: 0
		};
	},
	GETPARAMS(state, {type, arr}) {
		state.params[type] = arr;
		state.params.length++;
	},
	GETRESPONSES(state, {type, arr}) {
		state.responses[type] = arr;
		state.responses.length++;
	},
	GETREFS(state, data) {
		state.refs = data
	}
}

export default {
	state,
	mutations
}