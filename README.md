
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
**Error**
```
{
    "message",
    "status"
}
```
