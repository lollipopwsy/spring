package com.kob.backend.pojo;

//POJO和数据库是对应的

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

//pojo里的User是数据库表，user的一个对象是数据库表的一行
public class User1 {
    //  将数据库中的id域变为自增
    //1、在数据库中将id列变为自增
    //2、在pojo.User类中添加注解：@TableId(type = IdType.AUTO)
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String photo;//新增头像列，要和数据库对应
}


