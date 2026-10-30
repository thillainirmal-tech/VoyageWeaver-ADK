package dev.thillainirmal.voyageweaver.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.runner.InMemoryRunner;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
@ConditionalOnProperty(name = "voyage.ai.enabled", havingValue = "true")
public class GoogleAdkTravelPlanner implements TravelPlannerGateway {
    private static final String APP_NAME = "voyageweaver";
    private final LlmAgent coordinator;
    private final String modelName;

    public GoogleAdkTravelPlanner(LlmAgent coordinator, @Value("${voyage.ai.model:gemini-2.5-flash}") String modelName) {
        this.coordinator = coordinator; this.modelName = modelName;
    }

    @Override
    public String plan(String userId, String prompt) {
        var answer = new AtomicReference<String>();
        var runner = new InMemoryRunner(coordinator, APP_NAME);
        try {
            var session = runner.sessionService().createSession(APP_NAME, userId).blockingGet();
            runner.runAsync(session.userId(), session.id(), Content.fromParts(Part.fromText(prompt)))
                    .blockingForEach(event -> {
                        if (event.finalResponse() && event.functionCalls().isEmpty() && event.functionResponses().isEmpty()) {
                            var text = event.stringifyContent().strip();
                            if (!text.isBlank()) answer.set(text);
                        }
                    });
        } finally {
            runner.close().blockingAwait();
        }
        if (answer.get() == null) throw new IllegalStateException("The planning agent completed without a final response");
        return answer.get();
    }

    @Override public String modelName() { return modelName; }
}
