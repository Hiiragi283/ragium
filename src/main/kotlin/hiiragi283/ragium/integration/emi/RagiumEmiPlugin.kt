package hiiragi283.ragium.integration.emi

import com.mojang.logging.LogUtils
import dev.emi.emi.EmiPort
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiCraftingRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.stack.Comparison
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.data.HTFluidFuelData
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTFluidWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipeBase
import hiiragi283.ragium.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToFluidRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToItemRecipe
import hiiragi283.ragium.api.recipe.impl.HTPulverizingRecipe
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.registry.HTDeferredRecipeType
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.fluid.HTVaporizableFluidType
import hiiragi283.ragium.common.recipe.HTBlastChargeRecipe
import hiiragi283.ragium.common.recipe.HTDynamicRecipes
import hiiragi283.ragium.common.recipe.HTEternalTicketRecipe
import hiiragi283.ragium.common.recipe.HTIceCreamSodaRecipe
import hiiragi283.ragium.integration.emi.recipe.HTAlloyingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTBlastChargeEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTCrushingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTEternalTicketEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTFluidFuelEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTFluidWithCatalystToItemEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTItemToItemEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTItemWithFluidToItemEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTMeltingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTMixingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTRefiningEmiRecipe
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTDeviceVariant
import hiiragi283.ragium.util.variant.HTGeneratorVariant
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.datamaps.DataMapType
import org.slf4j.Logger
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

@EmiEntrypoint
class RagiumEmiPlugin : EmiPlugin {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    private lateinit var registry: EmiRegistry
    private lateinit var recipeManager: RecipeManager

