package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTMachineRecipeCategory<T : HTMachineRecipeBase>(
    val guiHelper: IGuiHelper,
    val machine: HTMachineType,
    val arrowX: Double,
    val arrowY: Double = 0.0,
) : HTRecipeCategory<RecipeHolder<T>> {
    final override fun getTitle(): Component = machine.text

    final override fun getIcon(): IDrawable? = guiHelper.createDrawableItemLike(machine)

    final override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<T>, focuses: IFocusGroup) {
        setRecipe(builder, recipe.value)
    }

    protected abstract fun setRecipe(builder: IRecipeLayoutBuilder, recipe: T)

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<T>, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(arrowX), getPosition(arrowY))
    }

    final override fun getRegistryName(recipe: RecipeHolder<T>): ResourceLocation = recipe.id

    final override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<RecipeHolder<T>> =
        codecHelper.getRecipeHolderCodec()
}
