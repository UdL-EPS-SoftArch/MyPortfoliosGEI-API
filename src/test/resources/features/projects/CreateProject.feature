Feature: Create Project
  In order to manage my work
  As an authenticated user
  I want to create projects with valid metadata and have invalid attempts rejected

  Background:
    Given There is a registered user with username "collaborator" and password "password123" and email "collaborator@sample.app"

  Scenario: Successfully create a project with minimal fields
    Given I login as "collaborator" with password "password123"
    When I create a new project with name "MyNewProject", description "A sample project" and status "ToDo"
    Then The response code is 201
    And The project has name "MyNewProject"
    And The project has description "A sample project"
    And The project has status "ToDo"
    And The project has isPrivate false

  Scenario: Created project is attributed to the authenticated user
    Given I login as "collaborator" with password "password123"
    When I create a new project with name "Attributed Project", description "Ownership check" and status "In_Progress"
    Then The response code is 201
    And The project was created by "collaborator"

  Scenario: Cannot create a project with a blank name
    Given I login as "collaborator" with password "password123"
    When I create a new project with name "", description "Some description" and status "ToDo"
    Then The response code is 400
    And The error message is "must not be blank"

  Scenario: Cannot create a project without authentication
    Given I'm not logged in
    When I create a new project with name "Sneaky Project", description "Should be rejected" and status "ToDo"
    Then The response code is 401
