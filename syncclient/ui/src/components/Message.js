import Vue from 'vue'

let alertVm, modalVm,delModalVm;
export default {
    alert: (message, time) => {
        if (!alertVm) {
            let container = document.createElement("div");
            container.setAttribute("class", "alert-component");
            document.body.appendChild(container)
            alertVm = new Vue({
                // el: document.querySelector('.alert-component'),
                el: container,
                template: `<div class="alert" v-if="!hidden">
								<div class="alert-dialog">
									<span class="alert-close" @click="hide">×</span>
							    	<div class="alert-message" style="word-break: break-word;max-height: 300px;overflow: auto;">{{message}}</div>
								</div>
							</div>`,
                data () {
                    return {
                        message: '',
                        timer: '',
                        hidden: true
                    }
                },
                methods: {
                    show (message, time = 1000) {
                        setTimeout(() => {
                            clearTimeout(this.timer);
                            this.message = message;
                            this.hidden = false;
                            this.timer = setTimeout(() => {
                                this.hide()
                            }, time)
                        }, 0)

                    },
                    hide () {
                        this.hidden = true;
                        this.message = '';
                    }
                }
            })
            $(document).on('click', function (e) {
                if (alertVm.hidden === false) {
                    if ($.contains(alertVm.$el, e.target) || alertVm.$el === e.target) {
                        return false;
                    }
                    alertVm.hide();
                }
            })
        }

        alertVm.show(message, time)
    },
    delModal:options=>{
        if (!delModalVm) {
            let container = document.createElement("div");
            container.setAttribute("class", "delModal-component");
            document.body.appendChild(container);
            delModalVm = new Vue({
                el: container,
                template: `<div class="modal" v-if="!hidden">
								<div class="modal-dialog" id='modalDialog'>
									<div class="modal-content clearfix" >
										<div class="modal-header" v-html="header" id='modal-header'></div>
										<div class="modal-body clearfix" v-html="body"></div>
										<div class="delModalFooter clearfix" >
											<div  @click="primaryClick" id='delPrimary'>{{primaryBtn}}</div>
											<div  @click="defaultClick" id='delDefault'>{{defaultBtn}}</div>
										</div>
									</div>
									<span class="modal-close" @click="hide"></span>
								</div>
							</div>`,
                data () {
                    return {
                        header: '提示',
                        body: '',
                        footer: '',
                        primaryBtn: '删除',
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
                        if (!options) options = {};
                        if (typeof options === 'string') {
                            this.body = options
                        } else {
                            this.hidden = false;
                            this.header = options.header ? options.header : '提示';
                            this.body = options.body ? options.body : '';
                            this.footer = options.footer ? options.footer : '';
                            this.primaryBtn = options.primaryBtn ? options.primaryBtn : '删除';
                            this.defaultBtn = options.defaultBtn ? options.defaultBtn : '取消';
                            options.primaryClick ? this.primaryClick = options.primaryClick : false;
                            options.defaultClick ? this.defaultClick = options.defaultClick : false;
                            options.change ? this.change = options.change : false;
                        }
                    },
                    hide () {
                        this.hidden = true;
                    }
                }
            })
        }

        delModalVm.show(options)
        Vue.nextTick(function () {

            if (options.callback) {
                options.callback();
            }

        })
    },
    modal: options => {
        if (!modalVm) {
            let container = document.createElement("div");
            container.setAttribute("class", "modal-component");
            document.body.appendChild(container)
            modalVm = new Vue({
                el: container,
                template: `<div class="modal" v-if="!hidden">
								<div class="modal-dialog" id='modal-dialog'>
									<div class="modal-content clearfix" id='modal-content'>
										<div class="modal-header" v-html="header"></div>
										<div class="modal-body clearfix" v-html="body"></div>
										<div class="modal-footer clearfix" id='modal-footer'>
											<div class="btn btn-pri" @click="primaryClick">{{primaryBtn}}</div>
											<div class="btn btn-pri" @click="defaultClick">{{defaultBtn}}</div>
										</div>
									</div>
									<span class="modal-close" @click="hide"></span>
								</div>
							</div>`,
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
                        change: () => {
                            console.log('liuyifei');
                        },
                        hidden: true
                    }
                },
                methods: {
                    show (options) {
                        if (!options) options = {};
                        if (typeof options === 'string') {
                            this.body = options
                        } else {
                            this.hidden = false;
                            this.header = options.header ? options.header : '提示';
                            this.body = options.body ? options.body : '';
                            this.footer = options.footer ? options.footer : '';
                            this.primaryBtn = options.primaryBtn ? options.primaryBtn : '确定';
                            this.defaultBtn = options.defaultBtn ? options.defaultBtn : '取消';
                            options.primaryClick ? this.primaryClick = options.primaryClick : false;
                            options.defaultClick ? this.defaultClick = options.defaultClick : false;
                            options.change ? this.change = options.change : false;
                        }
                    },
                    hide () {
                        this.hidden = true;
                    }
                }
            })
        }

        modalVm.show(options)
        Vue.nextTick(function () {
            var importFile = document.getElementById('import-file');
            var importInput = document.getElementById('importInput');
            importFile.onchange = function () {
                var arr = importFile.value.split("\\");
                importInput.value = arr[arr.length - 1];
                console.log(importFile.value);
                console.log(importFile.innerHTML);
            }
            if (options.callback) {
                options.callback();
            }

        })
    },
    hide: (e) => {
        if (!e || e === 'modal'||e==='delModal') {
            modalVm.hide();
        } else if (e === 'alert') {
            alertVm.hide();
        }
    }
}
