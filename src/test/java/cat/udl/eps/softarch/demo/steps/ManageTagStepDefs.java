package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Tag;
import cat.udl.eps.softarch.demo.repository.TagRepository;
import io.cucumber.java.en.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ManageTagStepDefs {

    private final StepDefs stepDefs;
    private final TagRepository tagRepository;

    private Long lastTagId;

    private static final String TEST_USER = "user";
    private static final String TEST_PASSWORD = "password";

    public ManageTagStepDefs(StepDefs stepDefs, TagRepository tagRepository) {
        this.stepDefs = stepDefs;
        this.tagRepository = tagRepository;
    }

    // -------------------------
    // BACKGROUND
    // -------------------------

    @Given("the system is initialized")
    public void systemInitialized() {
        // no-op
    }

    @Given("all tags are cleared")
    public void clearTags() {
        tagRepository.deleteAll();
    }

    // -------------------------
    // GIVEN TAG EXISTS
    // -------------------------

    @Given("^a tag exists with name \"([^\"]*)\"$")
    public void aTagExists(String name) {
        Tag tag = new Tag(name);
        tagRepository.save(tag);
        lastTagId = tag.getId();
    }

    // -------------------------
    // CREATE
    // -------------------------

    @When("^I create a tag with name \"([^\"]*)\"$")
    public void createTag(String name) throws Exception {

        Tag tag = new Tag(name);

        stepDefs.result = stepDefs.mockMvc.perform(
            withAuth(post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stepDefs.mapper.writeValueAsString(tag))
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON))
        ).andDo(print());
    }

    // -------------------------
    // READ
    // -------------------------

    @When("I request all tags")
    public void requestAllTags() throws Exception {

        stepDefs.result = stepDefs.mockMvc.perform(
            withAuth(get("/tags").accept(MediaType.APPLICATION_JSON))
        ).andDo(print());
    }

    @When("I request that tag by id")
    public void requestTagById() throws Exception {

        stepDefs.result = stepDefs.mockMvc.perform(
            withAuth(get("/tags/" + lastTagId).accept(MediaType.APPLICATION_JSON))
        ).andDo(print());
    }

    @When("^I request tag with id (\\d+)$")
    public void requestTagByIdNumber(Long id) throws Exception {

        stepDefs.result = stepDefs.mockMvc.perform(
            withAuth(get("/tags/" + id).accept(MediaType.APPLICATION_JSON))
        ).andDo(print());
    }

    // -------------------------
    // DELETE
    // -------------------------

    @When("I delete that tag")
    public void deleteThatTag() throws Exception {

        stepDefs.result = stepDefs.mockMvc.perform(
            withAuth(delete("/tags/" + lastTagId))
        ).andDo(print());
    }

    @When("^I delete tag with id (\\d+)$")
    public void deleteTagById(Long id) throws Exception {

        stepDefs.result = stepDefs.mockMvc.perform(
            withAuth(delete("/tags/" + id))
        ).andDo(print());
    }

    // -------------------------
    // ASSERTIONS
    // -------------------------

    @Then("^the tag name should be \"([^\"]*)\"$")
    public void tagNameShouldBe(String name) throws Exception {

        stepDefs.result.andExpect(
            jsonPath("$.name", is(name))
        );
    }

    @Then("^the response should contain (\\d+) tags$")
    public void responseShouldContainTags(int count) throws Exception {

        stepDefs.result.andExpect(
            jsonPath("$._embedded.tags", hasSize(count))
        );
    }

    @Then("requesting that tag by id should return 404")
    public void requestDeletedTag() throws Exception {

        stepDefs.mockMvc.perform(
            withAuth(get("/tags/" + lastTagId))
        ).andExpect(status().isNotFound());
    }

    // -------------------------
    // HELPER METHOD FOR AUTH
    // -------------------------

    private RequestBuilder withAuth(MockHttpServletRequestBuilder request) {
        String basicAuth = Base64.getEncoder()
            .encodeToString((TEST_USER + ":" + TEST_PASSWORD).getBytes());
        return request.header("Authorization", "Basic " + basicAuth);
    }
}