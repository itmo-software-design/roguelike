package messages

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
fun interface Subscriber {
    fun onMessage(message: Message)
}
