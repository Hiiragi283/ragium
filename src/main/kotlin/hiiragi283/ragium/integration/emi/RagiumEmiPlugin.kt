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
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTFluidWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToFluidRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToItemRecipe
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTDeferredRecipeType
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.recipe.HTBlastChargeRecipe
import hiiragi283.ragium.common.recipe.HTEternalTicketRecipe
import hiiragi283.ragium.common.recipe.HTIceCreamSodaRecipe
import hiiragi283.ragium.integration.emi.recipe.HTBlastChargeEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTCrushingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTDistillationEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTEternalTicketEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTFluidWithCatalystToItemEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTItemToItemEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTItemWithFluidToItemEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTItemWithItemToItemEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTMeltingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTMixingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTRefiningEmiRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
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

        registry.addWorkstation(VanillaEmiRecipeCategories.STONECUTTING, EmiStack.of(RagiumBlocks.Machines.ENGRAVER))
        // Recipe
        this.registry = registry
        recipeManager = registry.recipeManager

        addRecipes()
        // Functions
        registry.addGenericStackProvider(RagiumEmiStackProvider)

        registry.setDefaultComparison(
            RagiumItems.Tickets.RAGI.get(),
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
        crafting.forEach(recipeManager) { id: ResourceLocation, recipe: CraftingRecipe? ->
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
        // Block Action
        /*forEachRecipes(RagiumRecipeTypes.BLOCK_INTERACTING.get()) { id: ResourceLocation, recipe: HTBlockInteractingRecipe ->
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
        }*/
    }

    private fun addMachineRecipes() {
        // Alloying
        RagiumRecipeTypes.ALLOYING.forEach(recipeManager) { id: ResourceLocation, recipe: HTCombineItemToItemRecipe ->
            registry.addRecipe(
                HTItemWithItemToItemEmiRecipe.alloying(
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
            registry.addRecipe(
                HTCrushingEmiRecipe(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.resultMap.map { (result: HTItemResult, chance: Float) ->
                        result
                            .getStackResult()
                            .mapOrElse(
                                { stack: ItemStack -> EmiStack.of(stack).setChance(chance) },
                                ::createErrorStack,
                            )
                    },
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.CRUSHER.get(), HTRecipeHandler(RagiumEmiCategories.CRUSHING))
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
        // Pressing
        RagiumRecipeTypes.PRESSING.forEach(recipeManager) { id: ResourceLocation, recipe: HTItemWithCatalystToItemRecipe ->
            registry.addRecipe(
                HTItemWithItemToItemEmiRecipe.pressing(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.catalyst.toItemEmi(),
                    recipe.result.toEmi(),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.FORMING_PRESS.get(), HTRecipeHandler(RagiumEmiCategories.PRESSING))
        // Refining
        RagiumRecipeTypes.REFINING.forEach(recipeManager) { id: ResourceLocation, recipe: HTRefiningRecipe ->
            registry.addRecipe(
                HTRefiningEmiRecipe(
                    id.withPrefix("/"),
                    recipe.ingredient.toEmi(),
                    recipe.fluidResults[0].toEmi(),
                ),
            )

            registry.addRecipe(
                HTDistillationEmiRecipe(
                    id,
                    recipe.ingredient.toEmi(),
                    recipe.itemResult.map(HTItemResult::toEmi),
                    recipe.fluidResults.map(HTFluidResult::toEmi),
                ),
            )
        }
        registry.addRecipeHandler(RagiumMenuTypes.REFINERY.get(), HTRecipeHandler(RagiumEmiCategories.REFINING))
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
        addInteraction(EmiStack.of(Fluids.WATER), prefix = "fluid_generator") {
            leftInput(EmiStack.of(RagiumBlocks.Devices.WATER_COLLECTOR))
            rightInput(EmiStack.EMPTY, false)
        }
        // Lava Well
        addInteraction(EmiStack.of(Fluids.LAVA), prefix = "fluid_generator") {
            leftInput(EmiStack.of(RagiumBlocks.Devices.LAVA_COLLECTOR))
            rightInput(EmiStack.EMPTY, false)
        }
        // Milk Drain
        addInteraction(EmiStack.of(NeoForgeMod.MILK.get()), prefix = "fluid_generator") {
            leftInput(EmiStack.of(RagiumBlocks.Devices.MILK_DRAIN))
            rightInput(EmiStack.of(Items.COW_SPAWN_EGG), true)
        }
        // Exp Collector
        addInteraction(EmiStack.of(RagiumFluidContents.EXPERIENCE.get()), prefix = "fluid_generator") {
            leftInput(EmiStack.of(RagiumBlocks.Devices.EXP_COLLECTOR))
            rightInput(EmiStack.EMPTY, false)
        }

        // Bottled Bee
        addInteraction(EmiStack.of(RagiumItems.BOTTLED_BEE)) {
            leftInput(EmiStack.of(Items.GLASS_BOTTLE))
            rightInput(EmiStack.of(Items.BEE_SPAWN_EGG), false)
        }

        // Cauldron Interaction
        addInteraction(EmiStack.of(Items.MUSHROOM_STEW), prefix = "cauldron") {
            leftInput(EmiStack.of(Items.BOWL))
            rightInput(EmiStack.of(Items.CAULDRON), true)
            rightInput(EmiStack.of(RagiumFluidContents.MUSHROOM_STEW.get()), false)
        }

        // World Vaporization
        addInteraction(EmiStack.of(Items.SLIME_BALL)) {
            leftInput(EmiStack.of(RagiumFluidContents.SAP.getBucket()))
            rightInput(EmiStack.EMPTY, false)
        }
        addInteraction(EmiStack.of(Items.SUGAR)) {
            leftInput(EmiStack.of(RagiumFluidContents.SYRUP.getBucket()))
            rightInput(EmiStack.EMPTY, false)
        }
        addInteraction(EmiStack.of(RagiumItems.getGem(RagiumMaterialType.CRIMSON_CRYSTAL))) {
            leftInput(EmiStack.of(RagiumFluidContents.CRIMSON_SAP.getBucket()))
            rightInput(EmiStack.EMPTY, false)
        }
        addInteraction(EmiStack.of(RagiumItems.getGem(RagiumMaterialType.WARPED_CRYSTAL))) {
            leftInput(EmiStack.of(RagiumFluidContents.WARPED_SAP.getBucket()))
            rightInput(EmiStack.EMPTY, false)
        }

        // Crude Oil + Lava -> Magma Block / Soul Sand
        addFluidInteraction(Items.SOUL_SAND, RagiumFluidContents.CRUDE_OIL.get(), Fluids.LAVA)
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

    private fun addFluidInteraction(output: ItemLike, source: Fluid, flowing: Fluid) {
        val outputStack: EmiStack = EmiStack.of(output)

        val sourceStack: EmiStack = EmiStack.of(source, 1000)
        val flowingStack: EmiStack = EmiStack.of(flowing, 1000)

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
