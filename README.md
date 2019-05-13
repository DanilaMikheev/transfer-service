# Transfer-service
Billing RESTful webservice for processing transfers.

#### Modules
* [app]() - assembly module that initialize Jersey http server, inject service beans, initialize H2 db, creates application uber jar.
* [dao]() - dao module provides operations with Account and Transfer entities. Module includes manual transfer transaction management.
* [core]() - transfer service implementation. Service process transfers concurrently if transfer didn't conduct transaction, then it creates new runnable task to repeat the transfer (no more than 5 times)
* [web]() - web module. Contains rest controller implementation that includes custom @accountId annotation validation.

#### Frameworks and libraries
* [Java 11]()
* [Jersey http server]()
* [Apache commons]()
* [Lombok]()
* [Gradle]()
* [H2]()

#### Running the tests
1. Download or clone the project.
2. Change to the root project directory.
3. Run tests using gradle wrapper:
```bash
./gradlew tests
```
##### About tests
All modules except [web]() contain tests. App module contains integration tests and stress tests. Stress tests passed if 300 concurrently transfers from one account to other processed successfully.

#### Running instructions
1. Download or clone the project.
2. Change to the root project directory.
3. Build the project using gradle wrapper:
```bash
./gradlew :app:jar
```
On windows use
```bash
gradlew.bat :app:jar
```
4. Change to the <project root>/app/build/libs
5. Run the jar:
```bash
  java -jar transfer-service-0.1.0.jar
  ```
If something isn't working or you have any questions, feel free to contact me.

#### Future improvements
I recommend to separate one huge debit+credit transaction on two small transactions. First holds money on sender account then second try to add money to receiver account. This improvement will reduce number of locks and locking time duration in account table.

#### API documentation
* **URL**

  api/transfers

* **Method:**

    `POST` 
  
* **Data Params**

- [clientid]() - account owner id
- [fromAcc]()  - sender accountId 20 numeric symbols. clientId must be owner of the fromAcc account.
- [senderAcc]()  - 20 numeric symbols.
- [amount]()  - transfer amount in cents (long value).
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ uid : d9bbad7e0cea42b9bcd933ae524230db }`
 
* **Error Response:**

  * **Code:** 403 FORBIDDEN <br />
    **Content:** `{ error : "Operation not avalible for clientId 7" }`

  OR

  * **Code:** 409 CONFLICT <br />
    **Content:** `{ error : "Not enought funds, avalible 1500, required 2000" }`

* **Sample Call:**

`{
	"clientId": 1,
	"from": "40817810123456789011",
	"to": "40817810123456789012",
	"amount": 100
}`

* **URL**

  api/transfers/{id}

* **Method:**

    `GET` 
  
* **URL Params**
   **Required:**
 
   `id=[uid]`

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ transferStatus : 1 }`
 
* **Error Response:**

  * **Code:** 404 NOT_FOUND <br />
    **Content:** `{ error : "Transfer with id d9bbad7e0cea42b9bcd933ae524230db not exists" }`

* **Sample Call:**

    `api/transfers/d9bbad7e0cea42b9bcd933ae524230db`

