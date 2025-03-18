package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.item.HTItemStackBuilder
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

//    ItemLike    //

/**
 * この[ItemLike]から[ItemStack]を返します。
 * @param count [ItemStack]の個数
 */
fun ItemLike.toStack(count: Int = 1): ItemStack = ItemStack(asItem(), count)

//    Item    //

fun itemProperty(): Item.Properties = Item.Properties()

fun itemProperty(builderAction: Item.Properties.() -> Unit): Item.Properties = Item.Properties().apply(builderAction)

/**
 * 指定した[text]をアイテムの名前に設定します。
 */
fun Item.Properties.name(text: Component): Item.Properties = component(DataComponents.ITEM_NAME, text)

fun Item.Properties.lore(vararg keys: String): Item.Properties {
    val lines: List<MutableComponent> = keys.map(Component::translatable)
    return component(DataComponents.LORE, ItemLore(lines, lines.onEach { it.withStyle(ChatFormatting.AQUA) }))
}

fun Item.Properties.lore(vararg lines: Component): Item.Properties = component(DataComponents.LORE, ItemLore(lines.toList()))

//    ItemStack    //

fun createPotionStack(potion: Holder<Potion>, count: Int = 1): ItemStack = createPotionStack(PotionContents(potion), count)

fun createPotionStack(content: PotionContents, count: Int = 1): ItemStack = HTItemStackBuilder(Items.POTION, count)
    .put(DataComponents.POTION_CONTENTS, content)
    .build()

val ItemStack.isNotEmpty: Boolean get() = !isEmpty

/**
 * 残りの耐久値を返します。
 */
val ItemStack.restDamage: Int get() = maxDamage - damageValue

/**
 * 現在の個数が[isMaxCount]と一致するか判定します。
 */
val ItemStack.isMaxCount: Boolean get() = count == maxStackSize

//    IItemHandler    //

val IItemHandler.slotRange: IntRange
    get() = (0 until this.slots)

inline fun IItemHandler.forEach(action: (ItemStack) -> Unit) {
    slotRange.map(this::getStackInSlot).forEach(action)
}

inline fun IItemHandler.forEachSlot(action: (Int) -> Unit) {
    slotRange.forEach(action)
}

/**
 * この[IItemHandler]に保存されたすべての[ItemStack]を指定した[pos]にドロップします。
 */
fun IItemHandler.dropStacks(level: Level, pos: BlockPos) {
    forEach { dropStackAt(level, pos, it) }
}

fun moveNextOrDrop(level: Level, pos: BlockPos, stack: ItemStack) {
    var remaining: ItemStack = stack
    for (direction: Direction in Direction.entries) {
        val nextHandler: IItemHandler =
            level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(direction), direction.opposite) ?: continue
        remaining = ItemHandlerHelper.insertItem(nextHandler, stack, false)
        if (remaining.isEmpty) return
    }
    dropStackAt(level, pos.above(), remaining)
}
