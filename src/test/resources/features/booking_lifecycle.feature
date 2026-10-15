@bdd @regression
Feature: Booking lifecycle
  As a hotel front-desk system
  I want to create, amend and cancel guest bookings through the API
  So that reservations always reflect what the guest asked for

  Background:
    Given the booking service is available

  @smoke
  Scenario: A new booking can be created and retrieved
    Given a guest "Sara" "Ahmed" wants to stay 3 nights for 450 with deposit paid
    When the booking is submitted
    Then the booking is confirmed with a booking id
    And retrieving the booking returns the same details

  Scenario: An authorised user can change the guest name
    Given an existing booking for "Bilal" "Raza"
    And I am authenticated as an admin
    When I change the first name to "Bilal-Updated"
    Then the booking shows first name "Bilal-Updated"

  Scenario: An unauthenticated user cannot cancel a booking
    Given an existing booking for "Noor" "Fatima"
    When an unauthenticated user tries to cancel the booking
    Then the request is rejected with status 403
    And the booking still exists

  Scenario: A cancelled booking can no longer be retrieved
    Given an existing booking for "Usman" "Tariq"
    And I am authenticated as an admin
    When I cancel the booking
    Then retrieving the booking returns status 404

  Scenario Outline: Bookings across price boundaries are stored exactly
    Given a guest "<first>" "<last>" wants to stay <nights> nights for <price> with deposit <deposit>
    When the booking is submitted
    Then retrieving the booking returns the same details

    Examples:
      | first | last    | nights | price | deposit |
      | Low   | Price   | 1      | 1     | paid    |
      | High  | Price   | 30     | 99999 | paid    |
      | No    | Deposit | 2      | 200   | unpaid  |
