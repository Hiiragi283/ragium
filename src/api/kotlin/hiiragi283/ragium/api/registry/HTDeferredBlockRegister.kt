package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.item.HTBlockItem
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * @see [mekanism.common.registration.impl.BlockDeferredRegister]
 */
class HTDeferredBlockRegister(namespace: String) :
    HTDoubleDeferredRegister<Block, Item>(namespace, Registries.BLOCK, HTDeferredItemRegister(namespace)) {
    fun registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        itemProp: Item.Properties = Item.Properties(),
    ): HTSimpleDeferredBlockHolder = register(name, blockProp, ::Block, ::HTBlockItem, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockGetter: (BlockBehaviour.Properties) -> BLOCK,
        itemProp: Item.Properties = Item.Properties(),
    ): HTBasicDeferredBlockHolder<BLOCK> = register(name, blockProp, blockGetter, ::HTBlockItem, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockGetter: () -> BLOCK,
        itemProp: Item.Properties = Item.Properties(),
    ): HTBasicDeferredBlockHolder<BLOCK> = register(name, blockGetter, ::HTBlockItem, itemProp)

    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockGetter: (BlockBehaviour.Properties) -> BLOCK,
        itemGetter: (BLOCK, Item.Properties) -> ITEM,
        itemProp: Item.Properties = Item.Properties(),
    ): HTDeferredBlockHolder<BLOCK, ITEM> = register(
        name,
        { blockGetter(blockProp) },
        itemGetter,
        itemProp,
    )

    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockGetter: () -> BLOCK,
        itemGetter: (BLOCK, Item.Properties) -> ITEM,
        itemProp: Item.Properties = Item.Properties(),
    ): HTDeferredBlockHolder<BLOCK, ITEM> = registerAdvanced(
        name,
        { _: ResourceLocation -> blockGetter() },
        { block: DeferredHolder<Block, BLOCK> ->
            itemGetter(block.get(), itemProp)
        },
        ::HTDeferredBlockHolder,
    )
}
