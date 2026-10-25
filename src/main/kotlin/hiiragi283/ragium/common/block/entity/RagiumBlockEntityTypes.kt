package hiiragi283.ragium.common.block.entity

import hiiragi283.lib.HTConstants
import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.lib.capability.HTFluidCapabilities
import hiiragi283.lib.capability.HTItemCapabilities
import hiiragi283.lib.registry.HTDeferredBlock
import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.lib.registry.HTDeferredBlockEntityTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.common.block.HTBasicEntityBlock
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.block.entity.dynamo.HTSteamDynamoBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAssemblerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBreweryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCentrifugeBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTChemicalBathBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCompressorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCuttingMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTElectrolyzerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTEnchanterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTEngraverBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTFreezerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMixerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPlanterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPressMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPyrolyzerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTResourcePumpBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTScannerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSmelterBlockEntity
import hiiragi283.ragium.common.block.entity.misc.HTUpgradeStationBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCreativeBatteryBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCreativeTankBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTPotionTankBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent
import net.neoforged.neoforge.transfer.energy.InfiniteEnergyHandler

data object RagiumBlockEntityTypes {
    @JvmField
    val REGISTER = HTDeferredBlockEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(event: IEventBus) {
        REGISTER.addAlias("brewery", "mixer")

        event.addListener(::addSupportedBlocks)
        event.addListener(::registerCapabilities)

        REGISTER.register(event)
    }

    @JvmStatic
    private fun <BE : HTBlockEntity> registerTick(name: String, factory: BlockEntityType.BlockEntitySupplier<BE>): HTDeferredBlockEntityType<BE> =
        REGISTER.registerType(name, factory, HTBlockEntity::tickServer, HTBlockEntity::tickClient)

    //    Dynamo    //

    @JvmField
    val STEAM_DYNAMO: HTDeferredBlockEntityType<HTSteamDynamoBlockEntity> =
        registerTick(RagiumConstants.STEAM_DYNAMO, ::HTSteamDynamoBlockEntity)

    //    Machine    //

    // Mechanical
    @JvmField
    val ASSEMBLER: HTDeferredBlockEntityType<HTAssemblerBlockEntity> =
        registerTick(RagiumConstants.ASSEMBLER, ::HTAssemblerBlockEntity)

    @JvmField
    val CENTRIFUGE: HTDeferredBlockEntityType<HTCentrifugeBlockEntity> =
        registerTick(RagiumConstants.CENTRIFUGE, ::HTCentrifugeBlockEntity)

    @JvmField
    val CRUSHER: HTDeferredBlockEntityType<HTCrusherBlockEntity> =
        registerTick(RagiumConstants.CRUSHER, ::HTCrusherBlockEntity)

    @JvmField
    val COMPRESSOR: HTDeferredBlockEntityType<HTCompressorBlockEntity> =
        registerTick(RagiumConstants.COMPRESSOR, ::HTCompressorBlockEntity)

    @JvmField
    val CUTTING_MACHINE: HTDeferredBlockEntityType<HTCuttingMachineBlockEntity> =
        registerTick(RagiumConstants.CUTTING_MACHINE, ::HTCuttingMachineBlockEntity)

    @JvmField
    val PRESS_MACHINE: HTDeferredBlockEntityType<HTPressMachineBlockEntity> =
        registerTick(RagiumConstants.PRESS_MACHINE, ::HTPressMachineBlockEntity)

    // Heat
    @JvmField
    val ALLOY_SMELTER: HTDeferredBlockEntityType<HTAlloySmelterBlockEntity> =
        registerTick(RagiumConstants.ALLOY_SMELTER, ::HTAlloySmelterBlockEntity)

    @JvmField
    val FREEZER: HTDeferredBlockEntityType<HTFreezerBlockEntity> =
        registerTick(RagiumConstants.FREEZER, ::HTFreezerBlockEntity)

    @JvmField
    val MELTER: HTDeferredBlockEntityType<HTMelterBlockEntity> =
        registerTick(RagiumConstants.MELTER, ::HTMelterBlockEntity)

    @JvmField
    val PYROLYZER: HTDeferredBlockEntityType<HTPyrolyzerBlockEntity> =
        registerTick(RagiumConstants.PYROLYZER, ::HTPyrolyzerBlockEntity)

    @JvmField
    val REFINERY: HTDeferredBlockEntityType<HTRefineryBlockEntity> =
        registerTick(RagiumConstants.REFINERY, ::HTRefineryBlockEntity)

    @JvmField
    val SMELTER: HTDeferredBlockEntityType<HTSmelterBlockEntity> =
        registerTick(RagiumConstants.SMELTER, ::HTSmelterBlockEntity)

    // Chemical
    @JvmField
    val CHEMICAL_BATH: HTDeferredBlockEntityType<HTChemicalBathBlockEntity> =
        registerTick(RagiumConstants.CHEMICAL_BATH, ::HTChemicalBathBlockEntity)

    @JvmField
    val MIXER: HTDeferredBlockEntityType<HTMixerBlockEntity> =
        registerTick(RagiumConstants.MIXER, ::HTMixerBlockEntity)

