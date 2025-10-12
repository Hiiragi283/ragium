package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumEnchantmentComponents
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import org.apache.commons.lang3.mutable.MutableFloat
import kotlin.math.max

object HTItemHelper {
    @JvmField
    val DEFAULT_RANDOM: RandomSource = RandomSource.create()

    @JvmStatic
    fun modifyStorageCapacity(
        enchantment: Enchantment,
        random: RandomSource,
        enchantmentLevel: Int,
        storageCapacity: MutableFloat,
    ) {
        enchantment.modifyUnfilteredValue(
            RagiumEnchantmentComponents.CAPACITY,
            random,
            enchantmentLevel,
            storageCapacity,
        )
    }

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

    //    Block Entity    //

    @JvmStatic
    fun runIterationOnComponent(componentMap: DataComponentMap, visitor: EnchantmentHelper.EnchantmentVisitor) {
        val enchantments: ItemEnchantments = componentMap.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        for (entry in enchantments.entrySet()) {
            visitor.accept(entry.key, entry.intValue)
        }
    }

    @JvmStatic
    fun processStorageCapacity(random: RandomSource?, blockEntity: BlockEntity, capacity: Long): Long {
        val float = MutableFloat(capacity)
        runIterationOnComponent(blockEntity.components()) { holder: Holder<Enchantment>, level: Int ->
            modifyStorageCapacity(holder.value(), random ?: DEFAULT_RANDOM, level, float)
        }
        return max(0, float.toLong())
    }

    //    ItemStack    //

    fun processStorageCapacity(random: RandomSource?, stack: ItemStack, capacity: Long): Long {
        val float = MutableFloat(capacity)
        EnchantmentHelper.runIterationOnItem(stack) { holder: Holder<Enchantment>, level: Int ->
            modifyStorageCapacity(holder.value(), random ?: DEFAULT_RANDOM, level, float)
        }
        return max(0, float.toLong())
    }

    fun processCollectorRange(
        serverLevel: ServerLevel,
        stack: ItemStack,
        range: Double = RagiumConfig.COMMON.deviceCollectorEntityRange.asDouble,
    ): Double {
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
