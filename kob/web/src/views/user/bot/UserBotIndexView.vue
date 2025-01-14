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
    export default {
        components: {
            ContentField
        }
    }
</script>

<style scoped>
</style>