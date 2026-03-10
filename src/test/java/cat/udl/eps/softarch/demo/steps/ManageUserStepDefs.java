package cat.udl.eps.softarch.demo.steps;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public class ManageUserStepDefs {

    private final StepDefs stepDefs;

    public ManageUserStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    // When — retrieve (GET /users/{username})

    @When("^I retrieve the user with username \"([^\"]*)\"$")
    public void iRetrieveTheUserWithUsername(String username) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/users/{username}", username)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    // When — list (GET /users)

    @When("^I list all users$")
    public void iListAllUsers() throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    // When — patch email (PATCH /users/{username})

    @When("^I update the user \"([^\"]*)\" email to \"([^\"]*)\"$")
    public void iUpdateTheUserEmailTo(String username, String newEmail) throws Throwable {
        ObjectNode patch = stepDefs.mapper.createObjectNode();
        patch.put("email", newEmail);
        performPatch(username, patch);
    }

    // When — patch password (PATCH /users/{username})

    @When("^I update the user \"([^\"]*)\" password to \"([^\"]*)\"$")
    public void iUpdateTheUserPasswordTo(String username, String newPassword) throws Throwable {
        JSONObject patch = new JSONObject();
        patch.put("password", newPassword);
        patch.put("passwordReset", true);
        stepDefs.result = stepDefs.mockMvc.perform(
                patch("/users/{username}", username)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(patch.toString())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    // When — delete (DELETE /users/{username})

    @When("^I delete the user with username \"([^\"]*)\"$")
    public void iDeleteTheUserWithUsername(String username) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/users/{username}", username)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    // And — response body assertions

    @And("^The user has username \"([^\"]*)\"$")
    public void theUserHasUsername(String username) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.id", is(username)));
    }

    @And("^The user has email \"([^\"]*)\"$")
    public void theUserHasEmail(String email) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.email", is(email)));
    }

    @And("^The user password is not returned$")
    public void theUserPasswordIsNotReturned() throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.password").doesNotExist());
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void performPatch(String username, ObjectNode patch) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                patch("/users/{username}", username)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(patch))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }
}




