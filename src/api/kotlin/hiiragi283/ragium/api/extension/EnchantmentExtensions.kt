package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryAccess
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.minecraft.world.item.enchantment.ItemEnchantments

//    ItemStack    //

fun createEnchBook(holder: Holder<Enchantment>, level: Int = holder.value().maxLevel): ItemStack =
    EnchantedBookItem.createForEnchantment(EnchantmentInstance(holder, level))

fun ItemStack.getLevel(provider: HolderLookup.Provider?, key: ResourceKey<Enchantment>): Int =
    provider?.getHolder(Registries.ENCHANTMENT, key)?.map(this::getEnchantmentLevel)?.orElse(0) ?: 0

//    ItemEnchantments    //

fun ItemEnchantments.getLevel(key: ResourceKey<Enchantment>): Int {
    val access: RegistryAccess = RagiumAPI.getInstance().getRegistryAccess() ?: return 0
    return access.getHolder(Registries.ENCHANTMENT, key).map(this::getLevel).orElse(0)
}

fun ItemEnchantments.getLevel(tagKey: TagKey<Enchantment>): Int {
    val access: RegistryAccess = RagiumAPI.getInstance().getRegistryAccess() ?: return 0
    return access
        .lookupOrThrow(Registries.ENCHANTMENT)
        .get(tagKey)
        .map { holderSet: HolderSet.Named<Enchantment> -> holderSet.map(this::getLevel) }
        .map(List<Int>::max)
        .orElse(0)
}

fun <T : Any> ItemEnchantments.map(transform: (Holder<Enchantment>, Int) -> T): List<T> =
    entrySet().map { (holder: Holder<Enchantment>, level: Int) -> transform(holder, level) }

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
    this.set(type, newEnch)
    return this
}
