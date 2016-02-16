# Structure of the application
The following techniques are used:

* Players send data over UDP sockets to a data server
* Coaches can request information / heatmaps using a webservice
* Webservices request data from the data server. On request, the data server launches workers who do the actual work (i.e. generating heatmaps, calculating statistics, etc)

# Entity descriptions
## Player
Android application sending location data to the data server.

## Data server
Server that handles incoming data packets and store them in an organized manner.

# Inter-entity communication
## Player - Data server
Players send the following data to the data server, using UDP sockets:

* player_id
* location
* datetime

They send this data with a frequency of .. packets per ..
