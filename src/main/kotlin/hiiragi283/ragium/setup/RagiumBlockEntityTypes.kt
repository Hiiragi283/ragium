package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityTypeRegister
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBlockBreakerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCompressorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTEngraverBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPulverizerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSimulatorBlockEntity
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
        REGISTER.addAlias(RagiumAPI.id("item_collector"), RagiumAPI.id("item_buffer"))

        HTGeneratorVariant.entries
        HTMachineVariant.entries
        HTDrumVariant.entries

        REGISTER.register(eventBus)

        eventBus.addListener(::addSupportedBlock)
        eventBus.addListener(::registerBlockCapabilities)
    }

    @JvmStatic
    fun <BE : HTBlockEntity> registerTick(name: String, factory: (BlockPos, BlockState) -> BE): HTDeferredBlockEntityType<BE> =
        REGISTER.registerType(name, factory, HTBlockEntity::tickClient, HTBlockEntity::tickServer)

    @JvmField
    val MACHINES: Map<HTMachineVariant, HTDeferredBlockEntityType<HTBlockEntity>> = HTMachineVariant.entries.associateWith { variant ->
        val factory = when (variant) {
            // Basic
            HTMachineVariant.ALLOY_SMELTER -> ::HTAlloySmelterBlockEntity
            HTMachineVariant.BLOCK_BREAKER -> ::HTBlockBreakerBlockEntity
            HTMachineVariant.COMPRESSOR -> ::HTCompressorBlockEntity
            HTMachineVariant.ENGRAVER -> ::HTEngraverBlockEntity
            HTMachineVariant.EXTRACTOR -> ::HTExtractorBlockEntity
            HTMachineVariant.PULVERIZER -> ::HTPulverizerBlockEntity
            // Advanced
            HTMachineVariant.CRUSHER -> ::HTCrusherBlockEntity
            HTMachineVariant.MELTER -> ::HTMelterBlockEntity
            HTMachineVariant.REFINERY -> ::HTRefineryBlockEntity
            // Elite
            HTMachineVariant.MULTI_SMELTER -> ::HTMultiSmelterBlockEntity
            HTMachineVariant.SIMULATOR -> ::HTSimulatorBlockEntity
        }
        registerTick(variant.serializedName, factory)
    }

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
        registerHandlers<HTGeneratorVariant>(event)
        registerHandlers<HTMachineVariant>(event)
        registerHandlers<HTDeviceVariant>(event)
        registerHandlers<HTDrumVariant>(event)

        LOGGER.info("Registered Block Capabilities!")
    }

    @JvmStatic
    private inline fun <reified V> registerHandlers(
        event: RegisterCapabilitiesEvent,
    ) where V : HTVariantKey.WithBE<HTBlockEntity>, V : Enum<V> {
        for (variant: V in enumEntries<V>()) {
            val type: BlockEntityType<out HTBlockEntity> = variant.blockEntityHolder.get()
            event.registerBlockEntity(
                HTMultiCapability.ITEM.blockCapability,
                type,
                HTHandlerBlockEntity::getItemHandler,
            )
            event.registerBlockEntity(
                HTMultiCapability.FLUID.blockCapability,
                type,
                HTHandlerBlockEntity::getFluidHandler,
            )
            event.registerBlockEntity(
                HTMultiCapability.ENERGY.blockCapability,
                type,
                HTHandlerBlockEntity::getEnergyStorage,
            )
        }
    }
}
