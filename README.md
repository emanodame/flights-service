# flights-service
Flight Service microservice responsible for serving information about flights 

How to run:

Please just import with Maven. 'mvn clean install'.
After, start the Spring Boot application via IntelliJ.

Sample Url: http://localhost:8080/flights/interconnections?departure=DUB&arrival=WRO&departureDateTime=2020-10-10T07:00&arrivalDateTime=2020-10-15T21:00

---

Overview:

[Controller]

FlightController.java: Configuration of endpoint.

[Services]

RoutesService.java: Retrieves Routes from endpoint and applies appropriate filtering

SchedulesService.java: Retrieves Schedules from endpoint.

AirportGraphService.java: Constructs a Graph representation of Airports. This may be overkill for this
usecase but it makes finding direct and interconnecting airports very simple.

FlightsService.java: Main service which FlightController calls to get all flights.

DirectFlightsRetriever.java: Component that will use AirportGraph and get all direct flights. Also uses ScheduleService
get scheduling information. 

e.g If departure = JFK and arrival = MAD. Get Flight if airport graph has JFK -> MAD

InterconnectingFlightsRetriever.java: Component that will use AirportGraph and get all interconnecting flights. Also uses ScheduleService
get scheduling information.
 
e.g If departure = JFK and arrival = MAD. Get Flight if airport graph has JFK -> BER -> MAD

-------

Unfortunately, I had a very busy week/weekend so couldn't completely finish.

Features that need to be implemented/improved:

- Implement feature to ensure that direct flights are before arrival time. (Should be trivial with additional filtering in DirectFlightsRetriever.java)
- Move hardcoded client URLs to a application.properties file.
- Introduce more sub packages in domain package.
- Introduce Caching to the application for more efficient processing (Routes Client Cache + GraphService for example)
- More Unit Testing to cover some edge cases.
- General clean up InterconnectingFlightsRetriever.java can be simplified further.