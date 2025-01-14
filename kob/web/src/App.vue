<template>
  <NavBar/>

  <!-- <div>
    <div>Bot昵称：{{ bot_name }}</div>
    #调用bot_name 
    <div>Bot战力：{{ bot_rating }}</div>
    #调用bot_rating 
  </div>-->
  <router-view></router-view> 
</template>

<script> 
import NavBar from './components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"

// import $ from 'jquery';
// import { ref } from 'vue';
// // 导入Vue的ref函数，用于创建响应式变量。
export default {
  components: {
    NavBar
  }
}

// // 导出一个Vue组件对象。
//   name: 'App',
//   // 指定组件的名称为App。
//   setup:() =>{
//   // setup函数用于初始化组件的数据。
//     let bot_name = ref("");
//     let bot_rating= ref("");
//     // 创建两个响应式变量bot_Name和bot_rating。
    
//     // 通过ajax请求获取数据
//     $.ajax({
//       url: "http://localhost:3000/pk/getBotInfo/",
//       type: "get",
//       // get类型获取数据
//       success: resp=>{
//         bot_name.value = resp.name;
//         bot_rating.value = resp.rating;
//         // 将获取到的数据赋值给bot_Name和bot_rating。
//       }
//       // 
//     });
//     return {
//       bot_name,
//       bot_rating
//     }
//   }
// }
</script> 

<style>
body {
  background-image: url("./assets/images/background.png");
  background-size: cover;
}
</style>
