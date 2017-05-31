<template>
	<div id="ref" class="container">
		<div class="breadcrumb">
			<router-link to="/">项目列表</router-link>
			<router-link :to="{name: 'project', params: {id: this.$route.params.id}}">API列表</router-link>
			<router-link :to="{name: 'refs', params: {id: this.$route.params.id}}">数据类型</router-link>
			<a class="bc-active">模版详情</a>
		</div>
		<div class="panel">
			<div class="panel-header">
				<div class="panel-title">数据类型</div>
			</div>
			<div class="panel-body">
				<div class="col-md-3" v-if="status!=='create'">
					<div class="refs-tree-area">
						<span @click="add">+</span>
						<div ref="tree"
								@click="changeStatus"></div>
					</div>
				</div>
				<div class="col-md-9">
					<div class="parent-options" v-if="status==='root' || status==='create'">
						<div class="param-group">
							<div class="col-md-3">
								<div class="control-label">模板名称</div>
							</div>
							<div class="col-md-6">
								<input type="text" v-model="defName" :disabled="status!=='create'">
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-3">
								<div class="control-label">模板类型</div>
							</div>
							<div class="col-md-6">
								<select v-model="type" :disabled="status!=='create'">
									<option value="string">string</option>
									<option value="array">array</option>
									<option value="object">object</option>
									<option value="int">int</option>
									<option value="long">long</option>
									<option value="float">float</option>
									<option value="double">double</option>
									<option value="decimal">decimal</option>
									<option value="file">file</option>
									<option value="boolean">boolean</option>
									<option value="ref">ref</option>
								</select>
							</div>
						</div>
						<div class="param-group" v-if="type==='ref'">
							<div class="col-md-3">
								<div class="control-label">ref</div>
							</div>
							<div class="col-md-6">
								<select v-model="defaultValue">
									<option v-for="ref in refs" :value="ref.defName" v-if="ref.defName !== defName">
										{{ref.defName}}
									</option>
								</select>
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-3">
								<div class="control-label">描述</div>
							</div>
							<div class="col-md-6">
								<textarea v-model="description"></textarea>
							</div>
						</div>
						<div class="btn-group col-offset-3">
							<button class="btn btn-pri" @click="saveRef">保存</button>
						</div>
					</div>
					<div class="child-options" v-else>
						<div class="param-group">
							<div class="col-md-3">
								<div class="control-label">节点名称</div>
							</div>
							<div class="col-md-6">
								<input type="text" v-model="sub.name" disabled>
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-3">
								<div class="control-label">描述</div>
							</div>
							<div class="col-md-6">
								<textarea v-model="sub.description"></textarea>
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-3">
								<div class="control-label">类型</div>
							</div>
							<div class="col-md-6">
								<select v-model="sub.type">
									<option value="string">string</option>
									<option value="array">array</option>
									<option value="object">object</option>
									<option value="int">int</option>
									<option value="long">long</option>
									<option value="float">float</option>
									<option value="double">double</option>
									<option value="decimal">decimal</option>
									<option value="file">file</option>
									<option value="boolean">boolean</option>
									<option value="ref">ref</option>
								</select>
							</div>
						</div>
						<div class="param-group" v-if="['ref','object','array'].indexOf(sub.type)>-1">
							<div class="col-md-3">
								<div class="control-label">ref</div>
							</div>
							<div class="col-md-6">
								<select v-model="sub.default">
									<option v-for="ref in $store.state.refs.refs" :value="ref.defName"
											v-if="ref.defName !== defName">{{ref.defName}}
									</option>
								</select>
							</div>
						</div>
						<div class="param-group" v-else>
							<div class="col-md-3">
								<div class="control-label">默认值</div>
							</div>
							<div class="col-md-6">
								<input type="text" v-model="sub.default">
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-3">
								<div class="control-label">是否必须</div>
							</div>
							<div class="col-md-6">
								<select v-model="sub.required">
									<option value="true">是</option>
									<option value="false">否</option>
								</select>
							</div>
						</div>
						<div class="btn-group col-offset-3">
							<button class="btn btn-pri" @click="saveSub">保存</button>
							<button class="btn btn-pri" @click="deleteSub">删除</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>
