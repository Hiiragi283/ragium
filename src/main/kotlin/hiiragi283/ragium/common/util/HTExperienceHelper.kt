package hiiragi283.ragium.common.util

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.experience.HTExperienceStorage
import it.unimi.dsi.fastutil.longs.Long2LongArrayMap
import net.minecraft.world.entity.player.Player
import kotlin.math.max

/**
 * @see [me.desht.pneumaticcraft.common.util.EnchantmentUtils]
 */
object HTExperienceHelper {
    @JvmStatic
    fun getPlayerExp(player: Player): Long =
        getExpForLevel(player.experienceLevel) + (player.experienceProgress * player.xpNeededForNextLevel).toLong()

    @JvmStatic
    fun setPlayerExp(player: Player, amount: Long) {
        val fixedAmount: Long = max(0, amount)
        player.totalExperience = Ints.saturatedCast(fixedAmount)
        player.experienceLevel = Ints.saturatedCast(getLevelForExp(fixedAmount))
        val expForLevel: Int = getExpForLevel(player.experienceLevel)
        player.experienceProgress = (fixedAmount - expForLevel) / player.xpNeededForNextLevel.toFloat()
    }

    @JvmStatic
    fun getExpForLevel(level: Int): Int = when {
        level == 0 -> 0
        level <= 15 -> sum(level, 7, 2)
        level <= 30 -> 315 + sum(level - 15, 37, 5)
        else -> 1395 + sum(level - 30, 112, 9)
    }

    @JvmStatic
    private fun sum(n: Int, a: Int, d: Int): Int = n * (2 * a + (n - 1) * d) / 2

    @JvmStatic
    fun getExpBarCapacity(level: Long): Long = when {
        level >= 30 -> 112 + (level - 30) * 9
        level >= 15 -> 37 + (level - 15) * 5
        else -> 7 + level * 2
    }

    @JvmStatic
    private val levelCache: MutableMap<Long, Long> = Long2LongArrayMap()

    @JvmStatic
    fun getLevelForExp(exp: Long): Long = levelCache.computeIfAbsent(exp, ::findLevelForExp)

    @JvmStatic
    private fun findLevelForExp(exp: Long): Long {
        var exp1: Long = exp
        var level = 0L
        while (true) {
            val nextLevel: Long = getExpBarCapacity(level)
            if (exp1 < nextLevel) return level
            level++
            exp1 -= nextLevel
        }
    }

    fun moveExp(
        from: HTExperienceStorage?,
        to: HTExperienceStorage?,
        amount: Long = from?.getAmount() ?: 0,
        access: HTStorageAccess = HTStorageAccess.INTERNAL,
    ): Long {
        if (from == null || to == null || amount <= 0) return 0
        val simulatedExtracted: Long = from.extractExp(amount, HTStorageAction.SIMULATE, access)
        val simulatedRemain: Long = to.insertExp(simulatedExtracted, HTStorageAction.SIMULATE, access)
        val simulatedAccepted: Long = amount - simulatedRemain

        val extracted: Long = from.extractExp(simulatedAccepted, HTStorageAction.EXECUTE, access)
        val remainder: Long = to.insertExp(extracted, HTStorageAction.EXECUTE, access)
        if (remainder > 0) {
            val leftover: Long = from.insertExp(remainder, HTStorageAction.EXECUTE, access)
            if (leftover > 0) {
                RagiumAPI.LOGGER.error("Experience storage $from did not accept leftover amount from $to! Voiding it.")
            }
        }
        return remainder
    }
}
