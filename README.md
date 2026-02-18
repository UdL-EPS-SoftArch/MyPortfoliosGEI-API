# MyPortfolios GEI API

Template for a Spring Boot project including Spring REST, HATEOAS, JPA, etc. Additional details: [HELP.md](HELP.md)

[![Open Issues](https://img.shields.io/github/issues-raw/UdL-EPS-SoftArch/spring-template?logo=github)](https://github.com/orgs/UdL-EPS-SoftArch/projects/12)
[![CI/CD](https://github.com/UdL-EPS-SoftArch/spring-template/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/UdL-EPS-SoftArch/spring-template/actions)
[![CucumberReports: UdL-EPS-SoftArch](https://messages.cucumber.io/api/report-collections/faed8ca5-e474-4a1a-a72a-b8e2a2cd69f0/badge)](https://reports.cucumber.io/report-collections/faed8ca5-e474-4a1a-a72a-b8e2a2cd69f0)
[![Deployment status](https://img.shields.io/uptimerobot/status/m792691238-18db2a43adf8d8ded474f885)](https://spring-template.fly.dev/users)

## Vision

**For** ... **who** want to ...
**the project** ... **is an** ...
**that** allows ...
**Unlike** other ...

## Features per Stakeholder

| CREATOR                                   | ADMIN                | ANONYMOUS                    |
|-------------------------------------------|----------------------|------------------------------|
| Register                                  | Add Admin            | View public creator profiles |
| Login                                     | Login                | List public portfolios       |
| Logout                                    | Logout               | List portfolio projects      |
| Edit profile                              | Suspend Creator      | List project content         |
| Create portfolio                          | List flagged content | Search public content        |
| Edit portfolio                            | Remove content       | Report public content        |
| Create project                            |                      |                              |
| Edit project                              |                      |                              |
| Add content                               |                      |                              |
| Edit content                              |                      |                              |
| Create tag                                |                      |                              |
| Tag content                               |                      |                              |
| Set public / private / restricted         |                      |                              |
| Share restricted with user                |                      |                              |
| List public and shared portfolios         |                      |                              |
| List public and shared portfolio projects |                      |                              |
| List public and shared project content    |                      |                              |
| Search public and shared content          |                      |                              |
| Report content                            |                      |                              |

## Entities Model

```mermaid
classDiagram
    class User {
        <<abstract>>
        username : String
        password : String
        email : String
    }

    class UserDetails {
        <<interface>>
    }

    User ..|> UserDetails

    class Creator
    class Admin
    class SuperAdmin

    User <|-- Creator
    User <|-- Admin
    Admin <|-- SuperAdmin

    class Profile
    User "1" --> "1" Profile : owns

    class Portfolio {
        id : String
        name : String
        description : String
        visibility : Enum
    }

    class Project {
        id : String
        name : String
        description : String
        flagged : Bool
        visibility : Enum
    }

    class Assets {
        id : String
        name : String
        description : String
    }

    class Tag {
        name : String
    }

    class Status {
        # Provisional
    }

    class Collaborator {
        actions : Edit, View, Remove
    }

    Creator "1" --> "*" Portfolio : creates
    Portfolio "1" --> "1..*" Project : has
    Project "1" --> "1..*" Assets : has

    Creator "1" --> "*" Project : creates
    Creator "1" --> "*" Assets : uploads / edits / deletes

    Project "*" --> "*" Tag : tagged with
    Tag "*" --> "*" Project : moderates

    Project "*" --> "*" Collaborator : collaborates

    Project --> Status : has
```
