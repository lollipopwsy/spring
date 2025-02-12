import { createRouter, createWebHistory } from 'vue-router'

import PkIndexView from '../views/pk/PkIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import RanklistIndexView from '../views/ranklist/RanklistIndexView'
import UserBotIndexView from '../views/user/bot/UserBotIndexView'
import NotFound from '../views/error/NotFound'
// user的前端
import UserAccountLoginView from '@/views/user/account/UserAccountLoginView.vue'
import UserAccountRegisterView from '@/views/user/account/UserAccountRegisterView.vue'

// 需要读入是不是登录状态
import store from '../store/index'

const routes = [
  {
    path:"/",
    name:"home",
    redirect:"/pk/",
    // 存到域里需不需要授权
    meta:{
        requestAuth:true,
    }
  },
  {
    path:"/pk/",
    name:"pk_index",
    component:PkIndexView,

    meta:{
        requestAuth:true,
    }
  },
  {
    path:"/record/",
    name:"record_index",
    component:RecordIndexView,

    meta:{
        requestAuth:true,
    }
  },
  {
    path:"/ranklist/",
    name:"ranklist_index",
    component:RanklistIndexView,

    meta:{
        requestAuth:true,
    }
  },
  {
    path:"/user/bot/",
    name:"user_bot_index",
    component:UserBotIndexView,

    meta:{
        requestAuth:true,
    }
  },
  {
    path:"/404/",
    name:"404",
    component:NotFound,

    meta:{
        requestAuth:false,
    }
  },
  {
    path:"/:catchAll(.*)",//这是一个通配符，匹配所有路径
    redirect:"/404/"
  },
  {
    path:"/user/account/login/",
    name:"user_account_login",
    component:UserAccountLoginView,

    meta:{
        requestAuth:false,
    }
  },
  {
    path:"/user/account/register/",
    name:"user_account_register",
    component:UserAccountRegisterView,

    meta:{
        requestAuth:false,
    }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 前端页面授权
router.beforeEach((to,from,next)=>{
  // 打开的页面如果需要登录，就重定向到登录页面,不需要就跳转到默认页面
  if(to.meta.requestAuth && !store.state.user.is_login){
    next({name:"user_account_login"});
  }else{
    next();
  }

})

export default router
