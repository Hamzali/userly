# USERLY

A spring boot application for tracking user views 

H2 is used for storage


## How tos

- use ``

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





