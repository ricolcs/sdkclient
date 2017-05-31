<template>
	<div id="api" class="container">
		<div class="breadcrumb">
			<router-link to="/">项目列表</router-link>
			<router-link :to="{name: 'project', params: {id: this.$route.params.id}}">API列表</router-link>
			<a class="bc-active">创建API</a>
		</div>
		<ul class="tab-nav" v-if="route==='api'">
			<li class="basic" :class="{active: tab==='basic'}" @click="changeTab">基本信息</li>
			<!--<li class="request" :class="{active: tab==='request'}" @click="changeTab">请求信息</li>-->
			<!--<li class="response" :class="{active: tab==='response'}" @click="changeTab">响应信息</li>-->
		</ul>
		<div class="basic-info">
			<div class="param-group">
				<div class="col-md-3">
					<div class="control-label">接口ID</div>
				</div>
				<div class="col-md-6">
					<input type="text" v-model="basicInfo.operationId">
				</div>
			</div>
			<div class="param-group">
				<div class="col-md-3">
					<div class="control-label">接口名称</div>
				</div>
				<div class="col-md-6">
					<input type="text" v-model="basicInfo.apiName">
				</div>
			</div>
			<div class="param-group">
				<div class="col-md-3">
					<div class="control-label">请求URL</div>
				</div>
				<div class="col-md-6">
					<input type="text" v-model="basicInfo.url">
				</div>
			</div>
			<div class="param-group">
				<div class="col-md-3">
					<div class="control-label">请求协议</div>
				</div>
				<div class="col-md-6">
					<label><input type="checkbox" name="scheme" value="HTTP">HTTP </label>
					<label><input type="checkbox" name="scheme" value="HTTPS">HTTPS </label>
					<!--<label><input type="checkbox" name="scheme" value="WS">WS </label>-->
					<!--<label><input type="checkbox" name="scheme" value="WSS">WSS </label>-->
				</div>
			</div>
			<div class="param-group">
				<div class="col-md-3">
					<div class="control-label">请求方式</div>
				</div>
				<div class="col-md-6">
					<!--<label><input type="checkbox" name="requestWay" value="get" v-model="showGet">get </label>-->
					<!--<label><input type="checkbox" name="requestWay" value="head">head </label>-->
					<!--<label><input type="checkbox" name="requestWay" value="post" v-model="showPost">post </label>-->
					<!--<label><input type="checkbox" name="requestWay" value="put">put </label>-->
					<!--<label><input type="checkbox" name="requestWay" value="delete">delete </label>-->
					<!--<label><input type="checkbox" name="requestWay" value="options">options </label>-->
					<!--<label><input type="checkbox" name="requestWay" value="patch">patch </label>-->
					<label><input name="requestWay" type="radio" value="get" @click="showGet"/>get</label>
					<label><input name="requestWay" type="radio" value="post" @click="showPost"/>post</label>
				</div>
			</div>
			<div class="getBody" v-show="getActive">
				<div @click='add' data-http="get">+ 增加</div>

				<div class="getTree col-md-3"></div>
				<div class="getContent col-md-9">
					<div class="tree-attr" v-show="!requestHidden">
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">参数名称</div>
							</div>
							<div class="col-md-8">
								<input type="text" v-model="request.paramName">
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">请求方式</div>
							</div>
							<div class="col-md-8">
								<select v-model="request.requestWay" disabled>
									<option value="get">get</option>
									<option value="head">head</option>
									<option value="post">post</option>
									<option value="put">put</option>
									<option value="delete">delete</option>
									<option value="options">options</option>
									<option value="patch">patch</option>
								</select>
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">参数类型</div>
							</div>
							<div class="col-md-8">
								<select v-model="request.paramType">
									<option value="path">path</option>
									<option value="query">query</option>
									<option value="body">body</option>
									<option value="header">header</option>
									<option value="form">form</option>
									<option value="cookie">cookie</option>
									<option value="ref">ref</option>
								</select>
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">是否必须</div>
							</div>
							<div class="col-md-8">
								<select v-model="request.isRequired">
									<option value="true">是</option>
									<option value="false">否</option>
								</select>
							</div>
						</div>
						<div class="param-group" v-if="request.paramType==='body'||request.paramType==='ref'">
							<div class="col-md-2">
								<div class="control-label">模板名称</div>
							</div>
							<div class="col-md-8">
								<select class="defName" v-model="request.refName">
									<option v-for="ref in refs" :value="ref.defName">{{ref.defName}}</option>
								</select>
							</div>
						</div>
						<div class="param-group" v-else>
							<div class="col-md-2">
								<div class="control-label">默认值</div>
							</div>
							<div class="col-md-8">
								<input type="text" v-model="request.defaultValue">
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">描述信息</div>
							</div>
							<div class="col-md-8">
								<textarea name="" id="" cols="30" rows="10" v-model="request.description"></textarea>
							</div>
						</div>
						<div class="btn-group col-offset-2">
							<button class="btn btn-pri" data-http="get" @click="saveRequest">保存</button>
							<button class="btn btn-pri" data-http="get" @click="removeRequest">删除</button>
							<button class="btn btn-pri" @click="saveRequestInfo" v-if="route==='create'">下一步</button>
						</div>
					</div>

					<div class="tree-attr" v-show="!responseHidden">
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">响应名称</div>
							</div>
							<div class="col-md-8">
								<input type="text" v-model="response.responseName">
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">请求方式</div>
							</div>
							<div class="col-md-8">
								<select v-model="response.requestWay" disabled>
									<option value="get">get</option>
									<option value="head">head</option>
									<option value="post">post</option>
									<option value="put">put</option>
									<option value="delete">delete</option>
									<option value="options">options</option>
									<option value="patch">patch</option>
								</select>
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">引用模板名称</div>
							</div>
							<div class="col-md-8">
								<select class="defName" v-model="response.refName">
									<option v-for="ref in refs" :value="ref.defName">{{ref.defName}}</option>
								</select>
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">描述信息</div>
							</div>
							<div class="col-md-8">
								<textarea name="" id="" cols="30" rows="10" v-model="response.description"></textarea>
							</div>
						</div>
						<div class="btn-group col-offset-2">
							<!-- <button class="btn btn-pri" @click="prevTab">上一步</button> -->
							<button class="btn btn-pri" @click="saveResponse" data-http="get" v-if="route==='api'">保存
							</button>
							<button class="btn btn-pri" @click="removeResponse" data-http="get" v-if="route==='api'">
								删除
							</button>
							<!-- <button class="btn btn-pri" @click="update" v-else>保存</button> -->
						</div>
					</div>
				</div>
			</div>
			<div class="postBody" v-show="postActive">
				<div @click='add' data-http="post">+ 增加</div>

				<div class="postTree col-md-3"></div>
				<div class="postContent col-md-9">
					<div class="tree-attr" v-show="!responseHidden1">
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">响应名称</div>
							</div>
							<div class="col-md-8">
								<input type="text" v-model="response.responseName">
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">请求方式</div>
							</div>
							<div class="col-md-8">
								<select v-model="response.requestWay" disabled>
									<option value="get">get</option>
									<option value="head">head</option>
									<option value="post">post</option>
									<option value="put">put</option>
									<option value="delete">delete</option>
									<option value="options">options</option>
									<option value="patch">patch</option>
								</select>
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">引用模板名称</div>
							</div>
							<div class="col-md-8">
								<select class="defName" v-model="response.refName">
									<option v-for="ref in refs" :value="ref.defName">{{ref.defName}}</option>
								</select>
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">描述信息</div>
							</div>
							<div class="col-md-8">
								<textarea name="" id="" cols="30" rows="10" v-model="response.description"></textarea>
							</div>
						</div>
						<div class="btn-group col-offset-2">
							<!-- <button class="btn btn-pri" @click="prevTab">上一步</button> -->
							<button class="btn btn-pri" @click="saveResponse" data-http="post" v-if="route==='api'">保存
							</button>
							<button class="btn btn-pri" @click="removeResponse" data-http="post" v-if="route==='api'">
								删除
							</button>
							<!-- <button class="btn btn-pri" @click="update" v-else>保存</button> -->
						</div>
					</div>
					<div class="tree-attr" v-show="!requestHidden1">
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">参数名称</div>
							</div>
							<div class="col-md-8">
								<input type="text" v-model="request.paramName">
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">请求方式</div>
							</div>
							<div class="col-md-8">
								<select v-model="request.requestWay" disabled>
									<option value="get">get</option>
									<option value="head">head</option>
									<option value="post">post</option>
									<option value="put">put</option>
									<option value="delete">delete</option>
									<option value="options">options</option>
									<option value="patch">patch</option>
								</select>
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">参数类型</div>
							</div>
							<div class="col-md-8">
								<select v-model="request.paramType">
									<option value="path">path</option>
									<option value="query">query</option>
									<option value="body">body</option>
									<option value="header">header</option>
									<option value="form">form</option>
									<option value="cookie">cookie</option>
									<option value="ref">ref</option>
								</select>
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">是否必须</div>
							</div>
							<div class="col-md-8">
								<select v-model="request.isRequired">
									<option value="true">是</option>
									<option value="false">否</option>
								</select>
							</div>
						</div>
						<div class="param-group" v-if="request.paramType==='body'||request.paramType==='ref'">
							<div class="col-md-2">
								<div class="control-label">模板名称</div>
							</div>
							<div class="col-md-8">
								<select class="defName" v-model="request.refName">
									<option v-for="ref in refs" :value="ref.defName">{{ref.defName}}</option>
								</select>
							</div>
						</div>
						<div class="param-group" v-else>
							<div class="col-md-2">
								<div class="control-label">默认值</div>
							</div>
							<div class="col-md-8">
								<input type="text" v-model="request.defaultValue">
							</div>
						</div>
						<div class="param-group">
							<div class="col-md-2">
								<div class="control-label">描述信息</div>
							</div>
							<div class="col-md-8">
								<textarea name="" id="" cols="30" rows="10" v-model="request.description"></textarea>
							</div>
						</div>
						<div class="btn-group col-offset-2">
							<button class="btn btn-pri" @click="saveRequest" data-http="post">保存</button>
							<button class="btn btn-pri" @click="removeRequest" data-http="post">删除</button>
							<button class="btn btn-pri" @click="saveRequestInfo" v-if="route==='create'">下一步</button>
						</div>
					</div>
				</div>
			</div>
			<div class="param-group">
				<div class="col-md-3">
					<div class="control-label">标签</div>
				</div>
				<div class="col-md-6">
					<input type="text" v-model="basicInfo.tags">
				</div>
			</div>
			<div class="param-group">
				<div class="col-md-3">
					<div class="control-label">描述信息</div>
				</div>
				<div class="col-md-6">
					<textarea name="" id="" cols="30" rows="10" v-model="basicInfo.description"></textarea>
				</div>
			</div>
			<div class="btn-group col-offset-3 col-md-6">
				<button class="btn btn-pri" @click="saveBasicInfo" v-if="route==='create'">下一步</button>
				<button class="btn btn-pri" @click="updateBasicInfo" v-else>保存</button>
			</div>
		</div>
		<!--<div class="request-info" v-show="tab==='request'">-->
		<!--<div class="col-md-3 tree-area">-->
		<!--<Tree ref="requestTree"-->
		<!--@setParamData="setParamData"-->
		<!--@newParam="newParam"-->
		<!--@selectNoParam="selectNoParam"-->
		<!--:module="'request'"/>-->
		<!--</div>-->
		<!--<div class="col-md-9 tree-attr" v-show="!requestHidden">-->
		<!--<div class="param-group">-->
		<!--<div class="col-md-2">-->
		<!--<div class="control-label">参数名称</div>-->
		<!--</div>-->
		<!--<div class="col-md-8">-->
		<!--<input type="text" v-model="request.paramName">-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="param-group">-->
		<!--<div class="col-md-2">-->
		<!--<div class="control-label">请求方式</div>-->
		<!--</div>-->
		<!--<div class="col-md-8">-->
		<!--<select v-model="request.requestWay" disabled>-->
		<!--<option value="get">get</option>-->
		<!--<option value="head">head</option>-->
		<!--<option value="post">post</option>-->
		<!--<option value="put">put</option>-->
		<!--<option value="delete">delete</option>-->
		<!--<option value="options">options</option>-->
		<!--<option value="patch">patch</option>-->
		<!--</select>-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="param-group">-->
		<!--<div class="col-md-2">-->
		<!--<div class="control-label">参数类型</div>-->
		<!--</div>-->
		<!--<div class="col-md-8">-->
		<!--<select v-model="request.paramType">-->
		<!--<option value="path">path</option>-->
		<!--<option value="query">query</option>-->
		<!--<option value="body">body</option>-->
		<!--<option value="header">header</option>-->
		<!--<option value="form">form</option>-->
		<!--<option value="cookie">cookie</option>-->
		<!--<option value="ref">ref</option>-->
		<!--</select>-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="param-group">-->
		<!--<div class="col-md-2">-->
		<!--<div class="control-label">是否必须</div>-->
		<!--</div>-->
		<!--<div class="col-md-8">-->
		<!--<select v-model="request.isRequired">-->
		<!--<option value="true">是</option>-->
		<!--<option value="false">否</option>-->
		<!--</select>-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="param-group" v-if="request.paramType==='body'||request.paramType==='ref'">-->
		<!--<div class="col-md-2">-->
		<!--<div class="control-label">模板名称</div>-->
		<!--</div>-->
		<!--<div class="col-md-8">-->
		<!--<select v-model="request.refName">-->
		<!--<option v-for="ref in refs" :value="ref.defName">{{ref.defName}}</option>-->
		<!--</select>-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="param-group" v-else>-->
		<!--<div class="col-md-2">-->
		<!--<div class="control-label">默认值</div>-->
		<!--</div>-->
		<!--<div class="col-md-8">-->
		<!--<input type="text" v-model="request.defaultValue">-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="param-group">-->
		<!--<div class="col-md-2">-->
		<!--<div class="control-label">描述信息</div>-->
		<!--</div>-->
		<!--<div class="col-md-8">-->
		<!--<textarea name="" id="" cols="30" rows="10" v-model="request.description"></textarea>-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="btn-group col-offset-2">-->
		<!--<button class="btn btn-pri" @click="saveRequest">保存</button>-->
		<!--<button class="btn btn-pri" @click="removeRequest">删除</button>-->
		<!--<button class="btn btn-pri" @click="saveRequestInfo" v-if="route==='create'">下一步</button>-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="col-md-9" v-show="!operationHidden">-->
		<!--<div class="param-group">-->
		<!--<div class="col-md-3">-->
		<!--<div class="control-label">请求格式</div>-->
		<!--</div>-->
		<!--<div class="col-md-6">-->
		<!--<label><input type="checkbox" name="consumes" value="get">get </label>-->
		<!--<label><input type="checkbox" name="consumes" value="head">head </label>-->
		<!--<label><input type="checkbox" name="consumes" value="post">post </label>-->
		<!--<label><input type="checkbox" name="consumes" value="put">put </label>-->
		<!--<label><input type="checkbox" name="consumes" value="delete">delete </label>-->
		<!--<label><input type="checkbox" name="consumes" value="options">options </label>-->
		<!--<label><input type="checkbox" name="consumes" value="patch">patch </label>-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="param-group">-->
		<!--<div class="col-md-3">-->
		<!--<div class="control-label">响应格式</div>-->
		<!--</div>-->
		<!--<div class="col-md-6">-->
		<!--<label><input type="checkbox" name="produces" value="get">get </label>-->
		<!--<label><input type="checkbox" name="produces" value="head">head </label>-->
		<!--<label><input type="checkbox" name="produces" value="post">post </label>-->
		<!--<label><input type="checkbox" name="produces" value="put">put </label>-->
		<!--<label><input type="checkbox" name="produces" value="delete">delete </label>-->
		<!--<label><input type="checkbox" name="produces" value="options">options </label>-->
		<!--<label><input type="checkbox" name="produces" value="patch">patch </label>-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="btn-group col-offset-2">-->
		<!--<button class="btn btn-pri" @click="saveOperation">保存</button>-->
		<!--</div>-->
		<!--</div>-->
		<!--</div>-->

		<!--<div class="response-info" v-show="tab==='response'">-->
		<!--<div class="col-md-3 tree-area">-->
		<!--<Tree ref="responseTree"-->
		<!--@setParamData="setParamData"-->
		<!--@newResponse="newResponse"-->
		<!--@selectNoParam="selectNoParam"-->
		<!--:module="'response'"/>-->
		<!--</div>-->
		<!--<div class="col-md-9 tree-attr" v-show="!responseHidden">-->
		<!--<div class="param-group">-->
		<!--<div class="col-md-2">-->
		<!--<div class="control-label">响应名称</div>-->
		<!--</div>-->
		<!--<div class="col-md-8">-->
		<!--<input type="text" v-model="response.responseName">-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="param-group">-->
		<!--<div class="col-md-2">-->
		<!--<div class="control-label">请求方式</div>-->
		<!--</div>-->
		<!--<div class="col-md-8">-->
		<!--<select v-model="response.requestWay" disabled>-->
		<!--<option value="get">get</option>-->
		<!--<option value="head">head</option>-->
		<!--<option value="post">post</option>-->
		<!--<option value="put">put</option>-->
		<!--<option value="delete">delete</option>-->
		<!--<option value="options">options</option>-->
		<!--<option value="patch">patch</option>-->
		<!--</select>-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="param-group">-->
		<!--<div class="col-md-2">-->
		<!--<div class="control-label">引用模板名称</div>-->
		<!--</div>-->
		<!--<div class="col-md-8">-->
		<!--<select v-model="response.refName">-->
		<!--<option v-for="ref in refs" :value="ref.defName">{{ref.defName}}</option>-->
		<!--</select>-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="param-group">-->
		<!--<div class="col-md-2">-->
		<!--<div class="control-label">描述信息</div>-->
		<!--</div>-->
		<!--<div class="col-md-8">-->
		<!--<textarea name="" id="" cols="30" rows="10" v-model="response.description"></textarea>-->
		<!--</div>-->
		<!--</div>-->
		<!--<div class="btn-group col-offset-2">-->
		<!--&lt;!&ndash; <button class="btn btn-pri" @click="prevTab">上一步</button> &ndash;&gt;-->
		<!--<button class="btn btn-pri" @click="saveResponse" v-if="route==='api'">保存</button>-->
		<!--<button class="btn btn-pri" @click="removeResponse" v-if="route==='api'">删除</button>-->
		<!--&lt;!&ndash; <button class="btn btn-pri" @click="update" v-else>保存</button> &ndash;&gt;-->
		<!--</div>-->
		<!--</div>-->
		<!--</div>-->
	</div>
