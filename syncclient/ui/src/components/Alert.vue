<template>
	<div class="alert">
		<div class="alert-dialog">
			<span class="alert-close" @click="hide">×</span>
				<div class="alert-message">{{message}}</div>
		</div>
	</div>
</template>
<script>

export default {
	data () {
		return {
			message: '',
			timer: '',
			hidden: true
		}
	},
	methods: {
		show (message) {
			clearTimeout(this.timer)
			this.message = message;
			this.$el.style.display = 'block';
			this.timer = setTimeout(() => {
				this.hide()
			}, 15000)
		},
		hide () {
			this.$el.style.display = 'none';
		}
	}
}

</script>
<style>

.alert {
	position: fixed;
	top: 200px;
	left: 50%;
	margin-left: -200px;
	z-index: 10000;
}
.alert-dialog {
	position: relative;
	width: 400px;
	padding: 40px;
	background-color: #fff;
	box-shadow: 0 0 4px #ccc;
}
.alert-close {
	position: absolute;
	top: 10px;
	right: 10px;
	width: 15px;
	height: 15px;
	line-height: 15px;
	text-align: center;
	cursor: pointer;
	font-size: 15px;
}
.alert-close:hover {
	color: red;
}
.alert-message {
	word-break: break-word;
	min-height: 300px;
	overflow: auto;
}

</style>