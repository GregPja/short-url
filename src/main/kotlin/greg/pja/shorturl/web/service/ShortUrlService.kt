package greg.pja.shorturl.web.service

import greg.pja.shorturl.web.configuration.ShortUrlConfiguration
import greg.pja.shorturl.web.repository.ShortUrlRepository
import greg.pja.shorturl.web.service.component.UrlAliasGenerator
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.Integer.min

interface ShortUrlService {
    fun createShortUrl(url: String): String
    fun getUrl(short: String): String
}

@Service
class ShortUrlServiceImpl(
    private val shortUrlRepository: ShortUrlRepository,
    private val aliasGenerator: UrlAliasGenerator,
    private val config: ShortUrlConfiguration,
    metrics: MeterRegistry
) : ShortUrlService {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val clashesCounters = (config.minLength..config.maxLength).associateWith {
        metrics.counter("short.url.size.$it.clash")
    }

    override fun createShortUrl(url: String): String {
        // if we have it, we return it. It's ok if the creation of an url is "slower"
        return shortUrlRepository.findByUrl(url)?.short ?: let {
            val longestShortUrl = aliasGenerator.generate(url)
            createShortestUrlPossible(url, longestShortUrl)
        }
    }

    private fun createShortestUrlPossible(url: String, longestShortUrl: String): String {
        for (index in (config.minLength..min(config.maxLength, longestShortUrl.length))) {
            val shortestUrl = longestShortUrl.substring(0, index)
            val success = shortUrlRepository.create(
                short = shortestUrl,
                url = url
            )
            if (success) {
                return shortestUrl
            } else {
                logger.info("Clashed happened while generating shortest url with size $index")
                // to keep track of how many clashes we have for each length. So we can monitor stuff
                clashesCounters[index]?.increment()
            }
        }
        throw ImpossibleToGenerateShortUrlException(url)
    }

    override fun getUrl(short: String): String {
        return shortUrlRepository.findByShort(short)?.url?.also {
            // if we notice this gets too slow, we can also consider going a little more low level and create a function to retrieve and update the field. But this is an MVP
            shortUrlRepository.updateLastAccessedByShort(short)
        } ?: throw ShortUrlNotFoundException(short)
    }

}