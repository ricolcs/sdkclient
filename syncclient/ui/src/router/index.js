import Vue from 'vue';
import VueRouter from 'vue-router';
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