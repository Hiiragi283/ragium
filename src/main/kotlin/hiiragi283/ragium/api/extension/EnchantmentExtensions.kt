package hiiragi283.ragium.api.extension

import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments

//    ItemStack    //

fun ItemStack.getLevel(provider: HolderLookup.Provider?, key: ResourceKey<Enchantment>): Int {
    val provider1: HolderLookup.Provider = provider ?: return 0
    val enchantments: ItemEnchantments = this.getAllEnchantments(provider1.lookupOrThrow(Registries.ENCHANTMENT))
    return enchantments.getLevel(provider1, key)
}

//    ItemEnchantments    //

fun ItemEnchantments.getLevel(provider: HolderLookup.Provider?, key: ResourceKey<Enchantment>): Int {
    val provider1: HolderLookup.Provider = provider ?: return 0
    return provider1.getHolder(Registries.ENCHANTMENT, key).map(this::getLevel).orElse(0)
}

operator fun ItemEnchantments.get(holder: Holder<Enchantment>): Int = getLevel(holder)

operator fun ItemEnchantments.contains(key: ResourceKey<Enchantment>): Boolean = key in keySet().map(Holder<Enchantment>::keyOrThrow)

/**
 * この[ItemEnchantments]をコピーした新しいインスタンスを返します。
 * @param action エンチャントを編集するブロック
 */
inline fun ItemEnchantments.copyAndEdit(action: ItemEnchantments.Mutable.() -> Unit): ItemEnchantments =
    ItemEnchantments.Mutable(this).apply(action).toImmutable()

/**
 * この[ItemStack]が保持しているエンチャントを更新します。
 * @param type エンチャントが紐づいている[DataComponentType]
 * @param action エンチャントを更新するブロック
 */
fun ItemStack.modifyEnchantment(
    type: DataComponentType<ItemEnchantments> = DataComponents.ENCHANTMENTS,
    action: (ItemEnchantments) -> ItemEnchantments?,
): ItemEnchantments? = update(type, ItemEnchantments.EMPTY, action)
