# Start the application in dev mode
dev-mode:
	mvn clean compile quarkus:dev

# Run the tests
test:
	mvn clean install

# Package the application in a jar
package:
	mvn package -DskipTests

# Start the database container
start-db:
	docker-compose \
	-f infra/docker-compose.yml \
	up -d --build postgres

# Starts the database and application in Docker containers
start-all: package
	docker-compose \
	-f infra/docker-compose.yml \
	up -d --build

# Stops the application and database containers
stop-all:
	docker-compose \
    -f infra/docker-compose.yml \
    down