package hiiragi283.ragium.integration.emi

import com.mojang.logging.LogUtils
import dev.emi.emi.EmiPort
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiCraftingRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe
import dev.emi.emi.api.stack.Comparison
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.map.HTBrewingEffect
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.extension.holdersSequence
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.partially1
import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.manager.HTRecipeAccess
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.fluid.HTFluidType
import hiiragi283.ragium.common.recipe.HTSmithingModifyRecipe
import hiiragi283.ragium.common.util.HTRegistryHelper
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe
import hiiragi283.ragium.impl.recipe.HTSawmillRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.integration.emi.data.HTEmiBrewingEffect
import hiiragi283.ragium.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.integration.emi.recipe.HTBrewingEffectEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTSmithingModifyEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.generator.HTFuelGeneratorEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.machine.HTAlloyingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.machine.HTCrushingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.machine.HTCuttingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.machine.HTFluidTransformingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.machine.HTItemToItemEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.machine.HTMeltingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.machine.HTSimulatingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.machine.HTWashingEmiRecipe
import hiiragi283.ragium.integration.emi.type.HTRecipeViewerType
import hiiragi283.ragium.integration.emi.type.HTRegistryRecipeViewerType
import hiiragi283.ragium.integration.emi.type.RagiumRecipeViewerTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.client.Minecraft
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import org.slf4j.Logger

@EmiEntrypoint
class RagiumEmiPlugin : EmiPlugin {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()

        @JvmStatic
        internal lateinit var recipeAccess: HTRecipeAccess
            private set

