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
    <ContentField>
        我的Bot
    </ContentField>
    <!-- ContentField里面的内容会被渲染到ContentField.vue里的slot里 -->
</template>

<script>
    import ContentField from '../../../components/ContentField.vue'

    // 调试
    import $ from 'jquery'//引入ajax
    import {useStore} from'vuex'//引入全局变量

    export default {
        components: {
            ContentField
        },
        setup(){
            const store=useStore();
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
            $.ajax({
                url:"http://localhost:3000/user/bot/remove/",
                type:"post",
                data:{
                    bot_id:25,
                },
                headers:{
                    Authorization:"Bearer " + store.state.user.token,
                },
                success(resp){
                    console.log(resp);
                },
                error(resp){
                    console.log(resp);
                }
            })
        }
    }
</script>

<style scoped>
</style>