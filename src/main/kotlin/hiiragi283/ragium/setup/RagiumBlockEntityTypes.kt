package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.api.registry.HTBlockEntityTypeRegister
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.HTEnergyNetworkInterfaceBlockEntity
import hiiragi283.ragium.common.block.entity.HTSprinklerBlockEntity
import hiiragi283.ragium.common.block.entity.collect.*
import hiiragi283.ragium.common.block.entity.machine.HTAdvancedExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTInfuserBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTRefineryBlockEntity
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
    val ADVANCED_EXTRACTOR: HTDeferredBlockEntityType<HTAdvancedExtractorBlockEntity> = registerTick(
        "advanced_extractor",
        ::HTAdvancedExtractorBlockEntity,
    )

    @JvmField
    val CRUSHER: HTDeferredBlockEntityType<HTCrusherBlockEntity> = registerTick("crusher", ::HTCrusherBlockEntity)

    @JvmField
    val EXTRACTOR: HTDeferredBlockEntityType<HTExtractorBlockEntity> = registerTick(
        "extractor",
        ::HTExtractorBlockEntity,
    )

    @JvmField
    val INFUSER: HTDeferredBlockEntityType<HTInfuserBlockEntity> = registerTick("infuser", ::HTInfuserBlockEntity)

    @JvmField
    val REFINERY: HTDeferredBlockEntityType<HTRefineryBlockEntity> = registerTick("refinery", ::HTRefineryBlockEntity)

    //    Device    //

    @JvmField
    val ENI: HTDeferredBlockEntityType<HTEnergyNetworkInterfaceBlockEntity> = REGISTER.registerType(
        "energy_network_interface",
        ::HTEnergyNetworkInterfaceBlockEntity,
    )

    @JvmField
    val EXP_COLLECTOR: HTDeferredBlockEntityType<HTExpCollectorBlockEntity> = registerTick(
        "exp_collector",
        ::HTExpCollectorBlockEntity,
    )

    @JvmField
    val ITEM_COLLECTOR: HTDeferredBlockEntityType<HTItemCollectorBlockEntity> = registerTick(
        "item_collector",
        ::HTItemCollectorBlockEntity,
    )

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

    //    Event    //

    @SubscribeEvent
    fun addSupportedBlock(event: BlockEntityTypeAddBlocksEvent) {
        fun add(type: HTDeferredBlockEntityType<*>, block: DeferredBlock<*>) {
            event.modify(type.get(), block.get())
        }

        add(CRUSHER, RagiumBlocks.CRUSHER)
        add(EXTRACTOR, RagiumBlocks.EXTRACTOR)

        add(ADVANCED_EXTRACTOR, RagiumBlocks.ADVANCED_EXTRACTOR)
        add(INFUSER, RagiumBlocks.INFUSER)
        add(REFINERY, RagiumBlocks.REFINERY)

        add(ENI, RagiumBlocks.ENI)
        add(EXP_COLLECTOR, RagiumBlocks.EXP_COLLECTOR)
        add(ITEM_COLLECTOR, RagiumBlocks.ITEM_COLLECTOR)
        add(LAVA_COLLECTOR, RagiumBlocks.LAVA_COLLECTOR)
        add(MILK_DRAIN, RagiumBlocks.MILK_DRAIN)
        add(SPRINKLER, RagiumBlocks.SPRINKLER)
        add(WATER_COLLECTOR, RagiumBlocks.WATER_COLLECTOR)

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

        registerHandlers(ADVANCED_EXTRACTOR)
        registerHandlers(INFUSER)
        registerHandlers(REFINERY)

        registerHandlers(ENI)
        registerHandlers(EXP_COLLECTOR)
        registerHandlers(ITEM_COLLECTOR)
        registerHandlers(LAVA_COLLECTOR)
        registerHandlers(MILK_DRAIN)
        registerHandlers(SPRINKLER)
        registerHandlers(WATER_COLLECTOR)

        LOGGER.info("Registered Block Capabilities!")
    }
}
