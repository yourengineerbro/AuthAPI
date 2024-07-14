# AuthAPI

This module implements the services including:
1) Registering a user.
2) Login a user with his credentials and returning back a jwt token.
3) Refreshing the user token and invalidating the old one.
4) Revoking the user token.(In memory implementation)
5) Returning back the protected resources on valid token request.

### Starting the application:

```shell
docker compose up
```


### Use cases

**PLEASE RUN THESE COMMANDS IN BASH(EXAMPLE, GIT BASH), IF YOU ARE 0N WINDOWS.**

1) Using SignIn when user does not exist.
```shell
export TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/signin -H "Content-Type: application/json" -d '{"email": "test@example.com", "password": "password123"}')
```
```shell
echo $TOKEN
```
Output: Invalid credentials

2) Accessing protected resource with that result: ie, when TOKEN(after Bearer part) = Invalid credentials
 ```shell
curl -i -X GET http://localhost:8080/api/protected     -H "Authorization: Bearer $TOKEN"
```
Output: Invalid Token /The token was expected to have 3 parts, but got 0.

3) Creating new user.

```shell
curl -i -X POST http://localhost:8080/api/auth/signup -H "Content-Type: application/json" -d '{"email": "test@example.com","password": "password123"}'
```
Output: {"id":$USER_ID,"email":"test@example.com","password":"$2a$10$IFpEiAp8yO522u9W37ND7u.fL3mvzzrg9nlydo0RTQsUFYK1SR2R6"}

4) Trying login with wrong password:

```shell
export TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/signin -H "Content-Type: application/json" -d '{"email": "test@example.com", "password": "password1234"}')
```
```shell
echo $TOKEN
```

Output: Invalid credentials

5) Trying login with non-existing user:

```shell
export TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/signin -H "Content-Type: application/json" -d '{"email": "testtest@example.com", "password": "password123"}')
```
```shell
echo $TOKEN
```

Output: Invalid credentials

6) Trying login with existing user credentials:

```shell
export TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/signin -H "Content-Type: application/json" -d '{"email": "test@example.com", "password": "password123"}')
```
```shell
echo $TOKEN
```
Output: $TOKEN (for example, eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzIwMzQwMDM0LCJleHAiOjE3MjAzNDM2MzR9.V7ubAvRZHFUM1mHptXwb_Yuiv8R2MjpsT7VPFXdlOONfzHenmaCrPyVVvyd4vWSROU9rlhyFGtLHgkMkhHsKlQ)

7) Accessing protected resource with this token:

```shell
curl -i -X GET http://localhost:8080/api/protected     -H "Authorization: Bearer $TOKEN"
```

Output: This is a protected resource.

8) Refreshing token with distorted token(Invalid Token, distorted at the end):
```shell
export DISTORTEDTOKEN=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzIwMzQwMDM0LCJleHAiOjE3MjAzNDM2MzR9.V7ubAvRZHFUM1mHptXw
```
```shell
   command: curl -i -X POST http://localhost:8080/api/auth/refresh -H "Authorization: Bearer $DISTORTEDTOKEN"
 ```  
Output: Invalid Token

8) Refreshing token with valid token:
```shell
curl -i -X POST http://localhost:8080/api/auth/refresh -H "Authorization: Bearer $TOKEN"
```
**This will output the new token. 
Assign the value of NEWTOKEN to this value. Suppose the value is X. Then call:**

**export NEWTOKEN=X;**

9) Try accessing protected resource with old token:

```shell
curl -i -X GET http://localhost:8080/api/protected     -H "Authorization: Bearer $TOKEN"
```
Output: Invalid TOKEN

10) Try accessing protected resource with NEWTOKEN:

```shell
curl -i -X GET http://localhost:8080/api/protected     -H "Authorization: Bearer $NEWTOKEN"
```
Output: This is a protected resource.

11) Revoking NEWTOKEN:
```shell    
curl -i -X POST http://localhost:8080/api/auth/revoke   -H "Authorization: Bearer $NEWTOKEN"
```
Output: User Successfully Signed out

12) Accessing protected resource with the revoked token:
```shell
curl -i -X GET http://localhost:8080/api/protected     -H "Authorization: Bearer $NEWTOKEN"
```
Output: Invalid TOKEN

13) Revoking token If token already revoked,
```shell
curl -i -X POST http://localhost:8080/api/auth/revoke   -H "Authorization: Bearer $TOKEN"
```
Output: Invalid Token

### Way Forward:

- Authentication and Authorization part can be extracted out in a different microservice. Thus, can be exposed as gateway to our backend.

