package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.registry.HTBlockEntityTypeRegister
import hiiragi283.ragium.common.tile.consumer.HTFisherBlockEntity
import hiiragi283.ragium.common.tile.generator.*
import hiiragi283.ragium.common.tile.processor.*
import hiiragi283.ragium.common.tile.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.tile.storage.HTDrumBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object RagiumBlockEntityTypes {
    @JvmField
    val REGISTER = HTBlockEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : BlockEntity> register(
        machine: HTMachineType,
        factory: BlockEntityType.BlockEntitySupplier<T>,
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = REGISTER.registerType(machine.serializedName, factory, machine.holder)

    //    Consumer    //

    @JvmField
    val FISHER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTFisherBlockEntity>> =
        register(HTMachineType.FISHER, ::HTFisherBlockEntity)

    //    Generator    //

    @JvmField
    val COMBUSTION_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTCombustionGeneratorBlockEntity>> =
        register(HTMachineType.COMBUSTION_GENERATOR, ::HTCombustionGeneratorBlockEntity)

    @JvmField
    val ENCH_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTEnchantmentGeneratorBlockEntity>> =
        register(HTMachineType.ENCH_GENERATOR, ::HTEnchantmentGeneratorBlockEntity)

    @JvmField
    val SOLAR_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTSolarGeneratorBlockEntity>> =
        register(HTMachineType.SOLAR_GENERATOR, ::HTSolarGeneratorBlockEntity)

    @JvmField
    val STIRLING_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTStirlingGeneratorBlockEntity>> =
        register(HTMachineType.STIRLING_GENERATOR, ::HTStirlingGeneratorBlockEntity)

    @JvmField
    val THERMAL_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTThermalGeneratorBlockEntity>> =
        register(HTMachineType.THERMAL_GENERATOR, ::HTThermalGeneratorBlockEntity)

    //    Processor    //

    @JvmField
    val ASSEMBLER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTAssemblerBlockEntity>> =
        register(HTMachineType.ASSEMBLER, ::HTAssemblerBlockEntity)

    @JvmField
    val AUTO_CHISEL: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTAutoChiselBlockEntity>> =
        register(HTMachineType.AUTO_CHISEL, ::HTAutoChiselBlockEntity)

    @JvmField
    val BREWERY: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTBreweryBlockEntity>> =
        register(HTMachineType.BREWERY, ::HTBreweryBlockEntity)

    @JvmField
    val COMPRESSOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTCompressorBlockEntity>> =
        register(HTMachineType.COMPRESSOR, ::HTCompressorBlockEntity)

    @JvmField
    val ELECTRIC_FURNACE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTElectricFurnaceBlockEntity>> =
        register(HTMachineType.ELECTRIC_FURNACE, ::HTElectricFurnaceBlockEntity)

    @JvmField
    val EXTRACTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTExtractorBlockEntity>> =
        register(HTMachineType.EXTRACTOR, ::HTExtractorBlockEntity)

    @JvmField
    val GRINDER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTGrinderBlockEntity>> =
        register(HTMachineType.GRINDER, ::HTGrinderBlockEntity)

    @JvmField
    val GROWTH_CHAMBER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTGrowthChamberBlockEntity>> =
        register(HTMachineType.GROWTH_CHAMBER, ::HTGrowthChamberBlockEntity)

    @JvmField
    val INFUSER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTInfuserBlockEntity>> =
        register(HTMachineType.INFUSER, ::HTInfuserBlockEntity)

    @JvmField
    val LASER_ASSEMBLY: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTLaserAssemblyBlockEntity>> =
        register(HTMachineType.LASER_ASSEMBLY, ::HTLaserAssemblyBlockEntity)

    @JvmField
    val MIXER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTMixerBlockEntity>> =
        register(HTMachineType.MIXER, ::HTMixerBlockEntity)

    @JvmField
    val MULTI_SMELTER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTMultiSmelterBlockEntity>> =
        register(HTMachineType.MULTI_SMELTER, ::HTMultiSmelterBlockEntity)

    @JvmField
    val REFINERY: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTRefineryBlockEntity>> =
        register(HTMachineType.REFINERY, ::HTRefineryBlockEntity)

    @JvmField
    val SOLIDIFIER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTSolidifierBlockEntity>> =
        register(HTMachineType.SOLIDIFIER, ::HTSolidifierBlockEntity)

    //    Storage    //

    @JvmField
    val CRATE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTCrateBlockEntity>> =
        REGISTER.registerType("crate", ::HTCrateBlockEntity, RagiumBlocks.CRATES.values)

    @JvmField
    val DRUM: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTDrumBlockEntity>> =
        REGISTER.registerType("drum", ::HTDrumBlockEntity, RagiumBlocks.DRUMS.values)
}
