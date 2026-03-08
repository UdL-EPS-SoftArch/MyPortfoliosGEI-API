Feature: Profile Management
  The system must allow users to create, update, retrieve, search, and delete profiles.
  Profiles belong to Users and contain personal, social, and metadata information.

  Background:
    Given the system is running
    And a User exists with username "john_doe"
    And a User exists with username "alice_dev"

  # ------------------------------------------------------------
  # PROFILE CREATION
  # ------------------------------------------------------------

  Scenario: Successfully create a complete profile
    When I create a profile for "Ada Lovelace" with email "ada@example.com" and username "john_doe"
    Then the response status should be 201
    And the response body should contain "Ada Lovelace"
    And the response body should contain "ada@example.com"
    And the response body should contain "createdAt"
    And the response body should contain "updatedAt"

  Scenario: Create a profile with only required fields
    When I create a profile for "Alan Turing" with email "alan@example.com" and username "alice_dev"
    Then the response status should be 201
    And the response body should contain "Alan Turing"

  Scenario: Fail to create a profile with missing full name
    When I create a profile with a blank name, email "missing@example.com", and username "john_doe"
    Then the response status should be 400
    And the response body should contain "fullName"

  Scenario: Fail to create a profile with invalid email
    When I create a profile for "Invalid User" with email "not-an-email" and username "john_doe"
    Then the response status should be 400
    And the response body should contain "email"

  Scenario: Fail to create a profile with duplicate email
    Given a profile exists with email "ada@example.com"
    When I create a profile for "Duplicate User" with email "ada@example.com" and username "alice_dev"
    Then the response status should be 409

  Scenario: Fail to create a profile for a non-existing user
    When I create a profile for "Ghost User" with email "ghost@example.com" and username "non_existent_user"
    Then the response status should be 404

  # ------------------------------------------------------------
  # RETRIEVAL & SEARCH
  # ------------------------------------------------------------

  Scenario: Retrieve an existing profile
    Given a profile exists for "john_doe"
    When I request the profile for "john_doe"
    Then the response status should be 200
    And the response body should contain "john_doe"

  Scenario: Search profiles by name
    Given a profile exists for "john_doe" with name "Ada Lovelace"
    When I search for profiles containing "Ada"
    Then the response status should be 200
    And the response body should contain "Ada Lovelace"

  # ------------------------------------------------------------
  # UPDATE
  # ------------------------------------------------------------

  Scenario: Update a profile's biography
    Given a profile exists for "john_doe"
    When I update the bio for "john_doe" to "Mathematician and computing pioneer"
    Then the response status should be 200
    And the response body should contain "Mathematician and computing pioneer"
    And the response body should contain "updatedAt"

  Scenario: Update social links
    Given a profile exists for "john_doe"
    When I update the github handle for "john_doe" to "ada-lovelace-dev"
    Then the response status should be 200
    And the response body should contain "ada-lovelace-dev"

  # ------------------------------------------------------------
  # DELETE
  # ------------------------------------------------------------

  Scenario: Delete an existing profile
    Given a profile exists for "john_doe"
    When I delete the profile for "john_doe"
    Then the response status should be 204

  # ------------------------------------------------------------
  # RELATIONSHIPS & CONSTRAINTS
  # ------------------------------------------------------------

  Scenario: Prevent multiple profiles for the same user
    Given a profile exists for "john_doe"
    When I create a profile for "Second Profile" with email "second@example.com" and username "john_doe"
    Then the response status should be 409

  Scenario: Verify privacy status is respected
    When I create a private profile for "Secret User" with email "secret@example.com" and username "alice_dev"
    Then the response status should be 201
    And the response body should contain "isPrivate"
