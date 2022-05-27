import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {

    val saucenao by value(SaucenaoConfig())
    val proxy by value(ProxyConfig())
}

@Serializable
data class ProxyConfig(val isEnable: Boolean = false, val proxyUri: String = "")

@Serializable
data class SaucenaoConfig(val apikey: String = "")