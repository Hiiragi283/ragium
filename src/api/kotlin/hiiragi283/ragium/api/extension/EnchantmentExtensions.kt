package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance

//    ItemStack    //

fun createEnchBook(holder: Holder<Enchantment>, level: Int = holder.value().maxLevel): ItemStack =
    EnchantedBookItem.createForEnchantment(EnchantmentInstance(holder.delegate, level))

fun ItemStack.getEnchantmentLevel(key: ResourceKey<Enchantment>): Int {
    val access: RegistryAccess = RagiumAPI.getInstance().getRegistryAccess() ?: return 0
    return access
        .enchLookup()
        .get(key)
        .map(this::getEnchantmentLevel)
        .orElse(0)
}
