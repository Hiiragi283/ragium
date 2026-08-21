package hiiragi283.ragium.client.integration.jei

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.integration.jei.HTJeiPlugin
import hiiragi283.lib.integration.jei.HTJeiRecipeHelper
import hiiragi283.lib.integration.jei.category.HTDoubleItemToItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemAndFluidToFluidRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemAndFluidToItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemToDoubleItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemToFluidRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemToItemAndFluidRecipeCategory
import hiiragi283.lib.item.HTPotionBasedItem
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.ragium.client.integration.jei.category.RTElectrolyzingRecipeCategory
import hiiragi283.ragium.fluid.RagiumFluids
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
import net.neoforged.neoforge.fluids.FluidStack

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
                    registration.registerSubtypeInterpreter(item) { stack: ItemStack, _ -> HTPotionHelper.getContents(stack) }
                }
            }
    }

    override fun <T : Any> registerFluidSubtypes(registration: ISubtypeRegistration, platformFluidHelper: IPlatformFluidHelper<T>) {
        registration.registerSubtypeInterpreter(platformFluidHelper.fluidIngredientType, RagiumFluids.POTION.get()) { stack: T, _ -> (stack as? FluidStack)?.let(HTPotionHelper::getContents) }
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
                .toList(),
        )
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper

        registration.addRecipeCategories(
            // Mechanical
            HTDoubleItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.ASSEMBLING),
            HTItemToDoubleItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CRUSHING),
            HTItemToDoubleItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CUTTING),
            // Heat
            HTItemAndFluidToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.FREEZING),
            HTItemToFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.MELTING),
            HTItemToItemAndFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PYROLYZING),
            // Chemical
            HTItemAndFluidToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.BATHING),
            RTElectrolyzingRecipeCategory(guiHelper),
            // Bio
            HTItemAndFluidToFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.BREWING),
            // Electronics
            // Arcane
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        // Mechanical
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.ASSEMBLING, RagiumRecipeLookups.ASSEMBLING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.CRUSHING, RagiumRecipeLookups.CRUSHING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.CUTTING, RagiumRecipeLookups.CUTTING)
        // Heat
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.FREEZING, RagiumRecipeLookups.FREEZING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.MELTING, RagiumRecipeLookups.MELTING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.PYROLYZING, RagiumRecipeLookups.PYROLYZING)
        // Chemical
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.BATHING, RagiumRecipeLookups.BATHING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.ELECTROLYZING, RagiumRecipeLookups.ELECTROLYZING)
        // Bio
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.BREWING, RagiumRecipeLookups.BREWING)
        // Electronics
        // Arcane
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        registration.addGuiContainerHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
        registration.addGhostIngredientHandler(HTWidgetContainerScreen::class.java, HTWidgetContainerJeiHandler)
    }
}
