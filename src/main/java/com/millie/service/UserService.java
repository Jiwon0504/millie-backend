package com.millie.service;

import com.millie.dto.UserDto;
import com.millie.entity.User;
import com.millie.repository.UserRepository;
import com.millie.dto.LoginRequest;   
import com.millie.dto.LoginResponse;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto);
    }

    // ✅ 추가: 이메일로 사용자 조회
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDto);
    }

    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + userDto.getEmail());
        }
        
        User user = convertToEntity(userDto);
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));
        
        // Check if email is already taken by another user
        if (!user.getEmail().equals(userDto.getEmail()) && 
            userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + userDto.getEmail());
        }
        
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        
        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + id);
        }
        userRepository.deleteById(id);
    }

    // ✅ 추가: 더미 로그인 (테스트용)
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

            if (userOpt.isEmpty()) {
                return new LoginResponse(false, "존재하지 않는 이메일입니다.");
            }

            User user = userOpt.get();

         // 실제 데이터베이스 사용자들로 변경
        if ("1234".equals(loginRequest.getPassword())) {
            // 데이터베이스에 있는 사용자면 로그인 성공
            UserDto userDto = convertToDto(user);
            return new LoginResponse(true, "로그인 성공", userDto);
        } else {
            return new LoginResponse(false, "비밀번호가 일치하지 않습니다.");
        }

        } catch (Exception e) {
            return new LoginResponse(false, "로그인 처리 중 오류가 발생했습니다.");
        }
    }
    
    private UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
    
    private User convertToEntity(UserDto userDto) {
        return new User(userDto.getName(), userDto.getEmail());
    }
}
