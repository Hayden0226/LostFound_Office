package com.lostfound.controller;

import com.lostfound.common.Result;
import com.lostfound.dto.LoginDto;
import com.lostfound.dto.RegisterDto;
import com.lostfound.entity.User;
import com.lostfound.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDto dto) {
        String token = userService.login(dto.getUsername(), dto.getPassword());
        return Result.success(token);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterDto dto) {
        userService.register(dto.getUsername(), dto.getPassword(), dto.getNickname(), dto.getPhone(), dto.getEmail());
        return Result.success();
    }

    @GetMapping("/info")
    public Result<User> info(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(401, "未登录");
        }
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(null);
        return Result.success(user);
    }

    @GetMapping("/public/{id}")
    public Result<User> publicInfo(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody User user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        user.setId(userId);
        user.setUsername(null);  // 不允许修改用户名
        userService.updateById(user);
        return Result.success();
    }
}
