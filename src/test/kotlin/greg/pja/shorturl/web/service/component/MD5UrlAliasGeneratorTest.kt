package greg.pja.shorturl.web.service.component

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class MD5UrlAliasGeneratorTest {
    private val aliasGenerator = MD5UrlAliasGenerator()

    @Test
    fun `when string is passed hashed md5 version is returned`() {
        val hash = aliasGenerator.generate("anything.it.doesn/really-matter")
        // I checked online ;)
        assertThat(hash, `is`("b9b8e4d21e5df66cbaf514c86955045b"))
    }
}