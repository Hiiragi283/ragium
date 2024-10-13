package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.blockEntityType
import hiiragi283.ragium.common.block.entity.*
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

    @JvmField
    val META_MACHINE: BlockEntityType<HTMetaMachineBlockEntity> =
        register("meta_machine", ::HTMetaMachineBlockEntity)

    @JvmField
    val MANUAL_GRINDER: BlockEntityType<HTManualGrinderBlockEntity> =
        register("manual_grinder", ::HTManualGrinderBlockEntity)

    @JvmStatic
    private fun <T : HTBlockEntityBase> register(name: String, factory: BlockEntityType.BlockEntityFactory<T>): BlockEntityType<T> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            RagiumAPI.id(name),
            blockEntityType(factory),
        )

    @JvmStatic
    fun init() {
        ALCHEMICAL_INFUSER.addSupportedBlock(RagiumBlocks.ALCHEMICAL_INFUSER)
        ITEM_DISPLAY.addSupportedBlock(RagiumBlocks.ITEM_DISPLAY)
        MANUAL_GRINDER.addSupportedBlock(RagiumBlocks.MANUAL_GRINDER)
        META_MACHINE.addSupportedBlock(RagiumBlocks.META_MACHINE)
    }
}
