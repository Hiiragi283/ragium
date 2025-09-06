package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.item.HTBlockItem
import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.registry.HTDoubleDeferredHolder
import hiiragi283.ragium.api.text.HTHasComponent
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

typealias HTSimpleDeferredBlock = HTDeferredBlock<Block, HTBlockItem<Block>>

typealias HTBasicDeferredBlock<BLOCK> = HTDeferredBlock<BLOCK, HTBlockItem<BLOCK>>

class HTDeferredBlock<BLOCK : Block, ITEM : Item>(first: HTDeferredHolder<Block, BLOCK>, second: HTDeferredItem<ITEM>) :
    HTDoubleDeferredHolder<Block, BLOCK, Item, ITEM>(
        first,
        second,
    ),
    ItemLike,
    HTHasTranslationKey,
    HTHasComponent {
    constructor(first: HTDeferredHolder<Block, BLOCK>, second: HTDeferredHolder<Item, ITEM>) : this(
        first,
        HTDeferredItem(second.id),
    )

    val itemHolder: HTDeferredItem<ITEM> = second
    override val translationKey: String get() = get().descriptionId

    override fun getComponent(): Component = get().name

    override fun asItem(): ITEM = getSecond()

    fun toStack(count: Int = 1): ItemStack = itemHolder.toStack(count)

    fun isOf(state: BlockState): Boolean = state.`is`(this)

    fun isOf(stack: ItemStack): Boolean = itemHolder.isOf(stack)
}
