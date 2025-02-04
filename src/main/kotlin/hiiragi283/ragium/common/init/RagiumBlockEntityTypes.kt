package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.common.block.addon.HTCatalystAddonBlockEntity
import hiiragi283.ragium.common.block.addon.HTSlagCollectorBlockEntity
import hiiragi283.ragium.common.block.generator.HTCombustionGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTSolarGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTThermalGeneratorBlockEntity
import hiiragi283.ragium.common.block.machine.*
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
        blocks: Collection<Supplier<out Block>> = listOf(),
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = REGISTER.register(path) { _: ResourceLocation ->
        BlockEntityType.Builder.of(factory, *blocks.map(Supplier<out Block>::get).toTypedArray()).build(null)
    }

    @JvmStatic
    private fun <T : BlockEntity> register(
        path: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
        machine: HTMachineKey,
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

    //    Generator    //

    @JvmField
    val COMBUSTION_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTCombustionGeneratorBlockEntity>> =
        register("combustion_generator", ::HTCombustionGeneratorBlockEntity, RagiumMachineKeys.COMBUSTION_GENERATOR)

    @JvmField
    val SOLAR_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTSolarGeneratorBlockEntity>> =
        register("solar_generator", ::HTSolarGeneratorBlockEntity, RagiumMachineKeys.SOLAR_GENERATOR)

    @JvmField
    val THERMAL_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTThermalGeneratorBlockEntity>> =
        register("thermal_generator", ::HTThermalGeneratorBlockEntity, RagiumMachineKeys.THERMAL_GENERATOR)

    //    Processor    //

    @JvmField
    val BLAST_FURNACE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTBlastFurnaceBlockEntity>> =
        register("blast_furnace", ::HTBlastFurnaceBlockEntity, RagiumMachineKeys.BLAST_FURNACE)

    @JvmField
    val COMPRESSOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTCompressorBlockEntity>> =
        register("compressor", ::HTCompressorBlockEntity, RagiumMachineKeys.COMPRESSOR)

    @JvmField
    val EXTRACTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTExtractorBlockEntity>> =
        register("extractor", ::HTExtractorBlockEntity, RagiumMachineKeys.EXTRACTOR)

    @JvmField
    val INFUSER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTInfuserBlockEntity>> =
        register("infuser", ::HTInfuserBlockEntity, RagiumMachineKeys.INFUSER)

    @JvmField
    val MIXER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTMixerBlockEntity>> =
        register("mixer", ::HTMixerBlockEntity, RagiumMachineKeys.MIXER)

    @JvmField
    val MULTI_SMELTER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTMultiSmelterBlockEntity>> =
        register("multi_smelter", ::HTMultiSmelterBlockEntity, RagiumMachineKeys.MULTI_SMELTER)

    @JvmField
    val REFINERY: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTRefineryBlockEntity>> =
        register("refinery", ::HTRefineryBlockEntity, RagiumMachineKeys.REFINERY)

    //    Storage    //

    @JvmField
    val CATALYST_ADDON: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTCatalystAddonBlockEntity>> =
        register("catalyst_addon", ::HTCatalystAddonBlockEntity, RagiumBlocks.CATALYST_ADDON)

    @JvmField
    val DRUM: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTDrumBlockEntity>> =
        register("drum", ::HTDrumBlockEntity, RagiumBlocks.Drums.entries)

    @JvmField
    val SLAG_COLLECTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTSlagCollectorBlockEntity>> =
        register("slag_collector", ::HTSlagCollectorBlockEntity, RagiumBlocks.SLAG_COLLECTOR)
}
