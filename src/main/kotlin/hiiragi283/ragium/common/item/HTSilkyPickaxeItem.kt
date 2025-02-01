package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.copyAndEdit
import hiiragi283.ragium.api.extension.get
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import kotlin.math.max

class HTSilkyPickaxeItem(properties: Properties) : PickaxeItem(Tiers.IRON, properties) {
    override fun isPrimaryItemFor(stack: ItemStack, enchantment: Holder<Enchantment>): Boolean =
        !enchantment.`is`(Enchantments.SILK_TOUCH) && super.isPrimaryItemFor(stack, enchantment)

    override fun getEnchantmentLevel(stack: ItemStack, enchantment: Holder<Enchantment>): Int = when {
        stack.isEmpty -> 0
        enchantment.`is`(Enchantments.SILK_TOUCH) -> max(1, super.getEnchantmentLevel(stack, enchantment))
        else -> super.getEnchantmentLevel(stack, enchantment)
    }

    override fun getAllEnchantments(stack: ItemStack, lookup: HolderLookup.RegistryLookup<Enchantment>): ItemEnchantments {
        var enchantments: ItemEnchantments = super.getAllEnchantments(stack, lookup)
        lookup.get(Enchantments.SILK_TOUCH).ifPresent { holder: Holder.Reference<Enchantment> ->
            if (enchantments[holder] == 0) {
                enchantments = enchantments.copyAndEdit { set(holder, 1) }
            }
        }
        return enchantments
    }
}
