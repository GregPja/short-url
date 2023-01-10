package greg.pja.shorturl.web.configuration

import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties("shortener-service")
data class ShortUrlConfiguration(
    val minLength: Int,
    val maxLength: Int
) {
    @PostConstruct
    fun checkValue() {
        if (minLength < 1) {
            throw Exception("minimum length can not be smaller than 1. Current $minLength")
        }
        if (maxLength < minLength) {
            throw Exception("well, no need to explain")
        }
    }
}
