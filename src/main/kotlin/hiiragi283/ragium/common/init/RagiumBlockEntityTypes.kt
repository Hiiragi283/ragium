package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.entity.*
import hiiragi283.ragium.common.block.entity.machine.HTBlastFurnaceBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTDistillationTowerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTProcessorBlockEntityBase
import hiiragi283.ragium.common.util.blockEntityType
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumBlockEntityTypes {
    @JvmField
    val ALCHEMICAL_INFUSER: BlockEntityType<HTAlchemicalInfuserBlockEntity> =
        register("alchemical_infuser", ::HTAlchemicalInfuserBlockEntity)

    @JvmField
    val CREATIVE_SOURCE: BlockEntityType<HTCreativeSourceBlockEntity> =
        register("creative_source", ::HTCreativeSourceBlockEntity)

    @JvmField
    val DRIVE_SCANNER: BlockEntityType<HTDriveScannerBlockEntity> =
        register("drive_scanner", ::HTDriveScannerBlockEntity)

    @JvmField
    val ITEM_DISPLAY: BlockEntityType<HTItemDisplayBlockEntity> =
        register("item_display", ::HTItemDisplayBlockEntity)

    //    Generator    //

    @JvmField
    val GENERATOR_MACHINE: BlockEntityType<HTGeneratorBlockEntity> =
        register("generator_machine", ::HTGeneratorBlockEntity)

    //    Processor    //

    @JvmField
    val BLAST_FURNACE: BlockEntityType<HTBlastFurnaceBlockEntity> =
        register("blast_furnace", ::HTBlastFurnaceBlockEntity)

    @JvmField
    val DISTILLATION_TOWER: BlockEntityType<HTDistillationTowerBlockEntity> =
        register("distillation_tower", ::HTDistillationTowerBlockEntity)

    @JvmField
    val MANUAL_GRINDER: BlockEntityType<HTManualGrinderBlockEntity> =
        register("manual_grinder", ::HTManualGrinderBlockEntity)

    @JvmField
    val PROCESSOR_MACHINE: BlockEntityType<HTProcessorBlockEntityBase.Simple> =
        register("processor_machine", HTProcessorBlockEntityBase::Simple)

    @JvmStatic
    private fun <T : HTBlockEntityBase> register(name: String, factory: BlockEntityType.BlockEntityFactory<T>): BlockEntityType<T> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Ragium.id(name),
            blockEntityType(factory),
        )

    @JvmStatic
    fun init() {
        ALCHEMICAL_INFUSER.addSupportedBlock(RagiumContents.ALCHEMICAL_INFUSER)
        ITEM_DISPLAY.addSupportedBlock(RagiumContents.ITEM_DISPLAY)
        MANUAL_GRINDER.addSupportedBlock(RagiumContents.MANUAL_GRINDER)
    }
}
