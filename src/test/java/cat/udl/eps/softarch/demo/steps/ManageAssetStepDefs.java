package cat.udl.eps.softarch.demo.steps;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cat.udl.eps.softarch.demo.domain.Asset;
import cat.udl.eps.softarch.demo.repository.AssetRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.UUID;

public class ManageAssetStepDefs {
    private final StepDefs stepDefs;
    private final AssetRepository assetRepository;

    private Asset currentAsset;
    private String currentAssetId;

    public ManageAssetStepDefs(StepDefs stepDefs, AssetRepository assetRepository) {
        this.stepDefs = stepDefs;
        this.assetRepository = assetRepository;
    }

    @When("^I upload a new asset with name \"([^\"]*)\", description \"([^\"]*)\" content type \"([^\"]*)\" and size (\\d+)$")
    public void iUploadANewAsset(String name, String description, String contentType, Long size) throws Throwable {
        String assetId = UUID.randomUUID().toString();
        this.currentAssetId = assetId;

        JSONObject assetJson = new JSONObject();
        assetJson.put("id", assetId);
        assetJson.put("name", name);
        assetJson.put("description", description);
        assetJson.put("contentType", contentType);
        assetJson.put("size", size);
        assetJson.put("storageKey", "storage/" + assetId);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(assetJson.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("^I try to upload an asset with name \"([^\"]*)\", description \"([^\"]*)\", content type \"([^\"]*)\" and size (\\d+)$")
    public void iTryToUploadAnAsset(String name, String description, String contentType, Long size) throws Throwable {
        iUploadANewAsset(name, description, contentType, size);
    }

    @And("^An asset with name \"([^\"]*)\" has been created$")
    public void anAssetWithNameHasBeenCreated(String name) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.name", is(name)));
    }

    @And("^The asset has description \"([^\"]*)\"$")
    public void theAssetHasDescription(String description) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.description", is(description)));
    }

    @And("^The asset has name \"([^\"]*)\"$")
    public void theAssetHasName(String name) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.name", is(name)));
    }

    @And("^The asset has content type \"([^\"]*)\"$")
    public void theAssetHasContentType(String contentType) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.contentType", is(contentType)));
    }

    @And("^The asset has size (\\d+)$")
    public void theAssetHasSize(Long size) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.size", is(size.intValue())));
    }

    @Given("^An asset with name \"([^\"]*)\" and description \"([^\"]*)\" exists$")
    public void anAssetWithNameAndDescriptionExists(String name, String description) throws Throwable {
        currentAssetId = UUID.randomUUID().toString();
        Asset asset = new Asset(
                currentAssetId,
                name,
                description,
                "image/png",
                1024L,
                "storage/" + currentAssetId
        );
        asset.setCreatedAt(ZonedDateTime.now());
        asset.setUpdatedAt(ZonedDateTime.now());
        assetRepository.save(asset);
        this.currentAsset = asset;
    }

    @When("^I edit the asset with new name \"([^\"]*)\" and new description \"([^\"]*)\"$")
    public void iEditTheAssetWithNewNameAndDescription(String newName, String newDescription) throws Throwable {
        JSONObject updateJson = new JSONObject();
        updateJson.put("name", newName);
        updateJson.put("description", newDescription);

        stepDefs.result = stepDefs.mockMvc.perform(
                patch("/assets/{id}", currentAssetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("^I edit the asset with new name \"([^\"]*)\" only$")
    public void iEditTheAssetWithNewNameOnly(String newName) throws Throwable {
        JSONObject updateJson = new JSONObject();
        updateJson.put("name", newName);

        stepDefs.result = stepDefs.mockMvc.perform(
                patch("/assets/{id}", currentAssetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("^I edit the asset with new description \"([^\"]*)\" only$")
    public void iEditTheAssetWithNewDescriptionOnly(String newDescription) throws Throwable {
        JSONObject updateJson = new JSONObject();
        updateJson.put("description", newDescription);

        stepDefs.result = stepDefs.mockMvc.perform(
                patch("/assets/{id}", currentAssetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("^I try to edit the asset with new name \"([^\"]*)\"$")
    public void iTryToEditTheAssetWithNewName(String newName) throws Throwable {
        iEditTheAssetWithNewNameOnly(newName);
    }

    @When("^I delete the asset$")
    public void iDeleteTheAsset() throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/assets/{id}", currentAssetId)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("^I try to delete the asset$")
    public void iTryToDeleteTheAsset() throws Throwable {
        iDeleteTheAsset();
    }

    @And("^No asset exists with name \"([^\"]*)\"$")
    public void noAssetExistsWithName(String name) throws Throwable {
        boolean exists = assetRepository.findByName(name).isPresent();
        assertFalse(exists, "Asset with name \"" + name + "\" should not exist");
    }
}

