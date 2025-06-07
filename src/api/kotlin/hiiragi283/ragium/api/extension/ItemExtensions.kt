package hiiragi283.ragium.api.extension

import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable

//    ItemLike    //

/**
 * この[ItemLike]から[ItemStack]を返します。
 * @param count [ItemStack]の個数
 */
fun ItemLike.toStack(count: Int = 1): ItemStack = ItemStack(asItem(), count)

//    ItemStack    //

inline fun createItemStack(item: ItemLike, count: Int = 1, builderAction: MutableDataComponentHolder.() -> Unit): ItemStack =
    ItemStack(item, count).apply(builderAction)

fun createPotionStack(potion: Holder<Potion>, count: Int = 1, item: ItemLike = Items.POTION): ItemStack =
    createPotionStack(PotionContents(potion), count, item)

fun createPotionStack(content: PotionContents, count: Int = 1, item: ItemLike = Items.POTION): ItemStack = createItemStack(item, count) {
    set(DataComponents.POTION_CONTENTS, content)
}

/**
 * 残りの耐久値を返します。
 */
val ItemStack.restDamage: Int get() = maxDamage - damageValue

/**
 * 現在の個数が[isMaxCount]と一致するか判定します。
 */
val ItemStack.isMaxCount: Boolean get() = count == maxStackSize

//    IItemHandler    //

inline fun IItemHandler.forEachStacks(action: (ItemStack) -> Unit) {
    (0 until slots).map(this::getStackInSlot).forEach(action)
}

fun IItemHandler.dropStacksAt(level: Level, pos: BlockPos) {
    forEachStacks { stack: ItemStack -> dropStackAt(level, pos, stack) }
}

fun IItemHandler.dropStacksAt(entity: Entity) {
    forEachStacks { stack: ItemStack -> dropStackAt(entity, stack) }
}

fun IItemHandlerModifiable.consumeStackInSlot(slot: Int, count: Int) {
    val stack: ItemStack = getStackInSlot(slot)
    if (stack.hasCraftingRemainingItem()) {
        setStackInSlot(slot, stack.craftingRemainingItem)
    } else {
        stack.shrink(count)
    }
}
