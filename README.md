# Clematis Money Tracker Restful API 
  
[![License](https://img.shields.io/badge/License-GPLv2%202.0-blue.svg)](https://www.gnu.org/licenses/gpl-3.0.html)
[![Docker Image CI](https://github.com/grauds/money.tracker.api/actions/workflows/docker-image.yml/badge.svg)](https://github.com/grauds/money.tracker.api/actions/workflows/docker-image.yml)
[![Docker](https://github.com/grauds/money.tracker.api/actions/workflows/docker-publish.yml/badge.svg)](https://github.com/grauds/money.tracker.api/actions/workflows/docker-publish.yml)

## About

This is a Spring Data REST based Restful API service for database from MoneyTracker application (https://dominsoft.ru/). It uses [Clematis Keycloak Auth API](https://github.com/grauds/clematis.auth.api) to secure its endpoints. 

## Quick Start

Checkout the code
```
git clone https://github.com/grauds/money.tracker.api.git
```                                                            
The database file is a Firefox 2.5.9 database, so any data file from MoneyTracker application is possible to use with this web application. To do this, change the following line `/home/firebird/db` in the Docker compose sample file included into this repository with a path to actual directory on the server filesystem where a custom database file is stored:
```
volumes:
   - /home/firebird/db:/firebird/data
```
Set executable bit to gradlew
```
chmod +x gradlew
```

The following command builds the entire project

```
./gradlew clean build
```

To pack the application into a Docker container run the Docker build
```
docker build -t money.tracker.api .
```
The application is meant to be deployed with Docker compose along with Firefox 2.5.9-sc database as a dependency. The suggested Docker compose configuration can be found [here](https://github.com/grauds/money.tracker.api/blob/master/jenkins/docker-compose.yml). There are many environment variables defined in the [.env](https://github.com/grauds/money.tracker.api/blob/master/jenkins/.env) file, but if another environment needs to be configured, just replace the file with the actual one.

## Development

Current version of this application doesn't repeat the functionality of MoneyTracker but adds more insight into data. Therefore the database is accessed is readonly mode, with sophisticated native queries. There is an idea to convert that queries into stored procedures to further improve performance and use more features from Firebird 2 as there is no need to switch to another database version or vendor.

### Firebird Database Version

Unfortunately, MoneyTracker application is discontinued so there is no hope it will use Firebird 3, 4 or 5 version any time soon.

### Currency Exchange Rates

There is one requirement for the database to have a continuous history of exchange rates for all the currencies within the years of budget. This is an optional operation in MoneyTracker application, but it helps to calculate cross rates for any date.

