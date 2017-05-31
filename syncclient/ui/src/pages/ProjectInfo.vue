<template>
    <div id="project-info" class="container">
        <div class="breadcrumb">
            <router-link to="/">项目列表</router-link>
            <a class="bc-active" v-if="route==='create'">创建项目</a>
            <!-- <router-link to="" v-if="route==='change'">项目详情</router-link> -->
            <a class="bc-active" v-if="route==='change'">修改项目</a>
        </div>
        <h1 v-if="route==='change'">修改项目</h1>
        <h1 v-if="route==='create'">创建项目</h1>
        <div class="panel">
            <div class="panel-header">
                <div class="panel-title">基本信息</div>
            </div>
            <div class="panel-body">
                <form class="project-info-form">
                    <div class="form-group">
                        <div class="col-md-3">
                            <label class="control-label">项目名称</label>
                        </div>
                        <div class="col-md-6 changeProjectInfo">
                            <input type="text"
                                   v-model="title"
                                   check-type="required projectname"
                                   :disabled="!active">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-3">
                            <label class="control-label">访问主机</label>
                        </div>
                        <div class="col-md-6 changeProjectInfo">
                            <input type="text"
                                   v-model="host"
                                   check-type="required" :disabled="!active">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-3">
                            <label class="control-label">接口基路径</label>
                        </div>
                        <div class="col-md-6 changeProjectInfo">
                            <input type="text"
                                   v-model="basePath"
                                   check-type="required" :disabled="!active">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-3">
                            <label class="control-label">项目描述</label>
                        </div>
                        <div class="col-md-6 changeProjectInfo">
                            <textarea v-model="description" :disabled="!active"></textarea>
                        </div>
                    </div>
                    <div class="form-group" v-if="route==='create'">
                        <div class="col-md-3"></div>
                        <div class="col-md-9">
                            <button class="btn btn-pri" @click.prevent="createProject">提交</button>
                            <!-- <button class="btn btn-pri" @click="openImportModal">返回</button> -->
                            <button class="btn btn-pri" @click.prevent="backToHome">返回</button>
                        </div>
                    </div>
                    <div class="form-group" v-else="route='change'">
                        <div class="col-md-3"></div>
                        <div class="col-md-9">
                            <button class="btn btn-pri" @click.prevent="saveProject" v-if="active">保存</button>
                            <button class="btn btn-pri" @click.prevent="toggleActive" v-else>修改</button>
                            <!-- <button class="btn btn-pri" @click="openImportModal">返回</button> -->
                            <button class="btn btn-pri" @click.prevent="backToHome">返回</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <ImportModal ref="importModal"></ImportModal>
    </div>
</template>
<script type="es6">
    import ImportModal from '../components/project-info/ImportModal.vue'
    export default {
        components: { ImportModal },
        data () {
            return {
                title: '',
                description: '',
                route: '',
                active: true,
                host:'',
                basePath:''
            }
        },
        methods: {
            createProject () {
                let param = {
                    host: this.host,
                    basePath: this.basePath,
                    info: {
                        description: this.description,
                        title: this.title,
                        version: '1.0'
                    }
                }
                if ($(".project-info-form").valid(this, '内容出错') === false) {
                    return false;
                }
                $.ajax({
                    url: `${this.appConfig.apiBase}project/createProject`,
                    type: 'POST',
                    contentType: 'application/json; charset=utf-8',
                    cache: false,
                    data: JSON.stringify(param),
                    success: (data) => {
                        if (data.code === 0) {
                            this.$message.alert('创建成功！');
                            this.$router.push('/')
                        } else {
                            this.appConfig.showErrorAlert(data.code);
                        }
                    },
                    error: (data) => {
                        this.appConfig.showErrorAlert('default');
                    }
                })
            },
            openImportModal () {
                e.preventDefault();
                this.$refs.importModal.show()
            },
            saveProject () {
                let param = {
                    host: this.host,
                    basePath: this.basePath,
                    info: {
                        description: this.description,
                        title: this.title,
                        version: '1.0'
                    }
                };
                if ($(".project-info-form").valid(this, '内容出错') === false) {
                    return false;
                }
                $.ajax({
                    url: `${this.appConfig.apiBase}project/updateProjectInfo/${this.$route.params.id}`,
                    type: 'POST',
                    contentType: 'application/json; charset=utf-8',
                    cache: false,
                    data: JSON.stringify(param),
                    success: (data) => {
                        if (data.code === 0) {
                            this.$message.alert('修改成功！');
                            this.$router.push('/')
                        } else {
                            this.appConfig.showErrorAlert(data.code);
                        }
                    },
                    error: (data) => {
                        this.appConfig.showErrorAlert();
                    }
                })
            },
            toggleActive () {
                this.active = !this.active;
            },
            backToHome () {
                this.$router.push('/')
            }
        },
        created () {
            if (this.$route.params.id) {
                this.route = 'change';
                this.title = this.$route.params.projectName;
                this.description = this.$route.params.description;
                this.host = this.$route.params.host;
                this.basePath = this.$route.params.basePath;
                this.active = false;
            } else {
                this.route = 'create'
            }
        },
        mounted () {
            $('.project-info-form').validation();
        }
    }

</script>
<style>
    .panel {
        margin-top: 40px;
    }
    .control-label {
        display: inline-block;
        text-align: right;
        width: 100%;
        font-size: 12px;
        color: rgb(145,176,196);
    }
    .form-group {
        margin-bottom: -10px;
    }
    .changeProjectInfo input,.changeProjectInfo textarea{
        border-radius: 20px !important;
        padding-left: 25px !important;
        background-color: #f3f7fa;
    }

</style>
