import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URI

object HttpClientFactory {

    fun getHttpClient(proxyUri: String? = null, baseUrl: String = "http://locahost/"): Retrofit {
        val okHttpClient = OkHttpClient.Builder().apply {
            proxyUri?.let { proxy(parseProxyUrl(it)) }
        }.build()
        val converterFactory = JacksonConverterFactory.create(
            jacksonMapperBuilder().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).build()
        )
        return Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient).addConverterFactory(converterFactory).build()
    }

    private fun parseProxyUrl(proxyUrl: String): Proxy {
        val uri = URI(proxyUrl)
        val type = when (uri.scheme) {
            "socks5" -> Proxy.Type.SOCKS
            "http" -> Proxy.Type.HTTP
            else -> Proxy.Type.DIRECT
        }
        return Proxy(type, InetSocketAddress(uri.host, uri.port))
    }
}