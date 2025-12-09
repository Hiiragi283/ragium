package hiiragi283.ragium.common.util

import com.google.common.primitives.Ints
import hiiragi283.ragium.config.RagiumConfig
import it.unimi.dsi.fastutil.longs.Long2LongArrayMap
import net.minecraft.core.Holder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import kotlin.math.max

/**
 * @see me.desht.pneumaticcraft.common.util.EnchantmentUtils
 */
object HTExperienceHelper {
    fun fluidAmountFromExp(value: Int): Int = value * RagiumConfig.COMMON.expConversionRatio.asInt

    fun expAmountFromFluid(value: Int): Int = value / RagiumConfig.COMMON.expConversionRatio.asInt

    //    Player    //

    @JvmStatic
    fun getPlayerExp(player: Player): Int =
        getExpForLevel(player.experienceLevel) + (player.experienceProgress * player.xpNeededForNextLevel).toInt()

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

    //    Enchantment    //

    @JvmStatic
    inline fun getTotalCost(enchantments: ItemEnchantments, transform: (Enchantment, Int) -> Int): Int =
        enchantments.entrySet().sumOf { (holder: Holder<Enchantment>, level: Int) -> transform(holder.value(), level) }

    @JvmStatic
    fun getTotalMinCost(enchantments: ItemEnchantments): Int = getTotalCost(enchantments, Enchantment::getMinCost)

    @JvmStatic
    fun getTotalMaxCost(enchantments: ItemEnchantments): Int = getTotalCost(enchantments, Enchantment::getMaxCost)

    //    Interaction    //

    /*fun moveExperience(
        from: HTExperienceTank?,
        to: HTExperienceTank?,
        amount: Long = from?.getAmount() ?: 0,
        access: HTStorageAccess = HTStorageAccess.INTERNAL,
    ): Long? {
        if (from == null || to == null || amount <= 0) return null
        val simulatedExtracted: Long = from.extract(amount, HTStorageAction.SIMULATE, access)
        val simulatedInserted: Long = to.insert(simulatedExtracted, HTStorageAction.SIMULATE, access)

        val extracted: Long = from.extract(simulatedInserted, HTStorageAction.EXECUTE, access)
        val remainder: Long = to.insert(extracted, HTStorageAction.EXECUTE, access)
        if (remainder > 0) {
            val leftover: Long = from.insert(remainder, HTStorageAction.EXECUTE, access)
            if (leftover > 0) {
                RagiumAPI.LOGGER.error("Experience storage $from did not accept leftover amount from $to! Voiding it.")
            }
        }
        return remainder
    }

    fun moveExperience(from: HTItemSlot, containerSetter: HTStackSetter<ImmutableItemStack>, to: HTExperienceTank): Boolean {
        val stack: ImmutableItemStack = from.getStack() ?: return false
        if (!HTExperienceCapabilities.hasCapability(stack)) return false
        val wrapper: HTExperienceHandlerItemWrapper = HTExperienceHandlerItemWrapper.create(stack.copyWithAmount(1)) ?: return false
        return moveExperience(from, containerSetter, wrapper, to)
    }

    fun moveExperience(
        slot: HTItemSlot,
        containerSetter: HTStackSetter<ImmutableItemStack>,
        from: HTExperienceHandlerItemWrapper,
        to: HTExperienceTank,
    ): Boolean {
        val result: Long? = moveExperience(from, to)
        if (result != null) {
            val container: ImmutableItemStack? = from.container
            if (container != null) {
                if (container.amount() == 1) {
                    containerSetter.setStack(container)
                } else {
                    slot.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.MANUAL)
                }
            }
        }
        return result != null
    }*/
}
