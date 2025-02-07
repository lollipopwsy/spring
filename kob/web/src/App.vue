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

import $ from 'jquery';

// import $ from 'jquery';
// import { ref } from 'vue';
// // 导入Vue的ref函数，用于创建响应式变量。
export default {
  components: {
    NavBar
  },
  setup(){
    $.ajax({
      url: "http://localhost:3000/user/account/token/",
      type: "post",
      data:{
        username:'wsy',
        password:'123',
      },
      success(resp){
        console.log(resp);
      },
      error(resp){
        console.log(resp);
      }
    });

    $.ajax({
        url: "http://localhost:3000/user/account/info/",
        type: "get",
        //数据库上传需要上传表头
        headers:{
          Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0NWQxOWE1OGJkN2E0NmJkYWIyMjE5NmM5YmZmYmI5MiIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTczODkzOTI3MCwiZXhwIjoxNzQwMTQ4ODcwfQ.MzEmYi_3-vpkU_VKxsP1BGpGQQMKrADU8TSI2ic0t5g"
        },
        success(resp){
          console.log(resp);
        },
        error(resp){
          console.log(resp);
        }
    })

    $.ajax({
      url: "http://localhost:3000/user/account/register/",
      type: "post",
      data:{
        username:'',
        password:'123',
        confirmedPassword:'123',
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
