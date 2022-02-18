# Online-booking-restoraunt

## Why

There has always been a problem with reserving a table in a restoraunt, because every reservation includes making a phone call, hopefully that someone will pick up the phone
and answer. Most of the time it's not a problem, but what if it's a popular/busy restoraunt? The waiter will need to keep track of all the reservations and hopefully not make a mistake by overlapping two reservations on the same table. The purpose of this app is to allow users, instead of calling the restoraunt, to reserve a table digitally ahead of time, while the waiter/s has easy access and flexible maintenance over the reservations that the users make from their smart phones/PC's.

## Description

Online-booking-restoraunt is an online web application. The primary goal of the API is to allow reserving a table at a certain date/time in a restoraunt.
This API is categorised in three main sections.

- Admin section
- Public section
- Waiter section.

All three sections have different functionalities related to one database.
For the API to work correctly, all three sections must be in sync. 

## Overview

### Basic overview

In order to reserve a table, the client must sign up/login. Following the authentication procedure, the client is redirected to the public section where they can view the home page/main page for the restoraunt. From the page they can either make a reservation or cancel their current reservation. Each user can have up to one reservation.
The CEO/manager keeps track of all the users including all of the registered clients and the restoraunt's waiters.
For all three sections to be synced there must be a human maintenance in the restoraunt itself. This is where the waiter section comes in to play where there are 1+ responsible "waiters" for the tables and the clients's reservations.
Each user has a "balance" that corresponds to their money.

## Admin section 

The Administrative section corresponds to the manager/CEO functionalities of the app.
The manager for one has the following abilities/functionalities:

- Manage all users -> (promote a certain user to a waiter/admin, demote a user to an ordinary client)
- View all roles. The roles of a user determine whether they are authorised or not for a certain url.
- Remove a user.

after signing up, by default the role "USER", corresponding to the client(public section) is assigined to the new signed up user.

## Public section

The Public section is based on the client's functionalities. Every client has the following functionalities:

- View of the home page for the restoraunt.
- Reserving a table at a certain time and day.
- Canceling the same reservation -> input username and password.

### RULES:

### Reserving 

The client must fill in a form containing the username and password of the client. The form allows the user to select the date for the reservation using a calendar. 
Now, for the service to be functional and fair for every client, the form asks the client at which period of the day they would like to attend to their reservation. There are three periods, "Breakfast" {07:00 AM - 12:00 PM}, "Lunch" {01:00 PM - 07:00 PM}, "Dinner" {08:00 PM - 10:00 PM}. Following the period, the user needs to specify a time of arrival within their respective period interval. Every reservation has a "fee" that the client must pay in advance to insure that the they will in fact show up on the reservation. Breakfast - 150. Lunch - 300. Dinner - 600.
This must be accepted. The minimal time to reserve must be within a day from now. For example, if today is 18/2/22, the minimum time to make a reservation would be on 19/2/22. 
 
 ### Basic reservation procedure 
  
In case the client does not show up in the restoraunt within 50 minutes after the specified time, the reservation dies and the fee from the reservation is transfered to the admin's account. If the user is 50 minutes earlier before the time of the reservation the service will not find their reservation.

### Canceling the reservation 
   
The canceling procedure must be executed within 2 hours from the reserving moment. For example if the current time is 06:00 PM
the client must cancel their reservation until 08:00 PM. If the client doesn't cancel it within two hours, the reservation lives on.
 
## Waiter section
 
 This section follows the actions of the "waiter" - the person responsible for the reservations in the local place(restoraunt).
 The waiter keeps track of who arrives and whether they have a reservation or not. Which table they sit on, etc...
 Functionalities: 
 - View of all the tables in the restoraunt (ID, Occupied/Not, Actions). 
 - View of today reservations.
 - Removing all "expired reservations" -> Each reservation that hasn't been attended to is marked as an expired reservation, which the waiter can remove. While doing so
   the fee from every reservation with a user(non-guest reservation) is being transfered to the admin's wallet.
 - Actions -> on one table the waiter can execute the following commands:
 
### Occupied  
Informs the service that a user with a reservation has just arrived. The service looks for the current reservation
and sets that table to occupied with the current user that has just arrived. If the service does not return a reservation, the user is either late
(the user has arrived after 50 minutes from his original reservation time) or the user is too early (50 minutes before his reservation time).
   
### Empty 
Informs the service that the current client has called for check or has gotten up from the table. This is crucial as one 
table must be empty in order for another client to sit on tha table.
 
### No reservation arrival

What if a person without a reservation arrives and sits on a table where there is a reservation around the time when this person arrived? 
This is why the minimal time for reserving is one day. The waiters know that this day has only these reservations and nothing more. Having this in mind they can easily organise
and place this guest on a table without a reservation at this time of the day. Everyone is happy!

## Security

Each user has a collection of roles. 
There are three roles [User, Waiter, Admin].
Based on these roles, one user can access different urls.
But if the user does not contain one or two of these roles then they will not be authorised for some urls.
By default each user has the role USER that corresponds to the public section.






#### Mile Stanislavov
