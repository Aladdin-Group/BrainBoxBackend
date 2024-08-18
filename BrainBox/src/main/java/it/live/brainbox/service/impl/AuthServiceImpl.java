package it.live.brainbox.service.impl;

import it.live.brainbox.entity.User;
import it.live.brainbox.entity.enums.SystemRoleName;
import it.live.brainbox.exception.MainException;
import it.live.brainbox.jwt.JwtProvider;
import it.live.brainbox.mapper.UserMapper;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.UserDTO;
import it.live.brainbox.repository.UserRepository;
import it.live.brainbox.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;
    private Boolean isDebug = false;

    @Override
    public ResponseEntity<ApiResponse> regLog(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByEmailAndUniqueId(userDTO.getEmail(), userDTO.getUniqueId());

        if (existingUser.isPresent()) {
            // User exists with the same email and UID, just log them in
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Welcome back")
                    .status(200)
                    .object(jwtProvider.generateToken(userDTO.getEmail()))
                    .build());
        } else {
            // Check if a user with this email exists
            Optional<User> userWithSameEmail = userRepository.findByEmail(userDTO.getEmail());

            if (userWithSameEmail.isPresent()) {
                // A user with this email exists, but with a different UID
                // Delete the old user
                userRepository.delete(userWithSameEmail.get());
            }

            // Create a new user (either because no user existed or we just deleted the old one)
            User newUser = userMapper.toEntity(userDTO);
            userRepository.save(newUser);

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Welcome")
                    .status(200)
                    .object(jwtProvider.generateToken(userDTO.getEmail()))
                    .build());
        }
    }

    @Override
    public ResponseEntity<ApiResponse> isDebug() {
        if (isDebug)
            return ResponseEntity.ok(ApiResponse.builder().message("Xush kelibsiz").status(200).object(jwtProvider.generateToken("dev@gmail.com")).build());
        return null;
    }

    @Override
    public Boolean onOrOf() {
        if (isDebug) {
            isDebug = false;
            return false;
        } else {
            isDebug = true;
            return true;
        }
    }


}
