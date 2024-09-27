package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.entity.*
import hiiragi283.ragium.common.block.entity.generator.*
import hiiragi283.ragium.common.block.entity.machine.HTBlastFurnaceBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBrickAlloyFurnaceBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTDistillationTowerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSingleMachineBlockEntity
import hiiragi283.ragium.common.util.blockEntityType
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumBlockEntityTypes {
    @JvmField
    val ALCHEMICAL_INFUSER: BlockEntityType<HTAlchemicalInfuserBlockEntity> =
        register("alchemical_infuser", ::HTAlchemicalInfuserBlockEntity)

    @JvmField
    val BLAST_FURNACE: BlockEntityType<HTBlastFurnaceBlockEntity> =
        register("blast_furnace", ::HTBlastFurnaceBlockEntity)

    @JvmField
    val BRICK_ALLOY_FURNACE: BlockEntityType<HTBrickAlloyFurnaceBlockEntity> =
        register("brick_alloy_furnace", ::HTBrickAlloyFurnaceBlockEntity)

    @JvmField
    val BUFFER: BlockEntityType<HTBufferBlockEntity> =
        register("buffer", ::HTBufferBlockEntity)

    @JvmField
    val BLAZING_BOX: BlockEntityType<HTBlazingBoxBlockEntity> =
        register("blazing_box", ::HTBlazingBoxBlockEntity)

    @JvmField
    val BURNING_BOX: BlockEntityType<HTBurningBoxBlockEntity> =
        register("burning_box", ::HTBurningBoxBlockEntity)

    @JvmField
    val CREATIVE_SOURCE: BlockEntityType<HTCreativeSourceBlockEntity> =
        register("creative_source", ::HTCreativeSourceBlockEntity)

    @JvmField
    val DISTILLATION_TOWER: BlockEntityType<HTDistillationTowerBlockEntity> =
        register("distillation_tower", ::HTDistillationTowerBlockEntity)

    @JvmField
    val DRIVE_SCANNER: BlockEntityType<HTDriveScannerBlockEntity> =
        register("drive_scanner", ::HTDriveScannerBlockEntity)

    @JvmField
    val ITEM_DISPLAY: BlockEntityType<HTItemDisplayBlockEntity> =
        register("item_display", ::HTItemDisplayBlockEntity)

    @JvmField
    val MANUAL_GRINDER: BlockEntityType<HTManualGrinderBlockEntity> =
        register("manual_grinder", ::HTManualGrinderBlockEntity)

    @JvmField
    val SINGLE_MACHINE: BlockEntityType<HTSingleMachineBlockEntity> =
        register("single_machine", ::HTSingleMachineBlockEntity)

    @JvmField
    val WATER_GENERATOR: BlockEntityType<HTWaterGeneratorBlockEntity> =
        register("water_generator", ::HTWaterGeneratorBlockEntity)

    @JvmField
    val WIND_GENERATOR: BlockEntityType<HTWindGeneratorBlockEntity> =
        register("wind_generator", ::HTWindGeneratorBlockEntity)

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
        BRICK_ALLOY_FURNACE.addSupportedBlock(RagiumContents.BRICK_ALLOY_FURNACE)
        ITEM_DISPLAY.addSupportedBlock(RagiumContents.ITEM_DISPLAY)
        MANUAL_GRINDER.addSupportedBlock(RagiumContents.MANUAL_GRINDER)
    }
}
