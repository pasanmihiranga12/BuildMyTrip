package online.billmytrip.activity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityRepository repo;

    public ActivityController(ActivityRepository repo) {
        this.repo = repo;
    }

    public record ActivityInput(@NotBlank String name, @NotNull BigDecimal pricePerPerson) {}

    @GetMapping
    public List<Activity> all() { return repo.findAll(); }

    @PostMapping
    public Activity create(@Valid @RequestBody ActivityInput in) {
        Activity a = new Activity();
        a.setName(in.name());
        a.setPricePerPerson(in.pricePerPerson());
        return repo.save(a);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ActivityInput in) {
        return repo.findById(id).map(a -> {
            a.setName(in.name());
            a.setPricePerPerson(in.pricePerPerson());
            return ResponseEntity.ok(repo.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }
}
