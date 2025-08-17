package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.registry.HTBlockEntityTypeRegister
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTDeviceBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTDimensionalAnchorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTEnergyNetworkAccessBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTExpCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTItemBufferBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTLavaCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTMilkDrainBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTSprinklerBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTWaterCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.dynamo.HTGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBlockBreakerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCompressorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTEngraverBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTInfuserBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMixerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPulverizerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSolidifierBlockEntity
import hiiragi283.ragium.util.variant.HTDrumVariant
import hiiragi283.ragium.util.variant.HTGeneratorVariant
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent
import net.neoforged.neoforge.registries.DeferredBlock
import org.slf4j.Logger
import java.util.function.Supplier
import kotlin.enums.enumEntries

object RagiumBlockEntityTypes {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val REGISTER = HTBlockEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.addAlias(RagiumAPI.id("item_collector"), RagiumAPI.id("item_buffer"))

        HTDrumVariant.entries
        HTGeneratorVariant.entries

        REGISTER.register(eventBus)

        eventBus.addListener(::addSupportedBlock)
        eventBus.addListener(::registerBlockCapabilities)
    }

    @JvmStatic
    fun <T : HTTickAwareBlockEntity> registerTick(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
    ): HTDeferredBlockEntityType<T> = REGISTER.registerType(name, factory, HTTickAwareBlockEntity::serverTick)

    //    Dynamo    //

    //    Machine    //

    @JvmField
    val ALLOY_SMELTER: HTDeferredBlockEntityType<HTAlloySmelterBlockEntity> = registerTick("alloy_smelter", ::HTAlloySmelterBlockEntity)

    @JvmField
    val BLOCK_BREAKER: HTDeferredBlockEntityType<HTBlockBreakerBlockEntity> = registerTick("block_breaker", ::HTBlockBreakerBlockEntity)

    @JvmField
    val COMPRESSOR: HTDeferredBlockEntityType<HTCompressorBlockEntity> = registerTick("compressor", ::HTCompressorBlockEntity)

    @JvmField
    val CRUSHER: HTDeferredBlockEntityType<HTCrusherBlockEntity> = registerTick("crusher", ::HTCrusherBlockEntity)

    @JvmField
    val ENGRAVER: HTDeferredBlockEntityType<HTEngraverBlockEntity> = registerTick("engraver", ::HTEngraverBlockEntity)

    @JvmField
    val EXTRACTOR: HTDeferredBlockEntityType<HTExtractorBlockEntity> = registerTick("extractor", ::HTExtractorBlockEntity)

    @JvmField
    val INFUSER: HTDeferredBlockEntityType<HTInfuserBlockEntity> = registerTick("infuser", ::HTInfuserBlockEntity)

    @JvmField
    val MELTER: HTDeferredBlockEntityType<HTMelterBlockEntity> = registerTick("melter", ::HTMelterBlockEntity)

    @JvmField
    val MIXER: HTDeferredBlockEntityType<HTMixerBlockEntity> = registerTick("mixer", ::HTMixerBlockEntity)

    @JvmField
    val PULVERIZER: HTDeferredBlockEntityType<HTPulverizerBlockEntity> = registerTick("pulverizer", ::HTPulverizerBlockEntity)

    @JvmField
    val REFINERY: HTDeferredBlockEntityType<HTRefineryBlockEntity> = registerTick("refinery", ::HTRefineryBlockEntity)

    @JvmField
    val SOLIDIFIER: HTDeferredBlockEntityType<HTSolidifierBlockEntity> = registerTick("solidifier", ::HTSolidifierBlockEntity)

    @JvmField
    val BASIC_MACHINES: List<HTDeferredBlockEntityType<out HTMachineBlockEntity>> = listOf(
        BLOCK_BREAKER,
        COMPRESSOR,
        ENGRAVER,
        EXTRACTOR,
        PULVERIZER,
    )

    @JvmField
    val ADVANCED_MACHINES: List<HTDeferredBlockEntityType<out HTMachineBlockEntity>> = listOf(
        ALLOY_SMELTER,
        CRUSHER,
        INFUSER,
        MELTER,
        MIXER,
        REFINERY,
        SOLIDIFIER,
    )

    //    Device    //

    @JvmField
    val CEU: HTDeferredBlockEntityType<HTEnergyNetworkAccessBlockEntity> = registerTick(
        "creative_energy_unit",
        HTEnergyNetworkAccessBlockEntity::Creative,
    )

    @JvmField
    val DIM_ANCHOR: HTDeferredBlockEntityType<HTDimensionalAnchorBlockEntity> = REGISTER.registerType(
        "dimensional_anchor",
        ::HTDimensionalAnchorBlockEntity,
    )

    @JvmField
    val ENI: HTDeferredBlockEntityType<HTEnergyNetworkAccessBlockEntity> = registerTick(
        "energy_network_interface",
        HTEnergyNetworkAccessBlockEntity::Simple,
    )

    @JvmField
    val EXP_COLLECTOR: HTDeferredBlockEntityType<HTExpCollectorBlockEntity> = registerTick(
        "exp_collector",
        ::HTExpCollectorBlockEntity,
    )

    @JvmField
    val ITEM_BUFFER: HTDeferredBlockEntityType<HTItemBufferBlockEntity> = registerTick("item_buffer", ::HTItemBufferBlockEntity)

    @JvmField
    val LAVA_COLLECTOR: HTDeferredBlockEntityType<HTLavaCollectorBlockEntity> = registerTick(
        "lava_collector",
        ::HTLavaCollectorBlockEntity,
    )

    @JvmField
    val MILK_DRAIN: HTDeferredBlockEntityType<HTMilkDrainBlockEntity> = registerTick(
        "milk_drain",
        ::HTMilkDrainBlockEntity,
    )

    @JvmField
    val SPRINKLER: HTDeferredBlockEntityType<HTSprinklerBlockEntity> =
        registerTick("sprinkler", ::HTSprinklerBlockEntity)

    @JvmField
    val WATER_COLLECTOR: HTDeferredBlockEntityType<HTWaterCollectorBlockEntity> = registerTick(
        "water_collector",
        ::HTWaterCollectorBlockEntity,
    )

    @JvmField
    val DEVICES: List<HTDeferredBlockEntityType<out HTDeviceBlockEntity>> = listOf(
        ENI,
        EXP_COLLECTOR,
        ITEM_BUFFER,
        LAVA_COLLECTOR,
        MILK_DRAIN,
        SPRINKLER,
        WATER_COLLECTOR,
    )

    //    Storage    //

    //    Event    //

    @JvmStatic
    private fun addSupportedBlock(event: BlockEntityTypeAddBlocksEvent) {
        fun add(type: HTDeferredBlockEntityType<*>, block: Supplier<out Block>) {
            event.modify(type.get(), block.get())
        }

        fun <V : HTVariantKey.WithBE<*>> addAll(map: Map<V, DeferredBlock<*>>) {
            for ((variant: V, block: DeferredBlock<*>) in map) {
                event.modify(variant.blockEntityHolder.get(), block.get())
            }
        }

        addAll(RagiumBlocks.GENERATORS)

        add(BLOCK_BREAKER, RagiumBlocks.Machines.BLOCK_BREAKER)
        add(COMPRESSOR, RagiumBlocks.Machines.COMPRESSOR)
        add(ENGRAVER, RagiumBlocks.Machines.ENGRAVER)
        add(EXTRACTOR, RagiumBlocks.Machines.EXTRACTOR)
        add(PULVERIZER, RagiumBlocks.Machines.PULVERIZER)

        add(ALLOY_SMELTER, RagiumBlocks.Machines.ALLOY_SMELTER)
        add(CRUSHER, RagiumBlocks.Machines.CRUSHER)
        add(INFUSER, RagiumBlocks.Machines.INFUSER)
        add(MELTER, RagiumBlocks.Machines.MELTER)
        add(MIXER, RagiumBlocks.Machines.MIXER)
        add(REFINERY, RagiumBlocks.Machines.REFINERY)
        add(SOLIDIFIER, RagiumBlocks.Machines.SOLIDIFIER)

        add(DIM_ANCHOR, RagiumBlocks.Devices.DIM_ANCHOR)
        add(ENI, RagiumBlocks.Devices.ENI)
        add(EXP_COLLECTOR, RagiumBlocks.Devices.EXP_COLLECTOR)
        add(ITEM_BUFFER, RagiumBlocks.Devices.ITEM_BUFFER)
        add(LAVA_COLLECTOR, RagiumBlocks.Devices.LAVA_COLLECTOR)
        add(MILK_DRAIN, RagiumBlocks.Devices.MILK_DRAIN)
        add(SPRINKLER, RagiumBlocks.Devices.SPRINKLER)
        add(WATER_COLLECTOR, RagiumBlocks.Devices.WATER_COLLECTOR)

        add(CEU, RagiumBlocks.Devices.CEU)

        addAll(RagiumBlocks.DRUMS)

        LOGGER.info("Added supported blocks to BlockEntityType!")
    }

    @JvmStatic
    private fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        registerHandlers<HTGeneratorBlockEntity, HTGeneratorVariant>(event)

        for (type: HTDeferredBlockEntityType<out HTMachineBlockEntity> in BASIC_MACHINES) {
            registerHandlers(event, type)
        }

        for (type: HTDeferredBlockEntityType<out HTMachineBlockEntity> in ADVANCED_MACHINES) {
            registerHandlers(event, type)
        }

        for (type: HTDeferredBlockEntityType<out HTDeviceBlockEntity> in DEVICES) {
            registerHandlers(event, type)
        }

        registerHandlers(event, CEU)

        registerHandlers<HTDrumBlockEntity, HTDrumVariant>(event)

        LOGGER.info("Registered Block Capabilities!")
    }

    @JvmStatic
    private fun <BE> registerHandlers(
        event: RegisterCapabilitiesEvent,
        holder: HTDeferredBlockEntityType<BE>,
    ) where BE : BlockEntity, BE : HTHandlerBlockEntity {
        val type: BlockEntityType<BE> = holder.get()
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            type,
            HTHandlerBlockEntity::getItemHandler,
        )
        event.registerBlockEntity(
            Capabilities.FluidHandler.BLOCK,
            type,
            HTHandlerBlockEntity::getFluidHandler,
        )
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK,
            type,
            HTHandlerBlockEntity::getEnergyStorage,
        )
    }

    @JvmStatic
    private inline fun <BE, reified V> registerHandlers(
        event: RegisterCapabilitiesEvent,
    ) where BE : BlockEntity, BE : HTHandlerBlockEntity, V : HTVariantKey.WithBE<BE>, V : Enum<V> {
        for (variant: V in enumEntries<V>()) {
            registerHandlers(event, variant.blockEntityHolder)
        }
    }
}
