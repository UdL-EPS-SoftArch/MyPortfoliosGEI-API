package cat.udl.eps.softarch.demo.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VisibilityStepDefs {

    private final StepDefs stepDefs;

    public VisibilityStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    // --- Portfolio visibility ---

    @When("I create a portfolio named {string} with visibility {string}")
    public void iCreateAPortfolioNamedWithVisibility(String name, String visibility) throws Throwable {
        String body = """
                {
                  "name": "%s",
                  "description": "Visibility test portfolio",
                  "visibility": "%s"
                }
                """.formatted(name, visibility);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/portfolios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @And("The portfolio {string} is listed under visibility {string}")
    public void thePortfolioIsListedUnderVisibility(String name, String visibility) throws Throwable {
        stepDefs.mockMvc.perform(
                get("/portfolios/search/findByVisibility")
                        .param("visibility", visibility)
                        .with(AuthenticationStepDefs.authenticate())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.portfolios[*].name", hasItem(name)));
    }

    @And("The portfolio {string} is not listed under visibility {string}")
    public void thePortfolioIsNotListedUnderVisibility(String name, String visibility) throws Throwable {
        stepDefs.mockMvc.perform(
                get("/portfolios/search/findByVisibility")
                        .param("visibility", visibility)
                        .with(AuthenticationStepDefs.authenticate())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.portfolios[*].name", not(hasItem(name))));
    }

    // --- Project visibility ---

    @When("I create a project named {string} with visibility {string}")
    public void iCreateAProjectNamedWithVisibility(String name, String visibility) throws Throwable {
        String body = """
                {
                  "name": "%s",
                  "description": "Visibility test project",
                  "visibility": "%s"
                }
                """.formatted(name, visibility);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @And("The project {string} is listed under visibility {string}")
    public void theProjectIsListedUnderVisibility(String name, String visibility) throws Throwable {
        stepDefs.mockMvc.perform(
                get("/projects/search/findByVisibility")
                        .param("visibility", visibility)
                        .with(AuthenticationStepDefs.authenticate())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.projects[*].name", hasItem(name)));
    }

    @And("The project {string} is not listed under visibility {string}")
    public void theProjectIsNotListedUnderVisibility(String name, String visibility) throws Throwable {
        stepDefs.mockMvc.perform(
                get("/projects/search/findByVisibility")
                        .param("visibility", visibility)
                        .with(AuthenticationStepDefs.authenticate())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.projects[*].name", not(hasItem(name))));
    }
}
