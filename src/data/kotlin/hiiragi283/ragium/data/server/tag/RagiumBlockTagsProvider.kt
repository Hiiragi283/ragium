package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.collection.ImmutableMultiMap
import hiiragi283.ragium.api.collection.buildMultiMap
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.HTDecorationType
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.variant.HTGlassVariant
import hiiragi283.ragium.common.variant.HTOreVariant
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

class RagiumBlockTagsProvider(context: HTDataGenContext) : HTTagsProvider<Block>(Registries.BLOCK, context) {
    companion object {
        @JvmField
        val VANILLA_STORAGE_BLOCKS: Map<HTMaterialKey, HTHolderLike> = mapOf(
            VanillaMaterialKeys.AMETHYST to Blocks.AMETHYST_BLOCK.toHolderLike(),
            VanillaMaterialKeys.GLOWSTONE to Blocks.GLOWSTONE.toHolderLike(),
            VanillaMaterialKeys.QUARTZ to Blocks.QUARTZ_BLOCK.toHolderLike(),
        )

        @JvmStatic
        val STORAGE_BLOCK_TOOL: ImmutableMultiMap<HTMaterialKey, TagKey<Block>> = buildMultiMap {
            this[FoodMaterialKeys.CHOCOLATE] = BlockTags.MINEABLE_WITH_PICKAXE
            this[FoodMaterialKeys.CHOCOLATE] = BlockTags.MINEABLE_WITH_SHOVEL

            this[FoodMaterialKeys.RAW_MEAT] = BlockTags.MINEABLE_WITH_SHOVEL

            this[FoodMaterialKeys.COOKED_MEAT] = BlockTags.MINEABLE_WITH_SHOVEL
        }
    }

    override fun addTagsInternal(factory: BuilderFactory<Block>) {
        mineable(factory)
        category(factory)
    }

    //    Mineable    //

