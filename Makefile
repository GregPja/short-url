migrate:
	./gradlew flywayMigrate

down:
	docker-compose down

db-up:
	docker-compose up -d db
	sleep 4

jar:
	./gradlew bootJar

jooq:
	./gradlew generateJooq

init: db-up migrate jooq

start: init jar
	docker-compose up short-url