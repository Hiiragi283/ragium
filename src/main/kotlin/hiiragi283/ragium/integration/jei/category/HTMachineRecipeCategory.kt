package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import net.minecraft.world.level.ItemLike

abstract class HTMachineRecipeCategory<R : HTMachineRecipe>(
    guiHelper: IGuiHelper,
    icon: ItemLike,
    arrowX: Double,
    arrowY: Double = 0.0,
) : HTRecipeHolderCategory<R>(guiHelper, icon, arrowX, arrowY) {
    final override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: R) {
        recipe.getDefinition().ifSuccess { setRecipe(builder, it) }
    }

    protected abstract fun setRecipe(builder: IRecipeLayoutBuilder, definition: HTRecipeDefinition)
}
