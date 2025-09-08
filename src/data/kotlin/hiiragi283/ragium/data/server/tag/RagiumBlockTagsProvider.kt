package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.variant.HTDecorationVariant
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import vectorwing.farmersdelight.common.tag.ModTags
import java.util.concurrent.CompletableFuture

class RagiumBlockTagsProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    HTTagsProvider<Block>(output, Registries.BLOCK, provider, helper) {
    companion object {
        @Suppress("DEPRECATION")
        @JvmField
        val VANILLA_STORAGE_BLOCKS: Map<HTVanillaMaterialType, HTHolderLike> = mapOf(
            HTVanillaMaterialType.AMETHYST to Blocks.AMETHYST_BLOCK,
            HTVanillaMaterialType.GLOWSTONE to Blocks.GLOWSTONE,
            HTVanillaMaterialType.QUARTZ to Blocks.QUARTZ_BLOCK,
        ).mapValues { (_, block: Block) -> HTHolderLike.fromBlock(block) }
    }

    override fun addTags(builder: HTTagBuilder<Block>) {
        mineable(builder)
        category(builder)
    }

    //    Mineable    //

    private fun mineable(builder: HTTagBuilder<Block>) {
        // Axe
        builder.add(BlockTags.MINEABLE_WITH_AXE, RagiumBlocks.EXP_BERRIES)
        builder.add(BlockTags.MINEABLE_WITH_AXE, RagiumBlocks.WOODEN_CASING)
        // Hoe
        builder.add(BlockTags.MINEABLE_WITH_HOE, RagiumBlocks.SWEET_BERRIES_CAKE)
        // Pickaxe
        builder.addTag(BlockTags.MINEABLE_WITH_PICKAXE, RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
        builder.addTag(BlockTags.MINEABLE_WITH_PICKAXE, RagiumModTags.Blocks.LED_BLOCKS)
        builder.add(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.RESONANT_DEBRIS)

        for (variant: HTDecorationVariant in HTDecorationVariant.entries) {
            // Slab
            builder.add(BlockTags.MINEABLE_WITH_PICKAXE, variant.slab)
            builder.add(BlockTags.SLABS, variant.slab)
            // Stairs
            builder.add(BlockTags.MINEABLE_WITH_PICKAXE, variant.stairs)
            builder.add(BlockTags.STAIRS, variant.stairs)
            // Wall
            builder.add(BlockTags.MINEABLE_WITH_PICKAXE, variant.wall)
            builder.add(BlockTags.WALLS, variant.wall)
        }

        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.CASINGS)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.DECORATION_MAP)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.DEVICES.values)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.DRUMS)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.FRAMES)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.GENERATORS)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.MACHINES)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.MATERIALS.values)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.ORES.values)
        // Shovel
        builder.add(BlockTags.MINEABLE_WITH_SHOVEL, RagiumBlocks.CRIMSON_SOIL)
        builder.add(BlockTags.MINEABLE_WITH_SHOVEL, RagiumBlocks.SILT)
        // Other
        tag(RagiumModTags.Blocks.INCORRECT_FOR_DESTRUCTION_TOOL)

        builder.addTag(RagiumModTags.Blocks.MINEABLE_WITH_DRILL, BlockTags.MINEABLE_WITH_PICKAXE)
        builder.addTag(RagiumModTags.Blocks.MINEABLE_WITH_DRILL, BlockTags.MINEABLE_WITH_SHOVEL)

        builder.addTag(RagiumModTags.Blocks.MINEABLE_WITH_HAMMER, BlockTags.MINEABLE_WITH_PICKAXE)
        builder.addTag(RagiumModTags.Blocks.MINEABLE_WITH_HAMMER, BlockTags.MINEABLE_WITH_SHOVEL)

        builder.add(ModTags.MINEABLE_WITH_KNIFE, RagiumDelightAddon.RAGI_CHERRY_PIE)
    }

    //    Category    //

    private fun category(builder: HTTagBuilder<Block>) {
        // Ore
        RagiumBlocks.ORES.forEach { (variant: HTMaterialVariant.BlockTag, material: HTMaterialType, ore: HTHolderLike) ->
            builder.addMaterial(HTBlockMaterialVariant.ORE, material, ore)
            val groundTag: TagKey<Block> = when (variant) {
                HTBlockMaterialVariant.ORE -> Tags.Blocks.ORES_IN_GROUND_STONE
                HTBlockMaterialVariant.DEEP_ORE -> Tags.Blocks.ORES_IN_GROUND_DEEPSLATE
                HTBlockMaterialVariant.NETHER_ORE -> Tags.Blocks.ORES_IN_GROUND_NETHERRACK
                HTBlockMaterialVariant.END_ORE -> RagiumCommonTags.Blocks.ORES_IN_GROUND_END_STONE
                else -> return@forEach
            }
            builder.add(groundTag, ore)
        }
        builder.addTag(Tags.Blocks.ORES, RagiumCommonTags.Blocks.ORES_DEEP_SCRAP)
        builder.add(RagiumCommonTags.Blocks.ORES_DEEP_SCRAP, RagiumBlocks.RESONANT_DEBRIS)
        // Material
        RagiumBlocks.MATERIALS
            .forEach { (variant: HTMaterialVariant.BlockTag, material: HTMaterialType, block: HTHolderLike) ->
                if (variant == HTBlockMaterialVariant.STORAGE_BLOCK) {
                    builder.add(BlockTags.BEACON_BASE_BLOCKS, block)
                }
                builder.addMaterial(variant, material, block)
                if (variant == HTBlockMaterialVariant.TINTED_GLASS_BLOCK) {
                    builder.addMaterial(HTBlockMaterialVariant.GLASS_BLOCK, material, block)
                }
            }

        for ((material: HTVanillaMaterialType, holder: HTHolderLike) in VANILLA_STORAGE_BLOCKS) {
            builder.addMaterial(HTBlockMaterialVariant.STORAGE_BLOCK, material, holder)
        }
        // LED
        builder.addBlocks(RagiumModTags.Blocks.LED_BLOCKS, RagiumBlocks.LED_BLOCKS)
        // Stone
        builder.add(
            Tags.Blocks.OBSIDIANS,
            RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS,
            RagiumBlocks.MYSTERIOUS_OBSIDIAN,
        )
        builder.addTag(RagiumModTags.Blocks.RESONANT_DEBRIS_REPLACEABLES, BlockTags.DEEPSLATE_ORE_REPLACEABLES)
        // Crop
        builder.add(BlockTags.BEE_GROWABLES, RagiumBlocks.EXP_BERRIES)
        builder.add(BlockTags.FALL_DAMAGE_RESETTING, RagiumBlocks.EXP_BERRIES)
        builder.add(BlockTags.SWORD_EFFICIENT, RagiumBlocks.EXP_BERRIES)
        // Other
        builder.add(BlockTags.HOGLIN_REPELLENTS, RagiumBlocks.getStorageBlock(RagiumMaterialType.WARPED_CRYSTAL))
        builder.add(BlockTags.INFINIBURN_OVERWORLD, RagiumBlocks.getStorageBlock(RagiumMaterialType.CRIMSON_CRYSTAL))
        builder.add(BlockTags.SOUL_FIRE_BASE_BLOCKS, RagiumBlocks.getStorageBlock(RagiumMaterialType.WARPED_CRYSTAL))
        builder.add(BlockTags.STRIDER_WARM_BLOCKS, RagiumBlocks.getStorageBlock(RagiumMaterialType.CRIMSON_CRYSTAL))

        // WIP
        builder.addTag(RagiumModTags.Blocks.WIP, RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
        builder.add(RagiumModTags.Blocks.WIP, RagiumBlocks.WOODEN_CASING)
    }

    //    Extensions    //

    private fun HTTagBuilder<Block>.add(parent: TagKey<Block>, child: TagKey<Block>, block: HTHolderLike) {
        addTag(parent, child)
        add(child, block)
    }

    private fun HTTagBuilder<Block>.addMaterial(variant: HTMaterialVariant.BlockTag, material: HTMaterialType, block: HTHolderLike) {
        val blockCommonTag: TagKey<Block> = variant.blockCommonTag ?: return
        val tagKey: TagKey<Block> = variant.blockTagKey(material)
        add(blockCommonTag, tagKey, block)
    }

    private fun HTTagBuilder<Block>.addBlocks(tagKey: TagKey<Block>, blocks: Map<*, HTHolderLike>) {
        addBlocks(tagKey, blocks.values)
    }

    private fun HTTagBuilder<Block>.addBlocks(tagKey: TagKey<Block>, blocks: Iterable<HTHolderLike>) {
        for (holder: HTHolderLike in blocks) {
            add(tagKey, holder)
        }
    }
}
