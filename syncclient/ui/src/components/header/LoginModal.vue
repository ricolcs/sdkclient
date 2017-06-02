<template>
	<div class="modal login-modal" v-if="!hidden">
		<div class="modal-dialog login-modal-dialog">
			<h2 class="login-title">API生成平台</h2>
			<div class="login-modal-content">
				<div class="login-modal-body clearfix">

					<form class="forget-form" v-if="module==='forget'">
						<div class="input-group">
							<div class="form-group">
								<input type="text"
									class="forget-email"
									check-type="required mail"
									required-message="请输注册邮箱"
									placeholder="注册邮箱">
							</div>
						</div>
						<button @click="retrievePassword" class="btn login-btn">找回密码</button>
						<button @click="show" class="btn login-btn">返回登陆</button>
					</form>
					<form class="reg-form" v-else-if="module==='reg'">
						<div class="input-group">
							<div class="form-group">
								<input type="text"
										class="reg-username"
										check-type="required username"
										checkusername="check"
										required-message="请输入用户名"
										placeholder="用户名">
							</div>
							<div class="form-group">
								<input type="text"
										class="reg-realname"
										check-type="required chinese"
										required-message="请输入真实姓名"
										placeholder="真实姓名">
							</div>
							<div class="form-group">
								<input type="text"
										class="reg-email"
										check-type="required mail"
										checkmail="check"
										required-message="请输入邮箱"
										placeholder="邮箱">
							</div>
							<div class="form-group">
								<input type="password"
										class="reg-password"
										id="reg-password"
										check-type="required password"
										required-message="请输入密码"
										placeholder="密码">
							</div>
							<div class="form-group">
								<input type="password"
										class="reg-re-password"
										check-type="required"
										required-message="请重复密码"
										equal="#reg-password"
										placeholder="重复密码">
							</div>
						</div>
						<button @click="register" class="btn reg-btn"></button>
						<button @click="show" class="btn login-btn">返回登陆</button>
					</form>
					<form class="login-form" v-else>
						<div class="input-group">
							<div class="form-group">
								<div class="input-icon">
									<i class="fa fa-user"></i>
									<input type="text"
										   v-model="username"
										   class="login-username"
										   check-type="required username"
										   required-message="请输入用户名"
										   placeholder="用户名">
								</div>
							</div>
							<div class="form-group">
								<div class="input-icon">
									<i class="fa fa-lock"></i>
									<input type="password"
										   class="login-password"
										   check-type="required password"
										   required-message="请输入密码"
										   placeholder="密码">
								</div>
							</div>
						</div>

						<!--<hr>-->
						<label class="remember-label" @click="changeRemember">
							<span v-if="!remember || remember==='false'"><img class="remember-img" src="../../../public/img/unrem.png"></span>
							<span v-else><img class="remember-img" src="../../../public/img/rem.png"></span>
							<span class="remember-name">记住用户名</span>
						</label>
						<button @click="login" class="btn login-btn">登录</button>


						<div class="nopassword">
							<a @click="showReg" class="logup-btn">账号注册</a>
							<a @click="show('forget')" class="forget-btn">找回密码</a>
						</div>
						<!--<button @click="showReg" class="btn login-modal-reg"></button>-->
					</form>
				</div>
			</div>
			<div class="login-modal-footer"></div>
			<!-- <span class="modal-close" @click="hide"></span> -->
		</div>
	</div>
</template>
<script type="es6">
import Vue from 'vue'
import $ from 'jquery'
import SHA1 from 'sha1'

