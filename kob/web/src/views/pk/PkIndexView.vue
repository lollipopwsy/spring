<template>
    <!-- <div class="container content-field">
        <div class="card">
            <div class="card-body">
                对战
            </div>
        </div>
    </div>
    <div>对战</div> -->
    <!-- 这样写每个页面都要实现一遍，所以把他们重新做一个组件contentfield共同使用 -->
    <!-- <ContentField> -->
        <!-- 对战 -->
    <!-- </ContentField>  -->
    <!-- ContentField里面的内容会被渲染到ContentField.vue里的slot里 -->


    <!-- 对战里面，不用ContentField框，需要引入游戏界面，所以写了一个游戏界面的组件PlayGround -->
    <!-- 删掉了之前的ContentField -->
    <PlayGround v-if="$store.state.pk.status==='playing'"/>
    <!-- 当status是playing而不是matching时候展示 -->
    <MatchGround v-if="$store.state.pk.status==='matching'"/>
</template>

<script>
    // import ContentField from '../../components/ContentField.vue'
    // 引入ContentField组件
    // export default {
        // components: {
            // ContentField
        // }
    // }
    // 加载ContentField组件

    // 引入PlayGround组件
    import PlayGround from '../../components/PlayGround.vue'
    // 引入MatchGround组件
    import MatchGround from '../../components/MatchGround.vue'

    // 当组件被挂载/卸载之后执行的函数
    import { onMounted ,onUnmounted} from 'vue'
    // 引入全局变量
    import { useStore } from 'vuex'

    export default {
        components: {
            PlayGround,
            MatchGround,
        },
        setup(){
            const store = useStore();
            const socketUrl=`ws://localhost:3000/websocket/${store.state.user.token}/`;

            let socket =null;
            // 当当前组件被挂载的时候,创建链接，并把链接存到变量里
            onMounted(()=>{
                store.commit("updateOpponent",{
                    username: "对手",
                    photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
                });
                // 创建socket
                socket = new WebSocket(socketUrl);

                socket.onopen=()=>{
                    console.log("connected!");
                    store.commit("updateSocket",socket);//更新socket
                }

                socket.onmessage= msg =>{
                    const data= JSON.parse(msg.data);
                    // console.log(data);
                    if(data.event==="start-matching"){//匹配成功
                        store.commit("updateOpponent",{
                            username: data.opponent_username,
                            photo: data.opponent_photo,
                        });//更新对手信息
                        // 加个延迟
                        setTimeout(()=>{
                            store.commit("updateStatus","playing");//更新状态为playing
                        },2000);
                        // 更新地图
                        store.commit("updateGamemap",data.gamemap);//更新地图
                    }
                }

                socket.onclose=()=>{
                    console.log("disconnected!");
                }
            });

            // 当当前组件被卸载的时候，关闭链接,status改为matching(点击其他页面在回来时，状态变为matching)
            onUnmounted(()=>{
                socket.close();
                store.commit("updateStatus","matching");//更新状态为matching
            });
        }
    }
</script>

<style scoped>
</style>


