package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntityFactory
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.registry.HTFluidContent
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
import hiiragi283.ragium.common.block.entity.generator.HTFuelGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTNuclearReactorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTSolarGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBlockBreakerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBreweryBlockEntity
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
import hiiragi283.ragium.common.variant.HTCrateVariant
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTDrumVariant
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.common.variant.HTMachineVariant
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent
import java.util.function.Supplier

object RagiumBlockEntityTypes {
    @JvmField
    val REGISTER = HTDeferredBlockEntityTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        eventBus.addListener(::addSupportedBlock)
        eventBus.addListener(::registerBlockCapabilities)
    }

    @JvmStatic
    private fun <BE : HTBlockEntity> registerTick(name: String, factory: HTBlockEntityFactory<BE>): HTDeferredBlockEntityType<BE> =
        REGISTER.registerType(name, factory, HTBlockEntity::tickClient, HTBlockEntity::tickServer)

    //    Generator    //

    @JvmField
    val THERMAL: HTDeferredBlockEntityType<HTFuelGeneratorBlockEntity> = generator(
        HTGeneratorVariant.Fuel.THERMAL,
        HTFuelGeneratorBlockEntity.createSimple(
            { stack: ImmutableItemStack -> stack.stack.getBurnTime(null) / 10 },
            HTFluidContent.LAVA,
            RagiumDataMaps.INSTANCE::getThermalFuel,
            HTGeneratorVariant.Fuel.THERMAL,
        ),
    )

    @JvmField
    val COMBUSTION: HTDeferredBlockEntityType<HTFuelGeneratorBlockEntity> = generator(
        HTGeneratorVariant.Fuel.COMBUSTION,
        HTFuelGeneratorBlockEntity.createSimple(
            { stack: ImmutableItemStack ->
                when {
                    stack.isOf(ItemTags.COALS) -> 100
                    else -> 0
                }
            },
            RagiumFluidContents.CRUDE_OIL,
            RagiumDataMaps.INSTANCE::getCombustionFuel,
            HTGeneratorVariant.Fuel.COMBUSTION,
        ),
    )

    @JvmField
    val SOLAR: HTDeferredBlockEntityType<HTSolarGeneratorBlockEntity> = generator(
        HTGeneratorVariant.Solar,
        ::HTSolarGeneratorBlockEntity,
    )

    @JvmField
    val NUCLEAR: HTDeferredBlockEntityType<HTNuclearReactorBlockEntity> = generator(
        HTGeneratorVariant.Nuclear,
        ::HTNuclearReactorBlockEntity,
    )

    @JvmStatic
    private fun <BE : HTGeneratorBlockEntity> generator(
        variant: HTGeneratorVariant<*, BE>,
        factory: HTBlockEntityFactory<BE>,
    ): HTDeferredBlockEntityType<BE> = registerTick(variant.variantName(), factory)

    //    Machine    //

    @JvmField
    val MACHINES: Map<HTMachineVariant, HTDeferredBlockEntityType<HTBlockEntity>> =
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
    val CRATES: Map<HTCrateVariant, HTDeferredBlockEntityType<HTCrateBlockEntity>> =
        HTCrateVariant.entries.associateWith { variant: HTCrateVariant ->
            val factory = when (variant) {
                HTCrateVariant.SMALL -> HTCrateBlockEntity::Small
                HTCrateVariant.MEDIUM -> HTCrateBlockEntity::Medium
                HTCrateVariant.LARGE -> HTCrateBlockEntity::Large
                HTCrateVariant.HUGE -> HTCrateBlockEntity::Huge
            }
            registerTick("${variant.variantName()}_crate", factory)
        }

    @JvmField
    val DRUMS: Map<HTDrumVariant, HTDeferredBlockEntityType<HTDrumBlockEntity>> =
        HTDrumVariant.entries.associateWith { variant: HTDrumVariant ->
            val factory = when (variant) {
                HTDrumVariant.SMALL -> HTDrumBlockEntity::Small
                HTDrumVariant.MEDIUM -> HTDrumBlockEntity::Medium
                HTDrumVariant.LARGE -> HTDrumBlockEntity::Large
                HTDrumVariant.HUGE -> HTDrumBlockEntity::Huge
            }
            registerTick("${variant.variantName()}_drum", factory)
        }

    //    Event    //

    // Supported Blocks
    @JvmStatic
    private fun addSupportedBlock(event: BlockEntityTypeAddBlocksEvent) {
        addAll(event, HTGeneratorVariant.entries)
        addAll(event, HTMachineVariant.entries)

        addAll(event, HTDeviceVariant.entries)

        addAll(event, HTCrateVariant.entries)
        addAll(event, HTDrumVariant.entries)

        RagiumAPI.LOGGER.info("Added supported blocks to BlockEntityType!")
    }

    @JvmStatic
    private fun add(event: BlockEntityTypeAddBlocksEvent, type: HTDeferredBlockEntityType<*>, block: Supplier<out Block>) {
        event.modify(type.get(), block.get())
    }

    @JvmStatic
    private fun <V : HTVariantKey.WithBlockAndBE<*, *>> addAll(event: BlockEntityTypeAddBlocksEvent, entries: Iterable<V>) {
        for (variant: V in entries) {
            add(event, variant.blockEntityHolder, variant.blockHolder)
        }
    }

    // Capabilities
    @JvmStatic
    private fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        registerHandlers(event, HTGeneratorVariant.entries.map(HTGeneratorVariant<*, *>::blockEntityHolder))
        registerHandlers(event, MACHINES.values)

        registerHandlers(event, DEVICES.values)

        registerHandlers(event, CRATES.values)
        registerHandlers(event, DRUMS.values)

        RagiumAPI.LOGGER.info("Registered Block Capabilities!")
    }

    @JvmStatic
    private fun registerHandlers(event: RegisterCapabilitiesEvent, types: Iterable<HTDeferredBlockEntityType<out HTBlockEntity>>) {
        for (type: HTDeferredBlockEntityType<out HTBlockEntity> in types) {
            val type1: BlockEntityType<out HTBlockEntity> = type.get()
            event.registerBlockEntity(
                RagiumCapabilities.ITEM.blockCapability(),
                type1,
                HTHandlerProvider::getItemHandler,
            )
            event.registerBlockEntity(
                RagiumCapabilities.FLUID.blockCapability(),
                type1,
                HTHandlerProvider::getFluidHandler,
            )
            event.registerBlockEntity(
                RagiumCapabilities.ENERGY.blockCapability(),
                type1,
                HTHandlerProvider::getEnergyStorage,
            )
        }
    }
}
