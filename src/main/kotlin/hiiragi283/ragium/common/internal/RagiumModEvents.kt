package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.event.HTModifyPropertyEvent
import hiiragi283.ragium.api.event.HTRegisterMaterialEvent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.property.HTMachineParticleHandler
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPropertyKeys
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.world.energyNetwork
import hiiragi283.ragium.common.block.generator.HTCombustionGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTSolarGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTThermalGeneratorBlockEntity
import hiiragi283.ragium.common.block.machine.*
import hiiragi283.ragium.common.init.*
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
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
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack
import net.neoforged.neoforge.registries.DeferredItem
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
            .getBuilder(RagiumMachineKeys.BEDROCK_MINER)
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.BEDROCK_MINER)

        // Generator
        event
            .getBuilder(RagiumMachineKeys.COMBUSTION_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTCombustionGeneratorBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.FIRE_EXTINGUISH)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofSimple(ParticleTypes.ASH))

        event
            .getBuilder(RagiumMachineKeys.NUCLEAR_REACTOR)

        event
            .getBuilder(RagiumMachineKeys.SOLAR_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTSolarGeneratorBlockEntity)
            .put(HTMachinePropertyKeys.GENERATOR_PREDICATE) { level: Level, pos: BlockPos -> level.canSeeSky(pos.above()) && level.isDay }
            .put(HTMachinePropertyKeys.ROTATION_MAPPER, constFunction2(Direction.NORTH))

        event
            .getBuilder(RagiumMachineKeys.THERMAL_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTThermalGeneratorBlockEntity)

        event.getBuilder(RagiumMachineKeys.VIBRATION_GENERATOR)

        // Processor
        RagiumMachineKeys.PROCESSORS
            .map(event::getBuilder)
            .forEach { builder: HTPropertyHolderBuilder ->
                builder
                    .put(
                        HTMachinePropertyKeys.PARTICLE,
                        HTMachineParticleHandler.ofSimple(ParticleTypes.ELECTRIC_SPARK),
                    )
            }

        event
            .getBuilder(RagiumMachineKeys.ASSEMBLER)

        event
            .getBuilder(RagiumMachineKeys.BLAST_FURNACE)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTBlastFurnaceBlockEntity)
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.BLAST_FURNACE)
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
            .getBuilder(RagiumMachineKeys.EXTRACTOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTExtractorBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.PISTON_EXTEND)

        event
            .getBuilder(RagiumMachineKeys.GRINDER)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.GRINDSTONE_USE)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofMiddle(ParticleTypes.CRIT))

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
            .getBuilder(RagiumMachineKeys.MULTI_SMELTER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTMultiSmelterBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLAZE_AMBIENT)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofFront(ParticleTypes.SOUL_FIRE_FLAME))

        event
            .getBuilder(RagiumMachineKeys.REFINERY)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTRefineryBlockEntity)
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
        event.register(RagiumMaterials.RAGI_STEEL, HTMaterialType.ALLOY)
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
        event.register(RagiumAPI.Registries.MULTIBLOCK_COMPONENT_TYPE)
        event.register(RagiumAPI.Registries.RECIPE_CONDITION)

        LOGGER.info("Registered new registries!")
    }

    @SubscribeEvent
    fun addBlockToBlockEntity(event: BlockEntityTypeAddBlocksEvent) {
        LOGGER.info("Added external blocks to BlockEntityType!")
    }

    @SubscribeEvent
    fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        RagiumAPI.machineRegistry.blockMap.values.forEach { content: HTBlockContent ->
            event.registerBlock(
                RagiumAPI.BlockCapabilities.CONTROLLER_HOLDER,
                { _: Level, _: BlockPos, _: BlockState, blockEntity: BlockEntity?, _: Direction -> blockEntity as? HTControllerHolder },
                content.get(),
            )
        }

        fun registerTier(contents: Iterable<HTBlockContent>) {
            event.registerBlock(
                RagiumAPI.BlockCapabilities.MACHINE_TIER,
                { _: Level, _: BlockPos, state: BlockState, _: BlockEntity?, _: Void? ->
                    state.getItemData(RagiumAPI.DataMapTypes.MACHINE_TIER)
                },
                *contents.map(HTBlockContent::get).toTypedArray(),
            )
        }

        registerTier(RagiumBlocks.Grates.entries)
        registerTier(RagiumBlocks.Casings.entries)
        registerTier(RagiumBlocks.Burners.entries)

        registerTier(RagiumBlocks.Drums.entries)

        // from HTBlockEntityHandlerProvider
        fun <T> registerHandlers(supplier: Supplier<BlockEntityType<T>>) where T : BlockEntity, T : HTBlockEntityHandlerProvider {
            val type: BlockEntityType<T> = supplier.get()
            event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                type,
                HTBlockEntityHandlerProvider::getItemHandler,
            )
            event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                type,
                HTBlockEntityHandlerProvider::getFluidHandler,
            )
            event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                type,
                HTBlockEntityHandlerProvider::getEnergyStorage,
            )
        }

        registerHandlers(RagiumBlockEntityTypes.MANUAL_GRINDER)
        registerHandlers(RagiumBlockEntityTypes.PRIMITIVE_BLAST_FURNACE)

        registerHandlers(RagiumBlockEntityTypes.COMBUSTION_GENERATOR)
        registerHandlers(RagiumBlockEntityTypes.THERMAL_GENERATOR)

        registerHandlers(RagiumBlockEntityTypes.BLAST_FURNACE)
        registerHandlers(RagiumBlockEntityTypes.EXTRACTOR)
        registerHandlers(RagiumBlockEntityTypes.REFINERY)
        registerHandlers(RagiumBlockEntityTypes.MULTI_SMELTER)

        registerHandlers(RagiumBlockEntityTypes.CATALYST_ADDON)
        registerHandlers(RagiumBlockEntityTypes.DRUM)
        registerHandlers(RagiumBlockEntityTypes.SLAG_COLLECTOR)

        // Other
        event.registerBlock(
            Capabilities.EnergyStorage.BLOCK,
            { level: Level, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction ->
                level.asServerLevel()?.energyNetwork
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
                val tier: HTMachineTier =
                    stack.getItemData(RagiumAPI.DataMapTypes.MACHINE_TIER) ?: return@registerItem null
                FluidHandlerItemStack.SwapEmpty(
                    RagiumComponentTypes.FLUID_CONTENT,
                    stack,
                    ItemStack(stack.item),
                    tier.tankCapacity,
                )
            },
            *RagiumBlocks.Drums.entries.toTypedArray(),
        )

        LOGGER.info("Registered Item Capabilities!")
    }

    @SubscribeEvent
    fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
        event.register(RagiumAPI.DataMapTypes.MACHINE_KEY)
        event.register(RagiumAPI.DataMapTypes.MACHINE_TIER)
        event.register(RagiumAPI.DataMapTypes.RADIOACTIVES)

        LOGGER.info("Registered Data Map Types!")
    }

    @SubscribeEvent
    fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        // Item
        RagiumItems.MATERIAL_ITEMS.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, holder: DeferredItem<out Item>) ->
            event.modify(holder) { builder: DataComponentPatch.Builder -> builder.name(prefix.createText(key)) }
        }

        LOGGER.info("Modified item components!")
    }
}
