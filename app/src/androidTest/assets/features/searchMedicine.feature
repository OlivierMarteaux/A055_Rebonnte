Feature: Search for a medicine
  As a user on the Medicine List screen
  I want to search for a medicine
  So that the medicine is filtered from the others on the screen

  Background: Navigate to the Medicine List screen
    Given I am on the Home screen
    When I click on the "Medicines" button
    Then I should arrive on the MedicineList Screen

  Scenario: Search a medicine successfully
    Given I am on the MedicineList screen
    When I click on the "Search" icon button
    And I enter "Paracetamol" in the field tagged "SearchField"
    And I click the Search button in the keyboard
    Then I should see only the searched medicine in the medicine list