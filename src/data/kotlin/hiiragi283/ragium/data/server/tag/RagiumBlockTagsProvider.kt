package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.asBlockHolder
import hiiragi283.ragium.api.registry.HTBlockHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.HTBuildingBlockSets
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.IntrinsicHolderTagsProvider
import net.minecraft.data.tags.IntrinsicHolderTagsProvider.IntrinsicTagAppender
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier
import kotlin.enums.enumEntries

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

        fun addOres(ores: Iterable<HTBlockHolderLike.Materialized>, groundTag: TagKey<Block>) {
            for (ore: HTBlockHolderLike.Materialized in ores) {
                pickaxe.addBlock(ore)
                tag(ore.material.blockTag(RagiumConst.ORES)).addBlock(ore)
                tag(groundTag).addBlock(ore)
            }
        }
        addOres(RagiumBlocks.Ores.entries, Tags.Blocks.ORES_IN_GROUND_STONE)
        addOres(RagiumBlocks.DeepOres.entries, Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)
        addOres(RagiumBlocks.NetherOres.entries, Tags.Blocks.ORES_IN_GROUND_NETHERRACK)
        addOres(RagiumBlocks.EndOres.entries, RagiumCommonTags.Blocks.ORES_IN_GROUND_END_STONE)

        tag(Tags.Blocks.ORES)
            .addTag(RagiumCommonTags.Blocks.ORES_RAGI_CRYSTAL)
            .addTag(RagiumCommonTags.Blocks.ORES_RAGINITE)

        addBlock(Tags.Blocks.ORES, RagiumCommonTags.Blocks.ORES_DEEP_SCRAP, RagiumBlocks.RESONANT_DEBRIS)

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

        pickaxe.addBlocks<RagiumBlocks.Casings>()
        pickaxe.addBlocks<RagiumBlocks.Devices>()
        pickaxe.addBlocks<RagiumBlocks.Drums>()
        pickaxe.addBlocks<RagiumBlocks.Dynamos>()
        pickaxe.addBlocks<RagiumBlocks.Frames>()
        pickaxe.addBlocks<RagiumBlocks.Glasses>()
        pickaxe.addBlocks<RagiumBlocks.Machines>()

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
        tag(RagiumModTags.Blocks.LED_BLOCKS).addBlocks<RagiumBlocks.LEDBlocks>()
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

        tag(RagiumModTags.Blocks.MINEABLE_WITH_DRILL)
            .addTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(BlockTags.MINEABLE_WITH_SHOVEL)

        tag(RagiumModTags.Blocks.WIP)
            .addTag(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
            .addBlock(RagiumBlocks.ASH_LOG)
            .addBlock(RagiumBlocks.Casings.WOODEN)
            .addBlock(RagiumBlocks.Casings.STONE)
    }

    //    Extensions    //

    private fun addBlock(parent: TagKey<Block>, child: TagKey<Block>, block: Supplier<out Block>) {
        tag(parent).addTag(child)
        tag(child).add(block.get())
    }
}

private fun IntrinsicTagAppender<Block>.addBlock(holder: DeferredBlock<*>): IntrinsicTagAppender<Block> = apply {
    holder.unwrapKey().ifPresent(::add)
}

private fun IntrinsicTagAppender<Block>.addBlock(holderLike: HTBlockHolderLike): IntrinsicTagAppender<Block> = addBlock(holderLike.holder)

private inline fun <reified B> IntrinsicTagAppender<Block>.addBlocks(): IntrinsicTagAppender<Block> where B : HTBlockHolderLike, B : Enum<B> =
    apply {
        enumEntries<B>().mapNotNull(HTBlockHolderLike::getKey).forEach(::add)
    }
