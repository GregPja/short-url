package greg.pja.shorturl.web.service

import greg.pja.shorturl.web.configuration.ShortUrlConfiguration
import greg.pja.shorturl.web.repository.ShortUrlRepository
import greg.pja.shorturl.web.service.component.UrlAliasGenerator
import io.micrometer.core.instrument.MeterRegistry
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.doAnswer
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class ShortUrlServiceImplUnitTest {

    private val shortUrlRepository = mock<ShortUrlRepository>()
    private val aliasGenerator = mock<UrlAliasGenerator>()
    private val metric = mock<MeterRegistry>()

    private val service = ShortUrlServiceImpl(
        shortUrlRepository = shortUrlRepository,
        aliasGenerator = aliasGenerator,
        config = ShortUrlConfiguration(
            minLength = 1,
            maxLength = 3
        ),
        metric
    )

    @Test
    fun `when short url reaches max length exception is raised`() {
        doAnswer { false }.`when`(shortUrlRepository).create(any(), any())
        doAnswer { "CRAZY-AMAZING-SHORT" }.`when`(aliasGenerator).generate(any())
        assertThrows<ImpossibleToGenerateShortUrlException> { service.createShortUrl("url") }
    }

    @Test
    fun `when url does not exist exception is raised`() {
        doAnswer { null }.`when`(shortUrlRepository).findByShort(any())
        assertThrows<ShortUrlNotFoundException> {
            service.getUrl("short")
        }
    }
}