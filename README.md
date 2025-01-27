
# INotes Backend PROJECT
Backend to manage notes of INotes App

* Spring Boot 3
* Java 21
* Maven 3.9.5
* Docker
* Postgresql

``server.port=8080``

## EndPoints

### Auth
**(POST) SignUp**

``http://localhost:8080/v1/auth/signup``

*Request Body (JSON)*
```
{
    "username": "jodemvel",
    "email": "jodemvel@gmail.com",
    "password": "jodemvel",
    "roles": ["ADMIN"]
}
```

**(POST) LogIn**

``http://localhost:8080/v1/auth/login``

*Request Body (JSON)*
```
{
    "email": "jacks@gmail.com",
    "password": "1234"
}
```

### Notes

**(POST) Create**

``http://localhost:8080/v1/notes``

*Request Body (JSON)*
```
{
    "title": "Note Example",
    "content": "Some Content",
    "isArchived": false
}
```

**(PUT) Update**

``http://localhost:8080/v1/notes/{id}``

*Request Body (JSON)*
```
{
    "content": "Change Value"
}
```

**(GET) Get Notes**

``http://localhost:8080/v1/notes?archived=false``

*No Request Body*

**(GET) Get Notes**

``http://localhost:8080/v1/notes/{id}``

*No Request Body*

**(DELETE) Delete Note**

``http://localhost:8080/v1/notes/{id}``

*No Request Body*

**(PATCH) Archive/Unarchive Note**

``http://localhost:8080/v1/notes/{id}``

*No Request Body*


_____________________________
### Response Format
**Correct Auth**
```
{
    "username",
    "message",
    "jwt",
    "status"
}
```

**Note**
```
{
    "id": 29,
    "title": "Note Example",
    "content": "Some Content",
    "userId": 1,
    "archived": false
}
```
**Error**
```
{
    "message",
    "status"
}
```
