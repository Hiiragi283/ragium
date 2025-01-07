package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntityBase
import hiiragi283.ragium.api.extension.add
import hiiragi283.ragium.api.extension.addAllContents
import hiiragi283.ragium.api.extension.blockEntityType
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.common.block.entity.HTAutoIlluminatorBlockEntity
import hiiragi283.ragium.common.block.entity.HTCreativeSourceBlockEntity
import hiiragi283.ragium.common.block.entity.HTItemDisplayBlockEntity
import hiiragi283.ragium.common.block.machine.HTMachineInterfaceBlockEntity
import hiiragi283.ragium.common.block.machine.HTManualForgeBlockEntity
import hiiragi283.ragium.common.block.machine.HTManualGrinderBlockEntity
import hiiragi283.ragium.common.block.machine.HTManualMixerBlockEntity
import hiiragi283.ragium.common.block.machine.consume.*
import hiiragi283.ragium.common.block.machine.generator.*
import hiiragi283.ragium.common.block.machine.process.*
import hiiragi283.ragium.common.block.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.block.storage.HTCreativeCrateBlockEntity
import hiiragi283.ragium.common.block.storage.HTCreativeDrumBlockEntity
import hiiragi283.ragium.common.block.storage.HTDrumBlockEntity
import hiiragi283.ragium.common.block.transfer.HTCreativeExporterBlockEntity
import hiiragi283.ragium.common.block.transfer.HTExporterBlockEntity
import hiiragi283.ragium.common.block.transfer.HTFilteringPipeBlockEntity
import hiiragi283.ragium.common.block.transfer.HTSimplePipeBlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumBlockEntityTypes {
    //    Transfer    //

    @JvmField
    val CRATE: BlockEntityType<HTCrateBlockEntity> =
        register("crate", ::HTCrateBlockEntity)

    @JvmField
    val CREATIVE_CRATE: BlockEntityType<HTCreativeCrateBlockEntity> =
        register("creative_crate", ::HTCreativeCrateBlockEntity)

    @JvmField
    val CREATIVE_DRUM: BlockEntityType<HTCreativeDrumBlockEntity> =
        register("creative_drum", ::HTCreativeDrumBlockEntity)

    @JvmField
    val CREATIVE_EXPORTER: BlockEntityType<HTCreativeExporterBlockEntity> =
        register("creative_exporter", ::HTCreativeExporterBlockEntity)

    @JvmField
    val DRUM: BlockEntityType<HTDrumBlockEntity> =
        register("drum", ::HTDrumBlockEntity)

    @JvmField
    val EXPORTER: BlockEntityType<HTExporterBlockEntity> =
        register("exporter", ::HTExporterBlockEntity)

    @JvmField
    val PIPE: BlockEntityType<HTSimplePipeBlockEntity> =
        register("pipe", ::HTSimplePipeBlockEntity)

    //    Machine    //

    @JvmField
    val MANUAL_FORGE: BlockEntityType<HTManualForgeBlockEntity> =
        register("manual_forge", ::HTManualForgeBlockEntity)

    @JvmField
    val MANUAL_GRINDER: BlockEntityType<HTManualGrinderBlockEntity> =
        register("manual_grinder", ::HTManualGrinderBlockEntity)

    @JvmField
    val MANUAL_MIXER: BlockEntityType<HTManualMixerBlockEntity> =
        register("manual_mixer", ::HTManualMixerBlockEntity)

    @JvmField
    val ASSEMBLER: BlockEntityType<HTAssemblerBlockEntity> =
        register("assembler", ::HTAssemblerBlockEntity)

    @JvmField
    val BIOMASS_FERMENTER: BlockEntityType<HTBiomassFermenterBlockEntity> =
        register("biomass_fermenter", ::HTBiomassFermenterBlockEntity)

    @JvmField
    val BEDROCK_MINER: BlockEntityType<HTBedrockMinerBlockEntity> =
        register("bedrock_miner", ::HTBedrockMinerBlockEntity)

    @JvmField
    val CHEMICAL_PROCESSOR: BlockEntityType<HTChemicalRecipeProcessorBlockEntity> =
        register("chemical_processor", HTChemicalRecipeProcessorBlockEntity::fromState)

    @JvmField
    val COMBUSTION_GENERATOR: BlockEntityType<HTCombustionGeneratorBlockEntity> =
        register("combustion_generator", ::HTCombustionGeneratorBlockEntity)

    @JvmField
    val DISTILLATION_TOWER: BlockEntityType<HTDistillationTowerBlockEntity> =
        register("distillation_tower", ::HTDistillationTowerBlockEntity)

    @JvmField
    val DRAIN: BlockEntityType<HTDrainBlockEntity> =
        register("drain", ::HTDrainBlockEntity)

    @JvmField
    val FILTERING_PIPE: BlockEntityType<HTFilteringPipeBlockEntity> =
        register("filtering_pipe", ::HTFilteringPipeBlockEntity)

    @JvmField
    val FLUID_DRILL: BlockEntityType<HTFluidDrillBlockEntity> =
        register("fluid_drill", ::HTFluidDrillBlockEntity)

    @JvmField
    val GRINDER: BlockEntityType<HTGrinderBlockEntity> =
        register("grinder", ::HTGrinderBlockEntity)

    @JvmField
    val LARGE_CHEMICAL_REACTOR: BlockEntityType<HTLargeChemicalReactorBlockEntity> =
        register("large_chemical_reactor", ::HTLargeChemicalReactorBlockEntity)

    @JvmField
    val LARGE_PROCESSOR: BlockEntityType<HTLargeRecipeProcessorBlockEntity> =
        register("large_processor", HTLargeRecipeProcessorBlockEntity::fromState)

    @JvmField
    val MULTI_SMELTER: BlockEntityType<HTMultiSmelterBlockEntity> =
        register("multi_smelter", ::HTMultiSmelterBlockEntity)

    @JvmField
    val NUCLEAR_REACTOR: BlockEntityType<HTNuclearReactorBlockEntity> =
        register("nuclear_reactor", ::HTNuclearReactorBlockEntity)

    @JvmField
    val ROCK_GENERATOR: BlockEntityType<HTRockGeneratorBlockEntity> =
        register("rock_generator", ::HTRockGeneratorBlockEntity)

    @JvmField
    val SIMPLE_GENERATOR: BlockEntityType<HTSimpleGeneratorBlockEntity> =
        register("simple_generator", HTSimpleGeneratorBlockEntity::fromState)

    @JvmField
    val SIMPLE_PROCESSOR: BlockEntityType<HTSimpleRecipeProcessorBlockEntity> =
        register("simple_processor", HTSimpleRecipeProcessorBlockEntity::fromState)

    @JvmField
    val STEAM_GENERATOR: BlockEntityType<HTSteamGeneratorBlockEntity> =
        register("steam_generator", ::HTSteamGeneratorBlockEntity)

    @JvmField
    val THERMAL_GENERATOR: BlockEntityType<HTThermalGeneratorBlockEntity> =
        register("thermal_generator", ::HTThermalGeneratorBlockEntity)

    @JvmField
    val VIBRATION_GENERATOR: BlockEntityType<HTVibrationGeneratorBlockEntity> =
        register("vibration_generator", ::HTVibrationGeneratorBlockEntity)

    //    Misc    //

    @JvmField
    val AUTO_ILLUMINATOR: BlockEntityType<HTAutoIlluminatorBlockEntity> =
        register("auto_illuminator", ::HTAutoIlluminatorBlockEntity)

    @JvmField
    val CREATIVE_SOURCE: BlockEntityType<HTCreativeSourceBlockEntity> =
        register("creative_source", ::HTCreativeSourceBlockEntity)

    @JvmField
    val EXTENDED_PROCESSOR: BlockEntityType<HTExtendedProcessorBlockEntity> =
        register("extended_processor", ::HTExtendedProcessorBlockEntity)

    @JvmField
    val ITEM_DISPLAY: BlockEntityType<HTItemDisplayBlockEntity> =
        register("item_display", ::HTItemDisplayBlockEntity)

    @JvmField
    val MACHINE_INTERFACE: BlockEntityType<HTMachineInterfaceBlockEntity> =
        register("machine_interface", ::HTMachineInterfaceBlockEntity)

    @JvmStatic
    private fun <T : HTBlockEntityBase> register(name: String, factory: BlockEntityType.BlockEntityFactory<T>): BlockEntityType<T> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            RagiumAPI.id(name),
            blockEntityType(factory),
        )

    @JvmStatic
    fun init() {
        EXPORTER.addAllContents(RagiumBlocks.Exporters.entries)
        FILTERING_PIPE.addAllContents(RagiumBlocks.FilteringPipes.entries)
        PIPE.addAllContents(RagiumBlocks.Pipes.entries)

        CRATE.addAllContents(RagiumBlocks.Crates.entries)
        DRUM.addAllContents(RagiumBlocks.Drums.entries)

        CREATIVE_EXPORTER.add(RagiumBlocks.Creatives.EXPORTER)
        EXTENDED_PROCESSOR.add(RagiumBlocks.EXTENDED_PROCESSOR)
        ITEM_DISPLAY.add(RagiumBlocks.ITEM_DISPLAY)
        MACHINE_INTERFACE.add(RagiumBlocks.MACHINE_INTERFACE)
        MANUAL_FORGE.add(RagiumBlocks.MANUAL_FORGE)
        MANUAL_GRINDER.add(RagiumBlocks.MANUAL_GRINDER)
        MANUAL_MIXER.add(RagiumBlocks.MANUAL_MIXER)
        // consumers
        registerMachineBlocks(RagiumMachineKeys.BEDROCK_MINER, BEDROCK_MINER)
        registerMachineBlocks(RagiumMachineKeys.BIOMASS_FERMENTER, BIOMASS_FERMENTER)
        registerMachineBlocks(RagiumMachineKeys.DRAIN, DRAIN)
        registerMachineBlocks(RagiumMachineKeys.FLUID_DRILL, FLUID_DRILL)
        registerMachineBlocks(RagiumMachineKeys.ROCK_GENERATOR, ROCK_GENERATOR)
        // generators
        RagiumMachineKeys.GENERATORS.forEach {
            registerMachineBlocks(it, SIMPLE_GENERATOR)
        }
        registerMachineBlocks(RagiumMachineKeys.COMBUSTION_GENERATOR, COMBUSTION_GENERATOR)
        registerMachineBlocks(RagiumMachineKeys.NUCLEAR_REACTOR, NUCLEAR_REACTOR)
        registerMachineBlocks(RagiumMachineKeys.STEAM_GENERATOR, STEAM_GENERATOR)
        registerMachineBlocks(RagiumMachineKeys.THERMAL_GENERATOR, THERMAL_GENERATOR)
        registerMachineBlocks(RagiumMachineKeys.VIBRATION_GENERATOR, VIBRATION_GENERATOR)
        // processors
        RagiumMachineKeys.PROCESSORS.forEach {
            registerMachineBlocks(it, SIMPLE_PROCESSOR)
        }
        registerMachineBlocks(RagiumMachineKeys.ASSEMBLER, ASSEMBLER)
        registerMachineBlocks(RagiumMachineKeys.BLAST_FURNACE, LARGE_PROCESSOR)
        registerMachineBlocks(RagiumMachineKeys.CHEMICAL_REACTOR, CHEMICAL_PROCESSOR)
        registerMachineBlocks(RagiumMachineKeys.CUTTING_MACHINE, LARGE_PROCESSOR)
        registerMachineBlocks(RagiumMachineKeys.DISTILLATION_TOWER, DISTILLATION_TOWER)
        registerMachineBlocks(RagiumMachineKeys.ELECTROLYZER, CHEMICAL_PROCESSOR)
        registerMachineBlocks(RagiumMachineKeys.EXTRACTOR, CHEMICAL_PROCESSOR)
        registerMachineBlocks(RagiumMachineKeys.GRINDER, GRINDER)
        registerMachineBlocks(RagiumMachineKeys.INFUSER, CHEMICAL_PROCESSOR)
        registerMachineBlocks(RagiumMachineKeys.LARGE_CHEMICAL_REACTOR, LARGE_CHEMICAL_REACTOR)
        registerMachineBlocks(RagiumMachineKeys.MIXER, CHEMICAL_PROCESSOR)
        registerMachineBlocks(RagiumMachineKeys.MULTI_SMELTER, MULTI_SMELTER)
    }

    @JvmStatic
    private fun registerMachineBlocks(key: HTMachineKey, type: BlockEntityType<*>) {
        RagiumAPI
            .getInstance()
            .machineRegistry
            .getEntryOrNull(key)
            ?.let(type::add)
    }
}
