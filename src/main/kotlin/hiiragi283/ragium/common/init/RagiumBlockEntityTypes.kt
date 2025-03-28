package hiiragi283.ragium.common.init

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.api.registry.HTBlockEntityTypeRegister
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.HTEnergyNetworkInterfaceBlockEntity
import hiiragi283.ragium.common.block.entity.HTSprinklerBlockEntity
import hiiragi283.ragium.common.block.entity.collect.HTItemCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.collect.HTLavaCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.collect.HTMilkDrainBlockEntity
import hiiragi283.ragium.common.block.entity.collect.HTWaterCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCentrifugeBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTInfuserBlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent
import net.neoforged.neoforge.registries.DeferredBlock
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object RagiumBlockEntityTypes {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val REGISTER = HTBlockEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun <T : HTTickAwareBlockEntity> registerTick(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
    ): HTDeferredBlockEntityType<T> =
        REGISTER.registerType(name, factory, HTTickAwareBlockEntity::clientTick, HTTickAwareBlockEntity::serverTick)

    //    Machine    //

    @JvmField
    val CRUSHER: HTDeferredBlockEntityType<HTCrusherBlockEntity> = registerTick("crusher", ::HTCrusherBlockEntity)

    @JvmField
    val EXTRACTOR: HTDeferredBlockEntityType<HTExtractorBlockEntity> = registerTick(
        "extractor",
        ::HTExtractorBlockEntity,
    )

    @JvmField
    val CENTRIFUGE: HTDeferredBlockEntityType<HTCentrifugeBlockEntity> = registerTick(
        "centrifuge",
        ::HTCentrifugeBlockEntity,
    )

    @JvmField
    val INFUSER: HTDeferredBlockEntityType<HTInfuserBlockEntity> = registerTick("infuser", ::HTInfuserBlockEntity)

    //    Device    //

    @JvmField
    val MILK_DRAIN: HTDeferredBlockEntityType<HTMilkDrainBlockEntity> = registerTick(
        "milk_drain",
        ::HTMilkDrainBlockEntity,
    )

    @JvmField
    val ITEM_COLLECTOR: HTDeferredBlockEntityType<HTItemCollectorBlockEntity> = registerTick(
        "item_collector",
        ::HTItemCollectorBlockEntity,
    )

    @JvmField
    val WATER_COLLECTOR: HTDeferredBlockEntityType<HTWaterCollectorBlockEntity> = registerTick(
        "water_collector",
        ::HTWaterCollectorBlockEntity,
    )

    @JvmField
    val SPRINKLER: HTDeferredBlockEntityType<HTSprinklerBlockEntity> = registerTick("sprinkler", ::HTSprinklerBlockEntity)

    @JvmField
    val LAVA_COLLECTOR: HTDeferredBlockEntityType<HTLavaCollectorBlockEntity> = registerTick(
        "lava_collector",
        ::HTLavaCollectorBlockEntity,
    )

    @JvmField
    val ENI: HTDeferredBlockEntityType<HTEnergyNetworkInterfaceBlockEntity> = REGISTER.registerType(
        "energy_network_interface",
        ::HTEnergyNetworkInterfaceBlockEntity,
    )

    //    Event    //

    @SubscribeEvent
    fun addSupportedBlock(event: BlockEntityTypeAddBlocksEvent) {
        fun add(type: HTDeferredBlockEntityType<*>, block: DeferredBlock<*>) {
            event.modify(type.get(), block.get())
        }

        add(CRUSHER, RagiumBlocks.CRUSHER)
        add(EXTRACTOR, RagiumBlocks.EXTRACTOR)

        add(CENTRIFUGE, RagiumBlocks.CENTRIFUGE)
        add(INFUSER, RagiumBlocks.INFUSER)

        add(MILK_DRAIN, RagiumBlocks.MILK_DRAIN)

        add(ITEM_COLLECTOR, RagiumBlocks.ITEM_COLLECTOR)
        add(WATER_COLLECTOR, RagiumBlocks.WATER_COLLECTOR)
        add(SPRINKLER, RagiumBlocks.SPRINKLER)

        add(LAVA_COLLECTOR, RagiumBlocks.LAVA_COLLECTOR)
        add(ENI, RagiumBlocks.ENI)

        LOGGER.info("Added supported blocks to BlockEntityType!")
    }

    @SubscribeEvent
    fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        fun registerHandlers(holder: HTDeferredBlockEntityType<out HTBlockEntity>) {
            val type: BlockEntityType<out HTBlockEntity> = holder.get()
            event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                type,
                HTBlockEntity::getItemHandler,
            )
            event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                type,
                HTBlockEntity::getFluidHandler,
            )
            event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                type,
                HTBlockEntity::getEnergyStorage,
            )
        }

        registerHandlers(CRUSHER)
        registerHandlers(EXTRACTOR)

        registerHandlers(CENTRIFUGE)
        registerHandlers(INFUSER)

        registerHandlers(MILK_DRAIN)

        registerHandlers(ITEM_COLLECTOR)
        registerHandlers(WATER_COLLECTOR)
        registerHandlers(SPRINKLER)

        registerHandlers(LAVA_COLLECTOR)
        registerHandlers(ENI)

        LOGGER.info("Registered Block Capabilities!")
    }
}
