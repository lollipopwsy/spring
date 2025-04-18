<template>
    <div class="result-board">
        <div class="result-board-text" v-if="$store.state.pk.loser==='all'">
            Draw
        </div>
        <!-- 要判断自己是不是a,b -->
        <!-- 注意这里user.id和a_id不一样，一个是int一个是字符串，要用== 。或者===parseInt($store.shate.user.id)-->
        <div class="result-board-text" v-else-if="$store.state.pk.loser==='A' && $store.state.pk.a_id==$store.state.user.id">
            Lose
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser==='B' && $store.state.pk.b_id==$store.state.user.id">
            Lose
        </div>
        <div class="result-board-text" v-else>
            Win
        </div>
        <div class="result-board-btn">
            <button @click="restart" type="button" class="btn btn-warning btn-lg">
                重新匹配
            </button>
        </div>
    </div>
</template>

<script>

import {useStore} from 'vuex'

export default{
    setup(){
        const store=useStore();
        // 这里的store是vuex的store对象，里面有很多方法和属性可以使用

        const restart=()=>{
            store.commit("updateStatus","matching");//改为matching
            store.commit("updateLoser","none");
            store.commit("updateOpponent",{
                    username: "对手",
                    photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
                });
        }

        return {
            restart
        };
        // 这里的restart是给按钮绑定的事件函数
    }
}
</script>

<style scoped>
div.result-board{
    height:30vh;
    width:30vw;
    background-color: rgba(50,50,50,0.5);
    position:absolute;
    top:30vh;
    left:35vw;
}
div.result-board-text{
    text-align: center;
    font-size: 50px;
    font-weight: 600;
    font-style: italic;
    color: white;
    padding-top: 5vh;
}
div.result-board-btn{
    text-align: center;
    padding-top: 7vh;
}
</style>