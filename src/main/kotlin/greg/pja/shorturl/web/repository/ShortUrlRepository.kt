package greg.pja.shorturl.web.repository

import greg.pja.jooq.generated.Tables
import greg.pja.jooq.generated.tables.records.ShortUrlRecord
import greg.pja.shorturl.web.entity.ShortUrl
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime.now

interface ShortUrlRepository {
    fun create(short: String, url: String): Boolean
    fun findByShort(short: String): ShortUrl?
    fun findByUrl(url: String): ShortUrl?
    fun updateLastAccessedByShort(short: String)
}

@Repository
class ShortUrlRepositoryImpl(
    private val dslContext: DSLContext
) : ShortUrlRepository {
    private val table = Tables.SHORT_URL

    override fun create(short: String, url: String): Boolean {
        return dslContext.insertInto(table)
            .set(
                ShortUrlRecord(
                    short,
                    url,
                    now(),
                    null
                )
            ).onConflictDoNothing()
            .execute() == 1
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

    override fun updateLastAccessedByShort(short: String) {
        dslContext.update(table)
            .set(table.LAST_ACCESS, now())
            .execute()
    }
}