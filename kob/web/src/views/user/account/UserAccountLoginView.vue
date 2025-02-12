<template>
    <ContentField v-if="!$store.state.user.pulling_info">
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
            
            // let show_content=ref(false);//默认不展示登录页面

            // 登录页面持久化，将token存在localStorage里面，每次重定向到登陆页面时，先判断本地有没有存token
            const jwt_token=localStorage.getItem("jwt_token");//本地把token取出来
            if(jwt_token){//如果存在token，就存到内存
                store.commit("updateToken",jwt_token);//调用mutations里的函数要用commit
                // 验证token是否合法，就是从云端获取用户信息
                store.dispatch("getinfo",{//调用actions里的getinfo函数，用dispatch
                    success(){
                        router.push({name:'home'});//成功直接跳转到主页，但这种情况下会一闪而过登录页面，改进：先默认不展示登录页面，再写一个判断语句决定是否展示登录页面
                        store.commit("updatePullingInfo",false);//拉取结束用false，调用Mutation里的updatePullingInfo函数，用commit
                    },
                    error(){
                        // show_content.value=true;//如果获取失败，就展示登录页面
                        store.commit("updatePullingInfo",false);//拉取结束用false，调用Mutation里的updatePullingInfo函数，用commit
                    }
                })
            }else{
                // show_content.value=true;//如果本地没有token，就展示登录页面
                store.commit("updatePullingInfo",false);//拉取结束用false，调用Mutation里的updatePullingInfo函数，用commit
            }

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
                // show_content,//返回
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