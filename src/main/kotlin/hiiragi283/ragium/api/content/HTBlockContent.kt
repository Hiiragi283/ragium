package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.material.HTMaterialProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

interface HTBlockContent :
    HTContent<Block>,
    ItemLike {
    val blockId: ResourceLocation
        get() = id.withPrefix("block/")

    override val holder: DeferredBlock<out Block>

    val itemHolder: DeferredItem<out Item>

    override fun asItem(): Item = get().asItem()

    fun registerBlock(
        register: DeferredRegister.Blocks,
        properties: BlockBehaviour.Properties = blockProperty(),
        factory: (BlockBehaviour.Properties) -> Block = ::Block,
    ): DeferredBlock<Block> = register.registerBlock(id.path, factory, properties)

    fun registerBlockItem(register: DeferredRegister.Items, properties: Item.Properties = itemProperty()): DeferredItem<BlockItem> =
        register.registerSimpleBlockItem(holder, properties)

    interface Material :
        HTBlockContent,
        HTMaterialProvider

    interface Tier :
        HTBlockContent,
        HTMachineTierProvider
}
