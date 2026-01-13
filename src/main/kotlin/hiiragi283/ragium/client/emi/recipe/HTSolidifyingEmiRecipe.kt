package hiiragi283.ragium.client.emi.recipe

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTSolidifyingEmiRecipe(holder: RecipeHolder<HTSolidifyingRecipe>) :
    HTHolderModularEmiRecipe<HTSolidifyingRecipe>({ recipe: HTSolidifyingRecipe, root: UIElement ->
        RagiumModularUIHelper.singleCatalyst(
            root,
            inputSlot(recipe.ingredient),
            catalystSlot(recipe.catalyst),
            outputSlot(recipe.result),
        )
        root.addChild(RagiumModularUIHelper.fakeProgress(recipe.time))
    }, RagiumEmiRecipeCategories.SOLIDIFYING, holder)
