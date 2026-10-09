package hiiragi283.ragium.client.integration.jei

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.integration.jei.HTJeiDrawables
import hiiragi283.lib.integration.jei.HTJeiPlugin
import hiiragi283.lib.integration.jei.HTJeiRecipeHelper
import hiiragi283.lib.integration.jei.category.HTItemAndFluidToFluidRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemAndFluidToItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemToDoubleItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTSingleRecipeCategory
import hiiragi283.lib.integration.jei.category.HTSingleToItemAndFluidRecipeCategory
import hiiragi283.lib.integration.jei.category.HTTripleItemToRecipeCategory
import hiiragi283.lib.integration.jei.ingredient.HTIngredientTypes
import hiiragi283.lib.item.HTPotionBasedItem
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.registry.asHolderSequence
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.ragium.client.integration.jei.category.RTElectrolyzingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.RTEnchantingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.RTFreezingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.RTMixingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.RTReactingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.RTResourceExtractingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.RTWashingRecipeCategory
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.recipe.RTPotionBottleDrainingRecipe
import hiiragi283.ragium.common.recipe.RTPotionBottleFillingRecipe
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IPlatformFluidHelper
import mezz.jei.api.neoforge.NeoForgeTypes
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
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.fluids.FluidStack

@JeiPlugin
class RagiumJeiPlugin : HTJeiPlugin(RagiumAPI.MOD_ID) {
    companion object {
        @JvmStatic
        private fun listItems(): Sequence<Holder.Reference<Item>> = HTPhysicalSideHelper
            .filteredLookup(BuiltInRegistries.ITEM)
            .asHolderSequence()

        @JvmStatic
        private fun listPotions(): Sequence<Holder.Reference<Potion>> = HTPhysicalSideHelper
            .filteredLookup(BuiltInRegistries.POTION)
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

    override fun <T : Any> registerFluidSubtypes(
        registration: ISubtypeRegistration,
        platformFluidHelper: IPlatformFluidHelper<T>
    ) {
        registration.registerSubtypeInterpreter(
            NeoForgeTypes.FLUID_STACK,
            RagiumFluids.POTION.getOrThrow()
        ) { stack: FluidStack, _ -> HTPotionHelper.getContentsNotEmpty(stack) }
    }

    override fun registerIngredients(registration: IModIngredientRegistration) {
        HTIngredientTypes.register(registration)
    }

    override fun registerExtraIngredients(registration: IExtraIngredientRegistration) {
        registration.addExtraIngredients(
            NeoForgeTypes.FLUID_STACK,
            listPotions()
                .filter { !it.`is`(Potions.WATER) }
                .mapNotNull { HTPotionHelper.createFluid(it).getOrNull()?.create() }
                .toList()
        )
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper
        HTJeiDrawables.init(guiHelper)

        registration.addRecipeCategories(
            // Mechanical
            HTTripleItemToRecipeCategory.Basic(
                RagiumJeiRecipeTypes.ASSEMBLING,
                RagiumRecipeTypes.ASSEMBLING,
                guiHelper.createDrawableItemLike(RagiumBlocks.ASSEMBLER)
            ),
            HTSingleRecipeCategory.ItemToItem(
                RagiumJeiRecipeTypes.COMPRESSING,
                RagiumRecipeTypes.COMPRESSING,
                guiHelper.createDrawableItemLike(RagiumBlocks.COMPRESSOR)
            ),
            HTItemToDoubleItemRecipeCategory(
                RagiumJeiRecipeTypes.CRUSHING,
                RagiumRecipeTypes.CRUSHING,
                guiHelper.createDrawableItemLike(RagiumBlocks.CRUSHER)
            ),
            HTItemToDoubleItemRecipeCategory(
                RagiumJeiRecipeTypes.CUTTING,
                RagiumRecipeTypes.CUTTING,
                guiHelper.createDrawableItemLike(RagiumBlocks.CUTTING_MACHINE)
            ),
            HTSingleToItemAndFluidRecipeCategory.ItemTo(
                RagiumJeiRecipeTypes.DRAINING,
                RagiumRecipeTypes.DRAINING,
                guiHelper.createDrawableItemLike(RagiumBlocks.MACHINE_CASING)
            ),
            HTItemAndFluidToItemRecipeCategory(
                RagiumJeiRecipeTypes.FILLING,
                RagiumRecipeTypes.FILLING,
                guiHelper.createDrawableItemLike(RagiumBlocks.MACHINE_CASING)
            ),
            // Heat
            HTTripleItemToRecipeCategory.Basic(
                RagiumJeiRecipeTypes.ALLOYING,
                RagiumRecipeTypes.ALLOYING,
                guiHelper.createDrawableItemLike(RagiumBlocks.ALLOY_SMELTER)
            ),
            RTFreezingRecipeCategory(guiHelper),
            HTSingleRecipeCategory.ItemToFluid(
                RagiumJeiRecipeTypes.MELTING,
                RagiumRecipeTypes.MELTING,
                guiHelper.createDrawableItemLike(RagiumBlocks.MELTER)
            ),
            HTSingleToItemAndFluidRecipeCategory.ItemTo(
                RagiumJeiRecipeTypes.PYROLYZING,
                RagiumRecipeTypes.PYROLYZING,
                guiHelper.createDrawableItemLike(RagiumBlocks.PYROLYZER)
            ),
            HTSingleToItemAndFluidRecipeCategory.FluidTo(
                RagiumJeiRecipeTypes.REFINING,
                RagiumRecipeTypes.REFINING,
                guiHelper.createDrawableItemLike(RagiumBlocks.REFINERY)
            ),
            // Chemical
            HTItemAndFluidToItemRecipeCategory(
                RagiumJeiRecipeTypes.BATHING,
                RagiumRecipeTypes.BATHING,
                guiHelper.createDrawableItemLike(RagiumBlocks.CHEMICAL_BATH)
            ),
            RTElectrolyzingRecipeCategory(guiHelper),
            RTMixingRecipeCategory(guiHelper),
            RTReactingRecipeCategory(guiHelper),
            RTWashingRecipeCategory(guiHelper),
            // Bio
            HTItemAndFluidToFluidRecipeCategory(
                RagiumJeiRecipeTypes.BREWING,
                RagiumRecipeTypes.BREWING,
                guiHelper.createDrawableItemLike(RagiumBlocks.BREWERY)
            ),
            HTItemToDoubleItemRecipeCategory(
                RagiumJeiRecipeTypes.PLANTING,
                RagiumRecipeTypes.PLANTING,
                guiHelper.createDrawableItemLike(RagiumBlocks.PLANTER)
            ),
            // Electronics
            RTResourceExtractingRecipeCategory(guiHelper),
            // Arcane
            RTEnchantingRecipeCategory(guiHelper)
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        // Mechanical
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.ASSEMBLING, RagiumRecipeLookups.ASSEMBLING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.COMPRESSING, RagiumRecipeLookups.COMPRESSING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.CRUSHING, RagiumRecipeLookups.CRUSHING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.CUTTING, RagiumRecipeLookups.CUTTING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.DRAINING, RagiumRecipeLookups.DRAINING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.FILLING, RagiumRecipeLookups.FILLING)
        // Heat
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.ALLOYING, RagiumRecipeLookups.ALLOYING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.FREEZING, RagiumRecipeLookups.FREEZING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.MELTING, RagiumRecipeLookups.MELTING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.PYROLYZING, RagiumRecipeLookups.PYROLYZING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.REFINING, RagiumRecipeLookups.REFINING)
        // Chemical
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.BATHING, RagiumRecipeLookups.BATHING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.MIXING, RagiumRecipeLookups.MIXING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.REACTING, RagiumRecipeLookups.REACTING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.WASHING, RagiumRecipeLookups.WASHING)
        // Bio
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.BREWING, RagiumRecipeLookups.BREWING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.PLANTING, RagiumRecipeLookups.PLANTING)
        // Electronics
        HTJeiRecipeHelper.addRecipes(
            registration,
            RagiumJeiRecipeTypes.ELECTROLYZING,
            RagiumRecipeLookups.ELECTROLYZING
        )
        HTJeiRecipeHelper.addRecipes(
            registration,
            RagiumJeiRecipeTypes.RESOURCE_EXTRACTING,
            RagiumRecipeLookups.RESOURCE_EXTRACTING
        )
        // Arcane
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.ENCHANTING, RagiumRecipeLookups.ENCHANTING)

        registerDynamicRecipes(registration)
    }

    private fun registerDynamicRecipes(registration: IRecipeRegistration) {
        // Mechanical
        registration.addRecipes(
            RagiumJeiRecipeTypes.DRAINING,
            listPotions()
                .flatMap { potion: Holder.Reference<Potion> ->
                    HTBottleType.entries.map { bottleType: HTBottleType ->
                        RagiumRecipeBuilders.draining {
                            +HTJeiRecipeHelper.fakeItem(
                                bottleType.filledItem.asStackOrEmpty(patch = HTPotionHelper.createPotionPatch(potion))
                            )
                            itemResult { +bottleType.emptyItem }
                            fluidResult {
                                from(HTPotionHelper.createFluid(potion, HTPotionHelper.BOTTLE_AMOUNT).getOrThrow())
                            }
                            progressData = RTPotionBottleDrainingRecipe.PROGRESS_DATA
                            recipeId replace potion.getKeyOrThrow()
                                .identifier()
                                .withPrefix("potion_bottle/${bottleType.serializedName}/")
                        }.buildSynthetic()
                    }
                }.toList()
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
                }.toList()
        )
        // Chemical
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        // Mechanical
        registration.addCraftingStation(RagiumJeiRecipeTypes.ASSEMBLING, RagiumBlocks.ASSEMBLER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.CRUSHING, RagiumBlocks.CRUSHER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.COMPRESSING, RagiumBlocks.COMPRESSOR)
        registration.addCraftingStation(RagiumJeiRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE)
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
        registration.addCraftingStation(RagiumJeiRecipeTypes.REACTING, RagiumBlocks.CHEMICAL_REACTOR)
        registration.addCraftingStation(RagiumJeiRecipeTypes.MIXING, RagiumBlocks.MIXER)
        // Bio
        registration.addCraftingStation(RagiumJeiRecipeTypes.BREWING, RagiumBlocks.BREWERY)
        registration.addCraftingStation(RagiumJeiRecipeTypes.PLANTING, RagiumBlocks.PLANTER)
        // Electronics
        registration.addCraftingStation(RagiumJeiRecipeTypes.ELECTROLYZING, RagiumBlocks.ELECTROLYZER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.ASSEMBLING, RagiumBlocks.PRECISION_ASSEMBLER)
        // Arcane
        registration.addCraftingStation(RagiumJeiRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER)
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        registration.addGuiContainerHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
        registration.addGhostIngredientHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
    }
}
