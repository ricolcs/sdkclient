<template>
	<div class="modal" v-if="!hidden">
		<div class="modal-dialog">
			<div class="modal-content clearfix">
				<div class="modal-header" v-html="header"></div>
				<div class="modal-body clearfix" v-html="body"></div>
				<div class="modal-footer clearfix" id='modal-footer'>
					<div class="btn btn-primary modal-primary-btn modal-footer-primary" @click="primaryClick" id='modal-primary'>{{primaryBtn}}</div>
					<div class="btn btn-default modal-default-btn" @click="defaultClick" id='modal-default'>{{defaultBtn}}</div>
				</div>
			</div>
			<span class="modal-close" @click="hide"></span>
		</div>
	</div>
</template>
<script>
import $ from 'jquery'

export default {
	data () {
		return {
			header: '提示',
			body: '',
			footer: '',
			primaryBtn: '确定',
			defaultBtn: '取消',
			primaryClick: () => {
				this.hide()
			},
			defaultClick: () => {
				this.hide()
			},
			hidden: true
		}
	},
	methods: {
		show (options) {
			this.hidden = false;
			this.header = options.header ? options.header : '提示';
			this.body = options.body ? options.body : '';
			this.footer = options.footer ? options.footer : '';
			this.primaryBtn = options.primaryBtn ? options.primaryBtn : '确定';
			this.defaultBtn = options.defaultBtn ? options.defaultBtn : '取消';
			options.primaryClick ? this.primaryClick = options.primaryClick : false;
			options.defaultClick ? this.defaultClick = options.defaultClick : false;

		},
		hide () {
			this.hidden = true;
		}
	}
}
</script>
<style>
  
#modal-content{
margin-top: 20px;
margin-left: 65px;
}
#modal-dialog{
    box-shadow: 6px 0px 10px #eef3f5, -5px 9px 10px #eef3f5;
    width: 442px;
    height: 188px;
    background-color: rgba(255,255,255,0.95);
}
#modal-footer div{
    margin-top: 10px;
}
.btn.btn-pri{
        background-color: #01a2f5;
        color: white;
        font-size: 12px;
    }
.modal {
	position: fixed;
	top: 0;
	right: 0;
	left: 0;
	bottom: 0;
	z-index: 100;
	overflow: auto;
}
.modal-header {
	font-size: 16px;
	margin-bottom: 10px;
}
.modal-dialog {
	position: relative;
	width: 500px;
	margin: 20% auto 50px;
	border-radius: 4px;
	padding: 20px 20px;
	background-color: #fff;
	box-sizing: border-box;
	animation: modalSlide .3s;
}
.modal-close {
	position: absolute;
	right: 10px;
	top: 10px;
	cursor: pointer;
	width: 13px;
	height: 13px;
	background: url('../maimg/circleClose.png') no-repeat;
}
.modal-close:hover {
    background: url('../maimg/circleClose.png') no-repeat;
}
.modal-close:hover {
	color: red;
}
.input-group {
	width: 60%;
	margin: 0 auto;
}
.input-group input, .input-group select {
	border: none;
	border-radius: 0;
	border-bottom: 1px solid #999;
}
.input-group input:focus {
	border-color: #8dc6f3;
}
.modal-footer {
	float: right;
    margin-right: 25%;
}
.modal-primary-btn, .modal-default-btn {
	float: left;
	line-height: 45px;
	text-align: center;
	border-radius: 6px;
	margin-left: 10px;
}

@keyframes modalSlide {
	from { margin-top: 0px; opacity: 0; }
	to { margin-top: 20%; opacity: 1; }
}
#exportli button{
    width: 110px;
    height: 30px;
    margin-bottom:15px;
}
#exportli input{
    width: 200px;
    height: 30px;
    font-size: 18px;
    line-height: 30px;
}

#modalDialog{
   box-shadow: 6px 0px 10px #eef3f5, -5px 9px 10px #eef3f5;
    width: 442px;
    height: 160px;
    background-color: rgba(255,255,255,0.95);
}
    #modal-header{
        margin-top: 20px !important;
        margin-bottom: 20px !important;
        margin-left: 95px;
        font-size: 16px !important;
        color: rgb(153,153,153);
    }
    .delModalFooter{
      margin-left: 95px;
    }
    #delPrimary{
        font-size: 12px;
        color: rgb(255,255,255);
        background:#f46e6e;
        width: 100px;
        height: 30px;
        line-height: 30px;
        text-align: center;
        padding:0 !important;
        border-radius:20px; 
        display:inline-block;
        margin-right: 10px;
        cursor: pointer;
    }
    #delDefault{
      display: inline-block;
        font-size: 12px;
        color: rgb(255,255,255);
        width: 70px;
        height: 30px;
        background:#91B0C4;
        line-height: 30px;
        text-align: center;
        padding:0 !important;
        border-radius:20px; 
        cursor: pointer;
    }
</style>
