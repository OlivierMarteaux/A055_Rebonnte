Feature: Add a new medicine
  As a user on the Home screen
  I want to add a new medicine
  So that the new medicine appears at the top of the medicines list

  Background: Navigate to the AddOrEditMedicine screen
    Given I am on the Home screen
    When I click on the "Medicines" button
    And I click on the "AddMedicine" FAB button
    Then I should arrive on the AddOrEditMedicine Screen

  Scenario: Add a new medicine successfully
    Given I am on the AddOrEditMedicine Screen
    When I enter "Test Medicine" in the "Name" field
    And I select an aisle in the Aisle picker field
    And I select a quantity in the Stock picker field
    And I click on the "Validate" button
    Then I should arrive on the MedicineList Screen
    And I should see "Test Medicine" added at the top of the "medicines" list

#  Scenario: Cannot add new aisle when Name field is empty
#    Given I am on the AddAisle Screen
#    Then I cannot click on the "Validate" button