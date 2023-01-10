package greg.pja.shorturl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class ShortUrlApplication

fun main(args: Array<String>) {
    runApplication<ShortUrlApplication>(*args)
}
