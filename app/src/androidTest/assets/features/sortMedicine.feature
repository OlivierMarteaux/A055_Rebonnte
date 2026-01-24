Feature: Search and sort the medicine list
  As a user on the Home screen
  I want to search or sort the medicine list either by name or stock
  So that I can manage them more easily

  Scenario Outline: Sort the medicine list successfully
    Given I am on the Home screen
    When I click on the "Medicines" button
    And I click on the "Sort" icon button
    And I click on the "<SortOption>" menu entry
    Then I should see the medicines sorted by "<SortDirection>" "<SortField>"

    Examples:
      | SortOption       | SortDirection | SortField |
      | Ascending name   | ascending     | name      |
      | Ascending stock  | ascending     | stock     |
      | Descending stock | descending    | stock     |

  Scenario: Search a medicine successfully
    Given I am on the Home screen
    When I click on the "Medicines" button
    And I click on the "Search" icon button
    And I enter "Paracetamol" in the field tagged "SearchField"
    And I click the Search button in the keyboard
    Then I should see only the searched medicine in the medicine list