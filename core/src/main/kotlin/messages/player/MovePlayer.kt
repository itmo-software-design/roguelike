package messages.player

import messages.Message

/**
 *
 * @author sibmaks
 * @since 0.0.1
 */
data class MovePlayer(val direction: MoveDirection) : Message
