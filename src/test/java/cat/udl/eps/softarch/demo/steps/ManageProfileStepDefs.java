package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Profile;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.ProfileRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        // No-op
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

    // ── BASIC CREATE ────────────────────────────────────────────────────────────

    @When("^I create a profile for \"([^\"]*)\" with email \"([^\"]*)\" and username \"([^\"]*)\"$")
    public void iCreateAProfile(String fullName, String email, String username) throws Throwable {
        User user = userRepository.findById(username).orElseThrow();
        Profile profile = new Profile();
        profile.setFullName(fullName);
        profile.setEmail(email);
        profile.setIsPrivate(false);
        profile.setUser(user);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(profile))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("^I create a private profile for \"([^\"]*)\" with email \"([^\"]*)\" and username \"([^\"]*)\"$")
    public void iCreateAPrivateProfile(String fullName, String email, String username) throws Throwable {
        User user = userRepository.findById(username).orElseThrow();
        Profile profile = new Profile();
        profile.setFullName(fullName);
        profile.setEmail(email);
        profile.setIsPrivate(true);
        profile.setUser(user);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(profile))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @Given("^a profile exists for \"([^\"]*)\"$")
    public void aProfileExistsFor(String username) throws Throwable {
        iCreateAProfile("Test User", username + "@test.com", username);
    }

    // ── FULL PROFILE WITH DATATABLE ─────────────────────────────────────────────

    @When("^I create a full profile for \"([^\"]*)\" with:$")
    public void iCreateAFullProfile(String username, DataTable dataTable) throws Throwable {
        Map<String, String> fields = dataTable.asMap(String.class, String.class);
        User user = userRepository.findById(username).orElseThrow();

        Profile profile = new Profile();
        profile.setFullName(fields.getOrDefault("fullName", ""));
        profile.setEmail(fields.getOrDefault("email", ""));
        profile.setLocation(fields.getOrDefault("location", null));
        profile.setGithub(fields.getOrDefault("github", null));
        profile.setTwitter(fields.getOrDefault("twitter", null));
        profile.setLinkedin(fields.getOrDefault("linkedin", null));
        profile.setIsPrivate(false);
        profile.setUser(user);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(profile))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    // ── UPDATES ─────────────────────────────────────────────────────────────────

    @When("^I update the bio for \"([^\"]*)\" to \"([^\"]*)\"$")
    public void iUpdateBio(String username, String bio) throws Throwable {
        String uri = stepDefs.result.andReturn().getResponse().getHeader("Location");
        Profile patch = new Profile();
        patch.setBio(bio);

        stepDefs.result = stepDefs.mockMvc.perform(
                patch(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(patch))
                        .with(AuthenticationStepDefs.authenticate()));
    }

    @When("^I update the avatarUrl for \"([^\"]*)\" to \"([^\"]*)\"$")
    public void iUpdateAvatarUrl(String username, String avatarUrl) throws Throwable {
        String uri = stepDefs.result.andReturn().getResponse().getHeader("Location");
        Profile patch = new Profile();
        patch.setAvatarUrl(avatarUrl);

        stepDefs.result = stepDefs.mockMvc.perform(
                patch(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(patch))
                        .with(AuthenticationStepDefs.authenticate()));
    }

    @When("^I update the location for \"([^\"]*)\" to \"([^\"]*)\"$")
    public void iUpdateLocation(String username, String location) throws Throwable {
        String uri = stepDefs.result.andReturn().getResponse().getHeader("Location");
        Profile patch = new Profile();
        patch.setLocation(location);

        stepDefs.result = stepDefs.mockMvc.perform(
                patch(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(patch))
                        .with(AuthenticationStepDefs.authenticate()));
    }

    // ── DELETE ──────────────────────────────────────────────────────────────────

    @When("^I delete the profile for \"([^\"]*)\"$")
    public void iDeleteProfile(String username) throws Throwable {
        String uri = stepDefs.result.andReturn().getResponse().getHeader("Location");
        stepDefs.result = stepDefs.mockMvc.perform(
                delete(uri)
                        .with(AuthenticationStepDefs.authenticate()));
    }

    // ── FETCH ───────────────────────────────────────────────────────────────────

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

    // ── SEARCH ──────────────────────────────────────────────────────────────────

    @When("^I search for profiles with name \"([^\"]*)\"$")
    public void iSearchForProfilesByName(String name) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/profiles/search/findByFullNameContaining")
                        .param("name", name)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    // ── VALIDATION ──────────────────────────────────────────────────────────────

    @When("^I create a profile with missing fullName for \"([^\"]*)\" with email \"([^\"]*)\"$")
    public void iCreateProfileMissingFullName(String username, String email) throws Throwable {
        User user = userRepository.findById(username).orElseThrow();
        Profile profile = new Profile();
        profile.setEmail(email);
        profile.setIsPrivate(false);
        profile.setUser(user);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(profile))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("^I create a profile with missing email for \"([^\"]*)\" with fullName \"([^\"]*)\"$")
    public void iCreateProfileMissingEmail(String username, String fullName) throws Throwable {
        User user = userRepository.findById(username).orElseThrow();
        Profile profile = new Profile();
        profile.setFullName(fullName);
        profile.setIsPrivate(false);
        profile.setUser(user);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(profile))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("^I create a profile for \"([^\"]*)\" with invalid email \"([^\"]*)\"$")
    public void iCreateProfileInvalidEmail(String username, String invalidEmail) throws Throwable {
        User user = userRepository.findById(username).orElseThrow();
        Profile profile = new Profile();
        profile.setFullName("Test User");
        profile.setEmail(invalidEmail);
        profile.setIsPrivate(false);
        profile.setUser(user);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(profile))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    // ── ASSERTIONS ──────────────────────────────────────────────────────────────

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