Feature: Search medicines
  As a user on the Home screen
  I want to search a medicine
  So that the medicine is filtered from the others on the screen

  Scenario: Search a medicine successfully
    Given I am on the Home screen
    When I click on the "Medicines" button
    And I click on the "Search" icon button
    And I enter "Paracetamol" in the field tagged "SearchField"
    And I click the Search button in the keyboard
    Then I should see only the searched medicine in the medicine list