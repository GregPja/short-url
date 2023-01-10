package greg.pja.shorturl.repository

import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class ShortUrlRepositoryImplTest @Autowired constructor(
    private val shortUrlRepo: ShortUrlRepositoryImpl
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
}