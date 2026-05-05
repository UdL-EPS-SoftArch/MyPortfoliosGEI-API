Feature: Profile Management

  Background:
    Given the system is running
    And a User exists with username "john_doe" with password "Password123!"
    And a User exists with username "alice_dev" with password "Password123!"

  Scenario: Successfully create a complete profile
    Given I login as "john_doe" with password "Password123!"
    When I create a profile for "Ada Lovelace" with email "ada@example.com" and username "john_doe"
    Then the response status should be 201
    And the response body should contain "Ada Lovelace"

  Scenario: Create a private profile
    Given I login as "alice_dev" with password "Password123!"
    When I create a private profile for "Secret User" with email "secret@sample.app" and username "alice_dev"
    Then the response status should be 201
    And the "isPrivate" field should be true

  Scenario: Update a profile biography
    Given I login as "john_doe" with password "Password123!"
    And a profile exists for "john_doe"
    When I update the bio for "john_doe" to "Pioneer of computing"
    Then the response status should be 204
    When I fetch the profile for "john_doe"
    Then the response status should be 200
    And the response body should contain "Pioneer of computing"

  Scenario: Delete an existing profile
    Given I login as "john_doe" with password "Password123!"
    And a profile exists for "john_doe"
    When I delete the profile for "john_doe"
    Then the response status should be 204