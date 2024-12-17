package com.example.KTCK.service.impl;

import com.example.KTCK.dto.UserDto;
import com.example.KTCK.entity.User;
import com.example.KTCK.entity.Role;
import com.example.KTCK.repository.RoleRepository;
import com.example.KTCK.repository.UserRepository;
import com.example.KTCK.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
public void saveUser(UserDto userDto) {
    User user = new User();
    user.setName(userDto.getFirstName() + " " + userDto.getLastName());
    user.setEmail(userDto.getEmail());
    // Mã hóa mật khẩu
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));

    // Lấy vai trò từ DTO hoặc mặc định là ROLE_USER
    String roleName = userDto.getRole() != null ? userDto.getRole() : "ROLE_USER";
    Role role = roleRepository.findByName(roleName);

    // Nếu role chưa tồn tại, tạo role mới
    if (role == null) {
        role = new Role();
        role.setName(roleName);
        roleRepository.save(role);
    }

    // Gán vai trò cho người dùng
    user.setRoles(Arrays.asList(role));

    // Lưu người dùng vào cơ sở dữ liệu
    userRepository.save(user);
}

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Override
    public void updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}