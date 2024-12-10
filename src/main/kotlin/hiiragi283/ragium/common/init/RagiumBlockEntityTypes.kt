package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.blockEntityType
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.entity.*
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
import hiiragi283.ragium.common.block.transfer.*
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumBlockEntityTypes {
    //    Transfer    //

    @JvmField
    val BUFFER: BlockEntityType<HTBufferBlockEntity> =
        register("buffer", ::HTBufferBlockEntity)

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
    val CROSS_PIPE: BlockEntityType<HTCrossPipeBlockEntity> =
        register("cross_pipe", ::HTCrossPipeBlockEntity)

    @JvmField
    val DRUM: BlockEntityType<HTDrumBlockEntity> =
        register("drum", ::HTDrumBlockEntity)

    @JvmField
    val EXPORTER: BlockEntityType<HTExporterBlockEntity> =
        register("exporter", ::HTExporterBlockEntity)

    @JvmField
    val PIPE: BlockEntityType<HTPipeBlockEntity> =
        register("pipe", ::HTPipeBlockEntity)

    @JvmField
    val PIPE_STATION: BlockEntityType<HTPipeStationBlockEntity> =
        register("pipe_station", ::HTPipeStationBlockEntity)

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
    val BIOMASS_FERMENTER: BlockEntityType<HTBiomassFermenterBlockEntity> =
        register("biomass_fermenter", ::HTBiomassFermenterBlockEntity)

    @JvmField
    val BLAST_FURNACE: BlockEntityType<HTBlastFurnaceBlockEntity> =
        register("blast_furnace", ::HTBlastFurnaceBlockEntity)

    @JvmField
    val CANNING_MACHINE: BlockEntityType<HTCanningMachineBlockEntity> =
        register("exporter", ::HTCanningMachineBlockEntity)

    @JvmField
    val BEDROCK_MINER: BlockEntityType<HTBedrockMinerBlockEntity> =
        register("bedrock_miner", ::HTBedrockMinerBlockEntity)

    @JvmField
    val CHEMICAL_PROCESSOR: BlockEntityType<HTChemicalRecipeProcessorBlockEntity> =
        register("chemical_processor", ::HTChemicalRecipeProcessorBlockEntity)

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
    val MULTI_SMELTER: BlockEntityType<HTMultiSmelterBlockEntity> =
        register("multi_smelter", ::HTMultiSmelterBlockEntity)

    @JvmField
    val NUCLEAR_REACTOR: BlockEntityType<HTNuclearReactorBlockEntity> =
        register("nuclear_reactor", ::HTNuclearReactorBlockEntity)

    @JvmField
    val ROCK_GENERATOR: BlockEntityType<HTRockGeneratorBlockEntity> =
        register("rock_generator", ::HTRockGeneratorBlockEntity)

    @JvmField
    val SAW_MILL: BlockEntityType<HTSawmillBlockEntity> =
        register("saw_mill", ::HTSawmillBlockEntity)

    @JvmField
    val SIMPLE_GENERATOR: BlockEntityType<HTSimpleGeneratorBlockEntity> =
        register("simple_generator", ::HTSimpleGeneratorBlockEntity)

    @JvmField
    val SIMPLE_PROCESSOR: BlockEntityType<HTSimpleRecipeProcessorBlockEntity> =
        register("simple_processor", ::HTSimpleRecipeProcessorBlockEntity)

    @JvmField
    val STEAM_GENERATOR: BlockEntityType<HTSteamGeneratorBlockEntity> =
        register("steam_generator", ::HTSteamGeneratorBlockEntity)

    @JvmField
    val THERMAL_GENERATOR: BlockEntityType<HTThermalGeneratorBlockEntity> =
        register("thermal_generator", ::HTThermalGeneratorBlockEntity)

    //    Misc    //

    @JvmField
    val AUTO_ILLUMINATOR: BlockEntityType<HTAutoIlluminatorBlockEntity> =
        register("auto_illuminator", ::HTAutoIlluminatorBlockEntity)

    @JvmField
    val CREATIVE_SOURCE: BlockEntityType<HTCreativeSourceBlockEntity> =
        register("creative_source", ::HTCreativeSourceBlockEntity)

    @JvmField
    val ENCHANTMENT_BOOKSHELF: BlockEntityType<HTEnchantmentBookshelfBlockEntity> =
        register("enchantment_bookshelf", ::HTEnchantmentBookshelfBlockEntity)

    @JvmField
    val ITEM_DISPLAY: BlockEntityType<HTItemDisplayBlockEntity> =
        register("item_display", ::HTItemDisplayBlockEntity)

    @JvmField
    val LARGE_PROCESSOR: BlockEntityType<HTLargeProcessorBlockEntity> =
        register("large_processor", ::HTLargeProcessorBlockEntity)

    @JvmStatic
    private fun <T : HTBlockEntityBase> register(name: String, factory: BlockEntityType.BlockEntityFactory<T>): BlockEntityType<T> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            RagiumAPI.id(name),
            blockEntityType(factory),
        )

    @JvmStatic
    fun init() {
        registerBlocks(CROSS_PIPE, RagiumContents.CrossPipes.entries)
        registerBlocks(EXPORTER, RagiumContents.Exporters.entries)
        registerBlocks(FILTERING_PIPE, RagiumContents.FilteringPipe.entries)
        registerBlocks(PIPE, RagiumContents.Pipes.entries)
        registerBlocks(PIPE_STATION, RagiumContents.PipeStations.entries)

        registerBlocks(CRATE, RagiumContents.Crates.entries)
        registerBlocks(DRUM, RagiumContents.Drums.entries)

        CREATIVE_EXPORTER.addSupportedBlock(RagiumBlocks.CREATIVE_EXPORTER)
        ITEM_DISPLAY.addSupportedBlock(RagiumBlocks.ITEM_DISPLAY)
        LARGE_PROCESSOR.addSupportedBlock(RagiumBlocks.LARGE_PROCESSOR)
        MANUAL_FORGE.addSupportedBlock(RagiumBlocks.MANUAL_FORGE)
        MANUAL_GRINDER.addSupportedBlock(RagiumBlocks.MANUAL_GRINDER)
        MANUAL_MIXER.addSupportedBlock(RagiumBlocks.MANUAL_MIXER)
        // consumers
        registerMachineBlocks(RagiumMachineKeys.BEDROCK_MINER, BEDROCK_MINER)
        registerMachineBlocks(RagiumMachineKeys.BIOMASS_FERMENTER, BIOMASS_FERMENTER)
        registerMachineBlocks(RagiumMachineKeys.CANNING_MACHINE, CANNING_MACHINE)
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
        // processors
        RagiumMachineKeys.PROCESSORS.forEach {
            registerMachineBlocks(it, SIMPLE_PROCESSOR)
        }
        registerMachineBlocks(RagiumMachineKeys.BLAST_FURNACE, BLAST_FURNACE)
        registerMachineBlocks(RagiumMachineKeys.CHEMICAL_REACTOR, CHEMICAL_PROCESSOR)
        registerMachineBlocks(RagiumMachineKeys.DISTILLATION_TOWER, DISTILLATION_TOWER)
        registerMachineBlocks(RagiumMachineKeys.ELECTROLYZER, CHEMICAL_PROCESSOR)
        registerMachineBlocks(RagiumMachineKeys.EXTRACTOR, CHEMICAL_PROCESSOR)
        registerMachineBlocks(RagiumMachineKeys.MIXER, CHEMICAL_PROCESSOR)
        registerMachineBlocks(RagiumMachineKeys.MULTI_SMELTER, MULTI_SMELTER)
        registerMachineBlocks(RagiumMachineKeys.SAW_MILL, SAW_MILL)
    }

    @JvmStatic
    private fun registerBlocks(type: BlockEntityType<*>, blocks: Collection<HTContent<Block>>) {
        blocks.map(HTContent<Block>::value).forEach(type::addSupportedBlock)
    }

    @JvmStatic
    private fun registerMachineBlocks(key: HTMachineKey, type: BlockEntityType<*>) {
        RagiumAPI
            .getInstance()
            .machineRegistry
            .getEntry(key)
            .block
            .let(type::addSupportedBlock)
    }
}
