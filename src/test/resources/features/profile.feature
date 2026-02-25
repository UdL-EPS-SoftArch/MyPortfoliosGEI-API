Feature: Profile management
  As an API client
  I want to create, retrieve, update and validate Profiles
  So that users can manage their personal information

  Background:
    Given the system is running

  Scenario: Create a valid profile
    When I send a POST request to "/profiles" with body:
      """
      {
        "fullName": "Ada Lovelace",
        "email": "ada@example.com",
        "bio": "First computer programmer",
        "avatarUrl": "https://example.com/ada.png",
        "location": "London",
        "github": "ada-lovelace",
        "twitter": "ada_l",
        "instagram": "ada_insta",
        "linkedin": "ada-lovelace",
        "userId": 1
      }
      """
    Then the response status should be 201
    And the response body should contain "Ada Lovelace"
    And the response body should contain "ada@example.com"

  Scenario: Fail to create a profile with invalid email
    When I send a POST request to "/profiles" with body:
      """
      {
        "fullName": "Alan Turing",
        "email": "not-an-email",
        "userId": 1
      }
      """
    Then the response status should be 400
    And the response body should contain "email"

  Scenario: Retrieve an existing profile
    Given a profile exists with id 1
    When I send a GET request to "/profiles/1"
    Then the response status should be 200
    And the response body should contain "fullName"
    And the response body should contain "email"

  Scenario: Update a profile
    Given a profile exists with id 1
    When I send a PUT request to "/profiles/1" with body:
      """
      {
        "fullName": "Ada Byron Lovelace",
        "bio": "Mathematician and pioneer of computing"
      }
      """
    Then the response status should be 200
    And the response body should contain "Ada Byron Lovelace"

  Scenario: Prevent duplicate email
    Given a profile exists with email "ada@example.com"
    When I send a POST request to "/profiles" with body:
      """
      {
        "fullName": "Another User",
        "email": "ada@example.com",
        "userId": 2
      }
      """
    Then the response status should be 409
    And the response body should contain "email already exists"
