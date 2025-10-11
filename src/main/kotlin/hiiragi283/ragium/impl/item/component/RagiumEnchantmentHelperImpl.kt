package hiiragi283.ragium.impl.item.component

import hiiragi283.ragium.api.item.component.RagiumEnchantmentHelper
import hiiragi283.ragium.setup.RagiumEnchantmentComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import org.apache.commons.lang3.mutable.MutableFloat

internal class RagiumEnchantmentHelperImpl : RagiumEnchantmentHelper {
    override fun modifyStorageCapacity(
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

    override fun modifyCollectorRange(
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

    override fun hasStrike(stack: ItemStack): Boolean = EnchantmentHelper.has(stack, RagiumEnchantmentComponents.STRIKE)
}
