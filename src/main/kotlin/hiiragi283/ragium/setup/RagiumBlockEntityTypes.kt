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
import hiiragi283.ragium.common.block.entity.dynamo.HTGeneratorBlockEntity
import hiiragi283.ragium.util.variant.HTDeviceVariant
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

    //    Storage    //

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
        registerHandlers<HTGeneratorBlockEntity, HTGeneratorVariant>(event)
        registerHandlers<HTMachineBlockEntity, HTMachineVariant>(event)
        registerHandlers<HTDeviceBlockEntity, HTDeviceVariant>(event)
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
