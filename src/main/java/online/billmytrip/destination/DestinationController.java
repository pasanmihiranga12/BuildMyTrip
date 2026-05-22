package online.billmytrip.destination;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationRepository repo;

    public DestinationController(DestinationRepository repo) {
        this.repo = repo;
    }

    public record DestinationInput(
            @NotBlank String name,
            @NotBlank String location,
            String description,
            String imageUrl,
            @NotNull BigDecimal basePrice) {}

    @GetMapping
    public List<Destination> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destination> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Destination create(@Valid @RequestBody DestinationInput in) {
        Destination d = new Destination();
        apply(d, in);
        return repo.save(d);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody DestinationInput in) {
        return repo.findById(id).map(d -> {
            apply(d, in);
            return ResponseEntity.ok(repo.save(d));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    private void apply(Destination d, DestinationInput in) {
        d.setName(in.name());
        d.setLocation(in.location());
        d.setDescription(in.description());
        d.setImageUrl(in.imageUrl());
        d.setBasePrice(in.basePrice());
    }
}
