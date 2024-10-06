package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineBlockRegistry
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.entity.*
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTHeatGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.*
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
    val GENERATOR_MACHINE: BlockEntityType<HTGeneratorBlockEntity.Simple> =
        register("generator_machine", HTGeneratorBlockEntity::Simple)

    @JvmField
    val HEAT_GENERATOR: BlockEntityType<HTHeatGeneratorBlockEntity> =
        register("heat_generator", ::HTHeatGeneratorBlockEntity)

    //    Processor    //

    @JvmField
    val BLAST_FURNACE: BlockEntityType<HTBlastFurnaceBlockEntity> =
        register("blast_furnace", ::HTBlastFurnaceBlockEntity)

    @JvmField
    val DISTILLATION_TOWER: BlockEntityType<HTDistillationTowerBlockEntity> =
        register("distillation_tower", ::HTDistillationTowerBlockEntity)

    @JvmField
    val FLUID_DRILL: BlockEntityType<HTFluidDrillBlockEntity> =
        register("fluid_drill", ::HTFluidDrillBlockEntity)

    @JvmField
    val MANUAL_GRINDER: BlockEntityType<HTManualGrinderBlockEntity> =
        register("manual_grinder", ::HTManualGrinderBlockEntity)

    @JvmField
    val SAW_MILL: BlockEntityType<HTSawMillBlockEntity> =
        register("distillation_tower", ::HTSawMillBlockEntity)

    @JvmField
    val PROCESSOR_MACHINE: BlockEntityType<HTProcessorBlockEntityBase> =
        register("processor_machine", HTProcessorBlockEntityBase::Simple)

    @JvmStatic
    private fun <T : HTBlockEntityBase> register(name: String, factory: BlockEntityType.BlockEntityFactory<T>): BlockEntityType<T> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            RagiumAPI.id(name),
            blockEntityType(factory),
        )

    @JvmStatic
    fun init() {
        ALCHEMICAL_INFUSER.addSupportedBlock(RagiumContents.ALCHEMICAL_INFUSER)
        ITEM_DISPLAY.addSupportedBlock(RagiumContents.ITEM_DISPLAY)
        MANUAL_GRINDER.addSupportedBlock(RagiumContents.MANUAL_GRINDER)

        addMachineBlocks(RagiumMachineTypes.HEAT_GENERATOR, HEAT_GENERATOR)

        addMachineBlocks(RagiumMachineTypes.BLAST_FURNACE, BLAST_FURNACE)
        addMachineBlocks(RagiumMachineTypes.DISTILLATION_TOWER, DISTILLATION_TOWER)
        addMachineBlocks(RagiumMachineTypes.FLUID_DRILL, FLUID_DRILL)
        addMachineBlocks(RagiumMachineTypes.SAW_MILL, SAW_MILL)

        RagiumMachineTypes.Generator.entries.forEach { generator: RagiumMachineTypes.Generator ->
            addMachineBlocks(generator, GENERATOR_MACHINE)
        }

        RagiumMachineTypes.Processor.entries.forEach { processor: RagiumMachineTypes.Processor ->
            addMachineBlocks(processor, PROCESSOR_MACHINE)
        }
    }

    @JvmStatic
    private fun addMachineBlocks(machineType: HTMachineConvertible, type: BlockEntityType<*>) {
        HTMachineBlockRegistry
            .getAllTier(machineType)
            .values
            .forEach(type::addSupportedBlock)
    }
}
