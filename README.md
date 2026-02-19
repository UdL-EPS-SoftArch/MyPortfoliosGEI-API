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
        username : String
        password : String
        email : String
        role : Enum
    }

    class UserDetails {
        <<interface>>
    }

    User ..|> UserDetails

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

    User "1" --> "*" Portfolio : creates
    Portfolio "1" --> "1..*" Project : has
    Project "1" --> "1..*" Assets : has

    User "1" --> "*" Project : creates
    User "1" --> "*" Assets : uploads / edits / deletes

    %% Admin Moderation
    User "1" --> "*" Project : moderates (Admin/Superadmin)
    User "1" --> "*" Assets : moderates (Admin/Superadmin)

    %% Tagging
    Project "*" --> "*" Tag : tagged with

    %% Collaborator Flow
    User "1" --> "*" Collaborator : acts as
    Collaborator "*" --> "1" Project : assigned to

    Project --> Status : has
```
