package hiiragi283.ragium.setup

import hiiragi283.core.common.block.HTBlockWithEntity
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.capability.HTEnergyCapabilities
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.capability.HTItemCapabilities
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.registry.HTDeferredOnlyBlock
import hiiragi283.core.common.registry.register.HTDeferredBlockEntityTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.block.entity.HTImitationSpawnerBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTFermenterBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTPlanterBlockEntity
import hiiragi283.ragium.common.block.entity.enchant.HTEnchanterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCuttingMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTDryerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTFormingPressBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMixerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPyrolyzerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSolidifierBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTBatteryBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCreativeBatteryBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCreativeCrateBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCreativeTankBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTResonantInterfaceBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTUniversalChestBlockEntity
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent

object RagiumBlockEntityTypes {
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

    // Basic
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
    val FORMING_PRESS: HTDeferredBlockEntityType<HTFormingPressBlockEntity> =
        REGISTER.registerTick(RagiumConst.FORMING_PRESS, ::HTFormingPressBlockEntity)

    // Advanced
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
    val PYROLYZER: HTDeferredBlockEntityType<HTPyrolyzerBlockEntity> =
        REGISTER.registerTick(RagiumConst.PYROLYZER, ::HTPyrolyzerBlockEntity)

    @JvmField
    val SOLIDIFIER: HTDeferredBlockEntityType<HTSolidifierBlockEntity> =
        REGISTER.registerTick(RagiumConst.SOLIDIFIER, ::HTSolidifierBlockEntity)

    //    Device    //

    // Basic
    @JvmField
    val FERMENTER: HTDeferredBlockEntityType<HTFermenterBlockEntity> =
        REGISTER.registerTick(RagiumConst.FERMENTER, ::HTFermenterBlockEntity)

    @JvmField
    val PLANTER: HTDeferredBlockEntityType<HTPlanterBlockEntity> =
        REGISTER.registerTick(RagiumConst.PLANTER, ::HTPlanterBlockEntity)

    // Enchanting
    @JvmField
    val ENCHANTER: HTDeferredBlockEntityType<HTEnchanterBlockEntity> =
        REGISTER.registerTick(RagiumConst.ENCHANTER, ::HTEnchanterBlockEntity)

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

    @JvmField
    val CREATIVE_BATTERY: HTDeferredBlockEntityType<HTCreativeBatteryBlockEntity> =
        REGISTER.registerTick("creative_battery", ::HTCreativeBatteryBlockEntity)

    @JvmField
    val CREATIVE_CRATE: HTDeferredBlockEntityType<HTCreativeCrateBlockEntity> =
        REGISTER.registerTick("creative_crate", ::HTCreativeCrateBlockEntity)

    @JvmField
    val CREATIVE_TANK: HTDeferredBlockEntityType<HTCreativeTankBlockEntity> =
        REGISTER.registerTick("creative_tank", ::HTCreativeTankBlockEntity)

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
    }

    // Capabilities
    @JvmStatic
    private fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        // Machine
        registerHandler(event, ALLOY_SMELTER.get())
        registerHandler(event, CRUSHER.get())
        registerHandler(event, CUTTING_MACHINE.get())
        registerHandler(event, FORMING_PRESS.get())

        registerHandler(event, DRYER.get())
        registerHandler(event, MELTER.get())
        registerHandler(event, MIXER.get())
        registerHandler(event, PYROLYZER.get())
        registerHandler(event, SOLIDIFIER.get())

        // Device
        registerHandler(event, FERMENTER.get())
        registerHandler(event, PLANTER.get())

        registerHandler(event, ENCHANTER.get())

        // Storage
        registerHandler(event, BATTERY.get())
        registerHandler(event, CRATE.get())
        registerHandler(event, TANK.get())

        registerHandler(event, CREATIVE_BATTERY.get())
        registerHandler(event, CREATIVE_CRATE.get())
        registerHandler(event, CREATIVE_TANK.get())

        HTItemCapabilities.registerBlockEntity(event, UNIVERSAL_CHEST.get(), HTUniversalChestBlockEntity::getItemHandler)

        HTItemCapabilities.registerBlockEntity(event, RESONANT_INTERFACE.get(), HTResonantInterfaceBlockEntity::getItemHandler)
        HTItemCapabilities.registerBlockEntity(event, RESONANT_INTERFACE.get(), HTResonantInterfaceBlockEntity::getItemHandler)
        HTItemCapabilities.registerBlockEntity(event, RESONANT_INTERFACE.get(), HTResonantInterfaceBlockEntity::getItemHandler)
    }

    @JvmStatic
    private fun <BE : HTBlockEntity> registerHandler(event: RegisterCapabilitiesEvent, type: BlockEntityType<BE>) {
        HTItemCapabilities.registerBlockEntity(event, type, HTBlockEntity::getItemHandler)
        HTFluidCapabilities.registerBlockEntity(event, type, HTBlockEntity::getFluidHandler)
        HTEnergyCapabilities.registerBlockEntity(event, type, HTBlockEntity::getEnergyStorage)
    }
}
