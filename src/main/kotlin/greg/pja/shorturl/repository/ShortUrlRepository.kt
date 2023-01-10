package greg.pja.shorturl.repository

import greg.pja.jooq.generated.Tables
import greg.pja.jooq.generated.tables.records.ShortUrlRecord
import greg.pja.shorturl.entity.ShortUrl
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime.now

interface ShortUrlRepository {
    fun create(short: String, url: String): String
    fun findByShort(short: String): ShortUrl?
    fun findByUrl(url: String): ShortUrl?
}

@Repository
class ShortUrlRepositoryImpl(
    private val dslContext: DSLContext
) : ShortUrlRepository {
    private val table = Tables.SHORT_URL

    override fun create(short: String, url: String): String {
        return short.also {
            dslContext.insertInto(table)
                .set(
                    ShortUrlRecord(
                        short,
                        url,
                        now(),
                        null
                    )
                ).execute()
        }
    }

    override fun findByShort(short: String): ShortUrl? {
        return dslContext.select()
            .from(table)
            .where(table.SHORT.eq(short))
            .fetchOneInto(ShortUrl::class.java)
    }

    override fun findByUrl(url: String): ShortUrl? {
        return dslContext.select()
            .from(table)
            .where(table.URL.eq(url))
            .fetchOneInto(ShortUrl::class.java)
    }

}