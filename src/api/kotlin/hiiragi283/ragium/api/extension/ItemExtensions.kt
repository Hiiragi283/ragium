package hiiragi283.ragium.api.extension

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.MutableDataComponentHolder

//    ItemLike    //

/**
 * この[ItemLike]から[ItemStack]を返します。
 * @param count [ItemStack]の個数
 */
fun ItemLike.toStack(count: Int = 1): ItemStack = ItemStack(asItem(), count)

//    Item    //

fun itemProperty(): Item.Properties = Item.Properties()

//    ItemStack    //

inline fun createItemStack(item: ItemLike, count: Int = 1, builderAction: MutableDataComponentHolder.() -> Unit): ItemStack =
    ItemStack(item, count).apply(builderAction)

fun createPotionStack(potion: Holder<Potion>, count: Int = 1): ItemStack = createPotionStack(PotionContents(potion), count)

fun createPotionStack(content: PotionContents, count: Int = 1): ItemStack = createItemStack(Items.POTION, count) {
    set(DataComponents.POTION_CONTENTS, content)
}

val ItemStack.isNotEmpty: Boolean get() = !isEmpty

/**
 * 残りの耐久値を返します。
 */
val ItemStack.restDamage: Int get() = maxDamage - damageValue

/**
 * 現在の個数が[isMaxCount]と一致するか判定します。
 */
val ItemStack.isMaxCount: Boolean get() = count == maxStackSize
