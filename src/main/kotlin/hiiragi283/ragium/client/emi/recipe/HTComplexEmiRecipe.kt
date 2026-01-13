package hiiragi283.ragium.client.emi.recipe

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.base.HTComplexResultRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTComplexEmiRecipe<RECIPE : HTComplexResultRecipe.Simple>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTHolderModularEmiRecipe<RECIPE>({ recipe: RECIPE, root: UIElement ->
        RagiumModularUIHelper.complex(
            root,
            inputSlot(recipe.getFluidIngredient()),
            inputSlot(recipe.getItemIngredient()),
            outputSlot(recipe.result.getLeft()),
            outputSlot(recipe.result.getRight()),
        )
        root.addChild(RagiumModularUIHelper.fakeProgress(recipe.time))
    }, category, holder) {
    companion object {
        @JvmStatic
        fun drying(holder: RecipeHolder<HTDryingRecipe>): HTComplexEmiRecipe<HTDryingRecipe> =
            HTComplexEmiRecipe(RagiumEmiRecipeCategories.DRYING, holder)

        @JvmStatic
        fun mixing(holder: RecipeHolder<HTMixingRecipe>): HTComplexEmiRecipe<HTMixingRecipe> =
            HTComplexEmiRecipe(RagiumEmiRecipeCategories.MIXING, holder)
    }
}