</template>
<script type="es6">
	import $ from 'jquery'
	import Tree from '../components/api/Tree.vue'

	export default {
		components: {Tree},
		data () {
			return {
				route: 'create',
				tab: 'basic',

				oldRequest: {
					requestWay: '',
					paramName: '',
					paramType: '',
					isRequired: true,
					description: '',
					refName: '',
					defaultValue: '',
				},

				request: {
					requestWay: 'get',
					paramName: '',
					paramType: 'path',
					isRequired: true,
					description: '',
					refName: '',
					defaultValue: '',
				},

				oldResponse: {
					requestWay: '',
					responseName: '',
					refName: '',
					description: '',
				},

				response: {
					requestWay: 'get',
					responseName: '',
					refName: '',
					description: '',
				},

				req: {},
				resp: {},

				operationType: '',

				requestHidden: true,
				responseHidden: true,
				requestHidden1: true,
				responseHidden1: true,
				operationHidden: true,

				showGet: "",
				showPost: "",
				getActive: false,
				postActive: false
			}
		},
		computed: {
			basicInfo () {

				return this.$store.state.api.basicInfo;
			},
			refs () {
				console.log(this.$store.state.api.refs);
				return this.$store.state.api.refs;
			}
		},
		methods: {
			showGet(){
				this.postActive = false;
				this.getActive = true;
			},
			showPost(){
				this.getActive = false;
				this.postActive = true;
			},
			saveBasicInfo () {
				let params = {
					operationId: this.basicInfo.operationId,
					apiName: this.basicInfo.apiName,
					url: this.basicInfo.url,
					scheme: this.getCheckbox('scheme'),
					description: this.basicInfo.description,
					requestWay: this.getCheckbox('requestWay'),
					tags: this.basicInfo.tags,
				}
				$.ajax({
					url: `${this.appConfig.apiBase}api/basicinfo/save/${this.$route.params.id}`,
					type: 'POST',
					contentType: 'application/json; charset=utf-8',
					data: JSON.stringify(params),
					cache: false,
					success: (data) => {
						if (data.code === 0) {
							this.tab = 'request';
							this.$store.dispatch('getBasicInfo', params);
							params.requestWay.split(',').forEach((m) => {
								this.$store.dispatch('getParams', {type: m, arr: []})
							})

							this.$refs.requestTree.setTree();
							this.$refs.responseTree.setTree();
						} else {
							this.appConfig.showErrorAlert(data.code);
						}
					},
					error: (data) => {
						this.appConfig.showErrorAlert('default');
					}
				})
			},
			updateBasicInfo () {
				let params = {
					operationId: this.basicInfo.operationId,
					apiName: this.basicInfo.apiName,
					url: this.basicInfo.url,
					scheme: this.getCheckbox('scheme'),
					description: this.basicInfo.description,
					requestWay: this.getCheckbox('requestWay'),
					tags: this.basicInfo.tags,
				}
				$.ajax({
					url: `${this.appConfig.apiBase}api/basicinfo/update/${this.$route.params.id}`,
					type: 'POST',
					contentType: 'application/json; charset=utf-8',
					data: JSON.stringify(params),
					cache: false,
					success: (data) => {
						if (data.code === 0) {
							this.$router.go(-1)
						} else {
							this.appConfig.showErrorAlert(data.code);
						}
					},
					error: (data) => {
						this.appConfig.showErrorAlert('default');
					}
				})
			},
			add(tree){
				var http = $(tree.target).attr("data-http");
				var ref = $("." + http + "Tree").jstree(true), sel = ref.get_selected();
				sel = ref.get_parent(sel) === '#' ? sel : ref.get_parent(sel);
				sel = ref.create_node(sel, {"type": "file"});
				ref.deselect_node(ref.get_selected());
				//ref.select_node(sel);
				$("." + http + "Tree").jstree('select_node', sel);
				if ($("." + http + "Tree").jstree("get_node", ref.get_parent(sel)).text.indexOf("请求") > -1) {
					this.newParam(http);
				} else {
					this.newResponse(http)
				}
			},
			newParam (http) {
				let param = {};
				if (http == "get") {
					this.requestHidden = false;
					this.operationHidden = true;
				} else {
					this.requestHidden = true;
					this.operationHidden = false;
				}
				var info = JSON.parse(localStorage.pageInfo);
				info[http]["request"].push(param);
				localStorage.pageInfo = JSON.stringify(info);
				//this.$store.state.api[http]["request"].push(param);
				this.req = param;
				this.request.paramName = param.name = '';
				this.request.description = param.description = '';
				this.request.paramType = param.in = 'path';
				this.request.requestWay = http;
				this.request.defaultValue = param.defaultValue = '';
				this.request.isRequired = param.required = true;

				this.oldRequest.paramName = '';
			},
			newResponse  (http) {
				if (http == "get") {
					this.requestHidden1 = false;
					this.operationHidden1 = true;
				} else {
					this.requestHidden1 = true;
					this.operationHidden1 = false;
				}
				let param = {};
				var info = JSON.parse(localStorage.pageInfo);
				info[http]["response"].push(param);
				localStorage.pageInfo = JSON.stringify(info);
				//this.$store.state.api[http]["response"].push(param);
				this.resp = param;
				param.vendorExtensions = {};
				this.response.responseName = param.vendorExtensions['x-name'] = '';
				this.response.description = param.description = '';
				this.response.refName = '';
				param.$ref = '#/definitions/';
				this.response.resquestWay = http;

				this.oldResponse.responseName = '';
			},
			setParamData(parent, id, module) {
				if (module === 'request') {
					let m = this.$store.state.api.params[parent];
					if (parent !== "response") {
						this.requestHidden = false;
						this.responseHidden = true;
						m.forEach((param) => {
							if (param.name === id) {
								this.req = param;
								this.request.paramName = this.oldRequest.paramName = param.name;
								this.request.description = this.oldRequest.description = param.description;
								this.request.paramType = this.oldRequest.paramType = param.in;
								this.request.requestWay = this.oldRequest.requestWay = "get";
								this.request.defaultValue = this.oldRequest.defaultValue = param.defaultValue;
								this.request.isRequired = this.oldRequest.isRequired = param.required;
							}
						})
					} else {
						this.responseHidden = false;
						this.requestHidden = true;
						m.forEach((param) => {
							if (param.vendorExtensions['x-name'] === id) {
								this.resp = param;
								this.response.responseName = this.oldResponse.responseName = param.vendorExtensions['x-name'];
								this.response.description = this.oldResponse.description = param.description;
								this.response.refName = this.oldResponse.ref = param.schema.$ref.lastIndexOf('/') + 1 > 0 ? param.schema.$ref.substr(param.schema.$ref.lastIndexOf('/') + 1) : '';
								this.request.requestWay = this.oldResponse.requestWay = "get";
							}
						})
					}

				} else {
					let m = this.$store.state.api.responses[parent];
					if (parent !== "response") {
						this.requestHidden1 = false;
						this.responseHidden1 = true;
						m.forEach((param) => {
							if (param.name === id) {
								this.req = param;
								this.request.paramName = this.oldRequest.paramName = param.name;
								this.request.description = this.oldRequest.description = param.description;
								this.request.paramType = this.oldRequest.paramType = param.in;
								this.request.requestWay = this.oldRequest.requestWay = "post";
								this.request.defaultValue = this.oldRequest.defaultValue = param.defaultValue;
								this.request.isRequired = this.oldRequest.isRequired = param.required;
							}
						})
					} else {
						this.responseHidden1 = false;
						this.requestHidden1 = true;
						m.forEach((param) => {
							if (param.vendorExtensions['x-name'] === id) {
								this.resp = param;
								this.response.responseName = this.oldResponse.responseName = param.vendorExtensions['x-name'];
								this.response.description = this.oldResponse.description = param.description;
								this.response.refName = this.oldResponse.ref = param.schema.$ref.lastIndexOf('/') + 1 > 0 ? param.schema.$ref.substr(param.schema.$ref.lastIndexOf('/') + 1) : '';
								this.request.requestWay = this.oldResponse.requestWay = "post";
							}
						})
					}
				}
			},
			saveRequest (tree) {
				let params = {
					url: this.$store.state.api.basicInfo.url,
					requestWay: this.request.requestWay,
					paramName: this.request.paramName,
					required: this.request.isRequired,
					description: this.request.description,
					paramType: this.request.paramType,
					defaultValue: this.request.defaultValue,
					refName: this.request.refName
				}
				if (this.oldRequest.paramName === '') {
					$.ajax({
						url: `${this.appConfig.apiBase}api/param/save/${this.$route.params.id}`,
						type: 'POST',
						contentType: 'application/json; charset=utf-8',
						data: JSON.stringify(params),
						cache: false,
						success: (data) => {
							if (data.code === 0) {
								this.$message.alert('保存成功！');
								this.req.description = params.description;
								this.req.in = params.paramType;
								this.req.defaultValue = params.defaultValue;
								this.req.required = params.required;

								if (this.req.name !== params.paramName) {
									var http = $(tree.target).attr("data-http");
									var ref = $("." + http + "Tree").jstree(true), sel = ref.get_selected();
									$("." + http + "Tree").jstree("rename_node", sel[0], params.paramName);
									//this.$refs.requestTree.rename(params.paramName, tree);
									this.req.name = params.paramName;
								}
								//console.log(this.$refs.responseTree);
								// this.$refs.responseTree.setTree();
							} else {
								this.appConfig.showErrorAlert(data.code);
							}
						},
						error: (data) => {
							this.appConfig.showErrorAlert('default');
						}
					})
					return;
				}
				$.ajax({
					url: `${this.appConfig.apiBase}api/param/update/${this.$route.params.id}/${this.oldRequest.paramName}`,
					type: 'POST',
					contentType: 'application/json; charset=utf-8',
					data: JSON.stringify(params),
					cache: false,
					success: (data) => {
						if (data.code === 0) {
							this.$message.alert('保存成功！');
							this.req.description = params.description;
							this.req.in = params.paramType;
							this.req.defaultValue = params.defaultValue;
							this.req.required = params.required;
							if (this.req.name !== params.paramName) {
								//this.$refs.requestTree.rename(params.paramName);
								var http = $(tree.target).attr("data-http");
								var ref = $("." + http + "Tree").jstree(true), sel = ref.get_selected();
								$("." + http + "Tree").jstree("rename_node", sel[0], params.paramName);
								this.req.name = params.paramName;
							}
							console.log(this.$refs.responseTree, tree);
							// this.$refs.responseTree.setTree();
						} else {
							this.appConfig.showErrorAlert(data.code);
						}
					},
					error: (data) => {
						this.appConfig.showErrorAlert('default');
					}
				})
			},
			saveResponse (tree) {
				let _this = this;
				let params = {
					url: this.$store.state.api.basicInfo.url,
					requestWay: this.response.requestWay,
					responseName: this.response.responseName,
					description: this.response.description,
					refName: this.response.refName,
				}
				if (this.oldResponse.responseName === '') {
					$.ajax({
						url: `${this.appConfig.apiBase}api/response/save/${this.$route.params.id}`,
						type: 'POST',
						contentType: 'application/json; charset=utf-8',
						data: JSON.stringify(params),
						cache: false,
						success: (data) => {
							if (data.code === 0) {
								this.$message.alert('保存成功！');
								_this.resp.description = params.description;
								_this.resp.refName = params.refName;
								_this.resp.vendorExtensions = {};

								if (_this.resp.vendorExtensions['x-name'] !== params.responseName) {
									//_this.$refs.responseTree.rename(params.responseName);
									var http = $(tree.target).attr("data-http");
									var ref = $("." + http + "Tree").jstree(true), sel = ref.get_selected();
									$("." + http + "Tree").jstree("rename_node", sel[0], params.responseName);
									_this.resp.vendorExtensions['x-name'] = params.responseName;
								}
								_this.oldResponse.responseName = params.responseName;

							} else {
								_this.appConfig.showErrorAlert(data.code);
							}
						},
						error: (data) => {
							this.appConfig.showErrorAlert('default');
						}
					})
					return;
				}
				$.ajax({
					url: `${this.appConfig.apiBase}api/response/update/${this.$route.params.id}/${this.oldResponse.responseName}`,
					type: 'POST',
					contentType: 'application/json; charset=utf-8',
					data: JSON.stringify(params),
					cache: false,
					success: (data) => {
						if (data.code === 0) {
							this.$message.alert('保存成功！');
							_this.resp.description = params.description;
							_this.resp.refName = params.refName;
							_this.resp.vendorExtensions = {};

							if (_this.resp.vendorExtensions['x-name'] !== params.responseName) {
								//_this.$refs.responseTree.rename(params.responseName);
								var http = $(tree.target).attr("data-http");
								var ref = $("." + http + "Tree").jstree(true), sel = ref.get_selected();
								$("." + http + "Tree").jstree("rename_node", sel[0], params.responseName);
								_this.resp.name = params.responseName;

							}
							// this.$refs.responseTree.setTree();
						} else {
							_this.appConfig.showErrorAlert(data.code);
						}
					},
					error: (data) => {
						this.appConfig.showErrorAlert('default');
					}
				})
			},
			removeRequest (tree) {
				var http = $(tree.target).attr("data-http");
				var ref = $("." + http + "Tree").jstree(true), sel = ref.get_selected();
				var selected = $("." + http + "Tree").jstree("get_node", sel[0]);
				let params = {
					url: this.$store.state.api.basicInfo.url,
					type: this.request.requestWay,
					paramName: selected.text
				}
				$.ajax({
					url: `${this.appConfig.apiBase}api/param/remove/${this.$route.params.id}`,
					type: 'POST',
					contentType: 'application/json; charset=utf-8',
					data: JSON.stringify(params),
					cache: false,
					success: (data) => {
						if (data.code === 0) {
							//this.$refs.requestTree.deleteNode();

							$("." + http + "Tree").jstree("delete_node", sel[0], params.paramName);
						} else {
							this.appConfig.showErrorAlert(data.code);
						}
					},
					error: (data) => {
						this.appConfig.showErrorAlert('default');
					}
				})
			},
			removeResponse (tree) {
				var http = $(tree.target).attr("data-http");
				var ref = $("." + http + "Tree").jstree(true), sel = ref.get_selected();
				var selected = $("." + http + "Tree").jstree("get_node", sel[0]);
				let params = {
					url: this.$store.state.api.basicInfo.url,
					type: this.response.requestWay,
					name: selected.text
				}
				$.ajax({
					url: `${this.appConfig.apiBase}api/response/remove/${this.$route.params.id}`,
					type: 'POST',
					contentType: 'application/json; charset=utf-8',
					data: JSON.stringify(params),
					cache: false,
					success: (data) => {
						if (data.code === 0) {
							//this.$refs.responseTree.deleteNode();

							$("." + http + "Tree").jstree("delete_node", sel[0], params.paramName);
						} else {
							this.appConfig.showErrorAlert(data.code);
						}
					},
					error: (data) => {
						this.appConfig.showErrorAlert('default');
					}
				})
			},
			selectNoParam (module, operationType) {
				if (module === 'request') {
					// this.request = {};
					// this.req = {};
					// this.oldRequest = {};
					this.requestHidden = true;
					$('input[name=consumes]:checked').attr('checked', false);
					$('input[name=produces]:checked').attr('checked', false);
					let params = {
						url: this.$store.state.api.basicInfo.url,
						type: operationType
					}
					$.ajax({
						url: `${this.appConfig.apiBase}api/operation/get/${this.$route.params.id}`,
						type: 'POST',
						contentType: 'application/json; charset=utf-8',
						data: JSON.stringify(params),
						cache: false,
						success: (data) => {
							if (data.code === 0) {
								data.data.consumes.forEach(function (item) {
									$('input[name=consumes][value=' + item + ']').attr('checked', 'checked');
								})
								data.data.produces.forEach(function (item) {
									$('input[name=produces][value=' + item + ']').attr('checked', 'checked');
								})
							} else if (data.code === 5003) {
								return;
							} else {
								this.appConfig.showErrorAlert(data.code);
							}
						},
						error: (data) => {
							this.appConfig.showErrorAlert('default');
						}
					})
				} else {
					// this.response = {};
					// this.resp = {};
					// this.oldResponse = {};
					this.responseHidden = true;
				}
				this.operationHidden = true;
				this.operationType = operationType;
			},
			saveOperation () {
				let params = {
					url: this.$store.state.api.basicInfo.url,
					type: this.operationType,
					consumes: this.getCheckbox('consumes'),
					produces: this.getCheckbox('produces')
				}
				$.ajax({
					url: `${this.appConfig.apiBase}api/operation/save/${this.$route.params.id}`,
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
						this.appConfig.showErrorAlert('default');
					}
				})
			},
			saveRequestInfo () {
				this.tab = 'response'
			},
			saveResponseInfo () {

			},
			getCheckbox (name) {
				let checkboxArr = [].slice.call($('input[name=' + name + ']:checked'), 0),
						arr = [];
				checkboxArr.forEach(function (item) {
					arr.push(item.value)
				})
				return arr.join(',')
			},
			setCheckbox (param) {
				this.basicInfo[param].split(',').forEach(function (item) {
					$('input[name=' + param + '][value=' + item + ']').attr('checked', 'checked');
				})
			},
			changeTab (e) {
				//if (e.target.className.indexOf(this.tab) !== -1) return;
				this.tab = e.target.className;
			},
			nextTab () {
				if (this.tab === 'basic') {


					this.tab = 'request'
				} else {
					this.tab = 'response'
				}
			},
			prevTab () {
				if (this.tab === 'response') {
					this.tab = 'request'
				} else {
					this.tab = 'basic'
				}
			}
		},
		created () {
			if (this.$route.name === 'api') {
				this.route = 'api';
				this.$store.dispatch('getApi', {id: this.$route.params.id, apiName: this.$route.params.apiName});
			}
		},
		beforeDestroy () {
			this.$store.dispatch('clearApiInfo')
		},
		mounted(){
			let that = this;
			setTimeout(function () {
				var params = JSON.parse(localStorage.pageInfo);
				var getData = [], postData = [];
				getData.push({
					text: "get请求",
					children: [{text: "query", children: []}, {text: "body", children: []}]
				});
				getData.push({
					text: "get响应",
					children: [{text: "query", children: []}, {text: "body", children: []}]
				});
				postData.push({
					text: "post请求",
					children: [{text: "query", children: []}, {text: "body", children: []}]
				});
				postData.push({
					text: "post响应",
					children: [{text: "query", children: []}, {text: "body", children: []}]
				});
				if (typeof params["get"]["request"] == "object") {
					params["get"]["request"].forEach(function (param) {
						param.name = param.vendorExtensions['x-name'] ? param.vendorExtensions['x-name'] : param.name;
						getData[0].children.push({text: param.name})
					});
				}
				if (typeof params["get"]["response"] == "object") {
					params["get"]["response"].forEach(function (param) {
						param.name = param.vendorExtensions['x-name'] ? param.vendorExtensions['x-name'] : param.name;
						getData[1].children.push({text: param.name})
					})
				}

				if (typeof params["post"]["request"] == "object") {
					params["post"]["request"].forEach(function (param) {
						param.name = param.vendorExtensions['x-name'] ? param.vendorExtensions['x-name'] : param.name;
						postData[0].children.push({text: param.name})
					});
				}

				if (typeof params["post"]["response"] == "object") {
					params["post"]["response"].forEach(function (param) {
						param.name = param.vendorExtensions['x-name'] ? param.vendorExtensions['x-name'] : param.name;
						postData[1].children.push({text: param.name})
					})
				}
				$(".getTree").jstree({
					"core": {
						"animation": 0.5,
						"check_callback": true,
						"themes": {
							"responsive": false
						},
						"data": getData
					},
					"types": {
						"default": {"icon": "fa fa-link icon-state-success"}
					},
					"state": {
						"key": "demo2"
					},
					"plugins": ["unique", "state", "types", "wholerow", "changed"]
				});
				$(".postTree").jstree({
					"core": {
						"animation": 0.5,
						"check_callback": true,
						"themes": {
							"responsive": false
						},
						"data": postData
					},
					"types": {
						"default": {"icon": "fa fa-link icon-state-success"},
						"file": {"icon": "fa fa-link icon-state-success"}
					},
					"state": {
						"key": "demo2"
					},
					"plugins": ["unique", "state", "types", "wholerow", "changed"]
				});
				$(".getTree").on('loaded.jstree', () => {
					$(".getTree").jstree(true).open_all()
				})
				$(".postTree").on('loaded.jstree', () => {
					$(".postTree").jstree(true).open_all()
				})
				$(".getTree").on('changed.jstree', function (e, data) {
					var parent = "request";
					if (data.node) {
						if ($(".getTree").jstree("get_node", data.node.parent).text == "get响应") {
							that.responseHidden = false;
							that.requestHidden = true;
							parent = "response";
						} else {
							that.responseHidden = true;
							that.requestHidden = false;
						}
						getAllInfo("get", parent, data.node.text);
					}
				});
				$(".postTree").on('changed.jstree', function (e, data) {
					var parent = "request";
					if (data.node) {
						if ($(".postTree").jstree("get_node", data.node.parent).text == "post响应") {
							that.responseHidden1 = false;
							that.requestHidden1 = true;
							parent = "response";
						} else {
							that.responseHidden1 = true;
							that.requestHidden1 = false;
						}
						getAllInfo("post", parent, data.node.text)
					}
				});
				var getAllInfo = function (http, way, id) {
					var info = JSON.parse(localStorage.pageInfo)[http][way];
					if (way == "request") {
						if (typeof info != "object") {
							that.request.paramName = that.oldRequest.paramName = "";
							that.request.description = that.oldRequest.description = "";
							that.request.paramType = that.oldRequest.paramType = "";
							that.request.requestWay = that.oldRequest.requestWay = http;
							that.request.defaultValue = that.oldRequest.defaultValue = "";
							that.request.isRequired = that.oldRequest.isRequired = "";
						} else {
							info.forEach((param) => {
								if (param.name === id) {
									console.log(param);
									that.req = param;
									that.request.paramName = that.oldRequest.paramName = param.name;
									that.request.description = that.oldRequest.description = param.description;
									that.request.paramType = that.oldRequest.paramType = param.in;
									that.request.requestWay = that.oldRequest.requestWay = http;
									that.request.defaultValue = that.oldRequest.defaultValue = param.defaultValue;
									that.request.isRequired = that.oldRequest.isRequired = param.required;
								}
							})
						}
					} else {
						if (typeof info != "object") {
							that.response.responseName = that.oldResponse.responseName = "";
							that.response.description = that.oldResponse.description = "";
							that.response.refName = that.oldResponse.ref = "";
							that.response.requestWay = that.oldResponse.requestWay = http;
						} else {
							info.forEach((param) => {
								if (param.vendorExtensions['x-name'] === id) {
									console.log(param);
									that.resp = param;
									that.response.responseName = that.oldResponse.responseName = param.vendorExtensions['x-name'];
									that.response.description = that.oldResponse.description = param.description;
									that.response.refName = that.oldResponse.ref = param.schema.$ref.lastIndexOf('/') + 1 > 0 ? param.schema.$ref.substr(param.schema.$ref.lastIndexOf('/') + 1) : '';
									that.response.requestWay = that.oldResponse.requestWay = http;
								}
							})
						}
					}
				}
			}, 0)
		},
		watch: {
			"basicInfo.scheme" () {
				this.setCheckbox("scheme")
			},
			"basicInfo.requestWay" () {
				this.setCheckbox("requestWay")
			},

		}


	}
</script>
<style>
.tab-nav li {
	display: inline-block;
	padding: 0 20px;
	height: 32px;
	line-height: 32px;
	border-radius: 16px;
	margin-right: 8px;
	cursor: pointer;
}
.tab-nav li:hover {
	background-color: #62aee2;
}
.tab-nav li.active {
	cursor: default;
	background-color: #62aee2;
}
.basic-info, .request-info, .response-info {
	margin-top: 20px;
	border: 1px solid #ddd;
	/*height: 600px;*/
}
.tree-area {
	border-right: 1px solid #ddd;
}
.basic-info label {
	margin-right: 10px;
}

.icon-state-success {
	color: #26A69A;
}

.getBody, .postBody {
	border: solid #ddd 1px;
	padding: 10px 45px;
	margin: 10px;
}
</style>