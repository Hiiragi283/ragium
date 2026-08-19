package hiiragi283.ragium.client.integration.jei

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.integration.jei.HTJeiPlugin
import hiiragi283.lib.integration.jei.HTJeiRecipeHelper
import hiiragi283.lib.integration.jei.category.HTDoubleItemToItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemAndFluidToItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemOrFluidRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemToDoubleItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemToFluidRecipeCategory
import hiiragi283.lib.item.HTPotionBasedItem
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.client.integration.jei.category.RTElectrolyzingRecipeCategory
import hiiragi283.ragium.fluid.RagiumFluids
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IPlatformFluidHelper
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.registration.IExtraIngredientRegistration
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@JeiPlugin
class RagiumJeiPlugin : HTJeiPlugin(RagiumAPI.MOD_ID) {
    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        // Potion-Based Item
        HTPhysicalSideHelper
            .filteredLookup(Registries.ITEM)
            .getOrNull()
            ?.listElements()
            ?.map(Holder<Item>::value)
            ?.forEach { item: Item ->
                if (item is HTPotionBasedItem) {
                    registration.registerSubtypeInterpreter(item) { stack: ItemStack, _ -> HTPotionHelper.getContents(stack) }
                }
            }
    }

    override fun <T : Any> registerFluidSubtypes(registration: ISubtypeRegistration, platformFluidHelper: IPlatformFluidHelper<T>) {
        registration.registerSubtypeInterpreter(platformFluidHelper.fluidIngredientType, RagiumFluids.POTION.get()) { stack: T, _ -> (stack as? FluidStack)?.let(HTPotionHelper::getContents) }
    }

    override fun registerExtraIngredients(registration: IExtraIngredientRegistration) {
        HTPhysicalSideHelper
            .filteredLookup(Registries.POTION)
            .getOrNull()
            ?.listElements()
            ?.map(::BottledPotionContents)
            ?.filter { !it.isWater }
            ?.map { it.toFluidStack() }
            ?.toList()
            ?.let { registration.addExtraIngredients(NeoForgeTypes.FLUID_STACK, it) }
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper

        registration.addRecipeCategories(
            // Mechanical
            HTDoubleItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.ASSEMBLING),
            HTItemToDoubleItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CRUSHING),
            // Heat
            HTItemAndFluidToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.FREEZING),
            HTItemToFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.MELTING),
            // Chemical
            RTElectrolyzingRecipeCategory(guiHelper),
            // Bio
            HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.BREWING),
            // Electronics
            // Arcane
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        // Mechanical
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.ASSEMBLING, RagiumRecipeLookups.ASSEMBLING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.CRUSHING, RagiumRecipeLookups.CRUSHING)
        // Heat
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.FREEZING, RagiumRecipeLookups.FREEZING)
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.MELTING, RagiumRecipeLookups.MELTING)
        // Chemical
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.ELECTROLYZING, RagiumRecipeLookups.ELECTROLYZING)
        // Bio
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.BREWING, RagiumRecipeLookups.BREWING)
        // Electronics
        // Arcane
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
    }
}
