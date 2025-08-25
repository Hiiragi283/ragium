package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.item.HTBlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem

typealias HTSimpleDeferredBlockHolder = HTDeferredBlockHolder<Block, HTBlockItem<Block>>

typealias HTBasicDeferredBlockHolder<BLOCK> = HTDeferredBlockHolder<BLOCK, HTBlockItem<BLOCK>>

/**
 * @see [mekanism.common.registration.impl.BlockRegistryObject]
 */
class HTDeferredBlockHolder<BLOCK : Block, ITEM : Item>(first: DeferredHolder<Block, BLOCK>, second: DeferredItem<ITEM>) :
    HTDoubleDeferredHolder<Block, BLOCK, Item, ITEM>(
        first,
        second,
    ),
    ItemLike {
    constructor(first: DeferredHolder<Block, BLOCK>, second: DeferredHolder<Item, ITEM>) : this(
        first,
        DeferredItem.createItem(second.id),
    )

    val itemHolder: DeferredItem<ITEM> = second

    override fun asItem(): ITEM = getSecond()

    fun toStack(count: Int = 1): ItemStack = itemHolder.toStack(count)

    fun isOf(state: BlockState): Boolean = state.`is`(this)

    fun isOf(stack: ItemStack): Boolean = stack.`is`(itemHolder)
}
