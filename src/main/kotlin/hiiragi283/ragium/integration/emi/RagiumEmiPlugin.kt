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
import hiiragi283.ragium.api.data.HTFluidFuelData
import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.api.extension.holdersSequence
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.HTFluidTransformRecipe
import hiiragi283.ragium.api.recipe.HTRecipeGetter
import hiiragi283.ragium.api.recipe.HTSingleInputFluidRecipe
import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.fluid.HTFluidType
import hiiragi283.ragium.common.recipe.HTIceCreamSodaRecipe
import hiiragi283.ragium.common.recipe.HTSmithingModifyRecipe
import hiiragi283.ragium.common.util.HTRegistryHelper
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.impl.recipe.HTCompressingRecipe
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe
import hiiragi283.ragium.impl.recipe.HTExtractingRecipe
import hiiragi283.ragium.impl.recipe.HTMeltingRecipe
import hiiragi283.ragium.impl.recipe.HTPulverizingRecipe
import hiiragi283.ragium.impl.recipe.HTSawmillRecipe
import hiiragi283.ragium.impl.recipe.HTWashingRecipe
import hiiragi283.ragium.impl.recipe.base.HTChancedItemRecipeBase
import hiiragi283.ragium.integration.emi.recipe.HTAlloyingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTCrushingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTCuttingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTFluidFuelEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTFluidTransformingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTItemToItemEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTMeltingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTSimulatingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTSmithingModifyEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTWashingEmiRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.item.crafting.SmithingRecipe
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.datamaps.DataMapType
import org.slf4j.Logger

@EmiEntrypoint
class RagiumEmiPlugin : EmiPlugin {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    private lateinit var registry: EmiRegistry
    private lateinit var recipeGetter: HTRecipeGetter

    override fun register(registry: EmiRegistry) {
        // Category, Workstation
        RagiumEmiCategories.register(registry)
        // Recipe
        this.registry = registry
        recipeGetter = RagiumAPI.INSTANCE.wrapRecipeManager(registry.recipeManager)

        addRecipes()
        // Functions
        registry.addGenericStackProvider(RagiumEmiStackProvider)

        registry.setDefaultComparison(
            RagiumItems.LOOT_TICKET.get(),
            Comparison.compareData { stack: EmiStack -> stack.get(RagiumDataComponents.LOOT_TICKET) },
        )
    }

    //    Recipes    //

    private fun addRecipes() {
        addCustomRecipe()
        addMachineRecipes()
        addInteractions()
    }

