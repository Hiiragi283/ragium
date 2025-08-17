package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.extensions.IHolderExtension
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.concurrent.CompletableFuture
import kotlin.enums.enumEntries

class RagiumBlockTagsProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    HTTagsProvider<Block>(output, Registries.BLOCK, provider, helper) {
    override fun addTags(builder: HTTagBuilder<Block>) {
        mineable(builder)
        category(builder)
    }

    //    Mineable    //

    private fun mineable(builder: HTTagBuilder<Block>) {
        // Axe
        builder.add(BlockTags.MINEABLE_WITH_AXE, RagiumBlocks.EXP_BERRY_BUSH)
        builder.add(BlockTags.MINEABLE_WITH_AXE, RagiumBlocks.Casings.WOODEN)
        // Hoe
        builder.add(BlockTags.MINEABLE_WITH_HOE, RagiumBlocks.SWEET_BERRIES_CAKE)
        // Pickaxe
        builder.addTag(BlockTags.MINEABLE_WITH_PICKAXE, RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
        builder.addTag(BlockTags.MINEABLE_WITH_PICKAXE, RagiumModTags.Blocks.LED_BLOCKS)
        builder.add(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.RESONANT_DEBRIS)

        for (slab: RagiumBlocks.Slabs in RagiumBlocks.Slabs.entries) {
            builder.add(BlockTags.MINEABLE_WITH_PICKAXE, slab)
            builder.add(BlockTags.SLABS, slab)
        }
        for (stairs: RagiumBlocks.Stairs in RagiumBlocks.Stairs.entries) {
            builder.add(BlockTags.MINEABLE_WITH_PICKAXE, stairs)
            builder.add(BlockTags.STAIRS, stairs)
        }
        for (wall: RagiumBlocks.Walls in RagiumBlocks.Walls.entries) {
            builder.add(BlockTags.MINEABLE_WITH_PICKAXE, wall)
            builder.add(BlockTags.WALLS, wall)
        }

        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.DECORATION_MAP)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.DRUMS)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.GENERATORS)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.MACHINES)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.MATERIALS.values)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.ORES.values)
        builder.addBlocks<RagiumBlocks.Casings>(BlockTags.MINEABLE_WITH_PICKAXE)
        builder.addBlocks<RagiumBlocks.Devices>(BlockTags.MINEABLE_WITH_PICKAXE)
        builder.addBlocks<RagiumBlocks.Frames>(BlockTags.MINEABLE_WITH_PICKAXE)
        // Shovel
        builder.add(BlockTags.MINEABLE_WITH_SHOVEL, RagiumBlocks.ASH_LOG)
        builder.add(BlockTags.MINEABLE_WITH_SHOVEL, RagiumBlocks.CRIMSON_SOIL)
        builder.add(BlockTags.MINEABLE_WITH_SHOVEL, RagiumBlocks.SILT)
        // Other
        tag(RagiumModTags.Blocks.INCORRECT_FOR_DESTRUCTION_TOOL)

        builder.addTag(RagiumModTags.Blocks.MINEABLE_WITH_DRILL, BlockTags.MINEABLE_WITH_PICKAXE)
        builder.addTag(RagiumModTags.Blocks.MINEABLE_WITH_DRILL, BlockTags.MINEABLE_WITH_SHOVEL)

        builder.addTag(RagiumModTags.Blocks.MINEABLE_WITH_HAMMER, BlockTags.MINEABLE_WITH_PICKAXE)
        builder.addTag(RagiumModTags.Blocks.MINEABLE_WITH_HAMMER, BlockTags.MINEABLE_WITH_SHOVEL)
    }

    //    Category    //

    private fun category(builder: HTTagBuilder<Block>) {
        // Ore
        RagiumBlocks.ORES.forEach { (variant: HTMaterialVariant, material: HTMaterialType, ore: DeferredBlock<*>) ->
            builder.addBlock(variant, material, ore)
            val groundTag: TagKey<Block> = when (variant) {
                HTMaterialVariant.ORE -> Tags.Blocks.ORES_IN_GROUND_STONE
                HTMaterialVariant.DEEP_ORE -> Tags.Blocks.ORES_IN_GROUND_DEEPSLATE
                HTMaterialVariant.NETHER_ORE -> Tags.Blocks.ORES_IN_GROUND_NETHERRACK
                HTMaterialVariant.END_ORE -> RagiumCommonTags.Blocks.ORES_IN_GROUND_END_STONE
                else -> return@forEach
            }
            builder.add(groundTag, ore)
        }
        builder.addTag(Tags.Blocks.ORES, RagiumCommonTags.Blocks.ORES_DEEP_SCRAP)
        builder.add(RagiumCommonTags.Blocks.ORES_DEEP_SCRAP, RagiumBlocks.RESONANT_DEBRIS)
        // Material
        RagiumBlocks.MATERIALS.forEach { (variant: HTMaterialVariant, material: HTMaterialType, block: DeferredBlock<*>) ->
            if (variant == HTMaterialVariant.STORAGE_BLOCK) {
                builder.add(BlockTags.BEACON_BASE_BLOCKS, block)
            }
            builder.addBlock(variant, material, block)
        }
        // LED
        builder.addBlocks<RagiumBlocks.LEDBlocks>(RagiumModTags.Blocks.LED_BLOCKS)
        // Stone
        builder.addBlock(
            Tags.Blocks.OBSIDIANS,
            RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS,
            RagiumBlocks.MYSTERIOUS_OBSIDIAN,
        )
        builder.addTag(RagiumModTags.Blocks.RESONANT_DEBRIS_REPLACEABLES, BlockTags.DEEPSLATE_ORE_REPLACEABLES)
        // Crop
        builder.add(BlockTags.BEE_GROWABLES, RagiumBlocks.EXP_BERRY_BUSH)
        builder.add(BlockTags.FALL_DAMAGE_RESETTING, RagiumBlocks.EXP_BERRY_BUSH)
        builder.add(BlockTags.SWORD_EFFICIENT, RagiumBlocks.EXP_BERRY_BUSH)
        // Other
        builder.add(BlockTags.HOGLIN_REPELLENTS, RagiumBlocks.getStorageBlock(RagiumMaterialType.WARPED_CRYSTAL))
        builder.add(BlockTags.INFINIBURN_OVERWORLD, RagiumBlocks.getStorageBlock(RagiumMaterialType.CRIMSON_CRYSTAL))
        builder.add(BlockTags.SOUL_FIRE_BASE_BLOCKS, RagiumBlocks.getStorageBlock(RagiumMaterialType.WARPED_CRYSTAL))
        builder.add(BlockTags.STRIDER_WARM_BLOCKS, RagiumBlocks.getStorageBlock(RagiumMaterialType.CRIMSON_CRYSTAL))

        // WIP
        builder.addTag(RagiumModTags.Blocks.WIP, RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
        builder.add(RagiumModTags.Blocks.WIP, RagiumBlocks.ASH_LOG)
        builder.add(RagiumModTags.Blocks.WIP, RagiumBlocks.Casings.WOODEN)
    }

    //    Extensions    //

    private fun HTTagBuilder<Block>.addBlock(parent: TagKey<Block>, child: TagKey<Block>, block: IHolderExtension<Block>) {
        addTag(parent, child)
        add(child, block)
    }

    private fun HTTagBuilder<Block>.addBlock(variant: HTMaterialVariant, material: HTMaterialType, block: IHolderExtension<Block>) {
        val tagKey: TagKey<Block> = variant.blockTagKey(material)
        if (variant.generateTag) {
            addTag(variant.blockCommonTag, tagKey)
        }
        add(tagKey, block)
    }

    private fun HTTagBuilder<Block>.addBlocks(tagKey: TagKey<Block>, blocks: Map<*, IHolderExtension<Block>>) {
        addBlocks(tagKey, blocks.values)
    }

    private fun HTTagBuilder<Block>.addBlocks(tagKey: TagKey<Block>, blocks: Iterable<IHolderExtension<Block>>) {
        for (holder: IHolderExtension<Block> in blocks) {
            add(tagKey, holder)
        }
    }

    private inline fun <reified B> HTTagBuilder<Block>.addBlocks(tagKey: TagKey<Block>) where B : IHolderExtension<Block>, B : Enum<B> {
        for (holder: B in enumEntries<B>()) {
            add(tagKey, holder)
        }
    }
}
