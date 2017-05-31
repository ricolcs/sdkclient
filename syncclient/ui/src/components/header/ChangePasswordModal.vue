<template>
	<div class="modal change-password-modal" v-if="!hidden">
		<div class="modal-dialog">
			<div class="modal-content clearfix">
				<div class="modal-header">修改密码</div>
				<div class="modal-body clearfix">
					<div class="form-group">
						<input type="password"
							class="oldPassword"
							v-model="oldPassword"
							check-type="required password"
                            required-message="请输入原密码"
							placeholder="请输入原密码">
					</div>
					<div class="form-group">
						<input type="password"
							class="newPassword"
							v-model="newPassword"
							id="newPassword"
							check-type="required password"
                            required-message="请输入新密码"
							placeholder="请输入修改密码">
					</div>
					<div class="form-group re-password">
						<input type="password"
							class="rePassword"
							check-type="required"
							equal="#newPassword"
                            required-message="请重复新密码"
							placeholder="请确认修改密码">
					</div>
					<button class="btn btn-primary change-psw-btn" @click="changePassword">确定</button>
				</div>
			</div>
			<span class="modal-close" @click="hide"></span>
		</div>
	</div>
</template>
<script>
import Vue from 'vue'
import $ from 'jquery'
import { mapGetters } from 'vuex'
import SHA1 from 'sha1'


export default {
	data () {
		return {
			hidden: true,
			oldPassword: '',
			newPassword: ''
		}
	},
	computed: {
		...mapGetters({
			user: 'user'
		})
	},
	methods: {
		show (options) {
			this.hidden = false;
		},
		hide () {
			this.hidden = true;
			$('.modal input').val('')
		},
		changePassword (e) {
			e.preventDefault();
			if ($(".change-password-modal").valid(this, '内容出错') === false) {
	            return false;
	        }
			let param = {
				userName: this.user.username,
				oldPassword: SHA1(this.oldPassword),
				newPassword: SHA1(this.newPassword)
			}
			$.ajax({
				url: `${this.appConfig.apiBase}user/changePassword`,
				type: 'POST',
        		contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(param),
				cache: false,
				processData: false,
				success: (data) => {
					if (data.code === 0) {
						this.$message.alert('修改成功,请重新登陆！');

						let param = {
							token: this.user.token
						}
						$.ajax({
							url: `${this.appConfig.apiBase}user/logout`,
							type: 'POST',
			        		contentType: 'application/json; charset=utf-8',
							data: JSON.stringify(param),
							cache: false,
							success: (data) => {
							},
							error: (data) => {
							}
						})
						this.$store.dispatch('clearToken')

						this.hidden = true;
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
	watch: {
		hidden: function(hidden) {
			this.newPassword = '';
			this.oldPassword = '';
			Vue.nextTick(function() {
				if (hidden === false) {
					$('.change-password-modal').validation();
				}
			})
		}
	}
}
</script>
<style>

.form-group.re-password {
	width: 67%;
	display: inline-block;
}
.change-password-modal input {
	height: 38px;
	vertical-align: bottom;
	display: block;
}
.change-psw-btn {
	vertical-align: top;
	margin-top: -4px;
}

</style>
