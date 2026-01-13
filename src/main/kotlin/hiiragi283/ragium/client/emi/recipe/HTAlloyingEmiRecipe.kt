package hiiragi283.ragium.client.emi.recipe

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTAlloyingEmiRecipe(holder: RecipeHolder<HTAlloyingRecipe>) :
    HTHolderModularEmiRecipe<HTAlloyingRecipe>(
        { recipe: HTAlloyingRecipe, root: UIElement ->
            RagiumModularUIHelper.alloySmelter(
                root,
                inputSlot(recipe.firstIngredient),
                inputSlot(recipe.secondIngredient),
                inputSlot(recipe.thirdIngredient),
                outputSlot(recipe.result),
            )
            root.addChild(RagiumModularUIHelper.fakeProgress(recipe.time))
        },
        RagiumEmiRecipeCategories.ALLOYING,
        holder,
    )