    private fun mineable(factory: BuilderFactory<Block>) {
        // Axe
        factory
            .apply(BlockTags.MINEABLE_WITH_AXE)
            .add(RagiumBlocks.EXP_BERRIES)
        // Hoe
        factory
            .apply(BlockTags.MINEABLE_WITH_HOE)
            .add(RagiumBlocks.SWEET_BERRIES_CAKE)
        // Pickaxe
        val pickaxe: HTTagBuilder<Block> = factory.apply(BlockTags.MINEABLE_WITH_PICKAXE)
        buildList {
            add(RagiumBlocks.BUDDING_QUARTZ)
            add(RagiumBlocks.QUARTZ_CLUSTER)
            add(RagiumBlocks.RESONANT_DEBRIS)
            add(RagiumBlocks.IMITATION_SPAWNER)
            add(RagiumBlocks.SMOOTH_BLACKSTONE)
            add(RagiumBlocks.SOOTY_COBBLESTONE)
            // Generators
            add(RagiumBlocks.THERMAL_GENERATOR)

            add(RagiumBlocks.CULINARY_GENERATOR)
            add(RagiumBlocks.MAGMATIC_GENERATOR)

            add(RagiumBlocks.COMBUSTION_GENERATOR)
            add(RagiumBlocks.SOLAR_PANEL_UNIT)
            add(RagiumBlocks.SOLAR_PANEL_CONTROLLER)

            add(RagiumBlocks.ENCHANTMENT_GENERATOR)
            add(RagiumBlocks.NUCLEAR_REACTOR)
            // Processors
            add(RagiumBlocks.ALLOY_SMELTER)
            add(RagiumBlocks.BLOCK_BREAKER)
            add(RagiumBlocks.COMPRESSOR)
            add(RagiumBlocks.CUTTING_MACHINE)
            add(RagiumBlocks.ELECTRIC_FURNACE)
            add(RagiumBlocks.EXTRACTOR)
            add(RagiumBlocks.PULVERIZER)

            add(RagiumBlocks.CRUSHER)
            add(RagiumBlocks.MELTER)
            add(RagiumBlocks.MIXER)
            add(RagiumBlocks.REFINERY)

            add(RagiumBlocks.ADVANCED_MIXER)
            add(RagiumBlocks.BREWERY)
            add(RagiumBlocks.MULTI_SMELTER)
            add(RagiumBlocks.PLANTER)

            add(RagiumBlocks.ENCHANTER)
            add(RagiumBlocks.MOB_CRUSHER)
            add(RagiumBlocks.SIMULATOR)
            // Devices
            add(RagiumBlocks.DEVICE_CASING)

            add(RagiumBlocks.FLUID_COLLECTOR)
            add(RagiumBlocks.ITEM_COLLECTOR)

            add(RagiumBlocks.STONE_COLLECTOR)

            add(RagiumBlocks.DIM_ANCHOR)
            add(RagiumBlocks.ENI)

            add(RagiumBlocks.TELEPAD)

            add(RagiumBlocks.CEU)
            // Storage
            add(RagiumBlocks.CRATE)

            add(RagiumBlocks.TANK)
        }.forEach(pickaxe::add)

        pickaxe
            .addTag(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
            .addTag(RagiumModTags.Blocks.LED_BLOCKS)

        for (type: HTDecorationType in HTDecorationType.entries) {
            // Slab
            pickaxe.add(type.slab)
            factory.apply(BlockTags.SLABS).add(type.slab)
            // Stairs
            pickaxe.add(type.stairs)
            factory.apply(BlockTags.STAIRS).add(type.stairs)
            // Wall
            pickaxe.add(type.wall)
            factory.apply(BlockTags.WALLS).add(type.wall)
        }

        pickaxe.addBlocks(RagiumBlocks.COILS)
        pickaxe.addBlocks(RagiumBlocks.DECORATION_MAP)
        pickaxe.addBlocks(RagiumBlocks.GLASSES.values)
        pickaxe.addBlocks(RagiumBlocks.METAL_BARS)
        pickaxe.addBlocks(RagiumBlocks.ORES.values)

        RagiumBlocks.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, block: HTHolderLike) ->
            if (prefix.isOf(CommonMaterialPrefixes.STORAGE_BLOCK)) {
                val tagKeys: Collection<TagKey<Block>> = STORAGE_BLOCK_TOOL[key]
                    .takeUnless(Collection<TagKey<Block>>::isEmpty)
                    ?: listOf(BlockTags.MINEABLE_WITH_PICKAXE)
                for (builder: HTTagBuilder<Block> in tagKeys.map(factory::apply)) {
                    builder.add(block)
                }
            } else {
                pickaxe.add(block)
            }
        }
        // Shovel
        factory
            .apply(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(RagiumBlocks.CRIMSON_SOIL)
            .add(RagiumBlocks.SILT)
        // Other
        factory.apply(RagiumModTags.Blocks.INCORRECT_FOR_DESTRUCTION_TOOL)

        factory
            .apply(RagiumModTags.Blocks.MINEABLE_WITH_DRILL)
            .addTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(BlockTags.MINEABLE_WITH_SHOVEL)

        factory
            .apply(RagiumModTags.Blocks.MINEABLE_WITH_HAMMER)
            .addTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(BlockTags.MINEABLE_WITH_SHOVEL)
    }

    //    Category    //

