package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockEntityType
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.block.HTGeneratorBlockEntityBase
import hiiragi283.ragium.api.machine.block.HTProcessorBlockEntityBase
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.entity.*
import hiiragi283.ragium.common.machine.*
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumBlockEntityTypes {
    @JvmField
    val BLAST_FURNACE: BlockEntityType<HTBlastFurnaceBlockEntity> =
        register("blast_furnace", ::HTBlastFurnaceBlockEntity)

    @JvmField
    val COMBUSTION_GENERATOR: BlockEntityType<HTCombustionGeneratorBlockEntity> =
        register("combustion_generator", ::HTCombustionGeneratorBlockEntity)

    @JvmField
    val CREATIVE_SOURCE: BlockEntityType<HTCreativeSourceBlockEntity> =
        register("creative_source", ::HTCreativeSourceBlockEntity)

    @JvmField
    val DISTILLATION_TOWER: BlockEntityType<HTDistillationTowerBlockEntity> =
        register("distillation_tower", ::HTDistillationTowerBlockEntity)

    @JvmField
    val DRAIN: BlockEntityType<HTDrainBlockEntity> =
        register("drain", ::HTDrainBlockEntity)

    @JvmField
    val DRUM: BlockEntityType<HTDrumBlockEntity> =
        register("drum", ::HTDrumBlockEntity)

    @JvmField
    val EXPORTER: BlockEntityType<HTExporterBlockEntity> =
        register("exporter", ::HTExporterBlockEntity)

    @JvmField
    val FLUID_DRILL: BlockEntityType<HTFluidDrillBlockEntity> =
        register("fluid_drill", ::HTFluidDrillBlockEntity)

    @JvmField
    val FLUID_PIPE: BlockEntityType<HTPipeBlockEntity> =
        register("fluid_pipe", ::HTPipeBlockEntity)

    @JvmField
    val ITEM_DISPLAY: BlockEntityType<HTItemDisplayBlockEntity> =
        register("item_display", ::HTItemDisplayBlockEntity)

    @JvmField
    val LARGE_PROCESSOR: BlockEntityType<HTLargeProcessorBlockEntity> =
        register("large_processor", ::HTLargeProcessorBlockEntity)

    @JvmField
    val MANUAL_FORGE: BlockEntityType<HTManualForgeBlockEntity> =
        register("manual_forge", ::HTManualForgeBlockEntity)

    @JvmField
    val MANUAL_GRINDER: BlockEntityType<HTManualGrinderBlockEntity> =
        register("manual_grinder", ::HTManualGrinderBlockEntity)

    @JvmField
    val MULTI_SMELTER: BlockEntityType<HTMultiSmelterBlockEntity> =
        register("multi_smelter", ::HTMultiSmelterBlockEntity)

    @JvmField
    val MANUAL_MIXER: BlockEntityType<HTManualMixerBlockEntity> =
        register("manual_mixer", ::HTManualMixerBlockEntity)

    @JvmField
    val SAW_MILL: BlockEntityType<HTSawmillBlockEntity> =
        register("saw_mill", ::HTSawmillBlockEntity)

    @JvmField
    val SIMPLE_GENERATOR: BlockEntityType<HTGeneratorBlockEntityBase.Simple> =
        register("simple_generator", HTGeneratorBlockEntityBase::Simple)

    @JvmField
    val SIMPLE_PROCESSOR: BlockEntityType<HTProcessorBlockEntityBase.Simple> =
        register("simple_processor", HTProcessorBlockEntityBase::Simple)

    @JvmField
    val STEAM_GENERATOR: BlockEntityType<HTSteamGeneratorBlockEntity> =
        register("steam_generator", ::HTSteamGeneratorBlockEntity)

    @JvmStatic
    private fun <T : HTBlockEntityBase> register(name: String, factory: BlockEntityType.BlockEntityFactory<T>): BlockEntityType<T> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            RagiumAPI.id(name),
            blockEntityType(factory),
        )

    @JvmStatic
    fun init() {
        RagiumContents.Exporters.entries
            .map(RagiumContents.Exporters::value)
            .forEach(EXPORTER::addSupportedBlock)
        RagiumContents.Pipes.entries
            .map(RagiumContents.Pipes::value)
            .forEach(FLUID_PIPE::addSupportedBlock)
        RagiumContents.Drums.entries
            .map(RagiumContents.Drums::value)
            .forEach(DRUM::addSupportedBlock)
        ITEM_DISPLAY.addSupportedBlock(RagiumBlocks.ITEM_DISPLAY)
        LARGE_PROCESSOR.addSupportedBlock(RagiumBlocks.LARGE_PROCESSOR)
        MANUAL_FORGE.addSupportedBlock(RagiumBlocks.MANUAL_FORGE)
        MANUAL_GRINDER.addSupportedBlock(RagiumBlocks.MANUAL_GRINDER)
        MANUAL_MIXER.addSupportedBlock(RagiumBlocks.MANUAL_MIXER)
        // consumers
        registerMachineBlocks(RagiumMachineKeys.DRAIN, DRAIN)
        // generators
        RagiumMachineKeys.GENERATORS.forEach {
            registerMachineBlocks(it, SIMPLE_GENERATOR)
        }
        registerMachineBlocks(RagiumMachineKeys.COMBUSTION_GENERATOR, COMBUSTION_GENERATOR)
        registerMachineBlocks(RagiumMachineKeys.STEAM_GENERATOR, STEAM_GENERATOR)
        // processors
        RagiumMachineKeys.PROCESSORS.forEach {
            registerMachineBlocks(it, SIMPLE_PROCESSOR)
        }
        registerMachineBlocks(RagiumMachineKeys.BLAST_FURNACE, BLAST_FURNACE)
        registerMachineBlocks(RagiumMachineKeys.DISTILLATION_TOWER, DISTILLATION_TOWER)
        registerMachineBlocks(RagiumMachineKeys.FLUID_DRILL, FLUID_DRILL)
        registerMachineBlocks(RagiumMachineKeys.MULTI_SMELTER, MULTI_SMELTER)
        registerMachineBlocks(RagiumMachineKeys.SAW_MILL, SAW_MILL)
    }

    @JvmStatic
    private fun registerMachineBlocks(key: HTMachineKey, type: BlockEntityType<*>) {
        RagiumAPI
            .getInstance()
            .machineRegistry
            .getEntry(key)
            .blocks
            .forEach(type::addSupportedBlock)
    }
}
