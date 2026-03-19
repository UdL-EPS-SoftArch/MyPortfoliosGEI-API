package cat.udl.eps.softarch.demo.steps;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class PortfolioStepDefs {

    private final StepDefs stepDefs;

    // Variable per a guardar la URL del portfolio que acabem de crear i poder borrar-ho després
    private String lastCreatedPortfolioUrl;

    public PortfolioStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @When("I create a new portfolio with name {string}")
    public void iCreateANewPortfolioWithName(String name) throws Throwable {
        String portfolioJson = """
            {
            "name": "%s",
            "description": "Test description",
            "isPrivate": false,
            "creator": "/users/%s"
            }
            """.formatted(name, AuthenticationStepDefs.currentUsername);

        stepDefs.result = stepDefs.mockMvc.perform(
            post("/portfolios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(portfolioJson)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());

        if(stepDefs.result.andReturn().getResponse().getStatus() == 201) {
            lastCreatedPortfolioUrl = stepDefs.result.andReturn().getResponse().getHeader("Location");
        }
    }

    @And("The list of portfolios owned by {string} includes one named {string}")
    public void theListOfPortfoliosOwnedByIncludesOneNamed(String username, String portfolioName) throws Throwable {
        stepDefs.mockMvc.perform(
                get("/portfolios/search/findByCreator")
                    .param("user", "/users/" + username)
                    .with(AuthenticationStepDefs.authenticate())
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.portfolios[*].name", hasItem(portfolioName)));
    }

    @And("The new portfolio is owned by {string}")
    public void theNewPortfolioIsOwnedBy(String username) throws Throwable {
        String newPortfolioUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
        stepDefs.result = stepDefs.mockMvc.perform(
                get(newPortfolioUri + "/creator")
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.username", is(username)));
    }

    @And("There is no exisiting portfolio with name {string}")
    public void thereIsNoExisitingPortfolioWithName(String portfolioName) throws Exception {
        stepDefs.mockMvc.perform(
                get("/portfolios/search/findByNameContaining")
                    .param("name", portfolioName)
                    .with(AuthenticationStepDefs.authenticate())
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.portfolios[*].name", not(hasItem(portfolioName))));
    }

    @When("I try to create a portfolio with an empty name")
    public void iTryToCreateAPortfolioWithAnEmptyName() throws Exception {
        String portfolioJson = """
            {
              "name": "",
              "description": "This should fail due to @NotBlank",
              "isPrivate": false
            }
            """;

        stepDefs.result = stepDefs.mockMvc.perform(
            post("/portfolios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(portfolioJson)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());
    }

    @Then("The system should reject the portfolio creation")
    public void theSystemShouldRejectThePortfolioCreation() throws Exception {
        stepDefs.result.andExpect(status().isBadRequest());
    }

    @When("I try to create a portfolio with a description too long")
    public void iTryToCreateAPortfolioWithADescriptionTooLong() throws Exception {
        String longDescription = "A".repeat(2001);

        String portfolioJson = """
            {
              "name": "Portfolio Largo",
              "description": "%s",
              "isPrivate": false
            }
            """.formatted(longDescription);

        stepDefs.result = stepDefs.mockMvc.perform(
            post("/portfolios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(portfolioJson)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());
    }

    @When("I create a new PRIVATE portfolio with name {string}")
    public void iCreateANewPrivatePortfolioWithName(String name) throws Exception {
        String portfolioJson = """
            {
              "name": "%s",
              "description": "Top Secret",
              "isPrivate": true
            }
            """.formatted(name);

        stepDefs.result = stepDefs.mockMvc.perform(
            post("/portfolios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(portfolioJson)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());
    }

    @Then("The portfolio {string} should not be visible in the public list")
    public void thePortfolioShouldNotBeVisibleInThePublicList(String portfolioName) throws Exception {
        stepDefs.mockMvc.perform(
                get("/portfolios/search/findByIsPrivate")
                    .param("isPrivate", "false")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.portfolios[*].name", not(hasItem(portfolioName))));
    }

    @When("I create a new portfolio with name {string} assigned to {string}")
    public void iCreateANewPortfolioWithNameAssignedTo(String name, String targetUsername) throws Exception {
        String portfolioJson = """
            {
              "name": "%s",
              "description": "Admin created this",
              "isPrivate": false,
              "creator": "/users/%s"
            }
            """.formatted(name, targetUsername);

        stepDefs.result = stepDefs.mockMvc.perform(
            post("/portfolios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(portfolioJson)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());

        if(stepDefs.result.andReturn().getResponse().getStatus() == 201) {
            lastCreatedPortfolioUrl = stepDefs.result.andReturn().getResponse().getHeader("Location");
        }
    }

    @When("I try to delete the recently created portfolio")
    public void iTryToDeleteTheRecentlyCreatedPortfolio() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
            delete(lastCreatedPortfolioUrl)
                .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());
    }

    @Then("The system should reject the action with a Forbidden error")
    public void theSystemShouldRejectTheActionWithAForbiddenError() throws Exception {
        stepDefs.result.andExpect(status().isForbidden());
    }

    @Then("The portfolio is successfully deleted")
    public void thePortfolioIsSuccessfullyDeleted() throws Exception {
        stepDefs.result.andExpect(status().isNoContent());
    }
}