<!-- 对战里面，不用框，需要引入游戏界面，所以写了这个游戏界面的组件 -->

<template>
    <div class="matchground">
    <!-- 需要在pk页面加上import -->
    <div class="row">
        <div class="col-4">
            <div class="user-photo">
                <img :src="$store.state.user.photo" alt="">
            </div>
            <div class="user-username">
                {{$store.state.user.username}}
            </div>
        </div>
        <div class="col-4">
            <div class="user-select-bot">
                <select v-model="select_bot" class="form-select" aria-label="Default select example">
                    <option value="-1" selected>亲自出马</option>
                    <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                        {{ bot.title }}
                    </option>
                </select>
            </div>
        </div>
        <div class="col-4">
            <div class="user-photo">
                <img :src="$store.state.pk.opponent_photo" alt="">
            </div>
            <div class="user-username">
                {{$store.state.pk.opponent_username}}
            </div>
        </div>
        <div class="col-12" style="text-align: center; padding-top:15vh;">
            <button @click="click_match_btn" type="button" class="btn btn-warning btn-lg">{{match_btn_info}}</button>
        </div>
    </div>
    </div>
</template>

<script>
import {ref} from 'vue'
// 全局变量
import {useStore} from 'vuex';
import $ from 'jquery';

export default {
    setup(){
        const store=useStore();

        let match_btn_info=ref("开始匹配");
        // 创建一个变量match_btn_info，用来存储按钮的文字

        let bots=ref([]);//定义一个空列bots
        let select_bot=ref("-1");//定义一个空列select_bot，默认值是-1


        // 写一个事件函数，点击按钮的时候，如果按钮文字是开始匹配，就变成取消匹配，反之亦然
        const click_match_btn=()=>{
            if(match_btn_info.value==="开始匹配"){
                match_btn_info.value="取消匹配";
                // console.log(select_bot.value);
                // 开始匹配的话是cliet像后端发送一个请求，告诉后端我要开始匹配
                store.state.pk.socket.send(JSON.stringify({//可以发送字符串，把json封装成字符串
                    event:"start-matching",//向后端传一个域,后端可以onMessage接收到请求
                    bot_id:select_bot.value,//bot_id是选中的id
                }));
            }else{
                match_btn_info.value="开始匹配";
                store.state.pk.socket.send(JSON.stringify({//可以发送字符串，把json封装成字符串
                    event:"stop-matching",//传一个域

                }));
            }
        }

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

        refresh_bots();//调用函数从云端动态获取bots

        return{
            match_btn_info,
            click_match_btn,
            bots,
            select_bot,
        }
    }

}

</script>

<style scoped>
/* 设置长宽 */
div.matchground{
    width: 60vw;
    /* 屏幕宽度的60 */
    height: 70vh;
    /* 屏幕高度的70% */
    /* background-color: lightpink; */
    margin: 40px auto;
    /* 上下居中 */ 
    background-color:rgba(50,50,50,0.5);
}
div.user-photo{
    text-align: center;
    padding-top: 10vh;
}
div.user-photo >img{
    border-radius: 50%;
    width:20vh;
}
div.user-username{
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color:white;
    padding-top:2vh;
}
div.user-select-bot{
    padding-top: 20vh;
}
div.user-select-bot >select{
    width: 60%;
    margin: 0 auto;
    /* 居中 */
}
</style>