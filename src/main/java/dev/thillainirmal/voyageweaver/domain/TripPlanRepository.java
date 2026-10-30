package dev.thillainirmal.voyageweaver.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TripPlanRepository extends JpaRepository<TripPlan, UUID> {}