export default {
	data () {
		return {
			isLogin: true,
			module: 'login',
			hidden: true,
			remember: window.localStorage.getItem('remember') ? window.localStorage.getItem('remember') : false,
			username: window.localStorage.getItem('username') ? window.localStorage.getItem('username') : ''
		}
	},
	computed: {
	},
	methods: {
		show (module) {
			this.hidden = false;
			this.username = window.localStorage.getItem('username');
			this.module = module;
		},
		showReg (e) {
			e.preventDefault();
			$('.login-password').val('');
			this.show('reg');
		},
		hide () {
			this.hidden = true;
			$('.login-form input').val('');
		},
		changeRemember () {
			if (this.remember === 'false') {
				this.remember = true
			} else {
				this.remember = !this.remember;
			}
			if (!this.remember) {
				window.localStorage.removeItem('username');
			}

			window.localStorage.setItem('remember', this.remember);
		},
		login (e) {
			e.preventDefault();
			if ($(".login-form").valid(this, '内容出错') === false) {
	            return false;
	        }
	        $('.login-btn').html('正在登录。。。')
			let param = {
				userName: $('.login-username').val(),
				password: SHA1($('.login-password').val())
			};
			$.ajax({
				url: `${this.appConfig.apiBase}user/login`,
				type: 'POST',
				contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(param),
				cache: false,
				success: (data) => {
					if (data.code === 0) {
						this.$store.dispatch('setToken', {
							username: param.userName,
							email: data.data.email,
							token: data.data.token,
							userType: data.data.userType
						})

						this.hide();
						this.$message.alert('登录成功')
					} else {
						$('.login-btn').html('登录');
						this.appConfig.showErrorAlert(data.code);
					}
				},
				error: (data) => {
					$('.login-btn').html('登录');
					this.appConfig.showErrorAlert();
				}
			})
		},
		register (e) {
			e.preventDefault();
			if ($(".reg-form").valid(this, '内容出错') === false) {
	            return false;
	        }
			let param = {
				userName: $('.reg-username').val(),
				realName: $('.reg-realname').val(),
				password: SHA1($('.reg-password').val()),
				email: $('.reg-email').val(),
				userType: 'general',
				userStatus: ''
			}
			$.ajax({
				url: `${this.appConfig.apiBase}user/register`,
				type: 'POST',
				contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(param),
				cache: false,
				processData: false,
				success: (data) => {
					if (data.code === 0) {
						this.$message.alert('注册成功')
						$('.reg-form input').val('');
						this.show();
					} else {
						// this.$message.alert(data.message)
						this.appConfig.showErrorAlert(data.code);
					}
				},
				error: (data) => {
					this.appConfig.showErrorAlert();
				}
			})
		},
		retrievePassword (e) {
			e.preventDefault();
			if ($(".forget-form").valid(this, '内容出错') === false) {
	            return false;
	        }
	        let param = {email: $('.forget-email').val()}
	        $.ajax({
				url: `${this.appConfig.apiBase}user/retrievePassword`,
				type: 'POST',
				contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(param),
				cache: false,
				processData: false,
				success: (data) => {
					if (data.code === 0) {
						this.$message.alert('找回成功，请登陆邮箱查看');
						this.show();
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
	watch: {
		hidden: function(hidden) {
			Vue.nextTick(() => {
				if (hidden === false) {
					$('.login-modal-body').validation();
				}
			})
		},
		module: function() {
			Vue.nextTick(() => {
				$('.login-modal-body').validation();
			})
		}
	},
	mounted () {
	}
}
</script>
<style>
.login-modal-header {
	text-align: center;
	font-size: 20px;
	font-weight: bolder;
	margin-bottom: 10px;
}
.login-modal-dialog {
	width: 718px !important;
	min-height: 342px !important;
	background-image: url('../../../public/img/1280.png');
	background-repeat: no-repeat;
	background-position: top;
}
.login-modal-dialog .form-group {
	width: 100%;
}
.login-form, .reg-form {
	width: 400px;
	float: right;
	padding-top: 31px;
}
.reg-form {
	margin-top: 10px;
}
.login-form input, .reg-form input {
	padding: 0;
}
.remember-label {
	position: relative;
	display: block;
	width: 100%;
	height: 20px;
	top: -35px;
	font-size: 12px;
	margin: 0 auto;
	line-height: 14px;
	text-align: center;
	font-size: 14px;
	margin-top: 50px;
	padding-left: 42px;
}

.remember-name {
	position: relative;
	top: -13px;
}

.remember-img {
	padding: 10px;
    background-color: #fff;
}
input.login-username {
	margin-top: 18px;
	font-size: 14px;
	color: #999;
}
input.login-password {
	margin-top: 5px;
	font-size: 14px;
	color: #999;
}
.login-remember {
	margin: 1px 4px 0 0;
	vertical-align: top;
	display: block;
	margin: 0 auto 5px;
}
.login-btn, .reg-btn, .login-modal-reg {
	height: 50px;
	margin-top: 10px;
	line-height: 30px;
	border: none;
	outline: none;
	color: #fff;
	cursor: pointer;
}
.login-btn {
	/*background-image: url('../../../public/img/pri-bg.png');*/
	margin-top: 0px;
	margin-bottom: 0px;
	font-size: 14px;
	font-family: "Microsoft yahei";
	width: 238px !important;
	height: 34px !important;
	border-radius: 38px;
	background-color: blue !important;
}
.reg-btn, .login-modal-reg {
	background-image: url('../../../public/img/register.png');
	width: 173px !important;
	height: 50px !important;
}
.login-btn, .reg-btn, .login-modal-reg {
	background-color: #fff;
	box-shadow: none;
	width: 40%;
	margin-left: 33%;
	-webkit-background-size: 100% 100%;
	background-size: 100% 100%;
}
.login-btn:hover, .reg-btn:hover, .login-modal-reg:hover {
	box-shadow: none;
}
hr {
	width: 80%;
	border: none;
	border-bottom: 1px solid #999;
}
.forget-form {
	width: 400px;
	float: right;
	margin-top: 100px;
}

.forget-btn, .logup-btn {
	font-size: 12px;
	cursor: pointer;
	vertical-align: sub;
	margin-left: 10px;
}

.input-group .input-icon > i {
	color: #ccc;
	display: block;
	position: absolute;
	margin: 16px 2px 0px 12px;
	z-index: 3;
	width: 16px;
	font-size: 16px;
	text-align: center;
}

.input-group .input-icon .fa-user {
	margin-top: 30px;
}

.input-group .input-icon input {
	padding-left: 35px;
	border-radius: 31px;
	border: 1px solid #999;

}

.nopassword {
	margin-left: 188px;
}

.input-group {
	padding-left: 101px;
}

.login-modal {
	overflow: auto;
	background-image: url('../../../public/img/bg.jpg');
	background-size: cover;
	background-color: rgba(200, 200, 200, 1) !important;
}

.login-modal .modal-dialog {
	margin-top: 70px;
	border-radius: 0px;
	animation: none;
	position: absolute;
	top: 50%;
	left: 50%;
	-webkit-transform: translate(-50%, -50%);
	-moz-transform: translate(-50%, -50%);
	transform: translate(-50%, -50%);
}

.login-title {
	margin-top: -52px;
	color: #fff;
}
</style>