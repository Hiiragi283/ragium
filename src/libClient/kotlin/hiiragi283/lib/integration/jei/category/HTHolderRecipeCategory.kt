package hiiragi283.lib.integration.jei.category

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.integration.jei.HTRecipeHolderType
import hiiragi283.lib.recipe.HTRecipeHolder
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.RecipeType

/**
 * [HTRecipeHolder]に基づいた[HTBasicRecipeCategory]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTHolderRecipeCategory<RECIPE : Any>(
    recipeType: HTRecipeHolderType<RECIPE>,
    recipeType1: RecipeType<*>,
    icon: IDrawable,
    width: Int,
    height: Int,
    private val codec: Codec<HTRecipeHolder<RECIPE>>
) : HTBasicRecipeCategory<HTRecipeHolder<RECIPE>>(recipeType, recipeType1, icon, width, height) {
    constructor(
        recipeType: HTRecipeHolderType<RECIPE>,
        recipeType1: RecipeType<*>,
        icon: IDrawable,
        width: Int,
        height: Int,
        codec: MapCodec<RECIPE>
    ) : this(
        recipeType,
        recipeType1,
        icon,
        width,
        height,
        HTRecipeHolder.codec(codec)
    )

    final override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTRecipeHolder<RECIPE>, focuses: IFocusGroup) {
        setupRecipe(builder, recipe.recipe, focuses)
    }

    protected abstract fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup)

    final override fun createRecipeExtras(
        builder: IRecipeExtrasBuilder,
        recipe: HTRecipeHolder<RECIPE>,
        focuses: IFocusGroup
    ) {
        setupRecipeExtras(builder, recipe.recipe, focuses)
    }

    protected open fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {}

    final override fun getIdentifier(recipe: HTRecipeHolder<RECIPE>): Identifier = recipe.id

    final override fun getCodec(
        codecHelper: ICodecHelper,
        recipeManager: IRecipeManager
    ): Codec<HTRecipeHolder<RECIPE>> = codec
}
