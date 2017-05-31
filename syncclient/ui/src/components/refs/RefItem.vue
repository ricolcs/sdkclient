<template>
<tr>
	<td>
		<router-link :to="`/project/${$route.params.id}/refs/${refItem.defName}`">{{refItem.defName}}</router-link>
	</td>
	<td>{{refItem.description}}</td>
	<td><button class="btn btn-sm" @click="deleteRef">删除</button></td>
</tr>
</template>
<script>
export default {
	props: ['refItem'],
	methods: {
		deleteRef () {
			let params = {
				defName: this.refItem.defName
			}
			$.ajax({
				url: `${this.appConfig.apiBase}api/def/delete/root/${this.$route.params.id}`,
				type: 'POST',
				contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(params),
				cache: false,
				success: (data) => {
					if (data.code === 0) {
						this.$message.alert('删除成功');
						this.$store.dispatch('getRefs', this.$route.params.id)
					} else {
						this.appConfig.showErrorAlert(data.code);
					}
				},
				error: (data) => {
					this.appConfig.showErrorAlert('default');
				}
			})
		}
	},
	mounted () {
		if (this.refItem.isLast) {
			$('.refs-table').dataTable(this.appConfig.dataTableChinese('名称'))
		}
	}
}
</script>