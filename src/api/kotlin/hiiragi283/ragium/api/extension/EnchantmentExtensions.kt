package hiiragi283.ragium.api.extension

import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.neoforged.neoforge.common.CommonHooks

//    ItemStack    //

fun createEnchBook(holder: Holder<Enchantment>, level: Int = holder.value().maxLevel): ItemStack =
    EnchantedBookItem.createForEnchantment(EnchantmentInstance(holder.delegate, level))

fun ItemStack.getEnchantmentLevel(key: ResourceKey<Enchantment>): Int {
    val getter: HolderGetter<Enchantment> = CommonHooks.resolveLookup(Registries.ENCHANTMENT) ?: return 0
    return getter
        .get(key)
        .map(this::getEnchantmentLevel)
        .orElse(0)
}
