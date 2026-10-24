package hiiragi283.lib.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.lib.recipe.viewer.HTViewerRecipe
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.RecipeType

/**
 * [HTViewerRecipe]に基づいた[HTBasicRecipeCategory]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
abstract class HTViewerRecipeCategory<VIEWER : HTViewerRecipe>(
    recipeType: IRecipeType<VIEWER>,
    recipeType1: RecipeType<*>,
    icon: IDrawable,
    width: Int,
    height: Int,
    private val codec: Codec<VIEWER>
) : HTBasicRecipeCategory<VIEWER>(recipeType, recipeType1, icon, width, height) {
    final override fun getIdentifier(recipe: VIEWER): Identifier? = recipe.id

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<VIEWER> = codec

    override fun isHandled(recipe: VIEWER): Boolean = recipe.isHandled()
}
