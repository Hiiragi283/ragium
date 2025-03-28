package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.data.HTTagProvider
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.tag.RagiumBlockTags
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.concurrent.CompletableFuture

class RagiumBlockTagProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper,
) : HTTagProvider<Block>(Registries.BLOCK, output, provider, existingFileHelper) {
    override fun addTagsInternal(builder: HTTagBuilder<Block>, provider: HolderLookup.Provider) {
        mineable(builder)
        category(builder)
    }

    //    Mineable    //

    private fun mineable(builder: HTTagBuilder<Block>) {
        // Axe
        builder.add(BlockTags.MINEABLE_WITH_AXE, RagiumBlocks.EXP_BERRY_BUSH)
        builder.add(BlockTags.MINEABLE_WITH_AXE, RagiumBlocks.WOODEN_CASING)
        // Hoe
        builder.add(BlockTags.MINEABLE_WITH_HOE, RagiumBlocks.StorageBlocks.CHEESE.holder)
        builder.add(BlockTags.MINEABLE_WITH_HOE, RagiumBlocks.SPONGE_CAKE)
        builder.add(BlockTags.MINEABLE_WITH_HOE, RagiumBlocks.SPONGE_CAKE_SLAB)
        builder.add(BlockTags.MINEABLE_WITH_HOE, RagiumBlocks.SWEET_BERRIES_CAKE)
        // Pickaxe
        RagiumBlocks.RAGINITE_ORES.appendBlockTags(builder, BlockTags.MINEABLE_WITH_PICKAXE)
        RagiumBlocks.RAGI_CRYSTAL_ORES.appendBlockTags(builder, BlockTags.MINEABLE_WITH_PICKAXE)

        for (block: DeferredBlock<*> in RagiumBlocks.StorageBlocks.blocks) {
            builder.add(BlockTags.MINEABLE_WITH_PICKAXE, block)
        }

        RagiumBlocks.RAGI_BRICK_SETS.appendBlockTags(builder, BlockTags.MINEABLE_WITH_PICKAXE)
        RagiumBlocks.AZURE_TILE_SETS.appendBlockTags(builder, BlockTags.MINEABLE_WITH_PICKAXE)
        RagiumBlocks.EMBER_STONE_SETS.appendBlockTags(builder, BlockTags.MINEABLE_WITH_PICKAXE)
        RagiumBlocks.PLASTIC_SETS.appendBlockTags(builder, BlockTags.MINEABLE_WITH_PICKAXE)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.appendBlockTags(builder, BlockTags.MINEABLE_WITH_PICKAXE)

        for (glass: DeferredBlock<*> in RagiumBlocks.GLASSES) {
            builder.add(BlockTags.MINEABLE_WITH_PICKAXE, glass)
        }

        for (led: DeferredBlock<*> in RagiumBlocks.LED_BLOCKS.values) {
            builder.add(BlockTags.MINEABLE_WITH_PICKAXE, led)
        }

        for (casing: DeferredBlock<*> in RagiumBlocks.CASINGS) {
            builder.add(BlockTags.MINEABLE_WITH_PICKAXE, casing)
        }

        for (machine: DeferredBlock<*> in RagiumBlocks.MACHINES) {
            builder.add(BlockTags.MINEABLE_WITH_PICKAXE, machine)
        }

        for (device: DeferredBlock<*> in RagiumBlocks.DEVICES) {
            builder.add(BlockTags.MINEABLE_WITH_PICKAXE, device)
        }

        // Shovel
        builder.add(BlockTags.MINEABLE_WITH_SHOVEL, RagiumBlocks.SILT)
        builder.add(BlockTags.MINEABLE_WITH_SHOVEL, RagiumBlocks.ASH_LOG)
        // Other
    }

    private fun category(builder: HTTagBuilder<Block>) {
        // Storage Block
        for (block: RagiumBlocks.StorageBlocks in RagiumBlocks.StorageBlocks.entries) {
            val prefix: HTTagPrefix = block.prefix
            val materialTag: TagKey<Block> = prefix.createBlockTag(block.key)
            builder.addTag(Tags.Blocks.STORAGE_BLOCKS, materialTag)
            builder.add(materialTag, block.holder)
        }
        // Glass
        for (glass: DeferredBlock<*> in RagiumBlocks.GLASSES) {
            builder.add(Tags.Blocks.GLASS_BLOCKS, glass)
        }

        // Stone
        builder.addTag(RagiumBlockTags.STONES_ROCK_GENERATIONS, Tags.Blocks.STONES)

        // Flower
        builder.add(BlockTags.SMALL_FLOWERS, RagiumBlocks.LILY_OF_THE_ENDER)

        // Crop
        builder.add(BlockTags.BEE_GROWABLES, RagiumBlocks.EXP_BERRY_BUSH)
        builder.add(BlockTags.FALL_DAMAGE_RESETTING, RagiumBlocks.EXP_BERRY_BUSH)
        builder.add(BlockTags.SWORD_EFFICIENT, RagiumBlocks.EXP_BERRY_BUSH)
    }
}
