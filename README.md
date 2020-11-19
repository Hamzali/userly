# USERLY

A spring boot application for tracking user views 

H2 is used for storage

## How tos

- make sure `java 15` and `maven` are installed.

- use `./mvnw spring-boot:run` to run project.

- you can reach swagger documentation from `http://localhost:8080/swagger-ui.html`

## Application Designs

- `NOT include more than 20 items`; 
Pagination used for keeping user view list size at 20 for every response
-  `NOT include views older than 30 days`;
This is achieved by asynchronously check the oldest records time and if it is expired delete all expired records 


### User Entity
| id | name |
|----| ---- |
| Long| String |

- `id` is primary key keeps registered users.

### UserView Entity
| id | viewer_id | viewed_id | viewed_at |
|----| ---- | ----- | ---|
| Long| Long | Long | Long |  

- `id` is the primary key keeping order for creation.

### High Load Assumption

In the case of a high load for this project, I would split the stateless part which is saving and fetching views and stateful part which always keeps database cleaned via running periodically.
This way I could scale stateful and stateless parts of my app individually.
 

### Questions

- Do you delete any data from the database?
`Answer:` Yes, because data older than 30 days are unused and needs to be clean

- Do you have any periodic task type of batch jobs to maintain data?
`Answer:` No, because I cleaned database by caching the oldest value and if it is old enough I remove old data asynchronously on user view creation