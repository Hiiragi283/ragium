package hiiragi283.ragium.client.emi.recipe

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTMeltingEmiRecipe(holder: RecipeHolder<HTMeltingRecipe>) :
    HTHolderModularEmiRecipe<HTMeltingRecipe>(
        { recipe: HTMeltingRecipe, root: UIElement ->
            RagiumModularUIHelper.melter(
                root,
                inputSlot(recipe.ingredient),
                outputSlot(recipe.result),
            )
            root.addChild(RagiumModularUIHelper.fakeProgress(recipe.time))
        },
        RagiumEmiRecipeCategories.MELTING,
        holder,
    )
