<!-- 对战里面，不用框，需要引入游戏界面，所以写了这个游戏界面的组件 -->

<template>
    <div class="playground">
    <!-- 需要在pk页面加上import -->
    
    <!-- playground里面还需要加正方形地图，所以写新的组件（板子）GameMap --> 
        <GameMap></GameMap>
        <!-- 把gamemap放过来 -->
    </div>
</template>

<script>
import GameMap from './GameMap.vue'

export default {
    components: {
        GameMap,
    }
}

</script>

<style scoped>
/* 设置长宽 */
div.playground{
    width: 60vw;
    height: 70vh;
    /* 屏幕宽度的70% */
    background-color: lightpink;
    margin: 40px auto;
    /* 上下居中 */ 
}
</style>