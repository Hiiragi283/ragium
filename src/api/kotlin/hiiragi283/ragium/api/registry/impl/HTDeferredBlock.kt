package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.item.HTBlockItem
import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.registry.HTDoubleDeferredHolder
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.text.HTHasText
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

typealias HTSimpleDeferredBlock = HTDeferredBlock<Block, HTBlockItem<Block>>

typealias HTBasicDeferredBlock<BLOCK> = HTDeferredBlock<BLOCK, HTBlockItem<BLOCK>>

class HTDeferredBlock<BLOCK : Block, ITEM : Item>(first: HTDeferredHolder<Block, BLOCK>, second: HTDeferredItem<ITEM>) :
    HTDoubleDeferredHolder<Block, BLOCK, Item, ITEM>(
        first,
        second,
    ),
    HTHasTranslationKey,
    HTHasText,
    HTItemHolderLike {
    constructor(first: HTDeferredHolder<Block, BLOCK>, second: HTDeferredHolder<Item, ITEM>) : this(
        first,
        HTDeferredItem(second.id),
    )

    val itemHolder: HTDeferredItem<ITEM> = second
    override val translationKey: String get() = get().descriptionId

    override fun getText(): Component = get().name

    override fun asItem(): ITEM = getSecond()

    fun isOf(state: BlockState): Boolean = state.`is`(this)
}
