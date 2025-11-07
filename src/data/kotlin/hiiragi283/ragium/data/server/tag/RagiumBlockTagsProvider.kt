package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.integration.food.RagiumDelightAddon
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.variant.HTDecorationVariant
import hiiragi283.ragium.common.variant.HTOreVariant
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import vectorwing.farmersdelight.common.tag.ModTags

class RagiumBlockTagsProvider(context: HTDataGenContext) : HTTagsProvider<Block>(Registries.BLOCK, context) {
    companion object {
        @JvmField
        val VANILLA_STORAGE_BLOCKS: Map<HTMaterialKey, HTHolderLike> = mapOf(
            VanillaMaterialKeys.AMETHYST to Blocks.AMETHYST_BLOCK.toHolderLike(),
            VanillaMaterialKeys.GLOWSTONE to Blocks.GLOWSTONE.toHolderLike(),
            VanillaMaterialKeys.QUARTZ to Blocks.QUARTZ_BLOCK.toHolderLike(),
        )
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
        buildList {
            add(RagiumBlocks.AZURE_CLUSTER)
            add(RagiumBlocks.RESONANT_DEBRIS)
            // Generators
            add(RagiumBlocks.THERMAL_GENERATOR)

            add(RagiumBlocks.COMBUSTION_GENERATOR)

            add(RagiumBlocks.SOLAR_PANEL_CONTROLLER)

            add(RagiumBlocks.ENCHANTMENT_GENERATOR)
            add(RagiumBlocks.NUCLEAR_REACTOR)
            // Consumers
            add(RagiumBlocks.ALLOY_SMELTER)
            add(RagiumBlocks.BLOCK_BREAKER)
            add(RagiumBlocks.COMPRESSOR)
            add(RagiumBlocks.CUTTING_MACHINE)
            add(RagiumBlocks.EXTRACTOR)
            add(RagiumBlocks.PULVERIZER)

            add(RagiumBlocks.CRUSHER)
            add(RagiumBlocks.MELTER)
            add(RagiumBlocks.REFINERY)
            add(RagiumBlocks.WASHER)

            add(RagiumBlocks.BREWERY)
            add(RagiumBlocks.MULTI_SMELTER)
            add(RagiumBlocks.PLANTER)
            add(RagiumBlocks.SIMULATOR)
            // Devices
            add(RagiumBlocks.DEVICE_CASING)

            add(RagiumBlocks.ITEM_BUFFER)
            add(RagiumBlocks.MILK_COLLECTOR)
            add(RagiumBlocks.WATER_COLLECTOR)

            add(RagiumBlocks.EXP_COLLECTOR)
            add(RagiumBlocks.LAVA_COLLECTOR)

            add(RagiumBlocks.DIM_ANCHOR)
            add(RagiumBlocks.ENI)

            add(RagiumBlocks.MOB_CAPTURER)
            add(RagiumBlocks.TELEPAD)

            add(RagiumBlocks.CEU)
            // Storage
            add(RagiumBlocks.EXP_DRUM)
        }.forEach { builder.add(BlockTags.MINEABLE_WITH_PICKAXE, it) }
        builder.addTag(BlockTags.MINEABLE_WITH_PICKAXE, RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
        builder.addTag(BlockTags.MINEABLE_WITH_PICKAXE, RagiumModTags.Blocks.LED_BLOCKS)

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
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.COILS)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.CRATES)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.DECORATION_MAP)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.DRUMS)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.MATERIALS.values)
        builder.addBlocks(BlockTags.MINEABLE_WITH_PICKAXE, RagiumBlocks.ORES.values)
        // Shovel
        builder.add(BlockTags.MINEABLE_WITH_SHOVEL, RagiumBlocks.CRIMSON_SOIL)
        builder.add(BlockTags.MINEABLE_WITH_SHOVEL, RagiumBlocks.SILT)
        // Other
        @Suppress("DEPRECATION")
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
        RagiumBlocks.ORES.forEach { (variant: HTOreVariant, key, ore: HTHolderLike) ->
            builder.addMaterial(CommonMaterialPrefixes.ORE, key, ore)
            val groundTag: TagKey<Block> = when (variant) {
                HTOreVariant.DEFAULT -> Tags.Blocks.ORES_IN_GROUND_STONE
                HTOreVariant.DEEP -> Tags.Blocks.ORES_IN_GROUND_DEEPSLATE
                HTOreVariant.NETHER -> Tags.Blocks.ORES_IN_GROUND_NETHERRACK
                HTOreVariant.END -> RagiumCommonTags.Blocks.ORES_IN_GROUND_END_STONE
            }
            builder.add(groundTag, ore)

            if (variant == HTOreVariant.END) {
                builder.add(BlockTags.DRAGON_IMMUNE, ore)
            }
        }
        builder.addMaterial(CommonMaterialPrefixes.ORE, RagiumMaterialKeys.DEEP_SCRAP, RagiumBlocks.RESONANT_DEBRIS)
        // Material
        RagiumBlocks.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, block: HTHolderLike) ->
            builder.addMaterial(prefix, key, block)
            if (prefix == CommonMaterialPrefixes.STORAGE_BLOCK.asMaterialPrefix()) {
                builder.add(BlockTags.BEACON_BASE_BLOCKS, block)
            }
        }

        for ((key: HTMaterialKey, holder: HTHolderLike) in VANILLA_STORAGE_BLOCKS) {
            builder.addMaterial(CommonMaterialPrefixes.STORAGE_BLOCK, key, holder)
        }
        // LED
        builder.addBlocks(RagiumModTags.Blocks.LED_BLOCKS, RagiumBlocks.LED_BLOCKS)
        // Stone
        builder.addTags(Tags.Blocks.OBSIDIANS, RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS, RagiumBlocks.MYSTERIOUS_OBSIDIAN)
        builder.addTag(RagiumModTags.Blocks.RESONANT_DEBRIS_REPLACEABLES, BlockTags.DEEPSLATE_ORE_REPLACEABLES)
        // Crop
        builder.add(BlockTags.BEE_GROWABLES, RagiumBlocks.EXP_BERRIES)
        builder.add(BlockTags.FALL_DAMAGE_RESETTING, RagiumBlocks.EXP_BERRIES)
        builder.add(BlockTags.SWORD_EFFICIENT, RagiumBlocks.EXP_BERRIES)
        // Other
        builder.add(BlockTags.HOGLIN_REPELLENTS, RagiumBlocks.getStorageBlock(RagiumMaterialKeys.WARPED_CRYSTAL))
        builder.add(BlockTags.INFINIBURN_OVERWORLD, RagiumBlocks.getStorageBlock(RagiumMaterialKeys.CRIMSON_CRYSTAL))
        builder.add(BlockTags.SOUL_FIRE_BASE_BLOCKS, RagiumBlocks.getStorageBlock(RagiumMaterialKeys.WARPED_CRYSTAL))
        builder.add(BlockTags.STRIDER_WARM_BLOCKS, RagiumBlocks.getStorageBlock(RagiumMaterialKeys.CRIMSON_CRYSTAL))
        builder.add(Tags.Blocks.CLUSTERS, RagiumBlocks.AZURE_CLUSTER)

        // WIP
        builder.add(RagiumModTags.Blocks.WIP, RagiumBlocks.AZURE_CLUSTER)
        builder.add(RagiumModTags.Blocks.WIP, RagiumBlocks.BREWERY)
        builder.add(RagiumModTags.Blocks.WIP, RagiumBlocks.NUCLEAR_REACTOR)
        builder.add(RagiumModTags.Blocks.WIP, RagiumBlocks.SOLAR_PANEL_CONTROLLER)
        builder.add(RagiumModTags.Blocks.WIP, RagiumBlocks.TELEPAD)
        builder.addBlocks(RagiumModTags.Blocks.WIP, RagiumBlocks.CASINGS)
        builder.addTag(RagiumModTags.Blocks.WIP, RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
    }

    //    Extensions    //

    private fun HTTagBuilder<Block>.addBlocks(tagKey: TagKey<Block>, blocks: Map<*, HTHolderLike>) {
        addBlocks(tagKey, blocks.values)
    }

    private fun HTTagBuilder<Block>.addBlocks(tagKey: TagKey<Block>, blocks: Iterable<HTHolderLike>) {
        for (holder: HTHolderLike in blocks) {
            add(tagKey, holder)
        }
    }
}
