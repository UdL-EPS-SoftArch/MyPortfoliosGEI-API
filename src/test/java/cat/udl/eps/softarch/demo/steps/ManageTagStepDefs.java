package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Tag;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.TagRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ManageTagStepDefs {

    private final StepDefs stepDefs;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private Long lastTagId;
    private String lastCreatedTagName; // Guardamos el nombre por si el JSON viene vacío

    public ManageTagStepDefs(StepDefs stepDefs, TagRepository tagRepository, UserRepository userRepository) {
        this.stepDefs = stepDefs;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    private MockHttpServletRequestBuilder withAuth(MockHttpServletRequestBuilder builder) {
        String auth = "user:password";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        return builder.header("Authorization", "Basic " + encodedAuth);
    }

    @Given("the system is initialized")
    public void systemInitialized() {
        if (!userRepository.existsById("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword("password");
            user.setEmail("test@test.com");
            try { user.encodePassword(); } catch (Exception ignored) {}
            userRepository.save(user);
        }
    }

    @Given("all tags are cleared")
    public void clearTags() {
        tagRepository.deleteAll();
    }

    @Given("^a tag exists with name \"([^\"]*)\"$")
    public void aTagExists(String name) {
        Tag tag = tagRepository.findByName(name).orElseGet(() -> tagRepository.save(new Tag(name)));
        lastTagId = tag.getId();
    }

    @When("^I create a tag with name \"([^\"]*)\"$")
    public void createTag(String name) throws Exception {
        lastCreatedTagName = name;
        Tag tag = new Tag(name);
        stepDefs.result = stepDefs.mockMvc.perform(
                withAuth(post("/tags"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(tag))
                        .characterEncoding(StandardCharsets.UTF_8)
        ).andDo(print());
    }

    @When("I request all tags")
    public void requestAllTags() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                withAuth(get("/tags")).accept(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    @When("I request that tag by id")
    public void requestTagById() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                withAuth(get("/tags/" + lastTagId)).accept(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    @When("^I request tag with id (\\d+)$")
    public void requestTagByIdNumber(Long id) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                withAuth(get("/tags/" + id)).accept(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

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

    @Then("^the tag name should be \"([^\"]*)\"$")
    public void tagNameShouldBe(String expectedName) throws Exception {
        MvcResult mvcResult = stepDefs.result.andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        // Si el cuerpo no está vacío, validamos el JSON
        if (content != null && !content.isEmpty()) {
            stepDefs.result.andExpect(jsonPath("$.name", is(expectedName)));
        } else {
            // Si el cuerpo está vacío (común en 201), validamos que se haya guardado en la BD
            Tag tag = tagRepository.findByName(expectedName)
                    .orElseThrow(() -> new AssertionError("Tag not found in database: " + expectedName));
            assertEquals(expectedName, tag.getName());
        }
    }

    @Then("^the response should contain (\\d+) tags$")
    public void responseShouldContainTags(int count) throws Exception {
        stepDefs.result.andExpect(jsonPath("$._embedded.tags", hasSize(count)));
    }

    @Then("requesting that tag by id should return 404")
    public void requestDeletedTag() throws Exception {
        stepDefs.mockMvc.perform(
                withAuth(get("/tags/" + lastTagId))
        ).andExpect(status().isNotFound());
    }
}
