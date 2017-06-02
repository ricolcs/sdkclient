import Vue from 'vue'
import router from '../router'
import message from '../components/Message.js'
import store from '../store'

export default {
	domain: '',
	apiBase: '',
	codeMessage: {
		1: '服务器错误！',
		2: '登录超时！',
		3: '登录超时！',
		4: '登录超时！',
		8: '拒绝访问！',
		9: '没有权限！',
		11: '没有权限！',
		12: '没有通过审核，请联系超级管理员！',
		13: '用户被冻结，请联系超级管理员！',
		90: 'IO错误',
		99: '未知错误！',
		600: '参数错误！',
		605: '参数错误！',
		1000: '密码错误！',
		1001: '用户名已存在！',
		1002: '用户名不存在！',
		1003: 'email已存在！',
		1004: 'email不存在！',
		1005: '用户未审核通过！',
		1006: '帐户被禁用！',
		1007: '用户未审核通过或被锁定！',
		1008: '用户已登录！',
		1009: '密码错误！',
		1100: '不允许重设密码！',
		1101: '发送邮件错误！',
		1102: '被禁用！',
		1500: '数据库错误！',
		5001: '未发现license文件！',
		5002: 'license文件不存在！',
		5003: '用户签名失败！',
		5004: 'pubkey不存在！',
		5005: 'license信息不存在！',
		5006: 'XML签名失败！',
		5007: '解析XML失败！',
		5008: 'IO异常！',
		5009: '加密失败！',
		5010: '对象为空！',
		5011: '没有该算法！',
		5012: '编码错误！',
		5013: '还未上传license！',
		51101: '项目已存在！',
		51003: '项目不存在！',
		51105: '项目已存在！',
		6201: '创建iwa对比文件失败！',
		6202: '创建iwa包失败！',
		6203: '获取服务失败！',
		6204: '下载iwa文件失败！',
		6205: 'iwa文件不存在！',
		6206: '6206',
		6207: '测试iwa包失败！',
		6210: '删除iwa包失败！',
		6211: '压缩iw包失败！',
		6212: '初始化日志文件失败！',
		6213: '获取pubkeu失败！',
		6214: '项目被锁定！',
		6215: 'projectID用尽！',
		default: '请求失败，请重试！'
	},
	showErrorAlert (code) {
		if (!code) {
			Vue.prototype.$message.alert(this.codeMessage.default);
		} else if (code === 3 || code === 4) {
			router.push('/')
			Vue.prototype.$message.alert('登录超时！');
			store.dispatch('clearToken')
			store.dispatch('clearApplications')
		} else {
			if (this.codeMessage[code]) {
				Vue.prototype.$message.alert(this.codeMessage[code]);
			} else {
				Vue.prototype.$message.alert(this.codeMessage.default);
			}
			
		}
	},
	parseDateTime (timestamp) {
		let d = new Date(timestamp);
		return d.getFullYear() + '-' + this.serializeNum(d.getMonth() + 1) + '-' + this.serializeNum(d.getDate())
					+ ' ' + this.serializeNum(d.getHours()) + ':' + this.serializeNum(d.getMinutes()) + ':' + this.serializeNum(d.getSeconds());
	},
	parseDateHour (timestamp) {
		let d = new Date(timestamp);
		return d.getFullYear() + '-' + this.serializeNum(d.getMonth() + 1) + '-' + this.serializeNum(d.getDate())
			+ ' ' + this.serializeNum(d.getHours()) + '时';
	},
	parseDate (timestamp) {
		let d = new Date(timestamp);
		return d.getFullYear() + '-' + this.serializeNum(d.getMonth() + 1) + '-' + this.serializeNum(d.getDate());
	
	},
	serializeNum (num) {
		return num > 9 ? num : '0' + num;
	},
	dataTableChinese (name) {
	    return {
	        bLengthChange: false,
	        bFilter: false,
	        bInfo: false,
	        bSort: false,
	        sPaginationType: "full_numbers",
	        bRetrieve: true,
	        iDisplayLength: 5,
	        oLanguage: {
	            sLengthMenu: "每页显示 _MENU_ 条数据",
	            sInfo: "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
	            sInfoEmpty: "没有数据",
	            sInfoFiltered: "(从 _MAX_ 条数据中检索)",
	            sZeroRecords: "没有检索到数据",
	            sSearch: name,
	            oPaginate: {
	                sFirst: "首页",
	                sPrevious: '◀',
	                sNext: "▶",
	                sLast: "尾页"
	            }
	        }
	    }
	}
}