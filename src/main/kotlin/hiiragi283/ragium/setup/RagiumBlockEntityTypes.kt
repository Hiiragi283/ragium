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
import hiiragi283.ragium.util.variant.HTDrumVariant
import hiiragi283.ragium.util.variant.HTGeneratorVariant
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent
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

        HTGeneratorVariant.entries
        HTMachineVariant.entries
        HTDrumVariant.entries

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

    // Supported Blocks
    @JvmStatic
    private fun addSupportedBlock(event: BlockEntityTypeAddBlocksEvent) {
        addAll<HTGeneratorVariant>(event)
        addAll<HTMachineVariant>(event)

        add(event, DIM_ANCHOR, RagiumBlocks.Devices.DIM_ANCHOR)
        add(event, ENI, RagiumBlocks.Devices.ENI)
        add(event, EXP_COLLECTOR, RagiumBlocks.Devices.EXP_COLLECTOR)
        add(event, ITEM_BUFFER, RagiumBlocks.Devices.ITEM_BUFFER)
        add(event, LAVA_COLLECTOR, RagiumBlocks.Devices.LAVA_COLLECTOR)
        add(event, MILK_DRAIN, RagiumBlocks.Devices.MILK_DRAIN)
        add(event, SPRINKLER, RagiumBlocks.Devices.SPRINKLER)
        add(event, WATER_COLLECTOR, RagiumBlocks.Devices.WATER_COLLECTOR)

        add(event, CEU, RagiumBlocks.Devices.CEU)

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
        registerHandlers<HTGeneratorBlockEntity, HTGeneratorVariant>(event)
        registerHandlers<HTMachineBlockEntity, HTMachineVariant>(event)

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
