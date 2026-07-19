package com.lostfound.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lostfound.entity.User;

public interface UserService extends IService<User> {
    String login(String username, String password);
    boolean register(String username, String password, String nickname, String phone, String email);
}
