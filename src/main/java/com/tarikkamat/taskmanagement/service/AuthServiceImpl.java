package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.TokenDto;
import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.exception.DatabaseException;
import com.tarikkamat.taskmanagement.requests.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public TokenDto authenticate(LoginRequest loginRequest) {
        String identifier = loginRequest.identifier();
        String password = loginRequest.password();

        User user = userService.findByEmailOrUsername(identifier);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));

        String accessToken = jwtService.generateToken(user);
        long expiresIn = jwtService.getExpirationTime();

        return new TokenDto(accessToken, expiresIn);
    }

    @Override
    @Transactional
    public void register(UserDto registerUserDto) {
        try {
            String username = registerUserDto.username();
            String email = registerUserDto.email();
            String password = registerUserDto.password();

            User createdUser = new User();
            createdUser.setUsername(username);
            createdUser.setEmail(email);
            createdUser.setPassword(passwordEncoder.encode(password));

            userService.createUser(userService.toDto(createdUser));
        } catch (DataIntegrityViolationException ex) {
            throw DatabaseException.fromDataIntegrityViolation(ex);
        }
    }
}
