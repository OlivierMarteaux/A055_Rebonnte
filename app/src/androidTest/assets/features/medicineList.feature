Feature: Display and scroll the medicine list
  As a user on the Medicine screen
  I want to see a scrolling list of all the medicines

  Background: Navigate to the Medicine List screen
    Given I am on the Home screen
    When I click on the "Medicines" button
    Then I should arrive on the MedicineList Screen

  Scenario: Display and scroll the aisles list
    Given I am on the Home screen
    Then All the aisles are displayed and scrollable on the screen