package dev.thillainirmal.voyageweaver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "trip_plans")
public class TripPlan {
    @Id private UUID id;
    @Column(nullable = false, length = 120) private String origin;
    @Column(nullable = false, length = 120) private String destination;
    @Column(name = "request_json", nullable = false, columnDefinition = "text") private String requestJson;
    @Column(name = "plan_markdown", nullable = false, columnDefinition = "text") private String planMarkdown;
    @Column(nullable = false, length = 80) private String model;
    @Column(name = "created_at", nullable = false) private Instant createdAt;

    protected TripPlan() {}

    public TripPlan(UUID id, String origin, String destination, String requestJson, String planMarkdown, String model, Instant createdAt) {
        this.id = id; this.origin = origin; this.destination = destination; this.requestJson = requestJson;
        this.planMarkdown = planMarkdown; this.model = model; this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getRequestJson() { return requestJson; }
    public String getPlanMarkdown() { return planMarkdown; }
    public String getModel() { return model; }
    public Instant getCreatedAt() { return createdAt; }
}
