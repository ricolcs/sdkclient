<template>
	<div class="tree">
		<span @click='add'>+</span>
		<div ref="tree" @click="setParamData"></div>
	</div>
</template>
<script>
import $ from 'jquery'
import jstree from 'jstree'
import 'jstree/dist/themes/default/style.css'
import { mapGetters } from 'vuex'

export default {
	props: ['module'],
	data () {
		return {
			treeDom: {}
		}
	},
	computed: {
		...mapGetters({
			params: 'params',
			responses: 'responses'
		})
	},
	methods: {
		add () {
			let ref = this.treeDom.jstree(true),
					sel = ref.get_selected(),get="get";
			sel = ref.get_parent(sel) === '#' ? sel : ref.get_parent(sel);
			sel = ref.create_node(sel, {"type": "file"});
			ref.deselect_node(ref.get_selected());
			ref.select_node(sel);
			if (this.module === 'response') {
				get = "post";
			}
			if(this.treeDom.jstree(true).get_node(this.treeDom.jstree(true).get_parent(sel)).text === "request"){
				this.$emit('newParam', get);
			} else {
				this.$emit('newResponse', get);
			}
		},
		setParamData (init) {
			let oldSel = this.treeDom.jstree(true).get_selected(true);
			setTimeout(() => {
				let ref = this.treeDom.jstree(true),
						sel = ref.get_selected(true),
						parant;
				if (sel.length > 0) {
					parent = ref.get_parent(sel[0].id);
					if (parent === '#') {
//						this.$emit('selectNoParam', this.module, ref.get_selected(true)[0].text);
						// if (sel[0].children[0]) {
						// 	sel = sel[0].children[0];
						// 	sel = ref.get_node(sel);
						// 	parent = ref.get_parent(sel.id);
						// 	ref.deselect_node(ref.get_selected())
						// 	ref.select_node(sel);
						// 	this.$emit('setParamData', ref.get_node(sel.parent).text, sel.text, this.module)
						// } else {
							// this.$emit('seleteNoParam');
						// }
					} else{
						// if (!init) {
						// 	this.$emit('saveParam');
						// }
						this.$emit('setParamData', ref.get_node(sel[0].parent).text, sel[0].text, this.module)
					}
				}
			},0)
		},
		getTreeData () {
			let data = [],requestWayArr = this.$store.state.api.basicInfo.requestWay.split(','),requestWayArr1 = ["request","response"];;

			requestWayArr1.forEach((requestWay, index) => {
				let params = [];
				if (this.module === 'request') {
					params = this.$store.state.api.params[requestWay] || [];
					data.push({text: requestWay, children: []});
					params.forEach(function(param) {
						param.name = param.vendorExtensions['x-name']?param.vendorExtensions['x-name']:param.name;
						data[index].children.push({text: param.name})
					})
				} else {
					params = this.$store.state.api.responses[requestWay] || [];
					data.push({text: requestWay, children: []});
					params.forEach(function(param) {
						param.name = param.vendorExtensions['x-name']?param.vendorExtensions['x-name']:param.name;
						data[index].children.push({text: param.name})
					})
				}

			})
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
					this.setParamData(true)
				},0)
			})
		},
		rename (name,tree) {
			if(tree == "get"){
				this.treeDom = $(".getBody .jstree");
			} else {
				this.treeDom = $(".postBody .jstree");
			}
			this.treeDom.jstree(true).rename_node(this.treeDom.jstree(true).get_selected(true), name);
		},
		deleteNode () {
			this.treeDom.jstree(true).delete_node(this.treeDom.jstree(true).get_selected(true));
			this.treeDom.jstree(true).select_node('j1_1');
			this.setParamData();
		}
	},
	mounted () {
		if (this.module === 'request') {
			this.$watch('params.length', function() {
				if (this.params.length === this.$store.state.api.basicInfo.requestWay.split(',').length) {
					this.setTree();
				}
			}, {deep: true})
		} else {
			this.$watch('responses.length', function() {
				if (this.responses.length === this.$store.state.api.basicInfo.requestWay.split(',').length) {
					this.setTree();
				}
			}, {deep: true})
		}
		
	},
	watch: {
	},
}
</script>
<style>
.tree {
	height: 580px;
	overflow: auto;
}

</style>