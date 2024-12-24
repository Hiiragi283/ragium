package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.data.HTCookingRecipeJsonBuilder
import hiiragi283.ragium.api.data.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.api.data.HTShapedRecipeJsonBuilder
import hiiragi283.ragium.api.data.HTShapelessRecipeJsonBuilder
import hiiragi283.ragium.api.extension.aroundPos
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.material.*
import hiiragi283.ragium.api.util.TriConsumer
import hiiragi283.ragium.common.block.machine.consume.*
import hiiragi283.ragium.common.block.machine.generator.*
import hiiragi283.ragium.common.block.machine.process.*
import hiiragi283.ragium.common.init.*
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.FluidTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.BiConsumer

object RagiumDefaultPlugin : RagiumPlugin {
    override val priority: Int = -100

    override fun registerMachine(consumer: BiConsumer<HTMachineKey, HTMachineType>) {
        // consumer
        RagiumMachineKeys.CONSUMERS.forEach { consumer.accept(it, HTMachineType.CONSUMER) }
        // generators
        RagiumMachineKeys.GENERATORS.forEach { consumer.accept(it, HTMachineType.GENERATOR) }
        // processors
        RagiumMachineKeys.PROCESSORS.forEach { consumer.accept(it, HTMachineType.PROCESSOR) }
    }

