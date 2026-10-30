package dev.thillainirmal.voyageweaver.agent;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(TravelPlannerGateway.class)
public class DisabledTravelPlanner implements TravelPlannerGateway {
    @Override public String plan(String userId, String prompt) {
        throw new IllegalStateException("Travel AI is disabled. Set VOYAGE_AI_ENABLED=true and GOOGLE_API_KEY.");
    }
    @Override public String modelName() { return "disabled"; }
}
