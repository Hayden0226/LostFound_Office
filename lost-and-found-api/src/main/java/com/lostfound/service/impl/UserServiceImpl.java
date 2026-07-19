package com.lostfound.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lostfound.dto.LoginDto;
import com.lostfound.dto.RegisterDto;
import com.lostfound.entity.User;
import com.lostfound.mapper.UserMapper;
import com.lostfound.service.UserService;
import com.lostfound.utils.JwtUtils;
import com.lostfound.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public String login(String username, String password) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!PasswordUtils.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
    }

    @Override
    public boolean register(String username, String password, String nickname, String phone, String email) {
        User existing = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        if (existing != null) {
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(PasswordUtils.encrypt(password));
        user.setNickname(nickname != null ? nickname : username);
        user.setPhone(phone);
        user.setEmail(email);
        user.setRole("user");
        return save(user);
    }
}
