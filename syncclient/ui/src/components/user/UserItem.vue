<template>
	<tr>
		<td>{{item.userName}}</td>
		<td>{{item.realName}}</td>
		<td>{{item.email}}</td>
		<td>{{item.userType==='super'?'超级管理员':item.userType==='admin'?'管理员':'普通用户'}}</td>
		<td>{{item.userStatus==='success'?'激活':item.userStatus==='denied'?'冻结':'未激活'}}</td>
		<td class="operation">
			<div v-if="item.userType!=='super'">
				<button class="btn-sm" @click="confirm" v-if="item.userStatus==='registering'">通过审核</button>
				<button class="btn-sm" @click="admin" v-else-if="item.userType==='general'">升为管理员</button>
				<button class="btn-sm" @click="general" v-else-if="item.userType==='admin'">降为普通用户</button>
				<button class="btn-sm" @click="freeze" v-if="item.userStatus==='success'">冻结用户</button>
				<button class="btn-sm" @click="confirm" v-if="item.userStatus==='denied'">激活用户</button>
				<button class="btn-sm" @click="remove">移除用户</button>
			</div>
		</td>
	</tr>
</template>
<script>
import { mapGetters } from 'vuex'
import $ from 'jquery'
import User from '../../pages/User.vue'
import Vue from 'vue'

export default {
	props: ['item','isLast'],
	data () {
		return {
		}
	},
	computed: {
		...mapGetters({
			user: 'user'
		})
	},
	methods: {
		confirm () {
			let param = {
				userName: this.item.userName,
				userStatus: 'success'
			}
			this.updateUser(param);
		},
		freeze () {
			let param = {
				userName: this.item.userName,
				userStatus: 'denied'
			}
			this.updateUser(param);
		},
		admin () {
			let param = {
				userName: this.item.userName,
				userType: 'admin'
			}
			this.authorization(param)
		},
		general () {
			let param = {
				userName: this.item.userName,
				userType: 'general'
			}
			this.authorization(param)
		},
		updateUser (param) {
			$.ajax({
				url: `${this.appConfig.apiBase}user/updateUserStatus/${this.user.username}`,
				type: 'POST',
        		contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(param),
				cache: false,
				success: (data) => {
					if (data.code === 0) {
						this.$message.alert('用户状态更新成功！');
						this.$emit('refreshUserList')
					} else {
						this.appConfig.showErrorAlert(data.code);
					}
				},
				error: (data) => {
					this.appConfig.showErrorAlert('default');
				}
			})
		},
		authorization (param) {
			$.ajax({
				url: `${this.appConfig.apiBase}user/authorization/${this.user.username}`,
				type: 'POST',
        		contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(param),
				cache: false,
				success: (data) => {
					if (data.code === 0) {
						this.$message.alert('用户状态更新成功！');
						this.$emit('refreshUserList')
					} else {
						this.appConfig.showErrorAlert(data.code);
					}
					
				},
				error: (data) => {
					this.appConfig.showErrorAlert('default');
				}
			})
		},
		remove () {
			let param = {
				userName: this.item.userName
			}
			$.ajax({
				url: `${this.appConfig.apiBase}user/delete/${this.user.username}`,
				type: 'POST',
        		contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(param),
				cache: false,
				success: (data) => {
					if (data.code === 0) {
						this.$message.alert('删除成功！');
						this.$emit('refreshUserList')
					} else {
						this.appConfig.showErrorAlert(data.code);
					}
					
				},
				error: (data) => {
					this.appConfig.showErrorAlert();
				}
			})
		}
	},
	mounted () {
		if (this.isLast) {
		    $('.user-ctrl-table').dataTable(this.appConfig.dataTableChinese('用户名'));
		}
	}
}
</script>