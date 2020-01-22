## Money Transfer API

Design and implement a RESTful API (including data model and the backing implementation) for
money transfers between accounts.

Explicit requirements:
1. You can use Java or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about
requirement #2 and keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require a
pre-installed container/server).
7. Demonstrate with tests that the API works as expected.

Implicit requirements:
1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.


### How to run
```sh
./gradlew run
```

### Endpoints

| METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| GET | /accounts | get all accounts | 
| GET | /accounts/{id} | get account by id | 
| POST | /accounts | create a new account |
| PUT | /accounts/{id}/deposit/{amount} | money deposit to account | 
| PUT | /accounts/{id}/withdraw/{amount} | money withdraw from account | 
| PUT | /accounts/transfer?accountFromId=&accountToId=&amount= | transfer money between accounts | 
| DELETE | /accounts/{id} | delete account by id | 
| GET | /owners | get all account owners | 
| GET | /owners/{id} | get account owner by id | 
| GET | /owners/{id}/accounts | get owner accounts by owner id | 
| POST | /owners | create a new owner | 
| DELETE | /owner/{id} | delete owner | 

### Entity examples
Account onwer
```json
{
"name":"new1",
"lastName":"new111"
}
```
Account
```json
{
"amount":100.00,
"currencyCode":"EUR",
"ownerId": 2
}
```
AccountTransfer
```json
{
"accountFromId":1,
"accountToId":2,
"amount": 50.00
}
```

#### Technogies Used
Java8, Micronaut, Gradle, JUnit5