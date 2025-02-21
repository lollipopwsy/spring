<template>
    <!-- <div class="container content-field">
        <div class="card">
            <div class="card-body">
                我的Bot
            </div>
        </div>
    </div>
    <div>对战</div> -->
    <!-- 这样写每个页面都要实现一遍，所以把他们重新做一个组件contentfield共同使用 -->
    <!-- <ContentField> -->
        <!-- 我的Bot -->
    <!-- </ContentField> -->
    <!-- ContentField里面的内容会被渲染到ContentField.vue里的slot里 -->

    <div class="container">
    <!-- 放到container里面可以动态调整 -->
    <!-- 布局借助bootstrap的girds，将页面分为十二格，我们可以设置头像占左边三格，增删改bot占九格 -->
        <div class="row">
            <div class="col-3">
                <!-- 头像下面是白色区域，用cards -->
                <div class="card" style="margin-top:20px">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="" style="width:100%">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top:20px">
                    <div class="card-header">
                        <span style="font-size: 120%">我的Bot</span>
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">
                            创建Bot
                        </button>
                        <!-- 弹出一个创建浮窗来写新的bot的内容，用modal -->
                        <div class="modal fade" id="add-bot-btn" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                <div class="modal-header">
                                    <h1 class="modal-title">创建Bot</h1>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <!-- 里面的内容用表单forms -->
                                    <div class="mb-3">
                                        <label for="add-bot-title" class="form-label">名称</label>
                                        <input v-model="botadd.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-description" class="form-label">简介</label>
                                        <textarea v-model="botadd.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-content" class="form-label">代码</label>
                                        <VAceEditor
                                            v-model:value="botadd.content"
                                            @init="editorInit"
                                            lang="c_cpp"
                                            theme="textmate"
                                            style="height: 300px" 
                                            :options="{
                                                    enableBasicAutocompletion: true, //启用基本自动完成
                                                    enableSnippets: true, // 启用代码段
                                                    enableLiveAutocompletion: true, // 启用实时自动完成
                                                    fontSize: 18, //设置字号
                                                    tabSize: 4, // 标签大小
                                                    showPrintMargin: false, //去除编辑器里的竖线
                                                    highlightActiveLine: true,
                                            }"/>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <!-- 按钮左边写报错信息 -->
                                    <div class="error_message">{{ botadd.error_message }}</div>
                                    <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <!-- 这里展示所有bot，需要先定义一个变量把他们存下来，获取bot列表需要调用api -->
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                <!-- 循环遍历，每个bot占一行。要给每个bot绑定一个key -->
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createtime }}</td>
                                    <!-- 修改，删除操作按钮 -->
                                    <td>
                                        <button type="button" class="btn btn-secondary" style="margin-right:10px" data-bs-toggle="modal" :data-bs-target="'#update-bot-modal-'+bot.id">
                                            修改
                                        </button>
                                        <button type="button" class="btn btn-danger" @click="remove_bot(bot)">
                                            删除
                                        </button>
                                        <!-- 弹出一个创建浮窗来修改bot的内容，用modal -->
                                        <div class="modal fade" :id="'update-bot-modal-'+bot.id" tabindex="-1">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                <div class="modal-header">
                                                    <h1 class="modal-title">修改Bot</h1>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <!-- 里面的内容用表单forms -->
                                                    <div class="mb-3">
                                                        <label for="add-bot-title" class="form-label">名称</label>
                                                        <input v-model="bot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-description" class="form-label">简介</label>
                                                        <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-content" class="form-label">代码</label>
                                                        <VAceEditor
                                                            v-model:value="bot.content"
                                                            @init="editorInit"
                                                            lang="c_cpp"
                                                            theme="textmate"
                                                            style="height: 300px"
                                                            :options="{
                                                                    enableBasicAutocompletion: true, //启用基本自动完成
                                                                    enableSnippets: true, // 启用代码段
                                                                    enableLiveAutocompletion: true, // 启用实时自动完成
                                                                    fontSize: 18, //设置字号
                                                                    tabSize: 4, // 标签大小
                                                                    showPrintMargin: false, //去除编辑器里的竖线
                                                                    highlightActiveLine: true,
                                                            }" />
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <!-- 按钮左边写报错信息 -->
                                                    <div class="error_message">{{ bot.error_message }}</div>
                                                    <button type="button" class="btn btn-primary" @click="update_bot(bot)">保存修改</button>
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                                </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {ref,reactive} from 'vue'//绑定变量和对象
    import $ from 'jquery'
    import {useStore} from 'vuex'
    import { Modal } from 'bootstrap/dist/js/bootstrap';
    // 安装好vue3-ace-editor后，引入
    import { VAceEditor } from 'vue3-ace-editor';//vue里面也要下载依赖
    import ace from 'ace-builds';//vue里面也要下载依赖

    // editor不高亮
    import 'ace-builds/src-noconflict/mode-c_cpp';
    import 'ace-builds/src-noconflict/mode-json';
    import 'ace-builds/src-noconflict/theme-chrome';
    import 'ace-builds/src-noconflict/ext-language_tools';

    // import ContentField from '../../../components/ContentField.vue'

    // // 调试
    // import $ from 'jquery'//引入ajax
    // import {useStore} from'vuex'//引入全局变量

    export default {
        // components: {
        //     ContentField
        // },
        
        // 是组件
        components: {
            VAceEditor
        },
        setup(){
            ace.config.set(
                "basePath", 
                "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
            const store=useStore();
            // 定义一个bot
            let bots=ref([]);

            // 定义对象用于将表格里的数据和对象绑定起来
            const botadd= reactive({
                title:"",
                description:"",
                content:"",
                error_message:"",
            });

            // 写一个函数叫刷新列表
            const refresh_bots=()=>{
                $.ajax({
                    url:"http://localhost:3000/user/bot/getlist/",
                    type:"get",
                    headers:{
                        Authorization:"Bearer " + store.state.user.token,
                    },
                    success(resp){
                        bots.value=resp;
                    }
                })
            }
            refresh_bots();//调用一下
            
            // 后端add
            const add_bot=()=>{
                botadd.error_message="";
                $.ajax({
                    url:"http://localhost:3000/user/bot/add/",
                    type:"post",
                    data:{
                        title:botadd.title,
                        description:botadd.description,
                        content:botadd.content,
                    },
                    headers:{
                        Authorization:"Bearer " + store.state.user.token,
                    },
                    success(resp){
                        if(resp.error_message==="success"){
                            // 成功后清空表单
                            botadd.title="";
                            botadd.description="";
                            botadd.content="";
                            // 成功后自动关闭弹窗
                            Modal.getInstance("#add-bot-btn").hide();
                            refresh_bots();//刷新列表在前端显示
                        }else{
                            botadd.error_message=resp.error_message;
                        }
                    }
                })
            }

            // 删除
            const remove_bot=(bot)=>{//需要传入bot
                $.ajax({
                    url:"http://localhost:3000/user/bot/remove/",
                    type:"post",
                    data:{
                        bot_id:bot.id,
                    },
                    headers:{
                        Authorization:"Bearer " + store.state.user.token,
                    },
                    success(resp){
                        if(resp.error_message==="success"){
                            refresh_bots();
                        }
                    }
                })
            }

            // 修改
            const update_bot=(bot)=>{
                botadd.error_message="";
                $.ajax({
                    url:"http://localhost:3000/user/bot/update/",
                    type:"post",
                    data:{
                        bot_id:bot.id,
                        title:bot.title,
                        description:bot.description,
                        content:bot.content,
                    },
                    headers:{
                        Authorization:"Bearer " + store.state.user.token,
                    },
                    success(resp){
                        if(resp.error_message==="success"){
                            // 成功后自动关闭弹窗
                            Modal.getInstance('#update-bot-modal-'+bot.id).hide();
                            refresh_bots();//刷新列表在前端显示
                        }else{
                            bot.error_message=resp.error_message;
                        }
                    }
                })
            }

            return{
                bots,
                botadd,
                add_bot,
                remove_bot,
                update_bot,
            }

            // const store=useStore();
            // $.ajax({
            //     url:"http://localhost:3000/user/bot/add/",
            //     type:"post",
            //     data:{
            //         title:"bot标题",
            //         description:"bot描述",
            //         content:"bot代码",
            //     },
            //     headers:{
            //         Authorization:"Bearer " + store.state.user.token,
            //     },
            //     success(resp){
            //         console.log(resp);
            //     },
            //     error(resp){
            //         console.log(resp);
            //     }
            // })
            // $.ajax({
            //     url:"http://localhost:3000/user/bot/remove/",
            //     type:"post",
            //     data:{
            //         bot_id:25,
            //     },
            //     headers:{
            //         Authorization:"Bearer " + store.state.user.token,
            //     },
            //     success(resp){
            //         console.log(resp);
            //     },
            //     error(resp){
            //         console.log(resp);
            //     }
            // })
            // $.ajax({
            //     url:"http://localhost:3000/user/bot/update/",
            //     type:"post",
            //     data:{
            //         bot_id:17,
            //         title:"修改bot标题",
            //         description:"修改bot描述",
            //         content:"修改bot代码",
            //     },
            //     headers:{
            //         Authorization:"Bearer " + store.state.user.token,
            //     },
            //     success(resp){
            //         console.log(resp);
            //     },
            //     error(resp){
            //         console.log(resp);
            //     }
            // })
            // $.ajax({
            //     url:"http://localhost:3000/user/bot/getlist/",
            //     type:"get",
            //     headers:{
            //         Authorization:"Bearer " + store.state.user.token,
            //     },
            //     success(resp){
            //         console.log(resp);
            //     },
            //     error(resp){
            //         console.log(resp);
            //     }
            // })
        }
    }
</script>

<style scoped>
div.error_message{
    color:red;
}
</style>