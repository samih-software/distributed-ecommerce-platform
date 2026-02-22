package com.personal.store.auth;


import com.personal.store.users.UserDto;
import com.personal.store.users.UserMapper;
import com.personal.store.users.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response)
    {


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );


        var user =  userRepository.findByEmail(request.getEmail()).orElseThrow();


        var accessToken = jwtService.generateAccessToken(user);

        var refreshToken = jwtService.generateRefreshToken(user);


//        var cookie = new Cookie("refreshToken", refreshToken);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/auth/refresh");
//        cookie.setMaxAge(604800); // 7d same value
//        cookie.setSecure(true); // only send with https
//        response.addCookie(cookie);

        // Create refresh token cookie
        Cookie cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 7 days
        cookie.setSecure(true); // use 'true' in HTTPS

        // Add cookie to response
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));

    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(
            @CookieValue(value = "refreshToken") String refreshToken
    ){


        var jwt = jwtService.parseToken(refreshToken);

        if ( jwt == null || jwt.isExpired()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var userId = jwt.getUserId();
        var user = userRepository.findById(userId).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));

    }


    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
       var authentication =  SecurityContextHolder.getContext().getAuthentication();
       var userId = (Long) authentication.getPrincipal();

       var user = userRepository.findById(userId).orElse(null);

       if (user == null) {
           return ResponseEntity.notFound().build();
       }

       var userDto = userMapper.userToUserDto(user);

       return ResponseEntity.ok(userDto);

    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException(){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


}
