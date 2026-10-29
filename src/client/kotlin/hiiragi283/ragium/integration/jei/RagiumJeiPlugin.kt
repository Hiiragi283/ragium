package hiiragi283.ragium.integration.jei

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTJeiDrawables
import hiiragi283.lib.integration.jei.HTJeiPlugin
import hiiragi283.lib.integration.jei.category.HTCatalysedSingleToSingleViewerRecipeCategory
import hiiragi283.lib.integration.jei.category.HTDoubleToSingleViewerRecipeCategory
import hiiragi283.lib.integration.jei.category.HTFluidSeparatingRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemSeparatingRecipeCategory
import hiiragi283.lib.integration.jei.category.HTSingleToMultiViewerRecipeCategory
import hiiragi283.lib.integration.jei.category.HTSingleToSingleViewerRecipeCategory
import hiiragi283.lib.integration.jei.ingredient.HTIngredientTypes
import hiiragi283.lib.item.HTPotionBasedItem
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.display.HTDisplayableContent
import hiiragi283.lib.recipe.viewer.HTDoubleToSingleViewerRecipe
import hiiragi283.lib.recipe.viewer.HTSingleToSingleViewerRecipe
import hiiragi283.lib.recipe.viewer.HTViewerRecipeHelper
import hiiragi283.lib.registry.asHolderSequence
import hiiragi283.lib.util.getOrNull
import hiiragi283.lib.util.toOptional
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.recipe.RTEnchantingRecipe
import hiiragi283.ragium.api.recipe.RTMixingRecipe
import hiiragi283.ragium.api.recipe.RTResourcePumpingRecipe
import hiiragi283.ragium.api.recipe.RTWashingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.integration.jei.advanced.HTFluidBucketRecipePlugin
import hiiragi283.ragium.integration.jei.advanced.HTPotionFluidRecipePlugin
import hiiragi283.ragium.integration.jei.category.RTAlloyingRecipeCategory
import hiiragi283.ragium.integration.jei.category.RTAssemblingRecipeCategory
import hiiragi283.ragium.integration.jei.category.RTMixingRecipeCategory
import hiiragi283.ragium.integration.jei.category.RTPlantingRecipeCategory
import hiiragi283.ragium.integration.jei.category.RTWashingRecipeCategory
import hiiragi283.ragium.recipe.viewer.RTMixingViewerRecipe
import hiiragi283.ragium.recipe.viewer.RTWashingViewerRecipe
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IPlatformFluidHelper
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.registration.IAdvancedRegistration
import mezz.jei.api.registration.IExtraIngredientRegistration
import mezz.jei.api.registration.IGuiHandlerRegistration
import mezz.jei.api.registration.IModIngredientRegistration
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.neoforged.neoforge.fluids.FluidStack

@JeiPlugin
class RagiumJeiPlugin : HTJeiPlugin(RagiumAPI.MOD_ID) {
    companion object {
        @JvmStatic
        private fun listItems(): Sequence<Holder.Reference<Item>> = HTPhysicalSideHelper
            .filteredLookup(BuiltInRegistries.ITEM)
            .asHolderSequence()
    }

    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        // Potion-Based Item
        listItems()
            .map(Holder<Item>::value)
            .forEach { item: Item ->
                if (item is HTPotionBasedItem) {
                    registration.registerFromDataComponentTypes(item, DataComponents.POTION_CONTENTS)
                }
            }

