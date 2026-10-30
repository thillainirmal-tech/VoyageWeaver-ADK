package dev.thillainirmal.voyageweaver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.thillainirmal.voyageweaver.agent.TravelPlannerGateway;
import dev.thillainirmal.voyageweaver.domain.TripPlanRepository;
import dev.thillainirmal.voyageweaver.domain.TripRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TravelPlanningServiceTest {
    @Test
    void persistsAgentPlanWithModelProvenance() {
        var gateway = new TravelPlannerGateway() {
            public String plan(String userId, String prompt) { return "# Verified plan\nEstimates clearly labelled."; }
            public String modelName() { return "gemini-test"; }
        };
        var repository = mock(TripPlanRepository.class);
        when(repository.save(any())).thenAnswer(call -> call.getArgument(0));
        var clock = Clock.fixed(Instant.parse("2026-10-30T04:00:00Z"), ZoneOffset.UTC);
        var service = new TravelPlanningService(gateway, repository, new ObjectMapper().registerModule(new JavaTimeModule()), clock);
        var request = new TripRequest("Chennai", "Kyoto", LocalDate.of(2026, 12, 1), 7,
                new BigDecimal("180000"), "INR", 2, List.of("food", "temples"), "step-free routes", 85);

        var result = service.create("test-user", request);

        assertThat(result.getPlanMarkdown()).contains("Verified plan");
        assertThat(result.getModel()).isEqualTo("gemini-test");
        assertThat(result.getCreatedAt()).isEqualTo(clock.instant());
        assertThat(result.getRequestJson()).contains("Kyoto", "sustainabilityPriority");
    }
}
