package hiiragi283.ragium.client.emi.recipe

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTPyrolyzingEmiRecipe(holder: RecipeHolder<HTPyrolyzingRecipe>) :
    HTHolderModularEmiRecipe<HTPyrolyzingRecipe>({ recipe: HTPyrolyzingRecipe, root: UIElement ->
        RagiumModularUIHelper.pyrolyzer(
            root,
            inputSlot(recipe.ingredient),
            outputSlot(recipe.itemResult),
            outputSlot(recipe.fluidResult),
        )
        root.addChild(RagiumModularUIHelper.fakeProgress(recipe.time))
    }, RagiumEmiRecipeCategories.PYROLYZING, holder)
