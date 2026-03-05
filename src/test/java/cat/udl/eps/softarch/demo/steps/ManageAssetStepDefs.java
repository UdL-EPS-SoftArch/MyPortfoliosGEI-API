package cat.udl.eps.softarch.demo.steps;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import cat.udl.eps.softarch.demo.domain.Asset;
import cat.udl.eps.softarch.demo.repository.AssetRepository;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ManageAssetStepDefs {

    private final StepDefs stepDefs;
    private final AssetRepository assetRepository;

    private String currentAssetId;

    public ManageAssetStepDefs(StepDefs stepDefs, AssetRepository assetRepository) {
        this.stepDefs = stepDefs;
        this.assetRepository = assetRepository;
    }

    // Given — seed an asset directly into the DB

    @Given("^An asset with name \"([^\"]*)\" and description \"([^\"]*)\" exists$")
    public void anAssetWithNameAndDescriptionExists(String name, String description) {
        currentAssetId = UUID.randomUUID().toString();
        Asset asset = new Asset(currentAssetId, name, description,
                "application/octet-stream", 1024L, "storage/" + currentAssetId);
        assetRepository.save(asset);
    }

    // When — create (POST /assets)

    @When("^I create a new asset with name \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void iCreateANewAssetWithNameAndDescription(String name, String description) throws Throwable {
        currentAssetId = UUID.randomUUID().toString();
        Asset asset = new Asset(currentAssetId, name, description,
                "application/octet-stream", 1024L, "storage/" + currentAssetId);
        performPost(asset);
    }

    @When("^I create a new asset with name \"([^\"]*)\", description \"([^\"]*)\", content type \"([^\"]*)\" and size (\\d+)$")
    public void iCreateANewAssetWithAllFields(String name, String description,
                                              String contentType, Long size) throws Throwable {
        currentAssetId = UUID.randomUUID().toString();
        Asset asset = new Asset(currentAssetId, name, description,
                contentType, size, "storage/" + currentAssetId);
        performPost(asset);
    }

    // When — update (PATCH /assets/{id})

    @When("^I update the asset name to \"([^\"]*)\"$")
    public void iUpdateTheAssetNameTo(String newName) throws Throwable {
        ObjectNode patch = stepDefs.mapper.createObjectNode();
        patch.put("name", newName);
        performPatch(patch);
    }

    @When("^I update the asset description to \"([^\"]*)\"$")
    public void iUpdateTheAssetDescriptionTo(String newDescription) throws Throwable {
        ObjectNode patch = stepDefs.mapper.createObjectNode();
        patch.put("description", newDescription);
        performPatch(patch);
    }

    @When("^I update the asset name to \"([^\"]*)\" and description to \"([^\"]*)\"$")
    public void iUpdateTheAssetNameAndDescription(String newName, String newDescription) throws Throwable {
        ObjectNode patch = stepDefs.mapper.createObjectNode();
        patch.put("name", newName);
        patch.put("description", newDescription);
        performPatch(patch);
    }

    // When — delete (DELETE /assets/{id})

    @When("^I delete the asset$")
    public void iDeleteTheAsset() throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/assets/{id}", currentAssetId)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    // And — response body assertions

    @And("^The asset has name \"([^\"]*)\"$")
    public void theAssetHasName(String name) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.name", is(name)));
    }

    @And("^The asset has description \"([^\"]*)\"$")
    public void theAssetHasDescription(String description) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.description", is(description)));
    }

    @And("^The asset has content type \"([^\"]*)\"$")
    public void theAssetHasContentType(String contentType) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.contentType", is(contentType)));
    }

    @And("^The asset has size (\\d+)$")
    public void theAssetHasSize(Long size) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.size").value(size));
    }

    // And — ownership assertions

    @And("^The asset is owned by \"([^\"]*)\"$")
    public void theAssetIsOwnedBy(String username) throws Throwable {
        String uri = resolvedAssetUri() + "/owner";
        stepDefs.result = stepDefs.mockMvc.perform(
                get(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.id", is(username)));
    }

    @And("^The asset was created by \"([^\"]*)\"$")
    public void theAssetWasCreatedBy(String username) throws Throwable {
        String uri = resolvedAssetUri() + "/createdBy";
        stepDefs.result = stepDefs.mockMvc.perform(
                get(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.id", is(username)));
    }

    @And("^The asset was last modified by \"([^\"]*)\"$")
    public void theAssetWasLastModifiedBy(String username) throws Throwable {
        String uri = resolvedAssetUri() + "/lastModifiedBy";
        stepDefs.result = stepDefs.mockMvc.perform(
                get(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.id", is(username)));
    }

    // And — DB-level existence assertions

    @And("^No asset exists with name \"([^\"]*)\"$")
    public void noAssetExistsWithName(String name) {
        assertFalse(assetRepository.findByName(name).isPresent(),
                "Expected no asset named \"" + name + "\" but one was found.");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void performPost(Asset asset) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/assets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(asset))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    private void performPatch(ObjectNode patch) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                patch("/assets/{id}", currentAssetId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(patch))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    private String resolvedAssetUri() {
        String location = stepDefs.result.andReturn().getResponse().getHeader("Location");
        return location != null ? location : "/assets/" + currentAssetId;
    }
}
