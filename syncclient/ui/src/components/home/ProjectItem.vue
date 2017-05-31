<template>
	<tr id='projectItem'>
		<td >{{id}}</td>
		<td class='id'><router-link :to="{name: 'project', params: { id: project.projectID ,name:project.projectName}}">{{project.projectName}}</router-link></td>
		<td id='description'>{{project.description}}</td>
		<td id='buildTime'>{{date}}</td>
		<td class='icon'>
			<div class="btn  build" @click="copyProject" :disabled="!$store.state.user.token">
                <span></span>
            </div>
			<div class="btn  edit" @click="jumpToEdit" :disabled="!$store.state.user.token">
                    <span></span>
            </div>
			<div class="btn  del" @click="deleteProject" :disabled="!$store.state.user.token">
                    <span></span>
            </div>
		</td>
	</tr>
</template>
<script>
export default {
	props: ['project'],
  
	computed:　{
		id () {
			return this.project.projectID < 1000 ? ('0000' + this.project.projectID).slice(-4) : this.project.projectID;
		},
		date () {
			return this.appConfig.parseDateTime(this.project.createTime);
		}
	},
	methods: {
		deleteProject () {
            this.$message.delModal({
                header: '请确认是否要删除该项目',
                body: "",
                primaryClick: () => {
                        $.ajax({
                            url: `${this.appConfig.apiBase}project/deleteProject/${this.project.projectID}`,
                            type: 'GET',
                            contentType: 'application/json; charset=utf-8',
                            cache: false,
                            success: (data) => {
                                if (data.code === 0) {
                                    this.$message.hide();
                                    this.$message.alert('删除成功！');
                                    this.$store.dispatch('getProjects');
                                } else {
                                    this.appConfig.showErrorAlert(data.code);
                                }
                            },
                            error: (data) => {
                                this.appConfig.showErrorAlert(data.code);
                            }
                        })
                    }
            })

		},
		copyProject () {
			this.$message.modal({
				header: '复制项目',
				body: `<label><span id='newProjectName'>新项目名称</span><input type="text" class="copyProjectName" placeholder='请输入新项目名称' id='copyProjectName'/></label>`,
				primaryClick: () => {
					let newName = $('.copyProjectName').val()
					if (!newName) {
						this.$message.alert('请输入新项目名');
					} else {
						$.ajax({
							url: `${this.appConfig.apiBase}project/copyProject/${this.project.projectID}/${newName}`,
							type: 'GET',
							contentType: 'application/json; charset=utf-8',
							cache: false,
							success: (data) => {
								if (data.code === 0) {
									this.$message.hide();
									this.$message.alert('复制成功！');
									this.$store.dispatch('getProjects');
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
		jumpToEdit () {
			this.$router.push({name: "changeProject", params: {id: this.project.projectID, projectName: this.project.projectName, description: this.project.description,host: this.project.host,basePath: this.project.basePath}})
		}
	},
	mounted () {
		if (this.project.isLast) {
			let tableData = this.appConfig.dataTableChinese('用户名');
			tableData.searching = true;
			let table = $('.project-table').DataTable(tableData);
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

	}
}

</script>
<style>

    #copyProjectName{
        width: 65%;
        margin-left: 10px;
        padding-left: 20px;
        background: #f3f7fa;
        border-radius: 21px;
        outline: none;
        border: none;
        margin-bottom: 10px;
        color: #b9cedb;
        font-size: 12px;
    }
    #newProjectName{
        font-size: 12px;
        color: #91b0c4;
    }
table.sorting-table {cursor: move;}
table tr.sorting-row td {background-color: #eee;}
#projectItem>td{
    font-size: 12px;
    color: rgb(102,102,102);
    text-align: left;
    padding-left: 20px;
}
#projectItem .id a{
    font-size: 12px;
    color: #01A2F5;
}
    #projectItem .id a:hover{
        text-decoration: none;
    }
    #buildTime{
        width: 260px;
        font-size: 12px;
        color: rgb(255,166,0) !important;
    }
    .icon div{
        width: 17px;
        height: 17px;
        margin-right: 21px;
        background-color: #fff;
        border-radius: 11px;
        box-shadow: none;
    }
    .build span{
        width: 17px;
        height: 17px;
        display: inline-block;
        background: url(../../maimg/build.png) no-repeat;
    }
    .build:hover{
        background-color: #e3f2fa;
    }
    .build:hover span{
        background: url(../../maimg/Cbuild.png) no-repeat;
    }
    .edit span{
        width: 17px;
        height: 17px;
        display: inline-block;
        background: url(../../maimg/edit.png) no-repeat;
    }
    .edit:hover{
        box-shadow: none;
        background-color:  #e3f2fa;
    }
    .edit:hover span{
        background: url(../../maimg/Cedit.png) no-repeat;
    }
    .del span{
        width: 17px;
        height: 17px;
        display: inline-block;
        background: url(../../maimg/del.png)no-repeat;
    }
    .del:hover{
        box-shadow: none;
        background-color: #e3f2fa;
    }
    .del:hover span{
        background: url(../../maimg/Cdel.png) no-repeat;
    }
</style>
