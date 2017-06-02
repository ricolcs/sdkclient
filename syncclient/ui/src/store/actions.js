import $ from 'jquery'
import Vue from 'vue'

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