    private fun addCustomRecipe() {
        // Crafting
        RagiumRecipeTypes.CRAFTING.forEach(recipeGetter) { _: ResourceLocation, recipe: CraftingRecipe ->
            if (recipe is HTIceCreamSodaRecipe) {
                EmiPort.getPotionRegistry().holdersSequence().forEach { holder: Holder<Potion> ->
                    addRecipeSafe(
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
            }
        }
        // Smithing
        RagiumRecipeTypes.SMITHING.forEach(recipeGetter) { _: ResourceLocation, recipe: SmithingRecipe ->
            if (recipe is HTSmithingModifyRecipe) {
                registry.addRecipe(
                    HTSmithingModifyEmiRecipe(
                        recipe.template.let(EmiIngredient::of),
                        recipe.addition.let(EmiIngredient::of),
                        recipe,
                    ),
                )
            }
        }
        // Fluid Fuel
        addFuelRecipes(RagiumDataMaps.INSTANCE.combustionFuelType, HTGeneratorVariant.COMBUSTION)
        addFuelRecipes(RagiumDataMaps.INSTANCE.thermalFuelType, HTGeneratorVariant.THERMAL)
    }

    private fun addFuelRecipes(dataMapType: DataMapType<Fluid, HTFluidFuelData>, variant: HTGeneratorVariant) {
        val fluidRegistry: Registry<Fluid> = EmiPort.getFluidRegistry()
        for ((key: ResourceKey<Fluid>, fuelData: HTFluidFuelData) in fluidRegistry.getDataMap(dataMapType)) {
            fluidRegistry.getOptional(key).ifPresent { fluid: Fluid ->
                if (fluid.isSource(fluid.defaultFluidState())) {
                    registry.addRecipe(
                        HTFluidFuelEmiRecipe(
                            RagiumEmiCategories.getGenerator(variant),
                            key.location().withPrefix("/${dataMapType.id().path}/"),
                            EmiStack.of(fluid),
                            fuelData.amount,
                            variant.energyRate,
                        ),
                    )
                }
            }
        }
    }

    private fun addMachineRecipes() {
        // Alloying
        RagiumRecipeTypes.ALLOYING.forEach(recipeGetter) { id: ResourceLocation, recipe: HTCombineItemToItemRecipe ->
            registry.addRecipe(
                HTAlloyingEmiRecipe(
                    id,
                    recipe.ingredients.map(HTItemIngredient::toEmi),
                    recipe.result.toEmi(),
                ),
            )
        }
        // Compressing
        RagiumRecipeTypes.COMPRESSING.forEach(recipeGetter) { id: ResourceLocation, recipe: HTSingleInputRecipe ->
            if (recipe is HTCompressingRecipe) {
                registry.addRecipe(
                    HTItemToItemEmiRecipe.compressing(
                        id,
                        recipe.ingredient.toEmi(),
                        recipe.result.toEmi(),
                    ),
                )
            }
        }
        // Crushing
        RagiumRecipeTypes.CRUSHING.forEach(recipeGetter) { id: ResourceLocation, recipe: HTItemToChancedItemRecipe ->
            val recipe: HTCrushingEmiRecipe = when (recipe) {
                is HTCrushingRecipe -> HTCrushingEmiRecipe(id, recipe.ingredient.toEmi(), toEmiStacks(recipe))

                is HTPulverizingRecipe -> HTCrushingEmiRecipe(id, recipe.ingredient.toEmi(), listOf(recipe.result.toEmi()))

                else -> return@forEach
            }
            registry.addRecipe(recipe)
        }
        // Cutting
        RagiumRecipeTypes.SAWMILL.forEach(recipeGetter) { id: ResourceLocation, recipe: HTSingleInputRecipe ->
            if (recipe is HTSawmillRecipe) {
                registry.addRecipe(
                    HTCuttingEmiRecipe(
                        id.withPrefix("/"),
                        recipe.ingredient.toEmi(),
                        recipe.result.toEmi(),
                    ),
                )
            }
        }
        RagiumRecipeTypes.STONECUTTER.forEach(recipeGetter) { id: ResourceLocation, recipe: SingleItemRecipe ->
            registry.addRecipe(
                HTCuttingEmiRecipe(
                    id.withPrefix("/"),
                    EmiIngredient.of(recipe.ingredients[0]),
                    RagiumAPI
                        .INSTANCE
                        .getRegistryAccess()
                        ?.let(recipe::getResultItem)
                        ?.let(EmiStack::of)
                        ?: EmiStack.EMPTY,
                ),
            )
        }
        // Extracting
        RagiumRecipeTypes.EXTRACTING.forEach(recipeGetter) { id: ResourceLocation, recipe: HTSingleInputRecipe ->
            if (recipe is HTExtractingRecipe) {
                registry.addRecipe(
                    HTItemToItemEmiRecipe.extracting(
                        id,
                        recipe.ingredient.toEmi(),
                        recipe.result.toEmi(),
                    ),
                )
            }
        }
        // Fluid Transforming
        RagiumRecipeTypes.FLUID_TRANSFORM.forEach(recipeGetter) { id: ResourceLocation, recipe: HTFluidTransformRecipe ->
            registry.addRecipe(
                HTFluidTransformingEmiRecipe(
                    id,
                    recipe.fluidIngredient.toEmi(),
                    recipe.itemIngredient.toItemEmi(),
                    recipe.itemResult.toItemEmi(),
                    recipe.fluidResult.toFluidEmi(),
                ),
            )
        }
        // Melting
        RagiumRecipeTypes.MELTING.forEach(recipeGetter) { id: ResourceLocation, recipe: HTSingleInputFluidRecipe ->
            if (recipe is HTMeltingRecipe) {
                registry.addRecipe(
                    HTMeltingEmiRecipe(
                        id,
                        recipe.ingredient.toEmi(),
                        recipe.result.toEmi(),
                    ),
                )
            }
        }
        // Simulating
        RagiumRecipeTypes.SIMULATING.forEach(recipeGetter) { id: ResourceLocation, recipe: HTItemWithCatalystToItemRecipe ->
            registry.addRecipe(
                HTSimulatingEmiRecipe(
                    id,
                    recipe.ingredient.toItemEmi(),
                    recipe.catalyst.toEmi(),
                    recipe.result.toEmi(),
                ),
            )
        }
        // Washing
        RagiumRecipeTypes.WASHING.forEach(recipeGetter) { id: ResourceLocation, recipe: HTItemWithFluidToChancedItemRecipe ->
            if (recipe is HTWashingRecipe) {
                registry.addRecipe(
                    HTWashingEmiRecipe(
                        id,
                        recipe.ingredient.toEmi(),
                        recipe.fluidIngredient.toEmi(),
                        toEmiStacks(recipe),
                    ),
                )
            }
        }
    }

    private fun toEmiStacks(recipe: HTChancedItemRecipeBase<*>): List<EmiStack> =
        recipe.results.map { result: HTChancedItemRecipe.ChancedResult ->
            result.toEmi().setChance(result.chance)
        }

    private fun addInteractions() {
        // Water Well
        addInteraction(HTFluidContent.WATER.toFluidEmi(), prefix = "fluid_generator") {
            leftInput(EmiStack.of(HTDeviceVariant.WATER_COLLECTOR))
            rightInput(EmiStack.EMPTY, false)
        }
        // Lava Well
        addInteraction(HTFluidContent.LAVA.toFluidEmi(), prefix = "fluid_generator") {
            leftInput(EmiStack.of(HTDeviceVariant.LAVA_COLLECTOR))
            rightInput(EmiStack.EMPTY, false)
        }
        // Milk Drain
        addInteraction(HTFluidContent.MILK.toFluidEmi(), prefix = "fluid_generator") {
            leftInput(EmiStack.of(HTDeviceVariant.MILK_COLLECTOR))
            rightInput(EmiStack.of(Items.COW_SPAWN_EGG), true)
        }
        // Exp Collector
        addInteraction(EmiStack.of(RagiumFluidContents.EXPERIENCE.get()), prefix = "fluid_generator") {
            leftInput(EmiStack.of(HTDeviceVariant.EXP_COLLECTOR))
            rightInput(EmiStack.EMPTY, false)
        }

        // Bottled Bee
        addInteraction(EmiStack.of(RagiumItems.BOTTLED_BEE)) {
            leftInput(EmiStack.of(Items.GLASS_BOTTLE))
            rightInput(EmiStack.of(Items.BEE_SPAWN_EGG), false)
        }

        // World Vaporization
        for (fluid: Fluid in HTRegistryHelper.fluidSequence()) {
            val type: FluidType = fluid.fluidType
            if (type is HTFluidType) {
                val result: EmiStack = type.dropItem?.toEmi() ?: continue
                addInteraction(result) {
                    leftInput(EmiStack.of(fluid.bucket))
                    rightInput(EmiStack.EMPTY, false)
                }
            }
        }
        // Crude Oil + Lava -> Soul Sand
        addFluidInteraction(Items.SOUL_SAND, RagiumFluidContents.CRUDE_OIL, HTFluidContent.LAVA)

        // Water + Eldritch Flux -> Eldritch Stone
        addFluidInteraction(RagiumBlocks.ELDRITCH_STONE, HTFluidContent.WATER, RagiumFluidContents.ELDRITCH_FLUX)
    }

    private fun addInteraction(
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

    private fun addFluidInteraction(output: ItemLike, source: HTFluidContent<*, *, *>, flowing: HTFluidContent<*, *, *>) {
        val outputStack: EmiStack = EmiStack.of(output)

        val sourceStack: EmiStack = source.toFluidEmi(1000)
        val flowingStack: EmiStack = flowing.toFluidEmi(1000)

        addInteraction(outputStack, prefix = "fluid_interaction") {
            leftInput(sourceStack.copyAsCatalyst())
            rightInput(flowingStack.copyAsCatalyst(), false)
        }
    }

    //    Extension    //

    private inline fun addRecipeSafe(id: ResourceLocation, factory: (ResourceLocation) -> EmiRecipe) {
        runCatching {
            registry.addRecipe(factory(id))
        }.onFailure { throwable: Throwable ->
            LOGGER.warn("Exception thrown when parsing vanilla recipe: $id", throwable)
        }
    }
}
