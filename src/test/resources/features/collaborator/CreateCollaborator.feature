Feature: Add Collaborator to a Project
  In order to allow other users to work on my project
  As a project owner
  I want to add collaborators with specific permission levels

  Background:
    Given There is a registered user with username "owner" and password "password123" and email "owner@sample.app"
    Given There is a registered user with username "colleague" and password "password123" and email "colleague@sample.app"
    Given There is a registered user with username "stranger" and password "password123" and email "stranger@sample.app"
    Given I login as "owner" with password "password123"
    And I have created a project named "Shared Project"

  Scenario: Project owner can add a collaborator with View permission
    When I add "colleague" as a collaborator with action "View"
    Then The response code is 201
    And The collaborator has action "View"

  Scenario: Project owner can add a collaborator with Edit permission
    When I add "colleague" as a collaborator with action "Edit"
    Then The response code is 201
    And The collaborator has action "Edit"

  Scenario: Project owner can add a collaborator with Remove permission
    When I add "colleague" as a collaborator with action "Remove"
    Then The response code is 201
    And The collaborator has action "Remove"

  Scenario: A non-owner cannot add a collaborator to someone else's project
    Given I login as "stranger" with password "password123"
    When I add "colleague" as a collaborator with action "View"
    Then The response code is 403

  Scenario: Unauthenticated user cannot add a collaborator
    Given I'm not logged in
    When I add "colleague" as a collaborator with action "View"
    Then The response code is 401
