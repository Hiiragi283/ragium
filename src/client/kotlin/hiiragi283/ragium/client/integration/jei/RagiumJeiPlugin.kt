package hiiragi283.ragium.client.integration.jei

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.integration.jei.HTJeiPlugin
import hiiragi283.lib.integration.jei.HTJeiRecipeHelper
import hiiragi283.lib.integration.jei.category.HTDoubleItemToItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTFluidToItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemAndFluidToFluidRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemAndFluidToItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemToDoubleItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemToFluidRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemToItemAndFluidRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemToItemRecipeCategory
import hiiragi283.lib.item.HTPotionBasedItem
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.data.recipe.HTOreSlurryData
import hiiragi283.ragium.api.data.recipe.HTOreSlurryDataHelper
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.ragium.client.integration.jei.category.RTElectrolyzingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.RTReactingRecipeCategory
import hiiragi283.ragium.client.integration.jei.category.RTRefiningRecipeCategory
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IPlatformFluidHelper
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.registration.IExtraIngredientRegistration
import mezz.jei.api.registration.IGuiHandlerRegistration
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.display.FluidTagSlotDisplay
import kotlin.streams.asSequence

@JeiPlugin
class RagiumJeiPlugin : HTJeiPlugin(RagiumAPI.MOD_ID) {
    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        // Potion-Based Item
        HTPhysicalSideHelper
            .filteredLookup(BuiltInRegistries.ITEM)
            .listElements()
            .map(Holder<Item>::value)
            .forEach { item: Item ->
                if (item is HTPotionBasedItem) {
                    registration.registerSubtypeInterpreter(item) { stack: ItemStack, _ ->
                        HTPotionHelper.getContents(stack)
                    }
                }
            }
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

    override fun registerExtraIngredients(registration: IExtraIngredientRegistration) {
        registration.addExtraIngredients(
            NeoForgeTypes.FLUID_STACK,
            HTPhysicalSideHelper
                .filteredLookup(BuiltInRegistries.POTION)
                .listElements()
                .map(::BottledPotionContents)
                .filter { !it.isWater }
                .map(BottledPotionContents::toFluidStack)
                .toList()
        )
        registration.addExtraIngredients(
            NeoForgeTypes.FLUID_STACK,
            HTPhysicalSideHelper.registryOrThrow(RagiumRegistries.Keys.ORE_SLURRY_DATA)
                .listElements()
                .map(HTOreSlurryDataHelper::createFluid)
                .toList()
        )
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper

        registration.addRecipeCategories(
            // Mechanical
            HTDoubleItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.ASSEMBLING),
            HTItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.COMPRESSING),
            HTItemToDoubleItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CRUSHING),
            HTItemToDoubleItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CUTTING),
            HTItemToItemAndFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.DRAINING),
            HTItemAndFluidToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.FILLING),
            // Heat
            HTDoubleItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.ALLOYING),
            HTFluidToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.FREEZING),
            HTItemToFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.MELTING),
            HTItemToItemAndFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PYROLYZING),
            RTRefiningRecipeCategory(guiHelper),
            // Chemical
            HTItemAndFluidToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.BATHING),
            RTElectrolyzingRecipeCategory(guiHelper),
            HTItemAndFluidToFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.MIXING),
            RTReactingRecipeCategory(guiHelper),
            // Bio
            HTItemAndFluidToFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.BREWING),
            HTItemToDoubleItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PLANTING)
            // Electronics
            // Arcane
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
        // Arcane

        registerDynamicRecipes(registration)
    }

    private fun registerDynamicRecipes(registration: IRecipeRegistration) {
        // Chemical
        registration.addRecipes(
            RagiumJeiRecipeTypes.MIXING,
            HTPhysicalSideHelper.filteredLookup(BuiltInRegistries.ITEM)
                .listElements()
                .asSequence()
                .mapNotNull { input: Holder<Item> ->
                    val slurryData: Holder<HTOreSlurryData> =
                        input.components().get(RagiumDataComponents.ORE_SLURRY_DATA) ?: return@mapNotNull null
                    RagiumRecipeBuilders.mixing {
                        itemIngredient = HTJeiRecipeHelper.fakeItem(SlotDisplay.ItemSlotDisplay(input))
                        fluidIngredient = HTJeiRecipeHelper.fakeFluid(RagiumFluids.SULFURIC_ACID)
                        result { from(HTOreSlurryDataHelper.createFluid(slurryData)) }
                        recipeId replace input.getKeyOrThrow().identifier().withPrefix("solving/")
                    }.buildSynthetic()
                }.toList()
        )

        registration.addRecipes(
            RagiumJeiRecipeTypes.REACTING,
            HTPhysicalSideHelper.registryOrThrow(RagiumRegistries.Keys.ORE_SLURRY_DATA)
                .listElements()
                .asSequence()
                .mapNotNull { holder: Holder<HTOreSlurryData> ->
                    val template: ItemStackTemplate = holder.value().createTemplate() ?: return@mapNotNull null
                    RagiumRecipeBuilders.reacting {
                        primaryIngredient = HTJeiRecipeHelper.fakeFluid(HTOreSlurryDataHelper.createFluid(holder))
                        secondaryIngredient = HTJeiRecipeHelper.fakeFluid(FluidTagSlotDisplay(Tags.Fluids.WATER))
                        itemResult { from(template) }
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
        registration.addCraftingStation(RagiumJeiRecipeTypes.FREEZING, RagiumBlocks.FREEZER)
        registration.addCraftingStation(RagiumJeiRecipeTypes.MELTING, RagiumBlocks.MELTER)
        // Chemical
        registration.addCraftingStation(RagiumJeiRecipeTypes.BATHING, RagiumBlocks.CHEMICAL_BATH)
        // Bio
        registration.addCraftingStation(RagiumJeiRecipeTypes.BREWING, RagiumBlocks.BREWERY)
        // Electronics
        // Arcane
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        registration.addGuiContainerHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
        registration.addGhostIngredientHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
    }
}
