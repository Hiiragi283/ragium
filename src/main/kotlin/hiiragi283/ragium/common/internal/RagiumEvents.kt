package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.event.HTModifyPropertyEvent
import hiiragi283.ragium.api.event.HTRegisterMaterialEvent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.property.HTMachineParticleHandler
import hiiragi283.ragium.api.machine.property.HTMachineRecipeProxy
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.util.DataFunction
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
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
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
import java.util.function.Consumer
import java.util.function.Supplier

@EventBusSubscriber(modid = RagiumAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
internal object RagiumEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun modifyMachineProperties(event: HTModifyPropertyEvent.Machine) {
        fun HTPropertyHolderBuilder.putValidator(validator: DataFunction<HTMachineRecipe>): HTPropertyHolderBuilder =
            put(HTMachinePropertyKeys.RECIPE_VALIDATOR, validator)

        // Consumer
        event
            .getBuilder(RagiumMachineKeys.BEDROCK_MINER)

        // Generator
        event
            .getBuilder(RagiumMachineKeys.COMBUSTION_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTFluidGeneratorBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.FIRE_EXTINGUISH)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofSimple(ParticleTypes.ASH))

        event
            .getBuilder(RagiumMachineKeys.GAS_TURBINE)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTFluidGeneratorBlockEntity)

        event
            .getBuilder(RagiumMachineKeys.NUCLEAR_REACTOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTFluidGeneratorBlockEntity)

        event
            .getBuilder(RagiumMachineKeys.SOLAR_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTDefaultGeneratorBlockEntity)
            .put(HTMachinePropertyKeys.GENERATOR_PREDICATE) { level: Level, pos: BlockPos -> level.canSeeSky(pos.above()) && level.isDay }
            .put(HTMachinePropertyKeys.ROTATION_MAPPER, constFunction2(Direction.NORTH))

        event
            .getBuilder(RagiumMachineKeys.STEAM_TURBINE)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTFluidGeneratorBlockEntity)

        event
            .getBuilder(RagiumMachineKeys.THERMAL_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTFluidGeneratorBlockEntity)

        event.getBuilder(RagiumMachineKeys.VIBRATION_GENERATOR)

        // Processor
        RagiumMachineKeys.PROCESSORS
            .map(event::getBuilder)
            .forEach { builder: HTPropertyHolderBuilder ->
                builder
                    .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTDefaultProcessorBlockEntity)
                    .put(
                        HTMachinePropertyKeys.PARTICLE,
                        HTMachineParticleHandler.ofSimple(ParticleTypes.ELECTRIC_SPARK),
                    )
            }

        event
            .getBuilder(RagiumMachineKeys.ASSEMBLER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTLargeProcessorBlockEntity)
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.default(RagiumRecipes.ASSEMBLER),
            )

        event
            .getBuilder(RagiumMachineKeys.BLAST_FURNACE)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTLargeProcessorBlockEntity)
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.BLAST_FURNACE)
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.default(RagiumRecipes.BLAST_FURNACE),
            ).put(HTMachinePropertyKeys.SOUND, SoundEvents.BLAZE_AMBIENT)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofFront(ParticleTypes.FLAME))

        event
            .getBuilder(RagiumMachineKeys.CHEMICAL_REACTOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTLargeProcessorBlockEntity)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BREWING_STAND_BREW)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofMiddle(ParticleTypes.BUBBLE_POP))
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.material(
                    RagiumRecipes.CHEMICAL_REACTOR,
                    HTMachineConverters::chemicalOre3x,
                    HTMachineConverters::chemicalOre4x,
                    HTMachineConverters::chemicalOre5x,
                ),
            )

        event
            .getBuilder(RagiumMachineKeys.COMPRESSOR)
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.material(
                    RagiumRecipes.COMPRESSOR,
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
                HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>> ->
                    HTMachineRecipeProxy.default(RagiumRecipes.CUTTING_MACHINE).getRecipes(level, consumer)
                    level.recipeManager
                        .getAllRecipesFor(RecipeType.STONECUTTING)
                        .map { HTMachineConverters.fromCutting(it, level.registryAccess()) }
                        .forEach(consumer)
                },
            )

        event
            .getBuilder(RagiumMachineKeys.DISTILLATION_TOWER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY) { pos: BlockPos, state: BlockState, _: HTMachineKey ->
                HTDistillationTowerBlockEntity(pos, state)
            }.put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.DISTILLATION_TOWER)
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.default(RagiumRecipes.DISTILLATION_TOWER),
            ).putValidator { recipe: HTMachineRecipe ->
                when {
                    recipe.itemInputs.isNotEmpty() -> DataResult.error { "Distillation tower recipe not accepts item inputs!" }
                    recipe.fluidInputs.size != 1 -> DataResult.error { "Distillation tower recipe should have only one fluid input!" }
                    recipe.condition.isEmpty -> DataResult.error { "Distillation tower recipe requires condition!" }
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
                    RagiumRecipes.GRINDER,
                    HTMachineConverters::grinderMainToDust,
                    HTMachineConverters::grinderGearToDust,
                    HTMachineConverters::grinderPlateToDust,
                    HTMachineConverters::grinderRawToDust,
                    HTMachineConverters::grinderOreToRaw,
                ),
            )

        event.getBuilder(RagiumMachineKeys.GROWTH_CHAMBER)

        event
            .getBuilder(RagiumMachineKeys.LASER_TRANSFORMER)
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.default(RagiumRecipes.LASER_TRANSFORMER),
            )

        event
            .getBuilder(RagiumMachineKeys.MIXER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, ::HTLargeProcessorBlockEntity)
            .put(HTMachinePropertyKeys.ROTATION_MAPPER, constFunction2(Direction.NORTH))
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.default(RagiumRecipes.MIXER),
            ).put(HTMachinePropertyKeys.SOUND, SoundEvents.PLAYER_SWIM)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofTop(ParticleTypes.BUBBLE_POP))

        event
            .getBuilder(RagiumMachineKeys.MULTI_SMELTER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY) { pos: BlockPos, state: BlockState, _: HTMachineKey ->
                HTMultiSmelterBlockEntity(pos, state)
            }.put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.MULTI_SMELTER)
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.default(RagiumRecipes.MULTI_SMELTER),
            ).put(HTMachinePropertyKeys.SOUND, SoundEvents.BLAZE_AMBIENT)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofFront(ParticleTypes.SOUL_FIRE_FLAME))
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>> ->
                    level.recipeManager
                        .getAllRecipesFor(RecipeType.SMELTING)
                        .map { HTMachineConverters.fromCooking(it, level.registryAccess()) }
                        .forEach(consumer)
                },
            )

        event
            .getBuilder(RagiumMachineKeys.RESOURCE_PLANT)
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.RESOURCE_PLANT)
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.default(RagiumRecipes.RESOURCE_PLANT),
            )

        event
            .getBuilder(RagiumMachineKeys.STEAM_BOILER)
            .put(HTMachinePropertyKeys.RECIPE_PROXY, HTMachineRecipeProxy(HTMachineConverters::fromFuel))
            .put(
                HTMachinePropertyKeys.RECIPE_PROXY,
                HTMachineRecipeProxy.default(RagiumRecipes.STEAM_BOILER),
            )
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun registerMaterial(event: HTRegisterMaterialEvent) {
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
        event.register(CommonMaterials.NITER, HTMaterialType.MINERAL)
        event.register(CommonMaterials.OSMIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.PERIDOT, HTMaterialType.GEM)
        event.register(CommonMaterials.PLATINUM, HTMaterialType.METAL)
        event.register(CommonMaterials.PLUTONIUM, HTMaterialType.METAL)
        event.register(CommonMaterials.RUBY, HTMaterialType.GEM)
        event.register(CommonMaterials.SALT, HTMaterialType.MINERAL)
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

        event.register(RagiumMaterials.CRUDE_RAGINITE, HTMaterialType.MINERAL)
        event.register(RagiumMaterials.DEEP_STEEL, HTMaterialType.ALLOY)
        event.register(RagiumMaterials.DRAGONIUM, HTMaterialType.METAL)
        event.register(RagiumMaterials.ECHORIUM, HTMaterialType.METAL)
        event.register(RagiumMaterials.FIERIUM, HTMaterialType.METAL)
        event.register(RagiumMaterials.RAGI_ALLOY, HTMaterialType.ALLOY)
        event.register(RagiumMaterials.RAGI_CRYSTAL, HTMaterialType.GEM)
        event.register(RagiumMaterials.RAGI_STEEL, HTMaterialType.ALLOY)
        event.register(RagiumMaterials.RAGINITE, HTMaterialType.MINERAL)
        event.register(RagiumMaterials.RAGIUM, HTMaterialType.METAL)
        event.register(RagiumMaterials.REFINED_RAGI_STEEL, HTMaterialType.ALLOY)

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
        bindAllMachines(RagiumBlockEntityTypes.FLUID_GENERATOR)

        bindAllMachines(RagiumBlockEntityTypes.DEFAULT_PROCESSOR)
        bindAllMachines(RagiumBlockEntityTypes.LARGE_PROCESSOR)
        bindMachine(RagiumBlockEntityTypes.DISTILLATION_TOWER, RagiumMachineKeys.DISTILLATION_TOWER)
        bindMachine(RagiumBlockEntityTypes.MULTI_SMELTER, RagiumMachineKeys.MULTI_SMELTER)

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
        registerTier(RagiumBlocks.CasingWalls.entries)
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
        event.register(RagiumAPI.DataMapTypes.MACHINE_FUEL)

        event.register(RagiumAPI.DataMapTypes.MACHINE_KEY)
        event.register(RagiumAPI.DataMapTypes.MACHINE_TIER)
        event.register(RagiumAPI.DataMapTypes.RADIOACTIVES)

        LOGGER.info("Registered Data Map Types!")
    }

    @SubscribeEvent
    fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        // Item
        RagiumItems.MATERIAL_ITEMS.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, holder: DeferredItem<out Item>) ->
            event.modify(holder) { builder: DataComponentPatch.Builder ->
                builder.name(prefix.createText(key))
                if (key in RagiumMaterials.END_CONTENTS) {
                    builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                }
            }
        }

        LOGGER.info("Modified item components!")
    }
}
