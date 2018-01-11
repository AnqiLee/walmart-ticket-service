# Walmart Ticket Service Java Project
---

## Use Case
---

This assignment is to desing and write a Ticket Service that provides the following functions:

1- Find number of seats available within the venue.
Note: available seats are seats that are neither held nor reserved.

2- Find and hold the best available seats on behalf of a customer. 
Note: each ticket hold should expire within a set number of seconds.

3- Reserve and commit a specific group of held seats for a customer.

## Requirments
---

- Project should be written in java.
- Maven or Gradle should be used as the build tools.
- Implementation of disk-based storage, a REST API and front-end GUI are not required.

## Assumptions
---

- The venue contains a set of rows. Each row has a number of seats.
- Seats in each row will be assigned to customer based on availablity and there will be no number assigned to each seat. So customers will take seats in a first come first order manner in each row. 
- If the number of available seats are less than the number of seats requested, the application will take those available seats and will put them on hold for customer.
- SeatHolds will expure in 120 seconds and seats will be added back to their rows.
- There are 8 rows, from 'A' to 'H', each containing 20 seats. 

## Environment
---

Programming language and version: Java 8.

Build tool: Apache Maven.

Web framework: Spring Boot.

DataBase: In-memory java data structures.

Server: Embeded Spring Boot server.

## Dependecies
---

Framwork and server:
org.springframework.boot

Testing Framework:
junit - 
org.mockito

Logging and Common tools:
org.apache.commons

## Building the project

1- Clone the application.

```
https://github.com/Armin-Smailzade/walmart-ticket-service.git
```
    
2- Install Maven and JDK 8 and set them inside the environment variables of the system. 

3- Build and run the application:

```
cd ticket-service
mvn package
cd target
```

Now you can run the application with the following command:

```
java -jar ticket-service-0.0.1-SNAPSHOT.jar
```

The server will start on port 8080 by default.

## Overall Structure

This application is built following the Model-View-Controller design pattern. This pattern is implemented using Spring Boot annotations metadata instead of XML configurations. The aplication has 3 APIs to access the functionalities defined in the requirements.

The following examples demonstrate the usage of these APIs:

1- Find number of seats available within the venue.
Note: available seats are seats that are neither held nor reserved.

Request:

```
GET - http://localhost:8080/ticketservice/availableSeats
```

Response:
```
{
    "availableSeats": 160
}
```

2- Find and hold the best available seats on behalf of a customer. 
Note: each ticket hold should expire within a set number of seconds.

Request:

```
POST - http://localhost:8080/ticketservice/holdSeats
```

Request Body:
```
{
  "numSeats": "50",
  "customerEmail": "armin@gmail.com"
}
```

Response Body:
```
{
    "holdId": 0,
    "customerEmail": "armin@gmail.com",
    "seatHoldRowList": [
        {
            "id": 0,
            "holdId": 0,
            "rowId": 0,
            "rowName": "A",
            "numSeats": 20
        },
        {
            "id": 1,
            "holdId": 0,
            "rowId": 1,
            "rowName": "B",
            "numSeats": 20
        },
        {
            "id": 2,
            "holdId": 0,
            "rowId": 2,
            "rowName": "C",
            "numSeats": 10
        }
    ]
}
```

3- Reserve and commit a specific group of held seats for a customer.

```
POST - http://localhost:8080/ticketservice/reserve
```

Request Body:
```
{
  "seatHoldId": "0",
  "customerEmail": "armin@gmail.com"
}
```

Response Body:
```
{
    "seatHoldId": 0,
    "confirmationCode": "armin@gmail.com",
    "customerEmail": "32ad1539-1c8a-4656-aa9f-8a271f2d360c"
}
```
## Data Base
---
In order to run the application and show the functionalities, a number of fake data has been provided just to show case the runtime functionality. However, these data structures are not thread safe and should not be used in a multithreaded environment. Once a real database is connected these data will be moved to their defined tables. All the objects that are meant to be transfered to a database are defined inside the "com.walmart.ticketservice.entity.tables" package.

## Packets
---
The structure of the request and responses are defined inside the "packets" folder. The Spring framework will be able to turn these objects into JSON string and vice versa. 

## Testing

In order to unit test the components, we need to mock the database. The Mockito libary was used to mock some of the functions in the WalmartTicketService class just to showcase the testing framework. Test cases can be run with the following command.

```
mvn test
```



