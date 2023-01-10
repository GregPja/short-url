package greg.pja.shorturl.web.service

open class ShorUrlServiceException(message: String) : RuntimeException(message)

class ShortUrlNotFoundException(shortUrl: String) : ShorUrlServiceException("Impossible to find short url $shortUrl")

class ImpossibleToGenerateShortUrlException(url: String) :
    ShorUrlServiceException("Impossible to generate short url for $url")