package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.api.registry.HTBlockEntityTypeRegister
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.HTChargerBlockEntity
import hiiragi283.ragium.common.block.entity.HTCreativeEnergyUnitBlockEntity
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import hiiragi283.ragium.common.block.entity.HTEnergyNetworkInterfaceBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTExpCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTItemCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTLavaCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTMilkDrainBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTSprinklerBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTWaterCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBlockBreakerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSolidifierBlockEntity
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
    val ALLOY_SMELTER: HTDeferredBlockEntityType<HTAlloySmelterBlockEntity> = registerTick("alloy_smelter", ::HTAlloySmelterBlockEntity)

    @JvmField
    val BLOCK_BREAKER: HTDeferredBlockEntityType<HTBlockBreakerBlockEntity> = registerTick("block_breaker", ::HTBlockBreakerBlockEntity)

    @JvmField
    val CRUSHER: HTDeferredBlockEntityType<HTCrusherBlockEntity> = registerTick("crusher", ::HTCrusherBlockEntity)

    @JvmField
    val EXTRACTOR: HTDeferredBlockEntityType<HTExtractorBlockEntity> = registerTick("extractor", ::HTExtractorBlockEntity)

    @JvmField
    val MELTER: HTDeferredBlockEntityType<HTMelterBlockEntity> = registerTick("melter", ::HTMelterBlockEntity)

    @JvmField
    val REFINERY: HTDeferredBlockEntityType<HTRefineryBlockEntity> = registerTick("refinery", ::HTRefineryBlockEntity)

    @JvmField
    val SOLIDIFIER: HTDeferredBlockEntityType<HTSolidifierBlockEntity> = registerTick("solidifier", ::HTSolidifierBlockEntity)

    //    Device    //

    @JvmField
    val CEU: HTDeferredBlockEntityType<HTCreativeEnergyUnitBlockEntity> = REGISTER.registerType(
        "creative_energy_unit",
        ::HTCreativeEnergyUnitBlockEntity,
    )

    @JvmField
    val CHARGER: HTDeferredBlockEntityType<HTChargerBlockEntity> = registerTick("charger", ::HTChargerBlockEntity)

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

    //    Storage    //

    @JvmField
    val SMALL_DRUM: HTDeferredBlockEntityType<HTDrumBlockEntity> =
        REGISTER.registerType("small_drum", HTDrumBlockEntity::Small)

    @JvmField
    val MEDIUM_DRUM: HTDeferredBlockEntityType<HTDrumBlockEntity> =
        REGISTER.registerType("medium_drum", HTDrumBlockEntity::Medium)

    @JvmField
    val LARGE_DRUM: HTDeferredBlockEntityType<HTDrumBlockEntity> =
        REGISTER.registerType("large_drum", HTDrumBlockEntity::Large)

    @JvmField
    val HUGE_DRUM: HTDeferredBlockEntityType<HTDrumBlockEntity> =
        REGISTER.registerType("huge_drum", HTDrumBlockEntity::Huge)

    //    Event    //

    @SubscribeEvent
    fun addSupportedBlock(event: BlockEntityTypeAddBlocksEvent) {
        fun add(type: HTDeferredBlockEntityType<*>, block: DeferredBlock<*>) {
            event.modify(type.get(), block.get())
        }

        add(CRUSHER, RagiumBlocks.CRUSHER)
        add(BLOCK_BREAKER, RagiumBlocks.BLOCK_BREAKER)
        add(EXTRACTOR, RagiumBlocks.EXTRACTOR)

        add(ALLOY_SMELTER, RagiumBlocks.ALLOY_SMELTER)
        add(MELTER, RagiumBlocks.MELTER)
        add(REFINERY, RagiumBlocks.REFINERY)
        add(SOLIDIFIER, RagiumBlocks.SOLIDIFIER)

        add(CHARGER, RagiumBlocks.CHARGER)
        add(ENI, RagiumBlocks.ENI)
        add(EXP_COLLECTOR, RagiumBlocks.EXP_COLLECTOR)
        add(ITEM_COLLECTOR, RagiumBlocks.ITEM_COLLECTOR)
        add(LAVA_COLLECTOR, RagiumBlocks.LAVA_COLLECTOR)
        add(MILK_DRAIN, RagiumBlocks.MILK_DRAIN)
        add(SPRINKLER, RagiumBlocks.SPRINKLER)
        add(WATER_COLLECTOR, RagiumBlocks.WATER_COLLECTOR)

        add(CEU, RagiumBlocks.CEU)

        add(SMALL_DRUM, RagiumBlocks.SMALL_DRUM)
        add(MEDIUM_DRUM, RagiumBlocks.MEDIUM_DRUM)
        add(LARGE_DRUM, RagiumBlocks.LARGE_DRUM)
        add(HUGE_DRUM, RagiumBlocks.HUGE_DRUM)

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
        registerHandlers(BLOCK_BREAKER)
        registerHandlers(EXTRACTOR)

        registerHandlers(ALLOY_SMELTER)
        registerHandlers(MELTER)
        registerHandlers(REFINERY)
        registerHandlers(SOLIDIFIER)

        registerHandlers(CHARGER)
        registerHandlers(ENI)
        registerHandlers(EXP_COLLECTOR)
        registerHandlers(ITEM_COLLECTOR)
        registerHandlers(LAVA_COLLECTOR)
        registerHandlers(MILK_DRAIN)
        registerHandlers(SPRINKLER)
        registerHandlers(WATER_COLLECTOR)

        registerHandlers(CEU)

        registerHandlers(SMALL_DRUM)
        registerHandlers(MEDIUM_DRUM)
        registerHandlers(LARGE_DRUM)
        registerHandlers(HUGE_DRUM)

        LOGGER.info("Registered Block Capabilities!")
    }
}