    override fun register(registry: EmiRegistry) {
        // Category, Workstation
        RagiumEmiCategories.register(registry)
        // Recipe
        this.registry = registry
        recipeManager = registry.recipeManager

        addRecipes()
        // Functions
        registry.addGenericStackProvider(RagiumEmiStackProvider)

        registry.setDefaultComparison(
            RagiumItems.RAGI_TICKET.get(),
            Comparison.compareData { stack: EmiStack -> stack.get(RagiumDataComponents.LOOT_TABLE_ID.get()) },
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
        val crafting: HTDeferredRecipeType<CraftingInput, CraftingRecipe> =
            HTDeferredRecipeType.createType(vanillaId("crafting"))
        crafting.forEach(recipeManager) { id: ResourceLocation, recipe: CraftingRecipe ->
            if (recipe is HTIceCreamSodaRecipe) {
                EmiPort.getPotionRegistry().holders().forEach { holder: Holder.Reference<Potion> ->
                    addRecipeSafe(
                        holder.idOrThrow.withPrefix("/shapeless/ice_cream_soda/"),
                    ) { id: ResourceLocation ->
                        EmiCraftingRecipe(
                            listOf(
                                EmiStack.of(RagiumItems.ICE_CREAM),
                                EmiIngredient.of(RagiumCommonTags.Items.FOODS_CHERRY),
                                EmiStack.of(createPotionStack(holder)),
                                EmiIngredient.of(Tags.Items.DYES_GREEN),
                            ),
                            EmiStack.of(RagiumAPI.getInstance().createSoda(holder)),
                            id,
                            true,
                        )
                    }
                }
            }
            if (recipe is HTBlastChargeRecipe) {
                addRecipeSafe(id, ::HTBlastChargeEmiRecipe)
            }
            if (recipe is HTEternalTicketRecipe) {
                EmiPort
                    .getItemRegistry()
                    .holders()
                    .forEach { holder: Holder.Reference<Item> ->
                        val item: Item = holder.value()
                        if (!item.defaultInstance.isDamageableItem) return@forEach
                        addRecipeSafe(
                            holder.idOrThrow.withPrefix("/shapeless/eternal_ticket/"),
                        ) { id: ResourceLocation -> HTEternalTicketEmiRecipe(item, id) }
                    }
            }
        }
        // Fluid Fuel
        addFuelRecipes(RagiumDataMaps.COMBUSTION_FUEL, HTGeneratorVariant.COMBUSTION)
        addFuelRecipes(RagiumDataMaps.THERMAL_FUEL, HTGeneratorVariant.THERMAL)
    }

    private fun addFuelRecipes(dataMapType: DataMapType<Fluid, HTFluidFuelData>, variant: HTGeneratorVariant) {
        val fluidRegistry: Registry<Fluid> = EmiPort.getFluidRegistry()
        for ((key: ResourceKey<Fluid>, fuelData: HTFluidFuelData) in fluidRegistry.getDataMap(dataMapType)) {
            val fluid: Fluid = fluidRegistry.get(key) ?: continue
            if (!fluid.isSource(fluid.defaultFluidState())) continue
            registry.addRecipe(
                HTFluidFuelEmiRecipe(
                    RagiumEmiCategories.getGenerator(variant),
                    key.location().withPrefix("/${dataMapType.id().path}/"),
                    EmiStack.of(fluid).setAmount(fuelData.amount.toLong()),
                    variant.energyRate,
                ),
            )
        }
    }

    private fun addMachineRecipes() {
        // Alloying
        RagiumRecipeTypes.ALLOYING.forEach(recipeManager) { id: ResourceLocation, recipe: HTCombineItemToItemRecipe ->
            registry.addRecipe(
                HTAlloyingEmiRecipe(
                    id,
                    recipe.ingredients.map(HTItemIngredient::toEmi),
                    recipe.result.toEmi(),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.ALLOY_SMELTER.get(), HTRecipeHandler(RagiumEmiCategories.ALLOYING))
        // Compressing
        RagiumRecipeTypes.COMPRESSING.forEach(recipeManager) { id: ResourceLocation, recipe: HTItemToItemRecipe ->
            registry.addRecipe(
                HTItemToItemEmiRecipe.compressing(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.result.toEmi(),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.COMPRESSOR.get(), HTRecipeHandler(RagiumEmiCategories.COMPRESSING))
        // Crushing
        RagiumRecipeTypes.CRUSHING.forEach(recipeManager) { id: ResourceLocation, recipe: HTItemToChancedItemRecipe ->
            val recipe: HTCrushingEmiRecipe = when (recipe) {
                is HTItemToChancedItemRecipeBase -> HTCrushingEmiRecipe(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.results.map { result: HTItemToChancedItemRecipe.ChancedResult ->
                        result.toEmi().setChance(result.chance)
                    },
                )

                is HTPulverizingRecipe -> HTCrushingEmiRecipe(id, recipe.ingredient.toEmi(), listOf(recipe.result.toEmi()))

                else -> return@forEach
            }
            registry.addRecipe(recipe)
        }
        registry.addRecipeHandler(RagiumMenuTypes.CRUSHER.get(), HTRecipeHandler(RagiumEmiCategories.CRUSHING))
        registry.addRecipeHandler(RagiumMenuTypes.PULVERIZER.get(), HTRecipeHandler(RagiumEmiCategories.CRUSHING))
        // Engraving
        registry.addRecipeHandler(RagiumMenuTypes.ENGRAVER.get(), HTRecipeHandler(VanillaEmiRecipeCategories.STONECUTTING))
        // Extracting
        RagiumRecipeTypes.EXTRACTING.forEach(recipeManager) { id: ResourceLocation, recipe: HTItemToItemRecipe ->
            registry.addRecipe(
                HTItemToItemEmiRecipe.extracting(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.result.toEmi(),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.EXTRACTOR.get(), HTRecipeHandler(RagiumEmiCategories.EXTRACTING))
        // Infusing
        RagiumRecipeTypes.INFUSING.forEach(recipeManager) { id: ResourceLocation, recipe: HTItemWithFluidToItemRecipe ->
            registry.addRecipe(
                HTItemWithFluidToItemEmiRecipe.infusing(
                    id,
                    recipe.fluidIngredient.toFluidEmi(),
                    recipe.itemIngredient.toItemEmi(),
                    recipe.result.toEmi(),
                ),
            )
        }
        HTDynamicRecipes.bucketFilling().forEach { (id: ResourceLocation, recipe: HTItemWithFluidToItemRecipe) ->
            registry.addRecipe(
                HTItemWithFluidToItemEmiRecipe.infusing(
                    id,
                    recipe.fluidIngredient.toFluidEmi(),
                    recipe.itemIngredient.toItemEmi(),
                    recipe.result.toEmi(),
                ),
            )
        }

        registry.addRecipeHandler(RagiumMenuTypes.INFUSER.get(), HTRecipeHandler(RagiumEmiCategories.INFUSING))
        // Melting
        RagiumRecipeTypes.MELTING.forEach(recipeManager) { id: ResourceLocation, recipe: HTItemToFluidRecipe ->
            registry.addRecipe(
                HTMeltingEmiRecipe(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.result.toEmi(),
                ),
            )
        }
        HTDynamicRecipes.bucketEmptying().forEach { (id: ResourceLocation, recipe: HTItemToFluidRecipe) ->
            registry.addRecipe(
                HTMeltingEmiRecipe(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.result.toEmi(),
                ),
            )
        }

        registry.addRecipeHandler(RagiumMenuTypes.MELTER.get(), HTRecipeHandler(RagiumEmiCategories.MELTING))
        // Mixing
        RagiumRecipeTypes.MIXING.forEach(recipeManager) { id: ResourceLocation, recipe: HTItemWithFluidToFluidRecipe ->
            registry.addRecipe(
                HTMixingEmiRecipe(
                    id,
                    recipe.fluidIngredient.toFluidEmi(),
                    recipe.itemIngredient.toItemEmi(),
                    recipe.result.toEmi(),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.MIXER.get(), HTRecipeHandler(RagiumEmiCategories.MIXING))
        // Refining
        RagiumRecipeTypes.REFINING.forEach(recipeManager) { id: ResourceLocation, recipe: HTRefiningRecipe ->
            registry.addRecipe(
                HTRefiningEmiRecipe(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.catalyst.toItemEmi(),
                    recipe.result.toEmi(),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.REFINERY.get(), HTRecipeHandler(RagiumEmiCategories.REFINING))
        // Smelting
        registry.addRecipeHandler(RagiumMenuTypes.SMELTER.get(), HTRecipeHandler(RagiumEmiCategories.SMELTING))
        // Solidifying
        RagiumRecipeTypes.SOLIDIFYING.forEach(recipeManager) { id: ResourceLocation, recipe: HTFluidWithCatalystToItemRecipe ->
            registry.addRecipe(
                HTFluidWithCatalystToItemEmiRecipe.solidifying(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.catalyst.toItemEmi(),
                    recipe.result.toEmi(),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.SOLIDIFIER.get(), HTRecipeHandler(RagiumEmiCategories.SOLIDIFYING))
    }

    /*private fun addBucketExtracting(holder: Holder.Reference<Fluid>) {
        // 液体源でない場合はスキップ
        val fluid: Fluid = holder.value()
        if (!fluid.isSource(fluid.defaultFluidState())) return
        // バケツが存在しない場合はスキップ
        val bucket: ItemStack = FluidUtil.getFilledBucket(FluidStack(fluid, 1))
        if (bucket.isEmpty) return
        addRecipeSafe(holder.idOrThrow.withPrefix("/extracting/bucket/")) { id: ResourceLocation ->
            HTExtractingEmiRecipe(
                id,
                HTRecipeDefinition(
                    listOf(
                        SizedIngredient.of(bucket.item, 1),
                    ),
                    listOf(),
                    listOf(
                        HTItemOutput.of(bucket.craftingRemainingItem),
                    ),
                    listOf(
                        HTFluidOutput.of(fluid, 1000),
                    ),
                ),
            )
        }
    }

    private fun addBucketFilling(holder: Holder.Reference<Fluid>) {
        // 液体源でない場合はスキップ
        val fluid: Fluid = holder.value()
        if (!fluid.isSource(fluid.defaultFluidState())) return
        // バケツが存在しない場合はスキップ
        val bucket: ItemStack = FluidUtil.getFilledBucket(FluidStack(fluid, 1))
        if (bucket.isEmpty) return
        addRecipeSafe(holder.idOrThrow.withPrefix("/infusing/bucket/")) { id: ResourceLocation ->
            HTInfusingEmiRecipe(
                id,
                HTRecipeDefinition(
                    listOf(
                        SizedIngredient.of(bucket.craftingRemainingItem.item, 1),
                    ),
                    listOf(
                        SizedFluidIngredient.of(fluid, 1000),
                    ),
                    listOf(
                        HTItemOutput.of(bucket),
                    ),
                    listOf(),
                ),
            )
        }
    }*/

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
        for (fluid: Fluid in HTDynamicRecipes.fluidStream()) {
            val type: FluidType = fluid.fluidType
            if (type is HTVaporizableFluidType) {
                addInteraction(type.drop.toEmi()) {
                    leftInput(EmiStack.of(fluid.bucket))
                    rightInput(EmiStack.EMPTY, false)
                }
            }
        }
        // Crude Oil + Lava -> Magma Block / Soul Sand
        addFluidInteraction(Items.SOUL_SAND, RagiumFluidContents.CRUDE_OIL, HTFluidContent.LAVA)
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
