Feature: Tag management
    As a user
    I want to manage tags
    So that projects can be categorized

    Background:
        Given the system is initialized
        And all tags are cleared

    # -------------------------
    # CREATE TAG SCENARIOS
    # -------------------------
    Scenario: Create a valid tag
        When I create a tag with name "Java"
        Then the response status should be 201
        And the tag name should be "Java"

    Scenario: Create a tag with blank name
        When I create a tag with name ""
        Then the response status should be 400

    Scenario: Create a tag with invalid characters
        When I create a tag with name "C#_!"
        Then the response status should be 400

    Scenario: Create a tag with duplicate name
        Given a tag exists with name "Java"
        When I create a tag with name "Java"
        Then the response status should be 409

    # -------------------------
    # READ TAG SCENARIOS
    # -------------------------
    Scenario: Get all tags
        Given a tag exists with name "Spring"
        And a tag exists with name "Docker"
        When I request all tags
        Then the response status should be 200
        And the response should contain 2 tags

    Scenario: Get tag by id
        Given a tag exists with name "Kotlin"
        When I request that tag by id
        Then the response status should be 200
        And the tag name should be "Kotlin"

    Scenario: Get non-existing tag
        When I request tag with id 9999
        Then the response status should be 404

    # -------------------------
    # DELETE TAG SCENARIOS
    # -------------------------
    Scenario: Delete a tag
        Given a tag exists with name "React"
        When I delete that tag
        Then the response status should be 204

    Scenario: Verify tag deletion
        Given a tag exists with name "Angular"
        When I delete that tag
        Then the response status should be 204
        And requesting that tag by id should return 404

    Scenario: Delete non-existing tag
        When I delete tag with id 9999
        Then the response status should be 404