package hiiragi283.ragium.common.util

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap
import net.minecraft.world.entity.player.Player
import kotlin.math.max

/**
 * @see [me.desht.pneumaticcraft.common.util.EnchantmentUtils]
 */
object HTExperienceHelper {
    @JvmStatic
    fun getPlayerExp(player: Player): Int =
        getExpForLevel(player.experienceLevel) + (player.experienceProgress * player.xpNeededForNextLevel).toInt()

    @JvmStatic
    fun setPlayerExp(player: Player, amount: Int) {
        val fixedAmount: Int = max(0, amount)
        player.totalExperience = fixedAmount
        player.experienceLevel = getLevelForExp(fixedAmount)
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
    fun getExpBarCapacity(level: Int): Int = when {
        level >= 30 -> 112 + (level - 30) * 9
        level >= 15 -> 37 + (level - 15) * 5
        else -> 7 + level * 2
    }

    @JvmStatic
    private val levelCache: MutableMap<Int, Int> = Int2IntArrayMap()

    @JvmStatic
    fun getLevelForExp(exp: Int): Int = levelCache.computeIfAbsent(exp, ::findLevelForExp)

    @JvmStatic
    private fun findLevelForExp(exp: Int): Int {
        var exp1: Int = exp
        var level = 0
        while (true) {
            val nextLevel: Int = getExpBarCapacity(level)
            if (exp1 < nextLevel) return level
            level++
            exp1 -= nextLevel
        }
    }
}