    override fun setupMachineProperties(helper: RagiumPlugin.PropertyHelper<HTMachineKey>) {
        // consumers
        helper.modify(RagiumMachineKeys.BEDROCK_MINER) {
            set(HTMachinePropertyKeys.FRONT_MAPPER) { Direction.DOWN }
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTBedrockMinerBlockEntity))
            set(HTMachinePropertyKeys.MULTIBLOCK_PATTERN, RagiumMultiblockShapes.BEDROCK_MINER)
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.FIREWORK)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_STONE_BREAK)
        }
        helper.modify(RagiumMachineKeys.BIOMASS_FERMENTER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTBiomassFermenterBlockEntity))
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.COMPOSTER)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_COMPOSTER_FILL_SUCCESS)
        }
        helper.modify(RagiumMachineKeys.DRAIN) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTDrainBlockEntity))
            set(HTMachinePropertyKeys.SOUND, SoundEvents.ITEM_BUCKET_FILL)
        }
        helper.modify(RagiumMachineKeys.FLUID_DRILL) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTFluidDrillBlockEntity))
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.FIREWORK)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.ITEM_BUCKET_FILL)
        }
        helper.modify(RagiumMachineKeys.ROCK_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTRockGeneratorBlockEntity))
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.ASH)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_STONE_BREAK)
        }
        // generators
        helper.modify(RagiumMachineKeys.GENERATORS::contains) {
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.SMOKE)
        }
        helper.modify(RagiumMachineKeys.COMBUSTION_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTCombustionGeneratorBlockEntity))
            set(HTMachinePropertyKeys.MODEL_ID, RagiumAPI.id("block/generator"))
            set(HTMachinePropertyKeys.ACTIVE_MODEL_ID, RagiumAPI.id("block/generator"))
        }
        helper.modify(RagiumMachineKeys.NUCLEAR_REACTOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTNuclearReactorBlockEntity))
        }
        helper.modify(RagiumMachineKeys.SOLAR_GENERATOR) {
            set(HTMachinePropertyKeys.FRONT_MAPPER) { Direction.UP }
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, _: BlockPos -> world.isDay }
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(::HTSimpleGeneratorBlockEntity))
        }
        helper.modify(RagiumMachineKeys.STEAM_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTSteamGeneratorBlockEntity))
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.POOF)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_FIRE_EXTINGUISH)
        }
        helper.modify(RagiumMachineKeys.THERMAL_GENERATOR) {
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, pos: BlockPos ->
                when {
                    world.getBiome(pos).isIn(BiomeTags.IS_NETHER) -> true
                    else ->
                        pos
                            .aroundPos
                            .filter {
                                val fluidState: FluidState = world.getFluidState(it)
                                fluidState.isIn(FluidTags.LAVA) && fluidState.isStill
                            }.size >= 4
                }
            }
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTThermalGeneratorBlockEntity))
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.FLAME)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.ITEM_BUCKET_EMPTY_LAVA)
        }
        // processors
        helper.modify(RagiumMachineKeys.PROCESSORS::contains) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(::HTSimpleRecipeProcessorBlockEntity))
        }
        /*helper.modify(RagiumMachineKeys.ALLOY_FURNACE) {
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.FLAME)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE)
        }*/
        helper.modify(RagiumMachineKeys.BLAST_FURNACE) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(::HTLargeRecipeProcessorBlockEntity))
            set(HTMachinePropertyKeys.MULTIBLOCK_PATTERN, RagiumMultiblockShapes.BLAST_FURNACE)
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.FLAME)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE)
        }
        helper.modify(
            RagiumMachineKeys.CHEMICAL_REACTOR,
            RagiumMachineKeys.ELECTROLYZER,
            RagiumMachineKeys.EXTRACTOR,
            RagiumMachineKeys.INFUSER,
            RagiumMachineKeys.MIXER,
        ) {
            set(
                HTMachinePropertyKeys.MACHINE_FACTORY,
                HTMachineEntityFactory(::HTChemicalRecipeProcessorBlockEntity),
            )
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.ELECTRIC_SPARK)
        }
        helper.modify(RagiumMachineKeys.COMPRESSOR) {
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_PISTON_EXTEND)
        }
        helper.modify(RagiumMachineKeys.CUTTING_MACHINE) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(::HTLargeRecipeProcessorBlockEntity))
            set(HTMachinePropertyKeys.MULTIBLOCK_PATTERN, RagiumMultiblockShapes.CUTTING_MACHINE)
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.CRIT)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.ITEM_AXE_STRIP)
        }
        helper.modify(RagiumMachineKeys.DISTILLATION_TOWER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTDistillationTowerBlockEntity))
            set(HTMachinePropertyKeys.MULTIBLOCK_PATTERN, RagiumMultiblockShapes.DISTILLATION_TOWER)
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.FALLING_DRIPSTONE_LAVA)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_LAVA_POP)
        }
        helper.modify(RagiumMachineKeys.GRINDER) {
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.CRIT)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_GRINDSTONE_USE)
        }
        helper.modify(RagiumMachineKeys.GROWTH_CHAMBER) {
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.HAPPY_VILLAGER)
        }
        helper.modify(RagiumMachineKeys.MIXER) {
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.BUBBLE_POP)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE)
        }
        helper.modify(RagiumMachineKeys.MULTI_SMELTER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTMultiSmelterBlockEntity))
            set(HTMachinePropertyKeys.MULTIBLOCK_PATTERN, RagiumMultiblockShapes.MULTI_SMELTER)
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.SOUL_FIRE_FLAME)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_FIRE_EXTINGUISH)
        }
        helper.modify(RagiumMachineKeys.LASER_TRANSFORMER) {
            set(HTMachinePropertyKeys.PARTICLE, ParticleTypes.END_ROD)
            set(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL)
        }
    }

    override fun registerMaterial(helper: RagiumPlugin.MaterialHelper) {
        // alloy
        helper.register(RagiumMaterialKeys.DEEP_STEEL, HTMaterialType.ALLOY, Rarity.RARE)
        helper.register(RagiumMaterialKeys.NETHERITE, HTMaterialType.ALLOY, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.RAGI_ALLOY, HTMaterialType.ALLOY)
        helper.register(RagiumMaterialKeys.RAGI_STEEL, HTMaterialType.ALLOY, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.REFINED_RAGI_STEEL, HTMaterialType.ALLOY, Rarity.RARE)
        helper.register(RagiumMaterialKeys.STEEL, HTMaterialType.ALLOY, Rarity.UNCOMMON)

        helper.register(RagiumMaterialKeys.ELECTRUM, HTMaterialType.ALLOY, Rarity.UNCOMMON)
        // dust
        helper.register(RagiumMaterialKeys.ALKALI, HTMaterialType.DUST)
        helper.register(RagiumMaterialKeys.ASH, HTMaterialType.DUST)
        // gem
        helper.register(RagiumMaterialKeys.COAL, HTMaterialType.GEM)
        helper.register(RagiumMaterialKeys.CRYOLITE, HTMaterialType.GEM, Rarity.RARE)
        helper.register(RagiumMaterialKeys.DIAMOND, HTMaterialType.GEM, Rarity.RARE)
        helper.register(RagiumMaterialKeys.EMERALD, HTMaterialType.GEM, Rarity.RARE)
        helper.register(RagiumMaterialKeys.FLUORITE, HTMaterialType.GEM, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.LAPIS, HTMaterialType.GEM, Rarity.COMMON)
        helper.register(RagiumMaterialKeys.PERIDOT, HTMaterialType.GEM, Rarity.RARE)
        helper.register(RagiumMaterialKeys.QUARTZ, HTMaterialType.GEM, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.RAGI_CRYSTAL, HTMaterialType.GEM, Rarity.RARE)
        helper.register(RagiumMaterialKeys.RAGIUM, HTMaterialType.GEM, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.RUBY, HTMaterialType.GEM, Rarity.RARE)
        helper.register(RagiumMaterialKeys.SAPPHIRE, HTMaterialType.GEM, Rarity.RARE)
        // metal
        helper.register(RagiumMaterialKeys.ALUMINUM, HTMaterialType.METAL, Rarity.RARE)
        helper.register(RagiumMaterialKeys.COPPER, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.GOLD, HTMaterialType.METAL, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.IRON, HTMaterialType.METAL)
        // helper.register(RagiumMaterialKeys.SILICON, HTMaterialType.METAL, Rarity.UNCOMMON)

        helper.register(RagiumMaterialKeys.IRIDIUM, HTMaterialType.METAL, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.LEAD, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.NICKEL, HTMaterialType.METAL, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.PLATINUM, HTMaterialType.METAL, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.SILVER, HTMaterialType.METAL, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.TIN, HTMaterialType.METAL)
        helper.register(RagiumMaterialKeys.TUNGSTEN, HTMaterialType.METAL, Rarity.RARE)
        helper.register(RagiumMaterialKeys.ZINC, HTMaterialType.METAL)
        // mineral
        helper.register(RagiumMaterialKeys.BAUXITE, HTMaterialType.MINERAL, Rarity.RARE)
        helper.register(RagiumMaterialKeys.CRUDE_RAGINITE, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.NITER, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.RAGINITE, HTMaterialType.MINERAL, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.REDSTONE, HTMaterialType.MINERAL, Rarity.UNCOMMON)
        helper.register(RagiumMaterialKeys.SALT, HTMaterialType.MINERAL)
        helper.register(RagiumMaterialKeys.SULFUR, HTMaterialType.MINERAL)
        // helper.register(RagiumMaterialKeys.URANITE, HTMaterialType.MINERAL, Rarity.RARE)
        // plate
        // helper.register(RagiumMaterialKeys.ENGINEERING_PLASTIC, HTMaterialType.PLATE, Rarity.RARE)
        // helper.register(RagiumMaterialKeys.PLASTIC, HTMaterialType.PLATE, Rarity.UNCOMMON)
        // helper.register(RagiumMaterialKeys.STELLA, HTMaterialType.PLATE, Rarity.EPIC)
        helper.register(RagiumMaterialKeys.WOOD, HTMaterialType.PLATE)

        // alternative name
        helper.addAltName(RagiumMaterialKeys.WOOD, "saw")
        helper.addAltName(RagiumMaterialKeys.ASH, "ashes")
    }

    override fun setupMaterialProperties(helper: RagiumPlugin.PropertyHelper<HTMaterialKey>) {
        // metal
        helper.modify(RagiumMaterialKeys.COPPER, RagiumMaterialKeys.GOLD, RagiumMaterialKeys.IRON) {
            add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)
        }
        helper.modify(RagiumMaterialKeys.COPPER, RagiumMaterialKeys.IRON) {
            set(HTMaterialPropertyKeys.SMELTING_EXP, 0.7f)
        }
        helper.modify(RagiumMaterialKeys.GOLD) {
            set(HTMaterialPropertyKeys.SMELTING_EXP, 1f)
        }

        helper.modify(RagiumMaterialKeys.IRIDIUM, RagiumMaterialKeys.TUNGSTEN) {
            add(HTMaterialPropertyKeys.DISABLE_DUST_SMELTING)
            add(HTMaterialPropertyKeys.DISABLE_RAW_SMELTING)
        }
        // gem
        helper.modify(
            RagiumMaterialKeys.COAL,
            RagiumMaterialKeys.EMERALD,
            RagiumMaterialKeys.DIAMOND,
            RagiumMaterialKeys.LAPIS,
            RagiumMaterialKeys.QUARTZ,
        ) {
            add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)
        }
        helper.modify(RagiumMaterialKeys.LAPIS) {
            set(HTMaterialPropertyKeys.GRINDING_BASE_COUNT, 4)
        }
        // mineral
        helper.modify(RagiumMaterialKeys.REDSTONE) {
            set(HTMaterialPropertyKeys.GRINDING_BASE_COUNT, 2)
        }
    }

    override fun bindMaterialToItem(consumer: TriConsumer<HTTagPrefix, HTMaterialKey, ItemConvertible>) {
        fun bindContents(contents: List<HTMaterialProvider>) {
            contents.forEach { consumer.accept(it.tagPrefix, it.material, it) }
        }
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COAL, Items.DEEPSLATE_COAL_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COPPER, Items.DEEPSLATE_COPPER_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.DIAMOND, Items.DEEPSLATE_DIAMOND_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.EMERALD, Items.DEEPSLATE_EMERALD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.GOLD, Items.DEEPSLATE_GOLD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.IRON, Items.DEEPSLATE_IRON_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.LAPIS, Items.DEEPSLATE_LAPIS_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.REDSTONE, Items.DEEPSLATE_REDSTONE_ORE)

        consumer.accept(HTTagPrefix.DUST, RagiumMaterialKeys.REDSTONE, Items.REDSTONE)

        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.COAL, Items.COAL)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.DIAMOND, Items.DIAMOND)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.EMERALD, Items.EMERALD)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.LAPIS, Items.LAPIS_LAZULI)
        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.QUARTZ, Items.QUARTZ)

        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER, Items.COPPER_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.GOLD, Items.GOLD_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON, Items.IRON_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.NETHERITE, Items.NETHERITE_INGOT)

        consumer.accept(HTTagPrefix.NUGGET, RagiumMaterialKeys.GOLD, Items.GOLD_NUGGET)
        consumer.accept(HTTagPrefix.NUGGET, RagiumMaterialKeys.IRON, Items.IRON_NUGGET)

        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COAL, Items.COAL_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.COPPER, Items.COPPER_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.DIAMOND, Items.DIAMOND_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.EMERALD, Items.EMERALD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.GOLD, Items.GOLD_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.IRON, Items.IRON_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.LAPIS, Items.LAPIS_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.QUARTZ, Items.NETHER_QUARTZ_ORE)
        consumer.accept(HTTagPrefix.ORE, RagiumMaterialKeys.REDSTONE, Items.REDSTONE_ORE)

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
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.LAPIS, Items.LAPIS_BLOCK)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.NETHERITE, Items.NETHERITE_BLOCK)

        bindContents(RagiumBlocks.Ores.entries)
        bindContents(RagiumBlocks.StorageBlocks.entries)
        bindContents(RagiumItemsNew.Dusts.entries)
        bindContents(RagiumItemsNew.Gears.entries)
        bindContents(RagiumItemsNew.Gems.entries)
        bindContents(RagiumItemsNew.Ingots.entries)
        bindContents(RagiumItemsNew.Plates.entries)
        bindContents(RagiumItemsNew.RawMaterials.entries)
    }

    override fun registerRuntimeMaterialRecipes(
        exporter: RecipeExporter,
        key: HTMaterialKey,
        entry: HTMaterialRegistry.Entry,
        helper: RagiumPlugin.RecipeHelper,
    ) {
        if (HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING !in entry) {
            // ingot/gem -> block
            helper.useItemIfPresent(entry, HTTagPrefix.STORAGE_BLOCK) { output: Item ->
                val prefix: HTTagPrefix = entry.type.getMainPrefix() ?: return@useItemIfPresent
                // Shaped Crafting
                HTShapedRecipeJsonBuilder
                    .create(output)
                    .pattern3x3()
                    .input('A', prefix, key)
                    .offerTo(exporter)
            }
            // block -> ingot/gem
            helper.useItemFromMainPrefix(entry) { output: Item ->
                // Shapeless Crafting
                HTShapelessRecipeJsonBuilder
                    .create(output, 9)
                    .input(HTTagPrefix.STORAGE_BLOCK, key)
                    .offerTo(exporter)
            }
        }

        // ingot/gem -> plate
        helper.useItemIfPresent(entry, HTTagPrefix.PLATE) { output: Item ->
            entry.type.getMainPrefix()?.let { prefix: HTTagPrefix ->
                // Compressor Recipe
                HTMachineRecipeJsonBuilder
                    .create(RagiumMachineKeys.COMPRESSOR)
                    .itemInput(prefix, key)
                    .catalyst(RagiumItemsNew.PressMolds.PLATE)
                    .itemOutput(output)
                    .offerTo(exporter, output)
                // Cutting Machine Recipe
                HTMachineRecipeJsonBuilder
                    .create(RagiumMachineKeys.CUTTING_MACHINE)
                    .itemInput(HTTagPrefix.STORAGE_BLOCK, key)
                    .catalyst(RagiumItemsNew.PressMolds.PLATE)
                    .itemOutput(output, 9)
                    .offerTo(exporter, output)
            }
        }
        // xxx -> dust
        helper.useItemIfPresent(entry, HTTagPrefix.DUST) { output: Item ->
            // ingot/gem -> dust
            entry.type.getMainPrefix()?.let { prefix: HTTagPrefix ->
                HTMachineRecipeJsonBuilder
                    .create(RagiumMachineKeys.GRINDER)
                    .itemInput(prefix, key)
                    .itemOutput(output)
                    .offerTo(exporter, output, "_from_${prefix.asString()}")
            }
            // plate -> dust
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(HTTagPrefix.PLATE, key)
                .itemOutput(output)
                .offerTo(exporter, output, "_from_plate")
            // gear -> dust
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(HTTagPrefix.GEAR, key)
                .itemOutput(output, 4)
                .offerTo(exporter, output, "_from_gear")
        }
        // ingot/gem -> gear
        helper.useItemIfPresent(entry, HTTagPrefix.GEAR) { output: Item ->
            entry.type.getMainPrefix()?.let { prefix: HTTagPrefix ->
                // Shaped Recipe
                HTShapedRecipeJsonBuilder
                    .create(output)
                    .patterns(
                        " A ",
                        "ABA",
                        " A ",
                    ).input('A', prefix, key)
                    .input('B', HTTagPrefix.NUGGET, RagiumMaterialKeys.IRON)
                    .offerTo(exporter)
                // Compressor Recipe
                HTMachineRecipeJsonBuilder
                    .create(RagiumMachineKeys.COMPRESSOR)
                    .itemInput(prefix, key, 4)
                    .catalyst(RagiumItemsNew.PressMolds.GEAR)
                    .itemOutput(output)
                    .offerTo(exporter, output)
            }
        }

        // ore -> raw/gem
        helper.useItemFromRawPrefix(entry) { output: Item ->
            val count: Int = entry.getOrDefault(HTMaterialPropertyKeys.GRINDING_BASE_COUNT)
            // Grinder Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(HTTagPrefix.ORE, key)
                .itemOutput(output, count * 2)
                .offerTo(exporter, output)
            // 3x Chemical Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .itemInput(HTTagPrefix.ORE, key)
                .fluidInput(RagiumFluids.HYDROCHLORIC_ACID, FluidConstants.INGOT)
                .itemOutput(output, count * 3)
                .offerTo(exporter, output, "_3x")
            // 4x Chemical Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.BASIC)
                .itemInput(HTTagPrefix.ORE, key)
                .fluidInput(RagiumFluids.SULFURIC_ACID, FluidConstants.INGOT)
                .itemOutput(output, count * 4)
                .offerTo(exporter, output, "_4x")
        }
        // raw -> dust
        helper.useItemIfPresent(entry, HTTagPrefix.DUST) { output: Item ->
            // Grinder Recipe
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.GRINDER)
                .itemInput(HTTagPrefix.RAW_MATERIAL, key)
                .itemOutput(output, 2)
                .offerTo(exporter, output, "_from_raw")
        }
        if (HTMaterialPropertyKeys.DISABLE_RAW_SMELTING !in entry) {
            // raw -> ingot
            helper.useItemIfPresent(entry, HTTagPrefix.INGOT) { output: Item ->
                val input: TagKey<Item> = HTTagPrefix.RAW_MATERIAL.createTag(key)
                if (!helper.isPopulated(input)) return@useItemIfPresent
                HTCookingRecipeJsonBuilder.smeltAndBlast(
                    exporter,
                    input,
                    output,
                    entry.getOrDefault(HTMaterialPropertyKeys.SMELTING_EXP),
                    suffix = "_from_raw",
                )
            }
            // dust -> ingot
            helper.useItemIfPresent(entry, HTTagPrefix.INGOT) { output: Item ->
                val input: TagKey<Item> = HTTagPrefix.DUST.createTag(key)
                if (!helper.isPopulated(input)) return@useItemIfPresent
                HTCookingRecipeJsonBuilder.smeltAndBlast(
                    exporter,
                    input,
                    output,
                    entry.getOrDefault(HTMaterialPropertyKeys.SMELTING_EXP),
                    suffix = "_from_dust",
                )
            }
        }
    }
}
