package hiiragi283.ragium.common

import com.mojang.logging.LogUtils
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.event.HTModifyPropertyEvent
import hiiragi283.ragium.api.event.HTRegisterMaterialEvent
import hiiragi283.ragium.api.extension.isModLoaded
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.property.HTGeneratorFuel
import hiiragi283.ragium.api.machine.property.HTMachineEntityFactory
import hiiragi283.ragium.api.machine.property.HTMachineParticleHandler
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeValidator
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.block.generator.HTDefaultGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTFluidGeneratorBlockEntity
import hiiragi283.ragium.common.block.processor.HTDefaultProcessorBlockEntity
import hiiragi283.ragium.common.block.processor.HTDistillationTowerBlockEntity
import hiiragi283.ragium.common.block.processor.HTLargeProcessorBlockEntity
import hiiragi283.ragium.common.block.processor.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.internal.HTMachineRegistryImpl
import hiiragi283.ragium.common.internal.HTMaterialRegistryImpl
import hiiragi283.ragium.integration.RagiumEvilIntegration
import hiiragi283.ragium.integration.RagiumMekIntegration
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent
import net.neoforged.neoforge.fluids.FluidType
import org.slf4j.Logger
import java.util.function.BiPredicate
import java.util.function.Function
import java.util.function.UnaryOperator

@Mod(RagiumAPI.MOD_ID)
class RagiumCommon(eventBus: IEventBus, container: ModContainer) {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        eventBus.addListener(EventPriority.HIGHEST, ::modifyMachineProperties)
        eventBus.addListener(EventPriority.HIGHEST, ::registerMaterial)

        eventBus.addListener(::construct)
        eventBus.addListener(::commonSetup)
        eventBus.addListener(::sendMessage)
        if (isModLoaded("mekanism")) {
            RagiumMekIntegration.init(eventBus)
        }
        if (isModLoaded("evilcraft")) {
            RagiumEvilIntegration.init(eventBus)
        }

        RagiumComponentTypes.REGISTER.register(eventBus)

        RagiumMachineKeys
        RagiumMaterialKeys

        HTMachineRegistryImpl.registerBlocks()

        RagiumFluids.register(eventBus)
        RagiumBlocks.REGISTER.register(eventBus)
        RagiumEntityTypes.REGISTER.register(eventBus)
        RagiumItems.REGISTER.register(eventBus)

        RagiumBlockEntityTypes.REGISTER.register(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)
        RagiumMenuTypes.REGISTER.register(eventBus)
        RagiumRecipes.SERIALIZER.register(eventBus)
        RagiumRecipes.TYPE.register(eventBus)
        RagiumMachineRecipeConditions.REGISTER.register(eventBus)
        RagiumMultiblockComponentTypes.REGISTER.register(eventBus)

        container.registerConfig(ModConfig.Type.STARTUP, RagiumConfig.SPEC)

        LOGGER.info("Ragium loaded!")
    }

    private fun construct(event: FMLConstructModEvent) {
        HTMachineRegistryImpl.modifyProperties()

        HTMaterialRegistryImpl.initRegistry()
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        LOGGER.info("Loaded common setup!")
    }

    private fun sendMessage(event: InterModEnqueueEvent) {
        LOGGER.info("Sent IMC Messages!")
    }

    //    Machine    //

    private fun modifyMachineProperties(event: HTModifyPropertyEvent.Machine) {
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
            .put(
                HTMachinePropertyKeys.GENERATOR_FUEL,
                setOf(
                    HTGeneratorFuel(RagiumFluidTags.NON_NITRO_FUEL, FluidType.BUCKET_VOLUME / 10),
                    HTGeneratorFuel(RagiumFluidTags.NITRO_FUEL, FluidType.BUCKET_VOLUME / 100),
                ),
            ).put(HTMachinePropertyKeys.SOUND, SoundEvents.FIRE_EXTINGUISH)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofSimple(ParticleTypes.ASH))

        event
            .getBuilder(RagiumMachineKeys.GAS_TURBINE)
            .putFactory(::HTFluidGeneratorBlockEntity)
            .put(
                HTMachinePropertyKeys.GENERATOR_FUEL,
                setOf(
                    HTGeneratorFuel(RagiumFluids.METHANE, FluidType.BUCKET_VOLUME / 10),
                    HTGeneratorFuel(RagiumFluids.ETHENE, FluidType.BUCKET_VOLUME / 20),
                    HTGeneratorFuel(RagiumFluids.ACETYLENE, FluidType.BUCKET_VOLUME / 50),
                ),
            )

        event
            .getBuilder(RagiumMachineKeys.NUCLEAR_REACTOR)
            .putFactory(::HTFluidGeneratorBlockEntity)
            .put(
                HTMachinePropertyKeys.GENERATOR_FUEL,
                setOf(
                    HTGeneratorFuel(RagiumFluidTags.NUCLEAR_FUEL, FluidType.BUCKET_VOLUME / 10),
                ),
            )

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
            .put(
                HTMachinePropertyKeys.GENERATOR_FUEL,
                setOf(
                    HTGeneratorFuel(RagiumFluidTags.THERMAL_FUEL, FluidType.BUCKET_VOLUME / 10),
                ),
            )

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

        event.getBuilder(RagiumMachineKeys.CUTTING_MACHINE)

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
            .getBuilder(RagiumMachineKeys.GRINDER)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.GRINDSTONE_USE)
            .put(HTMachinePropertyKeys.PARTICLE, HTMachineParticleHandler.ofMiddle(ParticleTypes.CRIT))

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
    }

    //    Material    //

    private fun registerMaterial(event: HTRegisterMaterialEvent) {
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
}
