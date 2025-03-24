package hiiragi283.ragium.common.init

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.api.registry.HTBlockEntityTypeRegister
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.*
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
    val EXTRACTOR: HTDeferredBlockEntityType<HTExtractorBlockEntity> = registerTick("extractor", ::HTExtractorBlockEntity)

    //    Device    //

    @JvmField
    val WATER_WELL: HTDeferredBlockEntityType<HTWaterWellBlockEntity> = registerTick("water_well", ::HTWaterWellBlockEntity)

    @JvmField
    val LAVA_WELL: HTDeferredBlockEntityType<HTLavaWellBlockEntity> = registerTick("lava_well", ::HTLavaWellBlockEntity)

    @JvmField
    val MILK_DRAIN: HTDeferredBlockEntityType<HTMilkDrainBlockEntity> = registerTick("milk_drain", ::HTMilkDrainBlockEntity)

    @JvmField
    val ENI: HTDeferredBlockEntityType<HTEnergyNetworkInterfaceBlockEntity> =
        REGISTER.registerType("energy_network_interface", ::HTEnergyNetworkInterfaceBlockEntity)

    @JvmField
    val SPRINKLER: HTDeferredBlockEntityType<HTSprinklerBlockEntity> = registerTick("sprinkler", ::HTSprinklerBlockEntity)

    //    Event    //

    @SubscribeEvent
    fun addSupportedBlock(event: BlockEntityTypeAddBlocksEvent) {
        fun add(type: HTDeferredBlockEntityType<*>, block: DeferredBlock<*>) {
            event.modify(type.get(), block.get())
        }

        add(CRUSHER, RagiumBlocks.CRUSHER)
        add(EXTRACTOR, RagiumBlocks.EXTRACTOR)

        add(WATER_WELL, RagiumBlocks.WATER_WELL)
        add(LAVA_WELL, RagiumBlocks.LAVA_WELL)
        add(MILK_DRAIN, RagiumBlocks.MILK_DRAIN)

        add(ENI, RagiumBlocks.ENI)
        add(SPRINKLER, RagiumBlocks.SPRINKLER)

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

        registerHandlers(WATER_WELL)
        registerHandlers(LAVA_WELL)
        registerHandlers(MILK_DRAIN)

        registerHandlers(ENI)
        registerHandlers(SPRINKLER)

        LOGGER.info("Registered Block Capabilities!")
    }
}
