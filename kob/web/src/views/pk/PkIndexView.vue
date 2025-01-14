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
    <PlayGround />
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
    export default {
        components: {
            PlayGround
        }
    }
</script>

<style scoped>
</style>


