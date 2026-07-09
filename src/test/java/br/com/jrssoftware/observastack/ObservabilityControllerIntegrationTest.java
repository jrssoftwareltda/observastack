package br.com.jrssoftware.observastack;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ObservabilityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeCustomHealthEndpoint() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.service", is("ObservaStack")))
                .andExpect(jsonPath("$.dependencies", notNullValue()));
    }

    @Test
    void shouldCompareBeforeAndAfterOptimization() throws Exception {
        mockMvc.perform(get("/api/v1/observability/optimization/compare"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.before.mode", is("before")))
                .andExpect(jsonPath("$.after.mode", is("after")))
                .andExpect(jsonPath("$.before.resultCount", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.after.resultCount", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.businessMeaning", notNullValue()));
    }

    @Test
    void shouldSimulateTrafficAndExposeReport() throws Exception {
        mockMvc.perform(post("/api/v1/observability/traffic")
                        .param("requests", "5")
                        .param("optimized", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestedOperations", is(5)))
                .andExpect(jsonPath("$.optimizedPath", is(true)));

        mockMvc.perform(get("/api/v1/observability/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service", is("ObservaStack")))
                .andExpect(jsonPath("$.prometheusPath", is("/actuator/prometheus")));
    }

    @Test
    void shouldSimulateLatencyAlert() throws Exception {
        mockMvc.perform(post("/api/v1/observability/alerts/simulate")
                        .param("type", "latency"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("TRIGGERED")))
                .andExpect(jsonPath("$.severity", is("MEDIUM")))
                .andExpect(jsonPath("$.recommendedAction", notNullValue()));
    }
}
