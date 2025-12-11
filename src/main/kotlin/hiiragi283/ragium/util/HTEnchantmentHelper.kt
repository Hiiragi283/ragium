package hiiragi283.ragium.util

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.setup.RagiumEnchantmentComponents
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import org.apache.commons.lang3.mutable.MutableFloat
import kotlin.math.max

object HTEnchantmentHelper {
    @JvmStatic
    fun modifyCollectorRange(
        enchantment: Enchantment,
        serverLevel: ServerLevel,
        enchantmentLevel: Int,
        stack: ItemStack,
        collectorRange: MutableFloat,
    ) {
        enchantment.modifyItemFilteredCount(
            RagiumEnchantmentComponents.RANGE,
            serverLevel,
            enchantmentLevel,
            stack,
            collectorRange,
        )
    }

    @JvmStatic
    fun hasStrike(stack: ItemStack): Boolean = EnchantmentHelper.has(stack, RagiumEnchantmentComponents.STRIKE)

    //    ItemStack    //

    @JvmStatic
    fun processCollectorRange(serverLevel: ServerLevel, stack: ItemStack, range: Double): Double {
        val float = MutableFloat(range)
        EnchantmentHelper.runIterationOnItem(stack) { holder: Holder<Enchantment>, level: Int ->
            modifyCollectorRange(holder.value(), serverLevel, level, stack, float)
        }
        return max(0.0, float.toDouble())
    }

    // Energy Usage
    @JvmStatic
    fun getFixedUsage(stack: ItemStack, amount: Int): Int {
        val overworld: ServerLevel = RagiumPlatform.INSTANCE.getCurrentServer()?.overworld() ?: return amount
        return getFixedUsage(overworld, stack, amount)
    }

    @JvmStatic
    fun getFixedUsage(level: Level, stack: ItemStack, amount: Int): Int = when (level) {
        is ServerLevel -> getFixedUsage(level, stack, amount)
        else -> amount
    }

    @JvmStatic
    fun getFixedUsage(level: ServerLevel, stack: ItemStack, amount: Int): Int =
        EnchantmentHelper.processDurabilityChange(level, stack, amount)
}
