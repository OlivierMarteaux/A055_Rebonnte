Feature: Edit a medicine
  As a user on the Home screen
  I want to modify the stock of a medicine
  So that the new medicine stock is displayed on the home screen

  Background: Navigate to the AddOrEditMedicine screen
    Given I am on the Home screen
    When I click on the "Medicines" button
    And I click on the "Paracetamol" card
    Then I should arrive on the AddOrEditMedicine Screen

  Scenario: Edit the medicine stock successfully
    Given I am on the AddOrEditMedicine Screen
    When I select a quantity in the Stock picker field
    And I click on the "Validate" button
    Then I should arrive on the MedicineList Screen
    And I should see the edited medicine with the updated stock

  Scenario: Cannot validate if medicine stock is unchanged
    Given I am on the AddOrEditMedicine Screen
    Then I cannot click on the "Validate" button