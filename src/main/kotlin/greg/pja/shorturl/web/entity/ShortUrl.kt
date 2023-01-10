package greg.pja.shorturl.web.entity

import java.time.OffsetDateTime

data class ShortUrl(
    val short: String,
    val url: String,
    val createdAt: OffsetDateTime,
    val lastAccess: OffsetDateTime?
)
