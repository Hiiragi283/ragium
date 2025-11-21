package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntityFactory
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.chance.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.single.HTSingleItemRecipe
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityTypeRegister
import hiiragi283.ragium.api.registry.impl.HTDeferredOnlyBlock
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTHandlerProvider
import hiiragi283.ragium.api.storage.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.storage.capability.HTFluidCapabilities
import hiiragi283.ragium.api.storage.capability.HTItemCapabilities
import hiiragi283.ragium.common.block.HTTypedEntityBlock
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTDimensionalAnchorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTEnergyNetworkAccessBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTExpCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTFisherBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTItemBufferBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTMobCapturerBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTTelepadBlockentity
import hiiragi283.ragium.common.block.entity.device.HTWaterCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTFuelGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTNuclearReactorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTSolarPanelControllerBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTBlockBreakerBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTBreweryBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTCuttingMachineBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTElectricFurnaceBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTEnchantCopierBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTEnchanterBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTMixerBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTPlanterBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTSimulatorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTWasherBlockEntity
import hiiragi283.ragium.common.block.entity.processor.base.HTSingleItemInputBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTDrumBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTExpDrumBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTOpenCrateBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTTieredDrumBlockEntity
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent

object RagiumBlockEntityTypes {
    @JvmField
    val REGISTER = HTDeferredBlockEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        eventBus.addListener(::addSupportedBlocks)
        eventBus.addListener(::registerBlockCapabilities)
    }

    @JvmStatic
    private fun <BE : HTBlockEntity> registerTick(name: String, factory: HTBlockEntityFactory<BE>): HTDeferredBlockEntityType<BE> =
        REGISTER.registerType(name, factory, HTBlockEntity::tickClient, HTBlockEntity::tickServer)

    //    Generator    //

    // Basic
    @JvmField
    val THERMAL_GENERATOR: HTDeferredBlockEntityType<HTFuelGeneratorBlockEntity> = registerTick(
        "thermal_generator",
        HTFuelGeneratorBlockEntity.createSimple(
            { stack: ImmutableItemStack -> stack.unwrap().getBurnTime(null) / 10 },
            HTFluidHolderLike.LAVA,
            RagiumDataMaps.INSTANCE::getThermalFuel,
            RagiumBlocks.THERMAL_GENERATOR,
        ),
    )

    // Advanced
    @JvmField
    val COMBUSTION_GENERATOR: HTDeferredBlockEntityType<HTFuelGeneratorBlockEntity> = registerTick(
        "combustion_generator",
        HTFuelGeneratorBlockEntity.createSimple(
            { stack: ImmutableItemStack ->
                when {
                    stack.isOf(ItemTags.COALS) -> 100
                    else -> 0
                }
            },
            RagiumFluidContents.CRUDE_OIL,
            RagiumDataMaps.INSTANCE::getCombustionFuel,
            RagiumBlocks.COMBUSTION_GENERATOR,
        ),
    )

    // Elite
    @JvmField
    val SOLAR_PANEL_CONTROLLER: HTDeferredBlockEntityType<HTSolarPanelControllerBlockEntity> = registerTick(
        "solar_panel_controller",
        ::HTSolarPanelControllerBlockEntity,
    )

    // Ultimate
    @JvmField
    val ENCHANTMENT_GENERATOR: HTDeferredBlockEntityType<HTFuelGeneratorBlockEntity> = registerTick(
        "enchantment_generator",
        HTFuelGeneratorBlockEntity.createSimple(
            { 0 },
            RagiumFluidContents.EXPERIENCE,
            { _, holder: Holder<Fluid> -> if (RagiumFluidContents.EXPERIENCE.isOf(holder)) 10 else 0 },
            RagiumBlocks.ENCHANTMENT_GENERATOR,
        ),
    )

    @JvmField
    val NUCLEAR_REACTOR: HTDeferredBlockEntityType<HTNuclearReactorBlockEntity> = registerTick(
        "nuclear_reactor",
        ::HTNuclearReactorBlockEntity,
    )

    //    Processor    //

    // Basic
    @JvmField
    val ALLOY_SMELTER: HTDeferredBlockEntityType<HTAlloySmelterBlockEntity> = registerTick("alloy_smelter", ::HTAlloySmelterBlockEntity)

    @JvmField
    val BLOCK_BREAKER: HTDeferredBlockEntityType<HTBlockBreakerBlockEntity> = registerTick("block_breaker", ::HTBlockBreakerBlockEntity)

    @JvmField
    val COMPRESSOR: HTDeferredBlockEntityType<HTSingleItemInputBlockEntity<HTSingleItemRecipe>> = registerTick(
        "compressor",
        HTSingleItemInputBlockEntity.createSimple(
            SoundEvents.ANVIL_PLACE,
            0.25f to 0.5f,
            RagiumRecipeTypes.COMPRESSING,
        ),
    )

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
    val PULVERIZER: HTDeferredBlockEntityType<HTSingleItemInputBlockEntity<HTItemToChancedItemRecipe>> = registerTick(
        "pulverizer",
        HTSingleItemInputBlockEntity.createSimple(
            SoundEvents.GRINDSTONE_USE,
            0.25f to 1f,
            RagiumRecipeTypes.CRUSHING,
        ),
    )

    // Advanced
    @JvmField
    val CRUSHER: HTDeferredBlockEntityType<HTCrusherBlockEntity> = registerTick("crusher", ::HTCrusherBlockEntity)

    @JvmField
    val MELTER: HTDeferredBlockEntityType<HTMelterBlockEntity> = registerTick("melter", ::HTMelterBlockEntity)

    @JvmField
    val REFINERY: HTDeferredBlockEntityType<HTRefineryBlockEntity> = registerTick("refinery", ::HTRefineryBlockEntity)

    @JvmField
    val WASHER: HTDeferredBlockEntityType<HTWasherBlockEntity> = registerTick("washer", ::HTWasherBlockEntity)

    // Elite
    @JvmField
    val BREWERY: HTDeferredBlockEntityType<HTBreweryBlockEntity> = registerTick("brewery", ::HTBreweryBlockEntity)

    @JvmField
    val MIXER: HTDeferredBlockEntityType<HTMixerBlockEntity> = registerTick("mixer", ::HTMixerBlockEntity)

    @JvmField
    val MULTI_SMELTER: HTDeferredBlockEntityType<HTMultiSmelterBlockEntity> = registerTick("multi_smelter", ::HTMultiSmelterBlockEntity)

    @JvmField
    val PLANTER: HTDeferredBlockEntityType<HTPlanterBlockEntity> = registerTick("planter", ::HTPlanterBlockEntity)

    // Ultimate
    @JvmField
    val ENCHANT_COPIER: HTDeferredBlockEntityType<HTEnchantCopierBlockEntity> = registerTick(
        "enchantment_copier",
        ::HTEnchantCopierBlockEntity,
    )

    @JvmField
    val ENCHANTER: HTDeferredBlockEntityType<HTEnchanterBlockEntity> = registerTick("enchanter", ::HTEnchanterBlockEntity)

    @JvmField
    val SIMULATOR: HTDeferredBlockEntityType<HTSimulatorBlockEntity> = registerTick("simulator", ::HTSimulatorBlockEntity)

    //    Device    //

    // Basic
    @JvmField
    val ITEM_BUFFER: HTDeferredBlockEntityType<HTItemBufferBlockEntity> = registerTick(
        "item_buffer",
        ::HTItemBufferBlockEntity,
    )

    @JvmField
    val WATER_COLLECTOR: HTDeferredBlockEntityType<HTWaterCollectorBlockEntity> = registerTick(
        "water_collector",
        ::HTWaterCollectorBlockEntity,
    )

    // Advanced
    @JvmField
    val EXP_COLLECTOR: HTDeferredBlockEntityType<HTExpCollectorBlockEntity> = registerTick(
        "exp_collector",
        ::HTExpCollectorBlockEntity,
    )

    @JvmField
    val FISHER: HTDeferredBlockEntityType<HTFisherBlockEntity> = registerTick(
        "fisher",
        ::HTFisherBlockEntity,
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
    val MOB_CAPTURER: HTDeferredBlockEntityType<HTMobCapturerBlockEntity> = registerTick(
        "mob_capturer",
        ::HTMobCapturerBlockEntity,
    )

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
    val CRATES: Map<HTCrateTier, HTDeferredBlockEntityType<HTCrateBlockEntity>> =
        HTCrateTier.entries.associateWith { tier: HTCrateTier ->
            registerTick(tier.path) { pos: BlockPos, state: BlockState -> HTCrateBlockEntity(tier.getBlock(), pos, state) }
        }

    @JvmField
    val OPEN_CRATE: HTDeferredBlockEntityType<HTOpenCrateBlockEntity> = REGISTER.registerType("open_crate", ::HTOpenCrateBlockEntity)

    @JvmField
    val DRUMS: Map<HTDrumTier, HTDeferredBlockEntityType<HTDrumBlockEntity>> =
        HTDrumTier.entries.associateWith { tier: HTDrumTier ->
            registerTick(tier.path) { pos: BlockPos, state: BlockState -> HTTieredDrumBlockEntity(tier.getBlock(), pos, state) }
        }

    @JvmField
    val EXP_DRUM: HTDeferredBlockEntityType<HTExpDrumBlockEntity> = registerTick("experience_drum", ::HTExpDrumBlockEntity)

    //    Event    //

    // Supported Blocks
    @JvmStatic
    private fun addSupportedBlocks(event: BlockEntityTypeAddBlocksEvent) {
        for (holder: HTDeferredOnlyBlock<*> in RagiumBlocks.REGISTER.blockEntries) {
            val block: Block? = holder.get()
            if (block is HTTypedEntityBlock<*>) {
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
        registerHandler(event, REFINERY.get())
        registerHandler(event, WASHER.get())

        registerHandler(event, BREWERY.get())
        registerHandler(event, MIXER.get())
        registerHandler(event, MULTI_SMELTER.get())
        registerHandler(event, PLANTER.get())

        registerHandler(event, ENCHANT_COPIER.get())
        registerHandler(event, SIMULATOR.get())
        // Devices
        registerHandler(event, ITEM_BUFFER.get())
        registerHandler(event, WATER_COLLECTOR.get())

        registerHandler(event, EXP_COLLECTOR.get())
        registerHandler(event, FISHER.get())

        registerHandler(event, ENI.get())

        registerHandler(event, MOB_CAPTURER.get())
        registerHandler(event, TELEPAD.get())

        registerHandler(event, CEU.get())
        // Storage
        for (type: HTDeferredBlockEntityType<HTCrateBlockEntity> in CRATES.values) {
            registerHandler(event, type.get())
        }
        registerHandler(event, OPEN_CRATE.get())
        for (type: HTDeferredBlockEntityType<HTDrumBlockEntity> in DRUMS.values) {
            registerHandler(event, type.get())
        }
        registerHandler(event, EXP_DRUM.get())

        RagiumAPI.LOGGER.info("Registered Block Capabilities!")
    }

    @JvmStatic
    private fun registerHandler(event: RegisterCapabilitiesEvent, type: BlockEntityType<out HTBlockEntity>) {
        event.registerBlockEntity(HTItemCapabilities.block, type, HTHandlerProvider::getItemHandler)
        event.registerBlockEntity(HTFluidCapabilities.block, type, HTHandlerProvider::getFluidHandler)
        event.registerBlockEntity(HTEnergyCapabilities.block, type, HTHandlerProvider::getEnergyStorage)
    }
}
