<template>
    <header>
        <div id="header" class="container">
            <img src="../../maimg/logo.png" alt="" class='logo'>
            <div class="userbar">
                <div v-if="user.token">
                    <a class="user-dropdown">
                        <span>{{userTypeCn}}</span>&nbsp;
                        <span>{{user.username}}</span>
                        <span class="triangle"></span>
                    </a>
                    <div class="user-controller">
                        <router-link v-if="user.userType==='super'" to="/user">用户管理</router-link>
                        <a @click="showChangeEmailModal">修改邮箱</a>
                        <a @click="showChangePasswordModal">修改密码</a>
                        <a @click="logout">安全退出</a>
                    </div>
                </div>
                <div v-else>
                    <a href="javascript:;" @click="showLoginModal">登录</a><a href="javascript:;"
                                                                            @click="showRegisterModal">注册</a>
                </div>
            </div>
        </div>
        <LoginModal ref="loginModal"></LoginModal>
        <ChangePasswordModal ref="changePasswordModal"></ChangePasswordModal>
    </header>
</template>
<script>
    import LoginModal from './LoginModal.vue'
    import ChangePasswordModal from './ChangePasswordModal.vue'
    import { mapGetters } from 'vuex'

    export default {
        components: {LoginModal, ChangePasswordModal},
        computed: {
                ...mapGetters({
                    user: 'user'
                }),
        userTypeCn () {
            return this.user.userType === 'super' ? '管理员' : '用户'
        }
    }
    ,
    methods: {
        showLoginModal()
        {
            this.$refs.loginModal.show();
        }
    ,
        showRegisterModal()
        {
            this.$refs.loginModal.show('reg');
        }
    ,
        showChangePasswordModal()
        {
            this.$refs.changePasswordModal.show();
        }
    ,
        showChangeEmailModal()
        {
            this.$message.modal({
                        header: '修改邮箱',
                        body: `	<div class="form-group">
								<input type="text"
										class="previous-email-input form-control"
										value=${this.user.email}
										disabled/>
							</div>
							<div class="form-group">
								<input type="text"
										class="change-email-input form-control"
										check-type="required mail"
										required-message="请输email"/>
							</div>`,
                        primaryBtn: '修改',
                        primaryClick: () = > {
                        if ($('.modal-body').valid(this, '内容出错') === false)
            {
                return false;
            }
            let email = $('.change-email-input').val(),
                    param = {
                        userName: this.user.username,
                        email: email
                    };
            $.ajax({
                        url: `${this.appConfig.apiBase}user/updateCurrentUserEmail`,
                        type: 'POST',
                        contentType: 'application/json; charset=utf-8',
                        data: JSON.stringify(param),
                        cache: false,
                        success: (data) = > {
                        if (data.code === 0)
            {
                this.$message.hide();
                this.$message.alert('修改成功！')
                this.$store.dispatch('setEmail', email)
            }
        else
            {
                this.appConfig.showErrorAlert(data.code)
            }
        },
            error: (data) =
        >
            {
                this.appConfig.showErrorAlert()
            }
        })
        },
            callback: () =
        >
            {
                $('.modal-body').validation();
            }
        })
        }
    ,
        logout()
        {
            let param = {
                token: this.user.token
            }
            $.ajax({
                        url: `${this.appConfig.apiBase}user/logout`,
                        type: 'POST',
                        contentType: 'application/json; charset=utf-8',
                        data: JSON.stringify(param),
                        cache: false,
                        success: (data) = > {
                        // this.$store.dispatch('clearToken')
                        // this.$store.dispatch('clearProjects')
                        // this.$message.alert('登出成功')
                    },
                    error
        :
            (data) =
        >
            {
                // this.$store.dispatch('clearToken')
                // this.$store.dispatch('clearProjects')
                // this.$message.alert('登出成功')
            }
        })
            this.$store.dispatch('clearToken')
            this.$store.dispatch('clearProjects')
        }
    }
    ,
    mounted()
    {
        if (this.user.token === '') {
            this.showLoginModal();
        }
    }
    ,
    watch: {
        user: {
            handler: function () {
                if (this.user.token === '') {
                    this.$router.push({name: 'home'});
                    this.showLoginModal();
                } else {
                    this.$store.dispatch('getHaixinyuans');
                }
                console.log('user changed')
            }
        ,
            deep: true
        }
    }
    }

</script>
<style>

    header {
        position: relative;
        z-index: 2;
        height: 80px;
        width: 100%;
        /*box-shadow: 0 2px 2px #ccc;*/
        border-bottom: 1px solid #cccccc;
        margin-bottom: 10px;
    }

    #header {
        height: 80px;
        line-height: 80px;
    }

    .logo {
        padding-top: 20px;
        padding-bottom: 20px;
    }

    .userbar {
        float: right;
        position: relative;
    }

    .userbar:hover .user-controller {
        display: block;
    }

    .userbar:hover .triangle {
        transform: rotate(180deg);
    }

    .user-dropdown {
        padding-right: 20px;
    }

    .userbar a {
        display: inline-block;
        line-height: 60px;
        margin-left: 10px;
        text-decoration: none;
        color: #91B0C4;
        font-size: 14px;
    }

    .triangle {

        position: absolute;
        right: 0;
        top: 38px;
        width: 0;
        height: 0;
        border-style: solid;
        border-left: 6px double transparent;
        border-right: 6px double transparent;
        border-bottom: 0;
        border-top: 6px solid #91B0C4;
        transition: transform .3s;
    }

    .user-controller {
        position: absolute;
        display: none;
        right: -5px;
        top: 50px;
        background-color: #fff;
        width: 200px;
        border: 1px solid #ccc;
    }

    .user-controller:before {
        content: '';
        position: absolute;
        z-index: 10;
        right: 80px;
        top: -6px;
        width: 0;
        height: 0;
        border-style: solid;
        border-left: 6px solid transparent;
        border-right: 6px solid transparent;
        border-top: 0 solid #ccc;
        border-bottom: 6px solid #fff;
    }

    .user-controller:after {
        content: '';
        position: absolute;
        right: 80px;
        top: -7px;
        width: 0;
        height: 0;
        border-style: solid;
        border-left: 6px solid transparent;
        border-right: 6px solid transparent;
        border-top: 0 solid #ccc;
        border-bottom: 6px solid #ccc;
    }

    .user-controller a {
        display: block;
        padding: 10px;
        margin: 0;
        line-height: 1.2;
        cursor: pointer;
    }

    .user-controller a:hover {
        background-color: #eee;
    }

    .help-block {
        font-size: 12px;
        color: red;
    }

</style>