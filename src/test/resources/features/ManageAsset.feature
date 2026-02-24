Feature: Manage Asset
  In order to upload and manage content for my projects
  As an authenticated user
  I want to upload, edit and delete assets within a project

  Scenario: Upload a new asset as authenticated user
    Given I login as "demo" with password "password"
    When I upload a new asset with name "Project Screenshot", description "Screenshot of the project", content type "image/png" and size 2048
    Then The response code is 201
    And An asset with name "Project Screenshot" has been created
    And The asset has description "Screenshot of the project"

  Scenario: Edit asset metadata as asset owner
    Given I login as "demo" with password "password"
    And An asset with name "Original Asset" and description "Original description" exists
    When I edit the asset with new name "Updated Asset" and new description "Updated description"
    Then The response code is 200
    And The asset has name "Updated Asset"
    And The asset has description "Updated description"

  Scenario: Delete asset as asset owner
    Given I login as "demo" with password "password"
    And An asset with name "Asset to Delete" and description "This will be deleted" exists
    When I delete the asset
    Then The response code is 204
    And No asset exists with name "Asset to Delete"

  Scenario: Cannot upload asset without authentication
    Given I'm not logged in
    When I try to upload an asset with name "Unauthorized Asset", description "Should fail", content type "image/png" and size 1024
    Then The response code is 401

  Scenario: Cannot edit asset without authentication
    Given An asset with name "Asset for Edit" and description "Original" exists
    And I'm not logged in
    When I try to edit the asset with new name "Hacked Name"
    Then The response code is 401

  Scenario: Cannot delete asset without authentication
    Given An asset with name "Asset for Delete" and description "Original" exists
    And I'm not logged in
    When I try to delete the asset
    Then The response code is 401

  Scenario: Upload asset with empty name
    Given I login as "demo" with password "password"
    When I upload a new asset with name "", description "Some description", content type "image/png" and size 1024
    Then The response code is 400
    And The error message is "must not be blank"

  Scenario: Upload asset with valid metadata
    Given I login as "demo" with password "password"
    When I upload a new asset with name "Valid Asset", description "Valid description", content type "application/pdf" and size 5120
    Then The response code is 201
    And An asset with name "Valid Asset" has been created
    And The asset has content type "application/pdf"
    And The asset has size 5120

  Scenario: Update only asset name
    Given I login as "demo" with password "password"
    And An asset with name "Original Name" and description "Keep this" exists
    When I edit the asset with new name "New Name" only
    Then The response code is 200
    And The asset has name "New Name"
    And The asset has description "Keep this"

  Scenario: Update only asset description
    Given I login as "demo" with password "password"
    And An asset with name "Keep this name" and description "Original description" exists
    When I edit the asset with new description "New description" only
    Then The response code is 200
    And The asset has name "Keep this name"
    And The asset has description "New description"

