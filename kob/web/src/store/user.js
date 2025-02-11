// 存用户登录信息
// 用到了ajax需要先import进来
import $ from 'jquery'

export default {
    state: {
        id:"",
        username:"",
        photo:"",
        token:"",
        is_login:false,
    },
    getters: {
    },
    mutations: {//用于修改数据
        updateUser(state,user){
            state.id = user.id
            state.username = user.username
            state.photo = user.photo
            state.is_login = true
        },
        updateToken(state,token){
            state.token = token
        },
        logout(state){//退出登录
            state.id = ""//清空id
            state.username = ""//清空用户名
            state.photo = ""//清空头像
            state.token = ""//清空token
            state.is_login = false//登录状态设置为false
        },
    },
    actions: {//一般修改state的函数写在action里面
        login(context,data){//data包含用户输入的登录信息，context是上下文信息
            //登录
            $.ajax({
                url: "http://localhost:3000/user/account/token/",
                type: "post",
                data:{
                  username:data.username,
                  password:data.password,
                },
                success(resp){//成功的话需要将token存下来
                    if(resp.error_message==="success"){//登录成功,这里的error_message是后端LoginServiceImpl定义的字段
                        context.commit("updateToken",resp.token)//action里的函数调用mutation里的函数需要用到commit，这里的resp.token是后端LoginServiceImpl返回的token
                        data.success(resp);//调用成功的回调函数
                    }else{
                        data.error(resp);//调用失败的回调函数
                    }
                },
                error(resp){
                    data.error(resp);//调用失败的回调函数
                }
              });
        },
        getinfo(context,data){//辅助函数，当用户登录完后，动态显示用户信息，向后端发送请求获取用户名
            //获取用户信息
            $.ajax({
                url: "http://localhost:3000/user/account/info/",
                type: "get",
                //数据库上传需要上传表头
                headers:{
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp){
                    // 判断
                    if(resp.error_message==="success"){
                        context.commit("updateUser",{
                            ...resp,//解析resp里的所有字段
                            is_login:true,
                        });
                        // 调用回调函数
                        data.success(resp);
                    }else{
                        data.error(resp);
                    }
                },
                error(resp){
                    data.error(resp);
                }
              });
        },
        logout(context){
            //退出登录
            context.commit("logout");//调用mutation里的logout函数
        }
    },
    modules: {
    }

}