package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeJsonBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeJsonBuilder
import hiiragi283.ragium.api.extension.getAroundPos
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.block.HTGeneratorBlockEntityBase
import hiiragi283.ragium.api.machine.block.HTMachineEntityFactory
import hiiragi283.ragium.api.machine.block.HTProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.property.HTMachineTooltipAppender
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.TriConsumer
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import hiiragi283.ragium.common.machine.*
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.block.Block
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.FluidTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.BiConsumer

@Suppress("DEPRECATION")
object RagiumDefaultPlugin : RagiumPlugin {
    override val priority: Int = -100

    override fun registerMachineType(consumer: BiConsumer<HTMachineKey, HTMachineType>) {
        // consumer
        RagiumMachineKeys.CONSUMERS.forEach { consumer.accept(it, HTMachineType.CONSUMER) }
        // generators
        RagiumMachineKeys.GENERATORS.forEach { consumer.accept(it, HTMachineType.GENERATOR) }
        // processors
        RagiumMachineKeys.PROCESSORS.forEach { consumer.accept(it, HTMachineType.PROCESSOR) }
    }

    override fun registerMaterial(consumer: TriConsumer<HTMaterialKey, HTMaterialKey.Type, Rarity>) {
        // alloy
        consumer.accept(RagiumMaterialKeys.DEEP_STEEL, HTMaterialKey.Type.ALLOY, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.RAGI_ALLOY, HTMaterialKey.Type.ALLOY, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.RAGI_STEEL, HTMaterialKey.Type.ALLOY, Rarity.UNCOMMON)
        consumer.accept(RagiumMaterialKeys.REFINED_RAGI_STEEL, HTMaterialKey.Type.ALLOY, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.STEEL, HTMaterialKey.Type.ALLOY, Rarity.UNCOMMON)
        // dust
        consumer.accept(RagiumMaterialKeys.ALKALI, HTMaterialKey.Type.DUST, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.ASH, HTMaterialKey.Type.DUST, Rarity.COMMON)
        // gem
        consumer.accept(RagiumMaterialKeys.CRYOLITE, HTMaterialKey.Type.GEM, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.FLUORITE, HTMaterialKey.Type.GEM, Rarity.UNCOMMON)
        consumer.accept(RagiumMaterialKeys.RAGI_CRYSTAL, HTMaterialKey.Type.GEM, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.RAGIUM, HTMaterialKey.Type.GEM, Rarity.EPIC)
        // metal
        consumer.accept(RagiumMaterialKeys.ALUMINUM, HTMaterialKey.Type.METAL, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.COPPER, HTMaterialKey.Type.METAL, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.GOLD, HTMaterialKey.Type.METAL, Rarity.UNCOMMON)
        consumer.accept(RagiumMaterialKeys.IRON, HTMaterialKey.Type.METAL, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.SILICON, HTMaterialKey.Type.METAL, Rarity.UNCOMMON)
        // mineral
        consumer.accept(RagiumMaterialKeys.BAUXITE, HTMaterialKey.Type.MINERAL, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.CRUDE_RAGINITE, HTMaterialKey.Type.MINERAL, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.NITER, HTMaterialKey.Type.MINERAL, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.RAGINITE, HTMaterialKey.Type.MINERAL, Rarity.UNCOMMON)
        consumer.accept(RagiumMaterialKeys.SULFUR, HTMaterialKey.Type.MINERAL, Rarity.COMMON)
        // plate
        consumer.accept(RagiumMaterialKeys.ENGINEERING_PLASTIC, HTMaterialKey.Type.PLATE, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.PLASTIC, HTMaterialKey.Type.PLATE, Rarity.UNCOMMON)
        consumer.accept(RagiumMaterialKeys.STELLA, HTMaterialKey.Type.PLATE, Rarity.EPIC)
        consumer.accept(RagiumMaterialKeys.WOOD, HTMaterialKey.Type.PLATE, Rarity.COMMON)
    }

