export const user = state => {
	return state.user
}

// export const applications = state => {
// 	return state.applications.applications
// }

// export const applicationsDone = state => {
// 	return state.applications.applications.filter(app => app.status === 'complete' || app.status === 'not allowed')
// }

// export const applicationsActive = state => {
// 	return state.applications.applications.filter(app => app.status === 'review' || app.status === 'process' )
// }
export const params = state => {
	return state.api.params
}

export const responses = state => {
	return state.api.responses
}

export const refs = state => {
	return state.api.refs
}