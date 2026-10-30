package dev.thillainirmal.voyageweaver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thillainirmal.voyageweaver.agent.TravelPlannerGateway;
import dev.thillainirmal.voyageweaver.domain.TripPlan;
import dev.thillainirmal.voyageweaver.domain.TripPlanRepository;
import dev.thillainirmal.voyageweaver.domain.TripRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class TravelPlanningService {
    private final TravelPlannerGateway planner;
    private final TripPlanRepository repository;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    public TravelPlanningService(TravelPlannerGateway planner, TripPlanRepository repository, ObjectMapper objectMapper) {
        this(planner, repository, objectMapper, Clock.systemUTC());
    }

    TravelPlanningService(TravelPlannerGateway planner, TripPlanRepository repository, ObjectMapper objectMapper, Clock clock) {
        this.planner = planner; this.repository = repository; this.objectMapper = objectMapper; this.clock = clock;
    }

    @Transactional
    public TripPlan create(String userId, TripRequest request) {
        String markdown = planner.plan(userId, request.prompt());
        return repository.save(new TripPlan(UUID.randomUUID(), request.origin(), request.destination(),
                toJson(request), markdown, planner.modelName(), Instant.now(clock)));
    }

    @Transactional(readOnly = true)
    public TripPlan get(UUID id) {
        return repository.findById(id).orElseThrow(() -> new PlanNotFoundException(id));
    }

    private String toJson(TripRequest request) {
        try { return objectMapper.writeValueAsString(request); }
        catch (JsonProcessingException e) { throw new IllegalArgumentException("Trip request cannot be serialized", e); }
    }

    public static class PlanNotFoundException extends RuntimeException {
        public PlanNotFoundException(UUID id) { super("Trip plan not found: " + id); }
    }
}
