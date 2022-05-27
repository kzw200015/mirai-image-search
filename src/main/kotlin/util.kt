import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

object ImageUtil {

    private val client = Retrofit.Builder().baseUrl("http://localhost/").build().create(DownloadApi::class.java)

    suspend fun downloadAsImage(url: String, contact: Contact): Image {
        return client.download(url).byteStream().use {
            it.uploadAsImage(contact)
        }
    }
}

interface DownloadApi {

    @Streaming
    @GET
    suspend fun download(@Url url: String): ResponseBody
}
