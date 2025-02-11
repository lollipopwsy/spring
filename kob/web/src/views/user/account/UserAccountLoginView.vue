<template>
    <ContentField>
        <!-- 登录 -->
        <!-- 在Bootstrap里找grid -->
        <!-- div.row>div.col-3，然后tab键补全 -->
        <!-- justify-content-md-center，让div.row里的div.col-3居中 -->
        <div class="row justify-content-md-center">
            <div class="col-3">
                <!-- 再找一个表单的样式 -->
                <form @submit.prevent="login">
                <!-- 需要绑定内容和变量，提交触发login函数，prevent阻止默认行为 -->
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <!-- 密码错误报错信息 -->
                    <div class="error-message">{{error_message}}</div>
                    <button type="submit" class="btn btn-primary">提交</button>
                </form>
            </div>
        </div>
    </ContentField>
    <!-- ContentField里面的内容会被渲染到ContentField.vue里的slot里 -->
</template>

<script>
    import ContentField from '../../../components/ContentField.vue'

    import {useStore} from 'vuex'
    import {ref} from 'vue'//所有变量都要用ref

    // 提交正确后导入到主页面
    import router from '../../../router/index'

    export default {
        components: {
            ContentField
        },
        setup(){//写完user.js后，要在这个组件里面实现
            const store=useStore();
            let username=ref('');
            let password=ref('');
            let error_message=ref('');

            //定义一个点击提交button后触发登录的函数
            const login=()=>{
                // 每次提交先清空错误信息
                error_message.value='';
                // 如果触发的话，就会调用actions里的login函数，用dispatch
                store.dispatch("login",{
                    username:username.value,//ref的值要用value
                    password:password.value,
                    success(){
                        // 获取用户信息
                        store.dispatch("getinfo",{
                            // 调用回调函数
                            success(){
                                // 登录成功后跳转到主页
                                router.push({name:'home'});
                                console.log(store.state.user);
                            }
                        })
                    },
                    error(){
                        error_message.value="用户名或密码错误";
                    }
                })
            }

            return {
                username,
                password,
                error_message,
                login,
            }

        }
    }
</script>

<style scoped>
button{
    width: 100%;
}
div.error-message{
    color:red;
}
</style>