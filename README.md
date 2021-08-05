# Commit Lister

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application

This application includes a Makefile with the necessary commands to start the application.
Before trying out the application make sure that you have Maven and Docker installed, as well as jdk 8+.

To run the application we need to start the database using the included docker-compose file. You can use the makefile and run the following commands:
```shell script
# Run the database in a container, and the application locally
make start-db
make dev-mode
# Run both the database and application in Docker containers
make start-all
# You can stop all containers using the stop-all target of the makefile
make stop-all
```

> **_NOTE:_**  Quarkus ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.


## Solution

Commit lister is a concept github commit fetcher. It has two APIs to fetch commit information from GitHub. A CLI client which uses git, and a REST client which leverages Github's API. 

When data for a given repository is fetched, it is stored in a Postgres database, so subsequents requests don't have to tax the APIs again.

The default path uses Github's RESTful api, and the CLI API is used as a fallback.

It contains an healthcheck endpoint provided by SmallRye, which can be helpful when thinking of deployments to K8s environments.

To list the commits for a given repo, the application exposes a paginated endpoint which expects the owner and name of the repository. 
To call the endpoints, and check more information about the parameters and response, use the open-api documentation accessible at `http://localhost:8080/api-docs/`.

To take a look at all libs in use we can use the dev UI, which will show up the list of libraries, as well as a link to their documentation.

## Current limitations

The current solution has a few limitations due to time constraints:

* The number of commits fetched from each repository was set to 500. This was done to both simplify the process, and because unauthenticated access to GitHub's API is limited to 50 calls per hour.
* Timeouts can be configured. Fallback method calling can also be improved.
* Error codes should be improved.
* Tests include only a few cases, full coverage is still far.
