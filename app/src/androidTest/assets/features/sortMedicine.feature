Feature: Sort the medicine list
  As a user on the Home screen
  I want to sort the medicine list either by name or stock
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