        // Tanks
        registration.registerFromDataComponentTypes(RagiumBlocks.POTION_TANK.asItem(), RagiumDataComponents.FLUID)
        registration.registerFromDataComponentTypes(RagiumBlocks.CREATIVE_TANK.asItem(), RagiumDataComponents.FLUID)
    }

    override fun <T : Any> registerFluidSubtypes(registration: ISubtypeRegistration, platformFluidHelper: IPlatformFluidHelper<T>) {
        registration.registerSubtypeInterpreter(
            NeoForgeTypes.FLUID_STACK,
            RagiumFluids.POTION.getOrThrow(),
        ) { stack: FluidStack, _ -> HTPotionHelper.getContentsNotEmpty(stack) }
    }

    override fun registerIngredients(registration: IModIngredientRegistration) {
        HTIngredientTypes.register(registration)
    }

    override fun registerExtraIngredients(registration: IExtraIngredientRegistration) {
        registration.addExtraIngredients(
            NeoForgeTypes.FLUID_STACK,
            HTPhysicalSideHelper
                .filteredLookup(BuiltInRegistries.POTION)
                .asHolderSequence()
                .filterNot(HTPotionHelper::isWater)
                .mapNotNull { HTPotionHelper.createFluid(it).getOrNull()?.create() }
                .toList(),
        )
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper
        HTJeiDrawables.init(guiHelper)

        registration.addRecipeCategories(
            // Mechanical
            RTAssemblingRecipeCategory(guiHelper),
            HTItemSeparatingRecipeCategory(
                RagiumJeiRecipeTypes.CENTRIFUGING,
                RagiumRecipeTypes.CENTRIFUGING,
                guiHelper.createDrawableItemLike(RagiumBlocks.CENTRIFUGE),
            ),
            HTSingleToSingleViewerRecipeCategory(
                RagiumJeiRecipeTypes.COMPRESSING,
                RagiumRecipeTypes.COMPRESSING,
                guiHelper.createDrawableItemLike(RagiumBlocks.COMPRESSOR),
            ),
            HTSingleToMultiViewerRecipeCategory(
                RagiumJeiRecipeTypes.CRUSHING,
                RagiumRecipeTypes.CRUSHING,
                guiHelper.createDrawableItemLike(RagiumBlocks.CRUSHER),
            ),
            HTSingleToMultiViewerRecipeCategory(
                RagiumJeiRecipeTypes.CUTTING,
                RagiumRecipeTypes.CUTTING,
                guiHelper.createDrawableItemLike(RagiumBlocks.CUTTING_MACHINE),
            ),
            HTDoubleToSingleViewerRecipeCategory(
                RagiumJeiRecipeTypes.FILLING,
                RagiumRecipeTypes.FILLING,
                guiHelper.createDrawableItemLike(RagiumBlocks.MACHINE_CASING),
                HTBackgroundType.INPUT,
            ),
            HTCatalysedSingleToSingleViewerRecipeCategory(
                RagiumJeiRecipeTypes.PRESSING,
                RagiumRecipeTypes.PRESSING,
                guiHelper.createDrawableItemLike(RagiumBlocks.PRESS_MACHINE),
            ),
            // Heat
            RTAlloyingRecipeCategory(guiHelper),
            HTCatalysedSingleToSingleViewerRecipeCategory(
                RagiumJeiRecipeTypes.FREEZING,
                RagiumRecipeTypes.FREEZING,
                guiHelper.createDrawableItemLike(RagiumBlocks.FREEZER),
            ),
            HTSingleToSingleViewerRecipeCategory(
                RagiumJeiRecipeTypes.MELTING,
                RagiumRecipeTypes.MELTING,
                guiHelper.createDrawableItemLike(RagiumBlocks.MELTER),
            ),
            HTItemSeparatingRecipeCategory(
                RagiumJeiRecipeTypes.PYROLYZING,
                RagiumRecipeTypes.PYROLYZING,
                guiHelper.createDrawableItemLike(RagiumBlocks.PYROLYZER),
            ),
            HTFluidSeparatingRecipeCategory(
                RagiumJeiRecipeTypes.REFINING,
                RagiumRecipeTypes.REFINING,
                guiHelper.createDrawableItemLike(RagiumBlocks.REFINERY),
            ),
            // Chemical
            HTDoubleToSingleViewerRecipeCategory(
                RagiumJeiRecipeTypes.BATHING,
                RagiumRecipeTypes.BATHING,
                guiHelper.createDrawableItemLike(RagiumBlocks.CHEMICAL_BATH),
                HTBackgroundType.INPUT,
            ),
            RTMixingRecipeCategory(guiHelper),
            // RTReactingRecipeCategory(guiHelper),
            RTWashingRecipeCategory(guiHelper),
            // Resource
            HTDoubleToSingleViewerRecipeCategory(
                RagiumJeiRecipeTypes.BREWING,
                RagiumRecipeTypes.BREWING,
                guiHelper.createDrawableItemLike(RagiumBlocks.BREWERY),
                HTBackgroundType.INPUT,
            ),
            RTPlantingRecipeCategory(guiHelper),
            HTSingleToSingleViewerRecipeCategory(
                RagiumJeiRecipeTypes.RESOURCE_PUMPING,
                RagiumRecipeTypes.RESOURCE_PUMPING,
                guiHelper.createDrawableItemLike(RagiumBlocks.RESOURCE_PUMP),
            ),
            // Electronics
            HTFluidSeparatingRecipeCategory(
                RagiumJeiRecipeTypes.ELECTROLYZING,
                RagiumRecipeTypes.ELECTROLYZING,
                guiHelper.createDrawableItemLike(RagiumBlocks.ELECTROLYZER),
            ),
            HTCatalysedSingleToSingleViewerRecipeCategory(
                RagiumJeiRecipeTypes.ENGRAVING,
                RagiumRecipeTypes.ENGRAVING,
                guiHelper.createDrawableItemLike(RagiumBlocks.ENGRAVER),
            ),
            // Arcane
            HTDoubleToSingleViewerRecipeCategory(
                RagiumJeiRecipeTypes.ENCHANTING,
                RagiumRecipeTypes.ENCHANTING,
                guiHelper.createDrawableItemLike(RagiumBlocks.ENCHANTER),
                HTBackgroundType.EXTRA_INPUT,
            ),
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        // Mechanical
        registration.addRecipes(RagiumJeiRecipeTypes.ASSEMBLING, RagiumRecipeLookups.ASSEMBLING, HTViewerRecipeHelper::sizedShapeless)
        registration.addRecipes(RagiumJeiRecipeTypes.CENTRIFUGING, RagiumRecipeLookups.CENTRIFUGING, HTViewerRecipeHelper::itemSeparating)
        registration.addRecipes(RagiumJeiRecipeTypes.COMPRESSING, RagiumRecipeLookups.COMPRESSING, HTViewerRecipeHelper::singleToSingle)
        registration.addRecipes(RagiumJeiRecipeTypes.CRUSHING, RagiumRecipeLookups.CRUSHING, HTViewerRecipeHelper::singleToMultiItem)
        registration.addRecipes(RagiumJeiRecipeTypes.CUTTING, RagiumRecipeLookups.CUTTING, HTViewerRecipeHelper::singleToMultiItem)
        registration.addRecipes(RagiumJeiRecipeTypes.FILLING, RagiumRecipeLookups.FILLING, HTViewerRecipeHelper::itemAndFluidToItem)
        registration.addRecipes(RagiumJeiRecipeTypes.PRESSING, RagiumRecipeLookups.PRESSING, HTViewerRecipeHelper::catalysedSingleToSingle)
        // Heat
        registration.addRecipes(RagiumJeiRecipeTypes.ALLOYING, RagiumRecipeLookups.ALLOYING, HTViewerRecipeHelper::sizedShapeless)
        registration.addRecipes(RagiumJeiRecipeTypes.FREEZING, RagiumRecipeLookups.FREEZING, HTViewerRecipeHelper::catalysedSingleToSingle)
        registration.addRecipes(RagiumJeiRecipeTypes.MELTING, RagiumRecipeLookups.MELTING, HTViewerRecipeHelper::singleToSingle)
        registration.addRecipes(RagiumJeiRecipeTypes.PYROLYZING, RagiumRecipeLookups.PYROLYZING, HTViewerRecipeHelper::itemSeparating)
        registration.addRecipes(RagiumJeiRecipeTypes.REFINING, RagiumRecipeLookups.REFINING, HTViewerRecipeHelper::fluidSeparating)
        // Chemical
        registration.addRecipes(RagiumJeiRecipeTypes.BATHING, RagiumRecipeLookups.BATHING, HTViewerRecipeHelper::itemAndFluidToItem)
        registration.addRecipes(RagiumJeiRecipeTypes.MIXING, RagiumRecipeLookups.MIXING) { (key: RecipeKey, recipe: RTMixingRecipe) ->
            RTMixingViewerRecipe(
                key.identifier(),
                recipe.ingredients.getLeft()?.map(HTDisplayableContent::display) ?: listOf(),
                recipe.ingredients.getRight()?.map(HTDisplayableContent::display) ?: listOf(),
                recipe.result.content.getLeft()?.display().toOptional(),
                recipe.result.content.getRight()?.display().toOptional(),
                recipe.progressData,
            )
        }
        // registration.addRecipes(RagiumJeiRecipeTypes.REACTING, RagiumRecipeLookups.REACTING)
        registration.addRecipes(RagiumJeiRecipeTypes.WASHING, RagiumRecipeLookups.WASHING) { (key: RecipeKey, recipe: RTWashingRecipe) ->
            RTWashingViewerRecipe(
                key.identifier(),
                recipe.itemIngredient.display(),
                recipe.fluidIngredient.display(),
                recipe.results.map(HTDisplayableContent::display),
                recipe.progressData,
            )
        }
        // Resource
        registration.addRecipes(RagiumJeiRecipeTypes.BREWING, RagiumRecipeLookups.BREWING, HTViewerRecipeHelper::itemAndFluidToFluid)
        registration.addRecipes(RagiumJeiRecipeTypes.PLANTING, RagiumRecipeLookups.PLANTING, HTViewerRecipeHelper::singleToSingle)
        // Electronics
        registration.addRecipes(RagiumJeiRecipeTypes.ELECTROLYZING, RagiumRecipeLookups.ELECTROLYZING, HTViewerRecipeHelper::fluidSeparating)
        registration.addRecipes(RagiumJeiRecipeTypes.ENGRAVING, RagiumRecipeLookups.ENGRAVING, HTViewerRecipeHelper::catalysedSingleToSingle)
        registration.addRecipes(
            RagiumJeiRecipeTypes.RESOURCE_PUMPING,
            RagiumRecipeLookups.RESOURCE_PUMPING,
        ) { (key: RecipeKey, recipe: RTResourcePumpingRecipe) ->
            HTSingleToSingleViewerRecipe(
                key.identifier(),
                recipe.condition.display(),
                recipe.result.display(),
                recipe.progressData,
            )
        }
        // Arcane
        registration.addRecipes(
            RagiumJeiRecipeTypes.ENCHANTING,
            RagiumRecipeLookups.ENCHANTING,
        ) { (key: RecipeKey, recipe: RTEnchantingRecipe) ->
            HTDoubleToSingleViewerRecipe(
                key.identifier(),
                RTEnchantingRecipe.BOOK_INGREDIENT.display(),
                recipe.ingredient.display(),
                recipe.result.display(),
                recipe.progressData,
            )
        }

        // registerDynamicRecipes(registration)
    }

    /*private fun registerDynamicRecipes(registration: IRecipeRegistration) {
        val fluidHelper: IIngredientHelper<FluidStack> =
            registration.ingredientManager.getIngredientHelper(NeoForgeTypes.FLUID_STACK)
        // Mechanical
        registration.addRecipes(
            RagiumJeiRecipeTypes.CENTRIFUGING,
            listPotions()
                .flatMap { potion: Holder.Reference<Potion> ->
                    HTBottleType.entries.map { bottleType: HTBottleType ->
                        RagiumRecipeBuilders.centrifuging {
                            +HTJeiRecipeHelper.fakeItem(
                                bottleType.filledItem.asStackOrEmpty(patch = HTPotionHelper.createPotionPatch(potion))
                            )
                            itemResult { +bottleType.emptyItem }
                            fluidResult {
                                from(HTPotionHelper.createFluid(potion, HTPotionHelper.BOTTLE_AMOUNT).getOrThrow())
                            }
                            progressData = RTPotionBottleCentrifugingRecipe.PROGRESS_DATA
                            recipeId replace potion.getKeyOrThrow()
                                .identifier()
                                .withPrefix("potion_bottle/${bottleType.serializedName}/")
                        }.buildSynthetic()
                    }
                }.map(::HTJeiRecipeHolder)
                .toList()
        )
        registration.addRecipes(
            RagiumJeiRecipeTypes.CENTRIFUGING,
            listFluidStacks(registration.jeiHelpers)
                .mapNotNull { fluid: FluidStack ->
                    val filledBucket: ItemStack = fluid.fluidType.getBucket(fluid)
                    val remainder: HTItemResult =
                        filledBucket.craftingRemainder?.let(::HTItemResult) ?: return@mapNotNull null
                    RagiumRecipeBuilders.centrifuging {
                        ingredient = HTJeiRecipeHelper.fakeItem(filledBucket)
                        +remainder
                        fluidResult { from(fluid) }
                        progressData = RTBucketCentrifugingRecipe.progressData
                    }.buildSynthetic { id: Identifier, recipe: RTCentrifugingRecipe.Basic ->
                        // 液体ポーションなどのバリエーションを持つ液体はIDなしで登録する
                        if (fluidHelper.getUid(fluid, UidContext.Recipe) is List<*>) {
                            HTJeiRecipeHolder(recipe)
                        } else {
                            HTJeiRecipeHolder(id, recipe)
                        }
                    }
                }
        )
        registration.addRecipes(
            RagiumJeiRecipeTypes.FILLING,
            listPotions()
                .flatMap { potion: Holder.Reference<Potion> ->
                    HTBottleType.entries.map { bottleType: HTBottleType ->
                        RagiumRecipeBuilders.filling {
                            itemIngredient { items { +bottleType.emptyItem } }
                            fluidIngredient {
                                +HTPotionFluidIngredient(potion)
                                amount = HTPotionHelper.BOTTLE_AMOUNT
                            }
                            result {
                                bottleType.filledItem
                                    .asTemplate(patch = HTPotionHelper.createPotionPatch(potion))
                                    .getOrThrow()
                                    .let(::from)
                            }
                            progressData = RTPotionBottleFillingRecipe.PROGRESS_DATA
                            recipeId replace potion.getKeyOrThrow()
                                .identifier()
                                .withPrefix("potion_bottle/${bottleType.serializedName}/")
                        }.buildSynthetic()
                    }
                }.map(::HTJeiRecipeHolder)
                .toList()
        )
        registration.addRecipes(
            RagiumJeiRecipeTypes.FILLING,
            listFluidStacks(registration.jeiHelpers)
                .mapNotNull { fluid: FluidStack ->
                    val filledBucket: ItemStack = fluid.fluidType.getBucket(fluid)
                    val remainder: ItemStack = filledBucket.craftingRemainder?.create() ?: return@mapNotNull null
                    RagiumRecipeBuilders.filling {
                        itemIngredient = HTJeiRecipeHelper.fakeItem(remainder)
                        fluidIngredient = HTJeiRecipeHelper.fakeFluid(fluid)
                        result { from(filledBucket) }
                        progressData = RTBucketFillingRecipe.progressData
                    }.buildSynthetic { id: Identifier, recipe: RTFillingRecipe ->
                        // 液体ポーションなどのバリエーションを持つ液体はIDなしで登録する
                        if (fluidHelper.getUid(fluid, UidContext.Recipe) is List<*>) {
                            HTJeiRecipeHolder(recipe)
                        } else {
                            HTJeiRecipeHolder(id, recipe)
                        }
                    }
                }
        )
        // Chemical
    }*/

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        // Mechanical
        registration.addCraftingStation(RagiumJeiRecipeTypes.ASSEMBLING, RagiumBlocks.ASSEMBLER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.CENTRIFUGING, RagiumBlocks.CENTRIFUGE)
        registration.addCraftingStation(RagiumJeiRecipeTypes.CRUSHING, RagiumBlocks.CRUSHER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.COMPRESSING, RagiumBlocks.COMPRESSOR)
        registration.addCraftingStation(RagiumJeiRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE)
        registration.addCraftingStation(RagiumJeiRecipeTypes.PRESSING, RagiumBlocks.PRESS_MACHINE)
        // Heat
        registration.addCraftingStation(RagiumJeiRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.FREEZING, RagiumBlocks.FREEZER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.MELTING, RagiumBlocks.MELTER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.PYROLYZING, RagiumBlocks.PYROLYZER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.REFINING, RagiumBlocks.REFINERY)
        registration.addCraftingStation(RecipeTypes.SMELTING, RagiumBlocks.SMELTER)
        registration.addCraftingStation(RecipeTypes.BLASTING, RagiumBlocks.SMELTER)
        registration.addCraftingStation(RecipeTypes.SMOKING, RagiumBlocks.SMELTER)
        // Chemical
        registration.addCraftingStation(RagiumJeiRecipeTypes.BATHING, RagiumBlocks.CHEMICAL_BATH)
        // registration.addCraftingStation(RagiumJeiRecipeTypes.REACTING, RagiumBlocks.CHEMICAL_REACTOR)
        registration.addCraftingStation(RagiumJeiRecipeTypes.MIXING, RagiumBlocks.MIXER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.MIXING, RagiumBlocks.BLENDER)
        // Resource
        registration.addCraftingStation(RagiumJeiRecipeTypes.BREWING, RagiumBlocks.BREWERY)
        registration.addCraftingStation(RagiumJeiRecipeTypes.PLANTING, RagiumBlocks.PLANTER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.RESOURCE_PUMPING, RagiumBlocks.RESOURCE_PUMP)
        // Electronics
        registration.addCraftingStation(RagiumJeiRecipeTypes.ENGRAVING, RagiumBlocks.ENGRAVER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.ELECTROLYZING, RagiumBlocks.ELECTROLYZER)
        // Arcane
        registration.addCraftingStation(RagiumJeiRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER)
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        registration.addGuiContainerHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
        registration.addGhostIngredientHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
    }

    override fun registerAdvanced(registration: IAdvancedRegistration) {
        registration.addRecipeManagerPlugin(HTFluidBucketRecipePlugin)
        registration.addRecipeManagerPlugin(HTPotionFluidRecipePlugin)
    }
}
