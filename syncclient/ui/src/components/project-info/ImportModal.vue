<template>
	<div class="modal" v-if="!hidden">
		<div class="modal-dialog">
			<div class="modal-content clearfix">
				<div class="modal-header">导入项目</div>
				<div class="modal-body clearfix">
					<input type="file">
				</div>
				<div class="modal-footer clearfix">
					<div class="btn btn-pri" @click="">确定</div>
					<div class="btn btn-pri" @click="hide">取消</div>
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
			hidden: true
		}
	},
	methods: {
		show (options) {
			this.hidden = false;
		},
		hide () {
			this.hidden = true;
			$('.modal input').val('')
		}
	}
}
</script>
<style>

</style>