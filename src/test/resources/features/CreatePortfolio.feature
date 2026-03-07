Feature: Create Portfolio
    In order to create a portfolio
    As a user
    I want to be able to create a portfolio

Scenario: Create an owned Portfolio
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    When I create a new portfolio with name "pname"
    Then The response code is 201
    And The new portfolio is owned by "user"
    And The list of portfolios owned by "user" includes one named "pname"

Scenario: Create a new Portfolio
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And There is no exisiting portfolio with name "pname_2"
    When I create a new portfolio with name "pname_2"
    Then The response code is 201
    And The list of portfolios owned by "user" includes one named "pname_2"

Scenario: Cannot create a portfolio with an empty name
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    When I try to create a portfolio with an empty name
    Then The system should reject the portfolio creation

  Scenario: Cannot create a portfolio with a description too long
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    When I try to create a portfolio with a description too long
    Then The system should reject the portfolio creation

# Scenario: Private portfolios are hidden from public lists
#  Given There is a registered user with username "user" and password "password" and email "user@sample.app"
#  And I login as "user" with password "password"
#  When I create a new PRIVATE portfolio with name "Secret_Portfolio"
#  Then The response code is 201
#  And The portfolio "Secret_Portfolio" should not be visible in the public list

    Scenario: As an admin, I can assign a portfolio to a user
    Given There is a registered user with username "admin" and password "adminpass" and email "admin@sample.app"
    And There is a registered user with username "targetUser" and password "password" and email "target@sample.app"
    And I login as "admin" with password "adminpass"
    When I create a new portfolio with name "Assigned_By_Admin" assigned to "targetUser"
    Then The response code is 201
    And The new portfolio is owned by "targetUser"

  Scenario: As a user, I cannot delete another user's portfolio
    Given There is a registered user with username "user1" and password "password" and email "user1@sample.app"
    And There is a registered user with username "user2" and password "password" and email "user2@sample.app"
    And I login as "user1" with password "password"
    And I create a new portfolio with name "User1_Portfolio"
    # Cambiamos al usuario 2 e intentamos borrar el portfolio del usuario 1
    And I login as "user2" with password "password"
    When I try to delete the recently created portfolio
    Then The system should reject the action with a Forbidden error

  Scenario: As an admin, I can delete a user's portfolio
    Given There is a registered user with username "admin" and password "adminpass" and email "admin@sample.app"
    And There is a registered user with username "user1" and password "password" and email "user1@sample.app"
    And I login as "user1" with password "password"
    And I create a new portfolio with name "User1_Portfolio_To_Delete"
    # Cambiamos al admin para aplicar su poder
    And I login as "admin" with password "adminpass"
    When I try to delete the recently created portfolio
    Then The portfolio is successfully deleted