package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.item.HTBlockItem
import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.registry.HTDoubleDeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

/**
 * @see [mekanism.common.registration.impl.BlockDeferredRegister]
 */
class HTDeferredBlockRegister(namespace: String) :
    HTDoubleDeferredRegister<Block, Item>(namespace, Registries.BLOCK, HTDeferredItemRegister(namespace)) {
    fun registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        itemProp: Item.Properties = Item.Properties(),
    ): HTSimpleDeferredBlock = register(name, blockProp, ::Block, ::HTBlockItem, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockGetter: (BlockBehaviour.Properties) -> BLOCK,
        itemProp: Item.Properties = Item.Properties(),
    ): HTBasicDeferredBlock<BLOCK> = register(name, blockProp, blockGetter, ::HTBlockItem, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockProp: () -> BlockBehaviour.Properties,
        blockGetter: (BlockBehaviour.Properties) -> BLOCK,
        itemProp: Item.Properties = Item.Properties(),
    ): HTBasicDeferredBlock<BLOCK> = register(name, blockProp, blockGetter, ::HTBlockItem, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockGetter: () -> BLOCK,
        itemProp: Item.Properties = Item.Properties(),
    ): HTBasicDeferredBlock<BLOCK> = register(name, blockGetter, ::HTBlockItem, itemProp)

    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockGetter: (BlockBehaviour.Properties) -> BLOCK,
        itemGetter: (BLOCK, Item.Properties) -> ITEM,
        itemProp: Item.Properties = Item.Properties(),
    ): HTDeferredBlock<BLOCK, ITEM> = register(
        name,
        { blockGetter(blockProp) },
        itemGetter,
        itemProp,
    )

    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockProp: () -> BlockBehaviour.Properties,
        blockGetter: (BlockBehaviour.Properties) -> BLOCK,
        itemGetter: (BLOCK, Item.Properties) -> ITEM,
        itemProp: Item.Properties = Item.Properties(),
    ): HTDeferredBlock<BLOCK, ITEM> = register(
        name,
        { blockGetter(blockProp()) },
        itemGetter,
        itemProp,
    )

    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockGetter: () -> BLOCK,
        itemGetter: (BLOCK, Item.Properties) -> ITEM,
        itemProp: Item.Properties = Item.Properties(),
    ): HTDeferredBlock<BLOCK, ITEM> = registerAdvanced(
        name,
        { _: ResourceLocation -> blockGetter() },
        { block: HTDeferredHolder<Block, BLOCK> ->
            itemGetter(block.get(), itemProp)
        },
        ::HTDeferredBlock,
    )
}
