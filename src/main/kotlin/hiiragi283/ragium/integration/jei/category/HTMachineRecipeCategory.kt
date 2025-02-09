package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.network.chat.Component

abstract class HTMachineRecipeCategory<T : HTMachineRecipeBase>(val guiHelper: IGuiHelper, val key: HTMachineKey, val arrowX: Double) :
    HTRecipeCategory<T> {
    final override fun getTitle(): Component = key.text

    final override fun getIcon(): IDrawable? = guiHelper.createDrawableItemLike(key.getBlock())

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: T, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(arrowX), getPosition(0))
    }
}
