package online.billmytrip.pkg;

import java.math.BigDecimal;

public enum TransportType {
    BUS(new BigDecimal("2500.00")),
    TRAIN(new BigDecimal("6000.00")),
    FLIGHT(new BigDecimal("35000.00"));

    public final BigDecimal pricePerTraveler;
    TransportType(BigDecimal pricePerTraveler) { this.pricePerTraveler = pricePerTraveler; }
}
