package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.api.event.HTModifyPropertyEvent
import hiiragi283.ragium.api.event.HTRegisterMaterialEvent
import hiiragi283.ragium.api.extension.asServerLevel
import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.property.HTMachineParticleHandler
import hiiragi283.ragium.api.material.HTMaterialPropertyKeys
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.common.block.generator.HTCombustionGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTSolarGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTStirlingGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTThermalGeneratorBlockEntity
import hiiragi283.ragium.common.block.machine.HTFisherBlockEntity
import hiiragi283.ragium.common.block.processor.*
import hiiragi283.ragium.common.init.*
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.registries.Registries
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.CampfireBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent
import org.slf4j.Logger
import java.util.function.Supplier

@EventBusSubscriber(modid = RagiumAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
internal object RagiumModEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun modifyMachineProperties(event: HTModifyPropertyEvent.Machine) {
        // Consumer
        event
            .getBuilder(RagiumMachineKeys.FISHER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTFisherBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.FISHING_BOBBER_SPLASH)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofSimple(ParticleTypes.BUBBLE))

        // Generator
        event
            .getBuilder(RagiumMachineKeys.COMBUSTION_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTCombustionGeneratorBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.FIRE_EXTINGUISH)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofSimple(ParticleTypes.ASH))

        event
            .getBuilder(RagiumMachineKeys.SOLAR_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTSolarGeneratorBlockEntity)

        event
            .getBuilder(RagiumMachineKeys.STIRLING_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTStirlingGeneratorBlockEntity)

        event
            .getBuilder(RagiumMachineKeys.THERMAL_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTThermalGeneratorBlockEntity)

        event.getBuilder(RagiumMachineKeys.VIBRATION_GENERATOR)

        // Processor
        event
            .getBuilder(RagiumMachineKeys.ASSEMBLER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTAssemblerBlockEntity)

        event
            .getBuilder(RagiumMachineKeys.BLAST_FURNACE)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTBlastFurnaceBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLAZE_AMBIENT)
            .put(
                HTMachinePropertyKeys.PARTICLE,
                HTMachineParticleHandler { level: Level, pos: BlockPos, random: RandomSource, front: Direction ->
                    CampfireBlock.makeParticles(level, pos.relative(front.opposite, 2), false, false)
                },
            )

        event
            .getBuilder(RagiumMachineKeys.COMPRESSOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTCompressorBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.ANVIL_USE)

        event
            .getBuilder(RagiumMachineKeys.GRINDER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTGrinderBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.GRINDSTONE_USE)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofMiddle(ParticleTypes.CRIT))

        event
            .getBuilder(RagiumMachineKeys.MULTI_SMELTER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTMultiSmelterBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLAZE_AMBIENT)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofFront(ParticleTypes.SOUL_FIRE_FLAME))
        // Advanced
        event
            .getBuilder(RagiumMachineKeys.EXTRACTOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTExtractorBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.PISTON_EXTEND)

        event
            .getBuilder(RagiumMachineKeys.INFUSER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTInfuserBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BREWING_STAND_BREW)

        event
            .getBuilder(RagiumMachineKeys.MIXER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTMixerBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.PLAYER_SWIM)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofTop(ParticleTypes.BUBBLE_POP))

        event
            .getBuilder(RagiumMachineKeys.REFINERY)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTRefineryBlockEntity)
        // Elite
        event
            .getBuilder(RagiumMachineKeys.LASER_ASSEMBLY)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTLaserAssemblyBlockEntity)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun registerMaterial(event: HTRegisterMaterialEvent) {
        event.register(CommonMaterials.ALUMINA, HTMaterialType.DUST)
        event.register(CommonMaterials.ALUMINUM, HTMaterialType.METAL)
        event.register(CommonMaterials.ANTIMONY, HTMaterialType.METAL)
        event.register(CommonMaterials.BERYLLIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.ASH, HTMaterialType.DUST)
        event.register(CommonMaterials.BAUXITE, HTMaterialType.MINERAL)
        event.register(CommonMaterials.BRASS, HTMaterialType.ALLOY)
        event.register(CommonMaterials.BRONZE, HTMaterialType.ALLOY)
        event.register(CommonMaterials.CADMIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.CARBON, HTMaterialType.DUST)
        event.register(CommonMaterials.CHROMIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.CRYOLITE, HTMaterialType.GEM)
        event.register(CommonMaterials.ELECTRUM, HTMaterialType.ALLOY)
        event.register(CommonMaterials.FLUORITE, HTMaterialType.GEM)
        event.register(CommonMaterials.INVAR, HTMaterialType.ALLOY)
        event.register(CommonMaterials.IRIDIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.LEAD, HTMaterialType.METAL)
        event.register(CommonMaterials.NICKEL, HTMaterialType.METAL)
        event.register(CommonMaterials.OSMIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.PERIDOT, HTMaterialType.GEM)
        event.register(CommonMaterials.PLATINUM, HTMaterialType.METAL)
        event.register(CommonMaterials.PLUTONIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.RUBY, HTMaterialType.GEM)
        event.register(CommonMaterials.SALT, HTMaterialType.MINERAL)
        event.register(CommonMaterials.SALTPETER, HTMaterialType.MINERAL)
        event.register(CommonMaterials.SAPPHIRE, HTMaterialType.GEM)
        event.register(CommonMaterials.SILICON, HTMaterialType.METAL)
        event.register(CommonMaterials.SILVER, HTMaterialType.METAL)
        event.register(CommonMaterials.STAINLESS_STEEL, HTMaterialType.ALLOY)
        event.register(CommonMaterials.STEEL, HTMaterialType.ALLOY)
        event.register(CommonMaterials.SULFUR, HTMaterialType.MINERAL)
        event.register(CommonMaterials.TIN, HTMaterialType.METAL)
        event.register(CommonMaterials.TITANIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.TUNGSTEN, HTMaterialType.METAL)
        event.register(CommonMaterials.URANIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.WOOD, HTMaterialType.DUST)
        event.register(CommonMaterials.ZINC, HTMaterialType.METAL)

        event.register(RagiumMaterials.DEEP_STEEL, HTMaterialType.ALLOY)
        event.register(RagiumMaterials.ECHORIUM, HTMaterialType.METAL)
        event.register(RagiumMaterials.FIERY_COAL, HTMaterialType.GEM)
        event.register(RagiumMaterials.RAGI_ALLOY, HTMaterialType.ALLOY)
        event.register(RagiumMaterials.RAGI_CRYSTAL, HTMaterialType.GEM)
        event.register(RagiumMaterials.RAGINITE, HTMaterialType.MINERAL)
        event.register(RagiumMaterials.RAGIUM, HTMaterialType.METAL)

        event.register(VanillaMaterials.AMETHYST, HTMaterialType.GEM)
        event.register(VanillaMaterials.COAL, HTMaterialType.GEM)
        event.register(VanillaMaterials.COPPER, HTMaterialType.METAL)
        event.register(VanillaMaterials.DIAMOND, HTMaterialType.GEM)
        event.register(VanillaMaterials.EMERALD, HTMaterialType.GEM)
        event.register(VanillaMaterials.GOLD, HTMaterialType.METAL)
        event.register(VanillaMaterials.IRON, HTMaterialType.METAL)
        event.register(VanillaMaterials.LAPIS, HTMaterialType.GEM)
        event.register(VanillaMaterials.NETHERITE, HTMaterialType.ALLOY)
        event.register(VanillaMaterials.NETHERITE_SCRAP, HTMaterialType.GEM)
        event.register(VanillaMaterials.QUARTZ, HTMaterialType.GEM)
        event.register(VanillaMaterials.REDSTONE, HTMaterialType.MINERAL)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun modifyMaterialProperties(event: HTModifyPropertyEvent.Material) {
        // Grinder Ore Count
        event
            .getBuilder(VanillaMaterials.LAPIS)
            .put(HTMaterialPropertyKeys.GRINDER_RAW_COUNT, 4)

        event
            .getBuilder(VanillaMaterials.REDSTONE)
            .put(HTMaterialPropertyKeys.GRINDER_RAW_COUNT, 2)
    }

    @SubscribeEvent
    fun createRegistry(event: NewRegistryEvent) {
        event.register(RagiumRegistries.ITEM_RESULT)
        event.register(RagiumRegistries.MULTIBLOCK_COMPONENT_TYPE)

        LOGGER.info("Registered new registries!")
    }

    @SubscribeEvent
    fun addBlockToBlockEntity(event: BlockEntityTypeAddBlocksEvent) {
        LOGGER.info("Added external blocks to BlockEntityType!")
    }

    @SubscribeEvent
    fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        // from HTBlockEntityHandlerProvider
        fun <T> registerHandlers(supplier: Supplier<BlockEntityType<T>>) where T : BlockEntity, T : HTHandlerBlockEntity {
            val type: BlockEntityType<T> = supplier.get()
            event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                type,
                HTHandlerBlockEntity::getItemHandler,
            )
            event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                type,
                HTHandlerBlockEntity::getFluidHandler,
            )
            event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                type,
                HTHandlerBlockEntity::getEnergyStorage,
            )
        }

        registerHandlers(RagiumBlockEntityTypes.MANUAL_GRINDER)
        registerHandlers(RagiumBlockEntityTypes.PRIMITIVE_BLAST_FURNACE)

        registerHandlers(RagiumBlockEntityTypes.FISHER)

        registerHandlers(RagiumBlockEntityTypes.COMBUSTION_GENERATOR)
        registerHandlers(RagiumBlockEntityTypes.STIRLING_GENERATOR)
        registerHandlers(RagiumBlockEntityTypes.THERMAL_GENERATOR)

        registerHandlers(RagiumBlockEntityTypes.ASSEMBLER)
        registerHandlers(RagiumBlockEntityTypes.BLAST_FURNACE)
        registerHandlers(RagiumBlockEntityTypes.COMPRESSOR)
        registerHandlers(RagiumBlockEntityTypes.EXTRACTOR)
        registerHandlers(RagiumBlockEntityTypes.GRINDER)
        registerHandlers(RagiumBlockEntityTypes.INFUSER)
        registerHandlers(RagiumBlockEntityTypes.LASER_ASSEMBLY)
        registerHandlers(RagiumBlockEntityTypes.MIXER)
        registerHandlers(RagiumBlockEntityTypes.MULTI_SMELTER)
        registerHandlers(RagiumBlockEntityTypes.REFINERY)

        registerHandlers(RagiumBlockEntityTypes.CATALYST_ADDON)
        registerHandlers(RagiumBlockEntityTypes.DRUM)
        registerHandlers(RagiumBlockEntityTypes.SLAG_COLLECTOR)

        // Other
        event.registerBlock(
            Capabilities.EnergyStorage.BLOCK,
            { level: Level, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction ->
                level.asServerLevel()?.let(RagiumAPI.getInstance()::getEnergyNetwork)
            },
            RagiumBlocks.ENERGY_NETWORK_INTERFACE.get(),
        )

        LOGGER.info("Registered Block Capabilities!")
    }

    @SubscribeEvent
    fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            { stack: ItemStack, _: Void? ->
                val enchLevel: Int =
                    stack.getLevel(RagiumAPI.getInstance().getCurrentLookup(), RagiumEnchantments.CAPACITY)
                FluidHandlerItemStack(
                    RagiumComponentTypes.FLUID_CONTENT,
                    stack,
                    RagiumAPI.DEFAULT_TANK_CAPACITY * (enchLevel + 1),
                )
            },
            RagiumBlocks.COPPER_DRUM,
        )

        LOGGER.info("Registered Item Capabilities!")
    }

    @SubscribeEvent
    fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
        event.register(RagiumDataMaps.DEFOLIANT)
        event.register(RagiumDataMaps.EXECUTIONER_DROPS)
        event.register(RagiumDataMaps.SOAP)

        LOGGER.info("Registered Data Map Types!")
    }

    @SubscribeEvent
    fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        LOGGER.info("Modified item components!")
    }

    @SubscribeEvent
    fun buildCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        // Add Potion Can
        if (event.tabKey == CreativeModeTabs.FOOD_AND_DRINKS) {
            val parameters: CreativeModeTab.ItemDisplayParameters = event.parameters
            parameters
                .holders
                .lookupOrThrow(Registries.POTION)
                .listElements()
                .filter { it.value().isEnabled(parameters.enabledFeatures) }
                .map { PotionContents.createItemStack(RagiumItems.POTION_CAN.asItem(), it) }
                .forEach(event::accept)
            LOGGER.info("Modified existing Creative Tags!")
        }
    }
}
