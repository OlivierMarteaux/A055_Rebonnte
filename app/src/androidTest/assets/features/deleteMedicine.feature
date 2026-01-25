Feature: Delete a medicine
  As a user on the Medicine screen
  I want to delete a medicine
  So that the medicine is removed from the medicines list

  Scenario: Delete a medicine successfully
    Given I am on the Home screen
    When I click on the "Medicines" button
    And I click on the "Amoxicillin" card
    When I click on the "Delete" icon button
    Then I should arrive on the MedicineList Screen
    And the deleted medicine should have been removed from the top of the list