package com.example.KTCK.controller;

import com.example.KTCK.dto.UserDto;
import com.example.KTCK.entity.User;
import com.example.KTCK.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(@Qualifier("userServiceImpl") UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }
    // handler method to handle list of users
    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }
    @GetMapping("/info")
public String userInfo(Model model) {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String email;

    if (principal instanceof UserDetails) {
        email = ((UserDetails) principal).getUsername();
    } else {
        email = principal.toString();
    }

    // Tìm người dùng qua email và thêm thông tin vào model
    User user = userService.findUserByEmail(email);
    model.addAttribute("user", user);

    return "admin/user_info"; // Hiển thị trang thông tin người dùng
}
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/admin/users/add")
public String showAddUserForm(Model model) {
    UserDto user = new UserDto();
    model.addAttribute("user", user);
    return "admin/add-user";
}

@PostMapping("/admin/users/add")
public String addUser(@ModelAttribute("user") UserDto userDto) {
    userService.saveUser(userDto);
    return "redirect:/admin/users?success";
}

@GetMapping("/admin/users/edit/{id}")
public String showEditUserForm(@PathVariable("id") Long id, Model model) {
    User user = userService.findUserById(id); // Sử dụng service để tìm user theo id
    UserDto userDto = new UserDto();
    String[] names = user.getName().split(" ");
    userDto.setFirstName(names[0]);
    userDto.setLastName(names.length > 1 ? names[1] : "");
    userDto.setEmail(user.getEmail());
    model.addAttribute("user", userDto);
    model.addAttribute("id", id); // Gửi ID xuống view để sử dụng
    return "admin/edit-user";
}

@PostMapping("/admin/users/edit/{id}")
public String editUser(@PathVariable Long id, @ModelAttribute("user") UserDto userDto) {
    userService.updateUser(id, userDto);
    return "redirect:/admin/users?success";
}
@GetMapping("/admin/users/delete/{id}")
public String deleteUser(@PathVariable Long id) {
    userService.deleteUserById(id);
    return "redirect:/admin/users?success";
}

}
