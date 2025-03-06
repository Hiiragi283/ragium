package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.registry.HTBlockEntityTypeRegister
import hiiragi283.ragium.common.block.generator.*
import hiiragi283.ragium.common.block.machine.HTFisherBlockEntity
import hiiragi283.ragium.common.block.machine.HTManualGrinderBlockEntity
import hiiragi283.ragium.common.block.machine.HTPrimitiveBlastFurnaceBlockEntity
import hiiragi283.ragium.common.block.processor.*
import hiiragi283.ragium.common.block.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.block.storage.HTDrumBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object RagiumBlockEntityTypes {
    @JvmField
    val REGISTER = HTBlockEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : BlockEntity> register(
        path: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
        machine: HTMachineType,
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = REGISTER.registerType(path, factory, machine.getBlock())

    //    Manual Machine    //

    @JvmField
    val MANUAL_GRINDER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTManualGrinderBlockEntity>> =
        REGISTER.registerType("manual_grinder", ::HTManualGrinderBlockEntity, RagiumBlocks.MANUAL_GRINDER)

    @JvmField
    val PRIMITIVE_BLAST_FURNACE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTPrimitiveBlastFurnaceBlockEntity>> =
        REGISTER.registerType(
            "primitive_blast_furnace",
            ::HTPrimitiveBlastFurnaceBlockEntity,
            RagiumBlocks.PRIMITIVE_BLAST_FURNACE,
        )

    //    Consumer    //

    @JvmField
    val FISHER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTFisherBlockEntity>> =
        register("fisher", ::HTFisherBlockEntity, HTMachineType.FISHER)

    //    Generator    //

    @JvmField
    val COMBUSTION_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTCombustionGeneratorBlockEntity>> =
        register("combustion_generator", ::HTCombustionGeneratorBlockEntity, HTMachineType.COMBUSTION_GENERATOR)

    @JvmField
    val ENCH_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTEnchantmentGeneratorBlockEntity>> =
        register("enchantment_generator", ::HTEnchantmentGeneratorBlockEntity, HTMachineType.ENCH_GENERATOR)

    @JvmField
    val SOLAR_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTSolarGeneratorBlockEntity>> =
        register("solar_generator", ::HTSolarGeneratorBlockEntity, HTMachineType.SOLAR_GENERATOR)

    @JvmField
    val STIRLING_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTStirlingGeneratorBlockEntity>> =
        register("stirling_generator", ::HTStirlingGeneratorBlockEntity, HTMachineType.STIRLING_GENERATOR)

    @JvmField
    val THERMAL_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTThermalGeneratorBlockEntity>> =
        register("thermal_generator", ::HTThermalGeneratorBlockEntity, HTMachineType.THERMAL_GENERATOR)

    //    Processor    //

    @JvmField
    val ASSEMBLER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTAssemblerBlockEntity>> =
        register("assembler", ::HTAssemblerBlockEntity, HTMachineType.ASSEMBLER)

    @JvmField
    val AUTO_CHISEL: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTAutoChiselBlockEntity>> =
        register("auto_chisel", ::HTAutoChiselBlockEntity, HTMachineType.AUTO_CHISEL)

    @JvmField
    val BLAST_FURNACE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTBlastFurnaceBlockEntity>> =
        register("blast_furnace", ::HTBlastFurnaceBlockEntity, HTMachineType.BLAST_FURNACE)

    @JvmField
    val BREWERY: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTBreweryBlockEntity>> =
        register("brewery", ::HTBreweryBlockEntity, HTMachineType.BREWERY)

    @JvmField
    val COMPRESSOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTCompressorBlockEntity>> =
        register("compressor", ::HTCompressorBlockEntity, HTMachineType.COMPRESSOR)

    @JvmField
    val CRUSHER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTCrusherBlockEntity>> =
        register("crusher", ::HTCrusherBlockEntity, HTMachineType.CRUSHER)

    @JvmField
    val ELECTRIC_FURNACE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTElectricFurnaceBlockEntity>> =
        register("electric_furnace", ::HTElectricFurnaceBlockEntity, HTMachineType.ELECTRIC_FURNACE)

    @JvmField
    val EXTRACTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTExtractorBlockEntity>> =
        register("extractor", ::HTExtractorBlockEntity, HTMachineType.EXTRACTOR)

    @JvmField
    val GRINDER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTGrinderBlockEntity>> =
        register("grinder", ::HTGrinderBlockEntity, HTMachineType.GRINDER)

    @JvmField
    val GROWTH_CHAMBER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTGrowthChamberBlockEntity>> =
        register("growth_chamber", ::HTGrowthChamberBlockEntity, HTMachineType.GROWTH_CHAMBER)

    @JvmField
    val INFUSER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTInfuserBlockEntity>> =
        register("infuser", ::HTInfuserBlockEntity, HTMachineType.INFUSER)

    @JvmField
    val LASER_ASSEMBLY: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTLaserAssemblyBlockEntity>> =
        register("laser_assembly", ::HTLaserAssemblyBlockEntity, HTMachineType.LASER_ASSEMBLY)

    @JvmField
    val MIXER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTMixerBlockEntity>> =
        register("mixer", ::HTMixerBlockEntity, HTMachineType.MIXER)

    @JvmField
    val MULTI_SMELTER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTMultiSmelterBlockEntity>> =
        register("multi_smelter", ::HTMultiSmelterBlockEntity, HTMachineType.MULTI_SMELTER)

    @JvmField
    val REFINERY: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTRefineryBlockEntity>> =
        register("refinery", ::HTRefineryBlockEntity, HTMachineType.REFINERY)

    @JvmField
    val SOLIDIFIER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTSolidifierBlockEntity>> =
        register("solidifier", ::HTSolidifierBlockEntity, HTMachineType.SOLIDIFIER)

    //    Storage    //

    @JvmField
    val CRATE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTCrateBlockEntity>> =
        REGISTER.registerType("crate", ::HTCrateBlockEntity, RagiumBlocks.CRATES.values)

    @JvmField
    val DRUM: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTDrumBlockEntity>> =
        REGISTER.registerType("drum", ::HTDrumBlockEntity, RagiumBlocks.DRUMS.values)
}
