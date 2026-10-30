package dev.thillainirmal.voyageweaver.web;

import dev.thillainirmal.voyageweaver.domain.TripPlan;
import dev.thillainirmal.voyageweaver.domain.TripRequest;
import dev.thillainirmal.voyageweaver.service.TravelPlanningService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/trips")
public class TravelPlanController {
    private final TravelPlanningService service;
    public TravelPlanController(TravelPlanningService service) { this.service = service; }

    @PostMapping("/plan")
    @ResponseStatus(HttpStatus.CREATED)
    public PlanResponse plan(@Valid @RequestBody TripRequest request, Principal principal) {
        return PlanResponse.from(service.create(principal.getName(), request));
    }

    @GetMapping("/{id}")
    public PlanResponse get(@PathVariable UUID id) { return PlanResponse.from(service.get(id)); }

    public record PlanResponse(UUID id, String origin, String destination, String planMarkdown, String model, java.time.Instant createdAt) {
        static PlanResponse from(TripPlan plan) {
            return new PlanResponse(plan.getId(), plan.getOrigin(), plan.getDestination(), plan.getPlanMarkdown(), plan.getModel(), plan.getCreatedAt());
        }
    }
}
