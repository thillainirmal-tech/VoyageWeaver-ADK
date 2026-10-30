package dev.thillainirmal.voyageweaver.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:voyage-security;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa", "spring.datasource.password=",
        "voyage.security.api-key=integration-test-key-32-characters", "voyage.ai.enabled=false"
})
@AutoConfigureMockMvc
class SecurityIntegrationTest {
    @Autowired MockMvc mvc;

    @Test void landingIsPublicAndTripDataIsProtected() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk());
        String path = "/api/trips/00000000-0000-0000-0000-000000000000";
        mvc.perform(get(path)).andExpect(status().isForbidden());
        mvc.perform(get(path).header("X-API-Key", "integration-test-key-32-characters"))
                .andExpect(status().isNotFound());
    }
}
