import Vue from 'vue'
import App from './App.vue'
import store from './store'
import router from './router'
import appConfig from './app_config'
import Message from './components/Message.js'
import Modal from './components/Modal.vue'
import 'datatables'
import 'datatables/media/css/jquery.dataTables.css'
import './plugins/validation.js'


Vue.config.devtools = false;

appConfig.domain = location.protocol + '//' + location.host;
//appConfig.apiBase = appConfig.domain + location.pathname;
appConfig.apiBase = appConfig.domain + "/";

Vue.prototype.appConfig = appConfig;
Vue.prototype.$message = Message;

router.beforeEach(function (to, from, next) {
    /*if (!!store.state.user.token || to.name === 'home') {
     next()
     } else if (to.name !== 'home') {
     router.push('/')
     Vue.prototype.$message.alert('请先登录！');
     }*/
    next()
})

const app = new Vue({
    router,
    store,
    render: h => h(App),
    beforeCreate: () => {
        console.log("App before: start");
    },
    created () {
        this.checkToken();
    },
    methods: {
        checkToken () {
            /*if (!window.localStorage.getItem('token')) {
             this.$router.push('/')
             this.$store.dispatch('clearToken')
             // this.$store.dispatch('clearApplications')

             return;
             }
             let param = {
             token: this.$store.state.user.token
             }
             $.ajax({
             url: `${this.appConfig.apiBase}user/checkCurrentToken/${new Date().valueOf()}`,
             type: 'POST',
             contentType: 'application/json; charset=utf-8',
             data: JSON.stringify(param),
             cache: false,
             async: false,
             success: (data) => {
             if (data.code !== 0) {
             if (this.$route.path !== '/') {
             this.$router.push('/')
             this.$message.alert('登陆超时！')
             }
             this.$store.dispatch('clearToken')
             // this.$store.dispatch('clearApplications')
             }
             },
             error: (data) => {
             if (this.$route.path !== '/') {
             this.$router.push('/')
             this.$message.alert('登陆超时！')
             }
             this.$store.dispatch('clearToken')
             // this.$store.dispatch('clearApplications')
             }
             })*/
        }
    }
}).$mount('#app');
