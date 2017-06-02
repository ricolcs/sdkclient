<template>
    <div class="main_content">
        <div>
            <div style="float: right; margin-right: 20px;">
                <router-link to="/"> 返回数据列表</router-link>
            </div>
        </div>
        <div id="bedroom1"></div>
        <div id="bedroom2"></div>
        <div id="bedroom3"></div>
        <div id="bedroom4"></div>
        <div id="bedroom5"></div>
    </div>
</template>
<script>
    import echarts from 'echarts'
    export default {
        data() {
            return {}
        },
        computed: {
            haixinyuans () {
                return this.$store.state.haixinyuans.haixinyuans.reverse()
            }
        },
        methods: {
            getHaixinyuans () {
                this.$store.dispatch('getHaixinyuans', 'hour');
            },
            drawPie(){
                var opinionTimes = new Array();
                var opinionValues = new Array();
                var opinionCreateTotal = new Array();
                var opinionUpdateTotal = new Array();
                var opinionPositionUpdateTotal = new Array();
                var opinionPositionCreateTotal = new Array();
                var objs = this.haixinyuans;
                for (var i = 0; i < objs.length; i++) {
                    opinionTimes.push(this.appConfig.parseDateHour(objs[i].statisticalTime));
                    opinionValues.push(objs[i].vesselTotal);
                    opinionCreateTotal.push(objs[i].vesselCreateTotal);
                    opinionUpdateTotal.push(objs[i].vesselUpdateTotal);
                    opinionPositionUpdateTotal.push(objs[i].vesselPositionUpdateTotal);
                    opinionPositionCreateTotal.push(objs[i].vesselPositionCreateTotal);
                }
                this.setOption("船舶总量", opinionTimes, opinionValues, "艘", "bedroom1");
                this.setOption("新增数量", opinionTimes, opinionCreateTotal, "艘", "bedroom2");
                this.setOption("更改数量", opinionTimes, opinionUpdateTotal, "艘", "bedroom3");
                this.setOption("位置变更数量", opinionTimes, opinionPositionUpdateTotal, "艘", "bedroom4");
                this.setOption("获取位置数量", opinionTimes, opinionPositionCreateTotal, "个", "bedroom5");
            },
            setOption(titlename, opinionTimes, opinionValues, unit, id) {
                this.chart = echarts.init(document.getElementById(id));
                this.chart.setOption({
                    title: {
                        text: titlename,
                        subtext: '',
                        left: '50%',
                        top: '1%',
                        textAlign: 'center'
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross'
                        }
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: opinionTimes
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value} ' + unit
                        },
                        axisPointer: {
                            snap: true
                        }
                    },
                    series: [
                        {
                            name: titlename,
                            type: 'line',
                            smooth: true,
                            data: opinionValues
                        }
                    ]
                })
            }
        },
        mounted() {
            this.$nextTick(function () {
                this.drawPie()
            })
        },
        created () {
            this.getHaixinyuans();
        }
    }
</script>
<style scoped>
    #bedroom1, #bedroom2, #bedroom3, #bedroom4, #bedroom5 {
        position: relative;
        width: 20%;
        width: 675px;
        height: 450px;
        border: solid #D01257 1px;
        box-shadow: 0 0 8px #FB90B7;
        border-radius: 10px;
    }

    #bedroom1 {
        margin-top: 10px;
        margin-left: 140px;
        margin-bottom: -450px;
    }

    #bedroom2, #bedroom4 {
        margin-left: 920px;
        margin-bottom: 50px;
    }

    #bedroom3, #bedroom5 {
        margin-left: 140px;
        margin-bottom: -450px;
    }
</style>