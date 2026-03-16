package cat.udl.eps.softarch.demo.steps;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cat.udl.eps.softarch.demo.domain.Creator;
import cat.udl.eps.softarch.demo.repository.CreatorRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public class CreatorStepDefs {

    private final StepDefs stepDefs;
    private final CreatorRepository creatorRepository;

    public CreatorStepDefs(StepDefs stepDefs, CreatorRepository creatorRepository) {
        this.stepDefs = stepDefs;
        this.creatorRepository = creatorRepository;
    }

    @Given("^There is no registered creator with username \"([^\"]*)\"$")
    public void thereIsNoRegisteredCreatorWithUsername(String username) {
        assertFalse(creatorRepository.existsById(username),
            "Creator \"" + username + "\" shouldn't exist");
    }

    @Given("^There is a registered creator with username \"([^\"]*)\" and password \"([^\"]*)\" and email \"([^\"]*)\"$")
    public void thereIsARegisteredCreatorWithUsernameAndPasswordAndEmail(
        String username, String password, String email) {
        if (!creatorRepository.existsById(username)) {
            Creator creator = new Creator();
            creator.setUsername(username);
            creator.setEmail(email);
            creator.setPassword(password);
            creator.encodePassword();
            creatorRepository.save(creator);
        }
    }

    @When("^I register a new creator with username \"([^\"]*)\", email \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void iRegisterANewCreatorWithUsernameEmailAndPassword(
        String username, String email, String password) throws Throwable {
        Creator creator = new Creator();
        creator.setUsername(username);
        creator.setEmail(email);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/creators")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new JSONObject(
                        stepDefs.mapper.writeValueAsString(creator)
                    ).put("password", password).toString())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @And("^It has been created a creator with username \"([^\"]*)\" and email \"([^\"]*)\", the password is not returned$")
    public void itHasBeenCreatedACreatorWithUsernameAndEmail(String username, String email) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/creators/{username}", username)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.email", is(email)))
            .andExpect(jsonPath("$.password").doesNotExist());
    }

    @And("^It has not been created a creator with username \"([^\"]*)\"$")
    public void itHasNotBeenCreatedACreatorWithUsername(String username) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/creators/{username}", username)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @And("^The creator with username \"([^\"]*)\" has email \"([^\"]*)\"$")
    public void theCreatorWithUsernameHasEmail(String username, String email) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/creators/{username}", username)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.email", is(email)));
    }
}