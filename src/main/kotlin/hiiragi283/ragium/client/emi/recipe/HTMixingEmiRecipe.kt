package hiiragi283.ragium.client.emi.recipe

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTMixingEmiRecipe(holder: RecipeHolder<HTMixingRecipe>) :
    HTHolderModularEmiRecipe<HTMixingRecipe>({ recipe: HTMixingRecipe, root: UIElement ->
        RagiumModularUIHelper.mixer(
            root,
            inputSlot(recipe.itemIngredients.getOrNull(0)),
            inputSlot(recipe.itemIngredients.getOrNull(1)),
            inputSlot(recipe.fluidIngredients.getOrNull(0)),
            inputSlot(recipe.fluidIngredients.getOrNull(1)),
            outputSlot(recipe.result.getLeft()),
            outputSlot(recipe.result.getRight()),
        )
        root.addChild(RagiumModularUIHelper.fakeProgress(recipe.time))
    }, RagiumEmiRecipeCategories.MIXING, holder)
