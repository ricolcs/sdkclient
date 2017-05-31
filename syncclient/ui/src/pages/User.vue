<template>
	<div id="user" class="container">
		<div class="breadcrumb">
			<router-link to="/">项目列表</router-link>
			<a class="bc-active">用户管理</a>
		</div>
		<table class="user-ctrl-table">
			<thead>
				<tr>
					<th>用户名</th>
					<th>真实姓名</th>
					<th>邮箱</th>
					<th>用户角色</th>
					<th>用户状态</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<UserItem v-for="(item,index) in userList"
							:item="item"
							:isLast="index===userList.length-1"
							@refreshUserList="getList()">
				</UserItem>
			</tbody>
		</table>
	</div>
</template>
<script type="es6">
import UserItem from '../components/user/UserItem.vue'
import $ from 'jquery'
import { mapGetters } from 'vuex'


export default {
	components: { UserItem },
	data () {
		return {
			userList: []
		}
	},
	computed: {
		...mapGetters({
		  user: 'user'
		})
	},
	created () {
		if (this.$store.state.user.token === '') {
			this.$message.alert('请先登录');
			router.push('/');
			return;
		}
		this.getList();
		console.log('page user: created');
	},
	methods: {
		getList () {
			$.ajax({
				url: `${this.appConfig.apiBase}user/getUsers/${this.user.username}`,
				type: 'POST',
	    		contentType: 'application/json; charset=utf-8',
				cache: false,
				processData: false,
				success: (data) => {
					if (data.code === 0) {
						this.userList = data.data;
					} else {
						this.appConfig.showErrorAlert(data.code);
					}
				},
				error: (data) => {
					this.appConfig.showErrorAlert('default');
				}
			})
		}
	},
	mounted () {    
	}
}
</script>
<style>

.user-ctrl-table {
	width: 100%;
	text-align: center;
}
.user-ctrl-table tbody tr:nth-child(odd) {
	background-color: #eee
}

</style>