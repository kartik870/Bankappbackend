package com.nagarro.bankappbackend.service.implementation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nagarro.bankappbackend.dto.LoginDto;
import com.nagarro.bankappbackend.dto.SignUpDto;
import com.nagarro.bankappbackend.entity.Role;
import com.nagarro.bankappbackend.entity.User;
import com.nagarro.bankappbackend.repositories.UserRepository;
import com.nagarro.bankappbackend.response.AuthenticationResponse;
import com.nagarro.bankappbackend.service.JwtService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    public void testSignUpSupervisor() {
        SignUpDto signupDetails = new SignUpDto();
        signupDetails.setFirstName("John");
        signupDetails.setLastName("Doe");
        signupDetails.setEmail("john.doe@example.com");
        signupDetails.setPassword("password");
        signupDetails.setRole("supervisor");

        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role(Role.SUPERVISOR)
                .valid(true)
                .build();

        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(user);

        User createdUser = authService.signUp(signupDetails);

        assertEquals("John", createdUser.getFirstName());
        assertEquals("Doe", createdUser.getLastName());
        assertEquals("john.doe@example.com", createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(Role.SUPERVISOR, createdUser.getRole());
        assertEquals(true, createdUser.isValid());
    }

    @Test
    public void testSignUpUser() {
        SignUpDto signupDetails = new SignUpDto();
        signupDetails.setFirstName("Jane");
        signupDetails.setLastName("Smith");
        signupDetails.setEmail("jane.smith@example.com");
        signupDetails.setPassword("password");
        signupDetails.setRole("user");

        User user = User.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .valid(false)
                .build();

        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(user);

        User createdUser = authService.signUp(signupDetails);

        assertEquals("Jane", createdUser.getFirstName());
        assertEquals("Smith", createdUser.getLastName());
        assertEquals("jane.smith@example.com", createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(Role.USER, createdUser.getRole());
        assertEquals(false, createdUser.isValid());
    }

    @Test
    public void testLoginSuccess() {
        LoginDto loginDetails = new LoginDto();
        loginDetails.setEmail("john.doe@example.com");
        loginDetails.setPassword("password");

        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role(Role.SUPERVISOR)
                .valid(true)
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse response = authService.login(loginDetails);

        assertEquals("jwtToken", response.getToken());
        assertEquals(true, response.isValid());
        assertEquals("SUPERVISOR", response.getRole());
    }

    @Test
    public void testLoginFailure() {
        LoginDto loginDetails = new LoginDto();
        loginDetails.setEmail("invalid@example.com");
        loginDetails.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(AuthenticationException.class, () -> {
            authService.login(loginDetails);
        });
    }
}
