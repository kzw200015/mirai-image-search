import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.UserCommandSender
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.nextEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import retrofit2.HttpException
import kotlin.time.Duration.Companion.seconds

object SearchCommand : SimpleCommand(ImageSearchPlugin, "搜图") {

    private const val replyTimeout = 10

    @Handler
    suspend fun UserCommandSender.handle(imageArg: Image? = null) {
        val image: Image = imageArg ?: run {
            subject.sendMessage("请在${replyTimeout}秒内发送图片")
            try {
                withTimeout(replyTimeout.seconds) {
                    val imageEvent = globalEventChannel().nextEvent<MessageEvent> { it.sender.id == user.id }
                    imageEvent.message.firstIsInstance()
                }
            } catch (e: NoSuchElementException) {
                sendMessage("未解析到图片")
                null
            } catch (e: TimeoutCancellationException) {
                sendMessage("超时，请重新输入指令，并在${replyTimeout}秒内发送图片")
                null
            }
        } ?: return

        val url = image.queryUrl()
        ImageSearchPlugin.logger.debug("接收图片链接:$url")
        try {
            val results =
                Saucenao.saucenaoClient.search(url).results.sortedByDescending { it.header.similarity.toFloat() }
                    .take(3)
            val resultMsgs = results.map { result ->
                val textBuilder = StringBuilder()
                result.data.source?.let {
                    textBuilder.appendLine(it)
                }
                result.data.extUrls?.forEach { textBuilder.appendLine(it) }
                textBuilder.appendLine("相似度：${result.header.similarity}%")
                PlainText(textBuilder.toString()) + ImageUtil.downloadAsImage(
                    result.header.thumbnail, subject
                )
            }

            val forwardMsg = buildForwardMessage(subject) {
                add(bot, At(user))
                resultMsgs.forEach { add(bot, it) }
            }
            sendMessage(forwardMsg)
        } catch (e: NoSuchElementException) {
            sendMessage("未找到结果")
        } catch (e: HttpException) {
            sendMessage(e.localizedMessage)
        }
    }
}