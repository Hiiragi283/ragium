package hiiragi283.ragium.api.extension

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.common.CommonHooks

//    ItemStack    //

fun ItemStack.getEnchantmentLevel(key: ResourceKey<Enchantment>): Int = CommonHooks
    .resolveLookup(Registries.ENCHANTMENT)
    ?.get(key)
    ?.map(this::getEnchantmentLevel)
    ?.orElse(0)
    ?: 0
