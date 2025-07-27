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
import dev.emi.emi.recipe.special.EmiSmithingTrimRecipe
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.interaction.HTBlockAction
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTBlockInteractingRecipe
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTInfusingRecipe
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTBlockInteractingRecipeImpl
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTInfusingRecipeImpl
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.custom.HTBlastChargeRecipe
import hiiragi283.ragium.common.recipe.custom.HTEternalTicketRecipe
import hiiragi283.ragium.common.recipe.custom.HTIceCreamSodaRecipe
import hiiragi283.ragium.integration.emi.recipe.HTAlloyingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTBlastChargeEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTDecomposeEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTDistillationEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTInfusingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTMeltingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTPressingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTRefiningEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTSolidifyingEmiRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.SizedIngredient
import org.slf4j.Logger

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
        forEachRecipes(RecipeType.CRAFTING) { id: ResourceLocation, recipe: CraftingRecipe? ->
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
        }
        // Smithing
        addRecipeSafe(HTEternalTicketRecipe) { recipe: HTEternalTicketRecipe ->
            EmiSmithingTrimRecipe(
                EmiStack.of(RagiumItems.ETERNAL_TICKET),
                EmiPort
                    .getItemRegistry()
                    .holders()
                    .filter { holder: Holder<Item> -> holder.value().defaultInstance.isDamageableItem }
                    .toEmi(),
                EmiStack.EMPTY,
                EmiStack.EMPTY,
                recipe,
            )
        }
        // Block Action
        forEachRecipes(RagiumRecipeTypes.BLOCK_INTERACTING.get()) { id: ResourceLocation, recipe: HTBlockInteractingRecipe ->
            if (recipe is HTBlockInteractingRecipeImpl) {
                val firstStack: ItemStack = recipe.actions
                    .filterIsInstance<HTBlockAction.ItemPreview>()
                    .firstOrNull()
                    ?.getPreviewStack() ?: return@forEachRecipes
                addInteraction(EmiStack.of(firstStack), id) {
                    leftInput(recipe.blocks.toEmi())
                    rightInput(EmiIngredient.of(recipe.ingredient), false)
                }
            }
        }
    }

    private fun addMachineRecipes() {
        // Alloying
        forEachRecipes(RagiumRecipeTypes.ALLOYING.get()) { id: ResourceLocation, recipe: HTAlloyingRecipe ->
            registry.addRecipe(
                HTAlloyingEmiRecipe(
                    id,
                    recipe.ingredients.map(SizedIngredient::toEmi),
                    recipe.outputs.map(HTItemOutput::toEmi),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.ALLOY_SMELTER.get(), HTRecipeHandler(RagiumEmiCategories.ALLOYING))
        // Crushing
        forEachRecipes(RagiumRecipeTypes.CRUSHING.get()) { id: ResourceLocation, recipe: HTCrushingRecipe ->
            registry.addRecipe(
                HTDecomposeEmiRecipe.crushing(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.outputs.map(HTItemOutput::toEmi),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.CRUSHER.get(), HTRecipeHandler(RagiumEmiCategories.CRUSHING))
        // Extracting
        forEachRecipes(RagiumRecipeTypes.EXTRACTING.get()) { id: ResourceLocation, recipe: HTExtractingRecipe ->
            registry.addRecipe(
                HTDecomposeEmiRecipe.extracting(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.output.toEmi(),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.EXTRACTOR.get(), HTRecipeHandler(RagiumEmiCategories.EXTRACTING))
        // Infusing
        forEachRecipes(RagiumRecipeTypes.INFUSING.get()) { id: ResourceLocation, recipe: HTInfusingRecipe ->
            if (recipe is HTInfusingRecipeImpl) {
                registry.addRecipe(
                    HTInfusingEmiRecipe(
                        id,
                        EmiIngredient.of(recipe.ingredient),
                        recipe.result.toEmi(),
                        recipe.cost,
                    ),
                )
            }
        }
        registry.addRecipeHandler(RagiumMenuTypes.INFUSER.get(), HTRecipeHandler(RagiumEmiCategories.INFUSING))
        // Melting
        forEachRecipes(RagiumRecipeTypes.MELTING.get()) { id: ResourceLocation, recipe: HTMeltingRecipe ->
            registry.addRecipe(
                HTMeltingEmiRecipe(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.output.toEmi(),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.MELTER.get(), HTRecipeHandler(RagiumEmiCategories.MELTING))
        // Pressing
        forEachRecipes(RagiumRecipeTypes.PRESSING.get()) { id: ResourceLocation, recipe: HTPressingRecipe ->
            registry.addRecipe(
                HTPressingEmiRecipe(
                    id,
                    recipe.ingredient.toEmi(),
                    EmiIngredient.of(recipe.catalyst),
                    recipe.output.toEmi(),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.FORMING_PRESS.get(), HTRecipeHandler(RagiumEmiCategories.PRESSING))
        // Refining
        forEachRecipes(RagiumRecipeTypes.REFINING.get()) { id: ResourceLocation, recipe: HTRefiningRecipe ->
            registry.addRecipe(
                HTRefiningEmiRecipe(
                    id.withPrefix("/"),
                    recipe.ingredient.toEmi(),
                    recipe.fluidOutputs[0].toEmi(),
                ),
            )

            registry.addRecipe(
                HTDistillationEmiRecipe(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.itemOutput.map(HTItemOutput::toEmi),
                    recipe.fluidOutputs.map(HTFluidOutput::toEmi),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.REFINERY.get(), HTRecipeHandler(RagiumEmiCategories.REFINING))
        // Solidifying
        forEachRecipes(RagiumRecipeTypes.SOLIDIFYING.get()) { id: ResourceLocation, recipe: HTSolidifyingRecipe ->
            registry.addRecipe(
                HTSolidifyingEmiRecipe(
                    id,
                    recipe.ingredient.toEmi(),
                    EmiIngredient.of(recipe.catalyst),
                    recipe.output.toEmi(),
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
        addRecipeSafe(RagiumAPI.id("/world/fluid_generator/water_well")) { id: ResourceLocation ->
            EmiWorldInteractionRecipe
                .builder()
                .id(id)
                .leftInput(EmiStack.of(RagiumBlocks.WATER_COLLECTOR))
                .rightInput(EmiStack.EMPTY, false)
                .output(EmiStack.of(Fluids.WATER))
                .build()
        }
        // Lava Well
        addRecipeSafe(RagiumAPI.id("/world/fluid_generator/lava_well")) { id: ResourceLocation ->
            EmiWorldInteractionRecipe
                .builder()
                .id(id)
                .leftInput(EmiStack.of(RagiumBlocks.LAVA_COLLECTOR))
                .rightInput(EmiStack.EMPTY, false)
                .output(EmiStack.of(Fluids.LAVA))
                .build()
        }
        // Milk Drain
        addRecipeSafe(RagiumAPI.id("/world/fluid_generator/milk_drain")) { id: ResourceLocation ->
            EmiWorldInteractionRecipe
                .builder()
                .id(id)
                .leftInput(EmiStack.of(RagiumBlocks.MILK_DRAIN))
                .rightInput(EmiStack.of(Items.COW_SPAWN_EGG), true)
                .output(EmiStack.of(NeoForgeMod.MILK.get()))
                .build()
        }
        // Exp Collector
        addRecipeSafe(RagiumAPI.id("/world/fluid_generator/exp_collector")) { id: ResourceLocation ->
            EmiWorldInteractionRecipe
                .builder()
                .id(id)
                .leftInput(EmiStack.of(RagiumBlocks.EXP_COLLECTOR))
                .rightInput(EmiStack.EMPTY, false)
                .output(EmiStack.of(RagiumFluidContents.EXPERIENCE.get()))
                .build()
        }

        // Bottled Bee
        addInteraction(EmiStack.of(RagiumItems.BOTTLED_BEE)) {
            leftInput(EmiStack.of(Items.GLASS_BOTTLE))
            rightInput(EmiStack.of(Items.BEE_SPAWN_EGG), false)
        }

        // Cauldron Interaction
        addInteraction(EmiStack.of(Items.MUSHROOM_STEW), RagiumAPI.id("/world/cauldron/mushroom_stew")) {
            leftInput(EmiStack.of(Items.BOWL))
            rightInput(EmiStack.of(Items.CAULDRON), true)
            rightInput(EmiStack.of(RagiumFluidContents.MUSHROOM_STEW.get()), false)
        }
    }

    private fun addInteraction(
        output: EmiStack,
        id: ResourceLocation = output.id,
        builderAction: EmiWorldInteractionRecipe.Builder.() -> Unit,
    ) {
        addRecipeSafe(id.withPrefix("/world/interaction/")) { id1: ResourceLocation ->
            EmiWorldInteractionRecipe
                .builder()
                .apply(builderAction)
                .id(id1)
                .output(output)
                .build()
        }
    }

    //    Extension    //

    private inline fun <I : RecipeInput, R : Recipe<I>> forEachRecipes(recipeType: RecipeType<R>, action: (ResourceLocation, R) -> Unit) {
        for (holder: RecipeHolder<R> in recipeManager.getAllRecipesFor(recipeType)) {
            val id: ResourceLocation = holder.id
            val recipe: R = holder.value
            action(id, recipe)
        }
    }

    /**
     * @see dev.emi.emi.VanillaPlugin.addRecipeSafe
     */
    private inline fun <T : Recipe<*>> addRecipeSafe(recipe: T, factory: (T) -> EmiRecipe) {
        addRecipeSafe(EmiPort.getId(recipe)) { factory(recipe) }
    }

    private inline fun addRecipeSafe(id: ResourceLocation, factory: (ResourceLocation) -> EmiRecipe) {
        runCatching {
            registry.addRecipe(factory(id))
        }.onFailure { throwable: Throwable ->
            LOGGER.warn("Exception thrown when parsing vanilla recipe: $id", throwable)
        }
    }
}
