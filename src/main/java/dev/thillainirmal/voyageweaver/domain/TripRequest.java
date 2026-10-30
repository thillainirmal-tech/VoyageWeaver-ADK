package dev.thillainirmal.voyageweaver.domain;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record TripRequest(
        @NotBlank @Size(max = 120) String origin,
        @NotBlank @Size(max = 120) String destination,
        @NotNull @FutureOrPresent LocalDate startDate,
        @Min(1) @Max(45) int days,
        @NotNull @Min(1) BigDecimal budget,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @Min(1) @Max(12) int travellers,
        @Size(max = 12) List<@Size(max = 80) String> interests,
        @Size(max = 500) String accessibilityNeeds,
        @Min(0) @Max(100) int sustainabilityPriority) {

    public String prompt() {
        return """
                Build a practical trip plan using every relevant specialist.
                Origin: %s
                Destination: %s
                Start date: %s
                Duration: %d days
                Total budget: %s %s
                Travellers: %d
                Interests: %s
                Accessibility needs: %s
                Sustainability priority (0-100): %d

                Return Markdown with: assumptions, transport, day-by-day itinerary, lodging,
                itemized budget with total, safety/visa caveats, carbon-conscious alternatives,
                accessibility notes, and a booking checklist. Never claim live price or availability;
                label estimates and identify facts that the traveller must verify.
                """.formatted(origin, destination, startDate, days, budget, currency.toUpperCase(),
                travellers, interests == null ? List.of() : interests,
                accessibilityNeeds == null ? "none supplied" : accessibilityNeeds,
                sustainabilityPriority);
    }
}
