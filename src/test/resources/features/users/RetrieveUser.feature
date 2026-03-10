Feature: Retrieve User
  In order to view user profiles
  As a public or authenticated visitor
  I want to retrieve user information with appropriate access control

  Background:
    Given There is a registered user with username "alice" and password "password123" and email "alice@sample.app"

  # ── Public Access ───────────────────────────────────────────────────────────

  Scenario: Authenticated user can retrieve an existing user profile
    Given I login as "alice" with password "password123"
    When I retrieve the user with username "alice"
    Then The response code is 200
    And The user has username "alice"
    And The user has email "alice@sample.app"
    And The user password is not returned

  Scenario: Authenticated user can retrieve another user's profile
    Given There is a registered user with username "bob" and password "password123" and email "bob@sample.app"
    And I login as "alice" with password "password123"
    When I retrieve the user with username "bob"
    Then The response code is 200
    And The user has username "bob"
    And The user has email "bob@sample.app"

  Scenario: Authenticated user can list all users
    Given I login as "alice" with password "password123"
    When I list all users
    Then The response code is 200

  # ── Private Access ──────────────────────────────────────────────────────────

  Scenario: Cannot retrieve a user without authentication
    Given I'm not logged in
    When I retrieve the user with username "alice"
    Then The response code is 401

  Scenario: Cannot list users without authentication
    Given I'm not logged in
    When I list all users
    Then The response code is 401

  Scenario: Retrieving a non-existing user returns 404
    Given I login as "alice" with password "password123"
    When I retrieve the user with username "nonexistent"
    Then The response code is 404

