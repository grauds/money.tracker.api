# Clematis Money Tracker REST API 
  
[![License](https://img.shields.io/badge/License-GPLv2%202.0-blue.svg)](https://www.gnu.org/licenses/gpl-3.0.html)

## About

This is a Spring Data REST API service for a database from MoneyTracker application (https://dominsoft.ru/). It uses [Keycloak](https://keycloak.org) to secure its endpoints. 

### Swagger

Swagger JSON is available at [here](./docs/swagger.json)


## Quick Start

### Check out the code
```
git clone https://github.com/grauds/money.tracker.api.git
```                                                            
The database file is **a Firefox 2.5.9 database**, any data file from the MoneyTracker application is possible to use with this web application; however, it poses a limit on database features, since this version is legacy now. 

### Update the database file
Change the following line `/home/firebird/db` in the Docker compose file included in this repository to an actual directory on the server filesystem where a custom database file is stored:
```
volumes:
   - /home/firebird/db:/firebird/data
```

### Build

The following command builds the entire project

```
./gradlew clean build
```

To pack the application into a Docker container, run the Docker build
```
docker build -t money.tracker.api .
```
The application is meant to be deployed with Docker compose along with a Firefox 2.5.9-sc database as a dependency. The suggested Docker compose configuration can be found [here](https://github.com/grauds/money.tracker.api/blob/master/jenkins/docker-compose.yml). 

## Development

The current version of this application doesn't repeat the functionality of MoneyTracker desktop application but adds more insight into data and some other features. The database with financial data is accessed is readonly mode. 

### Deprecation warning

Unfortunately, MoneyTracker application is discontinued, so there is no hope it will use Firebird 3, 4 or 5 version any time soon.

Also, this limitation freezes the version of Spring Boot to the latest in the 2.x branch due to the limitation of the Firebird JDBC driver.

### Currency Exchange Rates

There is one requirement for the database to have a continuous history of exchange rates for all the currencies within the years of budget. This is an optional operation in the MoneyTracker application, but it is required to calculate cross-rates.

