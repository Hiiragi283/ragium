package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntityFactory
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityTypeRegister
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.isOf
import hiiragi283.ragium.api.storage.HTHandlerProvider
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTDeviceBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTDimensionalAnchorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTEnergyNetworkAccessBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTExpCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTItemBufferBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTLavaCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTMilkCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTMobCapturerBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTTelepadBlockentity
import hiiragi283.ragium.common.block.entity.device.HTWaterCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTEnchGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTFuelGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTNuclearReactorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTSolarGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBlockBreakerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBreweryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTConsumerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCuttingMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPlanterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSimulatorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSingleItemInputBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTWasherBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTDrumBlockEntity
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTMachineVariant
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.block.entity.BlockEntityType
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

    @JvmField
    val THERMAL_GENERATOR: HTDeferredBlockEntityType<HTFuelGeneratorBlockEntity> = registerTick(
        "thermal_generator",
        HTFuelGeneratorBlockEntity.createSimple(
            { stack: ImmutableItemStack -> stack.stack.getBurnTime(null) / 10 },
            HTFluidContent.LAVA,
            RagiumDataMaps.INSTANCE::getThermalFuel,
            RagiumBlocks.THERMAL_GENERATOR,
        ),
    )

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

    @JvmField
    val ENCHANTMENT_GENERATOR: HTDeferredBlockEntityType<HTFuelGeneratorBlockEntity> = registerTick(
        "enchantment_generator",
        ::HTEnchGeneratorBlockEntity,
    )

    @JvmField
    val SOLAR_PANEL_CONTROLLER: HTDeferredBlockEntityType<HTSolarGeneratorBlockEntity> = registerTick(
        "solar_panel_controller",
        ::HTSolarGeneratorBlockEntity,
    )

    @JvmField
    val NUCLEAR_REACTOR: HTDeferredBlockEntityType<HTNuclearReactorBlockEntity> = registerTick(
        "nuclear_reactor",
        ::HTNuclearReactorBlockEntity,
    )

    //    Machine    //

    @JvmField
    val MACHINES: Map<HTMachineVariant, HTDeferredBlockEntityType<HTConsumerBlockEntity>> =
        HTMachineVariant.entries.associateWith { variant: HTMachineVariant ->
            val factory = when (variant) {
                // Basic
                HTMachineVariant.ALLOY_SMELTER -> ::HTAlloySmelterBlockEntity
                HTMachineVariant.BLOCK_BREAKER -> ::HTBlockBreakerBlockEntity
                HTMachineVariant.COMPRESSOR -> HTSingleItemInputBlockEntity.createSimple(
                    RagiumMenuTypes.COMPRESSOR,
                    SoundEvents.ANVIL_PLACE,
                    0.25f to 0.5f,
                    RagiumRecipeTypes.COMPRESSING,
                    variant,
                )
                HTMachineVariant.CUTTING_MACHINE -> ::HTCuttingMachineBlockEntity
                HTMachineVariant.EXTRACTOR -> HTSingleItemInputBlockEntity.createSimple(
                    RagiumMenuTypes.EXTRACTOR,
                    SoundEvents.SPONGE_ABSORB,
                    1f to 0.5f,
                    RagiumRecipeTypes.EXTRACTING,
                    variant,
                )
                HTMachineVariant.PULVERIZER -> HTSingleItemInputBlockEntity.createSimple(
                    RagiumMenuTypes.PULVERIZER,
                    SoundEvents.GRINDSTONE_USE,
                    0.25f to 1f,
                    RagiumRecipeTypes.CRUSHING,
                    variant,
                )

                // Advanced
                HTMachineVariant.CRUSHER -> ::HTCrusherBlockEntity
                HTMachineVariant.MELTER -> ::HTMelterBlockEntity
                HTMachineVariant.REFINERY -> ::HTRefineryBlockEntity
                HTMachineVariant.WASHER -> ::HTWasherBlockEntity
                // Elite
                HTMachineVariant.BREWERY -> ::HTBreweryBlockEntity
                HTMachineVariant.MULTI_SMELTER -> ::HTMultiSmelterBlockEntity
                HTMachineVariant.PLANTER -> ::HTPlanterBlockEntity
                HTMachineVariant.SIMULATOR -> ::HTSimulatorBlockEntity
            }
            registerTick(variant.variantName(), factory)
        }

    //    Device    //

    @JvmField
    val DEVICES: Map<HTDeviceVariant, HTDeferredBlockEntityType<HTDeviceBlockEntity>> =
        HTDeviceVariant.entries.associateWith { variant: HTDeviceVariant ->
            val factory = when (variant) {
                // Basic
                HTDeviceVariant.ITEM_BUFFER -> ::HTItemBufferBlockEntity
                HTDeviceVariant.MILK_COLLECTOR -> ::HTMilkCollectorBlockEntity
                HTDeviceVariant.WATER_COLLECTOR -> ::HTWaterCollectorBlockEntity

                // Advanced
                HTDeviceVariant.ENI -> HTEnergyNetworkAccessBlockEntity::Simple
                HTDeviceVariant.EXP_COLLECTOR -> ::HTExpCollectorBlockEntity
                HTDeviceVariant.LAVA_COLLECTOR -> ::HTLavaCollectorBlockEntity

                // Elite
                HTDeviceVariant.DIM_ANCHOR -> ::HTDimensionalAnchorBlockEntity
                HTDeviceVariant.MOB_CAPTURER -> ::HTMobCapturerBlockEntity

                // Ultimate
                HTDeviceVariant.TELEPAD -> ::HTTelepadBlockentity

                // Creative
                HTDeviceVariant.CEU -> HTEnergyNetworkAccessBlockEntity::Creative
            }
            registerTick(variant.variantName(), factory)
        }

    //    Storage    //

    @JvmField
    val CRATES: Map<HTCrateTier, HTDeferredBlockEntityType<HTCrateBlockEntity>> =
        HTCrateTier.entries.associateWith { tier: HTCrateTier ->
            val factory = when (tier) {
                HTCrateTier.SMALL -> HTCrateBlockEntity::Small
                HTCrateTier.MEDIUM -> HTCrateBlockEntity::Medium
                HTCrateTier.LARGE -> HTCrateBlockEntity::Large
                HTCrateTier.HUGE -> HTCrateBlockEntity::Huge
            }
            registerTick(tier.path, factory)
        }

    @JvmField
    val DRUMS: Map<HTDrumTier, HTDeferredBlockEntityType<HTDrumBlockEntity>> =
        HTDrumTier.entries.associateWith { tier: HTDrumTier ->
            val factory = when (tier) {
                HTDrumTier.SMALL -> HTDrumBlockEntity::Small
                HTDrumTier.MEDIUM -> HTDrumBlockEntity::Medium
                HTDrumTier.LARGE -> HTDrumBlockEntity::Large
                HTDrumTier.HUGE -> HTDrumBlockEntity::Huge
            }
            registerTick(tier.path, factory)
        }

    //    Event    //

    // Supported Blocks
    @JvmStatic
    private fun addSupportedBlocks(event: BlockEntityTypeAddBlocksEvent) {
        addSupportedBlock(event, THERMAL_GENERATOR, RagiumBlocks.THERMAL_GENERATOR)
        addSupportedBlock(event, COMBUSTION_GENERATOR, RagiumBlocks.COMBUSTION_GENERATOR)
        addSupportedBlock(event, ENCHANTMENT_GENERATOR, RagiumBlocks.ENCHANTMENT_GENERATOR)
        addSupportedBlock(event, SOLAR_PANEL_CONTROLLER, RagiumBlocks.SOLAR_PANEL_CONTROLLER)
        addSupportedBlock(event, NUCLEAR_REACTOR, RagiumBlocks.NUCLEAR_REACTOR)

        addAll(event, HTMachineVariant.entries)

        addAll(event, HTDeviceVariant.entries)

        // Storage
        for ((tier: HTCrateTier, type: HTDeferredBlockEntityType<HTCrateBlockEntity>) in CRATES) {
            addSupportedBlock(event, type, tier.getBlock())
        }
        for ((tier: HTDrumTier, type: HTDeferredBlockEntityType<HTDrumBlockEntity>) in DRUMS) {
            addSupportedBlock(event, type, tier.getBlock())
        }

        RagiumAPI.LOGGER.info("Added supported blocks to BlockEntityType!")
    }

    @JvmStatic
    fun addSupportedBlock(event: BlockEntityTypeAddBlocksEvent, type: HTDeferredBlockEntityType<*>, block: HTDeferredBlock<*, *>) {
        event.modify(type.get(), block.get())
    }

    @JvmStatic
    private fun <V : HTVariantKey.WithBlockAndBE<*, *>> addAll(event: BlockEntityTypeAddBlocksEvent, entries: Iterable<V>) {
        for (variant: V in entries) {
            addSupportedBlock(event, variant.blockEntityHolder, variant.blockHolder)
        }
    }

    // Capabilities
    @JvmStatic
    private fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        registerHandler(event, THERMAL_GENERATOR.get())
        registerHandler(event, COMBUSTION_GENERATOR.get())
        registerHandler(event, ENCHANTMENT_GENERATOR.get())
        registerHandler(event, SOLAR_PANEL_CONTROLLER.get())
        registerHandler(event, NUCLEAR_REACTOR.get())

        registerHandlers(event, MACHINES.values)

        registerHandlers(event, DEVICES.values)

        registerHandlers(event, CRATES.values)
        registerHandlers(event, DRUMS.values)

        RagiumAPI.LOGGER.info("Registered Block Capabilities!")
    }

    @JvmStatic
    private fun registerHandlers(event: RegisterCapabilitiesEvent, types: Iterable<HTDeferredBlockEntityType<out HTBlockEntity>>) {
        for (type: HTDeferredBlockEntityType<out HTBlockEntity> in types) {
            registerHandler(event, type.get())
        }
    }

    @JvmStatic
    private fun registerHandler(event: RegisterCapabilitiesEvent, type: BlockEntityType<out HTBlockEntity>) {
        event.registerBlockEntity(
            RagiumCapabilities.ITEM.blockCapability(),
            type,
            HTHandlerProvider::getItemHandler,
        )
        event.registerBlockEntity(
            RagiumCapabilities.FLUID.blockCapability(),
            type,
            HTHandlerProvider::getFluidHandler,
        )
        event.registerBlockEntity(
            RagiumCapabilities.ENERGY.blockCapability(),
            type,
            HTHandlerProvider::getEnergyStorage,
        )
    }
}
