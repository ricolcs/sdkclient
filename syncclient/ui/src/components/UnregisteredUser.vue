<template>
	<tr>
		<td>{{item.username}}</td>
		<td>{{item.realname}}</td>
		<td>{{item.email}}</td>
		<td>{{item.userType}}</td>
		<td>{{item.userStatus}}</td>
		<td><button @click="confirm">通过审核</button></td>
	</tr>
</template>
<script>
import { mapGetters } from 'vuex'
import $ from 'jquery'
import User from '../view/User.vue'

export default {
	props: ['item'],
	computed: {
		...mapGetters({
			user: 'user'
		})
	},
	methods: {
		confirm () {
			let param = {
				userName: this.item.username
			}
			$.ajax({
				url: `${this.appConfig.apiBase}user/confirm/${this.user.username}`,
				type: 'POST',
        		contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(param),
				cache: false,
				success: (data) => {
					this.$emit('refreshUserList')
				},
				error: (data) => {

				}
			})
		}
	},
	created () {
		
	}
}
</script>