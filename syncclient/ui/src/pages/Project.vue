<template>
    <div id="project" class="container">
        <div class="breadcrumb">
            <router-link to="/">主页</router-link>
            <a class="bc-active">项目详情</a>
        </div>
        <div class="project-header clearfix">
            <h1 class="title">{{$route.params.name}}</h1>
            <div class="ctrl-bar fr">
                <button class="btn btn-pri" id='prev'>预览</button>
                <router-link id ='datatype' class="btn btn-pri btn-link" :to="{name: 'refs'}">数据类型</router-link>
                <a id = 'exportlo'class="btn btn-pri btn-link"
                   :href="`${this.appConfig.apiBase}project/download/${this.$route.params.id}`"
                   :download="`${this.$route.params.name}`+'.json'">导出到本地</a>
                <button id = 'exportli'class="btn btn-pri fr exportli" @click="exportLicenseModal">导出到生成平台</button>
            </div>
        </div>
        <div class="search-bar">
            <label class="search-label">
                <span class="search-title" >名称</span>
                <input class="search-input search-input-name" type="text">
            </label>
            <label class="search-title">
                <span class="search-title">描述</span>
                <input class="search-input search-input-desc" type="text">
            </label>
        </div>
        <div class="api-list panel">
            <div class="panel-header">
                <div class="panel-title">API列表</div>
                <div class="import-list">
                    <router-link :to="{name: 'createApi', params: {id: this.$route.params.id}}">+ 新建</router-link>
                </div>
            </div>
            <div class="panel-body">
                <div class="api-table-wrapper">
                    <table class="api-table" border="1" bordercolor="#ccc" cellspacing="0">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>名称</th>
                            <th>描述信息</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <ApiItem v-for="(api, index) in apis" :api="api" :index="index + 1"></ApiItem>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    import ApiItem from '../components/project/ApiItem.vue'

    export default {
        components: {ApiItem},
        data () {
            return {}
        },
        computed: {
            apis () {
                return this.$store.state.apis.apis
            }
        },
        methods: {
            exportLicenseModal(){
                var _this = this;
                this.$message.modal({
                    header: '导出到生成平台',
                    body: ` <form action="http://10.10.11.107:8080/apiMgmt/project/upload2GeneratePlat" id='exportForm' method='post' enctype='multipart/form-data'>
                    <button>用户名</button> <input type="text" name="userName" required><br>
                    <button>密码</button> <input type="text" name="passWord" required><br>
                    <button>用户组列表</button> <input type="text" name="groupName" required><br>
                    <input type="text" name="projectID"  hidden id='file'>
                </form>`,
                    primaryClick: () => {
                        var fileInput = document.getElementById('file');
                        fileInput.value = _this.$route.params.id;
                        console.log(_this.$route.params.id);
                        console.log($('#exportForm'));
                        var  formData = $('#exportForm').serializeArray();
                        var o = {};
                        $.each(formData, function() {
                            if (o[this.name]) {
                                if (!o[this.name].push) {
                                    o[this.name] = [o[this.name]];
                                }
                                o[this.name].push(this.value || '');
                            } else {
                                o[this.name] = this.value || '';
                            }
                        });
                        console.log(o);
                        console.log(JSON.stringify(o));
                        $.ajax({
                            url: 'http://10.10.11.107:8080/apiMgmt/project/upload2GeneratePlat',
                            type: 'POST',
                            data:JSON.stringify(o),
                            cache: false,
                            processData: false,
                            contentType: 'application/json; charset=utf-8',
                            success: (data) => {
                                if (data.code === 0) {
                                    this.$message.hide();
                                    this.$message.alert('导出到生成平台成功！');
                                } else {
                                    this.appConfig.showErrorAlert(data.code);
                                }
                            },
                            error: (data) => {
                                this.appConfig.showErrorAlert();
                            }
                        })

                    }
                })
            }
        },
        created () {
            this.$store.dispatch('getApis', this.$route.params.id);
        },
        beforeDestroy () {
            this.$store.dispatch('clearApis');
        }
    }
</script>
<style>
    #exportli,#exportlo,#prev,#datatype{
        width: 108px;
    }
    #project {
        /*margin-top: 80px;*/
    }

    .project-header {
        border-bottom: 1px solid #ccc;
        margin-bottom: 20px;
    }

    .btn-link {
        box-sizing: border-box;
        font-size: 14px;
    }

    .btn-link:hover {
        text-decoration: none;
    }

    .title {
        float: left;
    }

    .api-table-wrapper {
        width: 90%;
        margin: 0 auto;
    }


</style>
