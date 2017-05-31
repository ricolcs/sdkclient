import Vue from 'vue';
import VueRouter from 'vue-router';
import Home from '../pages/Home.vue';
import User from '../pages/User.vue';
import ProjectInfo from '../pages/ProjectInfo.vue';
import Project from '../pages/Project.vue';
import Api from '../pages/Api.vue';
import Refs from '../pages/Refs.vue';
import Ref from '../pages/Ref.vue';
import Haixinyuan from '../haixinyuan/Haixiyuan.vue';
import VesseltotalChart from '../haixinyuan/VesseltotalChart.vue';

Vue.use(VueRouter);

const router = new VueRouter({
	routes: [
		{
			path: '',
			name: 'haixinyuan',
			component: Haixinyuan
		},
		{
			path: '/haixinyuan',
			name: 'haixinyuan',
			component: Haixinyuan
		},
		{
			path: '/vesseltotalChart',
			name: 'vesseltotalChart',
			component: VesseltotalChart
		}
	]
})

// router.beforeEach(function() {
// 	console.log(999999)
// })

export default router;