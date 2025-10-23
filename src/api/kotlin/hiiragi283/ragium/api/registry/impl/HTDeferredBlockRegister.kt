package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.function.BlockFactory
import hiiragi283.ragium.api.function.ItemWithContextFactory
import hiiragi283.ragium.api.item.HTBlockItem
import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.registry.HTDoubleDeferredRegister
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

/**
 * @see [mekanism.common.registration.impl.BlockDeferredRegister]
 */
class HTDeferredBlockRegister(namespace: String) :
    HTDoubleDeferredRegister<Block, Item>(HTDeferredOnlyBlockRegister(namespace), HTDeferredItemRegister(namespace)) {
    fun registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        itemProp: Item.Properties = Item.Properties(),
    ): HTSimpleDeferredBlock = register(name, blockProp, ::Block, ::HTBlockItem, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockGetter: BlockFactory<BLOCK>,
        itemProp: Item.Properties = Item.Properties(),
    ): HTBasicDeferredBlock<BLOCK> = register(name, blockProp, blockGetter, ::HTBlockItem, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockProp: () -> BlockBehaviour.Properties,
        blockFactory: BlockFactory<BLOCK>,
        itemProp: Item.Properties = Item.Properties(),
    ): HTBasicDeferredBlock<BLOCK> = register(name, blockProp, blockFactory, ::HTBlockItem, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockFactory: () -> BLOCK,
        itemProp: Item.Properties = Item.Properties(),
    ): HTBasicDeferredBlock<BLOCK> = register(name, blockFactory, ::HTBlockItem, itemProp)

    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockFactory: BlockFactory<BLOCK>,
        itemFactory: ItemWithContextFactory<BLOCK, ITEM>,
        itemProp: Item.Properties = Item.Properties(),
    ): HTDeferredBlock<BLOCK, ITEM> = register(
        name,
        { blockFactory(blockProp) },
        itemFactory,
        itemProp,
    )

    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockProp: () -> BlockBehaviour.Properties,
        blockFactory: BlockFactory<BLOCK>,
        itemFactory: ItemWithContextFactory<BLOCK, ITEM>,
        itemProp: Item.Properties = Item.Properties(),
    ): HTDeferredBlock<BLOCK, ITEM> = register(
        name,
        { blockFactory(blockProp()) },
        itemFactory,
        itemProp,
    )

    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockGetter: () -> BLOCK,
        itemFactory: ItemWithContextFactory<BLOCK, ITEM>,
        itemProp: Item.Properties = Item.Properties(),
    ): HTDeferredBlock<BLOCK, ITEM> = registerAdvanced(
        name,
        { _: ResourceLocation -> blockGetter() },
        { block: HTDeferredHolder<Block, BLOCK> -> itemFactory(block.get(), itemProp) },
        ::HTDeferredBlock,
    )
}
