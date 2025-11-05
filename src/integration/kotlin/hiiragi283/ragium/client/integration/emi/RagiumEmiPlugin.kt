package hiiragi283.ragium.client.integration.emi

import dev.emi.emi.EmiPort
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiCraftingRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe
import dev.emi.emi.api.stack.Comparison
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.block.attribute.HTEnergyBlockAttribute
import hiiragi283.ragium.api.block.attribute.getAttributeOrThrow
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.data.registry.HTBrewingEffect
import hiiragi283.ragium.api.function.partially1
import hiiragi283.ragium.api.recipe.manager.castRecipe
import hiiragi283.ragium.api.recipe.manager.toFindable
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.holdersSequence
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.client.integration.emi.data.HTEmiBrewingEffect
import hiiragi283.ragium.client.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.client.integration.emi.recipe.HTBrewingEffectEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.HTSmithingModifyEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.generator.HTFuelGeneratorEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTAlloyingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTCrushingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTCuttingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTFluidTransformingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTItemToItemEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTMeltingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTPlantingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTSimulatingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTWashingEmiRecipe
import hiiragi283.ragium.client.integration.emi.type.HTRecipeViewerType
import hiiragi283.ragium.client.integration.emi.type.HTRegistryRecipeViewerType
import hiiragi283.ragium.client.integration.emi.type.RagiumRecipeViewerTypes
import hiiragi283.ragium.common.fluid.HTFluidType
import hiiragi283.ragium.common.recipe.HTSmithingModifyRecipe
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
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

@EmiEntrypoint
class RagiumEmiPlugin : EmiPlugin {
    companion object {
        @JvmStatic
        internal var recipeManager: RecipeManager? = null

        @JvmStatic
        fun recipeManager(): RecipeManager = this.recipeManager ?: error("Recipe Manager not initialized")

        @JvmStatic
        internal val registryAccess: RegistryAccess by lazy {
            Minecraft.getInstance().level?.registryAccess()
                ?: error("Cannot access client RegistryAccess")
        }
    }

    override fun register(registry: EmiRegistry) {
        // Recipe
        recipeManager = registry.recipeManager

        addCustomRecipe(registry)
        addGenerators(registry)
        addProcessors(registry)
        addInteractions(registry)
        // Functions
        registry.addGenericStackProvider(RagiumEmiStackProvider)

        registry.setDefaultComparison(
            RagiumItems.LOOT_TICKET.get(),
            Comparison.compareData { stack: EmiStack -> stack.get(RagiumDataComponents.LOOT_TICKET) },
        )
    }

    //    Recipes    //

