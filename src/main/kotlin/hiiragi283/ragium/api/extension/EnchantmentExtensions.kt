package hiiragi283.ragium.api.extension

import it.unimi.dsi.fastutil.objects.Object2IntMap
import net.minecraft.component.type.ItemEnchantmentsComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.EnchantmentLevelEntry
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.world.World

fun hasEnchantment(enchantment: RegistryKey<Enchantment>, world: World, stack: ItemStack): Boolean =
    EnchantmentHelper.getLevel(world.getEntry(RegistryKeys.ENCHANTMENT, enchantment), stack) > 0

fun ItemEnchantmentsComponent.toLevelMap(): List<EnchantmentLevelEntry> =
    enchantmentEntries.map(Object2IntMap.Entry<RegistryEntry<Enchantment>>::levelEntry)

fun Map.Entry<RegistryEntry<Enchantment>, Int>.levelEntry(): EnchantmentLevelEntry = EnchantmentLevelEntry(key, value)
