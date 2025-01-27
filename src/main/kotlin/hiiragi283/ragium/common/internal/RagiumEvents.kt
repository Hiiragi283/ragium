package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.event.HTModifyPropertyEvent
import hiiragi283.ragium.api.event.HTRegisterMaterialEvent
import hiiragi283.ragium.api.extension.asServerLevel
import hiiragi283.ragium.api.extension.getItemData
import hiiragi283.ragium.api.extension.name
import hiiragi283.ragium.api.extension.set
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.machine.property.HTMachineEntityFactory
import hiiragi283.ragium.api.machine.property.HTMachineParticleHandler
import hiiragi283.ragium.api.machine.property.HTMachineRecipeProxy
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeValidator
import hiiragi283.ragium.api.world.energyNetwork
import hiiragi283.ragium.common.block.generator.HTDefaultGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTFluidGeneratorBlockEntity
import hiiragi283.ragium.common.block.processor.HTDefaultProcessorBlockEntity
import hiiragi283.ragium.common.block.processor.HTDistillationTowerBlockEntity
import hiiragi283.ragium.common.block.processor.HTLargeProcessorBlockEntity
import hiiragi283.ragium.common.block.processor.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.recipe.HTMachineConverters
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent
import org.slf4j.Logger
import java.util.function.BiPredicate
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

