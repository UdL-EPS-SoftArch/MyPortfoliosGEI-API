package cat.udl.eps.softarch.demo.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasItem;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class PortfolioStepDefs {

    private final StepDefs stepDefs;

    public PortfolioStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }
    @When("I create a new portfolio with name {string}")
    public void iCreateANewPortfolioWithName(String name) throws Exception {
        String portfolioJson = """
            {
            "name": "%s",
            "description": "Test description",
            "visibility": "PUBLIC",
            }
            """.formatted(name);
        //Enviar JSON per evitar posar Portfolio public
        // i que es puguin crear objectes amb estats invalids

        stepDefs.result = stepDefs.mockMvc.perform(
            post("/portfolios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(portfolioJson)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());
    }

    @And("The list of portfolios owned by {string} includes one named {string}")
    public void theListOfPortfoliosOwnedByIncludesOneNamed(String username, String portfolioName) throws Exception {
        stepDefs.mockMvc.perform(
            get("/portfolios/serch/findByCreator")
                .param("user", username)
                .with(AuthenticationStepDefs.authenticate())
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.portfolios[*].name", hasItem(portfolioName)));
    }

    @And("The new portfolio is owned by {string}")
    public void theNewPortfolioIsOwnedBy(String username) throws Exception {
        stepDefs.result
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.creator").value(username));

    }

    @And("There is no exisiting portfolio with name {string}")
    public void thereIsNoExisitingPortfolioWithName(String portfolioName) throws Exception {
        //Needs Implementation
    }
}
