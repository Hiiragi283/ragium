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
import hiiragi283.ragium.api.material.HTMaterialPropertyKeys
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.TriConsumer
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import hiiragi283.ragium.common.machine.*
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions
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
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.function.BiConsumer

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

    override fun setupMachineProperties(helper: RagiumPlugin.PropertyHelper<HTMachineKey>) {
        // consumers
        helper.modify(RagiumMachineKeys.DRAIN) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTDrainBlockEntity))
            set(HTMachinePropertyKeys.SOUND, SoundEvents.ITEM_BUCKET_FILL)
        }
        helper.modify(RagiumMachineKeys.BIOMASS_FERMENTER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTBiomassFermenterBlockEntity))
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_COMPOSTER_FILL_SUCCESS)
        }
        helper.modify(RagiumMachineKeys.CANNING_MACHINE) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTCanningMachineBlockEntity))
        }
        helper.modify(RagiumMachineKeys.FLUID_DRILL) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTFluidDrillBlockEntity))
            set(HTMachinePropertyKeys.SOUND, SoundEvents.ITEM_BUCKET_FILL)
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
        // generators
        helper.modify(RagiumMachineKeys.GENERATORS::contains) {
        }
        helper.modify(RagiumMachineKeys.COMBUSTION_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTCombustionGeneratorBlockEntity))
            set(HTMachinePropertyKeys.MODEL_ID, RagiumAPI.id("block/generator"))
        }
        helper.modify(RagiumMachineKeys.SOLAR_PANEL) {
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, _: BlockPos -> world.isDay }
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTGeneratorBlockEntityBase::Simple))
            set(HTMachinePropertyKeys.MODEL_ID, RagiumAPI.id("block/solar_generator"))
            set(HTMachinePropertyKeys.VOXEL_SHAPE, Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0))
        }
        helper.modify(RagiumMachineKeys.STEAM_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTSteamGeneratorBlockEntity))
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_FIRE_EXTINGUISH)
        }
        helper.modify(RagiumMachineKeys.THERMAL_GENERATOR) {
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
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTThermalGeneratorBlockEntity))
            set(HTMachinePropertyKeys.SOUND, SoundEvents.ITEM_BUCKET_EMPTY_LAVA)
        }
        /*helper.modify(RagiumMachineKeys.WATER_GENERATOR) {
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, pos: BlockPos ->
                pos.getAroundPos { world.getFluidState(it).isIn(FluidTags.WATER) }.size >= 2
            }
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTGeneratorBlockEntityBase::Simple))
            set(HTMachinePropertyKeys.MODEL_ID, RagiumAPI.id("block/generator"))
        }*/
        // processors
        helper.modify(RagiumMachineKeys.PROCESSORS::contains) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTProcessorBlockEntityBase::Simple))
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
        helper.modify(RagiumMachineKeys.BLAST_FURNACE) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTBlastFurnaceBlockEntity))
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE)
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
        helper.modify(RagiumMachineKeys.CHEMICAL_REACTOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTProcessorBlockEntityBase::Chemical))
        }
        helper.modify(RagiumMachineKeys.COMPRESSOR) {
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_PISTON_EXTEND)
        }
        helper.modify(RagiumMachineKeys.DISTILLATION_TOWER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTDistillationTowerBlockEntity))
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_LAVA_POP)
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
        helper.modify(RagiumMachineKeys.ELECTROLYZER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTProcessorBlockEntityBase::Chemical))
        }
        helper.modify(RagiumMachineKeys.GRINDER) {
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_GRINDSTONE_USE)
        }
        helper.modify(RagiumMachineKeys.METAL_FORMER) {
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_ANVIL_USE)
        }
        helper.modify(RagiumMachineKeys.MIXER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTProcessorBlockEntityBase::Chemical))
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE)
        }
        helper.modify(RagiumMachineKeys.MULTI_SMELTER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTMultiSmelterBlockEntity))
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_FIRE_EXTINGUISH)
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
        helper.modify(RagiumMachineKeys.LASER_TRANSFORMER) {
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL)
        }
        helper.modify(RagiumMachineKeys.SAW_MILL) {
            set(HTMachinePropertyKeys.FRONT_TEX) { Identifier.of("block/stonecutter_saw") }
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTSawmillBlockEntity))
            set(HTMachinePropertyKeys.SOUND, SoundEvents.ITEM_AXE_STRIP)
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
    }

    override fun registerMaterial(consumer: TriConsumer<HTMaterialKey, HTMaterialKey.Type, Rarity>) {
        // alloy
        consumer.accept(RagiumMaterialKeys.DEEP_STEEL, HTMaterialKey.Type.ALLOY, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.RAGI_ALLOY, HTMaterialKey.Type.ALLOY, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.RAGI_STEEL, HTMaterialKey.Type.ALLOY, Rarity.UNCOMMON)
        consumer.accept(RagiumMaterialKeys.REFINED_RAGI_STEEL, HTMaterialKey.Type.ALLOY, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.STEEL, HTMaterialKey.Type.ALLOY, Rarity.UNCOMMON)

        consumer.accept(RagiumMaterialKeys.ELECTRUM, HTMaterialKey.Type.ALLOY, Rarity.UNCOMMON)
        // dust
        consumer.accept(RagiumMaterialKeys.ALKALI, HTMaterialKey.Type.DUST, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.ASH, HTMaterialKey.Type.DUST, Rarity.COMMON)
        // gem
        consumer.accept(RagiumMaterialKeys.COAL, HTMaterialKey.Type.GEM, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.CRYOLITE, HTMaterialKey.Type.GEM, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.DIAMOND, HTMaterialKey.Type.GEM, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.EMERALD, HTMaterialKey.Type.GEM, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.FLUORITE, HTMaterialKey.Type.GEM, Rarity.UNCOMMON)
        consumer.accept(RagiumMaterialKeys.PERIDOT, HTMaterialKey.Type.GEM, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.QUARTZ, HTMaterialKey.Type.GEM, Rarity.UNCOMMON)
        consumer.accept(RagiumMaterialKeys.RAGI_CRYSTAL, HTMaterialKey.Type.GEM, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.RAGIUM, HTMaterialKey.Type.GEM, Rarity.EPIC)
        consumer.accept(RagiumMaterialKeys.RUBY, HTMaterialKey.Type.GEM, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.SAPPHIRE, HTMaterialKey.Type.GEM, Rarity.RARE)
        // metal
        consumer.accept(RagiumMaterialKeys.ALUMINUM, HTMaterialKey.Type.METAL, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.COPPER, HTMaterialKey.Type.METAL, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.GOLD, HTMaterialKey.Type.METAL, Rarity.UNCOMMON)
        consumer.accept(RagiumMaterialKeys.IRON, HTMaterialKey.Type.METAL, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.SILICON, HTMaterialKey.Type.METAL, Rarity.UNCOMMON)

        consumer.accept(RagiumMaterialKeys.IRIDIUM, HTMaterialKey.Type.METAL, Rarity.EPIC)
        consumer.accept(RagiumMaterialKeys.LEAD, HTMaterialKey.Type.METAL, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.NICKEL, HTMaterialKey.Type.METAL, Rarity.UNCOMMON)
        consumer.accept(RagiumMaterialKeys.PLATINUM, HTMaterialKey.Type.METAL, Rarity.EPIC)
        consumer.accept(RagiumMaterialKeys.SILVER, HTMaterialKey.Type.METAL, Rarity.UNCOMMON)
        consumer.accept(RagiumMaterialKeys.TIN, HTMaterialKey.Type.METAL, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.TUNGSTEN, HTMaterialKey.Type.METAL, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.ZINC, HTMaterialKey.Type.METAL, Rarity.COMMON)
        // mineral
        consumer.accept(RagiumMaterialKeys.BAUXITE, HTMaterialKey.Type.MINERAL, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.CRUDE_RAGINITE, HTMaterialKey.Type.MINERAL, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.NITER, HTMaterialKey.Type.MINERAL, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.RAGINITE, HTMaterialKey.Type.MINERAL, Rarity.UNCOMMON)
        consumer.accept(RagiumMaterialKeys.SALT, HTMaterialKey.Type.MINERAL, Rarity.COMMON)
        consumer.accept(RagiumMaterialKeys.SULFUR, HTMaterialKey.Type.MINERAL, Rarity.COMMON)
        // plate
        consumer.accept(RagiumMaterialKeys.ENGINEERING_PLASTIC, HTMaterialKey.Type.PLATE, Rarity.RARE)
        consumer.accept(RagiumMaterialKeys.PLASTIC, HTMaterialKey.Type.PLATE, Rarity.UNCOMMON)
        consumer.accept(RagiumMaterialKeys.STELLA, HTMaterialKey.Type.PLATE, Rarity.EPIC)
        consumer.accept(RagiumMaterialKeys.WOOD, HTMaterialKey.Type.PLATE, Rarity.COMMON)
    }

    override fun setupMaterialProperties(helper: RagiumPlugin.PropertyHelper<HTMaterialKey>) {
        // metal
        helper.modify(RagiumMaterialKeys.IRON, RagiumMaterialKeys.COPPER) {
            set(HTMaterialPropertyKeys.SMELTING_EXP, 0.7f)
        }
        helper.modify(RagiumMaterialKeys.GOLD) {
            set(HTMaterialPropertyKeys.SMELTING_EXP, 1f)
        }

        helper.modify(RagiumMaterialKeys.IRIDIUM, RagiumMaterialKeys.TUNGSTEN) {
            add(HTMaterialPropertyKeys.DISABLE_DUST_SMELTING)
            add(HTMaterialPropertyKeys.DISABLE_RAW_SMELTING)
        }
    }

    override fun bindMaterialToItem(consumer: TriConsumer<HTTagPrefix, HTMaterialKey, ItemConvertible>) {
        fun bindContents(contents: List<HTContent.Material<*>>) {
            contents.forEach { consumer.accept(it.tagPrefix, it.material, it) }
        }
        consumer.accept(HTTagPrefix.DEEP_ORE, RagiumMaterialKeys.COAL, Items.DEEPSLATE_COAL_ORE)
        consumer.accept(HTTagPrefix.DEEP_ORE, RagiumMaterialKeys.COPPER, Items.DEEPSLATE_COPPER_ORE)
        consumer.accept(HTTagPrefix.DEEP_ORE, RagiumMaterialKeys.DIAMOND, Items.DEEPSLATE_DIAMOND_ORE)
        consumer.accept(HTTagPrefix.DEEP_ORE, RagiumMaterialKeys.EMERALD, Items.DEEPSLATE_EMERALD_ORE)
        consumer.accept(HTTagPrefix.DEEP_ORE, RagiumMaterialKeys.GOLD, Items.DEEPSLATE_GOLD_ORE)
        consumer.accept(HTTagPrefix.DEEP_ORE, RagiumMaterialKeys.IRON, Items.DEEPSLATE_IRON_ORE)

        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.COAL, Items.COAL)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.DIAMOND, Items.DIAMOND)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.EMERALD, Items.EMERALD)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.QUARTZ, Items.QUARTZ)

        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER, Items.COPPER_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.GOLD, Items.GOLD_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON, Items.IRON_INGOT)

        consumer.accept(HTTagPrefix.NUGGET, RagiumMaterialKeys.GOLD, Items.GOLD_NUGGET)
        consumer.accept(HTTagPrefix.NUGGET, RagiumMaterialKeys.IRON, Items.IRON_ORE)

        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COAL, Items.COAL_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COPPER, Items.COPPER_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.DIAMOND, Items.DIAMOND_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.EMERALD, Items.EMERALD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.GOLD, Items.GOLD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.IRON, Items.IRON_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.QUARTZ, Items.NETHER_QUARTZ_ORE)

        consumer.accept(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.COPPER, Items.RAW_COPPER)
        consumer.accept(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.GOLD, Items.RAW_GOLD)
        consumer.accept(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.IRON, Items.RAW_IRON)

        consumer.accept(HTTagPrefix.ROD, RagiumMaterialKeys.WOOD, Items.STICK)

        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.COAL, Items.COAL_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.COPPER, Items.COPPER_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.DIAMOND, Items.DIAMOND_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.EMERALD, Items.EMERALD_BLOCK)
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
        if (HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING !in entry) {
            // ingot/gem -> block
            helper.register(entry, HTTagPrefix.STORAGE_BLOCK) { map: Map<HTTagPrefix, Item> ->
                val block: Item = map[HTTagPrefix.STORAGE_BLOCK] ?: return@register
                val prefix: HTTagPrefix = entry.type.getMainPrefix() ?: return@register
                // Shaped Crafting
                HTShapedRecipeJsonBuilder
                    .create(block)
                    .patterns(
                        "AAA",
                        "AAA",
                        "AAA",
                    ).input('A', prefix, key)
                    .offerTo(exporter)
            }
            // block -> ingot/gem
            entry.type.getMainPrefix()?.let { prefix: HTTagPrefix ->
                helper.register(entry, prefix) { map: Map<HTTagPrefix, Item> ->
                    val output: Item = map[prefix] ?: return@register
                    // Shapeless Crafting
                    HTShapelessRecipeJsonBuilder
                        .create(output, 9)
                        .input(HTTagPrefix.STORAGE_BLOCK, key)
                        .offerTo(exporter)
                }
            }
        }
        // ingot -> plate
        helper.register(entry, HTTagPrefix.PLATE) { map: Map<HTTagPrefix, Item> ->
            val plate: Item = map[HTTagPrefix.PLATE] ?: return@register
            // Metal Former Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.METAL_FORMER)
                .itemInput(HTTagPrefix.INGOT, key)
                .itemOutput(plate)
                .offerTo(exporter, plate)
        }
        // ingot -> dust
        helper.register(entry, HTTagPrefix.DUST) { map: Map<HTTagPrefix, Item> ->
            val dust: Item = map[HTTagPrefix.DUST] ?: return@register
            // Grinder Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(HTTagPrefix.INGOT, key)
                .itemOutput(dust)
                .offerTo(exporter, dust, "_from_ingot")
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(HTTagPrefix.GEM, key)
                .itemOutput(dust)
                .offerTo(exporter, dust, "_from_gem")
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(HTTagPrefix.PLATE, key)
                .itemOutput(dust)
                .offerTo(exporter, dust, "_from_plate")
        }
        // ore -> raw/gem
        entry.type.getRawPrefix()?.let { prefix: HTTagPrefix ->
            helper.register(entry, prefix) { map: Map<HTTagPrefix, Item> ->
                val output: Item = map[prefix] ?: return@register
                // Grinder Recipe
                HTMachineRecipeJsonBuilder
                    .create(RagiumMachineKeys.GRINDER)
                    .itemInput(HTTagPrefix.ORE, key)
                    .itemOutput(output, 2)
                    .offerTo(exporter, output)
                // 3x Chemical Recipe
                HTMachineRecipeJsonBuilder
                    .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                    .itemInput(HTTagPrefix.ORE, key)
                    .fluidInput(RagiumFluids.HYDROCHLORIC_ACID, FluidConstants.INGOT)
                    .itemOutput(output, 3)
                    .offerTo(exporter, output, "_3x")
                // 4x Chemical Recipe
                HTMachineRecipeJsonBuilder
                    .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.BASIC)
                    .itemInput(HTTagPrefix.ORE, key)
                    .fluidInput(RagiumFluids.SULFURIC_ACID, FluidConstants.INGOT)
                    .itemOutput(output, 4)
                    .offerTo(exporter, output, "_4x")
            }
        }
        // raw -> dust
        helper.register(entry, HTTagPrefix.DUST) { map: Map<HTTagPrefix, Item> ->
            val dust: Item = map[HTTagPrefix.DUST] ?: return@register
            // Grinder Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(HTTagPrefix.RAW_MATERIAL, key)
                .itemOutput(dust, 2)
                .offerTo(exporter, dust, "_from_raw")
        }
        // raw -> ingot
        helper.register(entry) {
            // Smelting Recipe
            val result: ItemConvertible = key.entry.getFirstItem(HTTagPrefix.INGOT)
                ?: key.entry.getFirstItem(HTTagPrefix.GEM)
                ?: return@register
            if (entry.contains(HTMaterialPropertyKeys.DISABLE_RAW_SMELTING)) return@register
            val raw: TagKey<Item> = HTTagPrefix.RAW_MATERIAL.createTag(key)
            if (!ResourceConditions.tagsPopulated(raw).test(null)) return@register
            HTCookingRecipeJsonBuilder.smeltAndBlast(
                exporter,
                raw,
                result,
                entry.getOrDefault(HTMaterialPropertyKeys.SMELTING_EXP),
                suffix = "_from_raw",
            )
        }
        // dust -> ingot
        helper.register(entry) {
            // Smelting Recipe
            val result: ItemConvertible = key.entry.getFirstItem(HTTagPrefix.INGOT)
                ?: key.entry.getFirstItem(HTTagPrefix.GEM)
                ?: return@register
            if (entry.contains(HTMaterialPropertyKeys.DISABLE_DUST_SMELTING)) return@register
            val dust: TagKey<Item> = HTTagPrefix.DUST.createTag(key)
            if (!ResourceConditions.tagsPopulated(dust).test(null)) return@register
            HTCookingRecipeJsonBuilder.smeltAndBlast(
                exporter,
                dust,
                result,
                entry.getOrDefault(HTMaterialPropertyKeys.SMELTING_EXP),
                suffix = "_from_dust",
            )
        }
    }
}
