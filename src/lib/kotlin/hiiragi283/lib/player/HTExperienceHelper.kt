package hiiragi283.lib.player

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.RagiumConfigAccess
import it.unimi.dsi.fastutil.longs.Long2IntArrayMap
import net.minecraft.core.Vec3i
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

typealias ExpValue = Long
typealias ExpLevel = Int

/**
 * @since 26.1.13
 */
data object HTExperienceHelper {
    @JvmStatic
    fun getExpRatio(): Int = RagiumConfigAccess.INSTANCE.expRatio

    @JvmStatic
    fun fluidAmountFromExp(value: Int): Int = value * getExpRatio()

    @JvmStatic
    fun fluidAmountFromExp(value: ExpValue): Long = value * getExpRatio().toLong()

    @JvmStatic
    fun expAmountFromFluid(amount: Int): ExpValue = amount / getExpRatio().toLong()

    @JvmStatic
    fun popExperienceOrb(level: Level, pos: Vec3i, amount: ExpValue) {
        popExperienceOrb(level, Vec3.atCenterOf(pos), amount)
    }

    @JvmStatic
    fun popExperienceOrb(level: Level, pos: Vec3, amount: ExpValue) {
        if (level is ServerLevel) {
            repeatLongAsInt(amount) { ExperienceOrb.award(level, pos, it) }
        }
    }

    @JvmStatic
    fun repeatLongAsInt(amount: Long, action: (Int) -> Unit) {
        repeat((amount / Int.MAX_VALUE).toInt()) {
            action(Int.MAX_VALUE)
        }
        action((amount % Int.MAX_VALUE).toInt())
    }

    //    Player    //

    @JvmStatic
    fun getPlayerExp(player: Player): ExpValue =
        getExpForLevel(player.experienceLevel) + (player.experienceProgress * player.xpNeededForNextLevel).toLong()

    @JvmStatic
    fun setPlayerExp(player: Player, amount: Long) {
        val fixedAmount: Long = maxOf(0, amount)
        player.totalExperience = Ints.saturatedCast(fixedAmount)
        player.experienceLevel = getLevelForExp(fixedAmount)
        val expForLevel: ExpValue = getExpForLevel(player.experienceLevel)
        player.experienceProgress = (fixedAmount - expForLevel) / player.xpNeededForNextLevel.toFloat()
    }

    @JvmStatic
    fun getExpForLevel(level: ExpLevel): ExpValue = when {
        level == 0 -> 0L
        level <= 15 -> sum(level, 7, 2)
        level <= 30 -> 315 + sum(level - 15, 37, 5)
        else -> 1395 + sum(level - 30, 112, 9)
    }

    @JvmStatic
    private fun sum(n: Int, a: Int, d: Int): Long = n * (2 * a + (n - 1) * d) / 2L

    @JvmStatic
    fun getExpBarCapacity(level: ExpLevel): ExpValue = when {
        level >= 30 -> 112 + (level - 30L) * 9
        level >= 15 -> 37 + (level - 15L) * 5
        else -> 7 + level * 2L
    }

    @JvmStatic
    private val levelCache: MutableMap<Long, Int> = Long2IntArrayMap()

    @JvmStatic
    fun getLevelForExp(amount: ExpValue): ExpLevel = levelCache.computeIfAbsent(amount, ::findLevelForExp)

    @JvmStatic
    private fun findLevelForExp(amount: ExpValue): ExpLevel {
        var amount1: ExpValue = amount
        var level = 0
        while (true) {
            val nextAmount: ExpValue = getExpBarCapacity(level)
            if (amount1 < nextAmount) return level
            level++
            amount1 -= nextAmount
        }
    }
}
