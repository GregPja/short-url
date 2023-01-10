package greg.pja.shorturl.web

import greg.pja.shorturl.web.service.ImpossibleToGenerateShortUrlException
import greg.pja.shorturl.web.service.ShortUrlNotFoundException
import greg.pja.shorturl.web.service.ShortUrlService
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@ActiveProfiles(profiles = ["test"])
@SpringBootTest
class ShortUrlControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
) {
    @SpyBean
    lateinit var shortService: ShortUrlService

    @Test
    fun `when no url can be found 404 is returned`() {
        doThrow(ShortUrlNotFoundException("")).`when`(shortService).getUrl(any())
        mockMvc.perform(
            get("/api/short/shorty")
        ).andExpect(status().isNotFound)
    }

    @Test
    fun `when no short can be generated 400 is returned`() {
        doThrow(ImpossibleToGenerateShortUrlException("")).`when`(shortService).createShortUrlOrGetExisting(any())
        mockMvc.perform(
            post("/api/short")
                .asJson()
                .content(
                    "www.google.com"
                )
        ).andExpect(status().isBadRequest)
    }


    @Test
    fun `when url is found 200 and url are returned`() {
        doAnswer { "a-very-very-very-very-long-url" }.`when`(shortService).getUrl(any())
        mockMvc.perform(
            get("/api/short/shorty")
        ).andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$").value("a-very-very-very-very-long-url"))
    }

    @Test
    fun `when short is generated 200 and short are returned`() {
        doAnswer { "shrt" }.`when`(shortService).createShortUrlOrGetExisting(any())
        mockMvc.perform(
            post("/api/short")
                .asJson()
                .content(
                    "www.google.com"
                )
        ).andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$").value("shrt"))
    }

    fun MockHttpServletRequestBuilder.asJson(): MockHttpServletRequestBuilder {
        return this.header("Content-Type", "application/json")
    }
}