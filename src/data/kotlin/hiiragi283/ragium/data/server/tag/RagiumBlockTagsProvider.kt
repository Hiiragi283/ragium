package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.addHolder
import hiiragi283.ragium.api.extension.asBlockHolder
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.util.HTBuildingBlockSets
import hiiragi283.ragium.common.util.HTOreSets
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.IntrinsicHolderTagsProvider
import net.minecraft.tags.BlockTags
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
    override fun addTags(provider: HolderLookup.Provider) {
        mineable()
        category()
    }

    //    Mineable    //

    private fun mineable() {
        // Axe
        tag(BlockTags.MINEABLE_WITH_AXE).addHolder(RagiumBlocks.EXP_BERRY_BUSH, RagiumBlocks.WOODEN_CASING)
        // Hoe
        tag(BlockTags.MINEABLE_WITH_HOE).addHolder(
            RagiumBlocks.SPONGE_CAKE,
            RagiumBlocks.SPONGE_CAKE_SLAB,
            RagiumBlocks.SWEET_BERRIES_CAKE,
        )
        // Pickaxe
        val pickaxe: IntrinsicTagAppender<Block> = tag(BlockTags.MINEABLE_WITH_PICKAXE)
        pickaxe.addTag(RagiumModTags.Blocks.LED_BLOCKS)

        pickaxe.addHolder(RagiumBlocks.MYSTERIOUS_OBSIDIAN)

        for (sets: HTOreSets in listOf(RagiumBlocks.RAGINITE_ORES, RagiumBlocks.RAGI_CRYSTAL_ORES)) {
            for (ore: DeferredBlock<*> in sets.blockHolders) {
                pickaxe.addHolder(ore)
                tag(sets.blockOreTag).addHolder(ore)
            }
            // Ores in ground
            tag(Tags.Blocks.ORES_IN_GROUND_STONE).addHolder(sets.stoneOre)
            tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).addHolder(sets.deepOre)
            tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).addHolder(sets.netherOre)
            tag(blockTagKey(commonId("ores_in_ground/end_stone"))).addHolder(sets.endOre)
        }
        tag(Tags.Blocks.ORES).addTags(RagiumCommonTags.Blocks.ORES_RAGI_CRYSTAL, RagiumCommonTags.Blocks.ORES_RAGINITE)

        RagiumBlocks.STORAGE_BLOCKS.forEach(pickaxe::addHolder)

        for (sets: HTBuildingBlockSets in RagiumBlocks.DECORATIONS) {
            sets.blockHolders.forEach(pickaxe::addHolder)
            tag(BlockTags.STAIRS).addHolder(sets.stairs)
            tag(BlockTags.SLABS).addHolder(sets.slab)
            tag(BlockTags.WALLS).addHolder(sets.wall)
        }

        RagiumBlocks.GLASSES.forEach(pickaxe::addHolder)
        RagiumBlocks.CASINGS.forEach(pickaxe::addHolder)
        RagiumBlocks.MACHINES.forEach(pickaxe::addHolder)
        RagiumBlocks.CAULDRONS.forEach(tag(BlockTags.CAULDRONS)::addHolder)
        RagiumBlocks.DEVICES.forEach(pickaxe::addHolder)
        RagiumBlocks.DRUMS.forEach(pickaxe::addHolder)

        // Shovel
        tag(BlockTags.MINEABLE_WITH_SHOVEL).addHolder(
            RagiumBlocks.ASH_LOG,
            RagiumBlocks.CRIMSON_SOIL,
            RagiumBlocks.SILT,
        )
        // Other
    }

    private fun category() {
        // Glass
        tag(Tags.Blocks.GLASS_BLOCKS).addTags(
            RagiumCommonTags.Blocks.GLASS_BLOCKS_OBSIDIAN,
            RagiumCommonTags.Blocks.GLASS_BLOCKS_QUARTZ,
            RagiumCommonTags.Blocks.GLASS_BLOCKS_SOUL,
        )

        tag(RagiumCommonTags.Blocks.GLASS_BLOCKS_OBSIDIAN).addHolder(RagiumBlocks.OBSIDIAN_GLASS)
        tag(RagiumCommonTags.Blocks.GLASS_BLOCKS_QUARTZ).addHolder(RagiumBlocks.QUARTZ_GLASS)
        tag(RagiumCommonTags.Blocks.GLASS_BLOCKS_SOUL).addHolder(RagiumBlocks.SOUL_GLASS)
        // LED
        RagiumBlocks.LED_BLOCKS.values.forEach(tag(RagiumModTags.Blocks.LED_BLOCKS)::addHolder)
        // Stone
        tag(Tags.Blocks.OBSIDIANS).addTag(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
        tag(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS).addHolder(RagiumBlocks.MYSTERIOUS_OBSIDIAN)
        // Crop
        tag(BlockTags.BEE_GROWABLES).addHolder(RagiumBlocks.EXP_BERRY_BUSH)
        tag(BlockTags.FALL_DAMAGE_RESETTING).addHolder(RagiumBlocks.EXP_BERRY_BUSH)
        tag(BlockTags.SWORD_EFFICIENT).addHolder(RagiumBlocks.EXP_BERRY_BUSH)
        // Others
        RagiumBlocks.STORAGE_BLOCKS.forEach(tag(BlockTags.BEACON_BASE_BLOCKS)::addHolder)

        tag(BlockTags.HOGLIN_REPELLENTS).addHolder(RagiumBlocks.WARPED_CRYSTAL_BLOCK)
        tag(BlockTags.INFINIBURN_OVERWORLD).addHolder(RagiumBlocks.CRIMSON_CRYSTAL_BLOCK)
        tag(BlockTags.SOUL_FIRE_BASE_BLOCKS).addHolder(RagiumBlocks.WARPED_CRYSTAL_BLOCK)
        tag(BlockTags.STRIDER_WARM_BLOCKS).addHolder(RagiumBlocks.CRIMSON_CRYSTAL_BLOCK)

        tag(RagiumModTags.Blocks.WIP)
            .addTags(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
            .addHolder(
                RagiumBlocks.ASH_LOG,
                RagiumBlocks.COOKED_MEAT_ON_THE_BONE,
                RagiumBlocks.WOODEN_CASING,
                RagiumBlocks.STONE_CASING,
                RagiumBlocks.REFINERY,
                RagiumBlocks.SOLIDIFIER,
            ).apply {
                RagiumBlocks.CAULDRONS.forEach { holder: DeferredBlock<*> ->
                    addOptional(holder.id)
                }
            }
    }
}
