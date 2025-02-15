package com.kob.backend.service.user.account;

//user账户里面创建第一个api

import java.util.Map;

public interface LoginService {
    public Map<String, String> getToken(String username, String password);
}
