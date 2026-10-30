package dev.thillainirmal.voyageweaver.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.models.Gemini;
import com.google.adk.tools.AgentTool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "voyage.ai.enabled", havingValue = "true")
public class AdkTravelConfiguration {
    @Bean
    Gemini travelModel(@Value("${voyage.ai.model:gemini-2.5-flash}") String model,
                       @Value("${voyage.ai.api-key}") String apiKey) {
        return Gemini.builder().modelName(model).apiKey(apiKey).build();
    }

    @Bean
    LlmAgent travelCoordinator(Gemini model) {
        var mobility = specialist("mobility_specialist", "Find sensible transport options, transfer risks, realistic time buffers, and lower-carbon alternatives. Prices are estimates only.", model);
        var stay = specialist("stay_specialist", "Recommend suitable neighbourhoods and lodging types for budget, accessibility, safety, and transit. Do not invent availability.", model);
        var experience = specialist("experience_curator", "Create a paced itinerary around local culture, food, nature and stated interests. Avoid exploitative or unsafe activities.", model);
        var risk = specialist("risk_and_entry_specialist", "Identify likely visa, health, weather, insurance and safety checks. State that official sources must be verified.", model);
        var optimizer = specialist("budget_carbon_optimizer", "Audit the complete trip for budget arithmetic, hidden costs, carbon impact, over-scheduling and practical trade-offs.", model);

        return LlmAgent.builder()
                .name("voyageweaver_coordinator")
                .description("Coordinates an evidence-conscious, accessible and sustainable travel plan")
                .instruction("""
                        You are the lead trip architect. Consult all five specialist tools before answering.
                        Reconcile conflicts, keep the requested total budget, show arithmetic, and clearly
                        distinguish estimates from verified facts. Never claim live inventory, confirmed visas,
                        guaranteed safety, or completed bookings. Produce the exact structured Markdown requested.
                        """)
                .model(model)
                .tools(AgentTool.create(mobility), AgentTool.create(stay), AgentTool.create(experience),
                        AgentTool.create(risk), AgentTool.create(optimizer))
                .build();
    }

    private LlmAgent specialist(String name, String instruction, Gemini model) {
        return LlmAgent.builder().name(name).description(instruction).instruction(instruction).model(model).build();
    }
}
