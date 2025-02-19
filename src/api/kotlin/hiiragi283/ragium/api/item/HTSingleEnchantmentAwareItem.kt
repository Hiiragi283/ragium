package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.extension.copyAndEdit
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.neoforged.neoforge.common.extensions.IItemExtension
import kotlin.math.max

interface HTSingleEnchantmentAwareItem : IItemExtension {
    val targetEnchantment: ResourceKey<Enchantment>
    val targetLevel: Int

    override fun isPrimaryItemFor(stack: ItemStack, enchantment: Holder<Enchantment>): Boolean =
        !enchantment.`is`(targetEnchantment) && super.isPrimaryItemFor(stack, enchantment)

    override fun getEnchantmentLevel(stack: ItemStack, enchantment: Holder<Enchantment>): Int = when {
        stack.isEmpty -> 0
        enchantment.`is`(targetEnchantment) -> max(targetLevel, super.getEnchantmentLevel(stack, enchantment))
        else -> super.getEnchantmentLevel(stack, enchantment)
    }

    override fun getAllEnchantments(stack: ItemStack, lookup: HolderLookup.RegistryLookup<Enchantment>): ItemEnchantments {
        var enchantments: ItemEnchantments = super.getAllEnchantments(stack, lookup)
        lookup.get(targetEnchantment).ifPresent { holder: Holder.Reference<Enchantment> ->
            if (enchantments.getLevel(holder) == 0) {
                enchantments = enchantments.copyAndEdit { set(holder, targetLevel) }
            }
        }
        return enchantments
    }
}
