import com.fasterxml.jackson.annotation.JsonProperty
import retrofit2.http.GET
import retrofit2.http.Query


object Saucenao {

    val saucenaoClient: SaucenaoApi = HttpClientFactory.getHttpClient(
        Config.proxy.let { if (it.isEnable) it.proxyUri else null }, "https://saucenao.com"
    ).create(SaucenaoApi::class.java)
}

interface SaucenaoApi {

    @GET("/search.php")
    suspend fun search(
        @Query("url") url: String,
        @Query("db") db: Int = 999,
        @Query("numres") numres: Int = 10,
        @Query("api_key") apiKey: String = Config.saucenao.apikey,
        @Query("output_type") outputType: Int = 2
    ): Resp
}

data class Resp(
    val results: List<Result>
)


data class Result(
    val header: Header,
    val data: Data,
)


data class Header(
    val similarity: String,
    val thumbnail: String,
)

data class Data(
    val source: String? = null, @JsonProperty("ext_urls") val extUrls: List<String>? = null
)