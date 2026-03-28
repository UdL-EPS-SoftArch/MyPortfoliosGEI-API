package cat.udl.eps.softarch.demo.steps;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import cat.udl.eps.softarch.demo.domain.Project;
import cat.udl.eps.softarch.demo.domain.Status;
import cat.udl.eps.softarch.demo.repository.ProjectRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public class ManageProjectStepDefs {

    private final StepDefs stepDefs;
    private final ProjectRepository projectRepository;
    private Long currentProjectId;

    public ManageProjectStepDefs(StepDefs stepDefs, ProjectRepository projectRepository) {
        this.stepDefs = stepDefs;
        this.projectRepository = projectRepository;
    }

    @When("^I create a new project with name \"([^\"]*)\", description \"([^\"]*)\" and status \"([^\"]*)\"$")
    public void iCreateANewProject(String name, String description, String statusStr) throws Throwable {
        Project project = new Project(name, description, Boolean.FALSE);

        if (statusStr != null && !statusStr.isEmpty()) {
            project.setStatus(Status.valueOf(statusStr));
        }
        
        project.setFlagged(false);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/projects")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(project))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
            
        // extract ID if possible
        String location = stepDefs.result.andReturn().getResponse().getHeader("Location");
        if (location != null) {
            String[] parts = location.split("/");
            try {
                currentProjectId = Long.parseLong(parts[parts.length - 1]);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
    }

    @And("^The project has name \"([^\"]*)\"$")
    public void theProjectHasName(String name) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.name", is(name)));
    }

    @And("^The project has description \"([^\"]*)\"$")
    public void theProjectHasDescription(String description) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.description", is(description)));
    }

    @And("^The project has status \"([^\"]*)\"$")
    public void theProjectHasStatus(String status) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.status", is(status)));
    }

    @And("^The project has isPrivate (true|false)$")
    public void theProjectHasIsPrivate(String isPrivateStr) throws Throwable {
        boolean isPrivate = Boolean.parseBoolean(isPrivateStr);
        stepDefs.result.andExpect(jsonPath("$.isPrivate", is(isPrivate)));
    }

    @And("^The project was created by \"([^\"]*)\"$")
    public void theProjectWasCreatedBy(String username) throws Throwable {
        String uri = resolvedProjectUri() + "/creator";
        stepDefs.result = stepDefs.mockMvc.perform(
                get(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.username", is(username)));
    }

    private String resolvedProjectUri() {
        String location = stepDefs.result.andReturn().getResponse().getHeader("Location");
        return location != null ? location : "/projects/" + currentProjectId;
    }
}
