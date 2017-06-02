<template>
    <tr id='haixinyuanItem'>
        <td id='statisticalTime'>{{date}}</td>
        <td id='vesselTotal'>{{haixinyuan.vesselTotal}}</td>
        <td id='vesselCreateTotal'>{{haixinyuan.vesselCreateTotal}}</td>
        <td id='vesselUpdateTotal'>{{haixinyuan.vesselUpdateTotal}}</td>
        <td id='vesselPositionUpdateTotal'>{{haixinyuan.vesselPositionUpdateTotal}}</td>
        <td id='vesselPositionCreateTotal'>{{haixinyuan.vesselPositionCreateTotal}}</td>

    </tr>
</template>
<script>
    export default {
        props: ['haixinyuan'],

        computed: {
            date () {
                return this.appConfig.parseDateTime(this.haixinyuan.statisticalTime);
            }
        },
        mounted () {
            if (this.haixinyuan.isLast) {
                $('.haixinyuan-table').dataTable(this.appConfig.dataTableChinese('统计时间'));
            }
        }
    }
</script>
<style>

    #copyProjectName {
        width: 65%;
        margin-left: 10px;
        padding-left: 20px;
        background: #f3f7fa;
        border-radius: 21px;
        outline: none;
        border: none;
        margin-bottom: 10px;
        color: #b9cedb;
        font-size: 12px;
    }

    #newProjectName {
        font-size: 12px;
        color: #91b0c4;
    }

    table.sorting-table {
        cursor: move;
    }

    table tr.sorting-row td {
        background-color: #eee;
    }

    #haixinyuanItem > td {
        font-size: 12px;
        color: rgb(102, 102, 102);
        text-align: left;
        padding-left: 20px;
    }

    #haixinyuanItem .id a {
        font-size: 12px;
        color: #01A2F5;
    }

    #haixinyuanItem .id a:hover {
        text-decoration: none;
    }

    #buildTime {
        width: 260px;
        font-size: 12px;
        color: rgb(255, 166, 0) !important;
    }

    .icon div {
        width: 17px;
        height: 17px;
        margin-right: 21px;
        background-color: #fff;
        border-radius: 11px;
        box-shadow: none;
    }

    .build span {
        width: 17px;
        height: 17px;
        display: inline-block;
        background: url(../../maimg/build.png) no-repeat;
    }

    .build:hover {
        background-color: #e3f2fa;
    }

    .build:hover span {
        background: url(../../maimg/Cbuild.png) no-repeat;
    }

    .edit span {
        width: 17px;
        height: 17px;
        display: inline-block;
        background: url(../../maimg/edit.png) no-repeat;
    }

    .edit:hover {
        box-shadow: none;
        background-color: #e3f2fa;
    }

    .edit:hover span {
        background: url(../../maimg/Cedit.png) no-repeat;
    }

    .del span {
        width: 17px;
        height: 17px;
        display: inline-block;
        background: url(../../maimg/del.png) no-repeat;
    }

    .del:hover {
        box-shadow: none;
        background-color: #e3f2fa;
    }

    .del:hover span {
        background: url(../../maimg/Cdel.png) no-repeat;
    }
</style>