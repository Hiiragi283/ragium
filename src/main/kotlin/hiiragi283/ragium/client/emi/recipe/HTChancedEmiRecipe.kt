package hiiragi283.ragium.client.emi.recipe

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.base.HTChancedRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTChancedEmiRecipe<RECIPE : HTChancedRecipe>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTHolderModularEmiRecipe<RECIPE>({ recipe: RECIPE, root: UIElement ->
        RagiumModularUIHelper.chanced(
            root,
            inputSlot(recipe.ingredient),
            outputSlot(recipe.result),
            recipe.extraResults.map(::outputSlot),
        )
        root.addChild(RagiumModularUIHelper.fakeProgress(recipe.time))
    }, category, holder) {
    companion object {
        @JvmStatic
        fun crushing(holder: RecipeHolder<HTCrushingRecipe>): HTChancedEmiRecipe<HTCrushingRecipe> =
            HTChancedEmiRecipe(RagiumEmiRecipeCategories.CRUSHING, holder)

        @JvmStatic
        fun cutting(holder: RecipeHolder<HTCuttingRecipe>): HTChancedEmiRecipe<HTCuttingRecipe> =
            HTChancedEmiRecipe(RagiumEmiRecipeCategories.CUTTING, holder)
    }
}