    private fun category(factory: BuilderFactory<Block>) {
        // Ore
        RagiumBlocks.ORES.forEach { (variant: HTOreVariant, key: HTMaterialKey, ore: HTHolderLike) ->
            addMaterial(factory, CommonMaterialPrefixes.ORE, key).add(ore)
            when (variant) {
                HTOreVariant.DEFAULT -> Tags.Blocks.ORES_IN_GROUND_STONE
                HTOreVariant.DEEP -> Tags.Blocks.ORES_IN_GROUND_DEEPSLATE
                HTOreVariant.NETHER -> Tags.Blocks.ORES_IN_GROUND_NETHERRACK
                HTOreVariant.END -> RagiumCommonTags.Blocks.ORES_IN_GROUND_END_STONE
            }.let(factory::apply).add(ore)

            if (variant == HTOreVariant.END) {
                factory.apply(BlockTags.DRAGON_IMMUNE).add(ore)
            }
        }
        addTags(factory, Tags.Blocks.ORES, RagiumCommonTags.Blocks.ORES_DEEP_SCRAP)
            .add(RagiumBlocks.RESONANT_DEBRIS)
        // Material
        RagiumBlocks.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, block: HTHolderLike) ->
            addMaterial(factory, prefix, key).add(block)
            if (prefix.isOf(CommonMaterialPrefixes.STORAGE_BLOCK)) {
                if (STORAGE_BLOCK_TOOL[key].isEmpty()) {
                    factory.apply(BlockTags.BEACON_BASE_BLOCKS).add(block)
                }
            }
        }

        for ((key: HTMaterialKey, holder: HTHolderLike) in VANILLA_STORAGE_BLOCKS) {
            addMaterial(factory, CommonMaterialPrefixes.STORAGE_BLOCK, key).add(holder)
        }
        // Glasses
        RagiumBlocks.GLASSES.forEach { (variant: HTGlassVariant, key: HTMaterialKey, glass: HTHolderLike) ->
            when (variant) {
                HTGlassVariant.DEFAULT -> addMaterial(factory, CommonMaterialPrefixes.GLASS_BLOCK, key)
                HTGlassVariant.TINTED -> factory.apply(Tags.Blocks.GLASS_BLOCKS_TINTED)
            }.add(glass)
        }

        // LED
        factory.apply(RagiumModTags.Blocks.LED_BLOCKS).addBlocks(RagiumBlocks.LED_BLOCKS)
        // Stone
        addTags(factory, Tags.Blocks.OBSIDIANS, RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS)
            .add(RagiumBlocks.MYSTERIOUS_OBSIDIAN)
        factory
            .apply(RagiumModTags.Blocks.RESONANT_DEBRIS_REPLACEABLES)
            .add(Blocks.DEEPSLATE.toHolderLike())
        // Crop
        factory.apply(BlockTags.BEE_GROWABLES).add(RagiumBlocks.EXP_BERRIES)
        factory.apply(BlockTags.FALL_DAMAGE_RESETTING).add(RagiumBlocks.EXP_BERRIES)
        factory.apply(BlockTags.SWORD_EFFICIENT).add(RagiumBlocks.EXP_BERRIES)
        // Other
        factory
            .apply(BlockTags.CRYSTAL_SOUND_BLOCKS)
            .add(RagiumBlocks.BUDDING_QUARTZ)
        factory
            .apply(BlockTags.HOGLIN_REPELLENTS)
            .add(RagiumBlocks.getStorageBlock(RagiumMaterialKeys.WARPED_CRYSTAL))
        factory
            .apply(BlockTags.INFINIBURN_OVERWORLD)
            .add(RagiumBlocks.getStorageBlock(RagiumMaterialKeys.CRIMSON_CRYSTAL))
        factory
            .apply(BlockTags.SOUL_FIRE_BASE_BLOCKS)
            .add(RagiumBlocks.getStorageBlock(RagiumMaterialKeys.WARPED_CRYSTAL))
        factory
            .apply(BlockTags.STRIDER_WARM_BLOCKS)
            .add(RagiumBlocks.getStorageBlock(RagiumMaterialKeys.CRIMSON_CRYSTAL))
        factory
            .apply(Tags.Blocks.CLUSTERS)
            .add(RagiumBlocks.QUARTZ_CLUSTER)
        factory
            .apply(Tags.Blocks.BUDDING_BLOCKS)
            .add(RagiumBlocks.BUDDING_QUARTZ)
    }

    //    Extensions    //

    private fun HTTagBuilder<Block>.addBlocks(blocks: Map<*, HTHolderLike>) {
        this.addBlocks(blocks.values)
    }

    private fun HTTagBuilder<Block>.addBlocks(blocks: Iterable<HTHolderLike>) {
        blocks.forEach(this::add)
    }
}
