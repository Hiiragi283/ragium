package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment

//    ItemStack    //

fun ItemStack.getEnchantmentLevel(key: ResourceKey<Enchantment>): Int = RagiumAPI
    .getInstance()
    .resolveLookup(Registries.ENCHANTMENT)
    ?.get(key)
    ?.map(this::getEnchantmentLevel)
    ?.orElse(0)
    ?: 0
