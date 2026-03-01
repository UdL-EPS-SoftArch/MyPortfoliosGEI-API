package cat.udl.eps.softarch.demo.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasItem;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.not;

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
            "visibility": "PUBLIC"
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
            get("/portfolios/search/findByCreator")
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
        stepDefs.mockMvc.perform(
                get("/portfolios")
                    .with(AuthenticationStepDefs.authenticate()) // Afegim per si l'API requiereix autenticació per llegir
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            // Verificarem que l'array de nombres retornats NO contingui el nombre indicat
            .andExpect(jsonPath("$._embedded.portfolios[*].name", not(hasItem(portfolioName))));
    }

    @When("I try to create a portfolio with an empty name")
    public void iTryToCreateAPortfolioWithAnEmptyName() throws Exception {
        // Crearem un JSON amb el nom buit per forçar l'error de validació
        String portfolioJson = """
            {
              "name": "",
              "description": "This should fail due to @NotBlank",
              "visibility": "PUBLIC"
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
        // Verificarem que Spring retorna error 400 Bad Request per fallar el @NotBlank
        stepDefs.result.andExpect(status().isBadRequest());
    }

    @When("I try to create a portfolio with a description too long")
    public void iTryToCreateAPortfolioWithADescriptionTooLong() throws Exception {
        // Generarem un text de 2001 caracters (una "A" repetida 2001 vegades)
        String longDescription = "A".repeat(2001);

        String portfolioJson = """
            {
              "name": "Portfolio Largo",
              "description": "%s",
              "visibility": "PUBLIC"
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
              "visibility": "PRIVATE"
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
        // Fem una petició GET a la llista general, simulant un usuari anonim o sense permisos especials
        stepDefs.mockMvc.perform(
                get("/portfolios")
                    .accept(MediaType.APPLICATION_JSON)
                //  Simular que algú que no és el creador intenta veure-ho.
            )
            .andDo(print())
            .andExpect(status().isOk())
            // Verifiquem que el portfoli secret NO està dins la resposta
            .andExpect(jsonPath("$._embedded.portfolios[*].name", not(hasItem(portfolioName))));
    }
}
