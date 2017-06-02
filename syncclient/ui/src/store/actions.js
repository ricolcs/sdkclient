import $ from 'jquery'
import Vue from 'vue'

export const clearToken = function ({commit}) {
    commit('CLEARTOKEN')
}

export const setToken = function ({commit}, user) {
    commit('SETTOKEN', user)
}

export const setEmail = function ({commit}, email) {
    commit('SETEMAIL', email)
}

export const getProjects = function ({commit}) {
    $.ajax({
        url: `${Vue.prototype.appConfig.apiBase}project/getProjectList`,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        cache: false,
        success: (data) => {
            if (data.code === 0) {
                if (data.data.length > 0) {
                    data.data[data.data.length - 1].isLast = true;
                }
                commit('GETPROJECTS', data.data);
            } else {
                Vue.prototype.appConfig.showErrorAlert(data.code);
            }
        },
        error: (data) => {
            Vue.prototype.appConfig.showErrorAlert('default');
        }
    })
}

export const clearProjects = function ({commit}) {
    commit('CLEARPROJECTS')
}

export const getApis = function ({commit}, projectID) {
    $.ajax({
        url: `${Vue.prototype.appConfig.apiBase}api/getList/${projectID}`,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        cache: false,
        success: (data) => {
            if (data.code === 0) {
                if (data.data.length > 0) {
                    data.data[data.data.length - 1].isLast = true;
                }
                commit('GETAPIS', data.data)
            } else {
                Vue.prototype.appConfig.showErrorAlert(data.code);
            }
        },
        error: (data) => {
            Vue.prototype.appConfig.showErrorAlert('default');
        }
    })
}

export const clearApis = function ({commit}) {
    commit('CLEARAPIS')
}

export const getHaixinyuans = function ({commit}, timeInterval) {
    $.ajax({
        url: `${Vue.prototype.appConfig.apiBase}statistical/information/${timeInterval}`,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        cache: false,
        async: false,
        success: (data) => {
            if (data.code === 0) {
                if (data.data.length > 0) {
                    data.data[data.data.length - 1].isLast = true;
                }
                commit('GETHAIXINYUANS', data.data);
            } else {
                Vue.prototype.appConfig.showErrorAlert(data.code);
            }
        },
        error: (data) => {
            Vue.prototype.appConfig.showErrorAlert('default');
        }
    })

}

export const clearHaixinyuan = function ({commit}) {
    commit('CLEARHAIXINYUANS')
}

export const getApi = function ({commit}, {id, apiName}) {
    var json = {};
    localStorage.pageInfo = "";
    $.ajax({
        url: `${Vue.prototype.appConfig.apiBase}api/getApi/${id}?apiname=${apiName}`,
        type: 'GET',
        async: false,
        contentType: 'application/json; charset=utf-8',
        success: (data) => {
            if (data.code === 0) {
                commit('GETBASICINFO', data.data);
                json.info = data.data;
                json.get = {};
                json.post = {};
                if (data.data.requestWay) {
                    //data.data.requestWay.split(',').forEach((method) => {
                    $.ajax({
                        url: `${Vue.prototype.appConfig.apiBase}api/param/getList/${id}`,
                        type: 'POST',
                        contentType: 'application/json; charset=utf-8',
                        cache: false,
                        data: JSON.stringify({
                            url: data.data.url,
                            type: "get"
                        }),
                        success: data => {
                            commit('GETPARAMS', {type: "request", arr: data.data})
                            json.get.request = data.data;
                            localStorage.pageInfo = JSON.stringify(json);
                        },
                        error: data => {

                        }
                    })

                    $.ajax({
                        url: `${Vue.prototype.appConfig.apiBase}api/response/getList/${id}`,
                        type: 'POST',
                        contentType: 'application/json; charset=utf-8',
                        cache: false,
                        data: JSON.stringify({
                            url: data.data.url,
                            type: "get"
                        }),
                        success: data => {
                            commit('GETPARAMS', {type: "response", arr: data.data})
                            json.get.response = data.data;
                            localStorage.pageInfo = JSON.stringify(json);
                        },
                        error: data => {

                        }
                    })

                    $.ajax({
                        url: `${Vue.prototype.appConfig.apiBase}api/param/getList/${id}`,
                        type: 'POST',
                        contentType: 'application/json; charset=utf-8',
                        cache: false,
                        data: JSON.stringify({
                            url: data.data.url,
                            type: "post"
                        }),
                        success: data => {
                            commit('GETRESPONSES', {type: "request", arr: data.data})
                            json.post.request = data.data;
                            localStorage.pageInfo = JSON.stringify(json);
                        },
                        error: data => {

                        }
                    })

                    $.ajax({
                        url: `${Vue.prototype.appConfig.apiBase}api/response/getList/${id}`,
                        type: 'POST',
                        contentType: 'application/json; charset=utf-8',
                        cache: false,
                        data: JSON.stringify({
                            url: data.data.url,
                            type: "post"
                        }),
                        success: data => {
                            commit('GETRESPONSES', {type: "response", arr: data.data})
                            json.post.response = data.data;
                            localStorage.pageInfo = JSON.stringify(json);
                        },
                        error: data => {

                        }
                    })
                    //})
                }
            } else {
                Vue.prototype.appConfig.showErrorAlert(data.code);
            }
        },
        error: (data) => {
            Vue.prototype.appConfig.showErrorAlert('default');
        }
    })

    $.ajax({
        url: `${Vue.prototype.appConfig.apiBase}api/def/getList/${id}`,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        cache: false,
        success: (data) => {
            if (data.code === 0) {
                commit('GETREFS', data.data)
            } else if (data.code === 5006) {
                commit('GETREFS', []);
            } else {
                Vue.prototype.appConfig.showErrorAlert(data.code);
            }
        },
        error: (data) => {
            Vue.prototype.appConfig.showErrorAlert('default');
        }
    })
}

export const clearApiInfo = function ({commit}) {
    commit('CLEARAPIINFO')
}

export const getBasicInfo = function ({commit}, info) {
    commit('GETBASICINFO', info)
}

export const getParams = function ({commit}, data) {
    commit('GETPARAMS', data)
}

export const getRefs = function ({commit}, id) {
    $.ajax({
        url: `${Vue.prototype.appConfig.apiBase}api/def/getList/${id}`,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        cache: false,
        success: (data) => {
            if (data.code === 0) {
                if (data.data.length > 0) {
                    data.data[data.data.length - 1].isLast = true
                }
                commit('GETREFSLIST', data.data)
            } else if (data.code === 5006) {
                commit('GETREFSLIST', []);
            } else {
                Vue.prototype.appConfig.showErrorAlert(data.code);
            }
        },
        error: (data) => {
            Vue.prototype.appConfig.showErrorAlert('default');
        }
    })
}
