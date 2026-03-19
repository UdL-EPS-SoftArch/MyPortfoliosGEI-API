package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Tag;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.TagRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.*;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ManageTagStepDefs {

    private final StepDefs stepDefs;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private Long lastTagId;

    public ManageTagStepDefs(StepDefs stepDefs, TagRepository tagRepository, UserRepository userRepository) {
        this.stepDefs = stepDefs;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Given("the system is initialized")
    public void systemInitialized() {
        if (!userRepository.existsById("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword("password");
            user.setEmail("user@example.com");
            user.encodePassword();
            userRepository.save(user);
        }
    }

    @Given("all tags are cleared")
    public void clearTags() {
        tagRepository.deleteAll();
    }

    @Given("^a tag exists with name \"([^\"]*)\"$")
    public void aTagExists(String name) {
        Tag tag = tagRepository.findByName(name).orElseGet(() -> {
            Tag newTag = new Tag(name);
            return tagRepository.save(newTag);
        });
        lastTagId = tag.getId();
    }

    @When("^I create a tag with name \"([^\"]*)\"$")
    public void createTag(String name) throws Exception {
        Tag tag = new Tag(name);
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(tag))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with(AuthenticationStepDefs.authenticate()) 
        ).andDo(print());
    }

    @When("I request all tags")
    public void requestAllTags() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/tags")
                        .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());
    }

    @When("I request that tag by id")
    public void requestTagById() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/tags/" + lastTagId)
                        .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());
    }

    @When("^I request tag with id (\\d+)$")
    public void requestTagByIdNumber(Long id) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/tags/" + id)
                        .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());
    }

    @When("I delete that tag")
    public void deleteThatTag() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/tags/" + lastTagId)
                        .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());
    }

    @When("^I delete tag with id (\\d+)$")
    public void deleteTagById(Long id) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/tags/" + id)
                        .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());
    }

    @Then("^the tag name should be \"([^\"]*)\"$")
    public void tagNameShouldBe(String name) throws Exception {
        stepDefs.result.andExpect(jsonPath("$.name", is(name)));
    }

    @Then("^the response should contain (\\d+) tags$")
    public void responseShouldContainTags(int count) throws Exception {
        stepDefs.result.andExpect(jsonPath("$._embedded.tags", hasSize(count)));
    }

    @Then("requesting that tag by id should return 404")
    public void requestDeletedTag() throws Exception {
        stepDefs.mockMvc.perform(
                get("/tags/" + lastTagId)
                        .with(AuthenticationStepDefs.authenticate())
        ).andExpect(status().isNotFound());
    }
}
