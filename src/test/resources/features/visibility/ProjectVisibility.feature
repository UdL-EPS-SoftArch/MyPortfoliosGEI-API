Feature: Project Visibility Filtering
  In order to control who can see my projects
  As a user
  I want to filter projects by visibility level

  Background:
    Given There is a registered user with username "creator" and password "password123" and email "creator@sample.app"
    Given I login as "creator" with password "password123"

  Scenario: A PUBLIC project appears in the public list
    When I create a project named "Open Source App" with visibility "PUBLIC"
    Then The response code is 201
    And The project "Open Source App" is listed under visibility "PUBLIC"

  Scenario: A PRIVATE project does not appear in the PUBLIC list
    When I create a project named "Internal Tool" with visibility "PRIVATE"
    Then The response code is 201
    And The project "Internal Tool" is not listed under visibility "PUBLIC"

  Scenario: A PRIVATE project appears in the PRIVATE list
    When I create a project named "Internal Tool" with visibility "PRIVATE"
    Then The response code is 201
    And The project "Internal Tool" is listed under visibility "PRIVATE"
