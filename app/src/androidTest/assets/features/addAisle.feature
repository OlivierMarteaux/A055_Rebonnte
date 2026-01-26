Feature: Add a new aisle
  As a user on the Home screen
  I want to add a new aisle
  So that the new aisle appears at the top of the aisles list

  Background: Navigate to the Add Aisle screen
    Given I am on the Home screen
    When I click on the "AddAisle" FAB button
    Then I should arrive on the AddAisle Screen

  Scenario: Add a new aisle successfully
    Given I am on the AddAisle Screen
    When I enter "Test Aisle" in the "Name" field
    And I click on the "Validate" button
    Then I should arrive on the Home Screen
    And I should see "Test Aisle" added at the top of the "aisles" list

  Scenario: Cannot add new aisle when Name field is empty
    Given I am on the AddAisle Screen
    Then I cannot click on the "Validate" button