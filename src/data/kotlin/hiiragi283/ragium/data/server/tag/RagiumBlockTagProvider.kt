package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTTagProvider
import hiiragi283.ragium.api.tag.RagiumBlockTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.HTBuildingBlockSets
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
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
    override fun addTagsInternal(provider: HolderLookup.Provider) {
        mineable()
        category()
    }

    //    Mineable    //

    private fun mineable() {
        // Axe
        add(BlockTags.MINEABLE_WITH_AXE, RagiumBlocks.EXP_BERRY_BUSH)
        add(BlockTags.MINEABLE_WITH_AXE, RagiumBlocks.WOODEN_CASING)
        // Hoe
        add(BlockTags.MINEABLE_WITH_HOE, RagiumBlocks.CHEESE_BLOCK)
        add(BlockTags.MINEABLE_WITH_HOE, RagiumBlocks.SPONGE_CAKE)
        add(BlockTags.MINEABLE_WITH_HOE, RagiumBlocks.SPONGE_CAKE_SLAB)
        add(BlockTags.MINEABLE_WITH_HOE, RagiumBlocks.SWEET_BERRIES_CAKE)
        // Pickaxe
        add(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.MYSTERIOUS_OBSIDIAN)

        RagiumBlocks.RAGINITE_ORES.appendBlockTags(this, BlockTags.MINEABLE_WITH_PICKAXE)
        RagiumBlocks.RAGI_CRYSTAL_ORES.appendBlockTags(this, BlockTags.MINEABLE_WITH_PICKAXE)

        for (block: DeferredBlock<*> in RagiumBlocks.STORAGE_BLOCKS) {
            add(BlockTags.MINEABLE_WITH_PICKAXE, block)
        }

        for (sets: HTBuildingBlockSets in RagiumBlocks.DECORATIONS) {
            sets.appendBlockTags(this, BlockTags.MINEABLE_WITH_PICKAXE)
        }

        for (glass: DeferredBlock<*> in RagiumBlocks.GLASSES) {
            add(BlockTags.MINEABLE_WITH_PICKAXE, glass)
        }

        for (led: DeferredBlock<*> in RagiumBlocks.LED_BLOCKS.values) {
            add(BlockTags.MINEABLE_WITH_PICKAXE, led)
        }

        for (casing: DeferredBlock<*> in RagiumBlocks.CASINGS) {
            add(BlockTags.MINEABLE_WITH_PICKAXE, casing)
        }

        for (machine: DeferredBlock<*> in RagiumBlocks.MACHINES) {
            add(BlockTags.MINEABLE_WITH_PICKAXE, machine)
        }

        for (device: DeferredBlock<*> in RagiumBlocks.DEVICES) {
            add(BlockTags.MINEABLE_WITH_PICKAXE, device)
        }

        for (device: DeferredBlock<*> in RagiumBlocks.DRUMS) {
            add(BlockTags.MINEABLE_WITH_PICKAXE, device)
        }

        // Shovel
        add(BlockTags.MINEABLE_WITH_SHOVEL, RagiumBlocks.SILT)
        add(BlockTags.MINEABLE_WITH_SHOVEL, RagiumBlocks.ASH_LOG)
        // Other
    }

    private fun category() {
        // Storage Block
        /*for (block: RagiumBlocks.StorageBlocks in RagiumBlocks.StorageBlocks.entries) {
            val prefix: HTTagPrefix = block.prefix
            val materialTag: TagKey<Block> = prefix.createBlockTag(block)
            addTag(Tags.Blocks.STORAGE_BLOCKS, materialTag)
            add(materialTag, block.holder)
        }*/
        // Glass
        addTag(Tags.Blocks.GLASS_BLOCKS, RagiumBlockTags.GLASS_BLOCKS_OBSIDIAN)
        addTag(Tags.Blocks.GLASS_BLOCKS, RagiumBlockTags.GLASS_BLOCKS_QUARTZ)
        addTag(Tags.Blocks.GLASS_BLOCKS, RagiumBlockTags.GLASS_BLOCKS_SOUL)

        add(RagiumBlockTags.GLASS_BLOCKS_OBSIDIAN, RagiumBlocks.OBSIDIAN_GLASS)
        add(RagiumBlockTags.GLASS_BLOCKS_QUARTZ, RagiumBlocks.QUARTZ_GLASS)
        add(RagiumBlockTags.GLASS_BLOCKS_SOUL, RagiumBlocks.SOUL_GLASS)
        // Stone
        addTag(Tags.Blocks.OBSIDIANS, RagiumBlockTags.OBSIDIANS_MYSTERIOUS)
        add(RagiumBlockTags.OBSIDIANS_MYSTERIOUS, RagiumBlocks.MYSTERIOUS_OBSIDIAN)

        addTag(RagiumBlockTags.STONES_ROCK_GENERATIONS, Tags.Blocks.STONES)

        // Crop
        add(BlockTags.BEE_GROWABLES, RagiumBlocks.EXP_BERRY_BUSH)
        add(BlockTags.FALL_DAMAGE_RESETTING, RagiumBlocks.EXP_BERRY_BUSH)
        add(BlockTags.SWORD_EFFICIENT, RagiumBlocks.EXP_BERRY_BUSH)
    }
}
