package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.ItemLike

abstract class HTRecipeHolderCategory<R : Recipe<*>>(
    guiHelper: IGuiHelper,
    private val icon: ItemLike,
    private val arrowX: Double,
    private val arrowY: Double = 0.0,
) : HTRecipeCategory<RecipeHolder<R>>(guiHelper) {
    final override fun getTitle(): Component = ItemStack(icon).hoverName

    final override fun getIcon(): IDrawable? = guiHelper.createDrawableItemLike(icon)

    final override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<R>, focuses: IFocusGroup) {
        setRecipe(builder, recipe.value)
    }

    protected abstract fun setRecipe(builder: IRecipeLayoutBuilder, recipe: R)

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<R>, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(arrowX), getPosition(arrowY))
    }

    final override fun getRegistryName(recipe: RecipeHolder<R>): ResourceLocation = recipe.id

    final override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<RecipeHolder<R>> =
        codecHelper.getRecipeHolderCodec()
}
