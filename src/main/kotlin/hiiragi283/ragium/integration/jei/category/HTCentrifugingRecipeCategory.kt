package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.recipe.HTCentrifugingRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder

class HTCentrifugingRecipeCategory(guiHelper: IGuiHelper) :
    HTRecipeHolderCategory<HTCentrifugingRecipe>(guiHelper, RagiumBlocks.CRUSHER, 1.5, 1.0) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTCentrifugingRecipe) {
    }

    override fun getRecipeType(): RecipeType<RecipeHolder<HTCentrifugingRecipe>> = RagiumJEIRecipeTypes.CENTRIFUGING

    override fun getWidth(): Int = 18 * 6 + 8

    override fun getHeight(): Int = 18 * 3 + 8
}
