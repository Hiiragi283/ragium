package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.core.api.capability.HTEnergyCapabilities
import hiiragi283.core.api.capability.HTFluidCapabilities
import hiiragi283.core.api.capability.HTItemCapabilities
import hiiragi283.core.api.storage.HTHandlerProvider
import hiiragi283.core.common.block.HTBlockWithEntity
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.registry.HTDeferredOnlyBlock
import hiiragi283.core.common.registry.register.HTDeferredBlockEntityTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.block.entity.HTImitationSpawnerBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTCuttingMachineBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTDryerBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTMixerBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTPlanterBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTPyrolyzerBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTBatteryBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTResonantInterfaceBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTUniversalChestBlockEntity
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent
import org.slf4j.Logger

object RagiumBlockEntityTypes {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val REGISTER = HTDeferredBlockEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        eventBus.addListener(::addSupportedBlocks)
        eventBus.addListener(::registerBlockCapabilities)

        REGISTER.register(eventBus)
    }

    @JvmField
    val IMITATION_SPAWNER: HTDeferredBlockEntityType<HTImitationSpawnerBlockEntity> =
        REGISTER.registerType("imitation_spawner", ::HTImitationSpawnerBlockEntity)

    //    Machine    //

    @JvmField
    val ALLOY_SMELTER: HTDeferredBlockEntityType<HTAlloySmelterBlockEntity> =
        REGISTER.registerTick(RagiumConst.ALLOY_SMELTER, ::HTAlloySmelterBlockEntity)

    @JvmField
    val CRUSHER: HTDeferredBlockEntityType<HTCrusherBlockEntity> =
        REGISTER.registerTick(RagiumConst.CRUSHER, ::HTCrusherBlockEntity)

    @JvmField
    val CUTTING_MACHINE: HTDeferredBlockEntityType<HTCuttingMachineBlockEntity> =
        REGISTER.registerTick(RagiumConst.CUTTING_MACHINE, ::HTCuttingMachineBlockEntity)

    @JvmField
    val DRYER: HTDeferredBlockEntityType<HTDryerBlockEntity> =
        REGISTER.registerTick(RagiumConst.DRYER, ::HTDryerBlockEntity)

    @JvmField
    val MELTER: HTDeferredBlockEntityType<HTMelterBlockEntity> =
        REGISTER.registerTick(RagiumConst.MELTER, ::HTMelterBlockEntity)

    @JvmField
    val MIXER: HTDeferredBlockEntityType<HTMixerBlockEntity> =
        REGISTER.registerTick(RagiumConst.MIXER, ::HTMixerBlockEntity)

    @JvmField
    val PLANTER: HTDeferredBlockEntityType<HTPlanterBlockEntity> =
        REGISTER.registerTick(RagiumConst.PLANTER, ::HTPlanterBlockEntity)

    @JvmField
    val PYROLYZER: HTDeferredBlockEntityType<HTPyrolyzerBlockEntity> =
        REGISTER.registerTick(RagiumConst.PYROLYZER, ::HTPyrolyzerBlockEntity)

    //    Storage    //

    @JvmField
    val BATTERY: HTDeferredBlockEntityType<HTBatteryBlockEntity> =
        REGISTER.registerTick("battery", ::HTBatteryBlockEntity)

    @JvmField
    val CRATE: HTDeferredBlockEntityType<HTCrateBlockEntity> =
        REGISTER.registerTick("crate", ::HTCrateBlockEntity)

    @JvmField
    val TANK: HTDeferredBlockEntityType<HTTankBlockEntity> =
        REGISTER.registerTick("tank", ::HTTankBlockEntity)

    @JvmField
    val RESONANT_INTERFACE: HTDeferredBlockEntityType<HTResonantInterfaceBlockEntity> =
        REGISTER.registerType("resonant_interface", ::HTResonantInterfaceBlockEntity)

    @JvmField
    val UNIVERSAL_CHEST: HTDeferredBlockEntityType<HTUniversalChestBlockEntity> =
        REGISTER.registerType(RagiumConst.UNIVERSAL_CHEST, ::HTUniversalChestBlockEntity)

    //    Event    //

    // Supported Blocks
    @JvmStatic
    private fun addSupportedBlocks(event: BlockEntityTypeAddBlocksEvent) {
        for (holder: HTDeferredOnlyBlock<*> in RagiumBlocks.REGISTER.asBlockSequence()) {
            val block: Block = holder.get()
            if (block is HTBlockWithEntity) {
                event.modify(block.getBlockEntityType().get(), block)
            }
        }
        LOGGER.info("Added supported blocks to BlockEntityType!")
    }

    // Capabilities
    @JvmStatic
    private fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        // Machine
        registerHandler(event, ALLOY_SMELTER.get())
        registerHandler(event, CRUSHER.get())
        registerHandler(event, CUTTING_MACHINE.get())
        registerHandler(event, DRYER.get())
        registerHandler(event, MELTER.get())
        registerHandler(event, MIXER.get())
        registerHandler(event, PYROLYZER.get())

        // Device
        registerHandler(event, PLANTER.get())

        // Storage
        registerHandler(event, BATTERY.get())
        registerHandler(event, CRATE.get())
        registerHandler(event, TANK.get())
        registerHandler(event, RESONANT_INTERFACE.get())
        registerHandler(event, UNIVERSAL_CHEST.get())

        LOGGER.info("Registered Block Capabilities!")
    }

    @JvmStatic
    private fun <BE> registerHandler(
        event: RegisterCapabilitiesEvent,
        type: BlockEntityType<BE>,
    ) where BE : BlockEntity, BE : HTHandlerProvider {
        event.registerBlockEntity(HTItemCapabilities.block, type, HTHandlerProvider::getItemHandler)
        event.registerBlockEntity(HTFluidCapabilities.block, type, HTHandlerProvider::getFluidHandler)
        event.registerBlockEntity(HTEnergyCapabilities.block, type, HTHandlerProvider::getEnergyStorage)
    }
}
