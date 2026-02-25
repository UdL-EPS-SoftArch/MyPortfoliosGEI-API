Feature: Profile Management
  The system must allow users to create, update, retrieve, search, and delete profiles.
  Profiles belong to Users and contain personal, social, and metadata information.

  Background:
    Given the system is running
    And a User exists with id 1 and username "john_doe"
    And a User exists with id 2 and username "alice_dev"

  # ------------------------------------------------------------
  # PROFILE CREATION
  # ------------------------------------------------------------

  Scenario: Successfully create a complete profile
    When I send a POST request to "/profiles" with body:
      """
      {
        "fullName": "Ada Lovelace",
        "email": "ada@example.com",
        "bio": "Pioneer of computing",
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
    And the response body should contain "createdAt"
    And the response body should contain "updatedAt"

  Scenario: Create a profile with only required fields
    When I send a POST request to "/profiles" with body:
      """
      {
        "fullName": "Alan Turing",
        "email": "alan@example.com",
        "userId": 2
      }
      """
    Then the response status should be 201
    And the response body should contain "Alan Turing"
    And the response body should contain "alan@example.com"

  Scenario: Fail to create a profile with missing required fields
    When I send a POST request to "/profiles" with body:
      """
      {
        "email": "missingname@example.com",
        "userId": 1
      }
      """
    Then the response status should be 400
    And the response body should contain "fullName"

  Scenario: Fail to create a profile with invalid email
    When I send a POST request to "/profiles" with body:
      """
      {
        "fullName": "Invalid Email User",
        "email": "not-an-email",
        "userId": 1
      }
      """
    Then the response status should be 400
    And the response body should contain "email"

  Scenario: Fail to create a profile with duplicate email
    Given a profile exists with email "ada@example.com"
    When I send a POST request to "/profiles" with body:
      """
      {
        "fullName": "Duplicate User",
        "email": "ada@example.com",
        "userId": 2
      }
      """
    Then the response status should be 409
    And the response body should contain "email already exists"

  Scenario: Fail to create a profile for a non-existing user
    When I send a POST request to "/profiles" with body:
      """
      {
        "fullName": "Ghost User",
        "email": "ghost@example.com",
        "userId": 999
      }
      """
    Then the response status should be 404
    And the response body should contain "User not found"

  # ------------------------------------------------------------
  # RETRIEVAL
  # ------------------------------------------------------------

  Scenario: Retrieve an existing profile by id
    Given a profile exists with id 1
    When I send a GET request to "/profiles/1"
    Then the response status should be 200
    And the response body should contain "fullName"
    And the response body should contain "email"

  Scenario: Fail to retrieve a non-existing profile
    When I send a GET request to "/profiles/999"
    Then the response status should be 404
    And the response body should contain "Profile not found"

  # ------------------------------------------------------------
  # UPDATE
  # ------------------------------------------------------------

  Scenario: Update a profile's basic information
    Given a profile exists with id 1
    When I send a PUT request to "/profiles/1" with body:
      """
      {
        "fullName": "Ada Byron Lovelace",
        "bio": "Mathematician and computing pioneer"
      }
      """
    Then the response status should be 200
    And the response body should contain "Ada Byron Lovelace"
    And the response body should contain "updatedAt"

  Scenario: Update social links only
    Given a profile exists with id 1
    When I send a PATCH request to "/profiles/1" with body:
      """
      {
        "github": "new-github-handle",
        "twitter": "new-twitter"
      }
      """
    Then the response status should be 200
    And the response body should contain "new-github-handle"
    And the response body should contain "new-twitter"

  Scenario: Fail to update a profile with invalid data
    Given a profile exists with id 1
    When I send a PUT request to "/profiles/1" with body:
      """
      {
        "fullName": "",
        "email": "invalid"
      }
      """
    Then the response status should be 400
    And the response body should contain "fullName"
    And the response body should contain "email"

  # ------------------------------------------------------------
  # DELETE
  # ------------------------------------------------------------

  Scenario: Delete an existing profile
    Given a profile exists with id 1
    When I send a DELETE request to "/profiles/1"
    Then the response status should be 204

  Scenario: Fail to delete a non-existing profile
    When I send a DELETE request to "/profiles/999"
    Then the response status should be 404
    And the response body should contain "Profile not found"

  # ------------------------------------------------------------
  # SEARCH, FILTERING, SORTING, PAGINATION
  # ------------------------------------------------------------

  Scenario: List all profiles with pagination
    Given several profiles exist
    When I send a GET request to "/profiles?page=0&size=10"
    Then the response status should be 200
    And the response body should contain "content"
    And the response body should contain "totalElements"

  Scenario: Search profiles by name
    Given a profile exists with fullName "Ada Lovelace"
    When I send a GET request to "/profiles?search=ada"
    Then the response status should be 200
    And the response body should contain "Ada Lovelace"

  Scenario: Filter profiles by location
    Given a profile exists with location "London"
    When I send a GET request to "/profiles?location=London"
    Then the response status should be 200
    And the response body should contain "London"

  Scenario: Sort profiles alphabetically
    Given several profiles exist
    When I send a GET request to "/profiles?sort=fullName,asc"
    Then the response status should be 200
    And the response body should contain "content"

  # ------------------------------------------------------------
  # TIMESTAMPS
  # ------------------------------------------------------------

  Scenario: Verify timestamps are automatically updated
    Given a profile exists with id 1
    And the profile has an initial updatedAt timestamp
    When I send a PUT request to "/profiles/1" with body:
      """
      {
        "bio": "Updated biography"
      }
      """
    Then the response status should be 200
    And the response body should contain a newer "updatedAt" timestamp

  # ------------------------------------------------------------
  # RELATIONSHIP WITH USER
  # ------------------------------------------------------------

  Scenario: Retrieve the user associated with a profile
    Given a profile exists with id 1 and userId 1
    When I send a GET request to "/profiles/1/user"
    Then the response status should be 200
    And the response body should contain "john_doe"

  Scenario: Prevent creating multiple profiles for the same user
    Given a profile exists for userId 1
    When I send a POST request to "/profiles" with body:
      """
      {
        "fullName": "Another Profile",
        "email": "another@example.com",
        "userId": 1
      }
      """
    Then the response status should be 409
    And the response body should contain "User already has a profile"

# ------------------------------------------------------------
# VALIDATION EDGE CASES
# ------------------------------------------------------------

Scenario: Fail to create profile with blank fullName
    When I send a POST request to "/profiles" with body:
      """
      {
        "fullName": "   ",
        "email": "blankname@example.com",
        "userId": 1
      }
      """
    Then the response status should be 400
    And the response body should contain "fullName"

  Scenario: Fail to create profile with excessively long fullName
    When I send a POST request to "/profiles" with body:
      """
      {
        "fullName": "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
        "email": "toolong@example.com",
        "userId": 1
      }
      """
    Then the response status should be 400
    And the response body should contain "fullName"

    
