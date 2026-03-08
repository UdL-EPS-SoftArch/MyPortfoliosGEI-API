Feature: Profile Management

  Background:
    Given the system is running
    And a User exists with username "john_doe" with password "Password123!"
    And a User exists with username "alice_dev" with password "Password123!"

  # ── BASIC CRUD ────────────────────────────────────────────────────────────────

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

  # ── MORE PROFILE FIELDS ───────────────────────────────────────────────────────

  Scenario: Create a profile with social links and location
    Given I login as "john_doe" with password "Password123!"
    When I create a full profile for "john_doe" with:
      | fullName  | Ada Lovelace             |
      | email     | ada@example.com          |
      | location  | London, UK               |
      | github    | adalovelace              |
      | twitter   | ada_lovelace             |
      | linkedin  | ada-lovelace             |
    Then the response status should be 201
    And the response body should contain "London, UK"
    And the response body should contain "adalovelace"
    And the response body should contain "ada_lovelace"

  Scenario: Update profile avatar URL
    Given I login as "john_doe" with password "Password123!"
    And a profile exists for "john_doe"
    When I update the avatarUrl for "john_doe" to "https://example.com/avatar.png"
    Then the response status should be 204
    When I fetch the profile for "john_doe"
    Then the response status should be 200
    And the response body should contain "https://example.com/avatar.png"

  Scenario: Update profile location
    Given I login as "john_doe" with password "Password123!"
    And a profile exists for "john_doe"
    When I update the location for "john_doe" to "Barcelona, Spain"
    Then the response status should be 204
    When I fetch the profile for "john_doe"
    Then the response status should be 200
    And the response body should contain "Barcelona, Spain"

  # ── VALIDATION & EDGE CASES ───────────────────────────────────────────────────

  Scenario: Cannot create a profile without fullName
    Given I login as "john_doe" with password "Password123!"
    When I create a profile with missing fullName for "john_doe" with email "ada@example.com"
    Then the response status should be 400

  Scenario: Cannot create a profile without email
    Given I login as "john_doe" with password "Password123!"
    When I create a profile with missing email for "john_doe" with fullName "Ada Lovelace"
    Then the response status should be 400

  Scenario: Cannot create a profile with an invalid email format
    Given I login as "john_doe" with password "Password123!"
    When I create a profile for "john_doe" with invalid email "not-an-email"
    Then the response status should be 400

  Scenario: Cannot create two profiles with the same email
    Given I login as "john_doe" with password "Password123!"
    And a profile exists for "john_doe"
    Given I login as "alice_dev" with password "Password123!"
    When I create a profile for "Alice Dev" with email "john_doe@test.com" and username "alice_dev"
    Then the response status should be 409

  Scenario: Cannot create a second profile for the same user
    Given I login as "john_doe" with password "Password123!"
    And a profile exists for "john_doe"
    When I create a profile for "Ada Lovelace" with email "ada2@example.com" and username "john_doe"
    Then the response status should be 409

  # ── AUTHORIZATION ─────────────────────────────────────────────────────────────

  Scenario: Unauthenticated user cannot create a profile
    Given I'm not logged in
    When I create a profile for "Ada Lovelace" with email "ada@example.com" and username "john_doe"
    Then the response status should be 401

  Scenario: User cannot delete another user's profile
    Given I login as "john_doe" with password "Password123!"
    And a profile exists for "john_doe"
    Given I login as "alice_dev" with password "Password123!"
    When I delete the profile for "john_doe"
    Then the response status should be 403

  Scenario: User cannot update another user's profile
    Given I login as "john_doe" with password "Password123!"
    And a profile exists for "john_doe"
    Given I login as "alice_dev" with password "Password123!"
    When I update the bio for "john_doe" to "Hacked bio"
    Then the response status should be 403

  # ── SEARCH ────────────────────────────────────────────────────────────────────

  Scenario: Find profile by full name
    Given I login as "john_doe" with password "Password123!"
    And a profile exists for "john_doe"
    When I search for profiles with name "Test User"
    Then the response status should be 200
    And the response body should contain "Test User"

  Scenario: Search returns empty when no match
    Given I login as "john_doe" with password "Password123!"
    And a profile exists for "john_doe"
    When I search for profiles with name "NonExistentPerson"
    Then the response status should be 200
    And the response body should contain "[]"

  Scenario: Fetch a public profile while unauthenticated
    Given I login as "john_doe" with password "Password123!"
    And a profile exists for "john_doe"
    Given I'm not logged in
    When I fetch the profile for "john_doe"
    Then the response status should be 200

  Scenario: Private profile is not visible to other users
    Given I login as "alice_dev" with password "Password123!"
    When I create a private profile for "Secret User" with email "secret@sample.app" and username "alice_dev"
    Given I login as "john_doe" with password "Password123!"
    When I fetch the profile for "alice_dev"
    Then the response status should be 403