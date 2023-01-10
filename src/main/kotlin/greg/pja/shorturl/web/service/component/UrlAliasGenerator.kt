package greg.pja.shorturl.web.service.component

import org.springframework.stereotype.Component
import org.springframework.util.DigestUtils

interface UrlAliasGenerator {
    fun generate(url: String): String
}

@Component
class MD5UrlAliasGenerator : UrlAliasGenerator {
    // why MD5? Because we don't really care about security in this case, so any hashing algorithm is fine
    override fun generate(url: String): String {
        return DigestUtils.md5DigestAsHex(url.toByteArray())
    }
}