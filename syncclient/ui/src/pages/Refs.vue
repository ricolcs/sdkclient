<template>
	<div id="refs" class="container">
		<div class="breadcrumb">
			<router-link to="/">项目列表</router-link>
			<router-link :to="{name: 'project', params: {id: this.$route.params.id}}">API列表</router-link>
			<a class="bc-active">数据类型</a>
		</div>
		<div class="panel">
			<div class="panel-header">
				<div class="panel-title">模板列表</div>
				<div class="import-list"><router-link :to="{name: 'createRef', params: {id: this.$route.params.id}}">+ 新建</router-link></div>
			</div>
			<div class="panel-body">
				<div class="refs-table-wrapper">
					<table class="refs-table" border="1" bordercolor="#ccc" cellspacing="0">
						<thead>
							<tr>
								<th>名称</th>
								<th>描述</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<RefItem v-for="refItem in refs" :refItem="refItem"></RefItem>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</template>
<script type="es6">
import RefItem from '../components/refs/RefItem.vue'

export default {
	components: { RefItem },
	computed: {
		refs () {
			return this.$store.state.refs.refs;
		}
	},
	created () {
		this.$store.dispatch('getRefs', this.$route.params.id)
	}
}
</script>
<style>
.refs-table-wrapper {
	width: 90%;
	margin: 40px auto;
}
</style>