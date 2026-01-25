Feature: Display and scroll the medicine list
  As a user on the Medicine screen
  I want to see a scrolling list of all the medicines

  Scenario: Display and scroll the medicines list
    Given I am on the Home screen
    When I click on the "Medicines" button
    Then All the medicines are displayed and scrollable on the screen