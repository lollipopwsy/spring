<!-- 对战里面，不用框，需要引入游戏界面，所以写了这个游戏界面的组件 -->

<template>
    <div class="matchground">
    <!-- 需要在pk页面加上import -->
    <div class="row">
        <div class="col-6">
            <div class="user-photo">
                <img :src="$store.state.user.photo" alt="">
            </div>
            <div class="user-username">
                {{$store.state.user.username}}
            </div>
        </div>
        <div class="col-6">
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

export default {
    setup(){
        let match_btn_info=ref("开始匹配");
        // 创建一个变量match_btn_info，用来存储按钮的文字

        // 写一个事件函数，点击按钮的时候，如果按钮文字是开始匹配，就变成取消匹配，反之亦然
        const click_match_btn=()=>{
            if(match_btn_info.value==="开始匹配"){
                match_btn_info.value="取消匹配";
            }else{
                match_btn_info.value="开始匹配";
            }
        }
        return{
            match_btn_info,
            click_match_btn,
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
</style>