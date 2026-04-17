package hiiragi283.ragium.setup

import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.common.block.HTBlockWithEntity
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.capability.HTEnergyCapabilities
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.capability.HTItemCapabilities
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.registry.register.HTDeferredBlockEntityTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.block.entity.HTImitationSpawnerBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTPlanterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAssemblerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBreweryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTChemicalWasherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCuttingMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTEnchanterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTFluidMixerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTFreezerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTFurnaceBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTItemMixerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPyrolyzerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTStonecutterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTWasherBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTBatteryBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCreativeBatteryBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCreativeCrateBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCreativeTankBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTUniversalChestBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTVoidTankBlockEntity
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
    val ASSEMBLER: HTDeferredBlockEntityType<HTAssemblerBlockEntity> =
        REGISTER.registerTick(RagiumConst.ASSEMBLER, ::HTAssemblerBlockEntity)

    @JvmField
    val AUTO_CHISEL: HTDeferredBlockEntityType<HTStonecutterBlockEntity> =
        REGISTER.registerTick(RagiumConst.AUTO_CHISEL, ::HTStonecutterBlockEntity)

    @JvmField
    val CRUSHER: HTDeferredBlockEntityType<HTCrusherBlockEntity> =
        REGISTER.registerTick(RagiumConst.CRUSHER, ::HTCrusherBlockEntity)

    @JvmField
    val CUTTING_MACHINE: HTDeferredBlockEntityType<HTCuttingMachineBlockEntity> =
        REGISTER.registerTick(RagiumConst.CUTTING_MACHINE, ::HTCuttingMachineBlockEntity)

    @JvmField
    val ELECTRIC_FURNACE: HTDeferredBlockEntityType<HTFurnaceBlockEntity> =
        REGISTER.registerTick(RagiumConst.ELECTRIC_FURNACE, ::HTFurnaceBlockEntity)

    @JvmField
    val PLANTER: HTDeferredBlockEntityType<HTPlanterBlockEntity> =
        REGISTER.registerTick(RagiumConst.PLANTER, ::HTPlanterBlockEntity)

    // Advanced
    @JvmField
    val FREEZER: HTDeferredBlockEntityType<HTFreezerBlockEntity> =
        REGISTER.registerTick(RagiumConst.FREEZER, ::HTFreezerBlockEntity)

    @JvmField
    val MELTER: HTDeferredBlockEntityType<HTMelterBlockEntity> =
        REGISTER.registerTick(RagiumConst.MELTER, ::HTMelterBlockEntity)

    @JvmField
    val PYROLYZER: HTDeferredBlockEntityType<HTPyrolyzerBlockEntity> =
        REGISTER.registerTick(RagiumConst.PYROLYZER, ::HTPyrolyzerBlockEntity)

    @JvmField
    val REFINERY: HTDeferredBlockEntityType<HTRefineryBlockEntity> =
        REGISTER.registerTick(RagiumConst.REFINERY, ::HTRefineryBlockEntity)

    @JvmField
    val WASHER: HTDeferredBlockEntityType<HTWasherBlockEntity> =
        REGISTER.registerTick(RagiumConst.WASHER, ::HTWasherBlockEntity)

    // Elite
    @JvmField
    val BREWERY: HTDeferredBlockEntityType<HTBreweryBlockEntity> =
        REGISTER.registerTick(RagiumConst.BREWERY, ::HTBreweryBlockEntity)

    @JvmField
    val CHEMICAL_WASHER: HTDeferredBlockEntityType<HTChemicalWasherBlockEntity> =
        REGISTER.registerTick(RagiumConst.CHEMICAL_WASHER, ::HTChemicalWasherBlockEntity)

    @JvmField
    val FLUID_MIXER: HTDeferredBlockEntityType<HTFluidMixerBlockEntity> =
        REGISTER.registerTick(RagiumConst.FLUID_MIXER, ::HTFluidMixerBlockEntity)

    @JvmField
    val MIXER: HTDeferredBlockEntityType<HTItemMixerBlockEntity> =
        REGISTER.registerTick(RagiumConst.MIXER, ::HTItemMixerBlockEntity)

    // Ultimate
    @JvmField
    val ENCHANTER: HTDeferredBlockEntityType<HTEnchanterBlockEntity> =
        REGISTER.registerTick(RagiumConst.ENCHANTER, ::HTEnchanterBlockEntity)

    //    Device    //

    //    Storage    //

    @JvmField
    val UNIVERSAL_CHEST: HTDeferredBlockEntityType<HTUniversalChestBlockEntity> =
        REGISTER.registerType(RagiumConst.UNIVERSAL_CHEST, ::HTUniversalChestBlockEntity)

    // variable
    @JvmField
    val BATTERY: HTDeferredBlockEntityType<HTBatteryBlockEntity> =
        REGISTER.registerTick("battery", ::HTBatteryBlockEntity)

    @JvmField
    val CRATE: HTDeferredBlockEntityType<HTCrateBlockEntity> =
        REGISTER.registerTick("crate", ::HTCrateBlockEntity)

    @JvmField
    val TANK: HTDeferredBlockEntityType<HTTankBlockEntity> =
        REGISTER.registerTick("tank", ::HTTankBlockEntity)

    // Void
    @JvmField
    val VOID_TANK: HTDeferredBlockEntityType<HTVoidTankBlockEntity> =
        REGISTER.registerTick("void_tank", ::HTVoidTankBlockEntity)

    // Creative
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
        for (holder: HTBlockHolderLike<*> in RagiumBlocks.REGISTER.asBlockSequence()) {
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
        registerHandler(event, ASSEMBLER.get())
        registerHandler(event, AUTO_CHISEL.get())
        registerHandler(event, CRUSHER.get())
        registerHandler(event, CUTTING_MACHINE.get())
        registerHandler(event, ELECTRIC_FURNACE.get())
        registerHandler(event, PLANTER.get())

        registerHandler(event, FREEZER.get())
        registerHandler(event, MELTER.get())
        registerHandler(event, PYROLYZER.get())
        registerHandler(event, REFINERY.get())

        registerHandler(event, BREWERY.get())
        registerHandler(event, CHEMICAL_WASHER.get())
        registerHandler(event, FLUID_MIXER.get())
        registerHandler(event, MIXER.get())
        registerHandler(event, WASHER.get())

        registerHandler(event, ENCHANTER.get())

        // Device

        // Storage
        HTItemCapabilities.registerBlockEntity(event, UNIVERSAL_CHEST.get(), HTUniversalChestBlockEntity::getItemHandler)

        registerHandler(event, BATTERY.get())
        registerHandler(event, CRATE.get())
        registerHandler(event, TANK.get())

        registerHandler(event, VOID_TANK.get())

        registerHandler(event, CREATIVE_BATTERY.get())
        registerHandler(event, CREATIVE_CRATE.get())
        registerHandler(event, CREATIVE_TANK.get())
    }

    @JvmStatic
    private fun <BE : HTBlockEntity> registerHandler(event: RegisterCapabilitiesEvent, type: BlockEntityType<BE>) {
        HTItemCapabilities.registerBlockEntity(event, type, HTBlockEntity::getItemHandler)
        HTFluidCapabilities.registerBlockEntity(event, type, HTBlockEntity::getFluidHandler)
        HTEnergyCapabilities.registerBlockEntity(event, type, HTBlockEntity::getEnergyStorage)
    }
}
