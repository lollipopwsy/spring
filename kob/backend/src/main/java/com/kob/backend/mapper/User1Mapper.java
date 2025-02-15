package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.User1;
import org.apache.ibatis.annotations.Mapper;

//添加注解
@Mapper
//用extends继承来使用mybetis-plus的自动写语句
public interface User1Mapper extends BaseMapper<User1> {
}
