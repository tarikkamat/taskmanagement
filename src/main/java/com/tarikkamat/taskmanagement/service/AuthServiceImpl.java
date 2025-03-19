package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.user.AuthenticateDto;
import com.tarikkamat.taskmanagement.dto.user.LoginResponse;
import com.tarikkamat.taskmanagement.dto.user.RegisterDto;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.exception.DatabaseException;
import com.tarikkamat.taskmanagement.mapper.UserMapper;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public LoginResponse authenticate(AuthenticateDto authenticateDto) {
        String identifier = authenticateDto.identifier();
        String password = authenticateDto.password();

        User user = userRepository.findByEmailOrUsername(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));

        String accessToken = jwtService.generateToken(user);
        long expiresIn = jwtService.getExpirationTime();

        return new LoginResponse(accessToken, expiresIn);
    }

    @Override
    @Transactional
    public void register(RegisterDto registerDto) {
        try {
            String username = registerDto.username();
            String email = registerDto.email();
            String password = registerDto.password();

            User createdUser = new User();
            createdUser.setUsername(username);
            createdUser.setEmail(email);
            createdUser.setPassword(passwordEncoder.encode(password));

            userService.createUser(userMapper.toDto(createdUser));
        } catch (DataIntegrityViolationException ex) {
            throw DatabaseException.fromDataIntegrityViolation(ex);
        }
    }
}
