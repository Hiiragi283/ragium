package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.api.block.entity.HTBlockEntityFactory
import hiiragi283.ragium.api.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.capability.HTFluidCapabilities
import hiiragi283.ragium.api.capability.HTItemCapabilities
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityTypeRegister
import hiiragi283.ragium.api.registry.impl.HTDeferredOnlyBlock
import hiiragi283.ragium.api.storage.HTHandlerProvider
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTImitationSpawnerBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTDimensionalAnchorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTEnergyNetworkAccessBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTFluidCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTItemCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTStoneCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTTelepadBlockentity
import hiiragi283.ragium.common.block.entity.generator.HTCombustionGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTCulinaryGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTMagmaticGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTSolarPanelControllerBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTThermalGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTAdvancedMixerBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTBlockBreakerBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTBreweryBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTCompressorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTCuttingMachineBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTElectricFurnaceBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTEnchanterBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTMixerBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTMobCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTPulverizerBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTSimulatorBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent

object RagiumBlockEntityTypes {
    @JvmField
    val REGISTER = HTDeferredBlockEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.addAlias("water_collector", "fluid_collector")
        REGISTER.addAlias("exp_collector", "fluid_collector")

        REGISTER.addAlias("fisher", "item_collector")
        REGISTER.addAlias("item_buffer", "item_collector")
        REGISTER.addAlias("mob_capturer", "item_collector")

        listOf(
            "small",
            "medium",
            "large",
            "huge",
        ).forEach {
            REGISTER.addAlias("${it}_drum", "tank")
            REGISTER.addAlias("${it}_crate", "crate")
        }

        REGISTER.register(eventBus)

