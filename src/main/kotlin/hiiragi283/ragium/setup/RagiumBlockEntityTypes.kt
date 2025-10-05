package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityTypeRegister
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTDimensionalAnchorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTEnergyNetworkAccessBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTExpCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTItemBufferBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTLavaCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTMilkCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTMobCapturerBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTTelepadBlockentity
import hiiragi283.ragium.common.block.entity.device.HTWaterCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTCombustionGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTNuclearReactorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTSolarGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTThermalGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBlockBreakerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBreweryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCompressorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCuttingMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPulverizerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSimulatorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTWasherBlockEntity
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTDrumVariant
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.common.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent
import org.slf4j.Logger
import java.util.function.Supplier
import kotlin.enums.enumEntries

object RagiumBlockEntityTypes {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val REGISTER = HTDeferredBlockEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        eventBus.addListener(::addSupportedBlock)
        eventBus.addListener(::registerBlockCapabilities)
    }

    @JvmStatic
    private fun <BE : HTBlockEntity> registerTick(name: String, factory: (BlockPos, BlockState) -> BE): HTDeferredBlockEntityType<BE> =
        REGISTER.registerType(name, factory, HTBlockEntity::tickClient, HTBlockEntity::tickServer)

    //    Generator    //

    @JvmField
    val GENERATORS: Map<HTGeneratorVariant, HTDeferredBlockEntityType<HTBlockEntity>> =
        HTGeneratorVariant.entries.associateWith { variant: HTGeneratorVariant ->
            val factory = when (variant) {
                // Basic
                HTGeneratorVariant.THERMAL -> ::HTThermalGeneratorBlockEntity
                // Advanced
                HTGeneratorVariant.COMBUSTION -> ::HTCombustionGeneratorBlockEntity
                HTGeneratorVariant.SOLAR -> ::HTSolarGeneratorBlockEntity
                // Elite
                HTGeneratorVariant.NUCLEAR_REACTOR -> ::HTNuclearReactorBlockEntity
            }
            registerTick(variant.serializedName, factory)
        }

    //    Machine    //

    @JvmField
    val MACHINES: Map<HTMachineVariant, HTDeferredBlockEntityType<HTBlockEntity>> =
        HTMachineVariant.entries.associateWith { variant ->
            val factory = when (variant) {
                // Basic
                HTMachineVariant.ALLOY_SMELTER -> ::HTAlloySmelterBlockEntity
                HTMachineVariant.BLOCK_BREAKER -> ::HTBlockBreakerBlockEntity
                HTMachineVariant.COMPRESSOR -> ::HTCompressorBlockEntity
                HTMachineVariant.CUTTING_MACHINE -> ::HTCuttingMachineBlockEntity
                HTMachineVariant.EXTRACTOR -> ::HTExtractorBlockEntity
                HTMachineVariant.PULVERIZER -> ::HTPulverizerBlockEntity
                // Advanced
                HTMachineVariant.CRUSHER -> ::HTCrusherBlockEntity
                HTMachineVariant.MELTER -> ::HTMelterBlockEntity
                HTMachineVariant.REFINERY -> ::HTRefineryBlockEntity
                HTMachineVariant.WASHER -> ::HTWasherBlockEntity
                // Elite
                HTMachineVariant.BREWERY -> ::HTBreweryBlockEntity
                HTMachineVariant.MULTI_SMELTER -> ::HTMultiSmelterBlockEntity
                HTMachineVariant.SIMULATOR -> ::HTSimulatorBlockEntity
            }
            registerTick(variant.serializedName, factory)
        }

    //    Device    //

    @JvmField
    val DEVICES: Map<HTDeviceVariant, HTDeferredBlockEntityType<HTBlockEntity>> =
        HTDeviceVariant.entries.associateWith { variant: HTDeviceVariant ->
            val factory = when (variant) {
                // Basic
                HTDeviceVariant.ITEM_BUFFER -> ::HTItemBufferBlockEntity
                HTDeviceVariant.MILK_COLLECTOR -> ::HTMilkCollectorBlockEntity
                HTDeviceVariant.WATER_COLLECTOR -> ::HTWaterCollectorBlockEntity

                // Advanced
                HTDeviceVariant.ENI -> HTEnergyNetworkAccessBlockEntity::Simple
                HTDeviceVariant.EXP_COLLECTOR -> ::HTExpCollectorBlockEntity
                HTDeviceVariant.LAVA_COLLECTOR -> ::HTLavaCollectorBlockEntity

                // Elite
                HTDeviceVariant.DIM_ANCHOR -> ::HTDimensionalAnchorBlockEntity
                HTDeviceVariant.TELEPAD -> ::HTTelepadBlockentity
                HTDeviceVariant.MOB_CAPTURER -> ::HTMobCapturerBlockEntity

                // Creative
                HTDeviceVariant.CEU -> HTEnergyNetworkAccessBlockEntity::Creative
            }
            registerTick(variant.serializedName, factory)
        }

    //    Storage    //

    @JvmField
    val DRUMS: Map<HTDrumVariant, HTDeferredBlockEntityType<HTBlockEntity>> =
        HTDrumVariant.entries.associateWith { variant: HTDrumVariant ->
            val factory = when (variant) {
                HTDrumVariant.SMALL -> HTDrumBlockEntity::Small
                HTDrumVariant.MEDIUM -> HTDrumBlockEntity::Medium
                HTDrumVariant.LARGE -> HTDrumBlockEntity::Large
                HTDrumVariant.HUGE -> HTDrumBlockEntity::Huge
            }
            registerTick("${variant.serializedName}_drum", factory)
        }

    //    Event    //

    // Supported Blocks
    @JvmStatic
    private fun addSupportedBlock(event: BlockEntityTypeAddBlocksEvent) {
        addAll<HTGeneratorVariant>(event)
        addAll<HTMachineVariant>(event)
        addAll<HTDeviceVariant>(event)
        addAll<HTDrumVariant>(event)

        LOGGER.info("Added supported blocks to BlockEntityType!")
    }

    @JvmStatic
    private fun add(event: BlockEntityTypeAddBlocksEvent, type: HTDeferredBlockEntityType<*>, block: Supplier<out Block>) {
        event.modify(type.get(), block.get())
    }

    @JvmStatic
    private inline fun <reified V> addAll(event: BlockEntityTypeAddBlocksEvent) where V : HTVariantKey.WithBE<*>, V : Enum<V> {
        for (variant: V in enumEntries<V>()) {
            add(event, variant.blockEntityHolder, variant.blockHolder)
        }
    }

    // Capabilities
    @JvmStatic
    private fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        registerHandlers(event, GENERATORS)
        registerHandlers(event, MACHINES)
        registerHandlers(event, DEVICES)
        registerHandlers(event, DRUMS)

        LOGGER.info("Registered Block Capabilities!")
    }

    @JvmStatic
    private fun registerHandlers(event: RegisterCapabilitiesEvent, types: Map<*, HTDeferredBlockEntityType<out HTBlockEntity>>) {
        for (type: HTDeferredBlockEntityType<out HTBlockEntity> in types.values) {
            val type1: BlockEntityType<out HTBlockEntity> = type.get()
            event.registerBlockEntity(
                HTMultiCapability.ITEM.blockCapability,
                type1,
                HTHandlerBlockEntity::getItemHandler,
            )
            event.registerBlockEntity(
                HTMultiCapability.FLUID.blockCapability,
                type1,
                HTHandlerBlockEntity::getFluidHandler,
            )
            event.registerBlockEntity(
                HTMultiCapability.ENERGY.blockCapability,
                type1,
                HTHandlerBlockEntity::getEnergyStorage,
            )
        }
    }
}
