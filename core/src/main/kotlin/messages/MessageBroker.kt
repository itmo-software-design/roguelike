package messages

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
object MessageBroker {
    private val subscribers = ConcurrentHashMap<String, MutableSet<Subscriber>>()

    fun subscribe(topic: String, subscriber: Subscriber) {
        subscribers.computeIfAbsent(topic) { CopyOnWriteArraySet() }.add(subscriber)
    }

    fun unsubscribe(topic: String, subscriber: Subscriber) {
        subscribers[topic]?.remove(subscriber)
    }

    fun send(topic: String, message: Message) {
        subscribers[topic]?.forEach { it.onMessage(message) }
    }
}
