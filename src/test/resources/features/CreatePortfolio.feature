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
    When I create a new portfolio with name "pname"
    Then The response code is 201
    And There is no exisiting portfolio with name "pname"
    And The list of portfolios owned by "user" includes one named "pname"


