# Distributed systems and cloud services 2020

## Group work 2 Ringer's clock

During the second assignment we will be getting familiar with networking as well as continue using threads.

The topic of the assignment is a wake up service for bird researchers, "ringers".


### About bird ringing

Bird ringing is a vital accessory in researching bird migration. It allows us to get information about the bird migration routes, resting areas and destinations as well as the travel time. In addition to the migration research, ringing is an irreplaceable research method in many other areas of bird research. Ringing allows us to get data about the age of the birds, death rate, the causes of death, birth places, habitats, partner loyalty, yearly population changes, offspring, inheritance of different properties and dominance hierarchy of the flock to mention a few. The data provided by ringing is also highly valuable in bird conservation. _Translated and loosely based on: [LUOMUS, Rengastustoimisto](https://www.luomus.fi/fi/lintujen-rengastus#Miksi%20lintuja%20rengastetaan)_

Usually the the field work of bird research happens early in the morning and is possible only when the weather allows it. Work is done with a group of people. The challenge is to estimate accurately whether the weather of tomorrow is appropriate for ringing.


### Wake up service for ringers

The system works in client-server principle and uses open data API to determine the weather in the morning. The client application works as an "alarm clock" and users use it to set the alarm as well. The server cordinates the wake up calls, checks the weather and makes the actual request to set the alarm off.

#### Requirements
- The server supports unlimited amount of simultaneous users
- The server supports unlimited amount of simultaneous research groups
- Every group only has a **single** (1) leader
- The leader status is assigned to the one creating the group
- It should be possible to set alarm time freely (no hard coding the time)
- A single user is only allowed to join a single (1) group at the time
- Users should be able to resign from the group thus cancelling the alarm for themselves
- If the leader resigns from the group, the wake up should be cancelled for every group member, the group should be removed from the server and the alarm status should be resetted from clients
- The weather data should be fetched and parsed using the open API of Finnish Meteorological Institute (links given in code template)

#### Presumptions
- To simplify the assignment, the network traffic is assumed error-free
- The server is solely responsible for keeping time: the clients do not decide wheter to wake up or not; the server will decide when to set off alarm
- There are and will be only two conditions on weather
- To simplify the weater functionality; it is allowed to hard code the location (always Turku or whichever city you decide) into the server.

#### The wake up process

The leader will request an alarm by creating a wake up group using their client program. The new group should have a name, wake up time and the weather conditions specified. After creating the group, other group members should be able to join the group.

When the time for alarm is up, the server should check the current weather from the FMI API:
- If the weather conditions are not holding true, the wake up call should be canceled automatically and the alarm status should be reseted from the clients as well.
- If the weather is determined OK, only the group leader should be waken up first. The leader should be presented with a confirmation prompt and only after the prompt is accepted, the rest of the group will be woken up. After wake up, the clients should be reseted and the group should be removed.

### The program structure

![Layout diagram](images/deployment.jpg)

#### Client

**Package fi.utu.tech.ringersClock**

![Asiakasohjelma](images/client.jpg)

The client has the following classes:
- App
- ClockClient
- Gui_IO

The `App` class works as an entry point to our client program and is used to start the application. Only the `startClient()` method along with some variables may require modifications. 

`ClockClient` should contain the network services required by the client. It should be able to contact the server as well as listen to the incoming traffic from the server after the connection has been established. The methods sending data to the server can be written here.

`Gui_IO` contains methods that are (or should be) called once something happens in the graphical user interface or in the network. This class is one of the most important ones for completing the exercise. Some of the methods must be modified and some of them should not be. The methods requiring and not requiring modification are annotated in the code.


**Package fi.utu.tech.ringersClock.UI**

This package contains only JavaFX user interface controller and **it should NOT be modified.**

**Package fi.utu.tech.ringersClock.entities**

This package contains only `WakeUpGroup` class that describes a single wake up group. This class is used in the client UI and it can be used to transfer data over a network. **This class must be modified**. Also **the whole package most probably requires more classes made by you**.

#### Server

**Package fi.utu.tech.ringersClockServer**

![Server](images/server.jpg)

The server template includes the following classes:
 - ServerApp
 - ServerSockerListener
 - WakeUpService

The server application template is a bit simpler than the client one; giving more freedom but also more features to implement. The server does not have a graphical user interface. The class `ServerApp` serves as an entry point for the server application; the server should be started using this class. Mainly it works as-is but depending on your specific implementation, some changes might be mandatory: new variables, modifications to method parameters and method calls.

The class `ServerSockerListener` is the main component handling server-side networking. It should listen for new connections from clients. Implementing this functionality is in group's responsibility.

`WakeUpService` handles the cordination and "bookkeeping" of requested alarms and groups.

In addition to the classes provided, full implementation of the server requires classes written from scratch as well.


#### Weather Service

**Package fi.utu.tech.weatherInfo**

![Weather Service](images/weather.jpg)

The weather service includes the following classes:
 - FMIWeatherService
 - WeatherData

`FMIWeatherService` should have a static method that will use the FMI API to fetch the current weather data and return the relevant information encapsulated as a `WeatherData` object.


## Assignment 1: Planning

The first part of the assignment is creating a planning document. The purpose of this document is to help the student familiarize oneself with the topic and guide in the right direction. During this phase, it is good to go through the application template trying to understand the different components included. Understanding the template makes it easier to implement working final product.

A good planning will make implementation faster to work with and avoids designing invalid class hierarchies.

**Class- and sequence diagram**

The planning document should include a class diagram as well as a sequence diagram

The class diagram should include the classes required to implement the software and a brief description of the responsibilities for the classes. The provided classes work as a base and you should complete the diagram with your own classes. Note that **not** every method and variable should be described. The controller for graphical user interface (`fi.utu.tech.ringersClock.UI`) can be fully omitted from the diagram as well. Include the most important instance variables and methods and you should be good to go.

The sequence diagram should show, how the program execution advances. Which component does what and what kind of messages are transmitted. For example; if the user creates a new wake up group, what happens? Then again, when it is the time to wake up; what happens? The sequence diagram should be presented as a class level diagram. Ie. the actors in the diagram are classes.

## Assignment 2: Implementing software

The program will be developed using the provided template where the user interface has already been done for the student. The server software has a basic structure in place as well. For accessing weather data, class `FMIWeatherService` contains relevant links to the FMI API which can be used in your project. 

For more information regarding to The Finnish Meteorological Institute's open data:

*[The Finnish Meteorological Institute's open data](https://en.ilmatieteenlaitos.fi/open-data)*






