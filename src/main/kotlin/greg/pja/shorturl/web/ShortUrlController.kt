package greg.pja.shorturl.web

import greg.pja.shorturl.web.service.ImpossibleToGenerateShortUrlException
import greg.pja.shorturl.web.service.ShortUrlNotFoundException
import greg.pja.shorturl.web.service.ShortUrlService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/short")
class ShortUrlController(
    private val shortUrlService: ShortUrlService
) {

    @PostMapping("")
    fun generateOrRetrieveShortUrl(@RequestBody url: String): String {
        // TODO add a nice validation/transformation from string to actual URL
        return shortUrlService.createShortUrlOrGetExisting(url = url)
    }

    @GetMapping("{shortUrl}")
    fun getUrlByShort(@PathVariable shortUrl: String): String {
        return shortUrlService.getUrl(shortUrl)
    }

    @ExceptionHandler(ShortUrlNotFoundException::class)
    fun manageNotFound(exception: ShortUrlNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.message)

    @ExceptionHandler(ImpossibleToGenerateShortUrlException::class)
    fun manageVeryUnluckyClashes(exception: ImpossibleToGenerateShortUrlException) =
        ResponseEntity.badRequest().body(exception.message)


}