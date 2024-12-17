package com.example.KTCK.controller;

import com.example.KTCK.dto.UserDto;
import com.example.KTCK.entity.User;
import com.example.KTCK.security.CustomUserDetailsService;
import com.example.KTCK.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    public UserController(UserService userService, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
    }

    // Endpoint để hiển thị thông tin người dùng đã đăng nhập
    @GetMapping("/user-info")
    public String showUserInfo(Model model) {
        // Lấy người dùng từ SecurityContextHolder
        String email = getCurrentUserEmail();
        if (email != null) {
            User userDto = userService.findUserByEmail(email);
            model.addAttribute("user", userDto);
            return "user_info";  // Trả về trang Thymeleaf user_info.html
        } else {
            return "error";  // Nếu không tìm thấy người dùng, trả về trang lỗi
        }
    }

    private String getCurrentUserEmail() {
        // Lấy thông tin người dùng hiện tại từ SecurityContext
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();  // Email người dùng
        }
        return null;
    }
}
