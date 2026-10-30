package dev.thillainirmal.voyageweaver.agent;

public interface TravelPlannerGateway {
    String plan(String userId, String prompt);
    String modelName();
}
