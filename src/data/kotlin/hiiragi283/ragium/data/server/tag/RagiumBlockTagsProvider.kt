package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.addBlock
import hiiragi283.ragium.api.extension.asBlockHolder
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.registry.HTBlockHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.HTBuildingBlockSets
import hiiragi283.ragium.util.HTOreSets
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.IntrinsicHolderTagsProvider
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.concurrent.CompletableFuture

class RagiumBlockTagsProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    IntrinsicHolderTagsProvider<Block>(
        output,
        Registries.BLOCK,
        provider,
        { block: Block -> block.asBlockHolder().key },
        RagiumAPI.MOD_ID,
        helper,
    ) {
    private fun addBlock(parent: TagKey<Block>, child: TagKey<Block>, block: HTBlockHolderLike) {
        tag(parent).addTag(child)
        tag(child).addBlock(block)
    }

    override fun addTags(provider: HolderLookup.Provider) {
        mineable()
        category()
    }

    //    Mineable    //

    private fun mineable() {
        // Axe
        tag(BlockTags.MINEABLE_WITH_AXE)
            .addBlock(RagiumBlocks.EXP_BERRY_BUSH)
            .addBlock(RagiumBlocks.Casings.WOODEN)
        // Hoe
        val hoe: IntrinsicTagAppender<Block> = tag(BlockTags.MINEABLE_WITH_HOE)
            .addBlock(RagiumBlocks.SWEET_BERRIES_CAKE)
        // Pickaxe
        val pickaxe: IntrinsicTagAppender<Block> = tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
            .addTag(RagiumModTags.Blocks.LED_BLOCKS)
            .addBlock(RagiumBlocks.RESONANT_DEBRIS)

        for (sets: HTOreSets in listOf(RagiumBlocks.RAGINITE_ORES, RagiumBlocks.RAGI_CRYSTAL_ORES)) {
            for (ore: DeferredBlock<*> in sets.blockHolders) {
                pickaxe.addBlock(ore)
                tag(sets.blockOreTag).addBlock(ore)
            }
            // Ores in ground
            tag(Tags.Blocks.ORES_IN_GROUND_STONE).addBlock(sets.stoneOre)
            tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).addBlock(sets.deepOre)
            tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).addBlock(sets.netherOre)
            tag(blockTagKey(commonId("ores_in_ground/end_stone"))).addBlock(sets.endOre)
        }
        tag(Tags.Blocks.ORES)
            .addTag(RagiumCommonTags.Blocks.ORES_RAGI_CRYSTAL)
            .addTag(RagiumCommonTags.Blocks.ORES_RAGINITE)
            .addTag(RagiumCommonTags.Blocks.ORES_DEEP_SCRAP)
        tag(RagiumCommonTags.Blocks.ORES_DEEP_SCRAP).addBlock(RagiumBlocks.RESONANT_DEBRIS)

        for (block: RagiumBlocks.StorageBlocks in RagiumBlocks.StorageBlocks.entries) {
            pickaxe.addBlock(block)
            addBlock(Tags.Blocks.STORAGE_BLOCKS, block.tagKey, block)
            tag(BlockTags.BEACON_BASE_BLOCKS).addBlock(block)
        }

        for (sets: HTBuildingBlockSets in RagiumBlocks.DECORATIONS) {
            val builder: IntrinsicTagAppender<Block> = when (sets) {
                RagiumBlocks.SPONGE_CAKE_SETS -> hoe
                else -> pickaxe
            }
            sets.blockHolders.forEach(builder::addBlock)
            tag(BlockTags.STAIRS).addBlock(sets.stairs)
            tag(BlockTags.SLABS).addBlock(sets.slab)
            tag(BlockTags.WALLS).addBlock(sets.wall)
        }

        buildList {
            addAll(RagiumBlocks.Casings.entries)
            addAll(RagiumBlocks.Devices.entries)
            addAll(RagiumBlocks.Drums.entries)
            addAll(RagiumBlocks.Dynamos.entries)
            addAll(RagiumBlocks.Frames.entries)
            addAll(RagiumBlocks.Glasses.entries)
            addAll(RagiumBlocks.Machines.entries)
        }.forEach(pickaxe::addBlock)

        // Shovel
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .addBlock(RagiumBlocks.ASH_LOG)
            .addBlock(RagiumBlocks.CRIMSON_SOIL)
            .addBlock(RagiumBlocks.SILT)
        // Other
    }

    private fun category() {
        // Glass
        for (glass: RagiumBlocks.Glasses in RagiumBlocks.Glasses.entries) {
            addBlock(Tags.Blocks.GLASS_BLOCKS, glass.tagKey, glass)
        }
        // LED
        RagiumBlocks.LEDBlocks.entries.forEach(tag(RagiumModTags.Blocks.LED_BLOCKS)::addBlock)
        // Stone
        tag(Tags.Blocks.OBSIDIANS).addTag(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
        tag(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS).addBlock(RagiumBlocks.MYSTERIOUS_OBSIDIAN)
        tag(RagiumModTags.Blocks.RESONANT_DEBRIS_REPLACEABLES).addTag(BlockTags.DEEPSLATE_ORE_REPLACEABLES)
        // Crop
        tag(BlockTags.BEE_GROWABLES).addBlock(RagiumBlocks.EXP_BERRY_BUSH)
        tag(BlockTags.FALL_DAMAGE_RESETTING).addBlock(RagiumBlocks.EXP_BERRY_BUSH)
        tag(BlockTags.SWORD_EFFICIENT).addBlock(RagiumBlocks.EXP_BERRY_BUSH)
        // Others
        tag(BlockTags.HOGLIN_REPELLENTS).addBlock(RagiumBlocks.StorageBlocks.WARPED_CRYSTAL)
        tag(BlockTags.INFINIBURN_OVERWORLD).addBlock(RagiumBlocks.StorageBlocks.CRIMSON_CRYSTAL)
        tag(BlockTags.SOUL_FIRE_BASE_BLOCKS).addBlock(RagiumBlocks.StorageBlocks.WARPED_CRYSTAL)
        tag(BlockTags.STRIDER_WARM_BLOCKS).addBlock(RagiumBlocks.StorageBlocks.CRIMSON_CRYSTAL)

        tag(RagiumModTags.Blocks.WIP)
            .addTag(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
            .addBlock(RagiumBlocks.ASH_LOG)
            .addBlock(RagiumBlocks.Casings.WOODEN)
            .addBlock(RagiumBlocks.Casings.STONE)
    }
}
