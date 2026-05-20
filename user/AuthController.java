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

    public record RegisterRequest(
            @NotBlank @Size(min = 3, max = 60) String username,
            @NotBlank @Size(min = 6, max = 100) String password,
            @NotBlank @Email String email,
            String phone) {}

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

    public record AuthResponse(String token, String username, String role) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (users.existsByUsername(req.username())) {
            return ResponseEntity.badRequest().body(Map.of("error", "username taken"));
        }
        if (users.existsByEmail(req.email())) {
            return ResponseEntity.badRequest().body(Map.of("error", "email already registered"));
        }
        User u = new User();
        u.setUsername(req.username());
        u.setPassword(encoder.encode(req.password()));
        u.setEmail(req.email());
        u.setPhone(req.phone());
        u.setRole(Role.TOURIST);
        users.save(u);

        String token = jwt.issue(u.getUsername(), u.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, u.getUsername(), u.getRole().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        User u = users.findByUsername(req.username())
                .orElseThrow(() -> new AccessDeniedException("invalid credentials"));
        if (!encoder.matches(req.password(), u.getPassword())) {
            throw new AccessDeniedException("invalid credentials");
        }
        String token = jwt.issue(u.getUsername(), u.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, u.getUsername(), u.getRole().name()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "username", u.getUsername(),
                "email", u.getEmail(),
                "phone", u.getPhone() == null ? "" : u.getPhone(),
                "role", u.getRole().name()));
    }

    public record ProfileUpdate(@Email String email, String phone) {}

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(Authentication auth, @Valid @RequestBody ProfileUpdate req) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName()).orElseThrow();
        if (req.email() != null && !req.email().isBlank()) u.setEmail(req.email());
        if (req.phone() != null) u.setPhone(req.phone());
        users.save(u);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> denied(AccessDeniedException ex) {
        return ResponseEntity.status(401).body(Map.of("error", ex.getMessage()));
    }
}