        eventBus.addListener(::addSupportedBlocks)
        eventBus.addListener(::registerBlockCapabilities)
    }

    @JvmStatic
    private fun <BE : HTBlockEntity> registerTick(name: String, factory: HTBlockEntityFactory<BE>): HTDeferredBlockEntityType<BE> =
        REGISTER.registerType(name, factory, HTBlockEntity::tickClient, HTBlockEntity::tickServer)

    @JvmField
    val IMITATION_SPAWNER: HTDeferredBlockEntityType<HTImitationSpawnerBlockEntity> =
        REGISTER.registerType("imitation_spawner", ::HTImitationSpawnerBlockEntity)

    //    Generator    //

    // Basic
    @JvmField
    val THERMAL_GENERATOR: HTDeferredBlockEntityType<HTThermalGeneratorBlockEntity> =
        registerTick("thermal_generator", ::HTThermalGeneratorBlockEntity)

    // Advanced
    @JvmField
    val CULINARY_GENERATOR: HTDeferredBlockEntityType<HTCulinaryGeneratorBlockEntity> =
        registerTick("culinary_generator", ::HTCulinaryGeneratorBlockEntity)

    @JvmField
    val MAGMATIC_GENERATOR: HTDeferredBlockEntityType<HTMagmaticGeneratorBlockEntity> =
        registerTick("magmatic_generator", ::HTMagmaticGeneratorBlockEntity)

    // Elite
    @JvmField
    val COMBUSTION_GENERATOR: HTDeferredBlockEntityType<HTCombustionGeneratorBlockEntity> =
        registerTick("combustion_generator", ::HTCombustionGeneratorBlockEntity)

    @JvmField
    val SOLAR_PANEL_CONTROLLER: HTDeferredBlockEntityType<HTSolarPanelControllerBlockEntity> = registerTick(
        "solar_panel_controller",
        ::HTSolarPanelControllerBlockEntity,
    )

    // Ultimate
    @JvmField
    val ENCHANTMENT_GENERATOR: HTDeferredBlockEntityType<HTThermalGeneratorBlockEntity> =
        registerTick("enchantment_generator", ::HTThermalGeneratorBlockEntity)

    @JvmField
    val NUCLEAR_REACTOR: HTDeferredBlockEntityType<HTThermalGeneratorBlockEntity> =
        registerTick("nuclear_reactor", ::HTThermalGeneratorBlockEntity)

    //    Processor    //

    // Basic
    @JvmField
    val ALLOY_SMELTER: HTDeferredBlockEntityType<HTAlloySmelterBlockEntity> = registerTick("alloy_smelter", ::HTAlloySmelterBlockEntity)

    @JvmField
    val BLOCK_BREAKER: HTDeferredBlockEntityType<HTBlockBreakerBlockEntity> = registerTick("block_breaker", ::HTBlockBreakerBlockEntity)

    @JvmField
    val COMPRESSOR: HTDeferredBlockEntityType<HTCompressorBlockEntity> = registerTick("compressor", ::HTCompressorBlockEntity)

    @JvmField
    val CUTTING_MACHINE: HTDeferredBlockEntityType<HTCuttingMachineBlockEntity> = registerTick(
        "cutting_machine",
        ::HTCuttingMachineBlockEntity,
    )

    @JvmField
    val ELECTRIC_FURNACE: HTDeferredBlockEntityType<HTElectricFurnaceBlockEntity> = registerTick(
        "electric_furnace",
        ::HTElectricFurnaceBlockEntity,
    )

    @JvmField
    val EXTRACTOR: HTDeferredBlockEntityType<HTExtractorBlockEntity> = registerTick("extractor", ::HTExtractorBlockEntity)

    @JvmField
    val PULVERIZER: HTDeferredBlockEntityType<HTPulverizerBlockEntity> = registerTick("pulverizer", ::HTPulverizerBlockEntity)

    // Advanced
    @JvmField
    val CRUSHER: HTDeferredBlockEntityType<HTCrusherBlockEntity> = registerTick("crusher", ::HTCrusherBlockEntity)

    @JvmField
    val MELTER: HTDeferredBlockEntityType<HTMelterBlockEntity> = registerTick("melter", ::HTMelterBlockEntity)

    @JvmField
    val MIXER: HTDeferredBlockEntityType<HTMixerBlockEntity> = registerTick("mixer", ::HTMixerBlockEntity)

    @JvmField
    val REFINERY: HTDeferredBlockEntityType<HTRefineryBlockEntity> = registerTick("refinery", ::HTRefineryBlockEntity)

    // Elite
    @JvmField
    val ADVANCED_MIXER: HTDeferredBlockEntityType<HTAdvancedMixerBlockEntity> = registerTick("advanced_mixer", ::HTAdvancedMixerBlockEntity)

    @JvmField
    val BREWERY: HTDeferredBlockEntityType<HTBreweryBlockEntity> = registerTick("brewery", ::HTBreweryBlockEntity)

    @JvmField
    val MULTI_SMELTER: HTDeferredBlockEntityType<HTMultiSmelterBlockEntity> = registerTick("multi_smelter", ::HTMultiSmelterBlockEntity)

    @JvmField
    val PLANTER: HTDeferredBlockEntityType<HTMultiSmelterBlockEntity> = registerTick("planter", ::HTMultiSmelterBlockEntity)

    // Ultimate
    @JvmField
    val ENCHANTER: HTDeferredBlockEntityType<HTEnchanterBlockEntity> = registerTick("enchanter", ::HTEnchanterBlockEntity)

    @JvmField
    val MOB_CRUSHER: HTDeferredBlockEntityType<HTMobCrusherBlockEntity> = registerTick("mob_crusher", ::HTMobCrusherBlockEntity)

    @JvmField
    val SIMULATOR: HTDeferredBlockEntityType<HTSimulatorBlockEntity> = registerTick("simulator", ::HTSimulatorBlockEntity)

    //    Device    //

    // Basic
    @JvmField
    val FLUID_COLLECTOR: HTDeferredBlockEntityType<HTFluidCollectorBlockEntity> = registerTick(
        "fluid_collector",
        ::HTFluidCollectorBlockEntity,
    )

    @JvmField
    val ITEM_COLLECTOR: HTDeferredBlockEntityType<HTItemCollectorBlockEntity> = registerTick(
        "item_collector",
        ::HTItemCollectorBlockEntity,
    )

    // Advanced
    @JvmField
    val STONE_COLLECTOR: HTDeferredBlockEntityType<HTStoneCollectorBlockEntity> = registerTick(
        "stone_collector",
        ::HTStoneCollectorBlockEntity,
    )

    // Elite
    @JvmField
    val DIM_ANCHOR: HTDeferredBlockEntityType<HTDimensionalAnchorBlockEntity> = REGISTER.registerType(
        "dimensional_anchor",
        ::HTDimensionalAnchorBlockEntity,
    )

    @JvmField
    val ENI: HTDeferredBlockEntityType<HTEnergyNetworkAccessBlockEntity> = registerTick(
        "energy_network_interface",
        HTEnergyNetworkAccessBlockEntity::Simple,
    )

    // Ultimate
    @JvmField
    val TELEPAD: HTDeferredBlockEntityType<HTTelepadBlockentity> = REGISTER.registerType(
        "telepad",
        ::HTTelepadBlockentity,
    )

    // Creative
    @JvmField
    val CEU: HTDeferredBlockEntityType<HTEnergyNetworkAccessBlockEntity> = registerTick(
        "creative_energy_unit",
        HTEnergyNetworkAccessBlockEntity::Creative,
    )

    //    Storage    //

    @JvmField
    val CRATE: HTDeferredBlockEntityType<HTCrateBlockEntity> = registerTick("crate", ::HTCrateBlockEntity)

    @JvmField
    val TANK: HTDeferredBlockEntityType<HTTankBlockEntity> = registerTick("tank", ::HTTankBlockEntity)

    //    Event    //

    // Supported Blocks
    @JvmStatic
    private fun addSupportedBlocks(event: BlockEntityTypeAddBlocksEvent) {
        for (holder: HTDeferredOnlyBlock<*> in RagiumBlocks.REGISTER.blockEntries) {
            val block: Block? = holder.get()
            if (block is HTBlockWithEntity) {
                event.modify(block.getBlockEntityType().get(), block)
            }
        }

        RagiumAPI.LOGGER.info("Added supported blocks to BlockEntityType!")
    }

    // Capabilities
    @JvmStatic
    private fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        // Generator
        registerHandler(event, THERMAL_GENERATOR.get())

        registerHandler(event, CULINARY_GENERATOR.get())
        registerHandler(event, MAGMATIC_GENERATOR.get())

        registerHandler(event, COMBUSTION_GENERATOR.get())
        registerHandler(event, SOLAR_PANEL_CONTROLLER.get())

        registerHandler(event, ENCHANTMENT_GENERATOR.get())
        registerHandler(event, NUCLEAR_REACTOR.get())
        // Processor
        registerHandler(event, ALLOY_SMELTER.get())
        registerHandler(event, BLOCK_BREAKER.get())
        registerHandler(event, COMPRESSOR.get())
        registerHandler(event, CUTTING_MACHINE.get())
        registerHandler(event, ELECTRIC_FURNACE.get())
        registerHandler(event, EXTRACTOR.get())
        registerHandler(event, PULVERIZER.get())

        registerHandler(event, CRUSHER.get())
        registerHandler(event, MELTER.get())
        registerHandler(event, MIXER.get())
        registerHandler(event, REFINERY.get())

        registerHandler(event, ADVANCED_MIXER.get())
        registerHandler(event, BREWERY.get())
        registerHandler(event, MULTI_SMELTER.get())
        registerHandler(event, PLANTER.get())

        registerHandler(event, ENCHANTER.get())
        registerHandler(event, MOB_CRUSHER.get())
        registerHandler(event, SIMULATOR.get())
        // Devices
        registerHandler(event, FLUID_COLLECTOR.get())
        registerHandler(event, ITEM_COLLECTOR.get())

        registerHandler(event, STONE_COLLECTOR.get())

        registerHandler(event, ENI.get())

        registerHandler(event, TELEPAD.get())

        registerHandler(event, CEU.get())
        // Storage
        registerHandler(event, CRATE.get())
        registerHandler(event, TANK.get())

        RagiumAPI.LOGGER.info("Registered Block Capabilities!")
    }

    @JvmStatic
    private fun registerHandler(event: RegisterCapabilitiesEvent, type: BlockEntityType<out HTBlockEntity>) {
        event.registerBlockEntity(HTItemCapabilities.block, type, HTHandlerProvider::getItemHandler)
        event.registerBlockEntity(HTFluidCapabilities.block, type, HTHandlerProvider::getFluidHandler)
        event.registerBlockEntity(HTEnergyCapabilities.block, type, HTHandlerProvider::getEnergyStorage)
    }
}
