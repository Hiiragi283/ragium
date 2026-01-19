package hiiragi283.ragium.client.emi.recipe

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTRefiningEmiRecipe(holder: RecipeHolder<HTRefiningRecipe>) :
    HTHolderModularEmiRecipe<HTRefiningRecipe>({ recipe: HTRefiningRecipe, root: UIElement ->
        RagiumModularUIHelper.chanced(
            root,
            inputSlot(recipe.ingredient),
            outputSlot(recipe.extraResult.getLeft()),
            listOf(outputSlot(recipe.result), outputSlot(recipe.extraResult.getRight())),
        )
        root.addChild(RagiumModularUIHelper.fakeProgress(recipe.time))
    }, RagiumEmiRecipeCategories.REFINING, holder)
