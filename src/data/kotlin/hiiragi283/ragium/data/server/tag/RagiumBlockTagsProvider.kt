package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.asBlockHolder
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.registry.HTBlockHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.material.RagiumMaterialType
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
        RagiumBlocks.ORES.values.forEach(pickaxe::addBlock)
        RagiumBlocks.MATERIALS.values.forEach(pickaxe::addBlock)

        RagiumBlocks.DECORATION_MAP.values.forEach(pickaxe::addBlock)
        for (slab: RagiumBlocks.Slabs in RagiumBlocks.Slabs.entries) {
            pickaxe.addBlock(slab)
            tag(BlockTags.SLABS).addBlock(slab)
        }
        for (stairs: RagiumBlocks.Stairs in RagiumBlocks.Stairs.entries) {
            pickaxe.addBlock(stairs)
            tag(BlockTags.STAIRS).addBlock(stairs)
        }
        for (wall: RagiumBlocks.Walls in RagiumBlocks.Walls.entries) {
            pickaxe.addBlock(wall)
            tag(BlockTags.WALLS).addBlock(wall)
        }

        pickaxe.addBlocks<RagiumBlocks.Casings>()
        pickaxe.addBlocks<RagiumBlocks.Devices>()
        RagiumBlocks.DRUMS.values.forEach(pickaxe::addBlock)
        pickaxe.addBlocks<RagiumBlocks.Generators>()
        pickaxe.addBlocks<RagiumBlocks.Frames>()
        pickaxe.addBlocks<RagiumBlocks.Machines>()

        // Shovel
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .addBlock(RagiumBlocks.ASH_LOG)
            .addBlock(RagiumBlocks.CRIMSON_SOIL)
            .addBlock(RagiumBlocks.SILT)
        // Other
    }

    private fun category() {
        // Ore
        RagiumBlocks.ORES.forEach { (variant: HTMaterialVariant, material: HTMaterialType, ore: DeferredBlock<*>) ->
            addBlock(variant, material, ore)
            val groundTag: TagKey<Block> = when (variant) {
                HTMaterialVariant.ORE -> Tags.Blocks.ORES_IN_GROUND_STONE
                HTMaterialVariant.DEEP_ORE -> Tags.Blocks.ORES_IN_GROUND_DEEPSLATE
                HTMaterialVariant.NETHER_ORE -> Tags.Blocks.ORES_IN_GROUND_NETHERRACK
                HTMaterialVariant.END_ORE -> RagiumCommonTags.Blocks.ORES_IN_GROUND_END_STONE
                else -> return@forEach
            }
            tag(groundTag).addBlock(ore)
        }

        tag(Tags.Blocks.ORES).addTag(RagiumCommonTags.Blocks.ORES_DEEP_SCRAP)
        tag(RagiumCommonTags.Blocks.ORES_DEEP_SCRAP).addBlock(RagiumBlocks.RESONANT_DEBRIS)
        // Material
        RagiumBlocks.MATERIALS.forEach { (variant: HTMaterialVariant, material: HTMaterialType, block: DeferredBlock<*>) ->
            if (variant == HTMaterialVariant.STORAGE_BLOCK) {
                tag(BlockTags.BEACON_BASE_BLOCKS).addBlock(block)
            }
            addBlock(variant, material, block)
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
        tag(BlockTags.HOGLIN_REPELLENTS).addBlock(RagiumBlocks.getStorageBlock(RagiumMaterialType.WARPED_CRYSTAL))
        tag(BlockTags.INFINIBURN_OVERWORLD).addBlock(RagiumBlocks.getStorageBlock(RagiumMaterialType.CRIMSON_CRYSTAL))
        tag(BlockTags.SOUL_FIRE_BASE_BLOCKS).addBlock(RagiumBlocks.getStorageBlock(RagiumMaterialType.WARPED_CRYSTAL))
        tag(BlockTags.STRIDER_WARM_BLOCKS).addBlock(RagiumBlocks.getStorageBlock(RagiumMaterialType.CRIMSON_CRYSTAL))

        tag(RagiumModTags.Blocks.INCORRECT_FOR_DESTRUCTION_TOOL)

        tag(RagiumModTags.Blocks.MINEABLE_WITH_DRILL)
            .addTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(BlockTags.MINEABLE_WITH_SHOVEL)

        tag(RagiumModTags.Blocks.MINEABLE_WITH_HAMMER)
            .addTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(BlockTags.MINEABLE_WITH_SHOVEL)

        tag(RagiumModTags.Blocks.WIP)
            .addTag(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
            .addBlock(RagiumBlocks.ASH_LOG)
            .addBlock(RagiumBlocks.Casings.WOODEN)
            .addBlock(RagiumBlocks.Casings.STONE)
    }

    //    Extensions    //

    private fun addBlock(variant: HTMaterialVariant, material: HTMaterialType, block: DeferredBlock<*>) {
        val tagKey: TagKey<Block> = variant.blockTagKey(material)
        if (variant.generateTag) {
            tag(variant.blockCommonTag).addTag(tagKey)
        }
        tag(tagKey).addBlock(block)
    }
}

private fun IntrinsicTagAppender<Block>.addBlock(holder: DeferredBlock<*>): IntrinsicTagAppender<Block> = apply {
    holder.unwrapKey().ifPresent(::add)
}

private fun IntrinsicTagAppender<Block>.addBlock(holderLike: HTBlockHolderLike): IntrinsicTagAppender<Block> = addBlock(holderLike.holder)

private inline fun <reified B> IntrinsicTagAppender<Block>.addBlocks(): IntrinsicTagAppender<Block>
where B : HTBlockHolderLike, B : Enum<B> =
    apply {
        enumEntries<B>().mapNotNull(HTBlockHolderLike::getKey).forEach(::add)
    }