    // Resource
    @JvmField
    val BREWERY: HTDeferredBlockEntityType<HTBreweryBlockEntity> =
        registerTick(RagiumConstants.BREWERY, ::HTBreweryBlockEntity)

    @JvmField
    val PLANTER: HTDeferredBlockEntityType<HTPlanterBlockEntity> =
        registerTick(RagiumConstants.PLANTER, ::HTPlanterBlockEntity)

    @JvmField
    val RESOURCE_PUMP: HTDeferredBlockEntityType<HTResourcePumpBlockEntity> =
        registerTick(RagiumConstants.RESOURCE_PUMP, ::HTResourcePumpBlockEntity)

    // Electronics
    @JvmField
    val ELECTROLYZER: HTDeferredBlockEntityType<HTElectrolyzerBlockEntity> =
        registerTick(RagiumConstants.ELECTROLYZER, ::HTElectrolyzerBlockEntity)

    @JvmField
    val ENGRAVER: HTDeferredBlockEntityType<HTEngraverBlockEntity> =
        registerTick(RagiumConstants.ENGRAVER, ::HTEngraverBlockEntity)

    @JvmField
    val SCANNER: HTDeferredBlockEntityType<HTScannerBlockEntity> =
        registerTick(RagiumConstants.SCANNER, ::HTScannerBlockEntity)

    // Arcane
    @JvmField
    val ENCHANTER: HTDeferredBlockEntityType<HTEnchanterBlockEntity> =
        registerTick(RagiumConstants.ENCHANTER, ::HTEnchanterBlockEntity)

    //    Storage    //

    // Tank
    @JvmField
    val TANK: HTDeferredBlockEntityType<HTTankBlockEntity> =
        registerTick(HTConstants.TANK, ::HTTankBlockEntity)

    @JvmField
    val POTION_TANK: HTDeferredBlockEntityType<HTPotionTankBlockEntity> =
        registerTick("potion_tank", ::HTPotionTankBlockEntity)

    @JvmField
    val CREATIVE_TANK: HTDeferredBlockEntityType<HTCreativeTankBlockEntity> =
        registerTick("creative_tank", ::HTCreativeTankBlockEntity)

    // Battery
    @JvmField
    val CREATIVE_BATTERY: HTDeferredBlockEntityType<HTCreativeBatteryBlockEntity> =
        registerTick("creative_battery", ::HTCreativeBatteryBlockEntity)

    //    Misc    //

    @JvmField
    val UPGRADE_STATION: HTDeferredBlockEntityType<HTUpgradeStationBlockEntity> =
        registerTick("upgrade_station", ::HTUpgradeStationBlockEntity)

    //    Events    //

    @JvmStatic
    private fun addSupportedBlocks(event: BlockEntityTypeAddBlocksEvent) {
        for (holder: HTDeferredBlock<*> in RagiumBlocks.REGISTER.asBlockSequence()) {
            val block: Block = holder.get()
            if (block is HTBasicEntityBlock) {
                event.modify(block.type.get(), block)
            }
        }
    }

    @JvmStatic
    private fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        fun <BE : HTBlockEntity> registerBlockEntity(type: BlockEntityType<BE>) {
            event.registerBlockEntity(HTEnergyCapabilities.block, type, HTBlockEntity::getEnergyHandler)
            event.registerBlockEntity(HTFluidCapabilities.block, type, HTBlockEntity::getFluidHandler)
            event.registerBlockEntity(HTItemCapabilities.block, type, HTBlockEntity::getItemHandler)
        }

        // Dynamo
        registerBlockEntity(STEAM_DYNAMO.get())

        // Machine
        registerBlockEntity(ASSEMBLER.get())
        registerBlockEntity(CENTRIFUGE.get())
        registerBlockEntity(CRUSHER.get())
        registerBlockEntity(COMPRESSOR.get())
        registerBlockEntity(CUTTING_MACHINE.get())
        registerBlockEntity(PRESS_MACHINE.get())

        registerBlockEntity(ALLOY_SMELTER.get())
        registerBlockEntity(FREEZER.get())
        registerBlockEntity(MELTER.get())
        registerBlockEntity(PYROLYZER.get())
        registerBlockEntity(REFINERY.get())
        registerBlockEntity(SMELTER.get())

        registerBlockEntity(CHEMICAL_BATH.get())
        registerBlockEntity(MIXER.get())

        registerBlockEntity(BREWERY.get())
        registerBlockEntity(PLANTER.get())
        registerBlockEntity(RESOURCE_PUMP.get())

        registerBlockEntity(ELECTROLYZER.get())
        registerBlockEntity(ENGRAVER.get())
        registerBlockEntity(SCANNER.get())

        registerBlockEntity(ENCHANTER.get())
        // Storage
        registerBlockEntity(TANK.get())
        registerBlockEntity(POTION_TANK.get())
        registerBlockEntity(CREATIVE_TANK.get())

        event.registerBlockEntity(
            HTEnergyCapabilities.block,
            CREATIVE_BATTERY.get(),
        ) { _, _ -> InfiniteEnergyHandler.INSTANCE }
        // Misc
        registerBlockEntity(UPGRADE_STATION.get())
    }
}
