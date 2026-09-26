package hiiragi283.ragium.client.integration.jei

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.integration.jei.HTJeiPlugin
import hiiragi283.lib.integration.jei.HTJeiRecipeHelper
import hiiragi283.lib.integration.jei.category.HTItemAndFluidToFluidRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemAndFluidToItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemToDoubleItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemToItemAndFluidRecipeCategory
import hiiragi283.lib.integration.jei.category.HTSingleRecipeCategory
import hiiragi283.lib.integration.jei.category.HTTripleItemToRecipeCategory
import hiiragi283.lib.integration.jei.ingredient.HTIngredientTypes
import hiiragi283.lib.item.HTPotionBasedItem
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.registry.asHolderSequence
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.data.oreSlurry.HTOreSlurryData
import hiiragi283.ragium.api.data.oreSlurry.HTOreSlurryDataHelper
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.ragium.client.integration.jei.category.RTElectrolyzingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.RTEnchantingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.RTReactingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.RTRefiningRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.RTResourceExtractingRecipeCategory
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
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.display.FluidTagSlotDisplay

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

        // Creative Tank
        registration.registerFromDataComponentTypes(RagiumBlocks.CREATIVE_TANK.asItem(), RagiumDataComponents.FLUID)
        // Ore Slurry Bucket
        registration.registerFromDataComponentTypes(
            RagiumFluids.ORE_SLURRY.bucketHolder.get(),
            RagiumDataComponents.ORE_SLURRY_DATA
        )
    }

    override fun <T : Any> registerFluidSubtypes(
        registration: ISubtypeRegistration,
        platformFluidHelper: IPlatformFluidHelper<T>
    ) {
        registration.registerSubtypeInterpreter(
            NeoForgeTypes.FLUID_STACK,
            RagiumFluids.POTION.getOrThrow()
        ) { stack: FluidStack, _ -> HTPotionHelper.getContents(stack) }
        registration.registerSubtypeInterpreter(
            NeoForgeTypes.FLUID_STACK,
            RagiumFluids.ORE_SLURRY.getOrThrow()
        ) { stack: FluidStack, _ -> HTOreSlurryDataHelper.getHolder(stack) }
    }

    override fun registerIngredients(registration: IModIngredientRegistration) {
        HTIngredientTypes.register(registration)
    }

    override fun registerExtraIngredients(registration: IExtraIngredientRegistration) {
        registration.addExtraIngredients(
            NeoForgeTypes.FLUID_STACK,
            listPotions()
                .filter { !it.`is`(Potions.WATER) }
                .map(HTPotionHelper::createFluid)
                .toList()
        )
        registration.addExtraIngredients(
            NeoForgeTypes.FLUID_STACK,
            HTPhysicalSideHelper.registryOrThrow(RagiumRegistries.Keys.ORE_SLURRY_DATA)
                .asHolderSequence()
                .map(HTOreSlurryDataHelper::createFluid)
                .toList()
        )
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper

        registration.addRecipeCategories(
            // Mechanical
            HTTripleItemToRecipeCategory.Basic(guiHelper, RagiumJeiRecipeTypes.ASSEMBLING),
            HTSingleRecipeCategory.ItemToItem(guiHelper, RagiumJeiRecipeTypes.COMPRESSING),
            HTItemToDoubleItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CRUSHING),
            HTItemToDoubleItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CUTTING),
            HTItemToItemAndFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.DRAINING),
            HTItemAndFluidToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.FILLING),
            // Heat
            HTTripleItemToRecipeCategory.Basic(guiHelper, RagiumJeiRecipeTypes.ALLOYING),
            HTSingleRecipeCategory.FluidToItem(guiHelper, RagiumJeiRecipeTypes.FREEZING),
            HTSingleRecipeCategory.ItemToFluid(guiHelper, RagiumJeiRecipeTypes.MELTING),
            HTItemToItemAndFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PYROLYZING),
            RTRefiningRecipeCategory(guiHelper),
            // Chemical
            HTItemAndFluidToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.BATHING),
            RTElectrolyzingRecipeCategory(guiHelper),
            HTItemAndFluidToFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.MIXING),
            RTReactingRecipeCategory(guiHelper),
            // Bio
            HTItemAndFluidToFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.BREWING),
            HTItemToDoubleItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PLANTING),
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
        HTJeiRecipeHelper.addRecipes(
            registration,
            RagiumJeiRecipeTypes.ELECTROLYZING,
            RagiumRecipeLookups.ELECTROLYZING
        )
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.MIXING, RagiumRecipeLookups.MIXING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.REACTING, RagiumRecipeLookups.REACTING)
        // Bio
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.BREWING, RagiumRecipeLookups.BREWING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.PLANTING, RagiumRecipeLookups.PLANTING)
        // Electronics
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
                                bottleType.filledItem.toStack(patch = HTPotionHelper.createPotionPatch(potion))
                            )
                            itemResult { +bottleType.emptyItem }
                            fluidResult { from(HTPotionHelper.createFluid(potion, HTPotionHelper.BOTTLE_AMOUNT)) }
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
                                from(bottleType.filledItem.toStack(patch = HTPotionHelper.createPotionPatch(potion)))
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
        registration.addRecipes(
            RagiumJeiRecipeTypes.MIXING,
            listItems()
                .mapNotNull { input: Holder<Item> ->
                    val result: FluidStack =
                        HTOreSlurryDataHelper.createFluid(input.components()) ?: return@mapNotNull null
                    RagiumRecipeBuilders.mixing {
                        itemIngredient = HTJeiRecipeHelper.fakeItem(SlotDisplay.ItemSlotDisplay(input))
                        fluidIngredient = HTJeiRecipeHelper.fakeFluid(RagiumFluids.SULFURIC_ACID)
                        result { from(result) }
                        recipeId replace input.getKeyOrThrow().identifier().withPrefix("solving/")
                    }.buildSynthetic()
                }.toList()
        )

        registration.addRecipes(
            RagiumJeiRecipeTypes.REACTING,
            HTPhysicalSideHelper.registryOrThrow(RagiumRegistries.Keys.ORE_SLURRY_DATA)
                .asHolderSequence()
                .map { holder: Holder<HTOreSlurryData> ->
                    RagiumRecipeBuilders.reacting {
                        primaryIngredient = HTJeiRecipeHelper.fakeFluid(HTOreSlurryDataHelper.createFluid(holder))
                        secondaryIngredient = HTJeiRecipeHelper.fakeFluid(FluidTagSlotDisplay(Tags.Fluids.WATER))
                        +holder.value().result
                        recipeId replace holder.getKeyOrThrow().identifier().withPrefix("ore_slurry/")
                    }.buildSynthetic()
                }.toList()
        )
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
        registration.addCraftingStation(RagiumJeiRecipeTypes.ELECTROLYZING, RagiumBlocks.ELECTROLYZER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.MIXING, RagiumBlocks.MIXER)
        // Bio
        registration.addCraftingStation(RagiumJeiRecipeTypes.BREWING, RagiumBlocks.BREWERY)
        registration.addCraftingStation(RagiumJeiRecipeTypes.PLANTING, RagiumBlocks.PLANTER)
        // Electronics
        registration.addCraftingStation(RagiumJeiRecipeTypes.ASSEMBLING, RagiumBlocks.PRECISION_ASSEMBLER)
        // Arcane
        registration.addCraftingStation(RagiumJeiRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER)
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        registration.addGuiContainerHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
        registration.addGhostIngredientHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
    }
}
