package greg.pja.shorturl.web.service

import greg.pja.jooq.generated.Tables
import greg.pja.shorturl.web.configuration.ShortUrlConfiguration
import greg.pja.shorturl.web.repository.ShortUrlRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = ["test"])
class ShortUrlServiceImplIntegrationTest @Autowired constructor(
    private val service: ShortUrlServiceImpl,
    private val config: ShortUrlConfiguration,
    private val repository: ShortUrlRepository,
    private val dslContext: DSLContext
) {

    @AfterEach
    fun cleanDb() {
        // why this and not just Transactional? As Transactional in tests tries to rollback data, seeing as there was an
        // exception it will not really know what to rollback and will fail
        dslContext.truncate(Tables.SHORT_URL).execute()
    }

    @Test
    fun `when no other url exist the short url is as long as the minimum`() {
        val short = service.createShortUrl("nice-url")
        assertThat(short.length, `is`(config.minLength))
    }

    @Test
    fun `when there is a clash shortest url is one character bigger`() {
        repository.create("c9", "something-something")
        // nice-url md5 is c9a... so I check that it's equals to c9 + a
        val short = service.createShortUrl("nice-url")
        assertThat(short, `is`("c9a"))
    }

}