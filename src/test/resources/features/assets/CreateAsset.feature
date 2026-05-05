Feature: Create Asset
  In order to upload files into a project
  As an authenticated collaborator
  I want to create assets with valid metadata and have invalid attempts rejected

  Background:
    Given There is a registered user with username "collaborator" and password "password123" and email "collaborator@sample.app"

  # ── Success ─────────────────────────────────────────────────────────────────

  Scenario: Successfully create an asset with minimal fields
    Given I login as "collaborator" with password "password123"
    When I create a new asset with name "README" and description "Project readme file"
    Then The response code is 201
    And The asset has name "README"
    And The asset has description "Project readme file"

  Scenario: Successfully create an asset with full metadata
    Given I login as "collaborator" with password "password123"
    When I create a new asset with name "Banner", description "Project banner image" and url "http://example.com/image.png"
    Then The response code is 201
    And The asset has name "Banner"
    And The asset has description "Project banner image"
    And The asset has content type "image/png"


  Scenario: Created asset is attributed to the authenticated user
    Given I login as "collaborator" with password "password123"
    When I create a new asset with name "Attributed Asset" and description "Ownership check"
    Then The response code is 201
    And The asset was created by "collaborator"

  # ── Failure ──────────────────────────────────────────────────────────────────

  Scenario: Cannot create an asset with a blank name
    Given I login as "collaborator" with password "password123"
    When I create a new asset with name "" and description "Some description"
    Then The response code is 400
    And The error message is "must not be blank"

  Scenario: Cannot create an asset without authentication
    Given I'm not logged in
    When I create a new asset with name "Sneaky Asset" and description "Should be rejected"
    Then The response code is 401

  Scenario: Cannot create an asset with full metadata without authentication
    Given I'm not logged in
    When I create a new asset with name "Sneaky Banner", description "Should be rejected" and url "http://example.com/sneaky.png"
    Then The response code is 401

