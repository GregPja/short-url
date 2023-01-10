package greg.pja.shorturl.web.service

import greg.pja.shorturl.web.configuration.ShortUrlConfiguration
import greg.pja.shorturl.web.entity.ShortUrl
import greg.pja.shorturl.web.repository.ShortUrlRepository
import greg.pja.shorturl.web.service.component.UrlAliasGenerator
import io.micrometer.core.instrument.MeterRegistry
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
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
        assertThrows<ImpossibleToGenerateShortUrlException> { service.createShortUrlOrGetExisting("url") }
    }

    @Test
    fun `when url does not exist exception is raised`() {
        doAnswer { null }.`when`(shortUrlRepository).findByShort(any())
        assertThrows<ShortUrlNotFoundException> {
            service.getUrl("short")
        }
    }

    @Test
    fun `when url exist last access is updated`() {
        doAnswer {
            mock<ShortUrl>().apply {
                `when`(url).then { "url" }
            }
        }.`when`(shortUrlRepository).findByShort(any())
        service.getUrl("short")
        verify(shortUrlRepository, times(1)).updateLastAccessedByShort(any())
    }
}