<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">
    <!-- <a class="navbar-brand" href="/">King of Bot</a> -->
    <!-- 每次点击按钮都刷新页面的写法 -->
   <router-link class="navbar-brand" :to="{name:'home'}">King of Bot</router-link>
   <!-- 每次点击按钮都不刷新页面的写法 -->
    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <!-- <a class="nav-link active" href="/pk/">对战</a> -->
          <!-- <router-link class="nav-link" :to="{name:'pk_index'}">对战</router-link> -->
          <!-- 想高亮在class里用active，但我们想判断页面后让当前页面高亮 -->
          <!-- 在下面script里面设置返回名 -->
          <router-link :class="route_name=='pk_index' ? 'nav-link active': 'nav-link'" :to="{name:'pk_index'}">对战</router-link>
        </li>
        <li class="nav-item">
          <!-- <a class="nav-link" href="/record/">对局列表</a> -->
          <!-- <router-link class="nav-link" :to="{name:'record_index'}">对局列表</router-link> -->
          <router-link :class="route_name=='record_index' ? 'nav-link active': 'nav-link'" :to="{name:'record_index'}">对局列表</router-link>
        </li>
        <li class="nav-item">
          <!-- <a class="nav-link" href="/ranklist/">排行榜</a> -->
          <!-- <router-link class="nav-link" :to="{name:'ranklist_index'}">排行榜</router-link> -->
          <router-link :class="route_name=='ranklist_index' ? 'nav-link active': 'nav-link'" :to="{name:'ranklist_index'}">排行榜</router-link>
        </li>
      </ul>
      <ul class="navbar-nav" v-if="$store.state.user.is_login">
      <!-- v-if判断是否登录，如果登录则显示用户名，否则显示登录和注册 -->
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            {{$store.state.user.username}}
          </a>
          <ul class="dropdown-menu">
            <!-- <li><a class="dropdown-item" href="/user/bot/">我的Bot</a></li> -->
            <li><router-link class="dropdown-item" :to="{name:'user_bot_index'}">我的Bot</router-link></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="#" @click="logout">退出登录</a></li>
            <!-- @click="logout",点击退出登录时触发函数logout -->
          </ul>
        </li>
      </ul>
      <ul class="navbar-nav" v-else>
      <!-- v-else判断登录不成功的情况显示登录和注册 -->
        <li class="nav-item">
          <router-link class="nav-link" :to="{name:'user_account_login'}" role="button">
          <!-- 把a换成router-link来实现点击登录跳转到登录页面 -->
            登录
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{name:'user_account_register'}" role="button">
            注册
          </router-link>
        </li>
      </ul>
    </div>
  </div>
</nav>
</template>

<script>
    import { useRoute } from 'vue-router';
    // 先从vue-router中获取API
    import { computed } from 'vue';
    // 实时计算函数computed来实时计算

    // 退出登录事件要import一个useStore
    import { useStore } from 'vuex';

    export default {
        setup() {
            const store = useStore();

            const route = useRoute();
            // 通过useRoute()获取当前route
            let route_name = computed(() => route.name);
            // 通过computed()实时返回route的name

            // 退出登录添加事件
            const logout = () => {//触发函数
                store.dispatch('logout');
            }
            return {
                route_name,
                logout
            }
        }
    }
</script>

<style scoped>
/* 将样式限定在当前组件内，使其不会影响到其他组件 */

</style>