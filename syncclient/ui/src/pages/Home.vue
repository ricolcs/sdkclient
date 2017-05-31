<template>
    <div id="home" class="container">
        <div class="panel-title" id='xiangmuliebiao'>项目列表</div>
        <div class="search-bar">
            <label class="search-label">
                <span class="search-title">名称</span>
                <input class="search-input search-input-name" type="text" id='search-input-name' @focus='nameFocus' @blur='nameBlur'>
            </label>
            <label class="search-title">
                <span class="search-title">描述</span>
                <input class="search-input search-input-desc" type="text" id='search-input-desc' @focus="descfoucus" @blur='descBlur'>
            </label>
            <div class="import-list"><router-link to="/createProject"> 新建</router-link></div>
            <button class="btn btn-pri import-btn fr" @click="showImportModal">导入</button>
        </div>
        <div class="project-list panel">
            <div class="panel-body">
                <div class="project-table-wrapper">
                    <table class="project-table"  cellspacing="0">
                        <img src="../maimg/shadow.png" alt="" class='shadow'>
                        <thead id='thead'>
                        <tr>
                            <th>项目ID</th>
                            <th>名称</th>
                            <th>描述</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id='tbody'>
                        <ProjectItem v-for="project in projects" :project="project"></ProjectItem>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</template>
<script type="es6">
    import ProjectItem from '../components/home/ProjectItem.vue'
    
    export default {
        components: { ProjectItem },
        data () {
            return {
            }
        },
        computed: {
            projects () {
                return this.$store.state.projects.projects
            }
        },
        methods: {
            getProjects () {
                this.$store.dispatch('getProjects');
            },
            showImportModal () {
                this.$message.modal({
                    header: '导入项目',
                    body: `<form action="${this.appConfig.apiBase}project/upload" method="post" enctype="multipart/form-data" class="import-form"  id='import-form'>
                    <input type="file" name="swagger" id="import-file" @click='change'/>
                    <input type="text" placeholder='未选择文件' id='importInput'>
                    <span id='importArr'></span>
                    </form>`,
                    primaryClick: () => {
                        let formData = new FormData(document.querySelector('.import-form'));
                        if (formData.get('swagger').name === '') {
                            this.$message.alert('请选择文件！');
                            return;
                        }
                        var importFile = document.getElementById('import-file');
                        console.log(importFile);
                        $.ajax({
                            url: `${this.appConfig.apiBase}project/upload`,
                            type: "POST",
                            data: formData,
                            cache: false,
                            processData: false,
                            contentType: false,
                            success: (data) => {
                                if (data.code === 0) {
                                    this.$message.hide();
                                    this.$message.alert('上传成功！');
                                    this.$store.dispatch('getProjects');
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
            },
             change:()=>{
                        console.log('ma');
            },
            descfoucus(){
                $('#search-input-desc').css({
                    'backgroundColor': '#ffffff',
                    'border': '1px solid #91b0c4'});
            },
            descBlur(){
                $('#search-input-desc').css({
                    'backgroundColor':'#f3f7fa',
                    'border': '1px solid #f3f7fa'
                })
            },
            nameFocus(){
                $('#search-input-name').css({
                    'backgroundColor': '#ffffff',
                    'border': '1px solid #91b0c4'})
            },
            nameBlur(){
                $('#search-input-name').css({
                    'backgroundColor': '#f3f7fa',
                    'border': '1px solid #f3f7fa'})
            }
        },
        created () {
            if (this.$store.state.user.token) {
                this.getProjects();
            }
        }
    }

</script>
<style>
    #import-file{
        opacity: 0;
        position: absolute;;
        top: 14px;
        left: 194px;
        cursor: pointer;
        z-index: 10;
    }
    #importInput{
        margin-top: 10px;
        width: 270px;
        height: 30px;
        background-color: #f3f7fa;
        padding-left: 15px;
    }
    #import-form{
        position:relative;
    }
    #importArr{
        display: inline-block;
        width: 18px;
        height: 18px;
        background:url(../maimg/upload.png);
        position: absolute;
        top: 15px;
        right: 80px;
        cursor: pointer;
        z-index: 2;
    }
    .project-table{
        position: relative;
        margin-bottom: 20px !important;
    }
    .shadow{
        position: absolute;
        top: 2px;
        left: 0;
        z-index: -10;
    }
    #home {
        margin-top: 50px;
    }
    .panel {
        width: 100%;
    }

    table.dataTable thead th, table.dataTable thead td, table.dataTable.no-footer {
        border: none !important;
    }
    table.dataTable thead th, table.dataTable thead td{

    }
    #thead th{
        padding: 5px;
        width: 220px;
        text-align: left;
        height: 40px;
        line-height: 40px;
        font-size: 12px;
        color:#91B0C4;
        padding-left: 20px;
    }
    #tbody tr{
        height: 60px;
        line-height: 20px;
    }
    #tobdy td{
        text-align: left;
        padding-left: 20px;
    }
    #xiangmuliebiao {
        display: inline-block;
        margin-left: 20px;
        font-size: 16px;
        margin-bottom: 25px;
    }
    .import-list {
        float: right;
        margin-right: 20px;
    }

    .project-table-wrapper {

        margin: 0 auto;
        text-align: center;
    }
    table {
        width: 100%;
        text-align: center;
    }
    td, th {
        border: none;
        border-bottom: 3px solid #F0F4F5;
    }

    .search-bar {
        margin-bottom: 25px;
    }
    .search-label {
        display: inline-block;
        width: 20%;
        margin-right: -2%;
    }
    .search-title {
        display: inline-block;
        width: 20%;
        text-align: center;
        color: #91B0C4;
    }
    input.search-input {
        display: inline-block;
        width: 60% !important;
        border-radius:50px !important;
        height: 30px !important;
    }
    #search-input-name{
        border: 1px solid #f3f7fa;
        background-color: #f3f7fa;
    }
    #search-input-desc{
        border: 1px solid #f3f7fa ;
        background-color: #f3f7fa;
    }



    .dataTables_filter {
        display: none;
    }

    .import-list{
        width: 130px;
        height: 30px;
        float: right;
        background-color: #01a2f5;
        text-align: center;
        line-height: 24px;
        border-radius:30px;
    }
    .import-list a{
        display: inline-block;
        width: 100%;
        height: 100%;
        color: #ffffff;
        font-size: 12px;
    }


    .import-list a:hover{
        text-decoration: none;
    }
    .import-btn{
        float: right;
        height: 30px;
        width: 70px;
        margin-right: 10px;
        background-color: #91b0c4 !important;
        color: #ffffff;
        font-size: 12px !important;
    }


</style>
