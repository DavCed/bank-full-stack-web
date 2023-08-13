# Bank Web
## Description
A full stack banking application developed with Typescript in Angular 14 and Java in Spring Boot 2.

## Features
1. Register as Customer or Employee
2. Login as Customer or Employee
3. Register bank accounts as Customer
4. Approve/Deny bank accounts as Employee
5. Make transactions on bank account as a Customer
6. View owned bank accounts as Customer
7. View all bank accounts as Employee

## Frontend App
### Installation
1. Set endpoints in **_src/environments/environments.ts_**
  
2. Run npm commands
```shell
npm install -f
npm start
```

## Backend App
**_NOTE:_** Microservice Archectiture
### Installation
1. Download and configure [Flyway](https://documentation.red-gate.com/fd/configuration-files-184127472.html)

2. Set variables in **_src/main/resources/application.properties_** for:
- Eureka-Server  
- Users-micro
- Accounts-micro
      
3. Run Eureka-Server micro app

4. Run the other 2 micro apps
