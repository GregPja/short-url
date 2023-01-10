package greg.pja.shorturl.entity

import java.time.OffsetDateTime

data class ShortUrl(
    val short: String,
    val url: String,
    val createdAt: OffsetDateTime,
    val lastAccess: OffsetDateTime?
)
