package hiiragi283.lib.item

import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

//    ItemStackTemplate    //

/**
 * [ItemStackTemplate]が`null`の場合，[ItemStack.EMPTY]を返します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ItemStackTemplate?.createOrEmpty(): ItemStack = this?.create() ?: ItemStack.EMPTY

/**
 * この[ItemStackTemplate][this]をコピーします。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ItemStackTemplate.transmuteCopy(newItem: ItemLike, newCount: Int = this.count()): ItemStackTemplate? = when {
    newItem.asItem() == Items.AIR -> null
    else -> ItemStackTemplate(newItem.asItem(), newCount, this.components())
}

//    ItemStack    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
fun ItemStack(item: ItemLike, patch: DataComponentPatch): ItemStack = ItemStack(item, 1, patch)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ItemStack(item: ItemLike, count: Int, patch: DataComponentPatch): ItemStack {
    val stack = ItemStack(item, count)
    stack.applyComponents(patch)
    return stack
}

/**
 * [ItemStack]を[ItemStackTemplate]に変換します。
 * @return [ItemStack.isEmpty]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ItemStack.toTemplateOrNull(): ItemStackTemplate? = when {
    this.isEmpty -> null
    else -> ItemStackTemplate.fromNonEmptyStack(this)
}
