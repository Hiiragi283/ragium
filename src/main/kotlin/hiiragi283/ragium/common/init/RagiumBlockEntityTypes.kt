package hiiragi283.ragium.common.init

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.registry.HTBlockEntityTypeRegister
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.HTEnergyNetworkInterfaceBlockEntity
import hiiragi283.ragium.common.block.entity.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.HTSingleItemRecipeBlockEntity
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

    //    Machine    //

    @JvmField
    val CRUSHER: HTDeferredBlockEntityType<HTCrusherBlockEntity> =
        REGISTER.registerType(
            "crusher",
            ::HTCrusherBlockEntity,
            HTSingleItemRecipeBlockEntity::clientTick,
            HTSingleItemRecipeBlockEntity::serverTick,
        )

    @JvmField
    val EXTRACTOR: HTDeferredBlockEntityType<HTExtractorBlockEntity> =
        REGISTER.registerType(
            "extractor",
            ::HTExtractorBlockEntity,
            HTSingleItemRecipeBlockEntity::clientTick,
            HTSingleItemRecipeBlockEntity::serverTick,
        )

    //    Device    //

    @JvmField
    val ENI: HTDeferredBlockEntityType<HTEnergyNetworkInterfaceBlockEntity> =
        REGISTER.registerType("energy_network_interface", ::HTEnergyNetworkInterfaceBlockEntity)

    //    Event    //

    @SubscribeEvent
    fun addSupportedBlock(event: BlockEntityTypeAddBlocksEvent) {
        fun add(type: HTDeferredBlockEntityType<*>, block: DeferredBlock<*>) {
            event.modify(type.get(), block.get())
        }

        add(CRUSHER, RagiumBlocks.CRUSHER)
        add(EXTRACTOR, RagiumBlocks.EXTRACTOR)

        add(ENI, RagiumBlocks.ENI)

        LOGGER.info("Added supported blocks to BlockEntityType!")
    }

    @SubscribeEvent
    fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        fun <T : HTBlockEntity> registerHandlers(holder: HTDeferredBlockEntityType<T>) {
            val type: BlockEntityType<T> = holder.get()
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

        registerHandlers(ENI)

        LOGGER.info("Registered Block Capabilities!")
    }
}
