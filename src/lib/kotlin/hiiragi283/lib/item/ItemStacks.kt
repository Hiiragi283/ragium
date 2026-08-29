package hiiragi283.lib.item

import hiiragi283.lib.registry.isAir
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.toTextResult
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike

//    ItemLike    //

/**
 * この[ItemLike][this]が空かどうか判定します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@Suppress("DEPRECATION")
val ItemLike.isAir: Boolean get() = this.asItem().builtInRegistryHolder().isAir

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
    newItem.isAir -> null
    else -> ItemStackTemplate(newItem.asItem(), newCount, this.components())
}

//    ItemStack    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.4
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

/**
 * [ItemStack]を[ItemStackTemplate]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ItemStack.toTemplateResult(): HTTextResult<ItemStackTemplate> = this.toTemplateOrNull().toTextResult { "ItemStack must be non-empty" }
