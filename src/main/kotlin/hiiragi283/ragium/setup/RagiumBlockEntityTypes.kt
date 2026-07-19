package hiiragi283.ragium.setup

import hiiragi283.core.api.registry.HTDeferredBlock
import hiiragi283.core.common.block.HTBlockWithEntity
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.registry.register.HTDeferredBlockEntityTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.block.entity.HTImitationSpawnerBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTEnchanterBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTBoilerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAssemblerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBreweryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCompressorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCuttingMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTFluidDuplicatorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTFreezerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTFurnaceBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMassFabricatorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPlanterBlockEntity
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
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent

data object RagiumBlockEntityTypes {
    @JvmField
    val REGISTER = HTDeferredBlockEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        eventBus.addListener(::addSupportedBlocks)

        REGISTER.register(eventBus)
    }

    @JvmField
    val IMITATION_SPAWNER: HTDeferredBlockEntityType<HTImitationSpawnerBlockEntity> = REGISTER.registerType("imitation_spawner", ::HTImitationSpawnerBlockEntity)

    //    Generator    //

    // Basic
    @JvmField
    val BOILER: HTDeferredBlockEntityType<HTBoilerBlockEntity> = REGISTER.registerTick(RagiumConst.BOILER, ::HTBoilerBlockEntity)

    //    Machine    //

    // Basic
    @JvmField
    val ALLOY_SMELTER: HTDeferredBlockEntityType<HTAlloySmelterBlockEntity> = REGISTER.registerTick(RagiumConst.ALLOY_SMELTER, ::HTAlloySmelterBlockEntity)

    @JvmField
    val ASSEMBLER: HTDeferredBlockEntityType<HTAssemblerBlockEntity> = REGISTER.registerTick(RagiumConst.ASSEMBLER, ::HTAssemblerBlockEntity)

    @JvmField
    val AUTO_CHISEL: HTDeferredBlockEntityType<HTStonecutterBlockEntity> = REGISTER.registerTick(RagiumConst.AUTO_CHISEL, ::HTStonecutterBlockEntity)

    @JvmField
    val COMPRESSOR: HTDeferredBlockEntityType<HTCompressorBlockEntity> = REGISTER.registerTick(RagiumConst.COMPRESSOR, ::HTCompressorBlockEntity)

    @JvmField
    val CRUSHER: HTDeferredBlockEntityType<HTCrusherBlockEntity> = REGISTER.registerTick(RagiumConst.CRUSHER, ::HTCrusherBlockEntity)

    @JvmField
    val CUTTING_MACHINE: HTDeferredBlockEntityType<HTCuttingMachineBlockEntity> = REGISTER.registerTick(RagiumConst.CUTTING_MACHINE, ::HTCuttingMachineBlockEntity)

    @JvmField
    val ELECTRIC_FURNACE: HTDeferredBlockEntityType<HTFurnaceBlockEntity> = REGISTER.registerTick(RagiumConst.ELECTRIC_FURNACE, ::HTFurnaceBlockEntity)

    @JvmField
    val PLANTER: HTDeferredBlockEntityType<HTPlanterBlockEntity> = REGISTER.registerTick(RagiumConst.PLANTER, ::HTPlanterBlockEntity)

    // Advanced
    @JvmField
    val FREEZER: HTDeferredBlockEntityType<HTFreezerBlockEntity> = REGISTER.registerTick(RagiumConst.FREEZER, ::HTFreezerBlockEntity)

    @JvmField
    val MELTER: HTDeferredBlockEntityType<HTMelterBlockEntity> = REGISTER.registerTick(RagiumConst.MELTER, ::HTMelterBlockEntity)

    @JvmField
    val PYROLYZER: HTDeferredBlockEntityType<HTPyrolyzerBlockEntity> = REGISTER.registerTick(RagiumConst.PYROLYZER, ::HTPyrolyzerBlockEntity)

    @JvmField
    val REFINERY: HTDeferredBlockEntityType<HTRefineryBlockEntity> = REGISTER.registerTick(RagiumConst.REFINERY, ::HTRefineryBlockEntity)

    @JvmField
    val WASHER: HTDeferredBlockEntityType<HTWasherBlockEntity> = REGISTER.registerTick(RagiumConst.WASHER, ::HTWasherBlockEntity)

    // Elite
    @JvmField
    val BREWERY: HTDeferredBlockEntityType<HTBreweryBlockEntity> = REGISTER.registerTick(RagiumConst.BREWERY, ::HTBreweryBlockEntity)

    @JvmField
    val MIXER: HTDeferredBlockEntityType<HTBreweryBlockEntity> = REGISTER.registerTick(RagiumConst.MIXER, ::HTBreweryBlockEntity)

    // Ultimate
    @JvmField
    val FLUID_DUPLICATOR: HTDeferredBlockEntityType<HTFluidDuplicatorBlockEntity> = REGISTER.registerTick(RagiumConst.FLUID_DUPLICATOR, ::HTFluidDuplicatorBlockEntity)

    //    Device    //

    // Ultimate
    @JvmField
    val ENCHANTER: HTDeferredBlockEntityType<HTEnchanterBlockEntity> = REGISTER.registerTick(RagiumConst.ENCHANTER, ::HTEnchanterBlockEntity)

    @JvmField
    val MASS_FABRICATOR: HTDeferredBlockEntityType<HTMassFabricatorBlockEntity> = REGISTER.registerTick(RagiumConst.MASS_FABRICATOR, ::HTMassFabricatorBlockEntity)

    //    Storage    //

    @JvmField
    val UNIVERSAL_CHEST: HTDeferredBlockEntityType<HTUniversalChestBlockEntity> = REGISTER.registerType(RagiumConst.UNIVERSAL_CHEST, ::HTUniversalChestBlockEntity)

    // variable
    @JvmField
    val BATTERY: HTDeferredBlockEntityType<HTBatteryBlockEntity.Simple> = REGISTER.registerTick("battery", HTBatteryBlockEntity<*>::Simple)

    @JvmField
    val CRATE: HTDeferredBlockEntityType<HTCrateBlockEntity> = REGISTER.registerTick("crate", ::HTCrateBlockEntity)

    @JvmField
    val TANK: HTDeferredBlockEntityType<HTTankBlockEntity> = REGISTER.registerTick("tank", ::HTTankBlockEntity)

    // Void
    @JvmField
    val VOID_TANK: HTDeferredBlockEntityType<HTVoidTankBlockEntity> = REGISTER.registerTick("void_tank", ::HTVoidTankBlockEntity)

    // Creative
    @JvmField
    val CREATIVE_BATTERY: HTDeferredBlockEntityType<HTCreativeBatteryBlockEntity> = REGISTER.registerTick("creative_battery", ::HTCreativeBatteryBlockEntity)

    @JvmField
    val CREATIVE_CRATE: HTDeferredBlockEntityType<HTCreativeCrateBlockEntity> = REGISTER.registerTick("creative_crate", ::HTCreativeCrateBlockEntity)

    @JvmField
    val CREATIVE_TANK: HTDeferredBlockEntityType<HTCreativeTankBlockEntity> = REGISTER.registerTick("creative_tank", ::HTCreativeTankBlockEntity)

    //    Event    //

    // Supported Blocks
    @JvmStatic
    private fun addSupportedBlocks(event: BlockEntityTypeAddBlocksEvent) {
        for (holder: HTDeferredBlock<*> in RagiumBlocks.REGISTER.asBlockSequence()) {
            val block: Block = holder.get()
            if (block is HTBlockWithEntity) {
                event.modify(block.getBlockEntityType().get(), block)
            }
        }
    }
}
