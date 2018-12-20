# Sample Application

This project includes a Java sample application, built on the Sprint Boot Data REST framework, that exposes multiple REST services to read and write data stored in Azure Cosmos DB.

The REST services are hosted in containers running in Azure App Service for Containers.

HA/DR is provided by hosting the services in multiple regions, as well as Cosmos DB's native geo-redundancy. 

Traffic Manager is used to route traffic based on geo-proximity, and Application Gateway provides path-based routing, service authentication and DDoS protection.

Cosmos DB is configured to use the NoSQL MongoDB API. *(Note: We are currently working to add a sample that uses the Cosmos SQL API.)*

In order to demonstrate Cosmos DB performance with large amounts of data, the project imports historical movie data from [IMDb](https://www.imdb.com/interfaces/). See (https://datasets.imdbws.com/). The datasets include 8.9 million people, 5.3 million movies and 30 million relationships between them.

## REST API

We're using three kinds of models: `Person`, `Title`, and `Principal`. The `Person` model represents a person who participates in media, either in front of the camera or behind the scenes. The `Title` represents what it sounds like - the title of the piece of media, be it a movie, a TV series, or some other kind of similar media. Finally, the `Principal` model and its derivative child class `PrincipalWithName` represent the intersection of Person and Title, ie. what a particular person does or plays in a specific title.

To meaningfully access this IMDb dataset and these models, there are a few routes one can access on the API.

+ `/people`
  + `POST` - Creates a person, and returns information and ID of new person
  + `GET` - Returns a small number of people entries
+ `/people/{nconst}` > nconst is the unique identifier
  + `GET` - Gets the person associated with ID, and returns information about the person
  + `PUT` - Updates a person for a given ID, and returns information about updated person
  + `DELETE` - Deletes a person with a given ID, and returns the success/failure code
+ `/people/{nconst}/titles` > nconst is the unique identifier
  + `GET` - Gets the titles in the dataset associated with the person with specified ID and returns them in an array
+ `/titles`
  + `POST` - Creates a title, and returns the information and ID of the new titles
  + `GET` - Returns a small number of title entries
+ `/titles/{tconst}` > tconst is the unique identifier
  + `GET` - Gets the title of piece given the ID, and returns information about that title
  + `PUT` - Updates the title of a piece given the ID, and returns that updated information based on ID
  + `DELETE` - Deletes the piece of media given the ID, and returns the success/failure code
+ `/titles/{tconst}/people` > tconst is the unique identifier
  + `GET` - Gets the people in the dataset associated with the given title, and returns that list
+ `/titles/{tconst}/cast` > tconst is the unique identifier
  + `GET` - Gets the people in the dataset associated with the given title who act, and returns that list
+ `/titles/{tconst}/crew` > tconst is the unique identifier
  + `GET` - Gets the people in the dataset associated with the given title who participate behind the scenes, and returns that list

For more details, check out the [Swagger documentation](./api/swagger.yml).