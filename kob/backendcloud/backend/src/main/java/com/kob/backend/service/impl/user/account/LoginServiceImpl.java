//登陆service接口的实现
package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User1;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service//实现service记得要加注解
public class LoginServiceImpl implements LoginService {//实现接口

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
        //用户名密码需要先封装成类，因为数据库里存的不是明文
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        //这里需要传用户名密码
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);//登录失败会自动报异常
        //登陆成功后用之前定义的api把用户取出来
        UserDetailsImpl LoginUser = (UserDetailsImpl) authenticate.getPrincipal();//函数类型转化一下
        //取出用户
        User1 user1=LoginUser.getUser1();
        //取出来之后再把userid封装成jwttoken
        String jwt = JwtUtil.createJWT(user1.getId().toString());

        Map<String, String> map = new HashMap<>();
        map.put("error_message","success");
        map.put("token",jwt);

        return map;
    }
}
