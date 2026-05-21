package online.billmytrip.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import online.billmytrip.config.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    //REGISTER REQUEST

    public static class RegisterRequest {

        @NotBlank
        @Size(min = 3, max = 60)
        private String username;

        @NotBlank
        @Size(min = 6, max = 100)
        private String password;

        @NotBlank
        @Email
        private String email;

        private String phone;

        public RegisterRequest() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    // LOGIN REQUEST

    public static class LoginRequest {

        @NotBlank
        private String username;

        @NotBlank
        private String password;

        public LoginRequest() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // AUTH RESPONSE 

    public static class AuthResponse {

        private String token;
        private String username;
        private String role;

        public AuthResponse(String token, String username, String role) {
            this.token = token;
            this.username = username;
            this.role = role;
        }

        public String getToken() {
            return token;
        }

        public String getUsername() {
            return username;
        }

        public String getRole() {
            return role;
        }
    }

    //  PROFILE UPDATE 

    public static class ProfileUpdate {

        @Email
        private String email;

        private String phone;

        public ProfileUpdate() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    // REGISTER 

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {

        if (users.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "username taken"));
        }

        if (users.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "email already registered"));
        }

        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setEmail(req.getEmail());
        u.setPhone(req.getPhone());
        u.setRole(Role.TOURIST);

        users.save(u);

        String token = jwt.issue(u.getUsername(), u.getRole().name());

        return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        u.getUsername(),
                        u.getRole().name()
                )
        );
    }

    // LOGIN

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {

        User u = users.findByUsername(req.getUsername())
                .orElseThrow(() ->
                        new AccessDeniedException("invalid credentials"));

        if (!encoder.matches(req.getPassword(), u.getPassword())) {
            throw new AccessDeniedException("invalid credentials");
        }

        String token = jwt.issue(u.getUsername(), u.getRole().name());

        return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        u.getUsername(),
                        u.getRole().name()
                )
        );
    }

    // GET PROFILE 

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {

        if (auth == null) {
            return ResponseEntity.status(401).build();
        }

        User u = users.findByUsername(auth.getName())
                .orElseThrow();

        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "username", u.getUsername(),
                "email", u.getEmail(),
                "phone", u.getPhone() == null ? "" : u.getPhone(),
                "role", u.getRole().name()
        ));
    }

    //  UPDATE PROFILE 

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(
            Authentication auth,
            @Valid @RequestBody ProfileUpdate req
    ) {

        if (auth == null) {
            return ResponseEntity.status(401).build();
        }

        User u = users.findByUsername(auth.getName())
                .orElseThrow();

        if (req.getEmail() != null && !req.getEmail().isBlank()) {
            u.setEmail(req.getEmail());
        }

        if (req.getPhone() != null) {
            u.setPhone(req.getPhone());
        }

        users.save(u);

        return ResponseEntity.ok(Map.of("ok", true));
    }

    //  EXCEPTION 

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> denied(AccessDeniedException ex) {

        return ResponseEntity.status(401)
                .body(Map.of("error", ex.getMessage()));
    }
}