<script type="es6">


export default {
	data () {
		return {
			defName: this.$route.params.refName || '',
			status: 'root',

			description: '',
			type: '',
			defaultValue: '',

			children: [],

			sub: {}

		}
	},
	computed: {
		refs(){
			return this.$store.state.refs.refs;
		}
	},
	methods: {
		saveRef () {
			let params = {};

			this.type === 'ref' ? params = {
				defName: this.defName,
				description: this.description,
				type: this.type,
				value: this.defaultValue,
			} : params = {
				defName: this.defName,
				description: this.description,
				type: this.type,
			}
			var arr = [];
			for(var i=0;i<this.refs.length;i++)
			{
				arr.push(this.refs[i].defName);
			}
			if (arr.indexOf(this.defName) != -1) {
				this.$message.alert('项目已经存在！');
				return false;
			}
			if (this.status === 'create') {
				$.ajax({
					url: `${this.appConfig.apiBase}api/def/save/root/${this.$route.params.id}`,
					type: 'POST',
					contentType: 'application/json; charset=utf-8',
					data: JSON.stringify(params),
					cache: false,
					success: (data) => {
						if (data.code === 0) {
							this.$message.alert('保存成功！');
							this.$router.go(-1)
						} else {
							this.appConfig.showErrorAlert(data.code);
						}
					},
					error: (data) => {
						this.appConfig.showErrorAlert(data.code);
					}
				})
			} else {
				$.ajax({
					url: `${this.appConfig.apiBase}api/def/update/root/${this.$route.params.id}`,
					type: 'POST',
					contentType: 'application/json; charset=utf-8',
					data: JSON.stringify(params),
					cache: false,
					success: (data) => {
						if (data.code === 0) {
							this.$message.alert('保存成功！');
							this.getRef();
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
		getRef () {
			let params = {
				defName: this.$route.params.refName
			}
			$.ajax({
				url: `${this.appConfig.apiBase}api/def/getDef/${this.$route.params.id}?defname=${this.$route.params.refName}`,
				type: 'GET',
				contentType: 'application/json; charset=utf-8',
				cache: false,
				success: (data) => {
					if (data.code === 0) {
						if (data.data.$ref) {
							this.type = 'ref';
							this.defaultValue = data.data.$ref.substr(data.data.$ref.lastIndexOf('/') + 1);
							this.children = [];
						} else {
							this.type = 'object';
							this.children = data.data.properties;
						}
						this.description = data.data.description;
						this.setTree();
					} else {
						this.appConfig.showErrorAlert(data.code);
					}
				},
				error: (data) => {
					this.appConfig.showErrorAlert('default');
				}
			})
		},
		saveSub () {
			let params = {
				defName: this.defName,
				name: this.sub.name,
				description: this.sub.description,
				schemaType: this.sub.type,
				value: this.sub.default,
				required: this.sub.required
			}
			$.ajax({
				url: `${this.appConfig.apiBase}api/def/save/sub/${this.$route.params.id}`,
				type: 'POST',
				contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(params),
				cache: false,
				success: (data) => {
					if (data.code === 0) {
						this.$message.alert('保存成功');
					} else {
						this.appConfig.showErrorAlert(data.code);
					}
				},
				error: (data) => {
					this.appConfig.showErrorAlert();
				}
			})
		},
		deleteSub () {
			let params = {
				defName: this.defName,
				name: this.sub.name,
			}
			$.ajax({
				url: `${this.appConfig.apiBase}api/def/delete/sub/${this.$route.params.id}`,
				type: 'POST',
				contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(params),
				cache: false,
				success: (data) => {
					if (data.code === 0) {
						this.$message.alert('删除成功');
						this.deleteNode();
					} else {
						this.appConfig.showErrorAlert(data.code);
					}
				},
				error: (data) => {
					this.appConfig.showErrorAlert();
				}
			})
		},
		setRefData (name) {
			if (this.status === 'sub' && this.children && this.children[name]) {
				this.sub = this.children[name]
				if (this.children[name].$ref) {
					this.sub.value = this.children[name].$ref.slice(this.children[name].$ref.lastIndexOf('') + 1)
				}
			}
		},
		deleteNode () {
			this.treeDom.jstree(true).delete_node(this.treeDom.jstree(true).get_selected(true));
			this.treeDom.jstree(true).select_node('j1_1');
			this.changeStatus();
		},
		add () {
			let _this = this;
			if (this.type === 'ref') {
				this.$message.alert('模板类型为ref时不可添加子节点')
			} else {
				this.$message.modal({
					header: '创建子节点',
					body:　`<span>名称</span><input type="text" class="newName" />`,
					primaryClick: () => {
						var name = $('.newName').val();
						console.log(_this);
						//_this.children[name] = {
						//	"items": null,
						//	"maxItems": null,
						//	"minItems": null,
						//	"uniqueItems": null,
						//	"allowEmptyValue": null,
						//	"vendorExtensions": {},
						//	"example": null,
						//	"access": null,
						//	"title": null,
						//	"xml": null,
						//	"position": null,
						//	"readOnly": null,
						//	"format": "",
						//	"required": false,
						//	"description": null,
						//	"name": name,
						//	"type": "string"
						//};
						let ref = this.treeDom.jstree(true),
								sel = ref.get_selected();
						sel = ref.get_parent(sel) === '#' ? sel : ref.get_parent(sel);
						sel = ref.create_node(sel, {"type": "file"});
						ref.deselect_node(ref.get_selected());
						ref.select_node(sel);
						this.treeDom.jstree(true).rename_node(this.treeDom.jstree(true).get_selected(true), $('.newName').val());
						this.sub = {name: $('.newName').val()};
						this.status = 'sub';
						this.setRefData (name);
						this.$message.hide();
					}
				})
			}
		},
		changeStatus () {
			setTimeout(() => {
				let ref = this.treeDom.jstree(true),
						sel = ref.get_selected(true),
						parant;
				if (sel.length > 0) {
					parent = ref.get_parent(sel[0].id);
					if (parent === '#') {
						this.status = 'root';
					} else{
						this.status = 'sub';
						this.setRefData(sel[0].text);
					}
				}
			},0)
		},
		getTreeData () {
			let data = {};
			data.text = this.defName;
			data.children = [];
			if (this.type === 'ref') {
				return data;
			} else {
				// this.children.forEach(function(child) {
				// 	data.children.push({text: ""})
				// })
				for (let child in this.children) {
					data.children.push({text: child})
				}
			}
			return data;
		},
		setTree () {
			this.treeDom = $(this.$refs.tree);
			this.treeDom.jstree({
				"core": {
					"animation": 0.5,
					"check_callback": true,
					"themes": {
						"responsive": false
					},
					"data": this.getTreeData()
				},
				"state": {
					"key": "demo2"
				},
				"plugins": ["unique", "state", "types", "wholerow", "changed"]
			});
			this.initTree();
		},
		initTree () {
			this.treeDom.on('loaded.jstree', () => {
				this.treeDom.jstree(true).open_all()
				// this.treeDom.jstree(true).select_node('j1_1')

				setTimeout(() => {
					this.changeStatus()
				},0)
			})
		},
	},
	created () {
		if (this.$route.name === 'createRef') {
			this.status = 'create';
		} else {
			this.getRef();
		}
		this.$store.dispatch('getRefs', this.$route.params.id);
	}
}
</script>
<style>
#ref .panel-body {
	padding: 0;
	height: 500px;
}
.parent-options, .child-options {
	height: 460px;
}
.refs-tree-area {
	height: 460px;
	overflow: auto;
}
</style>