package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.repository.UserRepository;
import cat.udl.eps.softarch.demo.repository.ProfileRepository;
import cat.udl.eps.softarch.demo.domain.User;
import io.cucumber.java.en.*;
import org.springframework.http.MediaType;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

public class ManageProfileStepDefs {
    private final StepDefs stepDefs;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public ManageProfileStepDefs(StepDefs stepDefs, UserRepository userRepository, ProfileRepository profileRepository) {
        this.stepDefs = stepDefs;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Given("the system is running")
    public void theSystemIsRunning() {
        // No-op: Spring context is already started by StepDefs
    }

    @Given("^a User exists with username \"([^\"]*)\" with password \"([^\"]*)\"$")
    public void aUserExistsWithUsernameAndPassword(String username, String password) {
        if (!userRepository.existsById(username)) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(username + "@example.com");
            user.setPassword(password);
            user.encodePassword();
            userRepository.save(user);
        }
    }

    @When("^I create a profile for \"([^\"]*)\" with email \"([^\"]*)\" and username \"([^\"]*)\"$")
    public void iCreateAProfile(String name, String email, String username) throws Throwable {
        String profileJson = """
            {
              "fullName": "%s",
              "email": "%s",
              "isPrivate": false,
              "user": "/users/%s"
            }
            """.formatted(name, email, username);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(profileJson)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("^I create a private profile for \"([^\"]*)\" with email \"([^\"]*)\" and username \"([^\"]*)\"$")
    public void iCreateAPrivateProfile(String name, String email, String username) throws Throwable {
        String profileJson = """
            {
              "fullName": "%s",
              "email": "%s",
              "isPrivate": true,
              "user": "/users/%s"
            }
            """.formatted(name, email, username);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(profileJson)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @Given("^a profile exists for \"([^\"]*)\"$")
    public void aProfileExistsFor(String username) throws Throwable {
        iCreateAProfile("Test User", username + "@test.com", username);
    }

    @When("^I update the bio for \"([^\"]*)\" to \"([^\"]*)\"$")
    public void iUpdateBio(String username, String bio) throws Throwable {
        String uri = stepDefs.result.andReturn().getResponse().getHeader("Location");
        String updateJson = "{\"bio\": \"" + bio + "\"}";

        stepDefs.result = stepDefs.mockMvc.perform(
                patch(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson)
                        .with(AuthenticationStepDefs.authenticate()));
    }

    @When("^I fetch the profile for \"([^\"]*)\"$")
    public void iFetchProfile(String username) throws Throwable {
        User user = userRepository.findById(username).orElseThrow();
        Long profileId = profileRepository.findByUser(user).getId();

        stepDefs.result = stepDefs.mockMvc.perform(
                get("/profiles/" + profileId)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("^I delete the profile for \"([^\"]*)\"$")
    public void iDeleteProfile(String username) throws Throwable {
        String uri = stepDefs.result.andReturn().getResponse().getHeader("Location");
        stepDefs.result = stepDefs.mockMvc.perform(
                delete(uri)
                        .with(AuthenticationStepDefs.authenticate()));
    }

    @Then("^the response status should be (\\d+)$")
    public void verifyStatus(int status) throws Throwable {
        stepDefs.result.andExpect(status().is(status));
    }

    @Then("^the response body should contain \"([^\"]*)\"$")
    public void theResponseBodyShouldContain(String text) throws Throwable {
        stepDefs.result.andExpect(content().string(containsString(text)));
    }

    @Then("^the \"([^\"]*)\" field should be true$")
    public void theFieldShouldBeTrue(String fieldName) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$." + fieldName, is(true)));
    }
}