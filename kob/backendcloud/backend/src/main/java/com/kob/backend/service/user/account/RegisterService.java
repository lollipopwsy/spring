package com.kob.backend.service.user.account;

//user账户里面创建第3个api

import java.util.Map;

public interface RegisterService {
    public Map<String, String> register(String username, String password, String confirmedPassword, String qq);
}
