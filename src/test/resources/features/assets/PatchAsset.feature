Feature: Patch Asset
  In order to keep asset metadata accurate
  As an authenticated collaborator
  I want to update an asset's name and description and have invalid or unauthorized attempts rejected

  Background:
    Given There is a registered user with username "collaborator" and password "password123" and email "collaborator@sample.app"

  # ── Success ─────────────────────────────────────────────────────────────────

  Scenario: Successfully update the asset name only
    Given I login as "collaborator" with password "password123"
    And An asset with name "Old Name" and description "Stable description" exists
    When I update the asset name to "New Name"
    Then The response code is 200
    And The asset has name "New Name"
    And The asset has description "Stable description"

  Scenario: Successfully update the asset description only
    Given I login as "collaborator" with password "password123"
    And An asset with name "Stable Name" and description "Old description" exists
    When I update the asset description to "Brand new description"
    Then The response code is 200
    And The asset has name "Stable Name"
    And The asset has description "Brand new description"

  Scenario: Successfully update both name and description at once
    Given I login as "collaborator" with password "password123"
    And An asset with name "Original Name" and description "Original description" exists
    When I update the asset name to "Updated Name" and description to "Updated description"
    Then The response code is 200
    And The asset has name "Updated Name"
    And The asset has description "Updated description"

  Scenario: Patch records the last modifier
    Given I login as "collaborator" with password "password123"
    And An asset with name "Tracked Asset" and description "Before edit" exists
    When I update the asset name to "Tracked Asset Edited"
    Then The response code is 200
    And The asset was last modified by "collaborator"

  # ── Failure ──────────────────────────────────────────────────────────────────

  Scenario: Cannot patch an asset name to blank
    Given I login as "collaborator" with password "password123"
    And An asset with name "Valid Asset" and description "Some description" exists
    When I update the asset name to ""
    Then The response code is 400
    And The error message is "must not be blank"

  Scenario: Cannot patch an asset without authentication
    Given I login as "collaborator" with password "password123"
    And An asset with name "Protected Asset" and description "Owned asset" exists
    And I'm not logged in
    When I update the asset name to "Hacked Name"
    Then The response code is 401

  Scenario: Cannot patch an asset description without authentication
    Given I login as "collaborator" with password "password123"
    And An asset with name "Protected Asset" and description "Owned description" exists
    And I'm not logged in
    When I update the asset description to "Hacked description"
    Then The response code is 401

