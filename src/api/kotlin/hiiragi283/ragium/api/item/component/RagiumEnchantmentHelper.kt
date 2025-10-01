package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.entity.BlockEntity
import org.apache.commons.lang3.mutable.MutableFloat
import kotlin.math.max

interface RagiumEnchantmentHelper {
    companion object {
        @JvmField
        val INSTANCE: RagiumEnchantmentHelper = RagiumAPI.getService()

        @JvmField
        val DEFAULT_RANDOM: RandomSource = RandomSource.create()
    }

    fun runIterationOnComponent(componentMap: DataComponentMap, visitor: EnchantmentHelper.EnchantmentVisitor) {
        val enchantments: ItemEnchantments = componentMap.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        for (entry in enchantments.entrySet()) {
            visitor.accept(entry.key, entry.intValue)
        }
    }

    fun processStorageCapacity(random: RandomSource?, blockEntity: BlockEntity, capacity: Int): Int {
        val float = MutableFloat(capacity)
        runIterationOnComponent(blockEntity.components()) { holder: Holder<Enchantment>, level: Int ->
            modifyStorageCapacity(holder.value(), random ?: DEFAULT_RANDOM, level, float)
        }
        return max(0, float.toInt())
    }

    fun processStorageCapacity(random: RandomSource?, stack: ItemStack, capacity: Int): Int {
        val float = MutableFloat(capacity)
        EnchantmentHelper.runIterationOnItem(stack) { holder: Holder<Enchantment>, level: Int ->
            modifyStorageCapacity(holder.value(), random ?: DEFAULT_RANDOM, level, float)
        }
        return max(0, float.toInt())
    }

    fun processCollectorRange(serverLevel: ServerLevel, stack: ItemStack, range: Double): Double {
        val float = MutableFloat(range)
        EnchantmentHelper.runIterationOnItem(stack) { holder: Holder<Enchantment>, level: Int ->
            modifyCollectorRange(holder.value(), serverLevel, level, stack, float)
        }
        return max(0.0, float.toDouble())
    }

    fun modifyStorageCapacity(
        enchantment: Enchantment,
        random: RandomSource,
        enchantmentLevel: Int,
        storageCapacity: MutableFloat,
    )

    fun modifyCollectorRange(
        enchantment: Enchantment,
        serverLevel: ServerLevel,
        enchantmentLevel: Int,
        stack: ItemStack,
        collectorRange: MutableFloat,
    )
}
