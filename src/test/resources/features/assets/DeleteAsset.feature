    Feature: Delete Asset
  In order to remove files from a project
  As an authenticated collaborator
  I want to delete assets and have unauthorized attempts rejected

  Background:
    Given There is a registered user with username "collaborator" and password "password123" and email "collaborator@sample.app"

  # ── Success ─────────────────────────────────────────────────────────────────

  Scenario: Successfully delete an existing asset
    Given I login as "collaborator" with password "password123"
    And An asset with name "Disposable Asset" and description "Will be removed" exists
    When I delete the asset
    Then The response code is 204
    And No asset exists with name "Disposable Asset"

  # ── Failure ──────────────────────────────────────────────────────────────────

  Scenario: Cannot delete an asset without authentication
    Given I login as "collaborator" with password "password123"
    And An asset with name "Protected Asset" and description "Will not be deleted" exists
    And I'm not logged in
    When I delete the asset
    Then The response code is 401

