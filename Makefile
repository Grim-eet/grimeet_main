.PHONY: build test clean fclean docker-build docker-network docker-up docker-down run re

# Build the Gradle project (skip tests)
build:
	./gradlew build -x test

# Run tests
test:
	./gradlew test

# Clean the Gradle project
clean:
	./gradlew clean

# Force clean: clean, remove build, stop & remove containers, delete image & network
fclean: clean docker-down
	rm -rf build
	docker rmi -f grimeet_main-app || true
	docker network rm app-network || true

# Build Docker image (ensure JAR is built first)
docker-build: build
	docker build -t grimeet_main-app .

# Create Docker network if not exists
docker-network:
	docker network create app-network || true

# Start Docker containers with all compose files
docker-up: docker-network docker-build
	docker-compose -f docker-compose-db.yml -f docker-compose-redis.yml -f docker-compose-app.yml up -d

# Stop Docker containers (and remove volumes if any)
docker-down:
	docker-compose -f docker-compose-db.yml -f docker-compose-redis.yml -f docker-compose-app.yml down -v

# Redeploy: down, build image, up
re: docker-down docker-build docker-up

# Run the application using Gradle BootRun
run:
	./gradlew bootRun

# Check logs of the main app container
logs:
	docker logs -f grimeet_main-app