@EventBusSubscriber(modid = RagiumAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
internal object RagiumEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun modifyMachineProperties(event: HTModifyPropertyEvent.Machine) {
        fun HTPropertyHolderBuilder.putFactory(
            factory: BlockEntityType.BlockEntitySupplier<out HTMachineBlockEntity>,
        ): HTPropertyHolderBuilder = put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(factory::create))

        fun HTPropertyHolderBuilder.putFactory(factory: HTMachineEntityFactory): HTPropertyHolderBuilder =
            put(HTMachinePropertyKeys.MACHINE_FACTORY, factory)

        fun HTPropertyHolderBuilder.putValidator(validator: HTMachineRecipeValidator): HTPropertyHolderBuilder =
            put(HTMachinePropertyKeys.RECIPE_VALIDATOR, validator)

        // Consumer

        // Generator
        event
            .getBuilder(RagiumMachineKeys.COMBUSTION_GENERATOR)
            .putFactory(::HTFluidGeneratorBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.FIRE_EXTINGUISH)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofSimple(ParticleTypes.ASH))

        event
            .getBuilder(RagiumMachineKeys.GAS_TURBINE)
            .putFactory(::HTFluidGeneratorBlockEntity)

        event
            .getBuilder(RagiumMachineKeys.NUCLEAR_REACTOR)
            .putFactory(::HTFluidGeneratorBlockEntity)

        event
            .getBuilder(RagiumMachineKeys.SOLAR_GENERATOR)
            .putFactory(::HTDefaultGeneratorBlockEntity)
            .put(
                HTMachinePropertyKeys.GENERATOR_PREDICATE,
                BiPredicate { level: Level, pos: BlockPos -> level.canSeeSky(pos.above()) && level.isDay },
            ).put(
                HTMachinePropertyKeys.BLOCK_MODEL_MAPPER,
                Function { key: HTMachineKey -> RagiumAPI.id("block/solar_panel") },
            ).put(HTMachinePropertyKeys.ROTATION_MAPPER, UnaryOperator { Direction.NORTH })

        event.getBuilder(RagiumMachineKeys.STEAM_GENERATOR)

        event
            .getBuilder(RagiumMachineKeys.THERMAL_GENERATOR)
            .putFactory(::HTFluidGeneratorBlockEntity)

        event.getBuilder(RagiumMachineKeys.VIBRATION_GENERATOR)

        // Processor
        RagiumMachineKeys.PROCESSORS
            .map(event::getBuilder)
            .forEach { builder: HTPropertyHolderBuilder ->
                builder
                    .putFactory(::HTDefaultProcessorBlockEntity)
                    .put(
                        HTMachinePropertyKeys.PARTICLE,
                        HTMachineParticleHandler.ofSimple(ParticleTypes.ELECTRIC_SPARK),
                    )
            }

        event
            .getBuilder(RagiumMachineKeys.ASSEMBLER)
            .putFactory(::HTLargeProcessorBlockEntity)

        event
            .getBuilder(RagiumMachineKeys.BLAST_FURNACE)
            .putFactory(::HTLargeProcessorBlockEntity)
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.BLAST_FURNACE)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLAZE_AMBIENT)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofFront(ParticleTypes.FLAME))

        event
            .getBuilder(RagiumMachineKeys.CHEMICAL_REACTOR)
            .putFactory(::HTLargeProcessorBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BREWING_STAND_BREW)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofMiddle(ParticleTypes.BUBBLE_POP))
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.material(
                    true,
                    HTMachineConverters::chemicalOre3x,
                    HTMachineConverters::chemicalOre4x,
                    HTMachineConverters::chemicalOre5x,
                ),
            )

        event
            .getBuilder(RagiumMachineKeys.COKE_OVEN)
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.COKE_OVEN)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.SMOKER_SMOKE)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofFront(ParticleTypes.LARGE_SMOKE))

        event
            .getBuilder(RagiumMachineKeys.COMPRESSOR)
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.material(
                    true,
                    HTMachineConverters::compressorGear,
                    HTMachineConverters::compressorGem,
                    HTMachineConverters::compressorPlate,
                    HTMachineConverters::compressorRod,
                ),
            )

        event
            .getBuilder(RagiumMachineKeys.CUTTING_MACHINE)
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.convert(true, RecipeType.STONECUTTING, HTMachineConverters::fromCutting),
            )

        event
            .getBuilder(RagiumMachineKeys.DISTILLATION_TOWER)
            .putFactory(::HTDistillationTowerBlockEntity)
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.DISTILLATION_TOWER)
            .putValidator { recipe: HTMachineRecipe ->
                when {
                    recipe.itemInputs.isNotEmpty() -> DataResult.error { "Distillation tower recipe not accepts item inputs!" }
                    recipe.fluidInputs.size != 1 -> DataResult.error { "Distillation tower recipe should have only one fluid input!" }
                    // recipe.catalyst.isEmpty -> DataResult.error { "Distillation tower recipe requires catalyst!" }
                    recipe.getItemOutput(1) != null -> DataResult.error { "Distillation tower recipe should have one item output!" }
                    else -> DataResult.success(recipe)
                }
            }

        event
            .getBuilder(RagiumMachineKeys.EXTRACTOR)
            .put(HTMachinePropertyKeys.RECIPE_PROXY, HTMachineRecipeProxy(HTMachineConverters::fromComposting))

        event
            .getBuilder(RagiumMachineKeys.GRINDER)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.GRINDSTONE_USE)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofMiddle(ParticleTypes.CRIT))
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.material(
                    true,
                    HTMachineConverters::grinderMainToDust,
                    HTMachineConverters::grinderGearToDust,
                    HTMachineConverters::grinderPlateToDust,
                    HTMachineConverters::grinderRawToDust,
                    HTMachineConverters::grinderOreToRaw,
                ),
            )

        event.getBuilder(RagiumMachineKeys.GROWTH_CHAMBER)

        event.getBuilder(RagiumMachineKeys.LASER_TRANSFORMER)

        event
            .getBuilder(RagiumMachineKeys.MIXER)
            .putFactory(::HTLargeProcessorBlockEntity)
            .put(HTMachinePropertyKeys.ROTATION_MAPPER, UnaryOperator { Direction.NORTH })
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.PLAYER_SWIM)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofTop(ParticleTypes.BUBBLE_POP))

        event
            .getBuilder(RagiumMachineKeys.MULTI_SMELTER)
            .putFactory(::HTMultiSmelterBlockEntity)
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.MULTI_SMELTER)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLAZE_AMBIENT)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofFront(ParticleTypes.SOUL_FIRE_FLAME))
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.convert(false, RecipeType.SMELTING, HTMachineConverters::fromCooking),
            )
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun registerMaterial(event: HTRegisterMaterialEvent) {
        // Alloy
        event.register(RagiumMaterialKeys.DEEP_STEEL, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.NETHERITE, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.RAGI_ALLOY, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.RAGI_STEEL, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.REFINED_RAGI_STEEL, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.STEEL, HTMaterialType.ALLOY)

        event.register(RagiumMaterialKeys.BRASS, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.BRONZE, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.ELECTRUM, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.INVAR, HTMaterialType.ALLOY)
        // Dust
        event.register(RagiumMaterialKeys.ALKALI, HTMaterialType.DUST)
        event.register(RagiumMaterialKeys.ASH, HTMaterialType.DUST)
        event.register(RagiumMaterialKeys.CARBON, HTMaterialType.DUST)
        event.register(RagiumMaterialKeys.OBSIDIAN, HTMaterialType.DUST)
        event.register(RagiumMaterialKeys.WOOD, HTMaterialType.DUST)
        // Gem
        event.register(RagiumMaterialKeys.AMETHYST, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.CINNABAR, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.COAL, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.CRYOLITE, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.DIAMOND, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.EMERALD, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.FLUORITE, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.LAPIS, HTMaterialType.GEM)

        event.register(RagiumMaterialKeys.NETHERITE_SCRAP, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.PERIDOT, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.QUARTZ, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.RAGI_CRYSTAL, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.RUBY, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.SAPPHIRE, HTMaterialType.GEM)
        // Metal
        event.register(RagiumMaterialKeys.ALUMINUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.COPPER, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.GOLD, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.IRON, HTMaterialType.METAL)

        event.register(RagiumMaterialKeys.RAGIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.ECHORIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.FIERIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.DRAGONIUM, HTMaterialType.METAL)

        event.register(RagiumMaterialKeys.IRIDIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.LEAD, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.NICKEL, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.PLATINUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.PLUTONIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.SILVER, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.TIN, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.TITANIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.TUNGSTEN, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.URANIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.ZINC, HTMaterialType.METAL)
        // Mineral
        event.register(RagiumMaterialKeys.BAUXITE, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.CRUDE_RAGINITE, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.NITER, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.RAGINITE, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.REDSTONE, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.SALT, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.SULFUR, HTMaterialType.MINERAL)

        event.register(RagiumMaterialKeys.GALENA, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.PYRITE, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.SPHALERITE, HTMaterialType.MINERAL)
    }

    @SubscribeEvent
    fun createRegistry(event: NewRegistryEvent) {
        event.register(RagiumAPI.Registries.MULTIBLOCK_COMPONENT_TYPE)
        event.register(RagiumAPI.Registries.RECIPE_CONDITION)

        LOGGER.info("Registered new registries!")
    }

    @SubscribeEvent
    fun addBlockToBlockEntity(event: BlockEntityTypeAddBlocksEvent) {
        fun bindMachine(type: Supplier<out BlockEntityType<*>>, machine: HTMachineKey) {
            val content: HTBlockContent = machine.getBlockOrNull() ?: return
            event.modify(type.get(), content.get())
        }

        fun bindAllMachines(type: Supplier<out BlockEntityType<*>>) {
            HTMachineKey.allKeys.forEach { machine: HTMachineKey -> bindMachine(type, machine) }
        }

        bindAllMachines(RagiumBlockEntityTypes.DEFAULT_GENERATOR)
        bindMachine(RagiumBlockEntityTypes.FLUID_GENERATOR, RagiumMachineKeys.COMBUSTION_GENERATOR)
        bindMachine(RagiumBlockEntityTypes.FLUID_GENERATOR, RagiumMachineKeys.GAS_TURBINE)
        bindMachine(RagiumBlockEntityTypes.FLUID_GENERATOR, RagiumMachineKeys.THERMAL_GENERATOR)

        bindAllMachines(RagiumBlockEntityTypes.DEFAULT_PROCESSOR)
        bindAllMachines(RagiumBlockEntityTypes.LARGE_PROCESSOR)
        bindMachine(RagiumBlockEntityTypes.DISTILLATION_TOWER, RagiumMachineKeys.DISTILLATION_TOWER)
        bindMachine(RagiumBlockEntityTypes.MULTI_SMELTER, RagiumMachineKeys.MULTI_SMELTER)

        LOGGER.info("Added external blocks to BlockEntityType!")
    }

    @SubscribeEvent
    fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        // All Blocks
        fun <T : Any, C> registerForBlocks(capability: BlockCapability<T, C>, provider: IBlockCapabilityProvider<T, C>) {
            for (block: Block in BuiltInRegistries.BLOCK) {
                event.registerBlock(
                    capability,
                    provider,
                    block,
                )
            }
        }

        RagiumAPI.machineRegistry.blockMap.values.forEach { content: HTBlockContent ->
            event.registerBlock(
                RagiumAPI.BlockCapabilities.MACHINE_TIER,
                { _: Level, _: BlockPos, _: BlockState, blockEntity: BlockEntity?, _: Void? ->
                    (blockEntity as? HTMachineTierProvider)?.machineTier
                },
                content.get(),
            )
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
        registerTier(RagiumBlocks.Hulls.entries)
        registerTier(RagiumBlocks.Coils.entries)
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

        registerHandlers(RagiumBlockEntityTypes.CATALYST_ADDON)
        registerHandlers(RagiumBlockEntityTypes.DRUM)
        registerHandlers(RagiumBlockEntityTypes.MANUAL_GRINDER)

        registerHandlers(RagiumBlockEntityTypes.DEFAULT_GENERATOR)
        registerHandlers(RagiumBlockEntityTypes.FLUID_GENERATOR)

        registerHandlers(RagiumBlockEntityTypes.DEFAULT_PROCESSOR)
        registerHandlers(RagiumBlockEntityTypes.LARGE_PROCESSOR)
        registerHandlers(RagiumBlockEntityTypes.DISTILLATION_TOWER)
        registerHandlers(RagiumBlockEntityTypes.MULTI_SMELTER)

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
        event.register(RagiumAPI.DataMapTypes.MACHINE_FUEL)

        event.register(RagiumAPI.DataMapTypes.MATERIAL)

        event.register(RagiumAPI.DataMapTypes.TEMP_TIER)

        LOGGER.info("Registered Data Map Types!")
    }

    @SubscribeEvent
    fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        fun <T : ItemLike> modifyAll(items: Collection<T>, patch: (DataComponentPatch.Builder, T) -> Unit) {
            items.forEach { itemLike: T ->
                event.modify(itemLike.asItem()) { builder: DataComponentPatch.Builder -> patch(builder, itemLike) }
            }
        }

        fun tieredText(translationKey: String): (DataComponentPatch.Builder, HTMachineTierProvider) -> Unit =
            { builder: DataComponentPatch.Builder, provider: HTMachineTierProvider ->
                builder.name(provider.machineTier.createPrefixedText(translationKey))
            }

        val materialText: (DataComponentPatch.Builder, HTMaterialProvider) -> Unit =
            { builder: DataComponentPatch.Builder, provider: HTMaterialProvider ->
                builder.name(provider.prefixedText)
                if (provider.material in RagiumMaterialKeys.END_CONTENTS) {
                    builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                }
            }
        // Block
        modifyAll(RagiumBlocks.StorageBlocks.entries, materialText)
        modifyAll(RagiumBlocks.Grates.entries, tieredText(RagiumTranslationKeys.GRATE))
        modifyAll(RagiumBlocks.Casings.entries, tieredText(RagiumTranslationKeys.CASING))
        modifyAll(RagiumBlocks.Hulls.entries, tieredText(RagiumTranslationKeys.HULL))
        modifyAll(RagiumBlocks.Coils.entries, tieredText(RagiumTranslationKeys.COIL))
        modifyAll(RagiumBlocks.Burners.entries, tieredText(RagiumTranslationKeys.BURNER))

        modifyAll(RagiumBlocks.Drums.entries, tieredText(RagiumTranslationKeys.DRUM))
        // Item
        modifyAll(RagiumItems.MATERIALS, materialText)

        modifyAll(RagiumItems.Circuits.entries, tieredText(RagiumTranslationKeys.CIRCUIT))

        modifyAll(RagiumItems.Radioactives.entries) { builder: DataComponentPatch.Builder, radioactive: RagiumItems.Radioactives ->
            builder.set(RagiumComponentTypes.RADIOACTIVE, radioactive.level)
        }

        LOGGER.info("Modified item components!")
    }
}
