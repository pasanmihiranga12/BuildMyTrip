package online.billmytrip.pkg;

import online.billmytrip.activity.Activity;
import online.billmytrip.activity.ActivityRepository;
import online.billmytrip.destination.Destination;
import online.billmytrip.destination.DestinationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PricingService {

    private final DestinationRepository destinations;
    private final ActivityRepository activities;
    private final PromoRepository promos;

    public PricingService(DestinationRepository destinations, ActivityRepository activities, PromoRepository promos) {
        this.destinations = destinations;
        this.activities = activities;
        this.promos = promos;
    }

    public CostBreakdown calculate(PackageRequest req) {
        List<Destination> chosen = destinations.findAllById(req.destinationIds());
        List<Activity> acts = req.activityIds() == null || req.activityIds().isEmpty()
                ? Collections.emptyList() : activities.findAllById(req.activityIds());

        BigDecimal travelers = BigDecimal.valueOf(req.travelers());
        BigDecimal days = BigDecimal.valueOf(req.days());

        BigDecimal destSumPerPersonPerDay = chosen.stream()
                .map(Destination::getBasePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal destinationsCost = destSumPerPersonPerDay.multiply(travelers).multiply(days);

        BigDecimal hotelCost = destinationsCost.multiply(req.hotelType().multiplier.subtract(BigDecimal.ONE));

        BigDecimal transportCost = req.transportType().pricePerTraveler.multiply(travelers);

        BigDecimal activitiesPerPerson = acts.stream()
                .map(Activity::getPricePerPerson)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal activitiesCost = activitiesPerPerson.multiply(travelers);

        BigDecimal subtotal = destinationsCost.add(hotelCost).add(transportCost).add(activitiesCost);

        int discountPercent = 0;
        String promoApplied = null;
        if (req.promoCode() != null && !req.promoCode().isBlank()) {
            Optional<PromoCode> p = promos.findByCodeIgnoreCase(req.promoCode().trim());
            if (p.isPresent() && p.get().isActive()) {
                discountPercent = p.get().getDiscountPercent();
                promoApplied = p.get().getCode();
            }
        }
        BigDecimal discountAmount = subtotal
                .multiply(BigDecimal.valueOf(discountPercent))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

        List<String> destNames = new ArrayList<>();
        for (Destination d : chosen) destNames.add(d.getName());
        List<String> actNames = new ArrayList<>();
        for (Activity a : acts) actNames.add(a.getName());

        return new CostBreakdown(
                destinationsCost.setScale(2, RoundingMode.HALF_UP),
                hotelCost.setScale(2, RoundingMode.HALF_UP),
                transportCost.setScale(2, RoundingMode.HALF_UP),
                activitiesCost.setScale(2, RoundingMode.HALF_UP),
                subtotal.setScale(2, RoundingMode.HALF_UP),
                discountPercent,
                discountAmount,
                total,
                destNames,
                actNames,
                promoApplied);
    }
}