        @JvmStatic
        internal val registryAccess: RegistryAccess by lazy {
            Minecraft.getInstance().level?.registryAccess()
                ?: error("Cannot access client RegistryAccess")
        }
    }

    override fun register(registry: EmiRegistry) {
        // Recipe
        recipeAccess = RagiumAPI.INSTANCE.wrapRecipeManager(registry.recipeManager)

        addRecipes(registry)
        // Functions
        registry.addGenericStackProvider(RagiumEmiStackProvider)

        registry.setDefaultComparison(
            RagiumItems.LOOT_TICKET.get(),
            Comparison.compareData { stack: EmiStack -> stack.get(RagiumDataComponents.LOOT_TICKET) },
        )
    }

    //    Recipes    //

    private fun addRecipes(registry: EmiRegistry) {
        addCustomRecipe(registry)
        addDataMaps(registry)
        addMachineRecipes(registry)
        addInteractions(registry)
    }

    private fun addCustomRecipe(registry: EmiRegistry) {
        // Crafting
        EmiPort.getPotionRegistry().holdersSequence().forEach { holder: Holder<Potion> ->
            registry.addRecipeSafe(
                holder.idOrThrow.withPrefix("/shapeless/ice_cream_soda/"),
            ) { id: ResourceLocation ->
                EmiCraftingRecipe(
                    listOf(
                        EmiStack.of(RagiumItems.ICE_CREAM),
                        EmiIngredient.of(RagiumCommonTags.Items.FOODS_CHERRY),
                        EmiStack.of(PotionContents.createItemStack(Items.POTION, holder)),
                        EmiIngredient.of(Tags.Items.DYES_GREEN),
                    ),
                    EmiStack.of(RagiumAPI.INSTANCE.createSoda(holder)),
                    id,
                    true,
                )
            }
        }
        // Smithing
        RagiumRecipeTypes.SMITHING
            .getAllRecipes(recipeAccess)
            .filterIsInstance<HTSmithingModifyRecipe>()
            .forEach { recipe: HTSmithingModifyRecipe ->
                registry.addRecipe(
                    HTSmithingModifyEmiRecipe(
                        recipe.template.let(EmiIngredient::of),
                        recipe.addition.let(EmiIngredient::of),
                        recipe,
                    ),
                )
            }
    }

    private fun addDataMaps(registry: EmiRegistry) {
        val thermalCategory: HTEmiRecipeCategory =
            addFuelRecipes(registry, HTGeneratorVariant.THERMAL, RagiumDataMaps.INSTANCE.thermalFuelType)
        val combustionCategory: HTEmiRecipeCategory =
            addFuelRecipes(registry, HTGeneratorVariant.COMBUSTION, RagiumDataMaps.INSTANCE.combustionFuelType)

        val itemRegistry: Registry<Item> = EmiPort.getItemRegistry()

        // Thermal Generator
        val lavaConsumption: Int = RagiumDataMaps.INSTANCE.getThermalFuel(registryAccess, HTFluidContent.LAVA.toStack(1).fluidHolder)
        addRecipes(
            registry,
            thermalCategory,
            itemRegistry
                .getDataMap(NeoForgeDataMaps.FURNACE_FUELS)
                .map { (key: ResourceKey<Item>, fuel: FurnaceFuel) ->
                    val lavaInput: EmiStack = HTFluidContent.LAVA.toFluidEmi(fuel.burnTime / 10)
                    val lavaLevel: Float = lavaInput.amount / lavaConsumption.toFloat()

                    HTEmiFluidFuelData(
                        key.location().withPrefix("/${RagiumDataMaps.INSTANCE.thermalFuelType.id().path}/"),
                        (HTGeneratorVariant.THERMAL.energyRate * lavaLevel).toInt(),
                        itemRegistry.getOrThrow(key).let(EmiStack::of),
                        lavaInput,
                    )
                }.asSequence(),
            ::HTFuelGeneratorEmiRecipe,
        )
        // Combustion Generator
        registry.addRecipeSafe(
            RagiumDataMaps.INSTANCE.thermalFuelType
                .id()
                .withPrefix("/"),
        ) { id: ResourceLocation ->
            HTFuelGeneratorEmiRecipe(
                combustionCategory,
                id,
                HTEmiFluidFuelData(
                    id,
                    HTGeneratorVariant.COMBUSTION.energyRate,
                    EmiIngredient.of(ItemTags.COALS),
                    RagiumFluidContents.CRUDE_OIL.toFluidEmi(100),
                ),
            )
        }
    }

    private fun addMachineRecipes(registry: EmiRegistry) {
        // Basic
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.ALLOYING, ::HTAlloyingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.COMPRESSING, ::HTItemToItemEmiRecipe)
        addCategoryAndRecipes(
            registry,
            RagiumRecipeViewerTypes.CRUSHING,
        ) { category: HTEmiRecipeCategory, holder: HTRecipeHolder<HTItemToChancedItemRecipe> ->
            val (id: ResourceLocation, recipe: HTItemToChancedItemRecipe) = holder
            when (recipe) {
                is HTCrushingRecipe -> HTCrushingEmiRecipe(category, id, recipe)

                is HTItemToItemRecipe -> HTItemToItemEmiRecipe(category, id, recipe)

                else -> null
            }
        }
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.EXTRACTING, ::HTItemToItemEmiRecipe)
        // Cutting
        val cuttingCategory: HTEmiRecipeCategory =
            addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.SAWMILL, ::HTCuttingEmiRecipe)
        addRecipes(
            registry,
            cuttingCategory,
            RagiumRecipeTypes.STONECUTTER.getAllHolders(recipeAccess),
        ) { category: HTEmiRecipeCategory, id: ResourceLocation, holder: HTRecipeHolder<StonecutterRecipe> ->
            val recipe: StonecutterRecipe = holder.recipe
            val recipe1 = HTSawmillRecipe(
                HTItemIngredient.wrapVanilla(recipe.ingredients[0]),
                HTResultHelper.INSTANCE.item(recipe.getResultItem(registryAccess)),
            )
            HTCuttingEmiRecipe(category, id.withPrefix("/"), recipe1)
        }
        // Advanced
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.FLUID_TRANSFORM, ::HTFluidTransformingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.MELTING, ::HTMeltingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.WASHING, ::HTWashingEmiRecipe)
        // Elite
        addCategoryAndRecipes(
            registry,
            RagiumRecipeViewerTypes.BREWING,
            EmiPort
                .getItemRegistry()
                .getDataMap(RagiumDataMaps.INSTANCE.brewingEffectType)
                .map { (key: ResourceKey<Item>, value: HTBrewingEffect) -> HTEmiBrewingEffect(HTItemHolderLike.fromKey(key), value) }
                .asSequence(),
            ::HTBrewingEffectEmiRecipe,
        )

        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.SIMULATING, ::HTSimulatingEmiRecipe)
    }

    private fun addInteractions(registry: EmiRegistry) {
        // Water Well
        registry.addInteraction(HTFluidContent.WATER.toFluidEmi(), prefix = "fluid_generator") {
            leftInput(EmiStack.of(HTDeviceVariant.WATER_COLLECTOR))
            rightInput(EmiStack.EMPTY, false)
        }
        // Lava Well
        registry.addInteraction(HTFluidContent.LAVA.toFluidEmi(), prefix = "fluid_generator") {
            leftInput(EmiStack.of(HTDeviceVariant.LAVA_COLLECTOR))
            rightInput(EmiStack.EMPTY, false)
        }
        // Milk Drain
        registry.addInteraction(HTFluidContent.MILK.toFluidEmi(), prefix = "fluid_generator") {
            leftInput(EmiStack.of(HTDeviceVariant.MILK_COLLECTOR))
            rightInput(EmiStack.of(Items.COW_SPAWN_EGG), true)
        }
        // Exp Collector
        registry.addInteraction(EmiStack.of(RagiumFluidContents.EXPERIENCE.get()), prefix = "fluid_generator") {
            leftInput(EmiStack.of(HTDeviceVariant.EXP_COLLECTOR))
            rightInput(EmiStack.EMPTY, false)
        }

        // Bottled Bee
        registry.addInteraction(EmiStack.of(RagiumItems.BOTTLED_BEE)) {
            leftInput(EmiStack.of(Items.GLASS_BOTTLE))
            rightInput(EmiStack.of(Items.BEE_SPAWN_EGG), false)
        }

        // World Vaporization
        for (fluid: Fluid in HTRegistryHelper.fluidSequence()) {
            val type: FluidType = fluid.fluidType
            if (type is HTFluidType) {
                val result: EmiStack = type.dropItem?.toEmi() ?: continue
                registry.addInteraction(result) {
                    leftInput(EmiStack.of(fluid.bucket))
                    rightInput(EmiStack.EMPTY, false)
                }
            }
        }
        // Crude Oil + Lava -> Soul Sand
        registry.addFluidInteraction(Items.SOUL_SAND, RagiumFluidContents.CRUDE_OIL, HTFluidContent.LAVA)

        // Water + Eldritch Flux -> Eldritch Stone
        registry.addFluidInteraction(RagiumBlocks.ELDRITCH_STONE, HTFluidContent.WATER, RagiumFluidContents.ELDRITCH_FLUX)
    }

    //    Extension    //

    private inline fun EmiRegistry.addRecipeSafe(id: ResourceLocation, factory: (ResourceLocation) -> EmiRecipe) {
        runCatching {
            addRecipe(factory(id))
        }.onFailure { throwable: Throwable ->
            LOGGER.warn("Exception thrown when parsing vanilla recipe: $id", throwable)
        }
    }

    /**
     * @see [mekanism.client.recipe_viewer.emi.MekanismEmi.addCategoryAndRecipes]
     */
    private inline fun <I : RecipeInput, R1 : HTRecipe<I>, reified R2 : R1> addCategoryAndRecipes(
        registry: EmiRegistry,
        viewerType: HTRegistryRecipeViewerType<I, R1>,
        noinline factory: (HTEmiRecipeCategory, HTRecipeHolder<R2>) -> HTEmiRecipe<*>?,
    ): HTEmiRecipeCategory {
        val category: HTEmiRecipeCategory = registerCategory(registry, viewerType)
        viewerType
            .getAllHolders(recipeAccess)
            .map { it.castRecipe<R2>() }
            .mapNotNull(factory.partially1(category))
            .forEach(registry::addRecipe)
        return category
    }

    /**
     * @see [mekanism.client.recipe_viewer.emi.MekanismEmi.addCategoryAndRecipes]
     */
    private fun <RECIPE : HTHolderLike> addCategoryAndRecipes(
        registry: EmiRegistry,
        viewerType: HTRecipeViewerType<RECIPE>,
        recipes: Sequence<RECIPE>,
        factory: (HTEmiRecipeCategory, ResourceLocation, RECIPE) -> HTEmiRecipe<*>?,
    ): HTEmiRecipeCategory = addRecipes(registry, registerCategory(registry, viewerType), recipes, factory)

    private fun <RECIPE : HTHolderLike> addRecipes(
        registry: EmiRegistry,
        category: HTEmiRecipeCategory,
        recipes: Sequence<RECIPE>,
        factory: (HTEmiRecipeCategory, ResourceLocation, RECIPE) -> HTEmiRecipe<*>?,
    ): HTEmiRecipeCategory {
        for (recipe: RECIPE in recipes) {
            registry.addRecipe(factory(category, recipe.getId(), recipe))
        }
        return category
    }

    private fun registerCategory(registry: EmiRegistry, viewerType: HTRecipeViewerType<*>): HTEmiRecipeCategory {
        val category: HTEmiRecipeCategory = HTEmiRecipeCategory.create(viewerType)
        registry.addCategory(category)
        viewerType.workStations.map(EmiStack::of).forEach(registry::addWorkstation.partially1(category))
        return category
    }

    private fun addFuelRecipes(
        registry: EmiRegistry,
        variant: HTGeneratorVariant,
        dataMapType: DataMapType<Fluid, HTFluidFuelData>,
    ): HTEmiRecipeCategory {
        val viewerType: HTRecipeViewerType<HTEmiFluidFuelData> = RagiumRecipeViewerTypes.getGenerator(variant)
        val fluidRegistry: Registry<Fluid> = EmiPort.getFluidRegistry()
        return addCategoryAndRecipes(
            registry,
            viewerType,
            fluidRegistry
                .getDataMap(dataMapType)
                .map { (key: ResourceKey<Fluid>, fuelData: HTFluidFuelData) ->
                    HTEmiFluidFuelData(
                        key.location().withPrefix("/${dataMapType.id().path}/"),
                        variant.energyRate,
                        EmiStack.EMPTY,
                        fluidRegistry.getOrThrow(key).let(EmiStack::of).setAmount(fuelData.amount.toLong()),
                    )
                }.asSequence(),
            ::HTFuelGeneratorEmiRecipe,
        )
    }

    private fun EmiRegistry.addInteraction(
        output: EmiStack,
        id: ResourceLocation = output.id,
        prefix: String = "interaction",
        builderAction: EmiWorldInteractionRecipe.Builder.() -> Unit,
    ) {
        addRecipeSafe(RagiumAPI.id("/world/$prefix/${id.toString().replace(':', '/')}")) { id1: ResourceLocation ->
            EmiWorldInteractionRecipe
                .builder()
                .apply(builderAction)
                .id(id1)
                .output(output)
                .build()
        }
    }

    private fun EmiRegistry.addFluidInteraction(output: ItemLike, source: HTFluidContent<*, *, *>, flowing: HTFluidContent<*, *, *>) {
        val outputStack: EmiStack = EmiStack.of(output)

        val sourceStack: EmiStack = source.toFluidEmi(1000)
        val flowingStack: EmiStack = flowing.toFluidEmi(1000)

        addInteraction(outputStack, prefix = "fluid_interaction") {
            leftInput(sourceStack.copyAsCatalyst())
            rightInput(flowingStack.copyAsCatalyst(), false)
        }
    }
}