    private fun addCustomRecipe(registry: EmiRegistry) {
        // Crafting
        EmiPort.getPotionRegistry().holdersSequence().forEach { holder: Holder<Potion> ->
            registry.addRecipeSafe(
                holder.idOrThrow.withPrefix("/shapeless/ice_cream_soda/"),
            ) { id: ResourceLocation ->
                EmiCraftingRecipe(
                    listOf(
                        RagiumItems.ICE_CREAM.toEmi(),
                        RagiumCommonTags.Items.FOODS_CHERRY.toEmi(),
                        PotionContents.createItemStack(Items.POTION, holder).toEmi(),
                        Tags.Items.DYES_GREEN.toEmi(),
                    ),
                    RagiumPlatform.INSTANCE.createSoda(holder).toEmi(),
                    id,
                    true,
                )
            }
        }
        // Smithing
        RecipeType.SMITHING
            .toFindable()
            .getAllRecipes(recipeManager())
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

    private fun addGenerators(registry: EmiRegistry) {
        val thermalUsage: Int = RagiumBlocks.THERMAL_GENERATOR.getAttributeOrThrow<HTEnergyBlockAttribute>().getUsage()
        val combustionUsage: Int = RagiumBlocks.COMBUSTION_GENERATOR.getAttributeOrThrow<HTEnergyBlockAttribute>().getUsage()

        val thermalCategory: HTEmiRecipeCategory = addFuelRecipes(
            registry,
            RagiumRecipeViewerTypes.THERMAL,
            RagiumDataMaps.THERMAL_FUEL,
            thermalUsage,
        )
        val combustionCategory: HTEmiRecipeCategory = addFuelRecipes(
            registry,
            RagiumRecipeViewerTypes.COMBUSTION,
            RagiumDataMaps.COMBUSTION_FUEL,
            combustionUsage,
        )

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
                    key.location().withPrefix("/${RagiumDataMaps.THERMAL_FUEL.id().path}/") to
                        HTEmiFluidFuelData(
                            (thermalUsage * lavaLevel).toInt(),
                            itemRegistry.getOrThrow(key).toEmi(),
                            lavaInput,
                        )
                }.asSequence(),
            ::HTFuelGeneratorEmiRecipe,
        )
        // Combustion Generator
        registry.addRecipeSafe(RagiumDataMaps.THERMAL_FUEL.id().withPrefix("/")) { id: ResourceLocation ->
            HTFuelGeneratorEmiRecipe(
                combustionCategory,
                id,
                HTEmiFluidFuelData(combustionUsage, ItemTags.COALS.toEmi(), RagiumFluidContents.CRUDE_OIL.toFluidEmi(100)),
            )
        }
    }

    private fun addProcessors(registry: EmiRegistry) {
        // Basic
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.ALLOYING, ::HTAlloyingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.COMPRESSING, ::HTItemToItemEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.CRUSHING, ::HTCrushingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.CUTTING, ::HTCuttingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.EXTRACTING, ::HTItemToItemEmiRecipe)
        // Advanced
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.FLUID_TRANSFORM, ::HTFluidTransformingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.MELTING, ::HTMeltingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.WASHING, ::HTWashingEmiRecipe)
        // Elite
        addCategoryAndRecipes(
            registry,
            RagiumRecipeViewerTypes.BREWING,
            registryAccess
                .registryOrThrow(RagiumAPI.BREWING_EFFECT_KEY)
                .entrySet()
                .map { (key: ResourceKey<HTBrewingEffect>, effect: HTBrewingEffect) ->
                    key.location().withPrefix("/brewing/effect/") to
                        HTEmiBrewingEffect(EmiIngredient.of(effect.ingredient), effect.toPotion().toEmi())
                }.asSequence(),
            ::HTBrewingEffectEmiRecipe,
        )
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.PLANTING, ::HTPlantingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.SIMULATING, ::HTSimulatingEmiRecipe)
    }

    private fun addInteractions(registry: EmiRegistry) {
        // Water Well
        registry.addInteraction(HTFluidContent.WATER.toFluidEmi(), prefix = "fluid_generator") {
            leftInput(RagiumBlocks.WATER_COLLECTOR.toEmi())
            rightInput(EmiStack.EMPTY, false)
        }
        // Lava Well
        registry.addInteraction(HTFluidContent.LAVA.toFluidEmi(), prefix = "fluid_generator") {
            leftInput(RagiumBlocks.LAVA_COLLECTOR.toEmi())
            rightInput(EmiStack.EMPTY, false)
        }
        // Milk Drain
        registry.addInteraction(HTFluidContent.MILK.toFluidEmi(), prefix = "fluid_generator") {
            leftInput(RagiumBlocks.MILK_COLLECTOR.toEmi())
            rightInput(Items.COW_SPAWN_EGG.toEmi(), true)
        }
        // Exp Collector
        registry.addInteraction(EmiStack.of(RagiumFluidContents.EXPERIENCE.get()), prefix = "fluid_generator") {
            leftInput(RagiumBlocks.EXP_COLLECTOR.toEmi())
            rightInput(EmiStack.EMPTY, false)
        }

        // World Vaporization
        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            val fluidType: FluidType = content.getType()
            if (fluidType is HTFluidType) {
                val result: EmiStack = fluidType.dropItem?.toEmi() ?: continue
                registry.addInteraction(result) {
                    leftInput(content.toTagEmi())
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
            RagiumAPI.LOGGER.warn("Exception thrown when parsing vanilla recipe: $id", throwable)
        }
    }

    /**
     * @see mekanism.client.recipe_viewer.emi.MekanismEmi.addCategoryAndRecipes
     */
    private inline fun <INPUT : RecipeInput, BASE : Recipe<INPUT>, reified RECIPE : BASE, EMI_RECIPE : EmiRecipe> addCategoryAndRecipes(
        registry: EmiRegistry,
        viewerType: HTRegistryRecipeViewerType<INPUT, BASE>,
        noinline factory: (HTEmiRecipeCategory, RecipeHolder<RECIPE>) -> EMI_RECIPE?,
    ): HTEmiRecipeCategory {
        val category: HTEmiRecipeCategory = registerCategory(registry, viewerType)
        viewerType
            .getAllHolders(recipeManager())
            .mapNotNull { it.castRecipe<BASE, RECIPE>() }
            .mapNotNull(factory.partially1(category))
            .forEach(registry::addRecipe)
        return category
    }

    /**
     * 指定された引数からレシピを生成し，登録します。
     * @param RECIPE [recipes]で渡す一覧のクラス
     * @param EMI_RECIPE [factory]で返すレシピのクラス
     * @return 渡された[category]
     * @see mekanism.client.recipe_viewer.emi.MekanismEmi.addCategoryAndRecipes
     */
    private fun <RECIPE : Any, EMI_RECIPE : EmiRecipe> addCategoryAndRecipes(
        registry: EmiRegistry,
        viewerType: HTRecipeViewerType<RECIPE>,
        recipes: Sequence<Pair<ResourceLocation, RECIPE>>,
        factory: (HTEmiRecipeCategory, ResourceLocation, RECIPE) -> EMI_RECIPE?,
    ): HTEmiRecipeCategory = addRecipes(registry, registerCategory(registry, viewerType), recipes, factory)

    /**
     * 指定された引数からレシピを生成し，登録します。
     * @param CATEGORY [category]のクラス
     * @param RECIPE [recipes]で渡す一覧のクラス
     * @param EMI_RECIPE [factory]で返すレシピのクラス
     * @return 渡された[category]
     */
    private fun <CATEGORY : EmiRecipeCategory, RECIPE : Any, EMI_RECIPE : EmiRecipe> addRecipes(
        registry: EmiRegistry,
        category: CATEGORY,
        recipes: Sequence<Pair<ResourceLocation, RECIPE>>,
        factory: (CATEGORY, ResourceLocation, RECIPE) -> EMI_RECIPE?,
    ): CATEGORY {
        recipes.mapNotNull { (id: ResourceLocation, recipe: RECIPE) -> factory(category, id, recipe) }.forEach(registry::addRecipe)
        return category
    }

    /**
     * 指定された[viewerType]から[HTEmiRecipeCategory]を返します。
     */
    private fun registerCategory(registry: EmiRegistry, viewerType: HTRecipeViewerType<*>): HTEmiRecipeCategory {
        val category: HTEmiRecipeCategory = HTEmiRecipeCategory.create(viewerType)
        registry.addCategory(category)
        viewerType.workStations.map(ItemLike::toEmi).forEach(registry::addWorkstation.partially1(category))
        return category
    }

    private fun addFuelRecipes(
        registry: EmiRegistry,
        viewerType: HTRecipeViewerType<HTEmiFluidFuelData>,
        dataMapType: DataMapType<Fluid, HTFluidFuelData>,
        energyRate: Int,
    ): HTEmiRecipeCategory {
        val fluidRegistry: Registry<Fluid> = EmiPort.getFluidRegistry()
        return addCategoryAndRecipes(
            registry,
            viewerType,
            fluidRegistry
                .getDataMap(dataMapType)
                .map { (key: ResourceKey<Fluid>, fuelData: HTFluidFuelData) ->
                    key.location().withPrefix("/${dataMapType.id().path}/") to
                        HTEmiFluidFuelData(
                            energyRate,
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
        addInteraction(output.toEmi(), prefix = "fluid_interaction") {
            leftInput(source.toFluidEmi(1000).copyAsCatalyst())
            rightInput(flowing.toFluidEmi(1000).copyAsCatalyst(), false)
        }
    }
}
