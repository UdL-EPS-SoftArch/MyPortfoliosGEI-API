Feature: Portfolio Visibility Filtering
  In order to control who can see my portfolios
  As a user
  I want to filter portfolios by visibility level

  Background:
    Given There is a registered user with username "creator" and password "password123" and email "creator@sample.app"
    Given I login as "creator" with password "password123"

  Scenario: A PUBLIC portfolio appears in the public list
    When I create a portfolio named "Public Portfolio" with visibility "PUBLIC"
    Then The response code is 201
    And The portfolio "Public Portfolio" is listed under visibility "PUBLIC"

  Scenario: A PRIVATE portfolio does not appear in the PUBLIC list
    When I create a portfolio named "Secret Portfolio" with visibility "PRIVATE"
    Then The response code is 201
    And The portfolio "Secret Portfolio" is not listed under visibility "PUBLIC"

  Scenario: A PRIVATE portfolio appears in the PRIVATE list
    When I create a portfolio named "Secret Portfolio" with visibility "PRIVATE"
    Then The response code is 201
    And The portfolio "Secret Portfolio" is listed under visibility "PRIVATE"

  Scenario: An UNLISTED portfolio does not appear in the PUBLIC list
    When I create a portfolio named "Hidden Portfolio" with visibility "UNLISTED"
    Then The response code is 201
    And The portfolio "Hidden Portfolio" is not listed under visibility "PUBLIC"
