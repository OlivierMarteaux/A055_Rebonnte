Feature: Display the user profile
  As a user on the Home screen,
  I want to access to the user profile
#
##  Background: Navigate to Home Screen
##    Given I am on the Splash screen
##    When I click on the "Sign in with email" button
##    Then I should arrive on the Login Screen
##    When I enter "fievel.farwest@example.com" in the "Email" field
##    And I click on the "Next" button
##    Then I should arrive on the Password screen
##    When I enter "test123&" in the "Password" field
##    And I click on the "Sign in" button
##    Then I should arrive on the Home Screen
#
#  Background: Navigate to home screen
#    Given I am on the Splash screen
#    When I click on the button tagged "home_screen"
#    Then I should arrive on the Home Screen
#
  Scenario: Navigate to Profile Screen
    Given I am on the Home screen
    When I click on the "Profile" button
    Then I should arrive on the Profile Screen for the current user