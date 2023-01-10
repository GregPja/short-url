# SHORT-URL

## How to run the project


To prepare the service simply run this command:
* `make init`
  * to set up the db, migration and generating jooq's classes


### Other commands

* Run the DB with docker-compose:
  * `make db-up`
* Apply migrations to DB
  * `make migrate`
* Generate jooq's classes
  * `make migrate`


### Troubleshooting

This project requires Java17 to run, in case the `make init` (or `make migrate`) command is not working for you and you see an error like this:

    Incompatible because this component declares a component compatible with Java 17 and the consumer needed a component compatible with Java 11

It means your terminal's java version is 11 (or lower). You can temporarily fix your Java version by typing:

```bash
export JAVA_HOME="PATH TO YOUR JAVA 17"
export PATH=$PATH:$JAVA_HOME
```

Otherwise, use your IDE's functionality to run gradle's tasks