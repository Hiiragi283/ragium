package hiiragi283.ragium.api.extension

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments

//    ItemEnchantments    //

operator fun ItemEnchantments.get(holder: Holder<Enchantment>): Int = getLevel(holder)

operator fun ItemEnchantments.contains(key: ResourceKey<Enchantment>): Boolean = key in keySet().map(Holder<Enchantment>::keyOrThrow)

inline fun ItemEnchantments.copyAndEdit(action: ItemEnchantments.Mutable.() -> Unit): ItemEnchantments =
    ItemEnchantments.Mutable(this).apply(action).toImmutable()

fun ItemStack.modifyEnchantment(
    type: DataComponentType<ItemEnchantments> = DataComponents.ENCHANTMENTS,
    action: (ItemEnchantments) -> ItemEnchantments?,
): ItemEnchantments? = update(type, ItemEnchantments.EMPTY, action)
