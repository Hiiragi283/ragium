package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockEntityType
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.entity.*
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumBlockEntityTypes {
    @JvmField
    val CREATIVE_SOURCE: BlockEntityType<HTCreativeSourceBlockEntity> =
        register("creative_source", ::HTCreativeSourceBlockEntity)

    @JvmField
    val EXPORTER: BlockEntityType<HTExporterBlockEntity> =
        register("exporter", ::HTExporterBlockEntity)

    @JvmField
    val FLUID_PIPE: BlockEntityType<HTPipeBlockEntity> =
        register("fluid_pipe", ::HTPipeBlockEntity)

    @JvmField
    val ITEM_DISPLAY: BlockEntityType<HTItemDisplayBlockEntity> =
        register("item_display", ::HTItemDisplayBlockEntity)

    @JvmField
    val META_MACHINE: BlockEntityType<HTMetaMachineBlockEntity> =
        register("meta_machine", ::HTMetaMachineBlockEntity)

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
    val TRADER_STATION: BlockEntityType<HTTraderStationBlockEntity> =
        register("trader_station", ::HTTraderStationBlockEntity)

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
        ITEM_DISPLAY.addSupportedBlock(RagiumBlocks.ITEM_DISPLAY)
        MANUAL_FORGE.addSupportedBlock(RagiumBlocks.MANUAL_FORGE)
        MANUAL_GRINDER.addSupportedBlock(RagiumBlocks.MANUAL_GRINDER)
        MANUAL_MIXER.addSupportedBlock(RagiumBlocks.MANUAL_MIXER)
        META_MACHINE.addSupportedBlock(RagiumBlocks.META_CONSUMER)
        META_MACHINE.addSupportedBlock(RagiumBlocks.META_GENERATOR)
        META_MACHINE.addSupportedBlock(RagiumBlocks.META_PROCESSOR)
        TRADER_STATION.addSupportedBlock(RagiumBlocks.TRADER_STATION)
    }
}
