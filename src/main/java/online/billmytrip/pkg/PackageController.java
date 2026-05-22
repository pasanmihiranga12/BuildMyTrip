package online.billmytrip.pkg;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/packages")
public class PackageController {

    private final PricingService pricing;

    public PackageController(PricingService pricing) {
        this.pricing = pricing;
    }

    @PostMapping("/calculate")
    public CostBreakdown calculate(@Valid @RequestBody PackageRequest req) {
        return pricing.calculate(req);
    }
}
