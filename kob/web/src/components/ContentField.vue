<template>
    <div class="container content-field">
        <!-- 白框框太靠上了，所以直接在组件里面改，所有的页面都能共用content-field -->
        <div class="card">
            <div class="card-body">
                <slot></slot>
                <!-- 要填充的内容放在slot里面 -->
            </div>
        </div>
    </div>
</template>

<script>
</script>

<style scoped>
div.content-field{
    margin-top:20px;
    /* 上边距 */
}
</style>