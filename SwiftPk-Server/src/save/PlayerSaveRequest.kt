package kotlinsave.save

import com.twisted.game.world.entity.mob.player.Player

/**
 * @author Jire
 */
data class PlayerSaveRequest(
    val player: Player,
    val timestamp: Long,
    val whenComplete: Runnable?,
)
