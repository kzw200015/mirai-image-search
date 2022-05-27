import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

object ImageSearchPlugin : KotlinPlugin(
    JvmPluginDescription.loadFromResource()
) {

    override fun onEnable() {
        Config.reload()
        SearchCommand.register()
    }

    override fun onDisable() {
        SearchCommand.unregister()
    }
}