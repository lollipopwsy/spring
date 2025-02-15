<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <!-- 再找一个表单的样式 -->
                <form @submit.prevent="register">
                <!-- 需要绑定内容和变量，提交触发register函数，prevent阻止默认行为 -->
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <div class="mb-3">
                        <label for="confirmedPassword" class="form-label">确认密码</label>
                        <input v-model="confirmedPassword" type="password" class="form-control" id="confirmedPassword" placeholder="请再次输入密码">
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
    import {ref} from 'vue'
    import router from '../../../router/index'
    import $ from 'jquery'


    export default {
        components: {
            ContentField
        },
        setup(){
            let username = ref('');
            let password = ref('');
            let confirmedPassword = ref('');
            let error_message = ref('');

            // 触发函数
            const register=()=>{
                // 为什么注册的Ajax放在这里，不像登陆放在user.js里面?
                // 当里面的state会修改时，放在user.js里面，不会修改时，放在这里
                $.ajax({
                    url: "http://localhost:3000/user/account/register/",
                    type: "post",
                    data:{
                        username:username.value,
                        password:password.value,
                        confirmedPassword:confirmedPassword.value,
                    },
                    success(resp){
                        if(resp.error_message==="success"){
                            router.push({name:"user_account_login"});
                        }else{
                            error_message.value=resp.error_message;
                        }
                    },
                });
            }

            return{
                username,
                password,
                confirmedPassword,
                error_message,
                register,
            }
        }
    }
</script>

<style scoped>
button{
    width:100%;
}
div.error-message{
    color: red;
}
</style>