package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Project;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.domain.Visibility;
import cat.udl.eps.softarch.demo.repository.ProjectRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class CollaboratorStepDefs {

    private final StepDefs stepDefs;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private String currentProjectUri;
    private String lastCollaboratorUrl;

    public CollaboratorStepDefs(StepDefs stepDefs,
                                ProjectRepository projectRepository,
                                UserRepository userRepository) {
        this.stepDefs = stepDefs;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @And("I have created a project named {string}")
    public void iHaveCreatedAProjectNamed(String name) throws Throwable {
        User owner = userRepository.findById(AuthenticationStepDefs.currentUsername)
                .orElseThrow();
        Project project = new Project(name, "Test project", Visibility.PUBLIC);
        project.setCreator(owner);
        project = projectRepository.save(project);
        currentProjectUri = "/projects/" + project.getId();
    }

    @And("{string} is a collaborator with action {string} on the project")
    public void isACollaboratorWithActionOnTheProject(String username, String action) throws Throwable {
        String body = """
                {
                  "user": "/users/%s",
                  "project": "%s",
                  "action": "%s"
                }
                """.formatted(username, currentProjectUri, action);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/collaborators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());

        lastCollaboratorUrl = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }

    @When("I add {string} as a collaborator with action {string}")
    public void iAddAsACollaboratorWithAction(String username, String action) throws Throwable {
        String body = """
                {
                  "user": "/users/%s",
                  "project": "%s",
                  "action": "%s"
                }
                """.formatted(username, currentProjectUri, action);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/collaborators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());

        lastCollaboratorUrl = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }

    @And("The collaborator has action {string}")
    public void theCollaboratorHasAction(String action) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.action", is(action)));
    }

    @When("I remove the collaborator")
    public void iRemoveTheCollaborator() throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                delete(lastCollaboratorUrl)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }
}
