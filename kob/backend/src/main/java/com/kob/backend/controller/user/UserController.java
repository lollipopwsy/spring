////第一节课写的只是为了调试，第二节写完登陆的service后这个代码就没用了
//package com.kob.backend.controller.user;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.kob.backend.mapper.User1Mapper;
//import com.kob.backend.pojo.User1;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.management.Query;
//import java.util.List;
//
////加注解
//@RestController
//public class UserController {
//
////    要用到数据库里的Mapper的话，要加注解
//    @Autowired
////    调用定义的UserMapper接口
//    User1Mapper user1Mapper;
//
////    查询操作
//    @GetMapping("/user/all/")
//    public List<User1> getAll() {//返回所有的用户
//        return user1Mapper.selectList(null);
//    }
//
//    @GetMapping("/user/{userId}/")//传参数是在大括号里写上一个参数名称
//    public User1 getUser(@PathVariable int userId) {//路径参数需要加注解，声明userId是路径参数
////        pojo里的User是数据库表，user的一个对象是数据库表的一行，所以这里的getuser是返回数据库表的行
//        return user1Mapper.selectById(userId);
////        select * from where id = 123;
//
////        第二种写法,queryWrapper里面有很多api
////        QueryWrapper<User1> queryWrapper = new QueryWrapper<>();
////        queryWrapper.eq("id", userId);
////        queryWrapper.ge("id",2).le("id",3);
////        eq:=
////        gt:>
////        ge:>=
////        lt:<
////        le:<=
////        return user1Mapper.selectOne(queryWrapper);
//    }
//
////    插入操作
//    @GetMapping("/user/add/{userId}/{username}/{password}/")
//    public String addUser(//使用注解把路径里的三个参数取出来
//            @PathVariable int userId,
//            @PathVariable String username,
//            @PathVariable String password) {
//
////        if(password.length() < 6) {
////            return "密码太短";
////        }
//
////        直接存储加密后的密码
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(password);
//        //创建User1
//        User1 user1 = new User1(userId, username, encodedPassword);
//        //插入一行
//        user1Mapper.insert(user1);
//        return "Add User1 Successfully";
//    }
//
//    //删除操作
//    @GetMapping("/user/delete/{userId}/")
//    public String deleteUser(@PathVariable int userId) {
//        user1Mapper.deleteById(userId);
//        return "Delete User1 Successfully";
//    }
//}
