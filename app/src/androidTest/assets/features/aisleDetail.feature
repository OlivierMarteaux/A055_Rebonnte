Feature: Display Aisle content
  As a user on the Home screen
  I want to display the aisle content
  So that I can know which medicines are in this aisle

  Scenario: Navigate to the AisleDetail screen
    Given I am on the Home screen
    When I click on the "Analgesics & Pain Management" card
    Then I should arrive on the AisleDetail Screen
    And I can see and scroll the contained medicines