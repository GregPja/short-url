migrate:
	./gradlew flywayMigrate

down:
	docker-compose down

db-up:
	docker-compose up -d db

jar:
	./gradlew bootJar

jooq:
	./gradlew generateJooq

init: db-up migrate jooq