package greg.pja.shorturl.repository

import greg.pja.shorturl.web.repository.ShortUrlRepositoryImpl
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNull
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class ShortUrlRepositoryImplTest @Autowired constructor(
    private val shortUrlRepo: ShortUrlRepositoryImpl,
) {

    @TestFactory
    fun `when no url exist null is returned`() =
        listOf(
            shortUrlRepo::findByShort,
            shortUrlRepo::findByUrl
        ).map { repoFunction ->
            dynamicTest("when short url exist ${repoFunction.name} returns null") {
                assertThat(repoFunction("anything"), IsNull())
            }
        }

    @Test
    fun `when entity exist can be found by short`() {
        shortUrlRepo.create("short", "url")
        assertThat(shortUrlRepo.findByShort("short"), not(IsNull()))
    }

    @Test
    fun `when entity exist can be found by url`() {
        shortUrlRepo.create("short", "url")
        assertThat(shortUrlRepo.findByUrl("url"), not(IsNull()))
    }

    @TestFactory
    fun `when same key is created exception false is returned`() =
        listOf(
            { shortUrlRepo.create("same-short", UUID.randomUUID().toString()) },
            { shortUrlRepo.create(UUID.randomUUID().toString().substring(0, 5), "same-url") }
        ).map { creationFunction ->
            dynamicTest("when the same key exist twice exception is raised") {
                assertThat(creationFunction(), `is`(true))
                assertThat(creationFunction(), `is`(false))
            }
        }

    @Test
    fun `last access is updated correctly`() {
        shortUrlRepo.create("short", "url")
        val entity = shortUrlRepo.findByShort("short")!!
        assertThat(entity.lastAccess, IsNull())
        shortUrlRepo.updateLastAccessedByShort("short")
        assertThat(shortUrlRepo.findByShort("short"), not(IsNull()))
    }
}