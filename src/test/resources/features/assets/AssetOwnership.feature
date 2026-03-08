Feature: Asset Ownership and Authorship
  In order to track responsibility for project files
  As a system stakeholder
  I want every asset to record who owns it, who created it, and who last modified it

  Background:
    Given There is a registered user with username "alice" and password "password123" and email "alice@sample.app"
    And There is a registered user with username "bob" and password "password123" and email "bob@sample.app"

  Scenario: Creator is recorded when an asset is created
    Given I login as "alice" with password "password123"
    When I create a new asset with name "Alice's File" and description "Created by alice"
    Then The response code is 201
    And The asset was created by "alice"

  Scenario: Last modifier is recorded after a patch
    Given I login as "alice" with password "password123"
    And An asset with name "Shared File" and description "Initial content" exists
    When I update the asset description to "Updated by alice"
    Then The response code is 200
    And The asset was last modified by "alice"

  Scenario: Last modifier updates when a different collaborator patches
    Given I login as "alice" with password "password123"
    And An asset with name "Collaborative File" and description "Initial content" exists
    And I login as "bob" with password "password123"
    When I update the asset name to "Collaborative File v2"
    Then The response code is 200
    And The asset was last modified by "bob"

