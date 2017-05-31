<template>
    <div id="home" class="container">
        <div class="panel-title" id='xiangmuliebiao'>统计数据增长量</div>
        <div class="search-bar">
            <label class="">
                <p style="float:left;margin-top: 5px;">时间间隔:</p>
                <select name="operate-select" class='operate-select' id='operate-select' v-model='apiExcutor'
                        @change='selectInfoByTime'>
                    <option value="hour">时</option>
                    <option value="day">天</option>
                    <option value="month">月</option>
                </select>

            </label>
            <div class="import-list">
                <router-link to="/vesseltotalChart"> 查看折线图</router-link>
            </div>
        </div>
        <div class="haixinyuan-list panel">
            <div class="panel-body">
                <div class="haixinyuan-table-wrapper" v-show='ordinaryTable'>
                    <table class="haixinyuan-table" cellspacing="0">
                        <thead id='thead'>
                        <tr>
                            <th>统计时间</th>
                            <th>船舶总数</th>
                            <th>新增数量</th>
                            <th>更改数量</th>
                            <th>位置变更数量</th>
                            <th>获取位置数量</th>
                        </tr>
                        </thead>
                        <tbody id='tbody'>
                        <HaixinyuanItem v-for="haixinyuan in haixinyuans" :haixinyuan="haixinyuan"></HaixinyuanItem>
                        </tbody>
                    </table>
                </div>
                <div class="topBox" v-show="topTable">

                </div>
            </div>
        </div>
    </div>
</template>
<script type="text/javascript">
    import HaixinyuanItem from '../components/haixinyuan/HaixinyuanItem.vue'
    import $ from 'jquery'

    export default {
        components: {HaixinyuanItem},
        data () {
            return {
                apiExcutor: 'hour',
                topTable: false,
                ordinaryTable: true
            }
        },
        computed: {
            haixinyuans () {
                return this.$store.state.haixinyuans.haixinyuans
            }
        },
        methods: {
            getHaixinyuans () {
                this.$store.dispatch('getHaixinyuans', 'hour');
            },

            selectInfoByTime() {
                if (!this.apiExcutor) {
                    return false;
                }
                //$('.dataTables_paginate').remove();
                this.$store.dispatch('getHaixinyuans', this.apiExcutor);
                this.joinTable();
                //$('.haixinyuan-table').remove();
                //$('.topBox .dataTables_wrapper').remove();
                //$('.topBox').append(str);
            },
            joinTable(){
                var str2 = '';
                console.log(3);
                var arr = this.haixinyuans;
                var _this = this;

                var len = arr.length;
                for (var i = 0; i < len; i++) {
                    var j = i + 1;
                    str2 += '<tr id="haixinyuanItem">' +
                            '<td>' +
                            turn(arr[i].statisticalTime) +
                            '</td>' +
                            '<td>' +
                            arr[i].vesselTotal +
                            '</td>' +
                            '<td>' +
                            arr[i].vesselCreateTotal +
                            '</td>' +
                            '<td>' +
                            arr[i].vesselUpdateTotal +
                            '</td>' +
                            '<td>' +
                            arr[i].vesselPositionUpdateTotal +
                            '</td>' +
                            '<td>' +
                            arr[i].vesselPositionCreateTotal +
                            '</td>' +
                            '</tr>'
                }

                function turn(time) {
                    return _this.appConfig.parseDateTime(time)
                }

                var str = '<table class="haixinyuan-table" cellspacing="0">' +
                        '<thead id="thead">' +
                        '<tr>' +
                        '<th>统计时间</th>' +
                        '<th>船舶总数</th>' +
                        '<th>新增数量</th>' +
                        '<th>更改数量</th>' +
                        '<th>位置变更数量</th>' +
                        '<th>获取位置数量</th>' +
                        '</tr>' +
                        '</thead>' +
                        '<tbody id="tbody">' +
                        str2 +
                        '</tbody>' +
                        '</table>';
                // 将原来普通搜索的表格隐藏
                if ($('.haixinyuan-table').hasClass('dataTable')) {
                    this.ordinaryTable = false;
                    this.topTable = true;
                }
                // 每次更改的时候 我们需要将这个新增加的表格 删除 在添加
                $('.haixinyuan-table').remove();
                $('.topBox .dataTables_wrapper').remove();
                $('.topBox').append(str);
                let data = this.appConfig.dataTableChinese('');
                data.iDisplayLength = 5;
                data.searching = true;
                $('.haixinyuan-table').dataTable(data);
            }
        },
        created () {
            this.getHaixinyuans();
        }
    }

</script>

<style>
    #import-file {
        opacity: 0;
        position: absolute;;
        top: 14px;
        left: 194px;
        cursor: pointer;
        z-index: 10;
    }

    #importInput {
        margin-top: 10px;
        width: 270px;
        height: 30px;
        background-color: #f3f7fa;
        padding-left: 15px;
    }

    #import-form {
        position: relative;
    }

    #importArr {
        display: inline-block;
        width: 18px;
        height: 18px;
        background: url(../maimg/upload.png);
        position: absolute;
        top: 15px;
        right: 80px;
        cursor: pointer;
        z-index: 2;
    }

    .haixinyuan-table {
        position: relative;
        margin-bottom: 20px !important;
    }

    .haixinyuan-tb {
        position: relative;
        margin-bottom: 20px !important;
    }

    .shadow {
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

    table.dataTable thead th, table.dataTable thead td {

    }

    #thead th {
        padding: 5px;
        width: 220px;
        text-align: left;
        height: 40px;
        line-height: 40px;
        font-size: 12px;
        color: #91B0C4;
        padding-left: 20px;
    }

    #tbody tr {
        height: 60px;
        line-height: 20px;
    }

    #tobdy td {
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

    .haixinyuan-table-wrapper {

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
        border-radius: 50px !important;
        height: 30px !important;
    }

    #search-input-name {
        border: 1px solid #f3f7fa;
        background-color: #f3f7fa;
    }

    #search-input-desc {
        border: 1px solid #f3f7fa;
        background-color: #f3f7fa;
    }

    .dataTables_filter {
        display: none;
    }

    .import-list {
        width: 130px;
        height: 30px;
        float: right;
        background-color: #01a2f5;
        text-align: center;
        line-height: 24px;
        border-radius: 30px;
    }

    .import-list a {
        display: inline-block;
        width: 100%;
        height: 100%;
        color: #ffffff;
        font-size: 12px;
    }

    .import-list a:hover {
        text-decoration: none;
    }

    .import-btn {
        float: right;
        height: 30px;
        width: 70px;
        margin-right: 10px;
        background-color: #91b0c4 !important;
        color: #ffffff;
        font-size: 12px !important;
    }

    .operate-select {
        width: 140px;
        height: 30px;
        border: solid 1px #000;
        margin-left: 10px;
    }

    #haixinyuanItem > td {
        font-size: 12px;
        color: rgb(102, 102, 102);
        text-align: left;
        padding-left: 20px;
    }


</style>