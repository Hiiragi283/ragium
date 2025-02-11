package hiiragi283.ragium.api.extension

import it.unimi.dsi.fastutil.objects.Object2IntMap
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.minecraft.world.item.enchantment.ItemEnchantments

//    ItemStack    //

fun ItemStack.getLevel(provider: HolderLookup.Provider?, key: ResourceKey<Enchantment>): Int =
    provider?.getHolder(Registries.ENCHANTMENT, key)?.map(this::getEnchantmentLevel)?.orElse(0) ?: 0

//    ItemEnchantments    //

fun ItemEnchantments.getLevel(provider: HolderLookup.Provider?, key: ResourceKey<Enchantment>): Int {
    val provider1: HolderLookup.Provider = provider ?: return 0
    return provider1.getHolder(Registries.ENCHANTMENT, key).map(this::getLevel).orElse(0)
}

operator fun ItemEnchantments.get(holder: Holder<Enchantment>): Int = getLevel(holder)

operator fun ItemEnchantments.contains(key: ResourceKey<Enchantment>): Boolean = key in keySet().map(Holder<Enchantment>::keyOrThrow)

fun ItemEnchantments.toList(): List<EnchantmentInstance> =
    entrySet().map { entry: Object2IntMap.Entry<Holder<Enchantment>> -> EnchantmentInstance(entry.key, entry.intValue) }

/**
 * この[ItemEnchantments]をコピーした新しいインスタンスを返します。
 * @param action エンチャントを編集するブロック
 */
inline fun ItemEnchantments.copyAndEdit(action: ItemEnchantments.Mutable.() -> Unit): ItemEnchantments =
    ItemEnchantments.Mutable(this).apply(action).toImmutable()

/**
 * この[ItemStack]が保持しているエンチャントを更新します。
 * @param action エンチャントを更新するブロック
 */
fun ItemStack.modifyEnchantment(action: (ItemEnchantments.Mutable) -> ItemEnchantments?): ItemStack {
    val type: DataComponentType<ItemEnchantments> = EnchantmentHelper.getComponentType(this)
    val current: ItemEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(this)
    val newEnch: ItemEnchantments? = action(ItemEnchantments.Mutable(current))
    if (newEnch == null) this.remove(type) else this.set(type, newEnch)
    return this
}
