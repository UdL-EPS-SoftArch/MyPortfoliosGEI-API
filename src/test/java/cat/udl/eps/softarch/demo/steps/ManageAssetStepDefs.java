package cat.udl.eps.softarch.demo.steps;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import cat.udl.eps.softarch.demo.domain.Asset;
import cat.udl.eps.softarch.demo.repository.AssetRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
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

    @Given("An asset with name {string} and description {string} exists")
    public void anAssetWithNameAndDescriptionExists(String name, String description) {
        currentAssetId = UUID.randomUUID().toString();
        Asset asset = new Asset(
            currentAssetId,
            name,
            description,
            "image/png",
            1024L,
            "storage/" + currentAssetId
        );
        assetRepository.save(asset);
    }


    @When("I upload a new asset with name {string}, description {string}, content type {string} and size {long}")
    public void iUploadANewAsset(String name, String description,
                                 String contentType, Long size) throws Throwable {
        currentAssetId = UUID.randomUUID().toString();

        JSONObject body = new JSONObject();
        body.put("id", currentAssetId);
        body.put("name", name);
        body.put("description", description);
        body.put("contentType", contentType);
        body.put("size", size);
        body.put("storageKey", "storage/" + currentAssetId);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/assets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body.toString())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I try to upload an asset with name {string}, description {string}, content type {string} and size {long}")
    public void iTryToUploadAnAsset(String name, String description,
                                    String contentType, Long size) throws Throwable {
        iUploadANewAsset(name, description, contentType, size);
    }


    @When("I edit the asset with new name {string} and new description {string}")
    public void iEditTheAssetWithNewNameAndDescription(String newName,
                                                       String newDescription) throws Throwable {
        JSONObject body = new JSONObject();
        body.put("name", newName);
        body.put("description", newDescription);
        performPatch(body);
    }

    @When("I edit the asset with new name {string} only")
    public void iEditTheAssetWithNewNameOnly(String newName) throws Throwable {
        JSONObject body = new JSONObject();
        body.put("name", newName);
        performPatch(body);
    }

    @When("I edit the asset with new description {string} only")
    public void iEditTheAssetWithNewDescriptionOnly(String newDescription) throws Throwable {
        JSONObject body = new JSONObject();
        body.put("description", newDescription);
        performPatch(body);
    }

    @When("I try to edit the asset with new name {string}")
    public void iTryToEditTheAssetWithNewName(String newName) throws Throwable {
        iEditTheAssetWithNewNameOnly(newName);
    }


    @When("I delete the asset")
    public void iDeleteTheAsset() throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/assets/{id}", currentAssetId)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I try to delete the asset")
    public void iTryToDeleteTheAsset() throws Throwable {
        iDeleteTheAsset();
    }



    @And("An asset with name {string} has been created")
    public void anAssetWithNameHasBeenCreated(String name) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.name", is(name)));
    }

    @And("The asset has description {string}")
    public void theAssetHasDescription(String description) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.description", is(description)));
    }

    @And("The asset has name {string}")
    public void theAssetHasName(String name) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.name", is(name)));
    }

    @And("The asset has content type {string}")
    public void theAssetHasContentType(String contentType) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.contentType", is(contentType)));
    }

    @And("The asset has size {long}")
    public void theAssetHasSize(Long size) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.size").value(size));
    }

    @And("No asset exists with name {string}")
    public void noAssetExistsWithName(String name) {
        boolean exists = assetRepository.findByName(name).isPresent();
        assertFalse(exists, "Expected no asset named \"" + name + "\" but one was found.");
    }


    private void performPatch(JSONObject body) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                patch("/assets/{id}", currentAssetId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body.toString())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }
}
