package hiiragi283.ragium.integration.jei

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.integration.jei.HTJeiPlugin
import hiiragi283.lib.integration.jei.HTJeiRecipeHelper
import hiiragi283.lib.integration.jei.category.HTItemToDoubleItemRecipeCategory
import hiiragi283.lib.integration.jei.category.HTItemToFluidRecipeCategory
import hiiragi283.lib.item.HTPotionBasedItem
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

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

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper

        registration.addRecipeCategories(
            // Mechanical
            HTItemToDoubleItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CRUSHING),
            // Heat
            HTItemToFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.MELTING),
            // Chemical
            // Bio
            // Electronics
            // Arcane
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        // Mechanical
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.CRUSHING, RagiumRecipeLookups.CRUSHING)
        // Heat
        HTJeiRecipeHelper.addRecipes(registration, RagiumJeiRecipeTypes.MELTING, RagiumRecipeLookups.MELTING)
        // Chemical
        // Bio
        // Electronics
        // Arcane
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
    }
}
