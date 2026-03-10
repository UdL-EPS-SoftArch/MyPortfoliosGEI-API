Feature: Patch User
  In order to keep my profile up to date
  As an authenticated user
  I want to update my email and password and have invalid or unauthorized attempts rejected

  Background:
    Given There is a registered user with username "alice" and password "password123" and email "alice@sample.app"

  # ── Success ─────────────────────────────────────────────────────────────────

  Scenario: Successfully update user email
    Given I login as "alice" with password "password123"
    When I update the user "alice" email to "newalice@sample.app"
    Then The response code is 200
    And The user has email "newalice@sample.app"

  Scenario: Successfully update user password
    Given I login as "alice" with password "password123"
    When I update the user "alice" password to "newpassword123"
    Then The response code is 200
    And I can login with username "alice" and password "newpassword123"

  # ── Failure ──────────────────────────────────────────────────────────────────

  Scenario: Cannot update user email to blank
    Given I login as "alice" with password "password123"
    When I update the user "alice" email to ""
    Then The response code is 400
    And The error message is "must not be blank"

  Scenario: Cannot update user email to invalid format
    Given I login as "alice" with password "password123"
    When I update the user "alice" email to "invalidemail"
    Then The response code is 400
    And The error message is "must be a well-formed email address"

  Scenario: Cannot update user password to shorter than 8 characters
    Given I login as "alice" with password "password123"
    When I update the user "alice" password to "short"
    Then The response code is 400
    And The error message is "length must be between 8 and 256"

  Scenario: Cannot update user without authentication
    Given I'm not logged in
    When I update the user "alice" email to "hacked@sample.app"
    Then The response code is 401

  Scenario: Cannot update user password without authentication
    Given I'm not logged in
    When I update the user "alice" password to "hackedpassword"
    Then The response code is 401

  Scenario: Cannot update another user's account
    Given There is a registered user with username "bob" and password "password123" and email "bob@sample.app"
    And I login as "alice" with password "password123"
    When I update the user "bob" email to "hacked@sample.app"
    Then The response code is 403

