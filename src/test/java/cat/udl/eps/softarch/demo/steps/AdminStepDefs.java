package cat.udl.eps.softarch.demo.steps;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cat.udl.eps.softarch.demo.domain.Admin;
import cat.udl.eps.softarch.demo.repository.AdminRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public class AdminStepDefs {

    private final StepDefs stepDefs;
    private final AdminRepository adminRepository;

    public AdminStepDefs(StepDefs stepDefs, AdminRepository adminRepository) {
        this.stepDefs = stepDefs;
        this.adminRepository = adminRepository;
    }

    @Given("^There is no registered admin with username \"([^\"]*)\"$")
    public void thereIsNoRegisteredAdminWithUsername(String username) {
        assertFalse(adminRepository.existsById(username),
            "Admin \"" + username + "\" shouldn't exist");
    }

    @Given("^There is a registered admin with username \"([^\"]*)\" and password \"([^\"]*)\" and email \"([^\"]*)\"$")
    public void thereIsARegisteredAdminWithUsernameAndPasswordAndEmail(
        String username, String password, String email) {
        if (!adminRepository.existsById(username)) {
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setEmail(email);
            admin.setPassword(password);
            admin.encodePassword();
            adminRepository.save(admin);
        }
    }

    @When("^I register a new admin with username \"([^\"]*)\", email \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void iRegisterANewAdminWithUsernameEmailAndPassword(
        String username, String email, String password) throws Throwable {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setEmail(email);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/admins")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new JSONObject(
                        stepDefs.mapper.writeValueAsString(admin)
                    ).put("password", password).toString())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @And("^It has been created an admin with username \"([^\"]*)\" and email \"([^\"]*)\", the password is not returned$")
    public void itHasBeenCreatedAnAdminWithUsernameAndEmail(String username, String email) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/admins/{username}", username)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.email", is(email)))
            .andExpect(jsonPath("$.password").doesNotExist());
    }

    @And("^It has not been created an admin with username \"([^\"]*)\"$")
    public void itHasNotBeenCreatedAnAdminWithUsername(String username) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/admins/{username}", username)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @And("^The admin with username \"([^\"]*)\" has email \"([^\"]*)\"$")
    public void theAdminWithUsernameHasEmail(String username, String email) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/admins/{username}", username)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.email", is(email)));
    }
}