    override fun setupCommonMachineProperties(helper: RagiumPlugin.PropertyHelper<HTMachineKey>) {
        // consumers
        helper.modify(RagiumMachineKeys.DRAIN) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTDrainBlockEntity))
        }
        // generators
        helper.modify(RagiumMachineKeys.GENERATORS::contains) {
            set(HTMachinePropertyKeys.MODEL_ID, RagiumAPI.id("block/generator"))
        }
        helper.modify(RagiumMachineKeys.COMBUSTION_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTCombustionGeneratorBlockEntity))
            set(HTMachinePropertyKeys.GENERATOR_COLOR, DyeColor.BLUE)
        }
        helper.modify(RagiumMachineKeys.SOLAR_PANEL) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTGeneratorBlockEntityBase::Simple))
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, _: BlockPos -> world.isDay }
            set(HTMachinePropertyKeys.MODEL_ID, RagiumAPI.id("block/solar_generator"))
            set(HTMachinePropertyKeys.VOXEL_SHAPE, Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0))
        }
        helper.modify(RagiumMachineKeys.STEAM_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTSteamGeneratorBlockEntity))
            set(HTMachinePropertyKeys.FRONT_MAPPER) { Direction.UP }
        }
        helper.modify(RagiumMachineKeys.THERMAL_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTGeneratorBlockEntityBase::Simple))
            set(HTMachinePropertyKeys.GENERATOR_COLOR, DyeColor.ORANGE)
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, pos: BlockPos ->
                when {
                    world.getBiome(pos).isIn(BiomeTags.IS_NETHER) -> true
                    else ->
                        pos
                            .getAroundPos {
                                val fluidState: FluidState = world.getFluidState(it)
                                fluidState.isIn(FluidTags.LAVA) && fluidState.isStill
                            }.size >= 4
                }
            }
        }
        helper.modify(RagiumMachineKeys.WATER_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTGeneratorBlockEntityBase::Simple))
            set(HTMachinePropertyKeys.GENERATOR_COLOR, DyeColor.CYAN)
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, pos: BlockPos ->
                pos.getAroundPos { world.getFluidState(it).isIn(FluidTags.WATER) }.size >= 2
            }
        }
        // processors
        helper.modify(RagiumMachineKeys.PROCESSORS::contains) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTProcessorBlockEntityBase::Simple))
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
        helper.modify(RagiumMachineKeys.BLAST_FURNACE) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTBlastFurnaceBlockEntity))
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
        helper.modify(RagiumMachineKeys.DISTILLATION_TOWER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTDistillationTowerBlockEntity))
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
        helper.modify(RagiumMachineKeys.FLUID_DRILL) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTFluidDrillBlockEntity))
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
        helper.modify(RagiumMachineKeys.MULTI_SMELTER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTMultiSmelterBlockEntity))
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
        helper.modify(RagiumMachineKeys.SAW_MILL) {
            set(HTMachinePropertyKeys.FRONT_TEX) { Identifier.of("block/stonecutter_saw") }
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTSawmillBlockEntity))
        }
    }

    override fun bindMaterialToItem(consumer: TriConsumer<HTTagPrefix, HTMaterialKey, Item>) {
        fun bindContents(contents: List<HTContent.Material<*>>) {
            contents.forEach { consumer.accept(it.tagPrefix, it.material, it.asItem()) }
        }

        consumer.accept(HTTagPrefix.DEEP_ORE, RagiumMaterialKeys.COPPER, Items.DEEPSLATE_COPPER_ORE)
        consumer.accept(HTTagPrefix.DEEP_ORE, RagiumMaterialKeys.GOLD, Items.DEEPSLATE_GOLD_ORE)
        consumer.accept(HTTagPrefix.DEEP_ORE, RagiumMaterialKeys.IRON, Items.DEEPSLATE_IRON_ORE)

        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER, Items.COPPER_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.GOLD, Items.GOLD_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON, Items.IRON_INGOT)

        consumer.accept(HTTagPrefix.NUGGET, RagiumMaterialKeys.GOLD, Items.GOLD_NUGGET)
        consumer.accept(HTTagPrefix.NUGGET, RagiumMaterialKeys.IRON, Items.IRON_ORE)

        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COPPER, Items.COPPER_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.GOLD, Items.GOLD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.IRON, Items.IRON_ORE)

        consumer.accept(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.COPPER, Items.RAW_COPPER)
        consumer.accept(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.GOLD, Items.RAW_GOLD)
        consumer.accept(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.IRON, Items.RAW_IRON)

        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.COPPER, Items.COPPER_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.GOLD, Items.GOLD_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.IRON, Items.IRON_BLOCK)

        bindContents(RagiumContents.Ores.entries)
        bindContents(RagiumContents.StorageBlocks.entries)
        bindContents(RagiumContents.Dusts.entries)
        bindContents(RagiumContents.Gems.entries)
        bindContents(RagiumContents.Ingots.entries)
        bindContents(RagiumContents.Plates.entries)
        bindContents(RagiumContents.RawMaterials.entries)
    }

    override fun registerRuntimeRecipes(
        exporter: RecipeExporter,
        key: HTMaterialKey,
        entry: HTMaterialRegistry.Entry,
        helper: RagiumPlugin.RecipeHelper,
    ) {
        // ingot -> block
        helper.register(exporter, key, entry, HTTagPrefix.INGOT) { tagKeys: List<TagKey<Item>> ->
            val block: ItemConvertible = entry.getFirstItem(HTTagPrefix.STORAGE_BLOCK) ?: return@register
            // Shaped Crafting
            HTShapedRecipeJsonBuilder
                .create(block)
                .patterns(
                    "AAA",
                    "AAA",
                    "AAA",
                ).input('A', tagKeys[0])
                .unlockedBy(tagKeys[0])
                .offerTo(exporter)
        }
        // block -> ingot
        helper.register(exporter, key, entry, HTTagPrefix.STORAGE_BLOCK) { tagKeys: List<TagKey<Item>> ->
            val ingot: ItemConvertible = key.entry.getFirstItem(HTTagPrefix.INGOT) ?: return@register
            // Shapeless Crafting
            HTShapelessRecipeJsonBuilder
                .create(ingot, 9)
                .input(tagKeys[0])
                .unlockedBy(tagKeys[0])
                .offerTo(exporter)
        }
        // ingot -> plate
        helper.register(exporter, key, entry, HTTagPrefix.INGOT, HTTagPrefix.PLATE) { tagKeys: List<TagKey<Item>> ->
            // Metal Former Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.METAL_FORMER)
                .itemInput(tagKeys[0])
                .itemOutput(tagKeys[1])
                .offerTo(exporter, tagKeys[1])
        }
        // ingot -> dust
        helper.register(exporter, key, entry, HTTagPrefix.INGOT, HTTagPrefix.DUST) { tagKeys: List<TagKey<Item>> ->
            // Grinder Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(tagKeys[0])
                .itemOutput(tagKeys[1])
                .offerTo(exporter, tagKeys[1], "_from_ingot")
        }
        // gem -> dust
        helper.register(exporter, key, entry, HTTagPrefix.GEM, HTTagPrefix.DUST) { tagKeys: List<TagKey<Item>> ->
            // Grinder Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(tagKeys[0])
                .itemOutput(tagKeys[1])
                .offerTo(exporter, tagKeys[1], "_from_gem")
        }
        // plate -> dust
        helper.register(exporter, key, entry, HTTagPrefix.PLATE, HTTagPrefix.DUST) { tagKeys: List<TagKey<Item>> ->
            // Grinder Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(tagKeys[0])
                .itemOutput(tagKeys[1])
                .offerTo(exporter, tagKeys[1], "_from_plate")
        }
        // ore -> raw
        helper.register(
            exporter,
            key,
            entry,
            HTTagPrefix.ORE,
            HTTagPrefix.RAW_MATERIAL,
        ) { tagKeys: List<TagKey<Item>> ->
            // Grinder Recipe
            // Grinder Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(tagKeys[0])
                .itemOutput(tagKeys[1], 2)
                .offerTo(exporter, tagKeys[1])
        }
        // ore -> gem
        helper.register(exporter, key, entry, HTTagPrefix.ORE, HTTagPrefix.GEM) { tagKeys: List<TagKey<Item>> ->
            // Grinder Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(tagKeys[0])
                .itemOutput(tagKeys[1], 2)
                .offerTo(exporter, tagKeys[1])
        }
        // raw -> dust
        helper.register(
            exporter,
            key,
            entry,
            HTTagPrefix.RAW_MATERIAL,
            HTTagPrefix.DUST,
        ) { tagKeys: List<TagKey<Item>> ->
            // Grinder Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(tagKeys[0])
                .itemOutput(tagKeys[1], 2)
                .offerTo(exporter, tagKeys[1], "_from_raw")
            // 3x Chemical Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .itemInput(tagKeys[0])
                .fluidInput(RagiumFluids.HYDROCHLORIC_ACID, FluidConstants.INGOT)
                .itemOutput(tagKeys[1], 3)
                .offerTo(exporter, tagKeys[1], "_with_hcl")
            // 4x Chemical Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.BASIC)
                .itemInput(tagKeys[0])
                .fluidInput(RagiumFluids.SULFURIC_ACID, FluidConstants.INGOT)
                .itemOutput(tagKeys[1], 4)
                .offerTo(exporter, tagKeys[1], "_with_h2so4")
        }
        // raw -> ingot
        helper.register(exporter, key, entry, HTTagPrefix.RAW_MATERIAL) { tagKeys: List<TagKey<Item>> ->
            // Smelting Recipe
            val result: ItemConvertible = key.entry.getFirstItem(HTTagPrefix.INGOT)
                ?: key.entry.getFirstItem(HTTagPrefix.GEM)
                ?: return@register
            HTCookingRecipeJsonBuilder.smeltAndBlast(
                exporter,
                tagKeys[0],
                result,
                suffix = "_from_raw",
            )
        }
        // dust -> ingot
        helper.register(exporter, key, entry, HTTagPrefix.DUST) { tagKeys: List<TagKey<Item>> ->
            // Smelting Recipe
            val result: ItemConvertible = key.entry.getFirstItem(HTTagPrefix.INGOT)
                ?: key.entry.getFirstItem(HTTagPrefix.GEM)
                ?: return@register
            HTCookingRecipeJsonBuilder.smeltAndBlast(
                exporter,
                tagKeys[0],
                result,
                suffix = "_from_dust",
            )
        }
    }
}
