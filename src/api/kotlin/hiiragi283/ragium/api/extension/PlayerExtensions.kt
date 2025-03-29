package hiiragi283.ragium.api.extension

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import kotlin.math.pow
import kotlin.math.roundToInt

//    Player    //

fun Entity?.asPlayer(): Player? = this as? Player

fun Player?.asServerPlayer(): ServerPlayer? = this as? ServerPlayer

//    Experience    //

/**
 * @see [com.enderio.base.common.util.ExperienceUtil.getXpNeededForNextLevel]
 */
fun getExpAmountFromLevel(level: Int): Int = when {
    level >= 30 -> 112 + (level - 30) * 9
    level >= 15 -> 37 + (level - 15) * 5
    else -> 7 + level * 2
}

fun getExpLevelFromAmount(amount: Int): Int {
    var level = 0
    while (true) {
        val minAmount: Int = getExpAmountFromLevel(level)
        if (minAmount > amount) {
            return level - 1
        }
        level++
    }
}

fun getTotalExpAmountFromLevel(level: Int): Int = getTotalExpAmountFromLevel(level.toDouble()).roundToInt()

/**
 * @see [com.enderio.base.common.util.ExperienceUtil.getTotalXpFromLevel]
 */
private fun getTotalExpAmountFromLevel(level: Double): Double = when {
    level >= 32 -> 4.5 * level.pow(2) - 162.5 * level + 2220
    level >= 17 -> 2.5 * level.pow(2) - 40.5 + level + 360
    else -> level.pow(2) + 6 * level
}

fun Player.getTotalExpAmount(): Int =
    (getExpAmountFromLevel(this.experienceLevel) + this.experienceProgress * this.xpNeededForNextLevel).roundToInt()
