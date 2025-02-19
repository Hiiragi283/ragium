package hiiragi283.ragium.api.util

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

class HTBlockFamily(
    blockRegister: DeferredRegister.Blocks,
    itemRegister: DeferredRegister.Items,
    val base: DeferredBlock<*>,
    properties: BlockBehaviour.Properties,
    private val prefix: String = base.id.path,
) {
    val stairs: DeferredBlock<StairBlock> = blockRegister.registerBlock(
        "${prefix}_stairs",
        { properties: BlockBehaviour.Properties ->
            StairBlock(
                base.get().defaultBlockState(),
                properties,
            )
        },
        properties,
    )

    val slab: DeferredBlock<SlabBlock> = blockRegister.registerBlock(
        "${prefix}_slab",
        ::SlabBlock,
        properties,
    )

    val wall: DeferredBlock<WallBlock> = blockRegister.registerBlock(
        "${prefix}_wall",
        ::WallBlock,
        properties.forceSolidOn(),
    )

    val blocks: List<DeferredBlock<out Block>> = listOf(base, stairs, slab, wall)

    init {
        listOf(stairs, slab, wall).forEach(itemRegister::registerSimpleBlockItem)
    }

    //    Data Gen    //

    fun appendTags(mineable: TagKey<Block>, action: (TagKey<Block>, Holder<Block>) -> Unit) {
        blocks.forEach { action(mineable, it) }
        action(BlockTags.STAIRS, stairs)
        action(BlockTags.SLABS, slab)
        action(BlockTags.WALLS, wall)
    }

    fun generateStates(generator: BlockStateProvider) {
        val texId: ResourceLocation = base.id.withPrefix("block/")
        generator.simpleBlock(base.get())
        generator.stairsBlock(stairs.get(), texId)
        generator.slabBlock(slab.get(), texId, texId)
        generator.wallBlock(wall.get(), texId)
    }

    fun generateModels(provider: ItemModelProvider) {
        val texId: ResourceLocation = base.id.withPrefix("block/")

        provider.simpleBlockItem(base.id)
        provider.simpleBlockItem(stairs.id)
        provider.simpleBlockItem(slab.id)

        provider
            .withExistingParent("${prefix}_wall", ResourceLocation.withDefaultNamespace("block/wall_inventory"))
            .texture("wall", texId)
    }
}
