<template>
	<tr>
		<td :dataUrl='api.url'>
			<router-link :to="{name: 'api', params: {id: this.$route.params.id, apiName: api.apiName}}">{{index}}</router-link>

		</td>
		<td>
			<router-link :to="{name: 'api', params: {id: this.$route.params.id, apiName: api.apiName}}">{{api.apiName}}</router-link>
		</td>
		<td>{{api.description}}</td>
		<td>
			<button class="btn btn-sm" @click="copyApi">复制</button>
			<button class="btn btn-sm" @click="removeApi">删除</button>
		</td>
	</tr>
</template>
<script>
import RowSorter from '../../plugins/RowSorter.js'

export default {
	props: ['api', 'index'],
	methods: {
		copyApi () {
			this.$message.modal({
				header: '复制API',
				body: `<label><span>请输入新API的名称</span><input type="text" class="newName"/></label><br>
								<label><span>请输入新API的url</span><input type="text" class="newUrl"/></label>`,
				primaryBtn: '复制',
				primaryClick: () => {
					let newName = $('.newName').val(),
							newUrl = $('.newUrl').val(),
							params = {};
					if (!newName) {
						this.$message.alert('请输入新API的名称')
					} else if (!newUrl) {
						this.$message.alert('请输入新API的url')
					} else {
						params = {
							apiName: newName,
							newUrl: newUrl,
							url: this.api.url
						}
						$.ajax({
							url: `${this.appConfig.apiBase}api/copy/${this.$route.params.id}`,
							type: 'POST',
							contentType: 'application/json; charset=utf-8',
							data: JSON.stringify(params),
							cache: false,
							success: (data) => {
								if (data.code === 0) {
									this.$message.hide();
									this.$message.alert('复制成功！');
									this.$store.dispatch('getApis', this.$route.params.id);
								} else {
									this.appConfig.showErrorAlert(data.code);
								}
							},
							error: (data) => {
								this.appConfig.showErrorAlert(data.code);
							}
						})
					}
				}
			})
		},
		removeApi () {
			let params = {
				url: this.api.url
			}
			$.ajax({
				url: `${this.appConfig.apiBase}api/removeApi/${this.$route.params.id}`,
				type: 'POST',
				contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(params),
				cache: false,
				success: (data) => {
					if (data.code === 0) {
						// this.$message.hide();
						this.$message.alert('删除成功！');
						this.$store.dispatch('getApis', this.$route.params.id);
					} else {
						this.appConfig.showErrorAlert(data.code);
					}
				},
				error: (data) => {
					this.appConfig.showErrorAlert(data.code);
				}
			})
		},
		sortApi(tbody){
		   let params = {
		       url:this.api.url
           }
            var obj = {};
            obj.arr = [];
            var ele = tbody.getElementsByTagName('tr');
            var length = ele.length;
            for(var i=length-1;i>=0;i--)
            {
                var j=length-i-1;
                var url = ele[j].children[0].getAttribute('dataUrl');
                console.log(url);
                obj.arr.push({
                    'sort': i,
                    'url': url
                })
            }
            console.log(JSON.stringify(obj.arr));
            $.ajax({
                url: `${this.appConfig.apiBase}api/sort/${this.$route.params.id}`,
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(obj.arr),
                cache: false,
                success: (data) => {
                    if (data.code === 0) {
                        this.$message.alert('跟新排序成功！');
                    } else {
                        this.appConfig.showErrorAlert(data.code);
                    }
                },
                error: (data) => {
                    this.appConfig.showErrorAlert(data.code);
                }
            })
		}
	},
	mounted () {
	    var _this = this;
		if (this.api.isLast) {
			let tableData = this.appConfig.dataTableChinese('名称');
			tableData.searching = true;
			let table = $('.api-table').DataTable(tableData);
			$('.search-input-name').on('keyup change', function () {

				table
					.column(1)
					.search(this.value)
					.draw();
			});
			$('.search-input-desc').on('keyup change', function () {

				table
					.column(2)
					.search(this.value)
					.draw();

			});
		}
		RowSorter('.api-table', {
		    onDrop: function(tbody, row, new_index, old_index) {
		        // 这是计算之后的属性
//                    console.log('ma');;
//                    console.log(row);
//					console.log(new_index);
//					console.log(old_index);
//					console.log(tbody);

               var ele = tbody.getElementsByTagName('tr');
               var length = ele.length;
                 for(var i=0;i<length;i++)
                 {
                    ele[i].children[0].children[0].innerHTML = i+1;
                 }
                _this.sortApi(tbody);
		    }
		})
	}
}
</script>
