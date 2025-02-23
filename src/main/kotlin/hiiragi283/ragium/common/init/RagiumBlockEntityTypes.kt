package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.block.addon.HTSlagCollectorBlockEntity
import hiiragi283.ragium.common.block.generator.HTCombustionGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTSolarGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTStirlingGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTThermalGeneratorBlockEntity
import hiiragi283.ragium.common.block.machine.HTDisenchantingTableBlockEntity
import hiiragi283.ragium.common.block.machine.HTFisherBlockEntity
import hiiragi283.ragium.common.block.machine.HTManualGrinderBlockEntity
import hiiragi283.ragium.common.block.machine.HTPrimitiveBlastFurnaceBlockEntity
import hiiragi283.ragium.common.block.processor.*
import hiiragi283.ragium.common.block.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.block.storage.HTDrumBlockEntity
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object RagiumBlockEntityTypes {
    @JvmField
    val REGISTER: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : BlockEntity> register(
        path: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
        block: Supplier<out Block>,
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = REGISTER.register(path) { _: ResourceLocation ->
        BlockEntityType.Builder.of(factory, block.get()).build(null)
    }

    @JvmStatic
    private fun <T : BlockEntity> register(
        path: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
        machine: HTMachineType,
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = REGISTER.register(path) { _: ResourceLocation ->
        BlockEntityType.Builder.of(factory, machine.getBlock().get()).build(null)
    }

    //    Manual Machine    //

    @JvmField
    val MANUAL_GRINDER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTManualGrinderBlockEntity>> =
        register("manual_grinder", ::HTManualGrinderBlockEntity, RagiumBlocks.MANUAL_GRINDER)

    @JvmField
    val PRIMITIVE_BLAST_FURNACE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTPrimitiveBlastFurnaceBlockEntity>> =
        register("primitive_blast_furnace", ::HTPrimitiveBlastFurnaceBlockEntity, RagiumBlocks.PRIMITIVE_BLAST_FURNACE)

    @JvmField
    val DISENCHANTING_TABLE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTDisenchantingTableBlockEntity>> =
        register("disenchanting_table", ::HTDisenchantingTableBlockEntity, RagiumBlocks.DISENCHANTING_TABLE)

    //    Consumer    //

    @JvmField
    val FISHER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTFisherBlockEntity>> =
        register("fisher", ::HTFisherBlockEntity, HTMachineType.FISHER)

    //    Generator    //

    @JvmField
    val COMBUSTION_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTCombustionGeneratorBlockEntity>> =
        register("combustion_generator", ::HTCombustionGeneratorBlockEntity, HTMachineType.COMBUSTION_GENERATOR)

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
    val BLAST_FURNACE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTBlastFurnaceBlockEntity>> =
        register("blast_furnace", ::HTBlastFurnaceBlockEntity, HTMachineType.BLAST_FURNACE)

    @JvmField
    val COMPRESSOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTCompressorBlockEntity>> =
        register("compressor", ::HTCompressorBlockEntity, HTMachineType.COMPRESSOR)

    @JvmField
    val EXTRACTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTExtractorBlockEntity>> =
        register("extractor", ::HTExtractorBlockEntity, HTMachineType.EXTRACTOR)

    @JvmField
    val GRINDER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTGrinderBlockEntity>> =
        register("grinder", ::HTGrinderBlockEntity, HTMachineType.GRINDER)

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

    //    Storage    //

    @JvmField
    val CRATE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTCrateBlockEntity>> =
        register("crate", ::HTCrateBlockEntity, RagiumBlocks.IRON_CRATE)

    @JvmField
    val DRUM: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTDrumBlockEntity>> =
        register("drum", ::HTDrumBlockEntity, RagiumBlocks.COPPER_DRUM)

    @JvmField
    val SLAG_COLLECTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTSlagCollectorBlockEntity>> =
        register("slag_collector", ::HTSlagCollectorBlockEntity, RagiumBlocks.SLAG_COLLECTOR)
}
