package hiiragi283.ragium.api.item.component

import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import java.util.function.Predicate

fun ItemEnchantments.toMap(): Map<Holder<Enchantment>, Int> = this.keySet().associateWith(this::getLevel)

fun ItemEnchantments.toMutable(): ItemEnchantments.Mutable = ItemEnchantments.Mutable(this)

fun ItemEnchantments.filter(predicate: Predicate<Holder<Enchantment>>): ItemEnchantments =
    this.toMutable().apply { removeIf(predicate.negate()) }.toImmutable()

fun ItemEnchantments.filter(stack: ItemStack): ItemEnchantments = this.filter(stack::supportsEnchantment)
