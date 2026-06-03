Feature: Remove a Collaborator from a Project
  In order to revoke access to my project
  As a project owner or collaborator
  I want to remove a collaborator record

  Background:
    Given There is a registered user with username "owner" and password "password123" and email "owner@sample.app"
    Given There is a registered user with username "colleague" and password "password123" and email "colleague@sample.app"
    Given There is a registered user with username "stranger" and password "password123" and email "stranger@sample.app"
    Given I login as "owner" with password "password123"
    And I have created a project named "Shared Project"
    And "colleague" is a collaborator with action "View" on the project

  Scenario: Project owner can remove a collaborator
    When I remove the collaborator
    Then The response code is 204

  Scenario: The collaborator can remove themselves
    Given I login as "colleague" with password "password123"
    When I remove the collaborator
    Then The response code is 204

  Scenario: A stranger cannot remove a collaborator
    Given I login as "stranger" with password "password123"
    When I remove the collaborator
    Then The response code is 403
