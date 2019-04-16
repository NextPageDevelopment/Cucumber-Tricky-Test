@Sample02
Feature: Cucumber example

  @LaunchBrowser @CloseBrowser
  Scenario: Check Login
    Given I am on "http://localhost:5000/"
    When I type "myusername" for username and "mypassword" for password
    And I click submit button
    Then I should see "LOGIN SUCCESS"

  @LaunchBrowser @CloseBrowser
  Scenario: Check Login - failed
    Given I am on "http://localhost:5000/"
    When I type "admin" for username and "admin" for password
    And I click submit button
    Then I should see "LOGIN FAILED"
